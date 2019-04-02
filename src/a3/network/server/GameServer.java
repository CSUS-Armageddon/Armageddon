package a3.network.server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import a3.network.api.messages.MessageType;
import a3.network.logging.ServerLogger;
import a3.network.server.impl.UDPGameServer;

public class GameServer extends JFrame {

	private static final long serialVersionUID = 3258963953337964850L;

	private final ServerConfig sc;
	
	private boolean serverStarted = false;
	private UDPGameServer gameServer;
	
	private JTextField serverIPAddress;
	private JTextField serverPort;
	private JTextField serverName;
	private JButton startStopServer;
	private JTextArea serverMessageBox;
	private JScrollPane scrollPane;
	
	public GameServer() {
		this.sc = new ServerConfig("assets/config/server.properties");
		try {
			initWindow();
			ServerLogger.INSTANCE.setLogWindow(serverMessageBox, scrollPane);
			ServerLogger.INSTANCE.addFilter(MessageType.MOVE);
			ServerLogger.INSTANCE.addFilter(MessageType.ROTATE);
			this.setVisible(true);
		} catch (UnknownHostException e) {
			ServerLogger.INSTANCE.log(e);
		}
	}
	
	public static void main(String[] args) throws IOException {
		new GameServer();
	}
	
	private void initWindow() throws UnknownHostException {
		this.setName(sc.getString("server.name", "A BattleHatch Server"));
		//this.setResizable(false);
		this.setPreferredSize(new Dimension(800, 300));
		
		final JLabel serverIpAddressLabel = new JLabel("Server IP:");
		final JLabel serverPortLabel = new JLabel("Server Port:");
		final JLabel serverNameLabel = new JLabel("Server Name:");
		
		serverIPAddress = new JTextField(InetAddress.getLocalHost().getHostAddress().toString());
		serverPort = new JTextField(String.valueOf(sc.getInt("server.port", 6868)));
		serverName = new JTextField(sc.getString("server.name", "A BattleHatch Server"));
		
		startStopServer = new JButton("Start");
		startStopServer.addActionListener(new StartStopButtonAction());
		
		serverMessageBox = new JTextArea();
		serverMessageBox.setEditable(false);
		
		scrollPane = new JScrollPane(serverMessageBox);
		scrollPane.setPreferredSize(new Dimension(580, 25));
		
		this.getContentPane().setLayout(new BorderLayout());
		
		final JPanel topContainer = new JPanel();
		topContainer.setLayout(new FlowLayout());
		topContainer.setSize(580, 100);
		
		topContainer.add(serverIpAddressLabel);
		topContainer.add(serverIPAddress);
		topContainer.add(serverPortLabel);
		topContainer.add(serverPort);
		topContainer.add(serverNameLabel);
		topContainer.add(serverName);
		
		serverIPAddress.setEditable(false);
		
		this.getContentPane().add(topContainer, BorderLayout.BEFORE_FIRST_LINE);
		
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		this.getContentPane().add(startStopServer, BorderLayout.AFTER_LAST_LINE);
		
		this.pack();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (serverStarted) {
					serverStarted = false;
					gameServer.shutdown();
					gameServer = null;
					startStopServer.setText("Start");
					serverPort.setEditable(true);
					serverName.setEditable(true);
				}
			}
		});
	}
	
	private class StartStopButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (serverStarted) {
				serverStarted = false;
				gameServer.shutdown();
				gameServer = null;
				startStopServer.setText("Start");
				serverPort.setEditable(true);
				serverName.setEditable(true);
			} else {
				try {
					gameServer = new UDPGameServer(Integer.parseInt(serverPort.getText()));
					startStopServer.setText("Stop");
					serverPort.setEditable(false);
					serverName.setEditable(false);
					serverStarted = true;
				} catch (NumberFormatException e) {
					ServerLogger.INSTANCE.log(e);
				} catch (IOException e) {
					ServerLogger.INSTANCE.log(e);
				}
				
			}
		}
	}
	
}
