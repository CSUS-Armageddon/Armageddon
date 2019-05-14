package a3;

public class TrackGhostAvatars {
	float x;
	float y;
	float z;
	float nextX;
	float nextY;
	float nextZ;
	String name;
	String entityName;
	
	public String getEntityName() {
		return entityName;
	}



	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	boolean ifAvatarMoved = false;
	boolean ifAvatarAnimationPlayed = false;
	
	public TrackGhostAvatars(String name, String entityname, float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.nextX = 0;
		this.nextY = 0;
		this.nextZ = 0;
		
		this.name = name;
		this.entityName = entityname;
	}
	


	public float getX() {
		return x;
	}



	public void setX(float x) {
		this.x = x;
	}



	public float getY() {
		return y;
	}



	public void setY(float y) {
		this.y = y;
	}



	public float getZ() {
		return z;
	}



	public void setZ(float z) {
		this.z = z;
	}



	public float getNextX() {
		return nextX;
	}



	public void setNextX(float nextX) {
		this.nextX = nextX;
	}



	public float getNextY() {
		return nextY;
	}



	public void setNextY(float nextY) {
		this.nextY = nextY;
	}



	public float getNextZ() {
		return nextZ;
	}



	public void setNextZ(float nextZ) {
		this.nextZ = nextZ;
	}



	public String getName() {
		return name;
	}

	public String toString() {
		return " ghostavatar name: " + this.getName() + " ghostavatar's x: " + this.getX() + " ghostavatar's y: " + this.getY() + " ghostavatars z: " + this.getZ()
						+ " ghostavatar's nextX: " + this.getNextX() + " ghostavatar's nextY: " + this.getNextY() + " ghostavatar nextY: " + this.getNextY() + " ghostavatar's nextZ: " + this.getNextZ();
	}

	public void setName(String name) {
		this.name = name;
	}



	public void setNextZ(int nextZ) {
		this.nextZ = nextZ;
	}

	public boolean isIfAvatarMoved() {
		return ifAvatarMoved;
	}

	public void setIfAvatarMoved(boolean ifAvatarMoved) {
		this.ifAvatarMoved = ifAvatarMoved;
	}

	public boolean isIfAvatarAnimationPlayed() {
		return ifAvatarAnimationPlayed;
	}

	public void setIfAvatarAnimationPlayed(boolean ifAvatarAnimationPlayed) {
		this.ifAvatarAnimationPlayed = ifAvatarAnimationPlayed;
	}


}
