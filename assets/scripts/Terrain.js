// Terrain Configuration Script

var JavaPackages = new JavaImporter(
	Packages.ray.rage.scene.Tessellation,
	Packages.ray.rage.scene.SceneNode,
	Packages.ray.rage.Engine
);

with (JavaPackages) {
	
	var TESSELLATION_QUALITY = 8;
	var TESSELLATION_SUBDIVISIONS = 32;
	
	var TERRAIN_SCALE_X = 5000;
	var TERRAIN_SCALE_Y = 1000;
	var TERRAIN_SCALE_Z = 5000;
	
	var TERRAIN_HEIGHTMAP = "terrain/island/island-height-map2.png";
	var TERRAIN_MAP = "terrain/island/island-map.png";
	
	var TESSELLATION_ENTITY = "tessE";
	var TESSELLATION_NODE = "tessN";
	
	function configureTerrain(engine) {
		var tessE = engine.getSceneManager().createTessellation(TESSELLATION_ENTITY, TESSELLATION_QUALITY);
		var tessN = engine.getSceneManager().getRootSceneNode().createChildSceneNode(TESSELLATION_NODE);
		tessE.setSubdivisions(TESSELLATION_SUBDIVISIONS);
		tessE.setHeightMap(engine, TERRAIN_HEIGHTMAP);
		tessE.setTexture(engine, TERRAIN_MAP);
		tessN.attachObject(tessE);
		tessN.scale(TERRAIN_SCALE_X, TERRAIN_SCALE_Y ,TERRAIN_SCALE_Z);
	}
}