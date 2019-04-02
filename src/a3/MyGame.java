package a3;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.UnknownHostException;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

import a3.network.api.messages.MessageType;
import a3.network.client.GameClient;
import a3.network.client.GhostAvatar;
import a3.network.logging.ClientLogger;
import myGameEngine.controller.Camera3PController;
import myGameEngine.controller.InputType;
import myGameEngine.controller.controls.MoveForwardAction;
import myGameEngine.controller.controls.MoveLeftAction;
import myGameEngine.controller.controls.YawAction;
import myGameEngine.mesh.GroundPlane;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.Component.Identifier.Axis;
import net.java.games.input.Component.Identifier.Button;
import net.java.games.input.Component.Identifier.Key;
import net.java.games.input.Controller.Type;
import ray.input.GenericInputManager;
import ray.input.InputManager;
import ray.input.InputManager.INPUT_ACTION_TYPE;
import ray.input.action.AbstractInputAction;
import ray.networking.IGameConnection.ProtocolType;
import ray.rage.Engine;
import ray.rage.asset.texture.Texture;
import ray.rage.asset.texture.TextureManager;
import ray.rage.game.Game;
import ray.rage.game.VariableFrameRateGame;
import ray.rage.rendersystem.RenderSystem;
import ray.rage.rendersystem.RenderWindow;
import ray.rage.rendersystem.Renderable.Primitive;
import ray.rage.rendersystem.gl4.GL4RenderSystem;
import ray.rage.scene.Camera;
import ray.rage.scene.Camera.Frustum.Projection;
import ray.rage.scene.Entity;
import ray.rage.scene.Light;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;
import ray.rage.scene.SkyBox;
import ray.rage.util.Configuration;
import ray.rml.Matrix3;
import ray.rml.Vector3;

public class MyGame extends VariableFrameRateGame {
	
	private String serverAddress;
	private int serverPort;
	private ProtocolType serverProtocol;
	private GameClient gameClient;
	private boolean isClientConnected;
	private Vector<UUID> gameObjectsToRemove;
	
	private Camera3PController cameraController;
	private SceneNode playerN;
	private Camera playerCamera;
	private SceneNode playerCameraN;
	
	private float gameTime;
	
	public static final float PLAYER_SPEED = 0.025f;
	public static final float LOOK_SPEED = 0.75f;
	
	public static final String LIGHT_NAME = "Lamp1";
	public static final String LIGHT_NODE_NAME = "Lamp1Node";
	
	public static final String CAMERA_NAME = "MainCamera";
	public static final String CAMERA_NODE_NAME = "MainCameraNode";
	
	public static final String PLAYER_NAME = "Player";
	public static final String PLAYER_NODE_NAME = "PlayerNode";
	
	public static final String GROUND_PLANE_NAME = "GroundPlane";
	public static final String GROUND_PLANE_NODE_NAME = "GroundPlaneNode";
	
	private static final String HUD_BASE = "Game Time: ";
	
	private InputManager im;

	public MyGame(String serverAddress, int serverPort) {
		super();
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.serverProtocol = ProtocolType.UDP;
	}
	
