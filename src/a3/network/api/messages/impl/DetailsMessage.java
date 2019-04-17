package a3.network.api.messages.impl;

import a3.avatar.Avatar;
import a3.network.api.Position;
import a3.network.api.Rotation;
import a3.network.api.messages.BasicMessage;
import a3.network.api.messages.MessageType;

public class DetailsMessage extends BasicMessage {

	private static final long serialVersionUID = 2896350189251391096L;
	
	private Avatar avatar;
	private Position position;
	private Rotation rotation;
	
	public DetailsMessage() {
		super.setMessageType(MessageType.DETAILS);
	}

	/**
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/**
	 * @return the rotation
	 */
	public Rotation getRotation() {
		return rotation;
	}

	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation(Rotation rotation) {
		this.rotation = rotation;
	}
	
	/**
	 * @return the avatar
	 */
	public Avatar getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString().replace(" ]", ", "))
		.append(avatar).append(", ").append(position).append(", ").append(rotation).append(" ]");
		return sb.toString();
	}

}
