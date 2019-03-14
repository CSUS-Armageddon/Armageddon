package myGameEngine.controller.controls;

import net.java.games.input.Event;
import ray.input.action.Action;
import ray.rage.scene.SceneNode;
import ray.rml.Degreef;

public class YawAction implements Action {
	
	private final SceneNode node;
	
	public YawAction(SceneNode node) {
		this.node = node;
	}

	@Override
	public void performAction(float time, Event evt) {
		float movement = 0.0f;
		final String c = evt.getComponent().getName().toLowerCase();
		if (c.contentEquals("q")) {
			movement = evt.getValue();
		}
		else if (c.contentEquals("e")) {
			movement = -1.0f * evt.getValue();
		}
		else if (c.contentEquals("button 4")) {
			movement = evt.getValue();
		}
		else if (c.contentEquals("button 5")) {
			movement = -1.0f * evt.getValue();
		}
		this.node.yaw(Degreef.createFrom(movement));
	}

}
