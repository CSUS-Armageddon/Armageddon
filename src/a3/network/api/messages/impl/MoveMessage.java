package a3.network.api.messages.impl;

import a3.network.api.Position;
import a3.network.api.messages.BasicMessage;
import a3.network.api.messages.MessageType;

public class MoveMessage extends BasicMessage {

	private static final long serialVersionUID = 2262483951601551067L;
	
	private Position position;
	
	public MoveMessage() {
		super.setMessageType(MessageType.MOVE);
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
