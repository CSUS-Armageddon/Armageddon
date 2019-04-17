package a3;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import a3.avatar.Avatars;
import a3.network.server.ServerConfig;

public class MainMenu extends JDialog {

	private static final long serialVersionUID = -2162064079427792952L;
	
	private final ServerConfig sc;
	
	private boolean clientStarted = false;
	
	private JTextField serverIPAddress;
	private JTextField serverPort;
	private JCheckBox fullscreen;
	private JComboBox<String> avatarSelect;
	private JButton startStopButton;
	
	private final ClientStarter cs;
	
	private static final int DEFAULT_FONT_SIZE = 18;
	
	public MainMenu(ClientStarter cs) {
		this.cs = cs;
		this.sc = new ServerConfig("assets/config/server.properties");
		try {
			initWindow();
			this.setModal(true);
			this.setVisible(true);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	private void initWindow() throws UnknownHostException {
		this.setTitle("Armageddon Client");
		this.setResizable(false);
		this.setPreferredSize(new Dimension(500, 250));
		
		JLabel serverIPAddressLabel = new JLabel("Server IP:");
		JLabel serverPortLabel = new JLabel("Server Port:");
		
		setFont(serverIPAddressLabel);
		setFont(serverPortLabel);
		
		serverIPAddress = new JTextField(cs.getIpAddress());
		serverIPAddress.setPreferredSize(new Dimension(125, 25));
		
		serverPort = new JTextField(String.valueOf((cs.getPort() == -1) 
				? String.valueOf(sc.getInt("server.port", 6868)) : cs.getPort()));
		serverPort.setPreferredSize(new Dimension(75, 25));
		
		setFont(serverIPAddress);
		setFont(serverPort);
		
		fullscreen = new JCheckBox("Fullscreen", cs.isFullScreen());
		setFont(fullscreen);
		
		avatarSelect = new JComboBox<String>();
		
		for (Avatars avatar : Avatars.values()) {
			avatarSelect.addItem(avatar.getAvatar().getAvatarName());
		}
		setFont(avatarSelect);
		
		startStopButton = new JButton("Start");
		startStopButton.addActionListener(new StartStopButtonAction());
		startStopButton.setPreferredSize(new Dimension(100, 75));
		startStopButton.setFont(new Font(startStopButton.getFont().getName(), Font.PLAIN, 32));
		
		this.getContentPane().setLayout(new BorderLayout());
		
		final JPanel topContainer = new JPanel();
		topContainer.setLayout(new FlowLayout());
		topContainer.setSize(500, 250);
		
		topContainer.add(serverIPAddressLabel);
		topContainer.add(serverIPAddress);
		topContainer.add(serverPortLabel);
		topContainer.add(serverPort);
		
		
		final JPanel middleContainer = new JPanel();
		middleContainer.setLayout(new BoxLayout(middleContainer, BoxLayout.PAGE_AXIS));
		
		fullscreen.setAlignmentX(Component.CENTER_ALIGNMENT);
		avatarSelect.setAlignmentX(Component.CENTER_ALIGNMENT);
		middleContainer.add(fullscreen);
		middleContainer.add(avatarSelect);
		
		this.getContentPane().add(topContainer, BorderLayout.BEFORE_FIRST_LINE);
		this.getContentPane().add(middleContainer, BorderLayout.CENTER);		
		this.getContentPane().add(startStopButton, BorderLayout.AFTER_LAST_LINE);
		
		final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		this.pack();
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	private void setFont(JComponent comp) {
		comp.setFont(new Font(comp.getFont().getName(), Font.PLAIN, DEFAULT_FONT_SIZE));
	}
	
	private class StartStopButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (clientStarted) {
				clientStarted = false;
			} else {
				try {
					cs.setIpAddress(serverIPAddress.getText().trim());
					cs.setPort(Integer.parseInt(serverPort.getText().trim()));
					cs.setFullScreen(fullscreen.isSelected());
					cs.setAvatar(Avatars.fromAvatarName((String)avatarSelect.getSelectedItem()));
					cs.setDoRun(true);
					clientStarted = true;
					dispose();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				
			}
		}
	}

}
