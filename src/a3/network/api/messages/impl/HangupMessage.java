package a3.network.api.messages.impl;

import a3.network.api.messages.BasicMessage;
import a3.network.api.messages.MessageType;

public class HangupMessage extends BasicMessage {

	private static final long serialVersionUID = 6440975442483891846L;
	
	public HangupMessage() {
		super.setMessageType(MessageType.HANGUP);
	}

}
