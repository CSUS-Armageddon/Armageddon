package myGameEngine.asset;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import ray.rage.asset.AbstractAssetManager;
import ray.rage.asset.Asset;
import ray.rage.asset.AssetLoader;

public class ScriptManager extends AbstractAssetManager<ScriptAsset> {
	
	public ScriptManager() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		// hack to inject *.js file extension
		final Field classMapperField = getClass().getSuperclass().getDeclaredField("classMapper");
		classMapperField.setAccessible(true);
		final Object classMapper = classMapperField.get(this);
		final Field classMapField = classMapper.getClass().getDeclaredField("classMap");
		classMapField.setAccessible(true);
		final Object classMapObj = classMapField.get(classMapper);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final Map<String, Class<? extends AssetLoader<? extends Asset>>> classMap = (HashMap)classMapObj;
		classMap.put(".js", JavaScriptLoader.class);
	}

	@Override
	protected ScriptAsset createAssetImpl(String name) {
		return new ScriptAsset(this, name);
	}
}
