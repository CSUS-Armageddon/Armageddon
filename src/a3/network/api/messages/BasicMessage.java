package a3.network.api.messages;

import java.io.Serializable;
import java.util.UUID;

import a3.network.server.impl.ServerProtocol;

public abstract class BasicMessage implements Message, Serializable {

	private static final long serialVersionUID = -4895699512814380066L;

	private MessageType messageType;
	private ServerProtocol protocol;
	
	private UUID uuid;
	
	private String fromName;
	private String fromIP;
	private Integer fromPort;
	
	private String toName;
	private String toIP;
	private Integer toPort;
	
	
	public MessageType getMessageType() {
		return this.messageType;
	}
	
	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	public String getFromIP() {
		return this.fromIP;
	}
	
	public void setFromIP(String fromIP) {
		this.fromIP = fromIP;
	}
	
	public String getToIP() {
		return this.toIP;
	}
	
	public void setToIP(String toIP) {
		this.toIP = toIP;
	}
	
	public Integer getFromPort() {
		return this.fromPort;
	}
	
	public void setFromPort(Integer fromPort) {
		this.fromPort = fromPort;
	}
	
	public Integer getToPort() {
		return this.toPort;
	}
	
	public void setToPort(Integer toPort) {
		this.toPort = toPort;
	}
	
	public ServerProtocol getProtocol() {
		return this.protocol;
	}
	
	public void setProtocol(ServerProtocol protocol) {
		this.protocol = protocol;
	}
	
	public String getFromName() {
		return this.fromName;
	}
	
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	
	public String getToName() {
		return this.toName;
	}
	
	public void setToName(String toName) {
		this.toName = toName;
	}
	
	@Override
	public String toMessageString() {
		return toString();
	}
	
	public String cleanMessageString(String message) {
		return message.replace(getClass().getSimpleName() + " [ ", "").replace(" ] ]", " ]").replaceAll(", ", ",");
	}
	
	public String getContent(String keyValue) {
		if (null == keyValue || keyValue.contentEquals("") || !keyValue.contains("=")) {
			return null;
		}
		return keyValue.split("=")[1].replace("=\"", "").replace("\"", "");
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [ ")
			.append("MessageType=\"").append(getMessageType()).append("\", UUID=\"").append(getUUID())
			.append("\", FromName=\"").append(getFromName()).append("\", FromIP=\"").append(getFromIP()).append("\", FromPort=\"").append(getFromPort()).append("\", Protocol=\"").append(getProtocol())
			.append("\", ToName=\"").append(getToName()).append("\", ToIP=\"").append(getToIP()).append("\", ToPort=\"").append(getToPort())
		.append("\" ]");
		return sb.toString();
	}
	
}
