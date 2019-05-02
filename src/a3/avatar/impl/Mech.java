package a3.avatar.impl;

import a3.avatar.BasicAvatar;

public class Mech extends BasicAvatar {

	private static final long serialVersionUID = -2442634531374974869L;

	public Mech() {
		super.setAvatarName("Mech");
		super.setAvatarFileName("mechfinalone.obj");
		super.setAvatarHeightOffset(2.0f);
		super.setScale(1.0f);
		super.setMass(1.0f);
	}

}
