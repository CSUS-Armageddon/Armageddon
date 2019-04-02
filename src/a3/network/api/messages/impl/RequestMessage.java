package a3.network.api.messages.impl;

import a3.network.api.messages.BasicMessage;
import a3.network.api.messages.MessageType;

public class RequestMessage extends BasicMessage {

	private static final long serialVersionUID = -8094886894704009895L;
	
	public RequestMessage() {
		super.setMessageType(MessageType.REQUEST);
	}

}
