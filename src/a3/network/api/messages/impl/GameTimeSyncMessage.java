package a3.network.api.messages.impl;

import a3.network.api.messages.BasicMessage;
import a3.network.api.messages.MessageType;

public class GameTimeSyncMessage extends BasicMessage {

	private static final long serialVersionUID = -5901382185615114412L;
	
	private long gameTime;
	
	public GameTimeSyncMessage() {
		super.setMessageType(MessageType.TIME);
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
		.append("time=\"").append(getGameTime()).append(" ]");
		return sb.toString();
	}

}
