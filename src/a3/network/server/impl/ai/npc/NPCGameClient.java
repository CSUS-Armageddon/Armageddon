package a3.network.server.impl.ai.npc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

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
import a3.network.api.messages.impl.ShootMessage;
import a3.network.client.Client;
import a3.network.client.GhostAvatar;
import a3.network.logging.ServerLogger;
import ray.networking.client.GameConnectionClient;
import ray.networking.client.IClientSocket;
import ray.rml.Matrix3;
import ray.rml.Vector3;

public class NPCGameClient extends GameConnectionClient implements Client {

	//private final MyGame game;
	private final NPC npc;
	private final String clientName;
	private final UUID uuid;
	private Vector<GhostAvatar> ghostAvatars;
	
	private String remoteName;
	private final InetAddress remoteAddress;
	private final int remotePort;
	private final ProtocolType protocolType;
	
	public NPCGameClient(InetAddress remoteAddress, int remotePort, ProtocolType protocolType, NPC npc, String clientName) throws IOException {
		super(remoteAddress, remotePort, protocolType);
		this.npc = npc;
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
			ServerLogger.INSTANCE.logln("NPC > " + msg.toString());
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
			case SHOOT:
				// you do not need to know what this is!
				break;
			default:
				ServerLogger.INSTANCE.logln("Unknown Message Type!");
			}
		} catch (Exception e) {
			ServerLogger.INSTANCE.log(e);
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
			npc.setClientConnected(true);
			this.remoteName = jm.getFromName();
			//System.out.println("Join Success");
			sendCreateMessage(npc.getPlayerPosition(), npc.getPlayerRotation(), npc.getAvatar().getAvatarName());
		} else {
			npc.setClientConnected(false);
			//System.out.println("Join Failure");
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
		final NPCGhostAvatar avatar = new NPCGhostAvatar(cm.getUUID(), cm.getPosition().toVector3(), cm.getRotation().toMatrix3(), cm.getAvatar());
		try {
			npc.addGhostAvatar(avatar);
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
		final NPCGhostAvatar avatar = (NPCGhostAvatar) findGhostAvatarByUUID(mm.getUUID());
		if (avatar != null) {
			avatar.getNode().setLocalPosition(mm.getPosition().toVector3());
			npc.followPlayer(mm.getUUID(), avatar.getNode().getLocalPosition());
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
		final NPCGhostAvatar avatar = (NPCGhostAvatar) findGhostAvatarByUUID(rm.getUUID());
		if (avatar != null) {
			avatar.getNode().setLocalRotation(rm.getRotation().toMatrix3());
		}
	}
	
	@Override
	public void handleRequestMessage(RequestMessage rm) {
		sendDetailsMessage(npc.getPlayerPosition(), npc.getPlayerRotation(), npc.getAvatar().getAvatarName());
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
		final NPCGhostAvatar avatar = (NPCGhostAvatar) findGhostAvatarByUUID(dm.getUUID());
		if (avatar != null) {
			if (avatar.getNode() == null) {
				avatar.setNode(new GhostSceneNode());
			}
			avatar.getNode().setLocalPosition(dm.getPosition().toVector3());
			avatar.getNode().setLocalRotation(dm.getRotation().toMatrix3());
			if ((avatar.getAvatar() == null && dm.getAvatar() != null) 
					|| !avatar.getAvatar().getAvatarName().contentEquals(dm.getAvatar().getAvatarName())) {
				avatar.setAvatar(dm.getAvatar()); // success, remember avatar
			}
			npc.followPlayer(dm.getUUID(), avatar.getNode().getLocalPosition());
		}
	}

	@Override
	public void sendHangupMessage() {
		try {
			final HangupMessage hm = new HangupMessage();
			initMessage(hm);
			hm.setUUID(this.uuid);
			sendPacket(hm);
			//System.out.println("Sent hangup!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleHangupMessage(HangupMessage hm) {
		//System.out.println("Received Hangup for: " + hm.getUUID());
		final NPCGhostAvatar avatar = (NPCGhostAvatar) findGhostAvatarByUUID(hm.getUUID());
		if (avatar != null) {
			ghostAvatars.remove(avatar);
			npc.removeGhostAvatar(avatar);
		}
		if (hm.getUUID().equals(npc.getFollowPlayerUUID())) {
			sendHangupMessage();
			try {
				this.shutdown();
			} catch (Exception e) {
				ServerLogger.INSTANCE.log(e);
			}
		}
	}
	
	public GhostAvatar findGhostAvatarByUUID(UUID uuid) {
		if (uuid == null) return null;
		final Iterator<GhostAvatar> it = ghostAvatars.iterator();
		while (it.hasNext()) {
			final NPCGhostAvatar avatar = (NPCGhostAvatar) it.next();
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
	 * @return the npc
	 */
	public NPC getNPC() {
		return npc;
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

	@Override
	public void sendShootMessage(boolean avatarHasFired, Vector3 position, Vector3 forwardVector, float xForce,
			float yForce, float zForce) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleShootMessage(ShootMessage sm) {
		// TODO Auto-generated method stub
		
	}

}
