package a3.editor;

import a3.MyGame;
import a3.avatar.Avatar;
import a3.avatar.Avatars;
import a3.editor.controller.controls.CycleAvatarAction;
import net.java.games.input.Controller;
import net.java.games.input.Component.Identifier.Key;
import net.java.games.input.Controller.Type;
import ray.input.InputManager.INPUT_ACTION_TYPE;
import ray.rage.scene.SceneManager;

public class MyGameEditor extends MyGame {
	
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
	protected void setupInputs(SceneManager sm) {
		super.setupInputs(sm);
		
		final CycleAvatarAction cycleAvatarAction = new CycleAvatarAction(sm);
		
		for (Controller c : super.im.getControllers()) {
			if (c.getType() == Type.KEYBOARD && c.getName().toUpperCase().contains("KEYBOARD")) {	
		    	// cycle avatars using iterator backward/forward
		    	im.associateAction(c, Key.U, cycleAvatarAction, INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		    	im.associateAction(c, Key.O, cycleAvatarAction, INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		    	
		    	
			}
		}
	}

}
