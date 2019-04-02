// Ground Plane Configuration Script

var JavaPackages = new JavaImporter(
	Packages.myGameEngine.mesh.GroundPlane,
	Packages.ray.rage.scene.SceneNode
);

with (JavaPackages) {
	function configureGroundPlane(groundPlane, groundNode) {
		groundPlane.setTextureFilename("oxygen-refill.jpeg");
		groundPlane.init();
		groundNode.scale(150.0, 150.0, 150.0);
	}
}