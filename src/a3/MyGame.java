package a3;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import a3.avatar.Avatar;
import a3.editor.avatar.PlaceableAvatar;
import a3.network.api.messages.MessageType;
import a3.network.client.GameClient;
import a3.network.client.GhostAvatar;
import a3.network.logging.ClientLogger;
import myGameEngine.asset.JavaScriptLoader;
import myGameEngine.asset.ScriptAsset;
import myGameEngine.asset.ScriptManager;
import myGameEngine.controller.Camera3PController;
import myGameEngine.controller.InputType;
import myGameEngine.controller.controls.MoveForwardAction;
import myGameEngine.controller.controls.MoveLeftAction;
import myGameEngine.controller.controls.YawAction;
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
import ray.physics.PhysicsEngine;
import ray.physics.PhysicsEngineFactory;
import ray.physics.PhysicsObject;
import ray.rage.Engine;
import ray.rage.asset.texture.Texture;
import ray.rage.asset.texture.TextureManager;
import ray.rage.rendersystem.states.RenderState;
import ray.rage.game.VariableFrameRateGame;
import ray.rage.rendersystem.RenderSystem;
import ray.rage.rendersystem.RenderWindow;
import ray.rage.rendersystem.Renderable.Primitive;
import ray.rage.rendersystem.gl4.GL4RenderSystem;
import ray.rage.rendersystem.states.TextureState;
import ray.rage.scene.Camera;
import ray.rage.scene.Camera.Frustum.Projection;
import ray.rage.scene.Entity;
import ray.rage.scene.Light;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;
import ray.rage.scene.SkeletalEntity;
import ray.rage.scene.SkyBox;
import ray.rage.scene.Tessellation;
import ray.rage.util.Configuration;
import ray.rml.Degreef;
import ray.rml.Matrix3;
import ray.rml.Vector3;
import ray.rml.Vector3f;

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
	
	public static final float PLAYER_SPEED = 2.025f;
	public static final float LOOK_SPEED = 0.75f;
	
	public static final String LIGHT_NAME = "Lamp1";
	public static final String LIGHT_NODE_NAME = "Lamp1Node";
	
	public static final String CAMERA_NAME = "MainCamera";
	public static final String CAMERA_NODE_NAME = "MainCameraNode";
	
	public static final String PLAYER_NAME = "Player";
	public static final String PLAYER_NODE_NAME = "PlayerNode";
	
	private static final String HUD_BASE = "Game Time: ";
	
	public static final String SCENE_OBJECTS_NODE_GROUP = "SCENE_OBJECTS";
	
	private static final String PHYSICS_ENGINE_CLASS = "ray.physics.JBullet.JBulletPhysicsEngine";
	private static final float[] GRAVITY = { 0.0f, -3.0f, 0.0f };
	
	private PhysicsEngine physicsEngine;
	
	private SkeletalEntity playerE;
	
	protected final Map<String, PlaceableAvatar> placeableAvatarMap = new HashMap<String, PlaceableAvatar>();
	
	protected InputManager im;
	
	private ScriptManager scriptManager;
	private ScriptEngine jsEngine;
	private Invocable invocableEngine;
	private ScriptAsset groundPlaneScript;
	private ScriptAsset terrainScript;
	private ScriptAsset skyboxScript;
	private ScriptAsset buildingScript;
	private ScriptAsset sceneScript;
	
	private final boolean isFullScreen;
	
	private Avatar avatar;

	public MyGame(String serverAddress, int serverPort, boolean isFullScreen, Avatar avatar) {
		super();
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.serverProtocol = ProtocolType.UDP;
		this.isFullScreen = isFullScreen;
		this.avatar = avatar;
	}
	
	public void init() {
		try {
			startup();
			run();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shutdown();
			//exit();
		}
	}
	
	@Override
	public void startup() {
		ClientLogger.INSTANCE.addFilter(MessageType.MOVE);
		ClientLogger.INSTANCE.addFilter(MessageType.ROTATE);
		ClientLogger.INSTANCE.addFilter(MessageType.REQUEST);
		ClientLogger.INSTANCE.addFilter(MessageType.DETAILS);
		setupNetworking();
		super.startup();
	}
	
	@Override
	protected void update(Engine eng) {
		final GL4RenderSystem rs = (GL4RenderSystem) eng.getRenderSystem();
		
		gameTime += eng.getElapsedTimeMillis();
		
		rs.setHUD(HUD_BASE + ((((int)((gameTime / 1000.0d) * 10))/10.0d)), 15, 15);
		
		SkeletalEntity mechSE =
				(SkeletalEntity) eng.getSceneManager().getEntity(PLAYER_NAME);
		mechSE.update();
		
		im.update(gameTime);
		cameraController.updateCameraPosition();
		processNetworking(gameTime);
	}
	
	@Override
	protected void setupWindow(RenderSystem rs, GraphicsEnvironment ge) {
		
		System.out.println("Initializing Window...");
		
		if (isFullScreen) {
			rs.createRenderWindow(true);
		} else {
			rs.createRenderWindow(new DisplayMode(1000, 700, 24, 60), false);
		}
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
        playerCamera.getFrustum().setFarClipDistance(10000.0f);
	}
	
	private void setupOrbitCamera(Engine eng, SceneManager sm) {
		final SceneNode abovePN = sm.getRootSceneNode().createChildSceneNode("AbovePlayerN");
    	playerN.attachChild(abovePN);
    	abovePN.moveUp(1.0f);
    	cameraController = new Camera3PController(playerCameraN, abovePN, InputType.MOUSE, im);
	}

	@Override
	protected void setupScene(Engine eng, SceneManager sm) throws IOException {
		System.out.println("Initializing Scene...");
		
		setupScripting();
		setupSkybox(sm);
		setupGroundPlane(sm);
		setupTerrain(sm);
		setupObjects(sm);
		initPhysicsSystem();
		createRAGEPhysicsWorld(sm);
		setupLights(sm);
		try {
			setupInputs(sm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setupOrbitCamera(eng, sm);
	}
	
	private void setupScripting() {
		System.out.println("Initializing Scripting...");
		
		try {
			this.scriptManager = new ScriptManager();
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
			e1.printStackTrace();
		}
		this.scriptManager.addAssetLoader(new JavaScriptLoader());
		this.scriptManager.setBaseDirectoryPath(getEngine().getConfiguration().valueOf("assets.scripts.path"));
		
		final ScriptEngineManager factory = new ScriptEngineManager();
		this.jsEngine = factory.getEngineByName("js");
		
		try {
			this.groundPlaneScript = this.scriptManager.getAssetByPath("GroundPlane.js");
			this.terrainScript = this.scriptManager.getAssetByPath("Terrain.js");
			this.skyboxScript = this.scriptManager.getAssetByPath("Skybox.js");
			this.buildingScript = this.scriptManager.getAssetByPath("Buildings.js");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			this.sceneScript = this.scriptManager.getAssetByPath("SceneGenerator.js");
		} catch (IOException e) {
			System.out.println("SceneGenerator.js Does Not Exist! Run Game Editor first!");
		}
		
		if (this.groundPlaneScript != null) initScript(this.groundPlaneScript.getScriptFile());
		if (this.terrainScript != null) initScript(this.terrainScript.getScriptFile());
		if (this.skyboxScript != null) initScript(this.skyboxScript.getScriptFile());
		if (this.buildingScript != null) initScript(this.buildingScript.getScriptFile());
		if (this.sceneScript != null) initScript(this.sceneScript.getScriptFile());
		
		this.jsEngine.put("placeableAvatarMap", placeableAvatarMap);
		
		this.invocableEngine = (Invocable)jsEngine;
	}
	
	protected void setupObjects(SceneManager sm) throws IOException {
		
		// create root node for all scene objects
		sm.getRootSceneNode().createChildSceneNode(MyGame.SCENE_OBJECTS_NODE_GROUP);
		
		//invokeScript("configureBuildings", sm);
		invokeScript("generateSceneObjects", sm, MyGame.SCENE_OBJECTS_NODE_GROUP);
        
        // player 1
		//replace parameters with avatar.getrkmfile, and avatar.getrksfile
		final SkeletalEntity playerE = sm.createSkeletalEntity(PLAYER_NAME, avatar.getAvatarSkeletalMeshFileName(), avatar.getAvatarSkeletalFileName());
    	//final Entity playerE = sm.createEntity(PLAYER_NAME, avatar.getAvatarFileName());
		Texture tex = sm.getTextureManager().getAssetByPath(avatar.getAvatarTextureFileName());
		TextureState tstate = (TextureState)sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
		tstate.setTexture(tex);
		playerE.setRenderState(tstate);
    	playerE.setPrimitive(Primitive.TRIANGLES);
        playerN = sm.getRootSceneNode().createChildSceneNode(PLAYER_NODE_NAME);
        playerN.moveLeft(5.0f);
        playerN.moveUp(7.0f);
		playerN.yaw(Degreef.createFrom(90.0f));
        playerN.attachObject(playerE);
        
        //replace with avatar.getrunanimation for second parameter
        playerE.loadAnimation("runAnimation", avatar.getAvatarAnimationFileName());
        
        this.updateVerticalPosition();
	}
	
	public void mechrunAnimate(Engine eng)
	{ 	SkeletalEntity manSE =
		(SkeletalEntity) eng.getSceneManager().getEntity(PLAYER_NAME);
		manSE.stopAnimation();
		manSE.playAnimation("runAnimation", 0.5f, SkeletalEntity.EndType.LOOP, 0);
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
	
	protected void setupInputs(SceneManager sm) {
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
    	
    	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				new SendCloseConnectionPacketAction().performAction(0, null);
			}
    	}, "Shutdown-Thread"));
    	
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
			final Entity ghostE = this.getEngine().getSceneManager().createEntity(avatar.getUUID().toString(), avatar.getAvatar() == null ? "cube.obj" : avatar.getAvatar().getAvatarFileName());
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
		final AffineTransform xform = new AffineTransform();
		final SkyBox sb = sm.createSkyBox("SKYBOX");
        
		invokeScript("configureSkybox", tm, sm, sb, conf, xform);
		
	}
	
	private void setupTerrain(SceneManager sm) {
        
		invokeScript("configureTerrain", this.getEngine());
	}
	
	private void setupGroundPlane(SceneManager sm) {
		
		invokeScript("configureGroundPlane", this.getEngine());
	}
	
	public void updateVerticalPosition() {
		final SceneNode playerN = this.getEngine().getSceneManager().getSceneNode(PLAYER_NODE_NAME);
		final SceneNode tessN = this.getEngine().getSceneManager().getSceneNode("tessN");
		final Tessellation tessE = ((Tessellation)tessN.getAttachedObject("tessE"));
		
		// figure out Avatar's position relative to plane
		final Vector3 worldAvatarPosition = playerN.getWorldPosition();
		final Vector3 localAvatarPosition = playerN.getLocalPosition();
		
		// use avatar _WORLD_ coordinates to _get_ coordinated from height
		final Vector3 newAvatarPosition = Vector3f.createFrom(
					// Keep the X coordinate
					localAvatarPosition.x(),
					// The Y coordinate is the varying height
					tessE.getWorldHeight(worldAvatarPosition.x(), worldAvatarPosition.z()) + this.avatar.getAvatarHeightOffset(),
					// Keep the Z coordinate
					localAvatarPosition.z()
				);
		// use avatar _LOCAL_ coordinates to _set_ position, including height
		playerN.setLocalPosition(newAvatarPosition);
	}

	/**
	 * @return the avatar
	 */
	public Avatar getAvatar() {
		return avatar;
	}
	
	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}
	
	private void initScript(File scriptFile) {
		// setup buildings as configured in script
 		try (FileReader fileReader = new FileReader(scriptFile)) {
 			jsEngine.eval(fileReader);
 		} catch (FileNotFoundException e) {
 			e.printStackTrace();
 		} catch (IOException e) {
 			e.printStackTrace();
 		} catch (ScriptException e) {
 			e.printStackTrace();
 		} catch (NullPointerException e) {
 			e.printStackTrace();
 		}
	}
	
	private void invokeScript(String func, Object ... args) {
		try {
        	this.invocableEngine.invokeFunction(func, args);
        } catch (ScriptException e) {
        	e.printStackTrace();
        } catch (NoSuchMethodException e) {
        	e.printStackTrace();
        } catch (NullPointerException e) {
        	e.printStackTrace();
        }
	}
	
	private void initPhysicsSystem() {
		this.physicsEngine = PhysicsEngineFactory.createPhysicsEngine(MyGame.PHYSICS_ENGINE_CLASS);
		this.physicsEngine.initSystem();
		this.physicsEngine.setGravity(MyGame.GRAVITY);
	}
	
	private void createRAGEPhysicsWorld(SceneManager sm) {
		final float mass = 1.0f;
		final float[] up = { 0.0f, 1.0f, 0.0f };
		final SceneNode groundPlaneN = sm.getSceneNode("GroundPlaneN");
		
		double[] temptf;
		
		temptf = toDoubleArray(groundPlaneN.getLocalTransform().toFloatArray());
		final PhysicsObject groundPlaneP = 
				physicsEngine.addStaticPlaneObject(physicsEngine.nextUID(), temptf, up, 0.0f);
		groundPlaneP.setBounciness(1.0f);
		groundPlaneN.setPhysicsObject(groundPlaneP);
	}
	
	private double[] toDoubleArray(float[] arr) {
		if (arr == null) return null;
		int n = arr.length;
		double[] ret = new double[n];
		for (int i = 0; i < n; i++) {
			ret[i] = (double)arr[i];
		}
		return ret;
	}
}
