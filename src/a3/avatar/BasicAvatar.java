package a3.avatar;

public abstract class BasicAvatar implements Avatar {

	private static final long serialVersionUID = 3124433782849223911L;
	
	protected String avatarName = null;
	protected String avatarFileName = null;
	protected float avatarHeightOffset = 0.0f;

	protected String avatarSkeletalFileName = null;
	protected String avatarSkeletalMeshFileName = null;
	protected String avatarAnimationFileName = null;
	protected String avatarTextureFileName = null;

	protected float scale = 1.0f;
	protected float mass = 1.0f;
	
	protected float gunNode1HeightOffset = 0.0f;
	protected float gunNode1RightOffset = 0.0f;
	protected float gunNode2HeightOffset = 0.0f;
	protected float gunNode2LeftOffset = 0.0f;
	
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
	
	public String getAvatarSkeletalFileName() {
		return avatarSkeletalFileName;
	}
	
	public void setAvatarSkeletalFileName(String avatarSkeletalFileName) {
		this.avatarSkeletalFileName = avatarSkeletalFileName;
	}
	
	public String getAvatarSkeletalMeshFileName() {
		return this.avatarSkeletalMeshFileName;
	}
	
	public void setAvatarSkeletalMeshFileName(String avatarSkeletalMeshFileName) {
		this.avatarSkeletalMeshFileName = avatarSkeletalMeshFileName;
	}
	
	public String getAvatarAnimationFileName() {
		return this.avatarAnimationFileName;
	}
	
	public void setAvatarAnimationFileName(String avatarAnimationFileName) {
		this.avatarAnimationFileName = avatarAnimationFileName;
	}
	
	public String getAvatarTextureFileName() {
		return this.avatarTextureFileName;
	}
	
	public void setAvatarTextureFileName(String avatarTextureFileName) {
		this.avatarTextureFileName = avatarTextureFileName;
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
	
	/**
	 * @return the scale
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * @param scale the scale to set
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}

	/**
	 * @return the mass
	 */
	public float getMass() {
		return mass;
	}

	/**
	 * @param mass the mass to set
	 */
	public void setMass(float mass) {
		this.mass = mass;
	}

	/**
	 * @return the gunNode1HeightOffset
	 */
	public float getGunNode1HeightOffset() {
		return gunNode1HeightOffset;
	}

	/**
	 * @param gunNode1HeightOffset the gunNode1HeightOffset to set
	 */
	public void setGunNode1HeightOffset(float gunNode1HeightOffset) {
		this.gunNode1HeightOffset = gunNode1HeightOffset;
	}

	/**
	 * @return the gunNode1RightOffset
	 */
	public float getGunNode1RightOffset() {
		return gunNode1RightOffset;
	}

	/**
	 * @param gunNode1RightOffset the gunNode1RightOffset to set
	 */
	public void setGunNode1RightOffset(float gunNode1RightOffset) {
		this.gunNode1RightOffset = gunNode1RightOffset;
	}

	/**
	 * @return the gunNode2HeightOffset
	 */
	public float getGunNode2HeightOffset() {
		return gunNode2HeightOffset;
	}

	/**
	 * @param gunNode2HeightOffset the gunNode2HeightOffset to set
	 */
	public void setGunNode2HeightOffset(float gunNode2HeightOffset) {
		this.gunNode2HeightOffset = gunNode2HeightOffset;
	}

	/**
	 * @return the gunNode2LeftOffset
	 */
	public float getGunNode2LeftOffset() {
		return gunNode2LeftOffset;
	}

	/**
	 * @param gunNode2LeftOffset the gunNode2LeftOffset to set
	 */
	public void setGunNode2LeftOffset(float gunNode2LeftOffset) {
		this.gunNode2LeftOffset = gunNode2LeftOffset;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [ ")
			.append("avatarName=\"").append(getAvatarName())
			.append("\", avatarFileName=\"").append(getAvatarFileName())
			.append("\", avatarSkeletalFileName=\"").append(getAvatarSkeletalFileName())
			.append("\", avatarSkeletalMeshFileName=\"").append(getAvatarSkeletalMeshFileName())
			.append("\", avatarAnimationFileName=\"").append(getAvatarAnimationFileName())
			.append("\", avatarHeightOffset=\"").append(getAvatarHeightOffset())
		.append("\" ]");
		return sb.toString();
	}
}
