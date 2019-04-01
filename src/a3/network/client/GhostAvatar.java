package a3.network.client;

import java.util.UUID;

import ray.rage.scene.Entity;
import ray.rage.scene.SceneNode;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class GhostAvatar {

	private final UUID uuid;
	private SceneNode node;
	private Entity entity;
	private Vector3 position = Vector3f.createFrom(0.0f, 0.0f, 0.0f);
	
	public GhostAvatar(UUID uuid, Vector3 position) {
		this.uuid = uuid;
		this.position = position;
	}

	/**
	 * @return the node
	 */
	public SceneNode getNode() {
		return node;
	}

	/**
	 * @param node the node to set
	 */
	public void setNode(SceneNode node) {
		this.node = node;
	}

	/**
	 * @return the entity
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	/**
	 * @return the position
	 */
	public Vector3 getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Vector3 position) {
		this.position = position;
	}

	/**
	 * @return the uuid
	 */
	public UUID getUUID() {
		return uuid;
	}
}
