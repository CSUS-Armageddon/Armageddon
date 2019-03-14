package myGameEngine.controller.controls;

import a3.MyGame;
import net.java.games.input.Event;
import ray.input.action.Action;
import ray.rage.scene.SceneNode;
import ray.rml.Degreef;

public class LookLeftAction implements Action {
	
	private final SceneNode node;
	
	public LookLeftAction(SceneNode node) {
		this.node = node;
	}

	@Override
	public void performAction(float time, Event evt) {
		float leftAmount;
		if (evt.getValue() < -0.2f) {
			leftAmount = -1.0f * MyGame.LOOK_SPEED;
		} else if (evt.getValue() > 0.2f) {
			leftAmount = MyGame.LOOK_SPEED;
		} else {
			leftAmount = 0.0f;
		}
		node.yaw(Degreef.createFrom(leftAmount));
	}
	
}
