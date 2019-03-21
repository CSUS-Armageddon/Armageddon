package a3.server.api.messages.impl;

import java.io.IOException;

import a3.server.Position;
import a3.server.api.messages.BasicMessage;
import a3.server.api.messages.MessageType;

public class CreateMessage extends BasicMessage {

	private Position position;
	
	public CreateMessage() {
		super.setMessageType(MessageType.CREATE);
	}

	/**
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Position position) {
		this.position = position;
	}
	
	@Override
	public CreateMessage fromMessageString(String message) {
		try {
			
			if (null == message || message.contentEquals("")) {
				throw new IOException("Message cannot be null or empty!");
			}
			
			if (MessageType.getMessageType(message) != MessageType.CREATE) {
				throw new IOException("Message is not MessageType.CREATE!");
			}
			
			// if we're here, message is valid, so unmarshal it
			final String cleanString = cleanMessageString(message);
			final String[] parts = cleanString.split(",");
			
			this.setMessageType(MessageType.getMessageType(message));
			this.setFromName(getContent(parts[MessageParts.FromName]));
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return this;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString().replace(" ]", ", ")).append(position).append(" ]");
		return sb.toString();
	}
	
	private enum MessageParts {
		MessageType(0),
		FromName(1),
		FromIP(2),
		FromPort(3),
		FromProtocol(4),
		ToName(5),
		ToIP(6),
		ToPort(7),
		ToProtocol(8),
		Position(9),
		;
		
		private MessageParts(int place) {
			this.place = place;
		}
		
		private final int place;
		
		public int getPlace() {
			return this.place;
		}
	}
	
}
