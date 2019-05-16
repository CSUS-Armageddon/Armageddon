package a3.network.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

import a3.MyGame;
import a3.avatar.Avatars;
import a3.network.api.Position;
import a3.network.api.Rotation;
import a3.network.api.messages.Message;
import a3.network.api.messages.impl.CreateMessage;
import a3.network.api.messages.impl.DetailsMessage;
import a3.network.api.messages.impl.HangupMessage;
import a3.network.api.messages.impl.JoinMessage;
import a3.network.api.messages.impl.MoveMessage;
import a3.network.api.messages.impl.RequestMessage;
import a3.network.api.messages.impl.RotateMessage;
import a3.network.logging.ClientLogger;
import ray.networking.client.GameConnectionClient;
import ray.networking.client.IClientSocket;
import ray.rage.asset.texture.Texture;
import ray.rage.rendersystem.Renderable.Primitive;
import ray.rage.rendersystem.states.RenderState;
import ray.rage.rendersystem.states.TextureState;
import ray.rage.scene.SceneNode;
import ray.rage.scene.SkeletalEntity;
import ray.rml.Matrix3;
import ray.rml.Vector3;

public class GameClient extends GameConnectionClient implements Client {
	
	private final MyGame game;
	private final String clientName;
	private final UUID uuid;
	private Vector<GhostAvatar> ghostAvatars;
	
	private String remoteName;
	private final InetAddress remoteAddress;
	private final int remotePort;
	private final ProtocolType protocolType;
	
	public GameClient(InetAddress remoteAddress, int remotePort, ProtocolType protocolType, MyGame game, String clientName) throws IOException {
		super(remoteAddress, remotePort, protocolType);
		this.game = game;
		this.uuid = UUID.randomUUID();
		this.ghostAvatars = new Vector<GhostAvatar>();
		this.clientName = clientName;
		this.remoteAddress = remoteAddress;
		this.remotePort = remotePort;
		this.protocolType = protocolType;
	}
	
