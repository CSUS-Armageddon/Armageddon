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
		tessE.setHeightMap(engine, "terrain/city_map_height.png");
		tessE.setTexture(engine, "terrain/city_map.png");
		tessN.attachObject(tessE);
		tessN.scale(5000, 500 ,5000);
	}
}