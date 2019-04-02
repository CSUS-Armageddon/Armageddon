package myGameEngine.asset;

import java.io.IOException;
import java.nio.file.Path;

public class JavaScriptLoader implements ScriptLoader {

	@Override
	public void loadAsset(ScriptAsset script, Path path) throws IOException {
		if (script == null) {
			throw new NullPointerException("Null script");
		}
		if (path == null) {
			throw new NullPointerException("Null path");
		}
		
		script.setScriptFile(path.toFile());
		
	}

	@Override
	public void notifyDispose() {}

}
