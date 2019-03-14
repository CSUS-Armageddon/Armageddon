package myGameEngine.mesh;

import java.awt.Color;

import ray.rage.Engine;
import ray.rage.scene.SceneManager;

public class GroundPlane extends CustomObject {
	
	final private String materialFilename = "default.mtl";
	final private String textureFilename = "ground-plane.jpeg";
	final private Color emissiveColor = Color.YELLOW;
	
	final private float[] verticies = new float[]
			{
				-1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, -1.0f,
				-1.0f, 0.0f, 1.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f, -1.0f
			};
	final private float[] texcoords = new float[]
			{
				0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
				0.05f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f
			};
	final private float[] normals = new float[]
			{
				0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
				0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f
			};
	final private int[] indicies = new int[] { 0, 1, 2, 3, 4, 5 };

	public GroundPlane(String name, Engine eng, SceneManager sm) {
		super(name, eng, sm);
		init();
	}
	
	@Override
	public float[] getVerticies() {
		return verticies;
	}

	@Override
	public float[] getTexcoords() {
		return texcoords;
	}

	@Override
	public float[] getNormals() {
		return normals;
	}

	@Override
	public int[] getIndicies() {
		return indicies;
	}

	@Override
	public String getTextureFilename() {
		return textureFilename;
	}

	@Override
	public String getMaterialFilename() {
		return materialFilename;
	}

	@Override
	public Color getEmissiveColor() {
		return emissiveColor;
	}

}
