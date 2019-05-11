package a3.network.server.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
import a3.network.api.messages.impl.HangupMessage;
import a3.network.api.messages.impl.JoinMessage;
import a3.network.api.messages.impl.MoveMessage;
import a3.network.api.messages.impl.RequestMessage;
import a3.network.api.messages.impl.RotateMessage;
import a3.network.logging.ServerLogger;
import a3.network.server.Server;
import ray.networking.server.GameConnectionServer;
import ray.networking.server.IClientInfo;
import ray.rml.Matrix3;
import ray.rml.Vector3;

public class UDPGameServer extends GameConnectionServer<UUID> implements Server {
	
	private static final int SECONDS_DELAY_REQUEST = 3;
	
	private static final ProtocolType PROTOCOL_TYPE = ProtocolType.UDP;
	
	private final String ipAddress;
	private final int port;
	private final String serverName;
	
	final ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);

	public UDPGameServer(String ipAddress, int localPort, String serverName) throws IOException {
		super(localPort, PROTOCOL_TYPE);
		this.ipAddress = ipAddress;
		this.port = localPort;
		this.serverName = serverName;
		ses.scheduleAtFixedRate(new RequestDetailsTask(), SECONDS_DELAY_REQUEST, SECONDS_DELAY_REQUEST, TimeUnit.SECONDS);
		ServerLogger.INSTANCE.logln("UDPGameServer Started: " + this.ipAddress + ":" + this.port + " - " + this.serverName);
	}
	
	@Override
	public void processPacket(Object obj, InetAddress senderIP, int senderPort) {
		final Message msg = (Message)obj;
		msg.setFromIP(senderIP.getHostAddress());
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
		} catch (IOException e) {
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
		} catch (IOException e) {
			
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
		sendHangupMessage(hm.getUUID());
		removeClient(hm.getUUID());
		ServerLogger.INSTANCE.logln("Joined Clients:");
		this.getClients().forEach((k,v) -> ServerLogger.INSTANCE.logln("\t" + k));
	}
	
	@Override
	public void shutdown() {
		final HangupMessage hm = new HangupMessage();
		try {
			sendPacketToAll(hm);
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
	
	protected void initMessage(Message msg) throws UnknownHostException {
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
	
}
