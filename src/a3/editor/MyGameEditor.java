package a3.editor;

import java.io.IOException;

import a3.MyGame;
import a3.avatar.Avatar;
import a3.avatar.Avatars;
import a3.editor.controller.controls.CycleAvatarAction;
import a3.editor.controller.controls.PlaceAvatarAction;
import a3.editor.controller.controls.ScaleAvatarAction;
import a3.editor.controller.controls.VerticalAvatarAction;
import net.java.games.input.Controller;
import net.java.games.input.Component.Identifier.Key;
import net.java.games.input.Controller.Type;
import ray.input.InputManager.INPUT_ACTION_TYPE;
import ray.rage.scene.SceneManager;

public class MyGameEditor extends MyGame {
	
	public static String SCENE_OBJECTS_NODE_GROUP = "SCENE_OBJECTS";
	private int objectCount = 0;
	
	public static void main(String ... args) {
		new MyGameEditor(null, 6868, false, Avatars.MECH.getAvatar()).init();
	}

	public MyGameEditor(String serverAddress, int serverPort, boolean isFullScreen, Avatar avatar) {
		super(serverAddress, serverPort, isFullScreen, avatar);
	}
	
	@Override
	public void init() {
		try {
			startup();
			run();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shutdown();
			exit();
		}
	}
	
	@Override
	protected void setupObjects(SceneManager sm) throws IOException {
		super.setupObjects(sm);
		sm.getRootSceneNode().createChildSceneNode(MyGameEditor.SCENE_OBJECTS_NODE_GROUP);
	}
	
	@Override
	protected void setupInputs(SceneManager sm) {
		super.setupInputs(sm);
		
		final CycleAvatarAction cycleAvatarAction = new CycleAvatarAction(sm, this);
		final ScaleAvatarAction scaleAvatarAction = new ScaleAvatarAction(sm);
		final VerticalAvatarAction verticalAvatarAction = new VerticalAvatarAction(sm, this);
		final PlaceAvatarAction placeAvatarAction = new PlaceAvatarAction(sm, this);
		
		// hack to change avatars to first placeableAvatar
		cycleAvatarAction.performAction(0, null);
		
		for (Controller c : super.im.getControllers()) {
			if (c.getType() == Type.KEYBOARD && c.getName().toUpperCase().contains("KEYBOARD")) {	
		    	// cycle avatars using iterator backward/forward
		    	im.associateAction(c, Key.U, cycleAvatarAction, INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		    	im.associateAction(c, Key.O, cycleAvatarAction, INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		    	
		    	// scale avatars
		    	im.associateAction(c, Key.J, scaleAvatarAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	im.associateAction(c, Key.L, scaleAvatarAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	
		    	// up and down
		    	im.associateAction(c, Key.I, verticalAvatarAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	im.associateAction(c, Key.K, verticalAvatarAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	
		    	// place avatar
		    	im.associateAction(c, Key.SPACE, placeAvatarAction, INPUT_ACTION_TYPE.ON_PRESS_ONLY);
			}
		}
	}
	
	public void incrementObjectCount() {
		this.objectCount++;
	}
	
	public int getObjectCount() {
		return this.objectCount;
	}

}
