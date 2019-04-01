package a3.network.client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

import a3.MyGame;
import a3.network.api.messages.Message;
import a3.network.api.messages.impl.CreateMessage;
import a3.network.api.messages.impl.DetailsMessage;
import a3.network.api.messages.impl.HangupMessage;
import a3.network.api.messages.impl.JoinMessage;
import a3.network.api.messages.impl.MoveMessage;
import a3.network.api.messages.impl.RotateMessage;
import a3.network.server.impl.ServerProtocol;
import ray.networking.client.GameConnectionClient;
import ray.networking.client.IClientSocket;
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
		switch (msg.getMessageType()) {
		case JOIN:
			final JoinMessage jm = (JoinMessage)msg;
			handleJoinMessage(jm);
			break;
		case CREATE:
			
			break;
		case MOVE:
			
			break;
		case ROTATE:
			
			break;
		case DETAILS:
			
			break;
		case HANGUP:
			
			break;
		default:
			
		}
	}
	
	/* (non-Javadoc)
	 * @see a3.network.client.Client#sendJoinMessage()
	 */
	@Override
	public void sendJoinMessage() {
		try {
			final JoinMessage jm = new JoinMessage();
			jm.setProtocol(ServerProtocol.UDP);
			jm.setUUID(getUUID());
			jm.setFromName(getClientName());
			jm.setFromIP(this.getLocalInetAddress().getHostAddress().toString());
			jm.setFromPort(this.getLocalPort());
			jm.setToName("Server");
			jm.setToIP("localhost");
			jm.setToPort(6868);
			sendPacket(jm);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void handleJoinMessage(JoinMessage jm) {
		if (jm.isJoinSuccess()) {
			game.setClientConnected(true);
			sendCreateMessage(game.getPlayerPosition());
			System.out.println("Join Success");
		} else {
			game.setClientConnected(false);
			System.out.println("Join Failure");
		}
	}

	@Override
	public void sendCreateMessage(Vector3 playerPosition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleCreateMessage(CreateMessage cm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMoveMessage(Vector3 worldPosition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleMoveMessage(MoveMessage mm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendRotateMessage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleRotateMessage(RotateMessage rm) {
		// TODO Auto-generated method stub
		
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
		final HangupMessage hm = new HangupMessage();
		hm.setUUID(this.uuid);
		try {
			sendPacket(hm);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleHangupMessage(HangupMessage hm) {
		final GhostAvatar avatar = findGhostAvatarByUUID(hm.getUUID());
		if (avatar != null) {
			ghostAvatars.remove(avatar);
			game.removeGhostAvatar(avatar);
		}
	}
	
	private GhostAvatar findGhostAvatarByUUID(UUID uuid) {
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


}
