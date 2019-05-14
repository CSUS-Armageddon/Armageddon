package myGameEngine.controller;

import net.java.games.input.Component.Identifier.Axis;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import ray.input.InputManager;
import ray.input.InputManager.INPUT_ACTION_TYPE;
import ray.input.action.AbstractInputAction;
import ray.input.action.Action;
import ray.rage.scene.SceneNode;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class Camera3PController {
	
	private static final float ORBIT_SPEED = 0.75f;

	private final SceneNode cameraN; // the node the camera is attached to
	private final SceneNode target; // the target the camera looks at
	private final InputManager im; // the game input manager for this camera
	private final InputType inputType; // this controller type
	
	private float cameraAzimuth; // rotation of camera around Y axis
	private float cameraElevation; // elevation of camera above target
	private float radias; // distance between camera and target
	private Vector3 worldUpVec; // the world's up vector
	
	private static final float MAX_ZOOM_OUT = 150.0f;
	private static final float MAX_ZOOM_IN = 50.0f;
	
	public Camera3PController(SceneNode cameraN, SceneNode target, InputType inputType, InputManager im) {
		this.cameraN = cameraN;
		this.target = target;
		this.im = im;
		this.inputType = inputType;
		
		this.cameraAzimuth = 225.0f; // start from BEHIND and ABOVE the target
		this.cameraElevation = 20.0f; // elevation is in degrees
		this.radias = 75.0f;
		this.worldUpVec = Vector3f.createFrom(0.0f, 1.0f, 0.0f); // Y is UP
		setupInput(this.im, this.inputType);
		updateCameraPosition();
	}
	
	public void updateCameraPosition() {
		final double theta = Math.toRadians(this.cameraAzimuth); // rot around the target
		final double phi = Math.toRadians(this.cameraElevation); // altitude angle
		final double x = this.radias * Math.cos(phi) * Math.sin(theta);
		final double y = this.radias * Math.sin(phi);
		final double z = this.radias * Math.cos(phi) * Math.cos(theta);
		this.cameraN.setLocalPosition(Vector3f.createFrom((float)x, (float)y, (float)z).add(this.target.getWorldPosition()));
		this.cameraN.lookAt(this.target, this.worldUpVec);
	}
	
	private void setupInput(final InputManager im, final InputType inputType) {
		final Action orbitAroundAction = new OrbitAroundAction();
		OrbitRadiansAction orbitRadiansAction = new OrbitRadiansAction();
		OrbitElevationAction orbitElevationAction = new OrbitElevationAction();
		
		if (inputType == InputType.MOUSE) {
			for (Controller controller : im.getControllers()) {
				if (controller.getType() == Controller.Type.MOUSE
						&& (controller.getName().toLowerCase().contains("mouse")
								|| controller.getName().toLowerCase().contains("touchpad")
								|| controller.getName().toLowerCase().contains("deathadder"))) {
					im.associateAction(controller, Axis.X, orbitAroundAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
					im.associateAction(controller, Axis.Y, orbitElevationAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
					im.associateAction(controller, Axis.Z, orbitRadiansAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				}
			}
		}
		else if (inputType == InputType.GAMEPAD) {
			for (Controller controller : im.getControllers()) {
				if (controller.getType() == Controller.Type.GAMEPAD
						&& controller.getName().toLowerCase().contains("controller")) {
					im.associateAction(controller, Axis.RX, orbitAroundAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
					im.associateAction(controller, Axis.RY, orbitElevationAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
					im.associateAction(controller, Axis.Z, orbitRadiansAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				}
			}
		}
	}
	
	/**
	 * Moves the camera around the target (changes camera azimuth).
	 *
	 */
	private class OrbitAroundAction extends AbstractInputAction {
		@Override
		public void performAction(float time, Event evt) {
			float rotAmount;
			if (evt.getValue() < -0.2f) {
				rotAmount = -1.0f * ORBIT_SPEED;
			} else if (evt.getValue() > 0.2f) {
				rotAmount = ORBIT_SPEED;
			} else {
				rotAmount = 0.0f;
			}
			cameraAzimuth += rotAmount;
			cameraAzimuth = cameraAzimuth % 360;
			updateCameraPosition();
		}
	}
	
	/**
	 * Moves the camera closer or further from the target (changes camera radius) - aka, zoom
	 * 
	 * Note, XBox controller Z axis zooms way too quickly, so there's a special case
	 * to make it slower below
	 *
	 */
	private class OrbitRadiansAction extends AbstractInputAction {
		@Override
		public void performAction(float time, Event evt) {
			float zoomAmount;
			if (evt.getValue() > 0.2f) {
				if (evt.getComponent().getName().toLowerCase().contentEquals("z axis")) {
					// for xbox controller
					zoomAmount = 0.01f;
				} else {
					zoomAmount = -1.0f * ORBIT_SPEED;
				}
			} else if (evt.getValue() < -0.2f) {
				if (evt.getComponent().getName().toLowerCase().contentEquals("z axis")) {
					// for xbox controller
					zoomAmount = -1.0f * 0.01f;
				} else {
					zoomAmount = ORBIT_SPEED;
				}
			} else {
				zoomAmount = 0.0f;
			}
			
			final float check = radias + zoomAmount;
			if ((check < MAX_ZOOM_IN) || (check > MAX_ZOOM_OUT)) return;
			
			radias = check;
			radias = radias % 360;
			updateCameraPosition();
		}
	}
	
	/**
	 * Moves the camera elevation
	 *
	 */
	private class OrbitElevationAction extends AbstractInputAction {
		@Override
		public void performAction(float time, Event evt) {
			float elevateAmount;
			if (evt.getValue() > 0.2f) {
				elevateAmount = -1.0f * ORBIT_SPEED;
			} else if (evt.getValue() < -0.2f) {
				elevateAmount = ORBIT_SPEED;
			} else {
				elevateAmount = 0.0f;
			}
			
			final float check = cameraElevation + elevateAmount;
			if ((check < 0.0f) || (check > 75.0f)) return;
			
			cameraElevation = check;
			cameraElevation = cameraElevation % 360;
			updateCameraPosition();
		}
	}
	
}
