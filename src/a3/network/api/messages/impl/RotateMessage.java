package a3.network.api.messages.impl;

import a3.network.api.messages.BasicMessage;
import a3.network.api.messages.MessageType;
import a3.network.server.Rotation;

public class RotateMessage extends BasicMessage {

	private static final long serialVersionUID = -2239458239753012074L;
	
	private Rotation rotation;
	
	public RotateMessage() {
		super.setMessageType(MessageType.ROTATE);
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
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString().replace(" ]", ", ")).append(rotation).append(" ]");
		return sb.toString();
	}

}
