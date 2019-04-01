package a3.network.api.messages;

import java.util.UUID;

import a3.network.server.impl.ServerProtocol;

public interface Message {
	
	public UUID getUUID();
	public void setUUID(UUID uuid);
	
	public MessageType getMessageType();
	public void setMessageType(MessageType messageType);

	public String getFromIP();
	public void setFromIP(String fromIP);
	
	public String getToIP();
	public void setToIP(String toIP);
	
	public Integer getFromPort();
	public void setFromPort(Integer fromPort);
	
	public Integer getToPort();
	public void setToPort(Integer toPort);
	
	public ServerProtocol getProtocol();
	public void setProtocol(ServerProtocol protocol);
	
	public String getFromName();
	public void setFromName(String fromName);
	
	public String getToName();
	public void setToName(String toName);
	
	public String toMessageString();
	
}
