package a3.editor.controller.controls;

import a3.MyGame;
import a3.editor.MyGameEditor;
import net.java.games.input.Event;
import ray.input.action.Action;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;

public class VerticalAvatarAction implements Action {

	private final MyGameEditor editor;
	private final SceneNode playerNode;
	
	public VerticalAvatarAction(SceneManager sm, MyGameEditor editor) {
		this.editor = editor;
		this.playerNode = (SceneNode) sm.getRootSceneNode().getChild(MyGame.PLAYER_NODE_NAME);
	}
	
	@Override
	public void performAction(float time, Event evt) {
		final String key = evt.getComponent().getName();
		if (key.contentEquals("I")) {
			// on the way up...
			this.playerNode.moveUp(0.1f);
		} else if (key.contentEquals("K")) {
			// going down...
			this.playerNode.moveUp(-0.1f);
		}
		editor.getAvatar().setAvatarHeightOffset(this.playerNode.getLocalPosition().y());
	}
	
}
