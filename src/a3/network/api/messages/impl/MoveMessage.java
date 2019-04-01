package a3.network.api.messages.impl;

import a3.network.api.messages.BasicMessage;
import a3.network.api.messages.MessageType;

public class MoveMessage extends BasicMessage {

	private static final long serialVersionUID = 2262483951601551067L;
	
	public MoveMessage() {
		super.setMessageType(MessageType.MOVE);
	}

}