	public static void main(String[] args) {
		final Game game = new MyGame(args[0], Integer.parseInt(args[1]));
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
	public void startup() {
		ClientLogger.INSTANCE.addFilter(MessageType.MOVE);
		ClientLogger.INSTANCE.addFilter(MessageType.ROTATE);
		setupNetworking();
		super.startup();
	}
	
	@Override
	protected void update(Engine eng) {
		final GL4RenderSystem rs = (GL4RenderSystem) eng.getRenderSystem();
		
		gameTime += eng.getElapsedTimeMillis();
		
		rs.setHUD(HUD_BASE + ((((int)((gameTime / 1000.0d) * 10))/10.0d)), 15, 15);
		
		im.update(gameTime);
		cameraController.updateCameraPosition();
		processNetworking(gameTime);
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
		
		/*final SceneNode rootNode = sm.getRootSceneNode();
		final Camera camera = sm.createCamera(CAMERA_NAME, Projection.PERSPECTIVE);
		rw.getViewport(0).setCamera(camera);
		
		camera.setRt((Vector3f)Vector3f.createFrom(1.0f, 0.0f, 0.0f));
		camera.setUp((Vector3f)Vector3f.createFrom(0.0f, 1.0f, 0.0f));
		camera.setFd((Vector3f)Vector3f.createFrom(0.0f, 0.0f, -1.0f));
		camera.setPo((Vector3f)Vector3f.createFrom(0.0f, 2.0f, 0.0f));
		
		final SceneNode cameraNode = rootNode.createChildSceneNode(CAMERA_NODE_NAME);
		cameraNode.attachObject(camera);
		*/
		final SceneNode rootNode = sm.getRootSceneNode();
		
		playerCamera = sm.createCamera(CAMERA_NAME, Projection.PERSPECTIVE);
        rw.getViewport(0).setCamera(playerCamera);
        playerCameraN = rootNode.createChildSceneNode(CAMERA_NODE_NAME);
        playerCameraN.attachObject(playerCamera);
        playerCamera.setMode('n');
        playerCamera.getFrustum().setFarClipDistance(1000.0f);
	}
	
	private void setupOrbitCamera(Engine eng, SceneManager sm) {
		final SceneNode abovePN = sm.getRootSceneNode().createChildSceneNode("AbovePlayerN");
    	playerN.attachChild(abovePN);
    	abovePN.moveUp(0.25f);
    	cameraController = new Camera3PController(playerCameraN, abovePN, InputType.MOUSE, im);
	}

	@Override
	protected void setupScene(Engine eng, SceneManager sm) throws IOException {
		System.out.println("Initializing Scene...");
		
		setupSkybox(sm);
		setupObjects(sm);
		setupLights(sm);
		try {
			setupInputs(sm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setupOrbitCamera(eng, sm);
	}
	
	private void setupObjects(SceneManager sm) throws IOException {
		// setup ground plane
        final GroundPlane groundPlane = new GroundPlane(GROUND_PLANE_NAME, getEngine(), sm);
        final SceneNode groundPlaneN = sm.getRootSceneNode().createChildSceneNode(GROUND_PLANE_NODE_NAME);
        groundPlaneN.scale(50.0f, 50.0f, 50.0f);
        groundPlaneN.attachObject(groundPlane.getManualObject());
        
        
        // player 1
    	final Entity playerE = sm.createEntity(PLAYER_NAME, "dolphinHighPoly.obj");
    	playerE.setPrimitive(Primitive.TRIANGLES);
        playerN = sm.getRootSceneNode().createChildSceneNode(PLAYER_NODE_NAME);
        playerN.moveLeft(5.0f);
        playerN.moveUp(0.3f);
        playerN.attachObject(playerE);
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
    	
    	// build some action objects for doing things in response to user input
    	final MoveForwardAction p1MoveForwardAction = new MoveForwardAction(playerN, gameClient);
    	final MoveLeftAction p1MoveLeftAction = new MoveLeftAction(playerN, gameClient);
    	final YawAction p1YawAction = new YawAction(playerN, gameClient);
    	
    	// because gaming keyboards having "n-key rollover" typically emulate multiple keyboards,
    	// we must look for all "KeyBoard" devices and bind to them...
    	for (Controller c : im.getControllers()) {
    		/************************************
        	 * Keyboard Bindings
        	 ***********************************/
    		if (c.getType() == Type.KEYBOARD && c.getName().toUpperCase().contains("KEYBOARD")) {
		    	
		    	// yaw left
		    	im.associateAction(c, Key.Q, p1YawAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	// yaw right
		    	im.associateAction(c, Key.E, p1YawAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	
		    	// move backward
		    	im.associateAction(c, Key.S, p1MoveForwardAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	// move forward
		    	im.associateAction(c, Key.W, p1MoveForwardAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	
		    	// move left
		    	im.associateAction(c, Key.A, p1MoveLeftAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	// move right
		    	im.associateAction(c, Key.D, p1MoveLeftAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	
		    	im.associateAction(c, Key.ESCAPE, new SendCloseConnectionPacketAction(), INPUT_ACTION_TYPE.ON_PRESS_ONLY);
    			
    			
    		} else if (c.getType() == Type.GAMEPAD) {
    			/************************************
    	    	 * Gamepad Bindings
    	    	 ***********************************/
		    	
		    	// yaw left / yaw right
		    	im.associateAction(c, Button._4, p1YawAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	im.associateAction(c, Button._5, p1YawAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	
		    	// move backward / forward
		    	im.associateAction(c, Axis.Y, p1MoveForwardAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		    	
		    	// move left / right
		    	im.associateAction(c, Axis.X, p1MoveLeftAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    		}
    	}
    	
    	/*Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				new SendCloseConnectionPacketAction().performAction(0, null);
			}
    	}, "Shutdown-Thread"));
    	*/
	}
	
	private void setupNetworking() {
		System.out.println("Initializing Networking...");
		
		gameObjectsToRemove = new Vector<UUID>();
		isClientConnected = false;
		
		try {
			gameClient = new GameClient(InetAddress.getByName(serverAddress), serverPort, serverProtocol, this, InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (gameClient == null) {
			System.out.println("Missing Game Host");
		} else {
			// send join message
			gameClient.sendJoinMessage();
		}
	}
	
	protected void processNetworking(float gameTime) {
		// process packets received by the client from the server
		if (gameClient != null) {
			gameClient.processPackets();
		}
		
		// remove ghost avatars for players who have left the game
		Iterator<UUID> it = gameObjectsToRemove.iterator();
		while (it.hasNext()) {
			this.getEngine().getSceneManager().destroySceneNode(it.next().toString());
		}
		gameObjectsToRemove.clear();
	}
	
	public Vector3 getPlayerPosition() {
		final SceneNode node = this.getEngine().getSceneManager().getSceneNode(PLAYER_NODE_NAME);
		return node.getLocalPosition();
	}
	
	public Matrix3 getPlayerRotation() {
		final SceneNode node = this.getEngine().getSceneManager().getSceneNode(PLAYER_NODE_NAME);
		return node.getLocalRotation();
	}
	
	public void addGhostAvatar(GhostAvatar avatar) throws IOException {
		if (avatar != null) {
			final Entity ghostE = this.getEngine().getSceneManager().createEntity(avatar.getUUID().toString(), "cube.obj");
			ghostE.setPrimitive(Primitive.TRIANGLES);
			final SceneNode ghostN = this.getEngine().getSceneManager().getRootSceneNode().createChildSceneNode(avatar.getUUID().toString());
			ghostN.attachObject(ghostE);
			ghostN.setLocalPosition(avatar.getPosition());
			avatar.setNode(ghostN);
			avatar.setEntity(ghostE);
			avatar.setPosition(ghostN.getLocalPosition());
		}
	}
	
	public void removeGhostAvatar(GhostAvatar avatar) {
		if (avatar != null) {
			gameObjectsToRemove.add(avatar.getUUID());
		}
	}
	
	public boolean isClientConnected() {
		return this.isClientConnected;
	}
	
	public void setClientConnected(boolean isConnected) {
		this.isClientConnected = isConnected;
	}
	
	private class SendCloseConnectionPacketAction extends AbstractInputAction {
		// for leaving the game... need to attach to an input device
		@Override
		public void performAction(float time, Event evt) {
			if (gameClient != null && isClientConnected) {
				gameClient.sendHangupMessage();
			}
		}
	}
	
	private void setupSkybox(SceneManager sm) throws IOException {
		final Configuration conf = this.getEngine().getConfiguration();
		final TextureManager tm = this.getEngine().getTextureManager();
		tm.setBaseDirectoryPath(conf.valueOf("assets.skyboxes.path"));
		final Texture front = tm.getAssetByPath("posz.jpg");
		final Texture back = tm.getAssetByPath("negz.jpg");
		final Texture left = tm.getAssetByPath("negx.jpg");
		final Texture right = tm.getAssetByPath("posx.jpg");
		final Texture top = tm.getAssetByPath("posy.jpg");
		final Texture bottom = tm.getAssetByPath("negy.jpg");
		tm.setBaseDirectoryPath(conf.valueOf("assets.textures.path"));
		
		// cubemap textures are flipped upside down
		// all textures must hav ethe same dimensions, so any image's
		// height will work since they are all the same height
		
		AffineTransform xform = new AffineTransform();
		xform.translate(0, front.getImage().getHeight());
		xform.scale(1d, -1d);
		
		front.transform(xform);
		back.transform(xform);
		left.transform(xform);
		right.transform(xform);
		top.transform(xform);
		bottom.transform(xform);
		
		final SkyBox sb = sm.createSkyBox("SKYBOX");
		sb.setTexture(front, SkyBox.Face.FRONT);
		sb.setTexture(back, SkyBox.Face.BACK);
		sb.setTexture(left, SkyBox.Face.LEFT);
		sb.setTexture(right, SkyBox.Face.RIGHT);
		sb.setTexture(top, SkyBox.Face.TOP);
		sb.setTexture(bottom, SkyBox.Face.BOTTOM);
		
		sm.setActiveSkyBox(sb);
		
	}
}
