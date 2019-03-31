package a3.network.server.api.messages.impl;

import a3.network.server.Position;
import a3.network.server.api.messages.BasicMessage;
import a3.network.server.api.messages.MessageType;

public class CreateMessage extends BasicMessage {

	private Position position;
	
	public CreateMessage() {
		super.setMessageType(MessageType.CREATE);
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
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString().replace(" ]", ", ")).append(position).append(" ]");
		return sb.toString();
	}
	
}
