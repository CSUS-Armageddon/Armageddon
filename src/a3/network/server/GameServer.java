package a3.network.server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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
	
	private static final int DEFAULT_FONT_SIZE = 18;

	private final ServerConfig sc;
	
	private boolean serverStarted = false;
	private UDPGameServer gameServer;
	
	private JComboBox<String> serverIPAddress;
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
			ServerLogger.INSTANCE.addFilter(MessageType.REQUEST);
			ServerLogger.INSTANCE.addFilter(MessageType.DETAILS);
			this.setVisible(true);
		} catch (UnknownHostException e) {
			ServerLogger.INSTANCE.log(e);
		}
	}
	
	public static void main(String[] args) throws IOException {
		new GameServer();
	}
	
	private void initWindow() throws UnknownHostException {
		this.setTitle(sc.getString("server.name", "An Armageddon Server"));
		this.setPreferredSize(new Dimension(1024, 768));
		
		final JLabel serverIpAddressLabel = new JLabel("Server IP:");
		final JLabel serverPortLabel = new JLabel("Server Port:");
		final JLabel serverNameLabel = new JLabel("Server Name:");
		
		final List<String> ipList = new ArrayList<String>();
		Arrays.stream(InetAddress.getAllByName(InetAddress.getLocalHost().getHostName()))
			.forEach(e -> ipList.add(e.getHostAddress()));
		final String[] ipArray = new String[ipList.size()];
		ipList.toArray(ipArray);
		serverIPAddress = new JComboBox<String>(ipArray);
		
		serverPort = new JTextField(String.valueOf(sc.getInt("server.port", 6868)));
		serverName = new JTextField(sc.getString("server.name", "An Armageddon Server"));
		
		startStopServer = new JButton("Start");
		startStopServer.addActionListener(new StartStopButtonAction());
		
		serverMessageBox = new JTextArea();
		serverMessageBox.setEditable(false);
		
		setFont(serverIpAddressLabel);
		setFont(serverIPAddress);
		setFont(serverPortLabel);
		setFont(serverPort);
		setFont(serverNameLabel);
		setFont(serverName);
		setFont(startStopServer);
		setFont(serverMessageBox);
		
		scrollPane = new JScrollPane(serverMessageBox);
		
		this.getContentPane().setLayout(new BorderLayout());
		
		final JPanel topContainer = new JPanel();
		topContainer.setLayout(new FlowLayout());
		
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
		
		final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
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
	
	private void setFont(JComponent comp) {
		comp.setFont(new Font(comp.getFont().getName(), Font.PLAIN, DEFAULT_FONT_SIZE));
	}
	
	private class StartStopButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (serverStarted) {
				serverStarted = false;
				gameServer.shutdown();
				gameServer = null;
				startStopServer.setText("Start");
				serverIPAddress.setEditable(true);
				serverPort.setEditable(true);
				serverName.setEditable(true);
			} else {
				try {
					gameServer = new UDPGameServer(
							serverIPAddress.getSelectedItem().toString(),
							Integer.parseInt(serverPort.getText()),
							serverName.getText().trim());
					startStopServer.setText("Stop");
					serverIPAddress.setEditable(false);
					serverPort.setEditable(false);
					serverName.setEditable(false);
					setTitle(serverName.getText().trim());
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
