package a3.network.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

import a3.MyGame;
import a3.network.api.Position;
import a3.network.api.Rotation;
import a3.network.api.messages.Message;
import a3.network.api.messages.impl.CreateMessage;
import a3.network.api.messages.impl.DetailsMessage;
import a3.network.api.messages.impl.HangupMessage;
import a3.network.api.messages.impl.JoinMessage;
import a3.network.api.messages.impl.MoveMessage;
import a3.network.api.messages.impl.RotateMessage;
import a3.network.logging.ClientLogger;
import a3.network.server.impl.ServerProtocol;
import ray.networking.client.GameConnectionClient;
import ray.networking.client.IClientSocket;
import ray.rml.Matrix3;
import ray.rml.Vector3;

public class GameClient extends GameConnectionClient implements Client {
	
	private final MyGame game;
	private final String clientName;
	private final UUID uuid;
	private Vector<GhostAvatar> ghostAvatars;
	
	public GameClient(InetAddress remoteAddress, int remotePort, ProtocolType protocolType, MyGame game, String clientName) throws IOException {
		super(remoteAddress, remotePort, protocolType);
		this.game = game;
		this.uuid = UUID.randomUUID();
		this.ghostAvatars = new Vector<GhostAvatar>();
		this.clientName = clientName;
	}
	
	@Override
	protected void processPacket(Object obj) {
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
		case DETAILS:
			handleDetailsMessage((DetailsMessage)msg);
			break;
		case HANGUP:
			handleHangupMessage((HangupMessage)msg);
			break;
		default:
			System.out.println("Unknown Message Type!");
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
			System.out.println("Join Success");
			sendCreateMessage(game.getPlayerPosition());
		} else {
			game.setClientConnected(false);
			System.out.println("Join Failure");
		}
	}

	@Override
	public void sendCreateMessage(Vector3 playerPosition) {
		try {
			final CreateMessage cm = new CreateMessage();
			initMessage(cm);
			cm.setPosition(Position.fromVector3(playerPosition));
			sendPacket(cm);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleCreateMessage(CreateMessage cm) {
		final GhostAvatar avatar = new GhostAvatar(cm.getUUID(), cm.getPosition().toVector3());
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
	public void sendDetailsMessage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleDetailsMessage(DetailsMessage dm) {
		// TODO Auto-generated method stub
		
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

	/**
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}
	
	private void initMessage(Message msg) throws UnknownHostException {
		msg.setProtocol(ServerProtocol.UDP);
		msg.setUUID(getUUID());
		msg.setFromName(getClientName());
		msg.setFromIP(this.getLocalInetAddress().getHostAddress().toString());
		msg.setFromPort(this.getLocalPort());
		msg.setToName("Server");
		msg.setToIP("localhost");
		msg.setToPort(6868);
	}


}
