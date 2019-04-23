package a3.avatar;

public abstract class BasicAvatar implements Avatar {

	private static final long serialVersionUID = 3124433782849223911L;
	
	protected String avatarName = null;
	protected String avatarFileName = null;
	protected float avatarHeightOffset = 0.0f;
	
	/**
	 * @return the avatarName
	 */
	@Override
	public String getAvatarName() {
		return avatarName;
	}

	/**
	 * @param avatarName the avatarName to set
	 */
	public void setAvatarName(String avatarName) {
		this.avatarName = avatarName;
	}

	/**
	 * @return the avatarFileName
	 */
	@Override
	public String getAvatarFileName() {
		return avatarFileName;
	}
	
	/**
	 * @param avatarFileName the avatarFileName to set
	 */
	public void setAvatarFileName(String avatarFileName) {
		this.avatarFileName = avatarFileName;
	}
	
	/**
	 * @return the avatarHeightOffset
	 */
	@Override
	public float getAvatarHeightOffset() {
		return avatarHeightOffset;
	}
	
	/**
	 * @param avatarHeightOffset the avatarHeightOffset to set
	 */
	@Override
	public void setAvatarHeightOffset(float avatarHeightOffset) {
		this.avatarHeightOffset = avatarHeightOffset;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [ ")
			.append("avatarName=\"").append(getAvatarName())
			.append("\", avatarFileName=\"").append(getAvatarFileName())
			.append("\", avatarHeightOffset=\"").append(getAvatarHeightOffset())
		.append("\" ]");
		return sb.toString();
	}
}
