package a3.network.server.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import a3.network.api.messages.Message;
import a3.network.api.messages.impl.CreateMessage;
import a3.network.api.messages.impl.DetailsMessage;
import a3.network.api.messages.impl.HangupMessage;
import a3.network.api.messages.impl.JoinMessage;
import a3.network.api.messages.impl.MoveMessage;
import a3.network.api.messages.impl.RotateMessage;
import a3.network.server.Logger;
import a3.network.server.Position;
import a3.network.server.Server;
import ray.networking.server.GameConnectionServer;
import ray.networking.server.IClientInfo;
import ray.rml.Vector3;

public class UDPGameServer extends GameConnectionServer<UUID> implements Server {

	public UDPGameServer(int localPort) throws IOException {
		super(localPort, ProtocolType.UDP);
		Logger.INSTANCE.logln("UDPGameServer Started: " + this.getLocalInetAddress() + ":" + this.getLocalPort());
	}
	
	@Override
	public void processPacket(Object obj, InetAddress senderIP, int senderPort) {
		final Message msg = (Message)obj;
		Logger.INSTANCE.logln(msg.toString());
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
			Logger.INSTANCE.logln("Unknown Message Type!");
		}
	}
	
	@Override
	public void sendJoinMessage(UUID uuid, boolean success) {
		final JoinMessage sjm = new JoinMessage();
		sjm.setUUID(uuid);
		sjm.setJoinSuccess(success);
		try {
			sendPacket(sjm, uuid);
			Logger.INSTANCE.logln("Joined Clients:");
			this.getClients().forEach((k,v) -> Logger.INSTANCE.logln("\t" + k));
		} catch (IOException e) {
			Logger.INSTANCE.log(e);
		}
	}
	
	@Override
	public void handleJoinMessage(JoinMessage jm) {
		Logger.INSTANCE.logln(jm.toString());
		try {
			final IClientInfo ci = getServerSocket().createClientInfo(InetAddress.getByName(jm.getFromIP()), jm.getFromPort());
			addClient(ci, jm.getUUID());
			sendJoinMessage(jm.getUUID(), true);
			return; // if successful, return here
		} catch (IOException e) {
			Logger.INSTANCE.log(e);
		}
		sendJoinMessage(jm.getUUID(), false);
	}

	@Override
	public void sendCreateMessage(UUID uuid, Vector3 playerPosition) {
		try {
			final CreateMessage cm = new CreateMessage();
			initMessage(cm);
			cm.setUUID(uuid);
			cm.setPosition(Position.fromVector3(playerPosition));
			forwardPacketToAll(cm, uuid);
		} catch (IOException e) {
			Logger.INSTANCE.log(e);
		}
	}

	@Override
	public void handleCreateMessage(CreateMessage cm) {
		sendCreateMessage(cm.getUUID(), cm.getPosition().toVector3());
		sendDetailsMessage(cm.getUUID());
	}

	@Override
	public void sendMoveMessage(UUID uuid, Vector3 position) {
		try {
			final MoveMessage mm = new MoveMessage();
			initMessage(mm);
			mm.setUUID(uuid);
			mm.setPosition(Position.fromVector3(position));
			forwardPacketToAll(mm, uuid);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleMoveMessage(MoveMessage mm) {
		sendMoveMessage(mm.getUUID(), mm.getPosition().toVector3());
	}

	@Override
	public void sendRotateMessage(UUID uuid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleRotateMessage(RotateMessage rm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendDetailsMessage(UUID uuid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleDetailsMessage(DetailsMessage dm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendHangupMessage(UUID uuid) {
		try {
			final HangupMessage hm = new HangupMessage();
			initMessage(hm);
			hm.setUUID(uuid);
			forwardPacketToAll(hm, uuid);
		} catch (IOException e) {
			Logger.INSTANCE.log(e);
		}
	}

	@Override
	public void handleHangupMessage(HangupMessage hm) {
		sendHangupMessage(hm.getUUID());
		removeClient(hm.getUUID());
	}
	
	@Override
	public void shutdown() {
		final HangupMessage hm = new HangupMessage();
		try {
			sendPacketToAll(hm);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			super.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.INSTANCE.logln("GameServer Stoped");
	}
	
	private void initMessage(Message msg) throws UnknownHostException {
		msg.setProtocol(ServerProtocol.UDP);
		//msg.setUUID(getUUID());
		msg.setFromName("Server");
		msg.setFromIP(this.getLocalInetAddress().getHostAddress().toString());
		msg.setFromPort(this.getLocalPort());
		msg.setToName("*");
		msg.setToIP("*");
		msg.setToPort(-1);
	}
	
}