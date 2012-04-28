package com.jmd.chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;



public class ChatServer {
	private ServerSocket serverSocket;
	private ChatServerGUI chatServerGUI;	
	private String host;
	private int port;
	private boolean running;
	private SimpleDateFormat sdf;

	//Will hold an array of client threads - one for each client that connects
	private ArrayList<ClientThread> clientList;
	
	
	
	ChatServer(){				
		this("localhost", 2000, null);
	}
	ChatServer(String h, int p, ChatServerGUI csg){
		host = h;
		port = p;
		
		//will be null if started from terminal, otherwise hold reference to GUI that started server
		chatServerGUI = csg;
		clientList = new ArrayList<ClientThread>();
		sdf = new SimpleDateFormat("HH:mm:ss");
	}
	
	public void start(){
		running = true;
		try{
			serverSocket = new ServerSocket(port);
			while(running){
				//loop until told to stop
				displayMessage(sdf.format(new Date()) + ": Waiting for clients on port " + port);
				
				//create new socket connection
				Socket socket = serverSocket.accept();
				
				//before doing anything, stop running if requested
				if(!running){
					break;
				}
				
				//create new thread with incoming socket connection
				ClientThread thread = new ClientThread(socket); 
				//add new thread to list
				clientList.add(thread); 
				//start the thread
				thread.start();				
			}
			//if loop ends we want to gracefully close the socket server
			try{
				//close the socket
				serverSocket.close();
				Iterator<ClientThread> i = clientList.iterator();
				while(i.hasNext()){
					//get next client thread and close it
					ClientThread ct = (ClientThread) i.next();					
					ct.close();					
				}				
			}catch(IOException e){
				String msg = sdf.format(new Date()) + ": Exception thrown closing ServerSocket: " + e + "\n";
				displayMessage(msg);	
			}
		}catch(IOException e){
			String msg = sdf.format(new Date()) + ": Exception thrown opening ServerSocket: " + e + "\n";
			displayMessage(msg);
		}		
	}
	
	private void displayMessage(String msg){		
		//if there's no GUI, write to console, else write to GUI
		if(chatServerGUI == null){
			System.out.println(msg);
		}else{
			chatServerGUI.appendMSG(msg);
		}		
	}
	
	//broadcasts a message to each client
	private synchronized void broadcast(String msg){
		String timestamp = sdf.format(new Date());
		String stampedMSG = timestamp + ": " + msg + "\n";
		
		
		if(chatServerGUI == null){
			System.out.println(stampedMSG);			
		}else{
			chatServerGUI.appendMSG(stampedMSG);
		}
		
		//loop through userlist backwards
		//if we fail to send the message we remove the client
		for(int i = clientList.size(); --i >= 0;){
			ClientThread t = clientList.get(i);
			if(!t.writeMessage(stampedMSG)){
				t.close();
				clientList.remove(i);
				displayMessage(timestamp + ": Removing " + t.getUser() + " from server.");
			}
		}
	}
	
	
	
	
	
	
	class ClientThread extends Thread{
		private String userName;
		
		ClientThread(){
			
		}
		
		public ClientThread(Socket socket) {
			// TODO Auto-generated constructor stub
		}

		public void run(){
			
		}
		private void close(){
			
		}
		private boolean writeMessage(String msg){
			return true;
		}
		private String getUser(){
			return userName;
		}

		
	}
	
	
	
}
