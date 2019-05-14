package a3.avatar;

import java.io.Serializable;

public interface Avatar extends Serializable {

	public String getAvatarName();
	public String getAvatarFileName();
	public String getAvatarSkeletalFileName();
	public String getAvatarSkeletalMeshFileName();
	public String getAvatarAnimationFileName();
	public String getAvatarTextureFileName();
	public float getAvatarHeightOffset();
	public void setAvatarHeightOffset(float offset);
	public float getScale();
	public void setScale(float scale);
	public float getMass();
	public void setMass(float mass);
	public void setGunNode1HeightOffset(float offset);
	public float getGunNode1HeightOffset();
	public void setGunNode2HeightOffset(float offset);
	public float getGunNode2HeightOffset();
	public void setGunNode1RightOffset(float offset); // gun node 1 is always right
	public float getGunNode1RightOffset();
	public void setGunNode2LeftOffset(float offset); // gun node 1 is always right
	public float getGunNode2LeftOffset();
	
}
