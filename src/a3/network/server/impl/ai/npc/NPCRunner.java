package a3.network.server.impl.ai.npc;

import java.util.concurrent.atomic.AtomicBoolean;

import a3.network.logging.ServerLogger;

public class NPCRunner implements Runnable {
	
	private final NPC npc;
	private AtomicBoolean isRunning = new AtomicBoolean(false);
	private AtomicBoolean isInit = new AtomicBoolean(false);
	
	public NPCRunner(NPC npc) {
		this.npc = npc;
	}

	@Override
	public void run() {
		try {
			if (!isInit.get()) {
				npc.init();
				npc.joinSession();
				isInit.set(true);
			}
			if (!isRunning.get()) {
				npc.processNetworking();
			}
			if (npc.isClientConnected()) {
				isRunning.set(true);
			}
			npc.processNetworking();
		} catch (Exception e) {
			ServerLogger.INSTANCE.log(e);
		}
	}
}
