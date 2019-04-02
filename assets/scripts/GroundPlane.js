// Ground Plane Configuration Script

var JavaPackages = new JavaImporter(
	Packages.myGameEngine.mesh.GroundPlane	
);

with (JavaPackages) {
	function configureGroundPlane(groundPlane) {
		groundPlane.setTextureFilename("earth-day.jpeg");
		groundPlane.init();
	}
}