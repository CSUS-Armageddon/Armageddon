package a3.network.api.messages.impl;

import a3.network.api.messages.BasicMessage;
import a3.network.api.messages.MessageType;

public class ScoreMessage extends BasicMessage {

	private static final long serialVersionUID = 8565667712153050334L;

	private long currentScore;
	
	public ScoreMessage() {
		super.setMessageType(MessageType.SCORE);
	}

	/**
	 * @return the currentScore
	 */
	public long getCurrentScore() {
		return currentScore;
	}

	/**
	 * @param currentScore the currentScore to set
	 */
	public void setCurrentScore(long currentScore) {
		this.currentScore = currentScore;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString().replace(" ]", ", "))
		.append("currentScore=\"").append(currentScore).append(" ]");
		return sb.toString();
	}
	
}
