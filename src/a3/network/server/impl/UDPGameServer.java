package a3.network.server.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import a3.avatar.Avatars;
import a3.network.api.Position;
import a3.network.api.Rotation;
import a3.network.api.messages.Message;
import a3.network.api.messages.impl.CreateMessage;
import a3.network.api.messages.impl.DetailsMessage;
import a3.network.api.messages.impl.EndGameMessage;
import a3.network.api.messages.impl.GameTimeSyncMessage;
import a3.network.api.messages.impl.HangupMessage;
import a3.network.api.messages.impl.JoinMessage;
import a3.network.api.messages.impl.MoveMessage;
import a3.network.api.messages.impl.RequestMessage;
import a3.network.api.messages.impl.ResultMessage;
import a3.network.api.messages.impl.RotateMessage;
import a3.network.api.messages.impl.ScoreMessage;
import a3.network.api.messages.impl.ShootMessage;
import a3.network.api.messages.impl.StartGameMessage;
import a3.network.logging.ServerLogger;
import a3.network.server.Server;
import ray.networking.server.GameConnectionServer;
import ray.networking.server.IClientInfo;
import ray.rml.Matrix3;
import ray.rml.Vector3;

public class UDPGameServer extends GameConnectionServer<UUID> implements Server {
	
	private static final int SECONDS_DELAY_REQUEST = 50;
	
	//                                          60 sec * 3 mins
	//                                          Game runtime is 3 minutes
	private static final long SECONDS_GAME_RUN = 60 * 3;
	private long currentGameTime = SECONDS_GAME_RUN;
	private boolean isGameOver = false;
	
	private static final float MAX_X_LOC = 1000.0f;
	private static final float MAX_Z_LOC = 1000.0f;
	
	private final Random random = new Random();
	
	private Position gameZonePosition;
	
	private static final ProtocolType PROTOCOL_TYPE = ProtocolType.UDP;
	
	private final String ipAddress;
	private final int port;
	private final String serverName;
	
	final ScheduledExecutorService startGameService = Executors.newScheduledThreadPool(1);
	final ScheduledExecutorService gameTimeService = Executors.newScheduledThreadPool(1);
	final ScheduledExecutorService endGameService = Executors.newScheduledThreadPool(1);
	final ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
	
	private Map<UUID, Long> scoreMap;

	public UDPGameServer(String ipAddress, int localPort, String serverName) throws IOException {
		super(localPort, PROTOCOL_TYPE);
		this.ipAddress = ipAddress;
		this.port = localPort;
		this.serverName = serverName;
		ses.scheduleAtFixedRate(new RequestDetailsTask(), SECONDS_DELAY_REQUEST, SECONDS_DELAY_REQUEST, TimeUnit.MILLISECONDS);
		startGameService.scheduleAtFixedRate(new StartGameTask(), 15, 15, TimeUnit.SECONDS);
		ServerLogger.INSTANCE.logln("UDPGameServer Started: " + this.ipAddress + ":" + this.port + " - " + this.serverName);
	}
	
