package a3.network.server;

import java.util.UUID;

import a3.network.api.messages.impl.CreateMessage;
import a3.network.api.messages.impl.DetailsMessage;
import a3.network.api.messages.impl.HangupMessage;
import a3.network.api.messages.impl.JoinMessage;
import a3.network.api.messages.impl.MoveMessage;
import a3.network.api.messages.impl.RotateMessage;
import ray.rml.Vector3;

public interface Server {

	public void sendJoinMessage(UUID uuid, boolean success);
	public void handleJoinMessage(JoinMessage jm);
	
	public void sendCreateMessage(UUID uuid, Vector3 playerPosition);
	public void handleCreateMessage(CreateMessage cm);
	
	public void sendMoveMessage(UUID uuid, Vector3 worldPosition);
	public void handleMoveMessage(MoveMessage mm);
	
	public void sendRotateMessage(UUID uuid);
	public void handleRotateMessage(RotateMessage rm);
	
	public void sendDetailsMessage(UUID uuid);
	public void handleDetailsMessage(DetailsMessage dm);
	
	public void sendHangupMessage(UUID uuid);
	public void handleHangupMessage(HangupMessage hm);
	
}