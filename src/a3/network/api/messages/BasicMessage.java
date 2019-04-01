package a3.network.api.messages;

import java.io.Serializable;
import java.util.UUID;

import a3.network.client.GameClient;
import a3.network.server.impl.ServerProtocol;
import ray.networking.client.IClientSocket;

public abstract class BasicMessage implements Message, Serializable {

	private static final long serialVersionUID = -4895699512814380066L;

	private MessageType messageType;
	
	private UUID uuid;
	
	private String fromName;
	private String fromIP;
	private Integer fromPort;
	private ServerProtocol fromProtocol;
	
	private String toName;
	private String toIP;
	private Integer toPort;
	private ServerProtocol toProtocol;
	
	
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
	
	public ServerProtocol getFromProtocol() {
		return this.fromProtocol;
	}
	
	public void setFromProtocol(ServerProtocol fromProtocol) {
		this.fromProtocol = fromProtocol;
	}
	
	public ServerProtocol getToProtocol() {
		return this.toProtocol;
	}
	
	public void setToProtocol(ServerProtocol toProtocol) {
		this.toProtocol = toProtocol;
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
	
	/**
	 * initializes all default message packet data
	 */
	public void init(GameClient gameClient) {
		final IClientSocket ics = gameClient.getSocketInfo();
		
		setUUID(gameClient.getUUID());
		
		setFromName(ics.getLocalAddress().toString());
		setFromIP(ics.getInetAddress().toString());
		setFromPort(ics.getLocalPort());
		setFromProtocol(ServerProtocol.UDP);
		
		setToName(ics.getRemoteSocketAddress().toString());
		setToIP(ics.getRemoteSocketAddress().toString());
		//setToPort(ics.getRemoteSocketAddress().toString());
		setToProtocol(ServerProtocol.UDP);
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [ ")
			.append("MessageType=\"").append(getMessageType()).append("\", UUID=\"").append(getUUID())
			.append("\", FromName=\"").append(getFromName()).append("\", FromIP=\"").append(getFromIP()).append("\", FromPort=\"").append(getFromPort()).append("\", FromProtocol=\"").append(getFromProtocol())
			.append("\", ToName=\"").append(getToName()).append("\", ToIP=\"").append(getToIP()).append("\", ToPort=\"").append(getToPort()).append("\", ToProtocol=\"").append(getToProtocol())
		.append("\" ]");
		return sb.toString();
	}
	
}
