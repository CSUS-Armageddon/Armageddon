package a3;

import a3.avatar.Avatar;

public class ClientStarter {
	
	private String ipAddress;
	private int port = -1;
	private boolean isFullScreen = false;
	private Avatar avatar;
	
	private MyGame game;
	
	private boolean doRun = false;
	
	public static void main(String[] args) {
		new ClientStarter().init();
	}
	
	public void init() {
		new MainMenu(this);
		this.game = new MyGame(this.ipAddress, this.port, this.isFullScreen, this.avatar);
		if (doRun) {
			this.game.init();
			this.doRun = false;
			init();
		} else {
			System.exit(0);
		}
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the doRun
	 */
	public boolean isDoRun() {
		return doRun;
	}

	/**
	 * @param doRun the doRun to set
	 */
	public void setDoRun(boolean doRun) {
		this.doRun = doRun;
	}

	/**
	 * @return the isFullScreen
	 */
	public boolean isFullScreen() {
		return isFullScreen;
	}

	/**
	 * @param isFullScreen the isFullScreen to set
	 */
	public void setFullScreen(boolean isFullScreen) {
		this.isFullScreen = isFullScreen;
	}

	/**
	 * @return the avatar
	 */
	public Avatar getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}
	
}
