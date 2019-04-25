package a3.editor.controller.controls;

import a3.MyGame;
import a3.editor.MyGameEditor;
import a3.editor.avatar.PlaceableAvatar;
import net.java.games.input.Event;
import ray.input.action.Action;
import ray.rage.rendersystem.Renderable.Primitive;
import ray.rage.scene.Entity;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;

public class PlaceAvatarAction implements Action {

	private final SceneManager sm;
	private final MyGameEditor editor;
	
	public PlaceAvatarAction(SceneManager sm, MyGameEditor editor) {
		this.sm = sm;
		this.editor = editor;
	}
	
	@Override
	public void performAction(float time, Event evt) {
		try {
			// get object group
			final SceneNode sceneObjectGroup = 
					(SceneNode) sm.getRootSceneNode().getChild(MyGameEditor.SCENE_OBJECTS_NODE_GROUP);
			
			// new object being created
			editor.incrementObjectCount();
			final Entity en = 
					sm.createEntity("Object_" + editor.getObjectCount() + "_Entity", editor.getAvatar().getAvatarFileName());
			en.setPrimitive(Primitive.TRIANGLES);
			
			// new node for this object
			final SceneNode newObjectNode = 
					(SceneNode) sceneObjectGroup.createChildNode("Object_" + editor.getObjectCount() + "_Node");
			
			// attach object to node
			newObjectNode.attachObject(en);
			
			// clone attributes of current player node
			final SceneNode playerNode = (SceneNode) sm.getRootSceneNode().getChild(MyGame.PLAYER_NODE_NAME);
			newObjectNode.setLocalPosition(playerNode.getLocalPosition());
			newObjectNode.setLocalRotation(playerNode.getLocalRotation());
			newObjectNode.scale(playerNode.getLocalScale());
			
			// record object
			editor.addPlaceableAvatar("Object_" + editor.getObjectCount() + "_Entity", (PlaceableAvatar)editor.getAvatar());
		} catch (Exception e) {
			e.printStackTrace(); // don't crash no matter what... please...
		}
	}
	
}
