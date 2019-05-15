package a3.network.client;

import a3.network.api.messages.impl.CreateMessage;
import a3.network.api.messages.impl.DetailsMessage;
import a3.network.api.messages.impl.HangupMessage;
import a3.network.api.messages.impl.JoinMessage;
import a3.network.api.messages.impl.MoveMessage;
import a3.network.api.messages.impl.RequestMessage;
import a3.network.api.messages.impl.RotateMessage;
import a3.network.api.messages.impl.ShootMessage;
import ray.rml.Matrix3;
import ray.rml.Vector3;

public interface Client {

	public void sendJoinMessage();
	public void handleJoinMessage(JoinMessage jm);
	
	public void sendCreateMessage(Vector3 playerPosition, Matrix3 playerRotation, String avatarName);
	public void handleCreateMessage(CreateMessage cm);
	
	public void sendMoveMessage(Vector3 localPosition);
	public void handleMoveMessage(MoveMessage mm);
	
	public void sendRotateMessage(Matrix3 localRotation);
	public void handleRotateMessage(RotateMessage rm);
	
	public void handleRequestMessage(RequestMessage rm);
	
	public void sendDetailsMessage(Vector3 localPosition, Matrix3 localRotation, String avatarName);
	public void handleDetailsMessage(DetailsMessage dm);
	
	public void sendHangupMessage();
	public void handleHangupMessage(HangupMessage hm);
	
	public void sendShootMessage(boolean avatarHasFired, Vector3 position, Vector3 forwardVector, float xForce, float yForce, float zForce);
	public void handleShootMessage(ShootMessage sm);
	
}
