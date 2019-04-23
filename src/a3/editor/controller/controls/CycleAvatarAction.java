package a3.editor.controller.controls;

import java.io.IOException;

import a3.MyGame;
import a3.editor.avatar.PlaceableAvatars;
import net.java.games.input.Event;
import ray.input.action.Action;
import ray.rage.rendersystem.Renderable.Primitive;
import ray.rage.scene.Entity;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;

public class CycleAvatarAction implements Action {

	private final SceneManager sm;
	private final SceneNode playerNode;
	
	private PlaceableAvatars[] placeableAvatars;
	private int index;
	
	public CycleAvatarAction(SceneManager sm) {
		this.sm = sm;
		this.playerNode = (SceneNode) sm.getRootSceneNode().getChild(MyGame.PLAYER_NODE_NAME);
	}
	
	@Override
	public void performAction(float time, Event evt) {
		if (placeableAvatars == null) {
			placeableAvatars = PlaceableAvatars.values();
			index = 0;
		}
		removeEntity();
		try {
			attachEntity(index++);
			if (index > placeableAvatars.length - 1) index = 0;
			if (index < 0) index = placeableAvatars.length - 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void removeEntity() {
		final Entity en = (Entity) this.playerNode.detachObject(MyGame.PLAYER_NAME);
		sm.destroyEntity(en);
	}
	
	private void attachEntity(int index) throws IOException {
		final Entity en = 
				sm.createEntity(
						MyGame.PLAYER_NAME,
						placeableAvatars[index].getPlaceableAvatar().getAvatarFileName());
		en.setPrimitive(Primitive.TRIANGLES);
		this.playerNode.attachObject(en);
	}
	
}
