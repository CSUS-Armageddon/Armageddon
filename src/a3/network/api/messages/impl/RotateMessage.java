package a3.network.api.messages.impl;

import a3.network.api.messages.BasicMessage;
import a3.network.api.messages.MessageType;

public class RotateMessage extends BasicMessage {

	private static final long serialVersionUID = -2239458239753012074L;
	
	public RotateMessage() {
		super.setMessageType(MessageType.ROTATE);
	}

}
