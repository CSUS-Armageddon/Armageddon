package myGameEngine.mesh;

import java.awt.Color;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import ray.rage.Engine;
import ray.rage.asset.material.Material;
import ray.rage.asset.texture.Texture;
import ray.rage.rendersystem.Renderable.DataSource;
import ray.rage.rendersystem.Renderable.Primitive;
import ray.rage.rendersystem.shader.GpuShaderProgram;
import ray.rage.rendersystem.states.FrontFaceState;
import ray.rage.rendersystem.states.RenderState;
import ray.rage.rendersystem.states.TextureState;
import ray.rage.scene.ManualObject;
import ray.rage.scene.ManualObjectSection;
import ray.rage.scene.SceneManager;
import ray.rage.util.BufferUtil;

public abstract class CustomObject {

	protected final Engine eng;
	protected final SceneManager sm;
	
	protected String name;
	protected ManualObject mo;
	protected ManualObjectSection moSec;
	
	public CustomObject(String name, Engine eng, SceneManager sm) {
		this.sm = sm;
		this.eng = eng;
		this.name = name;
	}
	
	public void init() {
		mo = sm.createManualObject(name);
		moSec = mo.createManualSection(name + "Section");
		mo.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
		
		FloatBuffer vertBuf = null;
		FloatBuffer texBuf = null;
		FloatBuffer normBuf = null;
		IntBuffer indexBuf = null;
		
		if (getVerticies() != null) vertBuf = BufferUtil.directFloatBuffer(getVerticies());
		if (getTexcoords() != null) texBuf = BufferUtil.directFloatBuffer(getTexcoords());
		if (getNormals() != null) normBuf = BufferUtil.directFloatBuffer(getNormals());
		if (getIndicies() != null) indexBuf = BufferUtil.directIntBuffer(getIndicies());
		
		if (vertBuf != null) moSec.setVertexBuffer(vertBuf);
		if (texBuf != null) moSec.setTextureCoordsBuffer(texBuf);
		if (normBuf != null) moSec.setNormalsBuffer(normBuf);
		if (indexBuf != null) moSec.setIndexBuffer(indexBuf);
		
		if (null != getMaterialFilename() && !getMaterialFilename().equals("")) {
			try {
				final Material mat = eng.getMaterialManager().getAssetByPath(getMaterialFilename());
				if (null != getEmissiveColor()) {
					mat.setEmissive(getEmissiveColor());
				}
				mo.setMaterial(mat);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (null != getTextureFilename() && !getTextureFilename().equals("")) {
			try {
				final Texture tex = eng.getTextureManager().getAssetByPath(getTextureFilename());
				final TextureState texState = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
				texState.setTexture(tex);
				final FrontFaceState faceState = (FrontFaceState) sm.getRenderSystem().createRenderState(RenderState.Type.FRONT_FACE);
				mo.setDataSource(DataSource.INDEX_BUFFER);
				mo.setRenderState(texState);
				mo.setRenderState(faceState);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		mo.setPrimitive(Primitive.TRIANGLES);
	}

	/**
	 * @return the verticies
	 */
	abstract public float[] getVerticies();

	/**
	 * @return the texcoords
	 */
	abstract public float[] getTexcoords();

	/**
	 * @return the normals
	 */
	abstract public float[] getNormals();

	/**
	 * @return the indicies
	 */
	abstract public int[] getIndicies();
	
	/**
	 * @return the textureFilename
	 */
	abstract public String getTextureFilename();
	
	/**
	 * @return the materialFilename
	 */
	abstract public String getMaterialFilename();
	
	/**
	 * @return the emissiveColor
	 */
	abstract public Color getEmissiveColor();
	
	/**
	 * @return the mo
	 */
	public ManualObject getManualObject() {
		return mo;
	}
	
	public void setPrimitive(Primitive primitive) {
		mo.setPrimitive(primitive);
	}
	
}
