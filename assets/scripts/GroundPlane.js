// Ground Plane Configuration Script

var JavaPackages = new JavaImporter(
	Packages.ray.rage.scene.SceneNode,
	Packages.ray.rage.Engine
);

with (JavaPackages) {
	
	var GROUNDPLANE_SCALE_X = 5000;
	var GROUNDPLANE_SCALE_Y = 1;
	var GROUNDPLANE_SCALE_Z = 5000;
	
	var GROUNDPLANE_POS_X = 0;
	var GROUNDPLANE_POS_Y = -10;
	var GROUNDPLANE_POS_Z = 0;
	
	var GROUNDPLANE_OBJ = "ground_plane.obj";
	var GROUNDPLANE_TEXTURE = "terrain/island/island-map-ground-plane.png";
	
	var GROUNDPLANE_ENTITY = "GroundPlaneE";
	var GROUNDPLANE_NODE = "GroundPlaneN";
	
	function configureGroundPlane(sm, groupName) {
		var groundPlaneE = sm.createEntity(GROUNDPLANE_ENTITY, GROUNDPLANE_OBJ);
        var groundPlaneN = sm.getSceneNode(groupName).createChildSceneNode(GROUNDPLANE_NODE);
		groundPlaneN.attachObject(groundPlaneE);
		groundPlaneN.scale(GROUNDPLANE_SCALE_X, GROUNDPLANE_SCALE_Y, GROUNDPLANE_SCALE_Z);
		groundPlaneN.setLocalPosition(GROUNDPLANE_POS_X, GROUNDPLANE_POS_Y, GROUNDPLANE_POS_Z);
	}
}