package a3.network.api.messages.impl;

import java.io.IOException;
import java.util.UUID;

import a3.MyGame;
import a3.network.api.Force;
import a3.network.api.Position;
import a3.network.api.messages.BasicMessage;
import a3.network.api.messages.MessageType;
import myGameEngine.util.ArrayUtils;
import ray.physics.PhysicsObject;
import ray.rage.scene.Entity;
import ray.rage.scene.SceneNode;
import ray.rml.Vector3;

public class ShootMessage extends BasicMessage {
	
	private static final long serialVersionUID = 325035032850328L;
	
	private boolean hasAvatarFired;
	
	public Position bulletSpawnForwardVector;
	public Position bulletSpawnposition;
	public Force bulletForce;
	
	public boolean isHasAvatarFired() {
		return hasAvatarFired;
	}

	public void setHasAvatarFired(boolean hasAvatarFired) {
		this.hasAvatarFired = hasAvatarFired;
	}

	public Position getBulletSpawnForwardVector() {
		return bulletSpawnForwardVector;
	}

	public void setBulletSpawnForwardVector(Position bulletSpawnForwardVector) {
		this.bulletSpawnForwardVector = bulletSpawnForwardVector;
	}

	public Position getBulletSpawnposition() {
		return bulletSpawnposition;
	}

	public void setBulletSpawnposition(Position bulletSpawnposition) {
		this.bulletSpawnposition = bulletSpawnposition;
	}

	public ShootMessage() {
		super.setMessageType(MessageType.SHOOT);
	}
	
	public boolean getAvatarHasFired() {
		return this.hasAvatarFired;
	}
	
	public void setAvatarHasFired(boolean hasAvatarHasFired) {
		this.hasAvatarFired = hasAvatarHasFired;
	}
	
	public Position getBulletSpawnPosition() {
		return this.bulletSpawnposition;
	}
	
	public void setBulletSpawnPosition(Position position) {
		this.bulletSpawnposition = position;
	}
	
	public Force getBulletForce() {
		return this.bulletForce;
	}
	
	public void setBulletForce(Force bulletForce) {
		this.bulletForce = bulletForce;
	}
	
	public static void doShoot(MyGame game, Position bulletSpawnForwardVector, Position bulletSpawnposition, Force bulletForce) {
		try {
			final UUID id = UUID.randomUUID();
			final Entity bulletEntity = game.getEngine().getSceneManager().createEntity("bulletEntity-" + id, "earth.obj");
			final SceneNode bulletNode = game.getEngine().getSceneManager().getRootSceneNode().createChildSceneNode("bulletNode-" + id);
			bulletNode.attachObject(bulletEntity);
			
			final Vector3 spawnLoc = bulletSpawnposition.toVector3();
			bulletNode.setLocalPosition(spawnLoc.x(), spawnLoc.y(), spawnLoc.z());
			
			final double[] temptf = ArrayUtils.toDoubleArray(bulletNode.getLocalTransform().toFloatArray());
			final PhysicsObject physicsObj = 
					game.getPhysicsEngine().addSphereObject(game.getPhysicsEngine().nextUID(), 100.0f, temptf, 25.0f);
			physicsObj.setBounciness(0.5f);
			bulletNode.setPhysicsObject(physicsObj);
			
			physicsObj.applyForce(bulletForce.getX(), bulletForce.getY(), bulletForce.getZ(), 0, 0, 0);
			
			game.getBrc().addNode(bulletNode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString().replace(" ]", ", "))
		.append(hasAvatarFired).append(", ").append(bulletSpawnposition).append(", ").append(bulletSpawnForwardVector).append(", ").append(bulletForce).append(" ]");
		return sb.toString();
	}

}
