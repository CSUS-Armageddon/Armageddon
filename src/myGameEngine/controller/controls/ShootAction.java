package myGameEngine.controller.controls;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import a3.CreateShootSound;
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
	
	private static final float SHOOTING_FORCE = 50000000.0f;
	
	private final AtomicBoolean isRightGun = new AtomicBoolean(true);
	
	private final SceneManager sm;
	private final SceneNode node;
	private final GameClient gameClient;
	
	private final BulletRemovalController brc;
	
	CreateShootSound createSound;
	
	public ShootAction(SceneManager sm, SceneNode node, GameClient gameClient) {
		this.sm = sm;
		this.node = node;
		this.gameClient = gameClient;
		this.brc = new BulletRemovalController(gameClient.getGame().getPhysicsEngine(), sm, 5000);
	}

	@Override
	public void performAction(float time, Event evt) {
		try {
			final UUID id = UUID.randomUUID();
			final Entity bulletEntity = sm.createEntity("bulletEntity-" + id, "earth.obj");
			final SceneNode bulletNode = sm.getRootSceneNode().createChildSceneNode("bulletNode-" + id);
			bulletNode.attachObject(bulletEntity);
			
			if (isRightGun.get()) {
				final Vector3 gunNode1Pos = sm.getSceneNode(MyGame.PLAYER_GUN_NODE1_NAME).getWorldPosition();
				bulletNode.setLocalPosition(gunNode1Pos.x(), gunNode1Pos.y(), gunNode1Pos.z());
				bulletNode.setLocalRotation(node.getLocalRotation());
				isRightGun.set(false);
			} else {
				final Vector3 gunNode2Pos = sm.getSceneNode(MyGame.PLAYER_GUN_NODE2_NAME).getWorldPosition();
				bulletNode.setLocalPosition(gunNode2Pos.x(), gunNode2Pos.y(), gunNode2Pos.z());
				bulletNode.setLocalRotation(node.getLocalRotation());
				isRightGun.set(true);
			}
			
			final PhysicsEngine physicsEngine = gameClient.getGame().getPhysicsEngine();
			final double[] temptf = ArrayUtils.toDoubleArray(bulletNode.getLocalTransform().toFloatArray());
			final PhysicsObject physicsObj = 
					physicsEngine.addSphereObject(physicsEngine.nextUID(), 100.0f, temptf, 2.0f);
			physicsObj.setBounciness(0.5f);
			bulletNode.setPhysicsObject(physicsObj);
			
			final Vector3 forward = this.node.getLocalForwardAxis();
			final float xForce = forward.x() == 0 ? 0 : forward.x() * SHOOTING_FORCE;
			final float yForce = forward.y() == 0 ? 0 : forward.y() * SHOOTING_FORCE;
			final float zForce = forward.z() == 0 ? 0 : forward.z() * SHOOTING_FORCE;
			
			this.gameClient.sendShootMessage(true,bulletNode.getWorldPosition(),forward,xForce,yForce,zForce );
			physicsObj.applyForce(xForce, yForce, zForce, 0, 0, 0);
			
			brc.addNode(bulletNode);
			sm.addController(brc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
