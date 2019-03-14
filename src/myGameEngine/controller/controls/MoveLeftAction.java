package myGameEngine.controller.controls;

import a3.MyGame;
import net.java.games.input.Event;
import ray.input.action.Action;
import ray.rage.scene.SceneNode;

public class MoveLeftAction implements Action {
	
	private final SceneNode node;
	
	public MoveLeftAction(SceneNode node) {
		this.node = node;
	}

	@Override
	public void performAction(float time, Event evt) {
		float movement = 0.0f;
		final String c = evt.getComponent().getName().toLowerCase();
		if (c.contentEquals("d")) {
			movement = evt.getValue() * MyGame.PLAYER_SPEED;
		}
		else if (c.contentEquals("a")) {
			movement = -1.0f * evt.getValue() * MyGame.PLAYER_SPEED;
		}
		else if (c.contentEquals("x axis")) {
			if (evt.getValue() < -0.2f) {
				movement = evt.getValue() * MyGame.PLAYER_SPEED;
			}
			else if (evt.getValue() > 0.2f) {
				movement = evt.getValue() * MyGame.PLAYER_SPEED;
			}
		}
		this.node.moveLeft(movement);
	}

}
