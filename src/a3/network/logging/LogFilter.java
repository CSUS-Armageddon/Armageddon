package a3.network.logging;

import java.util.ArrayList;
import java.util.List;

import a3.network.api.messages.Message;
import a3.network.api.messages.MessageType;

public class LogFilter {
	
	private final List<MessageType> filterMessages = new ArrayList<MessageType>();
	
	public void addFilter(MessageType type) {
		this.filterMessages.add(type);
	}
	
	public void removeFilter(MessageType type) {
		this.filterMessages.remove(type);
	}
	
	public boolean filter(Message msg) {
		if (filterMessages.contains(msg.getMessageType())) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean filter(String msg) {
		for (MessageType t : filterMessages) {
			if (msg.toUpperCase().startsWith((t.toString().toUpperCase() + "MESSAGE"))) {
				return true;
			}
		}
		return false;
	}

}
