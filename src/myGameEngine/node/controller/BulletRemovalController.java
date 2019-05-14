package myGameEngine.node.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ray.physics.PhysicsEngine;
import ray.rage.scene.Node;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;
import ray.rage.scene.controllers.AbstractController;

public class BulletRemovalController extends AbstractController {
	
	private final PhysicsEngine physicsEngine;
	private final SceneManager sm;
	private final long maxTimeToLive;
	private final Map<Node, Long> nodeMap = new HashMap<Node, Long>();
	
	private final List<Node> removalList = new ArrayList<Node>();
	
	public BulletRemovalController(PhysicsEngine physicsEngine, SceneManager sm, long maxTimeToLive) {
		this.physicsEngine = physicsEngine;
		this.sm = sm;
		this.maxTimeToLive = maxTimeToLive;
	}

	@Override
	protected void updateImpl(float elapsedTimeMillis) {
		for (Node n : super.controlledNodesList) {
			if (nodeMap.containsKey(n)) {
				if (System.currentTimeMillis() - nodeMap.get(n) > maxTimeToLive) {
					removalList.add(n);
				}
			} else {
				nodeMap.put(n, System.currentTimeMillis());
			}
		}
		// remove and destroy any timed-out bullets
		for (Node n : removalList) {
			this.removeNode(n);
			physicsEngine.removeObject(((SceneNode)n).getPhysicsObject().getUID());
			sm.destroyEntity(((SceneNode)n).getAttachedObject(0).getName());
			
			sm.destroySceneNode((SceneNode)n);
			nodeMap.remove(n);
		}
		removalList.clear();
	}

}
