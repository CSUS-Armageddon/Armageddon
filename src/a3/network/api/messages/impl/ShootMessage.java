package a3.network.api.messages.impl;

import a3.network.api.Force;
import a3.network.api.Position;
import a3.network.api.messages.BasicMessage;
import a3.network.api.messages.MessageType;
import ray.rml.Vector3;

public class ShootMessage extends BasicMessage {
	
	private static final long serialVersionUID = 325035032850328L;
	
	private boolean hasAvatarFired;
	
	public Vector3 bulletSpawnForwardVector;
	public Position bulletSpawnposition;
	public Force bulletForce;
	
	public boolean isHasAvatarFired() {
		return hasAvatarFired;
	}

	public void setHasAvatarFired(boolean hasAvatarFired) {
		this.hasAvatarFired = hasAvatarFired;
	}

	public Vector3 getBulletSpawnForwardVector() {
		return bulletSpawnForwardVector;
	}

	public void setBulletSpawnForwardVector(Vector3 bulletSpawnForwardVector) {
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
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString().replace(" ]", ", "))
		.append(hasAvatarFired).append(", ").append(bulletSpawnposition).append(", ").append(bulletSpawnForwardVector).append(", ").append(bulletForce).append(" ]");
		return sb.toString();
	}

}
