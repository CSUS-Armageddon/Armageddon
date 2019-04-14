// Building Scenery Configuration Script

var JavaPackages = new JavaImporter(
	Packages.ray.rage.scene.SceneManager,
	Packages.ray.rage.rendersystem.Renderable.Primitive
);

with (JavaPackages) {
	var MAX_BUILDINGS = 1;
	function configureBuildings(sm) {
		for (var i = 0; i < MAX_BUILDINGS; i++) {
			var buildingE = sm.createEntity("buildingE_" + i, "skyscrapper_1.obj");
			buildingE.setPrimitive(Primitive.TRIANGLES);
			buildingN = sm.getRootSceneNode().createChildSceneNode("buildingN_" + i);
			buildingN.setLocalPosition(25, 0, 0);
			buildingN.scale(20, 20, 20);
			buildingN.attachObject(buildingE);
		}
	}
}