package a3.network.api;

import java.io.Serializable;

import ray.rml.Vector3;
import ray.rml.Vector3f;

public class Force implements Serializable {
	
	private static final long serialVersionUID = 575305732503275728L;
	
	final float x;
	final float y;
	final float z;
	
	public Force(final float x, final float y, final float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}
	
	public Vector3 toVector3() {
		return Vector3f.createFrom(this.x, this.y, this.z);
	}
	
	public static Force setXYZForce(float xForce, float yForce, float zForce) {
		return new Force(xForce,yForce,zForce);
	}
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [ ")
		.append("x=\"").append(getX()).append("\", y=\"").append(getY()).append("\", z=\"").append(getZ())
		.append("\" ]");
		return sb.toString();
	}


}
