package a3.network.api.messages.impl;

import a3.network.api.messages.BasicMessage;
import a3.network.api.messages.MessageType;

public class EndGameMessage extends BasicMessage {

	private static final long serialVersionUID = 1859824928331225151L;

	public EndGameMessage() {
		super.setMessageType(MessageType.END);
	}
	
}
