package a3.avatar.impl;

import a3.avatar.BasicAvatar;

public class Mech99 extends BasicAvatar {

	private static final long serialVersionUID = 8439757265932368730L;

	public Mech99() {
		super.setAvatarName("Mech99");
		super.setAvatarFileName("mech_1.obj");
		super.setAvatarSkeletalFileName("jasonmech.rks");
		super.setAvatarSkeletalMeshFileName("jasonmech.rkm");
		super.setAvatarAnimationFileName("jasonmechrun.rka");
		super.setAvatarTextureFileName("mech_1.png");
		super.setAvatarHeightOffset(6.75f);
	}
	
}
