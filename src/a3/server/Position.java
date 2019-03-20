package a3.server;

import ray.rml.Vector3;
import ray.rml.Vector3f;

public class Position {

	final float x;
	final float y;
	final float z;
	
	public Position(final float x, final float y, final float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @return the z
	 */
	public float getZ() {
		return z;
	}
	
	public Vector3 toVector3() {
		return Vector3f.createFrom(this.x, this.y, this.z);
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [ ")
			.append("x=\"").append(getX()).append("\", y=\"").append(getY()).append("\", z=\"").append(getZ())
		.append("\" ]");
		return sb.toString();
	}
	
}
