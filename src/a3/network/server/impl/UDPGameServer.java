package a3.network.server.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import a3.network.api.messages.Message;
import a3.network.api.messages.impl.CreateMessage;
import a3.network.api.messages.impl.DetailsMessage;
import a3.network.api.messages.impl.HangupMessage;
import a3.network.api.messages.impl.JoinMessage;
import a3.network.api.messages.impl.MoveMessage;
import a3.network.api.messages.impl.RotateMessage;
import a3.network.server.Logger;
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
		Logger.INSTANCE.logln(senderIP.toString() + ":" + senderPort);
		Logger.INSTANCE.logln(obj.getClass().getSimpleName());
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
	public void sendJoinMessage() {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleHangupMessage(HangupMessage hm) {
		// TODO Auto-generated method stub
		
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
	
}
