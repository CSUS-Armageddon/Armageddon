package a3.network.server;

import java.io.Serializable;

import ray.rml.Vector3;
import ray.rml.Vector3f;

public class Position implements Serializable {

	private static final long serialVersionUID = 6611472647331374688L;
	
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
	
	public static Position fromVector3(Vector3 vec) {
		return new Position(vec.x(), vec.y(), vec.z());
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
