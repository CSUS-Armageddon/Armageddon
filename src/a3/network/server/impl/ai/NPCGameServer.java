package a3.network.server.impl.ai;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import a3.avatar.Avatars;
import a3.network.api.Position;
import a3.network.api.messages.Message;
import a3.network.api.messages.impl.CreateMessage;
import a3.network.api.messages.impl.DetailsMessage;
import a3.network.api.messages.impl.HangupMessage;
import a3.network.api.messages.impl.HeightMessage;
import a3.network.api.messages.impl.JoinMessage;
import a3.network.api.messages.impl.MoveMessage;
import a3.network.api.messages.impl.RotateMessage;
import a3.network.logging.ServerLogger;
import a3.network.server.impl.UDPGameServer;
import a3.network.server.impl.ai.npc.NPC;
import a3.network.server.impl.ai.npc.NPCRunner;
import ray.rml.Vector3;

public class NPCGameServer extends UDPGameServer implements TerrainFollowingNPCGameServer {
	
	private final String ipAddress;
	private final int localPort;
	
	private UUID firstJoinedClientUUID;
	
	private static final int MAX_NPC = 5;
	private final List<NPC> npcs = new ArrayList<NPC>();
	private final ScheduledExecutorService npcExecutorService = Executors.newScheduledThreadPool(1);
	
	final Random random = new Random();
	
	public NPCGameServer(String ipAddress, int localPort, String serverName) throws IOException {
		super(ipAddress, localPort, serverName);
		this.ipAddress = ipAddress;
		this.localPort = localPort;
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
		case HEIGHT:
			handleHeightMessage((HeightMessage)msg);
			break;
		default:
			ServerLogger.INSTANCE.logln("Unknown Message Type!");
		}
	}
	
	@Override
	public void handleJoinMessage(JoinMessage jm) {
		super.handleJoinMessage(jm);
		this.firstJoinedClientUUID = jm.getUUID();
		if (npcs.size() < MAX_NPC) {
			final NPC npc = new NPC(this.ipAddress, this.localPort, 
					Math.round(Math.random()) == 1 ? Avatars.MECH.getAvatar() : Avatars.MECH99.getAvatar());
			npcs.add(npc);
			final NPCRunner npcRunner = new NPCRunner(npc);
			npcExecutorService.scheduleAtFixedRate(npcRunner, 5000, 50, TimeUnit.MILLISECONDS);
		}
	}

	@Override
	public void requestHeightMessage(UUID requestor, Vector3 globalPosition) {
		try {
			final HeightMessage hm = new HeightMessage();
			initMessage(hm);
			hm.setRequestor(requestor);
			hm.setPosition(Position.fromVector3(globalPosition));
			this.sendPacketToAll(hm);//, firstJoinedClientUUID);
		} catch (IOException e) {
			ServerLogger.INSTANCE.log(e);
		}
	}
	
	public void sendHeightMessage(HeightMessage hm) {
		try {
			this.forwardPacketToAll(hm, hm.getUUID());//hm.getRequestor());
		} catch (IOException e) {
			ServerLogger.INSTANCE.log(e);
		}
	}

	@Override
	public void handleHeightMessage(HeightMessage hm) {
		if (hm.getDirection() == HeightMessage.Direction.REQUEST) {
			requestHeightMessage(hm.getRequestor(), hm.getPosition().toVector3());
		} else {
			sendHeightMessage(hm);
		}
	}
	
//	private UUID getRandomClientUUID() {
//		UUID randomUUID = null;
//		int randomClient = random.nextInt(this.getClients().size());
//		for (int i = 0; i < randomClient; i++) {
//			randomUUID = this.getClients().keys().nextElement();
//		}
//		return randomUUID;
//	}
	
}
