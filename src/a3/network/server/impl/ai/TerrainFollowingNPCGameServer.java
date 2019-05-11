package a3.network.server.impl.ai;

import java.util.UUID;

import a3.network.api.messages.impl.HeightMessage;
import a3.network.server.Server;
import ray.rml.Vector3;

public interface TerrainFollowingNPCGameServer extends Server {

	public void requestHeightMessage(UUID requestor, Vector3 globalPosition);
	public void sendHeightMessage(HeightMessage hm);
	public void handleHeightMessage(HeightMessage hm);
	
}
