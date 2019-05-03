package a3.network.client;

import java.util.UUID;

import a3.avatar.Avatar;
import ray.rage.scene.Entity;
import ray.rage.scene.SceneNode;
import ray.rage.scene.SkeletalEntity;
import ray.rml.Matrix3;
import ray.rml.Matrix3f;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class GhostAvatar {

	private final UUID uuid;
	private Avatar avatar;
	private SceneNode node;
	private Entity entity;
	private SkeletalEntity skEntity;
	private Vector3 position = Vector3f.createFrom(0.0f, 0.0f, 0.0f);
	private Matrix3 rotation = Matrix3f.createIdentityMatrix();
	
	public GhostAvatar(UUID uuid, Vector3 position, Avatar avatar) {
		this.uuid = uuid;
		this.position = position;
		this.avatar = avatar;
	}
	
	public GhostAvatar(UUID uuid, Vector3 position, Matrix3 rotation, Avatar avatar) {
		this.uuid = uuid;
		this.position = position;
		this.rotation = rotation;
		this.avatar = avatar;
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
	
	public SkeletalEntity getSkeletalEntity() {
		return skEntity;
	}
	
	public void setSkeletalEntity(SkeletalEntity skEntity) {
		this.skEntity = skEntity;
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
	 * @return the rotation
	 */
	public Matrix3 getRotation() {
		return rotation;
	}

	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation(Matrix3 rotation) {
		this.rotation = rotation;
	}

	/**
	 * @return the uuid
	 */
	public UUID getUUID() {
		return uuid;
	}

	/**
	 * @return the avatar
	 */
	public Avatar getAvatar() {
		return avatar;
	}
	
	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}
}
