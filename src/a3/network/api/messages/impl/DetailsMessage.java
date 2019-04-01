package a3.network.api.messages.impl;

import a3.network.api.messages.BasicMessage;
import a3.network.api.messages.MessageType;

public class DetailsMessage extends BasicMessage {

	private static final long serialVersionUID = 2896350189251391096L;
	
	public DetailsMessage() {
		super.setMessageType(MessageType.DETAILS);
	}

}
