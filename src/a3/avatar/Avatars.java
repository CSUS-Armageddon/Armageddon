package a3.avatar;

public enum Avatars {

	MECH(new a3.avatar.impl.Mech()),
	MECH99(new a3.avatar.impl.Mech99()),
	SPHEREBOT(new a3.avatar.impl.SphereBot()),
	;
	
	private Avatars(Avatar avatar) {
		this.avatar = avatar;
	}
	
	private final Avatar avatar;
	
	public Avatar getAvatar() {
		return this.avatar;
	}
	
	public static Avatar fromAvatarName(String name) {
		if (name != null && !name.isEmpty()) {
			for (Avatars avatar : Avatars.values()) {
				if (avatar.getAvatar().getAvatarName().contentEquals(name)) {
					return avatar.getAvatar();
				}
			}
		}
		return null;
	}
	
}
