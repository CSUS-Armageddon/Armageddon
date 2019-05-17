package a3.network.api.messages;

import java.io.IOException;

public enum MessageType {

	JOIN,
	CREATE,
	MOVE,
	ROTATE,
	REQUEST,
	DETAILS,
	HANGUP,
	SHOOT,
	TIME,
	START,
	END,
	SCORE,
	RESULT,
	;
	
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
