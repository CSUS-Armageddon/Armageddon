package a3.network.server.impl.ai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import a3.avatar.Avatars;
import a3.network.api.messages.impl.CreateMessage;
import a3.network.server.impl.UDPGameServer;
import a3.network.server.impl.ai.npc.NPC;
import a3.network.server.impl.ai.npc.NPCRunner;

public class NPCGameServer extends UDPGameServer {
	
	private final String ipAddress;
	private final int localPort;
	
	private static final int MAX_NPC_THREADS = Runtime.getRuntime().availableProcessors();
	private final List<NPC> npcs = new ArrayList<NPC>();
	private final ScheduledExecutorService npcExecutorService = Executors.newScheduledThreadPool(MAX_NPC_THREADS);
	
	private final ScheduledExecutorService npcCleanupExecutor = Executors.newScheduledThreadPool(1);
	
	public NPCGameServer(String ipAddress, int localPort, String serverName) throws IOException {
		super(ipAddress, localPort, serverName);
		this.ipAddress = ipAddress;
		this.localPort = localPort;
		npcCleanupExecutor.scheduleAtFixedRate(new CleanupNPC(), 5000, 500, TimeUnit.MILLISECONDS);
	}
	
	@Override
	public void handleCreateMessage(CreateMessage cm) {
		super.handleCreateMessage(cm);
		if (!cm.getAvatar().getAvatarName().contentEquals(Avatars.SPHEREBOT.getAvatar().getAvatarName())) { // if the newly joined player is not a sphere bot...
			if (!isNPCAttached(cm.getUUID())) { // and if it has no attached NPC...
				// spawn a sphere bot to assist the player
				final NPC npc = new NPC(this.ipAddress, this.localPort, Avatars.SPHEREBOT.getAvatar(), cm.getUUID());
				final NPCRunner npcRunner = new NPCRunner(npc);
				npcs.add(npc);
				npcExecutorService.scheduleAtFixedRate(npcRunner, 3000, 50, TimeUnit.MILLISECONDS);
			}
		}
	}
	
	private boolean isNPCAttached(UUID clientUUID) {
		for (NPC npc : npcs) {
			if (npc.getFollowPlayerUUID() == clientUUID) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void shutdown() {
		super.shutdown();
		this.npcExecutorService.shutdown();
		this.npcCleanupExecutor.shutdown();
	}
	
	private class CleanupNPC implements Runnable {
		@Override
		public void run() {
			final List<NPC> npcsCopy = Collections.unmodifiableList(npcs);
			for (NPC npc : npcsCopy) {
				if (npc.isShouldRemove()) {
					npcs.remove(npc);
				}
			}
		}
	}
	
}
