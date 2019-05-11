package a3.network.api.messages.impl;

import java.util.UUID;

import a3.network.api.Position;
import a3.network.api.messages.BasicMessage;
import a3.network.api.messages.MessageType;

public class HeightMessage extends BasicMessage {

	private static final long serialVersionUID = -3846714569743604938L;
	
	private float height;
	private Position position;
	private UUID requestor;
	private Direction direction;
	
	public HeightMessage() {
		super.setMessageType(MessageType.HEIGHT);
	}

	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(float height) {
		this.height = height;
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
	 * @return the requestor
	 */
	public UUID getRequestor() {
		return requestor;
	}

	/**
	 * @param requestor the requestor to set
	 */
	public void setRequestor(UUID requestor) {
		this.requestor = requestor;
	}

	/**
	 * @return the direction
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString().replace(" ]", ", ")).append(", direction=\"").append(direction).append(", height=\"").append(height).append("\"")
		.append(", requestor=\"").append(requestor).append("\", ").append(position).append(" ]");
		return sb.toString();
	}
	
	public enum Direction {
		REQUEST,
		RECEIVE,
		;
	}

}
