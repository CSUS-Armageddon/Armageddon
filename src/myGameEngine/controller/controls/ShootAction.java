package myGameEngine.controller.controls;

import java.io.IOException;

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
			
			final Vector3 avatarPos = node.getLocalPosition();
			bulletNode.setLocalPosition(avatarPos.x(), avatarPos.y() + 5.0f, avatarPos.z());
			bulletNode.setLocalRotation(node.getLocalRotation());
			
			final PhysicsEngine physicsEngine = gameClient.getGame().getPhysicsEngine();
			final double[] temptf = ArrayUtils.toDoubleArray(bulletNode.getLocalTransform().toFloatArray());
			final PhysicsObject physicsObj = 
					physicsEngine.addSphereObject(physicsEngine.nextUID(), 1.0f, temptf, 2.0f);
			physicsObj.setBounciness(0.5f);
			bulletNode.setPhysicsObject(physicsObj);
			
			physicsObj.applyForce(5000.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
			
			brc.addNode(bulletNode);
			sm.addController(brc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
