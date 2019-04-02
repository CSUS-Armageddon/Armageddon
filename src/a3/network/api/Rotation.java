package a3.network.api;

import java.io.Serializable;
import java.util.Arrays;

import ray.rml.Matrix3;
import ray.rml.Matrix3f;

public class Rotation implements Serializable {

	private static final long serialVersionUID = -3441167665366306905L;

	final float[] rotationArray;
	
	public Rotation(float[] rotationArray) {
		this.rotationArray = rotationArray;
	}
	
	public float[] getRotationArray() {
		return this.rotationArray;
	}
	
	public Matrix3 toMatrix3() {
		return Matrix3f.createFrom(this.rotationArray);
	}
	
	public static Rotation fromMatrix3(Matrix3 rot) {
		return new Rotation(rot.toFloatArray());
	}
	
	public static Rotation defaultRotation() {
		return Rotation.fromMatrix3(Matrix3f.createIdentityMatrix());
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [ ")
			.append("rotation=\"").append(Arrays.toString(getRotationArray())).append("\"")
		.append("\" ]");
		return sb.toString();
	}
	
}
