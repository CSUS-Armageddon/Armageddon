package a3.network.server.impl.ai.npc;

public class NPCRunner implements Runnable {
	
	private final NPC npc;
	private boolean isRunning = false;
	
	public NPCRunner(NPC npc) {
		this.npc = npc;
	}

	@Override
	public void run() {
		if (!isRunning) {
			npc.init();
			if (npc.isClientConnected()) {
				isRunning = true;
			}
		}
		if (random() == 1) {
			if (random() == 1) {
				npc.moveForward();
			} else {
				npc.moveLeft();
			}
		} else {
			npc.yaw();
		}
	}
	
	private long random() {
		return Math.round(Math.random());
	}

}
