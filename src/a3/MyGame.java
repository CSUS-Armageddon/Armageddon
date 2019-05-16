package a3;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.UnknownHostException;
import java.util.ArrayList;
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
import a3.avatar.Avatars;
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
import myGameEngine.controller.controls.ShootAction;
import myGameEngine.controller.controls.YawAction;
import myGameEngine.util.ArrayUtils;
import net.java.games.input.Component;
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
import ray.rage.scene.Light;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;
import ray.rage.scene.SkeletalEntity;
import ray.rage.scene.SkyBox;
import ray.rage.scene.Tessellation;
import ray.rage.util.Configuration;
import ray.rml.Degreef;
import ray.rml.Matrix3;
import ray.rml.Matrix4;
import ray.rml.Matrix4f;
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
	
	public static final float PLAYER_SPEED = 500.0f;
	public static final float LOOK_SPEED = 0.75f;
	
	public static final String LIGHT_NAME = "Lamp1";
	public static final String LIGHT_NODE_NAME = "Lamp1Node";
	
	public static final String CAMERA_NAME = "MainCamera";
	public static final String CAMERA_NODE_NAME = "MainCameraNode";
	
	public static final String PLAYER_NAME = "Player";
	public static final String PLAYER_NODE_NAME = "PlayerNode";
	
	public static final String PLAYER_GUN_NODE1_NAME = "PlayerGunNode1";
	public static final String PLAYER_GUN_NODE2_NAME = "PlayerGunNode2";
	
	private static final String HUD_BASE = "Game Time: ";
	
	public static final String GROUNDPLANE_OBJECTS_NODE_GROUP = "GROUNDPLANE_OBJECTS";
	public static final String TERRAIN_OBJECTS_NODE_GROUP = "TERRAIN_OBJECTS";
	public static final String SCENE_OBJECTS_NODE_GROUP = "SCENE_OBJECTS";
	public static final String AVATAR_OBJECTS_NODE_GROUP = "AVATAR_OBJECTS";
	
	private static final String PHYSICS_ENGINE_CLASS = "ray.physics.JBullet.JBulletPhysicsEngine";
	private static final float[] GRAVITY = { 0.0f, -3000.0f, 0.0f };
	private static final float[] UP_VECTOR = { 0.0f, 1.0f, 0.0f };
	
	
	
	public float x;
	public float y;
	public float z;
	public float nextX;
	public float nextY;
	public float nextZ;
	
	public boolean isRunning = false; 
	public boolean animatePlaying = false;
	
	public boolean checkIfGhostMoveInitial = false;
	public boolean checkIfGhostMoveFinal = false;
	
	
	ArrayList<TrackGhostAvatars> trackAvatarList = new ArrayList<TrackGhostAvatars>();
	private PhysicsEngine physicsEngine;
	
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
		
		
		this.setX(this.getPlayerPosition().x());
		this.setY(this.getPlayerPosition().y());
		this.setZ(this.getPlayerPosition().z());
		
		//this.checkIfGhostMoved();
	//	System.out.println("*******************************************************");
		this.checkIfGhostMovedIntially();
		this.printTrackAvatarListInfo();
	//	System.out.println("*******************************************************");
		im.update(gameTime);
		
		
		
	
		cameraController.updateCameraPosition();
		processNetworking(gameTime);
		
		
	//	System.out.println("*******************************************************");
		this.checkIfGhostMovedFinal();
	//	this.printTrackAvatarListInfo();
	//	System.out.println("*******************************************************");
		
	//	System.out.println("*******************************************************");
		this.checkIfGhostMoved();
	//	this.printTrackAvatarListInfo();
	//	System.out.println("*******************************************************");
		
		
		for(int i =0 ; i<trackAvatarList.size(); i++) {
		SkeletalEntity mechGhost =
				(SkeletalEntity) eng.getSceneManager().getEntity(trackAvatarList.get(i).getEntityName());
		mechGhost.update();
		//System.out.println("playing animations");
		this.playGhostRunAnimation(eng);
		}
		physicsEngine.update(gameTime);
		for (SceneNode sn : eng.getSceneManager().getSceneNodes()) {
			if (sn.getPhysicsObject() != null) {
				final Matrix4 mat = Matrix4f.createFrom(ArrayUtils.toFloatArray(sn.getPhysicsObject().getTransform()));
				if (sn.getName().contentEquals(MyGame.PLAYER_NODE_NAME)) {
					sn.setLocalPosition(mat.value(0, 3), sn.getLocalPosition().y(), mat.value(2, 3));
				} else {
					sn.setLocalPosition(mat.value(0, 3), mat.value(1, 3), mat.value(2, 3));
				}
			}
		}
		this.setNextX(this.getPlayerPosition().x());
		this.setNextY(this.getPlayerPosition().y());
		this.setNextZ(this.getPlayerPosition().z());
		this.checkIfMoved();
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
        
        final Toolkit tk = Toolkit.getDefaultToolkit();
    	final Cursor noCursor = tk.createCustomCursor(tk.getImage(""), new Point(), "InvisibleCursor");
    	getEngine().getRenderSystem().getCanvas().setCursor(noCursor);
	}
	
	private void setupOrbitCamera(Engine eng, SceneManager sm) {
		final SceneNode abovePN = sm.getRootSceneNode().createChildSceneNode("AbovePlayerN");
    	playerN.attachChild(abovePN);
    	abovePN.moveUp(3.0f);
    	cameraController = new Camera3PController(playerCameraN, abovePN, InputType.MOUSE, im);
	}

	@Override
	protected void setupScene(Engine eng, SceneManager sm) throws IOException {
		System.out.println("Initializing Scene...");
		
		setupScripting();
		setupSceneNodeContainers(sm);
		setupSkybox(sm);
		setupGroundPlane(sm);
		setupTerrain(sm);
		setupObjects(sm);
		initPhysicsSystem();
		createRAGEPhysicsWorld(sm);
		setupLights(sm);
		this.updateVerticalPosition();
		try {
			setupInputs(sm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setupOrbitCamera(eng, sm);
	}
	
	private void setupSceneNodeContainers(SceneManager sm) {
		// ground plane nodes
		sm.getRootSceneNode().createChildSceneNode(MyGame.GROUNDPLANE_OBJECTS_NODE_GROUP);
		// root node for terrain
		sm.getRootSceneNode().createChildSceneNode(MyGame.TERRAIN_OBJECTS_NODE_GROUP);
		// create root node for all scene objects
		sm.getRootSceneNode().createChildSceneNode(MyGame.SCENE_OBJECTS_NODE_GROUP);
		// root node for all avatars
		sm.getRootSceneNode().createChildSceneNode(MyGame.AVATAR_OBJECTS_NODE_GROUP);
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
        playerN = sm.getSceneNode(MyGame.AVATAR_OBJECTS_NODE_GROUP).createChildSceneNode(PLAYER_NODE_NAME);
        playerN.moveLeft(5.0f);
        playerN.moveUp(17.0f);
		playerN.yaw(Degreef.createFrom(90.0f));
		
		playerN.scale(avatar.getScale(), avatar.getScale(), avatar.getScale());
		
        playerN.attachObject(playerE);
        
        final SceneNode playerGunNode1 = sm.getRootSceneNode().createChildSceneNode(PLAYER_GUN_NODE1_NAME);
        playerGunNode1.moveUp(avatar.getGunNode1HeightOffset());
        playerGunNode1.moveRight(avatar.getGunNode1RightOffset());
        playerN.attachChild(playerGunNode1);
        
        final SceneNode playerGunNode2 = sm.getRootSceneNode().createChildSceneNode(PLAYER_GUN_NODE2_NAME);
        playerGunNode2.moveUp(avatar.getGunNode2HeightOffset());
        playerGunNode2.moveLeft(avatar.getGunNode2LeftOffset());
        playerN.attachChild(playerGunNode2);
        
        //replace with avatar.getrunanimation for second parameter
        playerE.loadAnimation("runAnimation", avatar.getAvatarAnimationFileName());
        
	}
	
	public void mechrunAnimate(Engine eng)
	{ 	SkeletalEntity manSE =
	(SkeletalEntity) eng.getSceneManager().getEntity(PLAYER_NAME);
	
	if(this.getIsRunning() == true && this.getIsAnimated() == false ) {
		
	manSE.playAnimation("runAnimation", 0.5f, SkeletalEntity.EndType.LOOP, 0);
	this.setAnimateIsPlaying(true);
	} 
	
	if(this.getIsRunning() == false) {
		manSE.stopAnimation();
	}
	}
	
	public void playGhostRunAnimation(Engine eng) {
		for(int i =0; i<trackAvatarList.size(); i++) {
			SkeletalEntity manSE = (SkeletalEntity) eng.getSceneManager().getEntity(trackAvatarList.get(i).getEntityName());
				//	manSE.playAnimation("runAnimation", 0.5f, SkeletalEntity.EndType.LOOP, 0);
			//		System.out.println("trying to play animation");
			//		System.out.println("is avatar moving?: " + trackAvatarList.get(i).isIfAvatarMoved());
			//		System.out.println("is avatar animation playing?: " + trackAvatarList.get(i).isIfAvatarAnimationPlayed());
		if(trackAvatarList.get(i).isIfAvatarMoved() == true && trackAvatarList.get(i).isIfAvatarAnimationPlayed() == false) {
			manSE.playAnimation("runAnimation", 0.5f, SkeletalEntity.EndType.LOOP, 0);
	//		System.out.println("now playing animation for :" + manSE.getName());
			trackAvatarList.get(i).setIfAvatarAnimationPlayed(true);
		}
		
		if(trackAvatarList.get(i).ifAvatarMoved == false) {
			manSE.stopAnimation();
			trackAvatarList.get(i).setIfAvatarAnimationPlayed(false);
		}
		
		}
		
		
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
    	final ShootAction p1ShootAction = new ShootAction(sm, playerN, gameClient);
    	
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
    		else if (c.getType() == Type.MOUSE || c.getType() == Type.TRACKPAD) {
    			/************************************
    	    	 * Mouse/Trackpad Bindings
    	    	 ***********************************/
    			im.associateAction(c, Component.Identifier.Button.LEFT, p1ShootAction, INPUT_ACTION_TYPE.ON_PRESS_ONLY);
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
			final String check2 = it.next().toString();
			for(int i = 0; i<trackAvatarList.size(); i++) {
				String check1 = trackAvatarList.get(i).getName();
				if (check1.contentEquals(check2)) {
					//System.out.println("now removing :" + trackAvatarList.get(i).getEntityName() + " from trackAvatarList!!!");
					trackAvatarList.remove(i);
				}
			}
			//this.physicsEngine.removeObject(this.getEngine().getSceneManager().getSceneNode(check2).getPhysicsObject().getUID());
			this.getEngine().getSceneManager().destroySceneNode(check2);
			
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
			final SkeletalEntity ghostE = this.getEngine().getSceneManager().createSkeletalEntity(avatar.getUUID().toString(),
							avatar.getAvatar() == null ? Avatars.SPHEREBOT.getAvatar().getAvatarSkeletalMeshFileName() : avatar.getAvatar().getAvatarSkeletalMeshFileName(), 
							avatar.getAvatar() == null ? Avatars.SPHEREBOT.getAvatar().getAvatarSkeletalFileName() : avatar.getAvatar().getAvatarSkeletalFileName());
			final Texture tex = this.getEngine().getSceneManager().getTextureManager().getAssetByPath(avatar.getAvatar() == null ? Avatars.SPHEREBOT.getAvatar().getAvatarTextureFileName() : avatar.getAvatar().getAvatarTextureFileName());
			final TextureState tstate = (TextureState)this.getEngine().getSceneManager().getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
			tstate.setTexture(tex);
			ghostE.setRenderState(tstate);
	    	ghostE.setPrimitive(Primitive.TRIANGLES);
	    	ghostE.loadAnimation("runAnimation", avatar.getAvatar() == null ? "Sphere_Bot_Attack.rka" : avatar.getAvatar().getAvatarAnimationFileName());
	    	final SceneNode ghostN = this.getEngine().getSceneManager().getSceneNode(MyGame.AVATAR_OBJECTS_NODE_GROUP).createChildSceneNode(avatar.getUUID().toString());
	    	ghostN.setLocalPosition(avatar.getPosition());
			ghostN.attachObject(ghostE);
			
			final float scale = avatar.getAvatar() == null ? Avatars.SPHEREBOT.getAvatar().getScale() : avatar.getAvatar().getScale();
			final float heightOffset = avatar.getAvatar() == null ? Avatars.SPHEREBOT.getAvatar().getAvatarHeightOffset() : avatar.getAvatar().getAvatarHeightOffset();
			ghostN.scale(scale, scale, scale);
			ghostN.moveUp(heightOffset);
			
			avatar.setNode(ghostN);
			avatar.setEntity(ghostE);
			avatar.setPosition(ghostN.getLocalPosition());
			TrackGhostAvatars trackavatar = new TrackGhostAvatars(ghostN.getName(),ghostE.getName(), ghostN.getLocalPosition().x(), ghostN.getLocalPosition().y(), ghostN.getLocalPosition().z());
			trackAvatarList.add(trackavatar);
			
			//this.checkIfGhostMoved();
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
        
		invokeScript("configureTerrain", this.getEngine(), MyGame.TERRAIN_OBJECTS_NODE_GROUP);
	}
	
	private void setupGroundPlane(SceneManager sm) {
		
		invokeScript("configureGroundPlane", sm, GROUNDPLANE_OBJECTS_NODE_GROUP);
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
		 //setup terrain and ground plane
		final SceneNode terrainGroup = sm.getSceneNode(GROUNDPLANE_OBJECTS_NODE_GROUP);
		final SceneNode sn = (SceneNode) terrainGroup;
		double[] temptf = ArrayUtils.toDoubleArray(sn.getLocalTransform().toFloatArray());
		PhysicsObject physicsObj = 
				physicsEngine.addStaticPlaneObject(physicsEngine.nextUID(), temptf, MyGame.UP_VECTOR, 0.0f);
		physicsObj.setBounciness(1.0f);
		physicsObj.setFriction(1.0f);
		sn.setPhysicsObject(physicsObj);
		
		// setup player
		final SceneNode playerN = sm.getSceneNode(PLAYER_NODE_NAME);
		temptf = ArrayUtils.toDoubleArray(playerN.getLocalTransform().toFloatArray());
		final PhysicsObject playerPhys = physicsEngine.addSphereObject(physicsEngine.nextUID(), avatar.getMass(), temptf, 2.0f);
		playerPhys.setBounciness(0.0f);
		playerPhys.setFriction(1.0f);
		playerPhys.setDamping(0.98f, 0.98f);
		playerN.setPhysicsObject(playerPhys);
		
	}
	
	public PhysicsEngine getPhysicsEngine() {
		return this.physicsEngine;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getNextX() {
		return nextX;
	}

	public void setNextX(float nextX) {
		this.nextX = nextX;
	}

	public float getNextY() {
		return nextY;
	}

	public void setNextY(float nextY) {
		this.nextY = nextY;
	}

	public float getNextZ() {
		return nextZ;
	}

	public void setNextZ(float nextZ) {
		this.nextZ = nextZ;
	}
	
	public void checkIfMoved() {
		boolean checkX = false;
		boolean checkY = false;
		boolean checkZ = false; 
		
		boolean xHasChanged = false;
		boolean yHasChanged = false;
		boolean zHasChanged = false; 
		if(this.getX() - this.getNextX() != 0) {
			//System.out.println("mech has moved!!");
			
			xHasChanged = true;
		} else {
			xHasChanged = false;
		}
		
		checkX = true;
		
		if(this.getY() - this.getNextY() != 0) {
			//System.out.println("mech has moved!!!");
			yHasChanged = true;
		} else {
			yHasChanged = false;
		}
		
		checkY = true;
		
		if(this.getZ() - this.getNextZ() != 0) {
			//System.out.println("mech has moved!!!");
			zHasChanged = true;
		} else {
			zHasChanged = false;
		}
		
		checkZ = true;
		
		
		//once we check all of them then do the following
		if(checkX == true && checkY == true && checkZ == true) {
			if(xHasChanged == false && yHasChanged == false && zHasChanged == false) {
			//	System.out.println("mech has not moved");
				//this.stopMechRunAnimate(this.getEngine());
				//this.mechrunAnimate(getEngine());
				this.setIsRunning(false);
				this.setAnimateIsPlaying(false);
				this.mechrunAnimate(this.getEngine());
				
			}
			else {
			//	System.out.println("mech HAS moved!!!");
				this.setIsRunning(true);
				this.mechrunAnimate(this.getEngine());
				//this.stopMechRunAnimate(getEngine());
				
				
			}
		}
		
		
	}
	
	public void checkIfGhostMoved() {
	
		if(this.isCheckIfGhostMoveInitial() == true && this.isCheckIfGhostMoveFinal() == true) {
			
			float xtotal = 99999999;
			float ytotal = 99999999;
			float ztotal = 99999999;
		//	System.out.println("now checking if ghost moved for all");
			for(int i = 0; i<trackAvatarList.size(); i++) {
				xtotal = trackAvatarList.get(i).getX() - trackAvatarList.get(i).getNextX();
				ytotal = trackAvatarList.get(i).getY() - trackAvatarList.get(i).getNextY();
				ztotal = trackAvatarList.get(i).getZ() - trackAvatarList.get(i).getNextZ();
				
				if(xtotal == 0 && ytotal == 0 && ztotal == 0) {
					trackAvatarList.get(i).setIfAvatarMoved(false);
				//	System.out.println("avatar: " + trackAvatarList.get(i).getEntityName() + " has not moved!!!!!");
				//	System.out.println("value of x is : " + xtotal);
				//	System.out.println("value of y is: " + ytotal);
				//	System.out.println("value of z is: " + ztotal);
					xtotal = -1; ytotal = -1; ztotal = -1;
				}
				else if(xtotal != 0 || ytotal !=0 || ztotal!= 0){
			//		System.out.println("avatar: " + trackAvatarList.get(i).getEntityName() + " has moved!!!");
					trackAvatarList.get(i).setIfAvatarMoved(true);
				//	System.out.println("value of x is : " + xtotal);
				//	System.out.println("value of y is: " + ytotal);
				//	System.out.println("value of z is: " + ztotal);
					xtotal = -1; ytotal = -1; ztotal = -1;
				}
		
		
			}
			
			
			//checkComplete = true;
			
			//if(checkComplete == true) {
			//	for(int i = 0; i<trackAvatarList.size(); i++) {
			//		trackAvatarList.get(i).setIfAvatarMoved(false);
			//	}
			
			this.setCheckIfGhostMoveInitial(false);
			this.setCheckIfGhostMoveFinal(false);
			//checkComplete = false;
			//}
		}
		
	}
	
	public void checkIfGhostMovedIntially() {
		if(this.isCheckIfGhostMoveInitial() == false && this.isCheckIfGhostMoveFinal() == false) {
		//	System.out.println("Now checking to see if ghost moved initially");
			for(int i = 0; i<trackAvatarList.size(); i++) {
				SceneNode snode = this.getEngine().getSceneManager().getSceneNode(trackAvatarList.get(i).getName());
				trackAvatarList.get(i).setX(snode.getLocalPosition().x());
				trackAvatarList.get(i).setY(snode.getLocalPosition().y());
				trackAvatarList.get(i).setZ(snode.getLocalPosition().z());
		
		
			}
			this.setCheckIfGhostMoveInitial(true);
		}
	}
	
	public void checkIfGhostMovedFinal() {
		if(this.isCheckIfGhostMoveInitial() == true && this.isCheckIfGhostMoveFinal() == false) {
			//System.out.println("now checking to see if ghost move final");
			for(int i = 0; i<trackAvatarList.size(); i++) {
				SceneNode snode = this.getEngine().getSceneManager().getSceneNode(trackAvatarList.get(i).getName());
				trackAvatarList.get(i).setNextX(snode.getLocalPosition().x());
				trackAvatarList.get(i).setNextY(snode.getLocalPosition().y());
				trackAvatarList.get(i).setNextZ(snode.getLocalPosition().z());
		
		
			}
			this.setCheckIfGhostMoveFinal(true);
		}
	}
	
	
	public void printTrackAvatarListInfo() {
		for(int i = 0; i< trackAvatarList.size(); i++) {
		//	System.out.println(trackAvatarList.get(i).toString());
		}
	}
	
	public boolean isCheckIfGhostMoveInitial() {
		return checkIfGhostMoveInitial;
	}

	public void setCheckIfGhostMoveInitial(boolean checkIfGhostMoveInitial) {
		this.checkIfGhostMoveInitial = checkIfGhostMoveInitial;
	}

	public boolean isCheckIfGhostMoveFinal() {
		return checkIfGhostMoveFinal;
	}

	public void setCheckIfGhostMoveFinal(boolean checkIfGhostMoveFinal) {
		this.checkIfGhostMoveFinal = checkIfGhostMoveFinal;
	}

	public boolean getIsAnimated() {
		return this.animatePlaying;
	}
	
	public void setAnimateIsPlaying(boolean isPlaying) {
		this.animatePlaying = isPlaying;
	}
	
	public void stopMechRunAnimate(Engine eng)
	{
		SkeletalEntity manSE =
		(SkeletalEntity) eng.getSceneManager().getEntity(PLAYER_NAME);
			
		manSE.stopAnimation();
	}
	
	public boolean getIsRunning() {
		return this.isRunning;
	}
	
	public void setIsRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
}
