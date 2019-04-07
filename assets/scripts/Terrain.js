// Terrain Configuration Script

var JavaPackages = new JavaImporter(
	Packages.ray.rage.scene.Tessellation,
	Packages.ray.rage.scene.SceneNode,
	Packages.ray.rage.Engine
);

with (JavaPackages) {
	function configureTerrain(engine, tessE, tessN) {
		tessE.setQuality(8);
		tessE.setSubdivisions(32);
		tessE.setHeightMap(engine, "terrain/map.jpg");
		tessE.setTexture(engine, "terrain/frozen.jpg");
		tessN.attachObject(tessE);
		tessN.scale(500, 500 ,500);
	}
}