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
import ray.networking.IGameConnection.ProtocolType;
import ray.rage.scene.SceneNode;
import ray.rage.scene.Tessellation;
import ray.rml.Degreef;
import ray.rml.Matrix3;
import ray.rml.Matrix3f;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class NPC {
	
	final private String serverAddress;
	final private int serverPort;
	final private ProtocolType serverProtocol;
	private NPCGameClient gameClient;
	private boolean isClientConnected;
	private List<GhostAvatar> ghosts;
	private Vector<UUID> gameObjectsToRemove;
	
	final private Avatar avatar;
	
	private Vector3 position = Vector3f.createFrom(0, 10, 0);
	private Matrix3 rotation = Matrix3f.createIdentityMatrix();
	
	public NPC(String serverAddress, int serverPort, Avatar avatar) {
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.serverProtocol = ProtocolType.UDP;
		this.avatar = avatar;
	}
	
	public void init() {
		setupNetworking();
		this.isClientConnected = true;
		gameClient.sendCreateMessage(getPlayerPosition(), getPlayerRotation(), avatar.getAvatarName());
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
		gameObjectsToRemove.clear();
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
	
	public void moveForward() {
		final Vector3f pos = (Vector3f) getPlayerPosition();
		final Vector3f newPos = (Vector3f) Vector3f.createFrom(pos.x(), pos.y(), (pos.z() + (1.0f * MyGame.PLAYER_SPEED)));
		
		setPlayerPosition(newPos);
		this.gameClient.sendMoveMessage(this.getPlayerPosition());
		this.gameClient.sendHeightMessage(this.gameClient.getUUID(), this.getPlayerPosition());
	}
	
	public void moveLeft() {
		final Vector3f pos = (Vector3f) getPlayerPosition();
		final Vector3f newPos = 
				(Vector3f) Vector3f.createFrom((pos.x() + (1.0f * MyGame.PLAYER_SPEED)), pos.y(), pos.z());
		
		setPlayerPosition(newPos);
		this.gameClient.sendMoveMessage(this.getPlayerPosition());
		this.gameClient.sendHeightMessage(this.gameClient.getUUID(), this.getPlayerPosition());
	}
	
	public void yaw() {
		setPlayerRotation(getPlayerRotation().rotate(Degreef.createFrom(1.0f), Vector3f.createFrom(0.0f, 1.0f, 0.0f)));
		this.gameClient.sendRotateMessage(this.getPlayerRotation());
		this.gameClient.sendHeightMessage(this.gameClient.getUUID(), this.getPlayerPosition());
	}
	
}
