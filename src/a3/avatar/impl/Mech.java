package a3.avatar.impl;

import a3.avatar.BasicAvatar;

public class Mech extends BasicAvatar {

	private static final long serialVersionUID = -2442634531374974869L;

	public Mech() {
		super.setAvatarName("Mech");
		super.setAvatarFileName("mechfinalone.obj");
		super.setAvatarSkeletalFileName("mechhope777.rks");
		super.setAvatarSkeletalMeshFileName("mechhope777.rkm");
		super.setAvatarAnimationFileName("mechhope777.rka");
		super.setAvatarTextureFileName("mech2textures.png");
		super.setAvatarHeightOffset(15.5f);
		super.setScale(6.0f);
		super.setMass(1.0f);
		super.setGunNode1HeightOffset(5.0f);
		super.setGunNode1RightOffset(0.5f);
		super.setGunNode2HeightOffset(5.0f);
		super.setGunNode2LeftOffset(0.5f);
	}

}
