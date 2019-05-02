// Ground Plane Configuration Script

var JavaPackages = new JavaImporter(
	Packages.myGameEngine.mesh.GroundPlane,
	Packages.ray.rage.scene.SceneNode,
	Packages.ray.rage.Engine
);

with (JavaPackages) {
	
	var GROUNDPLANE_SCALE_X = 5000;
	var GROUNDPLANE_SCALE_Y = 1;
	var GROUNDPLANE_SCALE_Z = 5000;
	
	var GROUNDPLANE_TEXTURE = "terrain/island/island-map-ground-plane.png";
	
	var GROUNDPLANE_ENTITY = "GroundPlaneE";
	var GROUNDPLANE_NODE = "GroundPlaneN";
	
	function configureGroundPlane(engine, groupName) {
		var GroundPlane = Java.type("myGameEngine.mesh.GroundPlane");
		var groundPlane = new GroundPlane(GROUNDPLANE_ENTITY, engine, engine.getSceneManager(), false);
        var groundPlaneN = engine.getSceneManager().getSceneNode(groupName).createChildSceneNode(GROUNDPLANE_NODE);
		groundPlane.setTextureFilename(GROUNDPLANE_TEXTURE);
		groundPlane.init();
		groundPlaneN.attachObject(groundPlane.getManualObject());
		groundPlaneN.scale(GROUNDPLANE_SCALE_X, GROUNDPLANE_SCALE_Y, GROUNDPLANE_SCALE_Z);
	}
}