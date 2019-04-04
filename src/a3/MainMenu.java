package a3;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import a3.network.server.ServerConfig;

public class MainMenu extends JFrame {

	private static final long serialVersionUID = -2162064079427792952L;
	
	private final ServerConfig sc;
	
	private boolean clientStarted = false;
	
	private JLabel serverIPAddressLabel;
	private JTextField serverIPAddress;
	private JLabel serverPortLabel;
	private JTextField serverPort;
	private JButton startStopButton;
	
	public MainMenu() {
		this.sc = new ServerConfig("assets/config/server.properties");
		try {
			initWindow();
			this.setVisible(true);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new MainMenu();
	}
	
	private void initWindow() throws UnknownHostException {
		this.setName("BattleHatch Client");
		this.setResizable(false);
		this.setPreferredSize(new Dimension(500, 250));
		
		serverIPAddressLabel = new JLabel("Server IP:");
		serverPortLabel = new JLabel("Server Port:");
		
		serverIPAddress = new JTextField();
		serverIPAddress.setPreferredSize(new Dimension(125, 25));
		serverPort = new JTextField(String.valueOf(sc.getInt("server.port", 6868)));
		serverPort.setPreferredSize(new Dimension(75, 25));
		
		startStopButton = new JButton("Start");
		startStopButton.addActionListener(new StartStopButtonAction());
		startStopButton.setPreferredSize(new Dimension(100, 75));
		
		this.getContentPane().setLayout(new BorderLayout());
		
		final JPanel topContainer = new JPanel();
		topContainer.setLayout(new FlowLayout());
		topContainer.setSize(500, 250);
		
		topContainer.add(serverIPAddressLabel);
		topContainer.add(serverIPAddress);
		topContainer.add(serverPortLabel);
		topContainer.add(serverPort);
		
		this.getContentPane().add(topContainer, BorderLayout.BEFORE_FIRST_LINE);
		
		this.getContentPane().add(startStopButton, BorderLayout.AFTER_LAST_LINE);
		
		this.pack();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/*this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (clientStarted) {
					clientStarted = false;
					gameServer.shutdown();
					gameServer = null;
					startStopButton.setText("Start");
					serverPort.setEditable(true);
					serverName.setEditable(true);
				}
			}
		});
		*/
	}
	
	private class StartStopButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (clientStarted) {
				clientStarted = false;
			} else {
				try {
							((JFrame)SwingUtilities.getWindowAncestor(((JButton)evt.getSource()))).setVisible(false);
					final MyGame game = new MyGame(serverIPAddress.getText().trim(),
										Integer.parseInt(serverPort.getText().trim()));
					game.init();
					clientStarted = true;
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				
			}
		}
	}

}
