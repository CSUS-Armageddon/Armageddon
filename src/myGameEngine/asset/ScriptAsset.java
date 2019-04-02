package myGameEngine.asset;

import java.io.File;

import ray.rage.asset.AbstractAsset;

public class ScriptAsset extends AbstractAsset {
	
	private File scriptFile;

	protected ScriptAsset(ScriptManager scriptManager, String name) {
		super(scriptManager, name);
	}

	/**
	 * @return the scriptFile
	 */
	public File getScriptFile() {
		return scriptFile;
	}

	/**
	 * @param scriptFile the scriptFile to set
	 */
	public void setScriptFile(File scriptFile) {
		this.scriptFile = scriptFile;
	}

}
