package a3.network.server.impl.ai.npc;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import a3.MyGame;
import a3.avatar.Avatar;
import a3.network.client.GhostAvatar;
import a3.network.logging.ServerLogger;
import myGameEngine.util.MovementUtils;
import ray.networking.IGameConnection.ProtocolType;
import ray.rml.Degreef;
import ray.rml.Matrix3;
import ray.rml.Matrix3f;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class NPC {
	
	private boolean shouldRemove = false;
	
	final private String serverAddress;
	final private int serverPort;
	final private ProtocolType serverProtocol;
	private NPCGameClient gameClient;
	private boolean isClientConnected;
	private List<GhostAvatar> ghosts;
	private Vector<UUID> gameObjectsToRemove;
	
	final private Avatar avatar;
	
	final private UUID followPlayerUUID;
	private static final float MAX_SEPARATION = 15.0f;
	
	private Vector3 position = Vector3f.createFrom(0, 35, 0);
	private Matrix3 rotation = Matrix3f.createIdentityMatrix();
	
	public NPC(String serverAddress, int serverPort, Avatar avatar, UUID followPlayerUUID) {
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.serverProtocol = ProtocolType.UDP;
		this.avatar = avatar;
		this.followPlayerUUID = followPlayerUUID;
	}
	
	public void init() {
		setupNetworking();
	}

	private void setupNetworking() {
		
		gameObjectsToRemove = new Vector<UUID>();
		ghosts = new ArrayList<GhostAvatar>();
		isClientConnected = false;
		try {
			gameClient = new NPCGameClient(InetAddress.getByName(serverAddress), serverPort, serverProtocol, this, InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (gameClient == null) {
			ServerLogger.INSTANCE.logln("Missing Game Host");
		}
	}
	
	public void joinSession() {
		gameClient.sendJoinMessage();
	}
	
	protected void processNetworking() {
		try {
			// process packets received by the client from the server
			if (gameClient != null) {
				gameClient.processPackets();
			}
			gameObjectsToRemove.clear();
		} catch (Exception e) {
			ServerLogger.INSTANCE.log(e);;
		}
	}
	
	public void setPlayerPosition(Vector3 position) {
		this.position = position;
	}
	
	public Vector3 getPlayerPosition() {
		return this.position;
	}
	
	public void setPlayerRotation(Matrix3 rotation) {
		this.rotation = rotation;
	}
	
	public Matrix3 getPlayerRotation() {
		return this.rotation;
	}
	
	public void addGhostAvatar(GhostAvatar avatar) throws IOException {
		if (avatar != null) {
			ghosts.add(avatar);
			avatar.setNode(new GhostSceneNode());
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
	
	public Avatar getAvatar() {
		return avatar;
	}
	
	public void moveForward(float amount) {
		final Vector3f pos = (Vector3f) getPlayerPosition();
		final Vector3f newPos = (Vector3f) Vector3f.createFrom(pos.x(), pos.y(), (pos.z() + (amount * MyGame.PLAYER_SPEED)));
		
		setPlayerPosition(newPos);
		this.gameClient.sendMoveMessage(this.getPlayerPosition());
	}
	
	public void moveLeft(float amount) {
		final Vector3f pos = (Vector3f) getPlayerPosition();
		final Vector3f newPos = 
				(Vector3f) Vector3f.createFrom((pos.x() + (amount * MyGame.PLAYER_SPEED)), pos.y(), pos.z());
		
		setPlayerPosition(newPos);
		this.gameClient.sendMoveMessage(this.getPlayerPosition());
	}
	
	public void yaw(float amount) {
		setPlayerRotation(getPlayerRotation().rotate(Degreef.createFrom(amount), Vector3f.createFrom(0.0f, 1.0f, 0.0f)));
		this.gameClient.sendRotateMessage(this.getPlayerRotation());
	}

	/**
	 * @return the followPlayerUUID
	 */
	public UUID getFollowPlayerUUID() {
		return followPlayerUUID;
	}
	
	public void followPlayer(UUID messageUUID, Vector3 playerPosition) {
		if (messageUUID.equals(getFollowPlayerUUID())) {
			if (!MovementUtils.validateSeparation(playerPosition, getPlayerPosition(), MAX_SEPARATION)) {
				// player has strayed farther than the allowed separation... so lets move the npc closer
				setPlayerPosition(playerPosition);
				setHeight(messageUUID, playerPosition.y());
				// fake a move so we can send the message to other clients
				this.gameClient.sendMoveMessage(getPlayerPosition());
			}
		}
	}
	
	public void setHeight(UUID messageUUID, float height) {
		if (messageUUID == getFollowPlayerUUID()) {
			final Vector3f pos = (Vector3f) getPlayerPosition();
			final Vector3f newPos = (Vector3f) Vector3f.createFrom(pos.x(), height + this.getAvatar().getAvatarHeightOffset(), pos.z());
			
			setPlayerPosition(newPos);
		}
	}

	/**
	 * @return the shouldRemove
	 */
	public boolean isShouldRemove() {
		return shouldRemove;
	}

	/**
	 * @param shouldRemove the shouldRemove to set
	 */
	public void setShouldRemove(boolean shouldRemove) {
		this.shouldRemove = shouldRemove;
	}
	
}