	@Override
	public void processPacket(Object obj, InetAddress senderIP, int senderPort) {
		final Message msg = (Message)obj;
		msg.setFromIP(senderIP.getHostAddress());
		msg.setFromPort(senderPort);
		ServerLogger.INSTANCE.logln(msg.toString());
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
		case SHOOT:
			handleShootMessage((ShootMessage)msg);
			break;
		case TIME:
			break; // nothing to do, server is authoritative source!
		case START:
			break; // nothing to do, server is authoritative source!
		case END:
			break; // nothing to do, server is authoritative source!
		case SCORE:
			handleScoreMessage((ScoreMessage)msg);
			break;
		case RESULT:
			break; // nothing to do, server is authoritative source!
		default:
			ServerLogger.INSTANCE.logln("Unknown Message Type!");
		}
	}
	
	@Override
	public void sendJoinMessage(UUID uuid, boolean success) {
		final JoinMessage sjm = new JoinMessage();
		try {
			initMessage(sjm);
		} catch (UnknownHostException e) {
			ServerLogger.INSTANCE.log(e);
		}
		sjm.setUUID(uuid);
		sjm.setJoinSuccess(success);
		try {
			sendPacket(sjm, uuid);
			ServerLogger.INSTANCE.logln("Joined Clients:");
			this.getClients().forEach((k,v) -> ServerLogger.INSTANCE.logln("\t" + k));
			
			// new client is joined, now send that client all existing client create messages
			this.getClients().forEach((k,v) -> sendGhostCreateMessage(uuid, k, v));
			// now request details from everyone so we can fix the position and rotation to represent current game state
			sendRequestMessage();
		} catch (IOException e) {
			ServerLogger.INSTANCE.log(e);
		}
	}
	
	@Override
	public void handleJoinMessage(JoinMessage jm) {
		try {
			final IClientInfo ci = getServerSocket().createClientInfo(InetAddress.getByName(jm.getFromIP()), jm.getFromPort());
			addClient(ci, jm.getUUID());
			sendJoinMessage(jm.getUUID(), true);
			return; // if successful, return here
		} catch (Exception e) {
			ServerLogger.INSTANCE.log(e);
		}
		sendJoinMessage(jm.getUUID(), false);
	}
	
	@Override
	public void sendGhostCreateMessage(UUID uuid, UUID ghostUUID, IClientInfo ghostClientInfo) {
		if (uuid.equals(ghostUUID)) return;
		try {
			final CreateMessage cm = new CreateMessage();
			initMessage(cm);
			cm.setUUID(ghostUUID);
			cm.setPosition(Position.defaultPosition());
			cm.setRotation(Rotation.defaultRotation());
			sendPacket(cm, uuid);
		} catch (IOException e) {
			ServerLogger.INSTANCE.log(e);
		}
	}

	@Override
	public void sendCreateMessage(UUID uuid, Vector3 playerPosition, Matrix3 playerRotation, String avatarName) {
		try {
			final CreateMessage cm = new CreateMessage();
			initMessage(cm);
			cm.setUUID(uuid);
			cm.setPosition(Position.fromVector3(playerPosition));
			cm.setRotation(Rotation.fromMatrix3(playerRotation));
			cm.setAvatar(Avatars.fromAvatarName(avatarName));
			forwardPacketToAll(cm, uuid);
		} catch (IOException e) {
			ServerLogger.INSTANCE.log(e);
		}
	}

	@Override
	public void handleCreateMessage(CreateMessage cm) {
		sendCreateMessage(cm.getUUID(), cm.getPosition().toVector3(), cm.getRotation().toMatrix3(), cm.getAvatar().getAvatarName());
		sendDetailsMessage(cm.getUUID(), cm.getPosition().toVector3(), cm.getRotation().toMatrix3(), cm.getAvatar().getAvatarName());
	}

	@Override
	public void sendMoveMessage(UUID uuid, Vector3 localPosition) {
		try {
			final MoveMessage mm = new MoveMessage();
			initMessage(mm);
			mm.setUUID(uuid);
			mm.setPosition(Position.fromVector3(localPosition));
			forwardPacketToAll(mm, uuid);
		} catch (IOException e) {
			ServerLogger.INSTANCE.log(e);
		}
	}

	@Override
	public void handleMoveMessage(MoveMessage mm) {
		sendMoveMessage(mm.getUUID(), mm.getPosition().toVector3());
	}

	@Override
	public void sendRotateMessage(UUID uuid, Matrix3 rotation) {
		try {
			final RotateMessage rm = new RotateMessage();
			initMessage(rm);
			rm.setUUID(uuid);
			rm.setRotation(Rotation.fromMatrix3(rotation));
			forwardPacketToAll(rm, uuid);
		} catch (IOException e) {
			ServerLogger.INSTANCE.log(e);
		}
	}

	@Override
	public void handleRotateMessage(RotateMessage rm) {
		sendRotateMessage(rm.getUUID(), rm.getRotation().toMatrix3());
	}
	
	@Override
	public void sendRequestMessage() {
		try {
			final RequestMessage rm = new RequestMessage();
			initMessage(rm);
			sendPacketToAll(rm);
		} catch (Exception e) {
			ServerLogger.INSTANCE.log(e);
		}
	}

	@Override
	public void sendDetailsMessage(UUID uuid, Vector3 localPosition, Matrix3 localRotation, String avatarName) {
		try {
			final DetailsMessage dm = new DetailsMessage();
			initMessage(dm);
			dm.setUUID(uuid);
			dm.setPosition(Position.fromVector3(localPosition));
			dm.setRotation(Rotation.fromMatrix3(localRotation));
			dm.setAvatar(Avatars.fromAvatarName(avatarName));
			forwardPacketToAll(dm, uuid);
		} catch (IOException e) {
			ServerLogger.INSTANCE.log(e);
		}
	}

	@Override
	public void handleDetailsMessage(DetailsMessage dm) {
		sendDetailsMessage(dm.getUUID(), dm.getPosition().toVector3(), dm.getRotation().toMatrix3(), dm.getAvatar().getAvatarName());
	}

	@Override
	public void sendHangupMessage(UUID uuid) {
		try {
			final HangupMessage hm = new HangupMessage();
			initMessage(hm);
			hm.setUUID(uuid);
			forwardPacketToAll(hm, uuid);
		} catch (IOException e) {
			ServerLogger.INSTANCE.log(e);
		}
	}

	@Override
	public void handleHangupMessage(HangupMessage hm) {
		removeClient(hm.getUUID());
		sendHangupMessage(hm.getUUID());
		ServerLogger.INSTANCE.logln("Joined Clients:");
		this.getClients().forEach((k,v) -> ServerLogger.INSTANCE.logln("\t" + k));
	}
	
	@Override
	public void sendShootMessage(UUID uuid, ShootMessage sm) {
		try {
			forwardPacketToAll(sm, uuid);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void handleShootMessage(ShootMessage sm) {
		sendShootMessage(sm.getUUID(), sm);
	}
	
	@Override
	public void handleScoreMessage(ScoreMessage sm) {
		scoreMap.put(sm.getUUID(), sm.getCurrentScore());
//		for (Map.Entry<UUID, Long> score : scoreMap.entrySet()) {
//			if (score.getValue() == -1) {
//				return;
//			}
//		}
		// we have all scores in
		sendResultMessage();
	}
	
	@Override
	public void sendStartMessage() {
		try {
			final StartGameMessage sm = new StartGameMessage();
			initMessage(sm);
			sm.setGameTime(getCurrentGameTime());
			sm.setGameZonePosition(getGameZonePosition());
			sendPacketToAll(sm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void sendEndMessage() {
		try {
			final EndGameMessage em = new EndGameMessage();
			initMessage(em);
			sendPacketToAll(em);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void sendResultMessage() {
		Map.Entry<UUID, Long> winner = null;
		for (Map.Entry<UUID, Long> entry : scoreMap.entrySet()) {
			if (winner == null) {
				winner = entry;
			}
			if (winner.getValue() < entry.getValue()) {
				winner = entry;
			}
		}
		
		try {
			final ResultMessage rm = new ResultMessage();
			initMessage(rm);
			rm.setWinnerUUID(winner.getKey());
			sendPacketToAll(rm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void startGame() {
		this.currentGameTime = UDPGameServer.SECONDS_GAME_RUN;
		
		// set all player scores to -1
		scoreMap = new HashMap<UUID, Long>();
		final Enumeration<UUID> clientUUIDs = this.getClients().keys();
		while (clientUUIDs.hasMoreElements()) {
			scoreMap.put(clientUUIDs.nextElement(), -1L);
		}
		
		// randomize zone area
		this.gameZonePosition = getRandomZonePosition();
		
		sendStartMessage();
		
		gameTimeService.scheduleAtFixedRate(new CountdownGameTimeTask(), 0, 1, TimeUnit.SECONDS);
		endGameService.scheduleAtFixedRate(new GameOverTask(), 5, 1, TimeUnit.SECONDS);
		
		ServerLogger.INSTANCE.logln("Game Started!");
	}

	@Override
	public void endGame() {
		gameTimeService.shutdownNow();
		sendEndMessage();
		ServerLogger.INSTANCE.logln("Game Ended!");
	}

	@Override
	public void syncGameTime() {
		try {
			final GameTimeSyncMessage gtsm = new GameTimeSyncMessage();
			initMessage(gtsm);
			gtsm.setGameTime(currentGameTime);
			sendPacketToAll(gtsm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void shutdown() {
		final HangupMessage hm = new HangupMessage();
		try {
			sendPacketToAll(hm);
			this.ses.shutdown();
		} catch (IOException e) {
			ServerLogger.INSTANCE.log(e);
		}
		try {
			super.shutdown();
		} catch (IOException e) {
			ServerLogger.INSTANCE.log(e);
		}
		ServerLogger.INSTANCE.logln("GameServer Stoped");
	}
	
	private void initMessage(Message msg) throws UnknownHostException {
		msg.setProtocol(PROTOCOL_TYPE);
		msg.setFromName(this.serverName);
		msg.setFromIP(this.ipAddress);
		msg.setFromPort(this.port);
		msg.setToName("*");
		msg.setToIP("*");
		msg.setToPort(-1);
	}
	
	private class RequestDetailsTask implements Runnable {
		@Override
		public void run() {
			sendRequestMessage();
		}
	}

	/**
	 * @return the currentGameTime
	 */
	public long getCurrentGameTime() {
		return currentGameTime;
	}

	/**
	 * @param currentGameTime the currentGameTime to set
	 */
	public void setCurrentGameTime(long currentGameTime) {
		this.currentGameTime = currentGameTime;
	}
	
	/**
	 * @return the gameZonePosition
	 */
	public Position getGameZonePosition() {
		return gameZonePosition;
	}

	/**
	 * @param gameZonePosition the gameZonePosition to set
	 */
	public void setGameZonePosition(Position gameZonePosition) {
		this.gameZonePosition = gameZonePosition;
	}

	private class CountdownGameTimeTask implements Runnable {
		@Override
		public void run() {
			currentGameTime--;
			if (currentGameTime <= 0) {
				isGameOver = true;
			}
		}
	}
	
	private class GameOverTask implements Runnable {
		@Override
		public void run() {
			if (isGameOver) {
				endGame();
				endGameService.shutdownNow();
			}
		}
	}
	
	private class StartGameTask implements Runnable {
		@Override
		public void run() {
			if (getClients().size() > 2) { // 2 equals 1 player and 1 npc
				// good to start game!
				startGame();
				startGameService.shutdown();
			}
		}
	}
	
	private Position getRandomZonePosition() {
		return new Position(getRandomFloat(MAX_X_LOC), 250.0f, getRandomFloat(MAX_Z_LOC));
	}
	
	private float getRandomFloat(float max) {
		return random.nextFloat() * (max - 0.0f);
	}
	
}
