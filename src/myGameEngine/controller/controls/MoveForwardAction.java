package myGameEngine.controller.controls;

import a3.MyGame;
import a3.network.client.GameClient;
import myGameEngine.util.ArrayUtils;
import net.java.games.input.Event;
import ray.input.action.Action;
import ray.rage.scene.SceneNode;
import ray.rml.Matrix4;
import ray.rml.Matrix4f;
import ray.rml.Vector3;

public class MoveForwardAction implements Action {
	
	private final SceneNode node;
	private final GameClient gameClient;
	
	public MoveForwardAction(SceneNode node, GameClient gameClient) {
		this.node = node;
		this.gameClient = gameClient;
	}

	@Override
	public void performAction(float time, Event evt) {
		float movement = 0.0f;
		final String c = evt.getComponent().getName().toLowerCase();
		if (c.contentEquals("w")) {
			movement = evt.getValue() * MyGame.PLAYER_SPEED;
		}
		else if (c.contentEquals("s")) {
			movement = -1.0f * evt.getValue() * MyGame.PLAYER_SPEED;
		}
		else if (c.contentEquals("y axis")) { // xbox needs y axis reversed
			if (evt.getValue() < -0.2f) {
				movement = -1.0f * evt.getValue() * MyGame.PLAYER_SPEED;
			}
			else if (evt.getValue() > 0.2f) {
				movement = -1.0f * evt.getValue() * MyGame.PLAYER_SPEED;
			}
		}
		
		final Vector3 forward = this.node.getLocalForwardAxis();
		final float xForce = forward.x() == 0 ? 0 : forward.x() * movement;
		final float yForce = forward.y() == 0 ? 0 : forward.y() * movement;
		final float zForce = forward.z() == 0 ? 0 : forward.z() * movement;
		
		this.node.getPhysicsObject().applyForce(xForce, yForce, zForce, 0, 0, 0);
		
		final Matrix4 mat = Matrix4f.createFrom(ArrayUtils.toFloatArray(this.node.getPhysicsObject().getTransform()));
		this.node.setLocalPosition(mat.value(0, 3), mat.value(1, 3), mat.value(2, 3));
		
		this.gameClient.getGame().mechrunAnimate(this.gameClient.getGame().getEngine());
		this.gameClient.getGame().updateVerticalPosition();
		this.gameClient.sendMoveMessage(this.node.getLocalPosition());
	}

}
