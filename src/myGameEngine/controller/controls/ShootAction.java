package myGameEngine.controller.controls;

import java.io.IOException;

import a3.MyGame;
import a3.network.client.GameClient;
import myGameEngine.node.controller.BulletRemovalController;
import myGameEngine.util.ArrayUtils;
import net.java.games.input.Event;
import ray.input.action.Action;
import ray.physics.PhysicsEngine;
import ray.physics.PhysicsObject;
import ray.rage.scene.Entity;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;
import ray.rml.Vector3;

public class ShootAction implements Action {
	
	private static final float SHOOTING_FORCE = 50000.0f;
	
	private final SceneManager sm;
	private final SceneNode node;
	private final GameClient gameClient;
	
	private final BulletRemovalController brc;
	
	public ShootAction(SceneManager sm, SceneNode node, GameClient gameClient) {
		this.sm = sm;
		this.node = node;
		this.gameClient = gameClient;
		this.brc = new BulletRemovalController(gameClient.getGame().getPhysicsEngine(), sm, 5000);
	}

	@Override
	public void performAction(float time, Event evt) {
		try {
			final long id = System.currentTimeMillis();
			final Entity bulletEntity = sm.createEntity("bulletEntity-" + id, "earth.obj");
			final SceneNode bulletNode = sm.getRootSceneNode().createChildSceneNode("bulletNode-" + id);
			bulletNode.attachObject(bulletEntity);
			
			//final Vector3 avatarPos = node.getLocalPosition();
			final Vector3 gunNode1Pos = sm.getSceneNode(MyGame.PLAYER_GUN_NODE1_NAME).getWorldPosition();
			bulletNode.setLocalPosition(gunNode1Pos.x(), gunNode1Pos.y(), gunNode1Pos.z());
			bulletNode.setLocalRotation(node.getLocalRotation());
			
			final PhysicsEngine physicsEngine = gameClient.getGame().getPhysicsEngine();
			final double[] temptf = ArrayUtils.toDoubleArray(bulletNode.getLocalTransform().toFloatArray());
			final PhysicsObject physicsObj = 
					physicsEngine.addSphereObject(physicsEngine.nextUID(), 1.0f, temptf, 2.0f);
			physicsObj.setBounciness(0.5f);
			bulletNode.setPhysicsObject(physicsObj);
			
			final Vector3 forward = this.node.getLocalForwardAxis();
			final float xForce = forward.x() == 0 ? 0 : forward.x() * SHOOTING_FORCE;
			final float yForce = forward.y() == 0 ? 0 : forward.y() * SHOOTING_FORCE;
			final float zForce = forward.z() == 0 ? 0 : forward.z() * SHOOTING_FORCE;
			
			physicsObj.applyForce(xForce, yForce, zForce, 0, 0, 0);
			
			brc.addNode(bulletNode);
			sm.addController(brc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
