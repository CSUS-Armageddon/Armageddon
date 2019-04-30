package a3.editor.avatar;

public enum PlaceableAvatars {

	Tree_01(new a3.editor.avatar.impl.Tree_01()),
	Tree_02(new a3.editor.avatar.impl.Tree_02()),
	;
	
	private PlaceableAvatars(PlaceableAvatar placeableAvatar) {
		this.placeableAvatar = placeableAvatar;
	}
	
	private final PlaceableAvatar placeableAvatar;
	
	public PlaceableAvatar getPlaceableAvatar() {
		return this.placeableAvatar;
	}
	
	public static PlaceableAvatar fromPlaceableAvatarName(String name) {
		if (name != null && !name.isEmpty()) {
			for (PlaceableAvatars avatar : PlaceableAvatars.values()) {
				if (avatar.getPlaceableAvatar().getAvatarName().contentEquals(name)) {
					return avatar.getPlaceableAvatar();
				}
			}
		}
		return null;
	}
	
	public static PlaceableAvatar fromPlaceableAvatarFileName(String fileName) {
		if (fileName != null && !fileName.isEmpty()) {
			for (PlaceableAvatars avatar : PlaceableAvatars.values()) {
				if (avatar.getPlaceableAvatar().getAvatarFileName().contentEquals(fileName)) {
					return avatar.getPlaceableAvatar();
				}
			}
		}
		return null;
	}
	
}
