// Building Scenery Configuration Script

var JavaPackages = new JavaImporter(
	Packages.ray.rage.scene.SceneManager,
	Packages.ray.rage.rendersystem.Renderable.Primitive
);

with (JavaPackages) {
	function configureBuildings(sm) {
		var MAX_BUILDINGS = 9;
		var SEPARATION = 112.5;
		var x = 118.53;
		var y = 0;
		var z = 140.70;
		erectBuildings(sm,	MAX_BUILDINGS,		x,									z,									SEPARATION,	true,	"_1");
		erectBuildings(sm,	MAX_BUILDINGS,		x,									z + SEPARATION,						SEPARATION, false,	"_2");
		erectBuildings(sm,	MAX_BUILDINGS,		x,									z + (SEPARATION * MAX_BUILDINGS),	SEPARATION, true,	"_3");
		erectBuildings(sm,	MAX_BUILDINGS + 1,	x + (SEPARATION * MAX_BUILDINGS),	z,									SEPARATION,	false,	"_4");
	}
	
	function erectBuildings(sm, num, x, z, mod, isX, ident) {
		for (var i = 0; i < num; i++) {
			var buildingE = sm.createEntity("buildingE_" + i + ident, "skyscrapper_1.obj");
			buildingE.setPrimitive(Primitive.TRIANGLES);
			buildingN = sm.getRootSceneNode().createChildSceneNode("buildingN_" + i + ident);
			buildingN.setLocalPosition(x, 0.0, z);
			buildingN.scale(50, 50, 50);
			buildingN.attachObject(buildingE);
			if (isX) {
				x = x + mod;
			} else {
				z = z + mod;
			}
		}
	}
}