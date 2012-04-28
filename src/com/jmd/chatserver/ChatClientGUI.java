package com.jmd.chatserver;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ChatClientGUI extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField userField;
	private JTextField serverLabel, portLabel;
	private JButton loginButton, logoutButton, whoisButton;
	private JTextArea chatBox;
	private JTextArea inputBox;
	private JList userList;
	
	private boolean connected;	
		
	private int defaultPort;
	private String defaultHost;
	
	private ChatClient chatClient;
	
	ChatClientGUI(String host, int port){
		super("Chat Client");
		connected = false;
		initComponents(host, port);		
	}
	
	//Sets up and initializes GUI
	private void initComponents(String host, int port){
		defaultPort = port;
		defaultHost = host;		
		
		JPanel topPanel = new JPanel(new GridLayout(3,2));
		JPanel serverPortLabel = new JPanel(new GridLayout(1,5));
		serverLabel = new JTextField(host);
		portLabel = new JTextField(port);
		portLabel.setHorizontalAlignment(SwingConstants.RIGHT);		
		serverPortLabel.add(new JLabel("Host: "));
		serverPortLabel.add(serverLabel);
		serverPortLabel.add(new JLabel("Port: "));
		serverPortLabel.add(portLabel);				
		userField = new JTextField("Enter User Name");
		
		topPanel.add(serverPortLabel);
		topPanel.add(userField);
		topPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		add(topPanel, BorderLayout.NORTH);
		
		JPanel middlePanel = new JPanel(new GridLayout(1,1));
		chatBox = new JTextArea("Chat box");
		chatBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		middlePanel.add(chatBox);		
		add(middlePanel,BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel(new GridLayout(2,1));
		JPanel buttonPanel = new JPanel(new GridLayout(1,3));
		inputBox = new JTextArea("Enter message here...");		
		loginButton = new JButton("Login");		
		logoutButton = new JButton("Logout");
		whoisButton = new JButton("Who's Here");
		
		buttonPanel.add(loginButton);
		buttonPanel.add(logoutButton);
		buttonPanel.add(whoisButton);
		bottomPanel.setBorder((BorderFactory.createLineBorder(Color.BLACK)));
		bottomPanel.add(inputBox);
		bottomPanel.add(buttonPanel);		
		add(bottomPanel,BorderLayout.SOUTH);
		
		loginButton.addActionListener(this);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 800);
		setVisible(true);		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		System.out.print(source);			
		
	}
	
	public static void main(String[] args) {
		new ChatClientGUI("localhost", 2222);
	}


}
