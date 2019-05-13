package a3.network.server.impl.ai.npc;

import java.util.UUID;

import a3.avatar.Avatar;
import a3.network.client.GhostAvatar;
import ray.rml.Matrix3;
import ray.rml.Vector3;

public class NPCGhostAvatar extends GhostAvatar {
	
	private GhostSceneNode node;

	public NPCGhostAvatar(UUID uuid, Vector3 position, Avatar avatar) {
		super(uuid, position, avatar);
	}
	
	public NPCGhostAvatar(UUID uuid, Vector3 position, Matrix3 rotation, Avatar avatar) {
		super(uuid, position, rotation, avatar);
	}

	/**
	 * @return the node
	 */
	public GhostSceneNode getNode() {
		return node;
	}

	/**
	 * @param node the node to set
	 */
	public void setNode(GhostSceneNode node) {
		this.node = node;
	}

}
