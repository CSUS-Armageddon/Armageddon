package myGameEngine.controller;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import a3.network.client.GameClient;
import myGameEngine.controller.controls.MoveForwardAction;
import myGameEngine.controller.controls.MoveLeftAction;
import myGameEngine.controller.controls.YawAction;
import net.java.games.input.Controller;
import net.java.games.input.Component.Identifier.Axis;
import net.java.games.input.Component.Identifier.Button;
import net.java.games.input.Component.Identifier.Key;
import net.java.games.input.Controller.Type;
import ray.input.InputManager;
import ray.input.InputManager.INPUT_ACTION_TYPE;
import ray.rage.rendersystem.RenderSystem;
import ray.rage.rendersystem.RenderWindow;
import ray.rage.rendersystem.Viewport;
import ray.rage.scene.Camera;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;
import ray.rml.Degreef;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class Camera1PController implements MouseListener, MouseMotionListener {
	
	private final GameClient gameClient;
	
	private final SceneManager sm;
	private final InputManager im;
	private final String cameraName;
	private final String cameraNodeName;
	
	private final Robot robot;
	private final Camera camera;
	
	private RenderSystem rs;
	private RenderWindow rw;
	
	private float prevMouseX, prevMouseY, curMouseX, curMouseY;
	private int centerX, centerY;
	private boolean isRecentering;
	
	public Camera1PController(SceneManager sm, InputManager im, String cameraName, String cameraNodeName, GameClient gameClient) throws AWTException {
		this.sm = sm;
		this.im = im;
		this.rs = sm.getRenderSystem();
		this.rw = this.rs.getRenderWindow();
		this.cameraName = cameraName;
		this.cameraNodeName = cameraNodeName;
		this.robot = new Robot();
		this.camera = sm.getCamera(this.cameraName);
		this.camera.setMode('c');
		this.gameClient = gameClient;
		init();
	}

	private void init() {
		final Toolkit tk = Toolkit.getDefaultToolkit();
    	final Cursor noCursor = tk.createCustomCursor(tk.getImage(""), new Point(), "InvisibleCursor");
    	sm.getRenderSystem().getCanvas().setCursor(noCursor);
    	
    	initMouseMode(rs, rw);
		
		final SceneNode cameraN = sm.getSceneNode(cameraNodeName);
    	
    	// build some action objects for doing things in response to user input
    	final MoveForwardAction moveForwardAction = new MoveForwardAction(cameraN, gameClient);
    	final MoveLeftAction moveLeftAction = new MoveLeftAction(cameraN, gameClient);
    	final YawAction yawAction = new YawAction(cameraN, gameClient);
    	//final LookUpAction lookUpAction = new LookUpAction(cameraN);
    	//final LookLeftAction lookLeftAction = new LookLeftAction(cameraN);
    	
    	// because gaming keyboards having "n-key rollover" typically emulate multiple keyboards,
    	// we must look for all "KeyBoard" devices and bind to them...
    	for (Controller c : im.getControllers()) {
    		/************************************
        	 * Keyboard Bindings
        	 ***********************************/
    		if (c.getType() == Type.KEYBOARD && c.getName().toUpperCase().contains("KEYBOARD")) {
	    		// yaw left
		    	im.associateAction(c, Key.Q, yawAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	// yaw right
		    	im.associateAction(c, Key.E, yawAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	
		    	// move backward
		    	im.associateAction(c, Key.S, moveForwardAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	// move forward
		    	im.associateAction(c, Key.W, moveForwardAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	
		    	// move left
		    	im.associateAction(c, Key.A, moveLeftAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	// move right
		    	im.associateAction(c, Key.D, moveLeftAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	
    		}
    		//else if (c.getType() == Type.MOUSE) {// && (c.getName().toUpperCase().contains("MOUSE") || c.getName().toUpperCase().contains("TOUCHPAD"))) {
			//	im.associateAction(c, Axis.X, lookUpAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			//	im.associateAction(c, Axis.Y, lookLeftAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    		//}
    		else if (c.getType() == Type.GAMEPAD) {
    			/************************************
    	    	 * Gamepad Bindings
    	    	 ***********************************/
		    	
		    	// yaw left / yaw right
		    	im.associateAction(c, Button._4, yawAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	im.associateAction(c, Button._5, yawAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	
		    	// move backward / forward
		    	im.associateAction(c, Axis.Y, moveForwardAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	
		    	// move left / right
		    	im.associateAction(c, Axis.X, moveLeftAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	
    		}
    	}
	}
	
	private void initMouseMode(RenderSystem rs, RenderWindow rw) {
		final Viewport v = rw.getViewport(0);
		int left = rw.getLocationLeft();
		int top = rw.getLocationTop();
		int widt = v.getActualScissorWidth();
		int hei = v.getActualScissorHeight();
		int centerX = left + widt/2;
		int centerY = top + hei/2;
		isRecentering = false;
		recenterMouse();
		prevMouseX = centerX; // 'prevMouse' defines the initial
		prevMouseY = centerY; // mouse position
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		// if robot is recentering and the MouseEvent location is in the center,
		// then this event was generated by the robot
		if (isRecentering && centerX == e.getXOnScreen() && centerY == e.getYOnScreen()) {
			isRecentering = false; // mouse recentered, recentering complete
		} else {
			// event was due to a user mouse-move, and must be processed
			curMouseX = e.getXOnScreen();
			curMouseY = e.getYOnScreen();
			float mouseDeltaX = prevMouseX - curMouseX;
			float mouseDeltaY = prevMouseY - curMouseY;
			yaw(mouseDeltaX);
			pitch(mouseDeltaY);
			prevMouseX = curMouseX;
			prevMouseY = curMouseY;
			// tell robot to put the cursor to the center (since user just moved it)
			recenterMouse();
			prevMouseX = centerX; //reset prev to center
			prevMouseY = centerY;
		}
	}
	
	private void recenterMouse() {
		// use the robot to move the mouse to the center point.
		// Note that this generates one MouseEvent.
		Viewport v = rw.getViewport(0);
		int left = rw.getLocationLeft();
		int top = rw.getLocationTop();
		int widt = v.getActualScissorWidth();
		int hei = v.getActualScissorHeight();
		centerX = left + widt/2;
		centerY = top + hei/2;
		isRecentering = true;
		robot.mouseMove((int)centerX, (int)centerY);
	}
	
	public void pitch(float mouseDeltaY) {
		float tilt;
		Vector3 f = camera.getFd();
		Vector3 r = camera.getRt();
		Vector3 u = camera.getUp();
		if (mouseDeltaY < 0.0) {
			tilt = -1.0f;
		} else if (mouseDeltaY > 0.0) {
			tilt = 1.0f;
		} else {
			tilt = 0.0f;
		}
		Vector3 fn = (f.rotate(Degreef.createFrom(0.1f * tilt), r)).normalize();
		Vector3 un = (u.rotate(Degreef.createFrom(0.1f * tilt), r)).normalize();
		camera.setFd((Vector3f)Vector3f.createFrom(fn.x(),fn.y(),fn.z()));
		camera.setUp((Vector3f)Vector3f.createFrom(un.x(),un.y(),un.z()));
	}
	
	public void yaw(float mouseDeltaX) {
		float yaw;
		Vector3 f = camera.getFd();
		Vector3 r = camera.getRt();
		Vector3 u = camera.getUp();
		if (mouseDeltaX < 0.0) {
			yaw = -1.0f;
		} else if (mouseDeltaX > 0.0) {
			yaw = 1.0f;
		} else {
			yaw = 0.0f;
		}
		Vector3 fn = (f.rotate(Degreef.createFrom(0.1f * yaw), r)).normalize();
		Vector3 rn = (r.rotate(Degreef.createFrom(0.1f * yaw), u)).normalize();
		camera.setFd((Vector3f)Vector3f.createFrom(fn.x(),fn.y(),fn.z()));
		camera.setRt((Vector3f)Vector3f.createFrom(rn.x(),rn.y(),rn.z()));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
