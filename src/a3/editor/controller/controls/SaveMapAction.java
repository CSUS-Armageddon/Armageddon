package a3.editor.controller.controls;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.Date;

import a3.editor.MyGameEditor;
import net.java.games.input.Event;
import ray.input.action.Action;
import ray.rage.scene.Node;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;
import ray.rage.scene.SceneObject;
import ray.rml.Matrix3;
import ray.rml.Vector3;

public class SaveMapAction implements Action {
	
	private final SceneManager sm;
	private final MyGameEditor editor;
	
	private static final String DATE_PLACEHOLDER = "%DATE%";
	private static final String BODY_PLACEHOLDER = "%BODY%";
	
	private static final String TEMPLATE = 
			"/**\r\n" +
			" * %%% AUTO-GENERATED %%%\r\n" +
			" * Scenery Configuration Script\r\n" +
			" * Generated: " + DATE_PLACEHOLDER + "\r\n" + 
			" * %%% AUTO-GENERATED %%%\r\n" +
			" */\r\n" +
			"\r\n" + 
			"var JavaPackages = new JavaImporter(\r\n" + 
			"	Packages.ray.rage.scene.SceneManager,\r\n" + 
			"	Packages.ray.rage.rendersystem.Renderable.Primitive\r\n" + 
			");\r\n" +
			"\r\n" +
			"with (JavaPackages) {\r\n" +
			"    function generateSceneObjects(sm) {\r\n" + 
			         BODY_PLACEHOLDER + 
			"    }\r\n" + 
			"\r\n}\r\n";
	
	public SaveMapAction(SceneManager sm, MyGameEditor editor) {
		this.sm = sm;
		this.editor = editor;
	}
	
	@Override
	public void performAction(float time, Event evt) {
		
		final SceneNode sceneObjectGroup = 
				(SceneNode) sm.getRootSceneNode().getChild(MyGameEditor.SCENE_OBJECTS_NODE_GROUP);
		
		final StringBuilder template = initDocument();
		final StringBuilder body = generateDocumentBody(sceneObjectGroup);
		final String doc = finalizeDocument(template, body);
		try {
			writeDocument("assets/scripts/SceneGenerator.js", doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private StringBuilder initDocument() {
		final StringBuilder doc = new StringBuilder();
		doc.append(TEMPLATE);
		return doc;
	}
	
	private StringBuilder generateDocumentBody(SceneNode sceneObjectGroup) {
		final StringBuilder body = new StringBuilder();
		for (Node node : sceneObjectGroup.getChildNodes()) {
			final SceneNode sn = (SceneNode) node;
			appendSceneObject(body, sn);
		}
		return body;
	}
	
	private String finalizeDocument(StringBuilder doc, StringBuilder body) {
		return doc.toString()
				.replace(DATE_PLACEHOLDER, new Date().toInstant().atZone(ZoneId.systemDefault()).toString())
				.replace(BODY_PLACEHOLDER, body.toString()).toString();
	}
	
	private void writeDocument(String name, String doc) throws IOException {
		final Path path = Paths.get(name);
		if (Files.exists(path)) {
			// rename existing file as a backup
			path.toFile().renameTo(new File(path.toString().replace(".js", "_BACKUP.js")));
		}
		Files.write(path, doc.getBytes());
	}
	
	private void appendSceneObject(StringBuilder sb, SceneNode sn) {
		final SceneObject obj = sn.getAttachedObject(0); // only 1 object ever attached
		final String entityName = obj.getName();
		final String nodeName = entityName.replace("Entity", "Node");
		final String objId = obj.getName().replace("Object_", "").replace("_Entity", "");
		final Vector3 pos = sn.getLocalPosition();
		final Matrix3 rot = sn.getLocalRotation();
		final Vector3 scl = sn.getLocalScale();
		
		sb.append("        ")
			.append("/******** >>> BEGIN Object_" + objId + " <<< ********/").append("\r\n");
		sb.append("        ")
			.append("var entity_" + objId + " = sm.createEntity('" 
					+ entityName + "', " + "'" 
					+ editor.getPlaceableAvatar(obj.getName()).getAvatarFileName() + "');").append("\r\n");
		sb.append("        ")
			.append("entity_" + objId + ".setPrimitive(Primitive.TRIANGLES);").append("\r\n");
		sb.append("        ")
			.append("var node_" + objId + " = sm.getRootSceneNode().createChildSceneNode('" + nodeName + "');").append("\r\n");
		sb.append("        ")
			.append("node_" + objId + ".setLocalPosition(" + pos.x() + ", " + pos.y() + ", " + pos.z() + ");").append("\r\n");
		sb.append("        ")
			.append("node_" + objId + ".setLocalRotation(" + rot + ");").append("\r\n");
		sb.append("        ")
			.append("node_" + objId + ".setLocalScale(" + scl.x() + ", " + scl.y() + ", " + scl.z() + ");").append("\r\n");
		sb.append("        ")
			.append("node_" + objId + ".attachObject(entity_" + objId + ");").append("\r\n");
		sb.append("        ")
			.append("/******** >>> END Object_" + objId + " <<< ********/").append("\r\n").append("\r\n");
	}
	
}
