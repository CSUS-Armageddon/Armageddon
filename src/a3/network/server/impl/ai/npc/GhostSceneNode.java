package a3.network.server.impl.ai.npc;

import a3.network.api.Position;
import a3.network.api.Rotation;
import ray.physics.PhysicsObject;
import ray.rage.scene.Node;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;
import ray.rage.scene.SceneObject;
import ray.rml.Angle;
import ray.rml.Matrix3;
import ray.rml.Matrix4;
import ray.rml.Vector3;

public class GhostSceneNode implements SceneNode {

	private Vector3 localPosition = Position.defaultPosition().toVector3();
	private Matrix3 localRotation = Rotation.defaultRotation().toMatrix3();
	
	/**
	 * @return the localPosition
	 */
	public Vector3 getLocalPosition() {
		return localPosition;
	}
	/**
	 * @param localPosition the localPosition to set
	 */
	public void setLocalPosition(Vector3 localPosition) {
		this.localPosition = localPosition;
	}
	/**
	 * @return the localRotation
	 */
	public Matrix3 getLocalRotation() {
		return localRotation;
	}
	/**
	 * @param localRotation the localRotation to set
	 */
	public void setLocalRotation(Matrix3 localRotation) {
		this.localRotation = localRotation;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Node createChildNode(String paramString) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setLocalPosition(float paramFloat1, float paramFloat2, float paramFloat3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setLocalScale(float paramFloat1, float paramFloat2, float paramFloat3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setLocalScale(Vector3 paramVector3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Vector3 getLocalScale() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Matrix4 getLocalTransform() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Vector3 getWorldPosition() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Matrix3 getWorldRotation() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Vector3 getWorldScale() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Matrix4 getWorldTransform() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void moveForward(float paramFloat) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void moveBackward(float paramFloat) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void moveLeft(float paramFloat) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void moveRight(float paramFloat) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void moveUp(float paramFloat) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void moveDown(float paramFloat) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void translate(float paramFloat1, float paramFloat2, float paramFloat3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void translate(Vector3 paramVector3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void yaw(Angle paramAngle) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void roll(Angle paramAngle) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void pitch(Angle paramAngle) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void rotate(Angle paramAngle, Vector3 paramVector3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void scale(float paramFloat1, float paramFloat2, float paramFloat3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void scale(Vector3 paramVector3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void lookAt(float paramFloat1, float paramFloat2, float paramFloat3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void lookAt(Vector3 paramVector3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void lookAt(Vector3 paramVector31, Vector3 paramVector32) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void lookAt(Node paramNode) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void lookAt(Node paramNode, Vector3 paramVector3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Vector3 getLocalRightAxis() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Vector3 getLocalUpAxis() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Vector3 getLocalForwardAxis() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Vector3 getWorldRightAxis() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Vector3 getWorldUpAxis() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Vector3 getWorldForwardAxis() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void notifyAttached(Node paramNode) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void notifyDetached() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Node getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void attachChild(Node paramNode) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Node getChild(String paramString) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Node getChild(int paramInt) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getChildCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Iterable<Node> getChildNodes() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void detachChild(Node paramNode) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void detachAllChildren() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public PhysicsObject getPhysicsObject() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setPhysicsObject(PhysicsObject paramPhysicsObject) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void update(boolean paramBoolean1, boolean paramBoolean2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void updateFromParent() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setListener(Listener paramListener) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Listener getListener() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SceneManager getManager() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SceneNode createChildSceneNode(String paramString) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void notifyRootNode() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void attachObject(SceneObject paramSceneObject) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public SceneObject getAttachedObject(String paramString) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SceneObject getAttachedObject(int paramInt) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getAttachedObjectCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Iterable<SceneObject> getAttachedObjects() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void detachObject(SceneObject paramSceneObject) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public SceneObject detachObject(String paramString) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void detachAllObjects() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void notifyInSceneGraph(boolean paramBoolean) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isInSceneGraph() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}
