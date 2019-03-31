package a3.network.server.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import ray.networking.server.GameConnectionServer;

public class UDPGameServer extends GameConnectionServer<UUID> {

	public UDPGameServer(int localPort) throws IOException {
		super(localPort, ProtocolType.UDP);
	}
	
	@Override
	public void processPacket(Object o, InetAddress senderIP, int senderPort) {
		final String messageString = (String)o;
		
	}
	
}
