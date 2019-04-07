// Skybox Configuration Script

var JavaPackages = new JavaImporter(
	Packages.ray.rage.asset.texture.TextureManager,
	Packages.ray.rage.scene.SceneManager,
	Packages.ray.rage.scene.SkyBox,
	Packages.ray.rage.util.Configuration,
	Packages.ava.awt.geom.AffineTransform
);

with (JavaPackages) {
	function configureSkybox(tm, sm, sb, conf, xform) {
		tm.setBaseDirectoryPath(conf.valueOf("assets.skyboxes.path"));
		var front = tm.getAssetByPath("posz.jpg");
		var back = tm.getAssetByPath("negz.jpg");
		var left = tm.getAssetByPath("negx.jpg");
		var right = tm.getAssetByPath("posx.jpg");
		var top = tm.getAssetByPath("posy.jpg");
		var bottom = tm.getAssetByPath("negy.jpg");
		tm.setBaseDirectoryPath(conf.valueOf("assets.textures.path"));
		
		// cubemap textures are flipped upside down
		// all textures must hav ethe same dimensions, so any image's
		// height will work since they are all the same height
		xform.translate(0, front.getImage().getHeight());
		xform.scale(1, -1);
		
		front.transform(xform);
		back.transform(xform);
		left.transform(xform);
		right.transform(xform);
		top.transform(xform);
		bottom.transform(xform);
		
		sb.setTexture(front, SkyBox.Face.FRONT);
		sb.setTexture(back, SkyBox.Face.BACK);
		sb.setTexture(left, SkyBox.Face.LEFT);
		sb.setTexture(right, SkyBox.Face.RIGHT);
		sb.setTexture(top, SkyBox.Face.TOP);
		sb.setTexture(bottom, SkyBox.Face.BOTTOM);
		
		sm.setActiveSkyBox(sb);
	}
}