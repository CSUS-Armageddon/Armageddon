package myGameEngine.controller.controls;

import a3.MyGame;
import net.java.games.input.Event;
import ray.input.action.Action;
import ray.rage.scene.SceneNode;
import ray.rml.Degreef;

public class LookUpAction implements Action {
	
	private final SceneNode node;
	
	public LookUpAction(SceneNode node) {
		this.node = node;
	}

	@Override
	public void performAction(float time, Event evt) {
		System.out.println(evt.getComponent().getName() + " | " + evt.getValue());
		float upAmount;
		if (evt.getValue() < -0.2f) {
			upAmount = -1.0f * MyGame.LOOK_SPEED;
		} else if (evt.getValue() > 0.2f) {
			upAmount = MyGame.LOOK_SPEED;
		} else {
			upAmount = 0.0f;
		}
		node.pitch(Degreef.createFrom(upAmount));
	}
	
}
