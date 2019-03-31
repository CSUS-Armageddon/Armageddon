package a3.network.api.messages;

import java.io.IOException;

import a3.network.api.messages.impl.CreateMessage;

public enum MessageType {

	CREATE(CreateMessage.class),
	MOVE(null),
	ROTATE(null),
	DETAILS(null),
	HANGUP(null),
	;
	
	private MessageType(Class<?> unmarshalClass) {
		this.unmarshalClass = unmarshalClass;
	}
	
	private final Class<?> unmarshalClass;
	
	public Class<?> getUnmarshalClass() {
		return this.unmarshalClass;
	}
	
	public static MessageType getMessageType(String message) throws IOException {
		if (null == message || message.trim().equals("")) {
            throw new IOException("Input is null or empty!");
        }
		
		for (MessageType type : MessageType.values()) {
			if (message.contains("MessageType=\"" + type + "\"")) {
				return type;
			}
		}
		
		return null;
	}
	
}
