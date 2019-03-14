package a3;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.io.IOException;

import myGameEngine.controller.CameraController1P;
import myGameEngine.mesh.GroundPlane;
import net.java.games.input.Controller;
import ray.input.GenericInputManager;
import ray.input.InputManager;
import ray.rage.Engine;
import ray.rage.game.Game;
import ray.rage.game.VariableFrameRateGame;
import ray.rage.rendersystem.RenderSystem;
import ray.rage.rendersystem.RenderWindow;
import ray.rage.rendersystem.gl4.GL4RenderSystem;
import ray.rage.scene.Camera;
import ray.rage.scene.Camera.Frustum.Projection;
import ray.rage.scene.Light;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;
import ray.rml.Vector3f;

public class MyGame extends VariableFrameRateGame {
	
	private float gameTime;
	
	public static final float PLAYER_SPEED = 0.025f;
	public static final float LOOK_SPEED = 0.75f;
	
	public static final String LIGHT_NAME = "Lamp1";
	public static final String LIGHT_NODE_NAME = "Lamp1Node";
	
	public static final String CAMERA_NAME = "MainCamera";
	public static final String CAMERA_NODE_NAME = "MainCameraNode";
	
	public static final String GROUND_PLANE_NAME = "GroundPlane";
	public static final String GROUND_PLANE_NODE_NAME = "GroundPlaneNode";
	
	private static final String HUD_BASE = "Game Time: ";
	
	private InputManager im;

	public MyGame() {
		super();
	}
	
	public static void main(String[] args) {
		final Game game = new MyGame();
		try {
			game.startup();
			game.run();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			game.shutdown();
			game.exit();
		}
	}
	
	@Override
	protected void setupWindow(RenderSystem rs, GraphicsEnvironment ge) {
		System.out.println("Initializing Window...");
		
		rs.createRenderWindow(new DisplayMode(1000, 700, 24, 60), false);
		//rs.createRenderWindow(true);
	}

	@Override
	protected void setupCameras(SceneManager sm, RenderWindow rw) {
		System.out.println("\nInitializing Camera(s)...");
		
		final SceneNode rootNode = sm.getRootSceneNode();
		final Camera camera = sm.createCamera(CAMERA_NAME, Projection.PERSPECTIVE);
		rw.getViewport(0).setCamera(camera);
		
		camera.setRt((Vector3f)Vector3f.createFrom(1.0f, 0.0f, 0.0f));
		camera.setUp((Vector3f)Vector3f.createFrom(0.0f, 1.0f, 0.0f));
		camera.setFd((Vector3f)Vector3f.createFrom(0.0f, 0.0f, -1.0f));
		camera.setPo((Vector3f)Vector3f.createFrom(0.0f, 2.0f, 0.0f));
		
		final SceneNode cameraNode = rootNode.createChildSceneNode(CAMERA_NODE_NAME);
		cameraNode.attachObject(camera);
	}

	@Override
	protected void setupScene(Engine eng, SceneManager sm) throws IOException {
		System.out.println("Initializing Scene...");
		
		setupObjects(sm);
		setupLights(sm);
		try {
			setupInputs(sm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void update(Engine eng) {
		final GL4RenderSystem rs = (GL4RenderSystem) eng.getRenderSystem();
		
		gameTime += eng.getElapsedTimeMillis();
		
		rs.setHUD(HUD_BASE + ((((int)((gameTime / 1000.0d) * 10))/10.0d)), 15, 15);
		
		im.update(gameTime);
	}
	
	private void setupObjects(SceneManager sm) {
		// setup ground plane
        final GroundPlane groundPlane = new GroundPlane(GROUND_PLANE_NAME, getEngine(), sm);
        final SceneNode groundPlaneN = sm.getRootSceneNode().createChildSceneNode(GROUND_PLANE_NODE_NAME);
        groundPlaneN.scale(50.0f, 50.0f, 50.0f);
        groundPlaneN.attachObject(groundPlane.getManualObject());
	}
	
	private void setupLights(SceneManager sm) {
		System.out.println("Initializing Lighting...");
		
    	sm.getAmbientLight().setIntensity(new Color(.7f, .7f, .7f));
    	
    	final Light plight = sm.createLight(LIGHT_NAME, Light.Type.POINT);
		plight.setAmbient(new Color(.3f, .3f, .3f));
        plight.setDiffuse(new Color(.7f, .7f, .7f));
		plight.setSpecular(new Color(1.0f, 1.0f, 1.0f));
        plight.setRange(100.0f);
		
        final SceneNode plightNode = sm.getRootSceneNode().createChildSceneNode(LIGHT_NODE_NAME);
        plightNode.attachObject(plight);
    }
	
	private void setupInputs(SceneManager sm) throws AWTException {
		System.out.println("Initializing Inputs...");
		
		this.im = new GenericInputManager();
		System.out.println("\tEnumerating Input Devices...");
    	for (Controller c : im.getControllers()) {
    		System.out.println("\t\t" + c.getName() + "\t|\tType: " + c.getType());
    	}
    	
    	CameraController1P cameraController1P = new CameraController1P(sm, im, CAMERA_NAME, CAMERA_NODE_NAME);
    	sm.getRenderSystem().getRenderWindow().addMouseListener(cameraController1P);
    	sm.getRenderSystem().getRenderWindow().addMouseMotionListener(cameraController1P);
	}
}
