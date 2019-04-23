package a3.editor.controller.controls;

import a3.MyGame;
import net.java.games.input.Event;
import ray.input.action.Action;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;

public class ScaleAvatarAction implements Action {

	private final SceneNode playerNode;
	
	public ScaleAvatarAction(SceneManager sm) {
		this.playerNode = (SceneNode) sm.getRootSceneNode().getChild(MyGame.PLAYER_NODE_NAME);
	}
	
	@Override
	public void performAction(float time, Event evt) {
		final String key = evt.getComponent().getName();
		try {
			if (key.contentEquals("J")) {
				// scale down
				this.playerNode.scale(0.99f, 0.99f, 0.99f);
			} else if (key.contentEquals("L")) {
				// scale up
				this.playerNode.scale(1.01f, 1.01f, 1.01f);
			}
		} catch (Exception e) {
			e.printStackTrace(); // just in case we eventually divide by zero
		}
	}
	
}