	@Override
	protected void processPacket(Object obj) {
		try {
			final Message msg = (Message)obj;
			ClientLogger.INSTANCE.logln(msg.toString());
			switch (msg.getMessageType()) {
			case JOIN:
				handleJoinMessage((JoinMessage)msg);
				break;
			case CREATE:
				handleCreateMessage((CreateMessage)msg);
				break;
			case MOVE:
				handleMoveMessage((MoveMessage)msg);
				break;
			case ROTATE:
				handleRotateMessage((RotateMessage)msg);
				break;
			case REQUEST:
				handleRequestMessage((RequestMessage)msg);
				break;
			case DETAILS:
				handleDetailsMessage((DetailsMessage)msg);
				break;
			case HANGUP:
				handleHangupMessage((HangupMessage)msg);
				break;
			default:
				System.out.println("Unknown Message Type!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see a3.network.client.Client#sendJoinMessage()
	 */
	@Override
	public void sendJoinMessage() {
		try {
			final JoinMessage jm = new JoinMessage();
			initMessage(jm);
			sendPacket(jm);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void handleJoinMessage(JoinMessage jm) {
		if (jm.isJoinSuccess()) {
			game.setClientConnected(true);
			this.remoteName = jm.getFromName();
			System.out.println("Join Success");
			sendCreateMessage(game.getPlayerPosition(), game.getPlayerRotation(), game.getAvatar().getAvatarName());
		} else {
			game.setClientConnected(false);
			System.out.println("Join Failure");
		}
	}

	@Override
	public void sendCreateMessage(Vector3 playerPosition, Matrix3 playerRotation, String avatarName) {
		try {
			final CreateMessage cm = new CreateMessage();
			initMessage(cm);
			cm.setPosition(Position.fromVector3(playerPosition));
			cm.setRotation(Rotation.fromMatrix3(playerRotation));
			cm.setAvatar(Avatars.fromAvatarName(avatarName));
			sendPacket(cm);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleCreateMessage(CreateMessage cm) {
		final GhostAvatar avatar = new GhostAvatar(cm.getUUID(), cm.getPosition().toVector3(), cm.getRotation().toMatrix3(), cm.getAvatar());
		try {
			game.addGhostAvatar(avatar);
			ghostAvatars.add(avatar);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendMoveMessage(Vector3 localPosition) {
		try {
			final MoveMessage mm = new MoveMessage();
			initMessage(mm);
			mm.setPosition(Position.fromVector3(localPosition));
			sendPacket(mm);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleMoveMessage(MoveMessage mm) {
		final GhostAvatar avatar = findGhostAvatarByUUID(mm.getUUID());
		if (avatar != null) {
			avatar.getNode().setLocalPosition(mm.getPosition().toVector3());
		}
	}

	@Override
	public void sendRotateMessage(Matrix3 localRotation) {
		try {
			final RotateMessage rm = new RotateMessage();
			initMessage(rm);
			rm.setRotation(Rotation.fromMatrix3(localRotation));
			sendPacket(rm);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleRotateMessage(RotateMessage rm) {
		final GhostAvatar avatar = findGhostAvatarByUUID(rm.getUUID());
		if (avatar != null) {
			avatar.getNode().setLocalRotation(rm.getRotation().toMatrix3());
		}
	}
	
	@Override
	public void handleRequestMessage(RequestMessage rm) {
		sendDetailsMessage(game.getPlayerPosition(), game.getPlayerRotation(), game.getAvatar().getAvatarName());
	}

	@Override
	public void sendDetailsMessage(Vector3 localPosition, Matrix3 localRotation, String avatarName) {
		try {
			final DetailsMessage dm = new DetailsMessage();
			initMessage(dm);
			dm.setPosition(Position.fromVector3(localPosition));
			dm.setRotation(Rotation.fromMatrix3(localRotation));
			dm.setAvatar(Avatars.fromAvatarName(avatarName));
			sendPacket(dm);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleDetailsMessage(DetailsMessage dm) {
		final GhostAvatar avatar = findGhostAvatarByUUID(dm.getUUID());
		if (avatar != null) {
			avatar.getNode().setLocalPosition(dm.getPosition().toVector3());
			avatar.getNode().setLocalRotation(dm.getRotation().toMatrix3());
			try {
				if ((avatar.getAvatar() == null && dm.getAvatar() != null) || !avatar.getAvatar().getAvatarName().contentEquals(dm.getAvatar().getAvatarName())) {
					final SceneNode ghostN = game.getEngine().getSceneManager().getSceneNode(avatar.getUUID().toString()); // get existing node
					try {
						ghostN.detachObject(avatar.getUUID().toString()); // detach the existing entity from the node
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (ghostN.getPhysicsObject() != null) {
							game.getPhysicsEngine().removeObject(ghostN.getPhysicsObject().getUID());
						}
						game.getEngine().getSceneManager().destroyEntity(dm.getUUID().toString()); // make sure sm forgets about it
					} catch (RuntimeException re) {
						// nothing to do here, just ensuring the entity does not exist
					}
//					final Entity ghostE = game.getEngine().getSceneManager().createEntity(avatar.getUUID().toString(), dm.getAvatar().getAvatarFileName()); // make new entity
					
					final SkeletalEntity ghostE = game.getEngine().getSceneManager().createSkeletalEntity(avatar.getUUID().toString(), dm.getAvatar().getAvatarSkeletalMeshFileName(), dm.getAvatar().getAvatarSkeletalFileName());
					final Texture tex = game.getEngine().getSceneManager().getTextureManager().getAssetByPath(dm.getAvatar().getAvatarTextureFileName());
					final TextureState tstate = (TextureState)game.getEngine().getSceneManager().getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
					tstate.setTexture(tex);
					ghostE.setRenderState(tstate);
			    	ghostE.setPrimitive(Primitive.TRIANGLES);
			    	ghostE.loadAnimation("runAnimation", dm.getAvatar().getAvatarAnimationFileName());
			    	
			    	final float scale = dm.getAvatar().getScale();
					final float heightOffset = dm.getAvatar().getAvatarHeightOffset();
					ghostN.scale(scale, scale, scale);
					ghostN.moveUp(heightOffset);
			    	
					ghostN.attachObject(ghostE); // and attach it
					
					avatar.setAvatar(dm.getAvatar()); // success, remember avatar
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void sendHangupMessage() {
		try {
			final HangupMessage hm = new HangupMessage();
			initMessage(hm);
			hm.setUUID(this.uuid);
			sendPacket(hm);
			System.out.println("Sent hangup!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleHangupMessage(HangupMessage hm) {
		System.out.println("Received Hangup for: " + hm.getUUID());
		final GhostAvatar avatar = findGhostAvatarByUUID(hm.getUUID());
		if (avatar != null) {
			ghostAvatars.remove(avatar);
			game.removeGhostAvatar(avatar);
		}
	}
	
	private GhostAvatar findGhostAvatarByUUID(UUID uuid) {
		if (uuid == null) return null;
		final Iterator<GhostAvatar> it = ghostAvatars.iterator();
		while (it.hasNext()) {
			final GhostAvatar avatar = it.next();
			if (avatar.getUUID().toString().contentEquals(uuid.toString())) {
				return avatar;
			}
		}
		return null;
	}

	/**
	 * @return the ghostAvatars
	 */
	public Vector<GhostAvatar> getGhostAvatars() {
		return ghostAvatars;
	}

	/**
	 * @param ghostAvatars the ghostAvatars to set
	 */
	public void setGhostAvatars(Vector<GhostAvatar> ghostAvatars) {
		this.ghostAvatars = ghostAvatars;
	}

	/**
	 * @return the game
	 */
	public MyGame getGame() {
		return game;
	}

	/**
	 * @return the uuid
	 */
	public UUID getUUID() {
		return uuid;
	}
	
	public IClientSocket getSocketInfo() {
		return this.getClientSocket();
	}

	/**
	 * @return the clientName
	 */
	public String getClientName() {
		return clientName;
	}
	
	private void initMessage(Message msg) throws UnknownHostException {
		msg.setProtocol(this.protocolType);
		msg.setUUID(getUUID());
		msg.setFromName(getClientName());
		msg.setFromIP(this.getLocalInetAddress().getHostAddress());
		msg.setFromPort(this.getLocalPort());
		msg.setToName(this.remoteName);
		msg.setToIP(this.remoteAddress.getHostAddress());
		msg.setToPort(this.remotePort);
	}


}
