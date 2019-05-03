package a3.network.server.impl.ai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import a3.avatar.Avatars;
import a3.network.api.messages.impl.JoinMessage;
import a3.network.server.impl.UDPGameServer;
import a3.network.server.impl.ai.npc.NPC;
import a3.network.server.impl.ai.npc.NPCRunner;

public class NPCGameServer extends UDPGameServer {
	
	private final String ipAddress;
	private final int localPort;
	
	private static final int MAX_NPC = 5;
	private final List<NPC> npcs = new ArrayList<NPC>();
	private final ScheduledExecutorService npcExecutorService = Executors.newScheduledThreadPool(1);
	
	public NPCGameServer(String ipAddress, int localPort, String serverName) throws IOException {
		super(ipAddress, localPort, serverName);
		this.ipAddress = ipAddress;
		this.localPort = localPort;
	}
	
	@Override
	public void handleJoinMessage(JoinMessage jm) {
		super.handleJoinMessage(jm);
		if (npcs.size() < MAX_NPC) {
			final NPC npc = new NPC(this.ipAddress, this.localPort, 
					Math.round(Math.random()) == 1 ? Avatars.MECH.getAvatar() : Avatars.MECH99.getAvatar());
			npcs.add(npc);
			final NPCRunner npcRunner = new NPCRunner(npc);
			npcExecutorService.scheduleAtFixedRate(npcRunner, 5000, 50, TimeUnit.MILLISECONDS);
		}
	}
	
}
