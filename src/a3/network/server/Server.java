package a3.network.server;

import a3.network.api.messages.impl.CreateMessage;
import a3.network.api.messages.impl.DetailsMessage;
import a3.network.api.messages.impl.HangupMessage;
import a3.network.api.messages.impl.JoinMessage;
import a3.network.api.messages.impl.MoveMessage;
import a3.network.api.messages.impl.RotateMessage;
import ray.rml.Vector3;

public interface Server {

	public void sendJoinMessage();
	public void handleJoinMessage(JoinMessage jm);
	
	public void sendCreateMessage(Vector3 playerPosition);
	public void handleCreateMessage(CreateMessage cm);
	
	public void sendMoveMessage(Vector3 worldPosition);
	public void handleMoveMessage(MoveMessage mm);
	
	public void sendRotateMessage();
	public void handleRotateMessage(RotateMessage rm);
	
	public void sendDetailsMessage();
	public void handleDetailsMessage(DetailsMessage dm);
	
	public void sendHangupMessage();
	public void handleHangupMessage(HangupMessage hm);
	
}
