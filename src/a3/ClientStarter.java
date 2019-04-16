package a3;

public class ClientStarter {
	
	private String ipAddress;
	private int port = -1;
	private boolean isFullScreen = false;
	private String avatarModelName;
	
	private MyGame game;
	
	private boolean doRun = false;
	
	public static void main(String[] args) {
		new ClientStarter().init();
	}
	
	public void init() {
		new MainMenu(this);
		this.game = new MyGame(this.ipAddress, this.port, this.isFullScreen, this.avatarModelName);
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
	 * @return the avatarModelName
	 */
	public String getAvatarModelName() {
		return avatarModelName;
	}

	/**
	 * @param avatarModelName the avatarModelName to set
	 */
	public void setAvatarModelName(String avatarModelName) {
		this.avatarModelName = avatarModelName;
	}
	
}
