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
	
}
