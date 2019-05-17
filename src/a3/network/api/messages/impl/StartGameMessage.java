package a3.network.api.messages.impl;

import a3.network.api.Position;
import a3.network.api.messages.BasicMessage;
import a3.network.api.messages.MessageType;

public class StartGameMessage extends BasicMessage {

	private static final long serialVersionUID = -6182248499482313513L;

	private Position gameZonePosition;
	private long gameTime;
	
	public StartGameMessage() {
		super.setMessageType(MessageType.START);
	}

	/**
	 * @return the gameZonePosition
	 */
	public Position getGameZonePosition() {
		return gameZonePosition;
	}

	/**
	 * @param gameZonePosition the gameZonePosition to set
	 */
	public void setGameZonePosition(Position gameZonePosition) {
		this.gameZonePosition = gameZonePosition;
	}
	
	/**
	 * @return the gameTime
	 */
	public long getGameTime() {
		return gameTime;
	}

	/**
	 * @param gameTime the gameTime to set
	 */
	public void setGameTime(long gameTime) {
		this.gameTime = gameTime;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString().replace(" ]", ", "))
		.append("time=\"").append(getGameTime()).append(", ").append(gameZonePosition).append(" ]");
		return sb.toString();
	}
	
}
