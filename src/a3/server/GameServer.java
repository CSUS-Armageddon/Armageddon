package a3.server;

import java.io.IOException;

import a3.server.api.messages.impl.CreateMessage;
import a3.server.impl.ServerProtocol;

public class GameServer {

	public static void main(String[] args) throws IOException {
		final ServerConfig sc = new ServerConfig("assets/config/server.properties");
		System.out.println("Server Port: " + sc.getString("server.protocol", ServerProtocol.UDP.name()) + " " + sc.getInt("server.port", 6868));
		final CreateMessage cm = new CreateMessage();
		cm.setFromName("GameServer");
		cm.setFromIP("10.5.0.50");
		cm.setFromPort(sc.getInt("server.port"));
		cm.setFromProtocol(ServerProtocol.UDP);
		cm.setToName("Player1");
		cm.setToIP("192.168.254.176");
		cm.setToPort(sc.getInt("server.port"));
		cm.setToProtocol(ServerProtocol.UDP);
		cm.setPosition(new Position(12.5f, 15.34f, 0.5f));
		System.out.println(cm.toString());
	}
	
}
