package a3.avatar.impl;

import a3.avatar.BasicAvatar;

public class SphereBot extends BasicAvatar {

	private static final long serialVersionUID = 5825427995305685076L;

	public SphereBot() {
		super.setAvatarName("SphereBot");
		super.setAvatarSkeletalFileName("Sphere_Bot.rks");
		super.setAvatarSkeletalMeshFileName("Sphere_Bot.rkm");
		super.setAvatarAnimationFileName("Sphere_Bot_Attack.rka");
		super.setAvatarTextureFileName("Sphere_Bot_color_1.jpg");
		super.setAvatarHeightOffset(15.0f);
		super.setScale(1.0f);
		super.setMass(1.0f);
	}
	
}
