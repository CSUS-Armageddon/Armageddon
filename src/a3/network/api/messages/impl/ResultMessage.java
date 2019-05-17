package a3.network.api.messages.impl;

import java.util.UUID;

import a3.network.api.messages.BasicMessage;
import a3.network.api.messages.MessageType;

public class ResultMessage extends BasicMessage {

	private static final long serialVersionUID = 8905626130875262908L;
	
	private UUID winnerUUID;
	
	public ResultMessage() {
		super.setMessageType(MessageType.RESULT);
	}

	/**
	 * @return the winnerUUID
	 */
	public UUID getWinnerUUID() {
		return winnerUUID;
	}

	/**
	 * @param winnerUUID the winnerUUID to set
	 */
	public void setWinnerUUID(UUID winnerUUID) {
		this.winnerUUID = winnerUUID;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString().replace(" ]", ", "))
		.append("winner=\"").append(winnerUUID.toString()).append(" ]");
		return sb.toString();
	}

}
