// Building Scenery Configuration Script

var JavaPackages = new JavaImporter(
	Packages.ray.rage.scene.SceneManager,
	Packages.ray.rage.rendersystem.Renderable.Primitive
);

with (JavaPackages) {
	function configureBuildings(sm) {
		var MAX_BUILDINGS = 10;
		var x = 118.53;
		var y = 0;
		var z = 137.70;
		for (var i = 0; i < MAX_BUILDINGS; i++) {
			var buildingE = sm.createEntity("buildingE_" + i, "skyscrapper_1.obj");
			buildingE.setPrimitive(Primitive.TRIANGLES);
			buildingN = sm.getRootSceneNode().createChildSceneNode("buildingN_" + i);
			buildingN.setLocalPosition(x, y, z);
			buildingN.scale(50, 50, 50);
			buildingN.attachObject(buildingE);
			x = x + 110.0;
		}
	}
}