package com.jmd.chatserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	private static int defaultPort = 2000;
	private static int uniqueID;	
	private boolean running;
	private SimpleDateFormat sdf;

	//Will hold an array of client threads - one for each client that connects
	private ArrayList<ClientThread> clientList;
	
	
	
	ChatServer(){				
		this(defaultPort, null);
	}
	ChatServer(int p){		
		this(p, null);
	}
	ChatServer(int p, ChatServerGUI csg){		
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
			displayMessage("Starting server on port " + port);
			
			while(running){
				//loop until told to stop
				displayMessage("Waiting for clients on port " + port);
				
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
				String msg = "Exception thrown closing ServerSocket: " + e + "\n";
				displayMessage(msg);	
			}
		}catch(IOException e){
			String msg = "Exception thrown opening ServerSocket: " + e + "\n";
			displayMessage(msg);
		}		
	}
	
	private void displayMessage(String msg){		
		//timestamp message
		String timestamp = sdf.format(new Date()) + ": ";
		msg = timestamp + msg;
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
				displayMessage(timestamp + ": Removing " + t.userName + " from server.");
			}
		}
	}
	
	protected void stop(){
		running = false;		
	}
	synchronized void remove(int id){
		for(int i = 0; i < clientList.size(); i++){
			ClientThread t = clientList.get(i);
			if(t.id == id){
				t.close();
				clientList.remove(i);
			}
		}
	}
	
	public static void main(String[] args){
		//if there's no port specified we'll use the default port
		int portNumber = defaultPort;
		if(args.length==1){//if there's one argument, try to parse it for an int
			try{
				portNumber = Integer.parseInt(args[0]);
			}catch(Exception e){
				System.out.println("Exception parsing port number");
				e.printStackTrace();
				System.out.println("\nSpecify port number with: >java ChatServer [port number]");
			}						
		}else if(args.length==0){			
			// do nothing
		}else{
			System.out.println("Specify port number with: >java ChatServer [port number]");
			return;
		}
		
		ChatServer server = new ChatServer(portNumber);
		server.start();	
		
	}
	
	class ClientThread extends Thread{
		//IO Items, object streams because we'll construct our own message object
		Socket socket;
		ObjectInputStream oInput;
		ObjectOutputStream oOutput;		
		
		private int id;
		private String userName;
		
		ChatMessage cm;
		String date;	
		
		
		ClientThread(Socket socket) {
			this.socket = socket;
			id = ++uniqueID;
			
			try{
				oInput = new ObjectInputStream(socket.getInputStream());
				oOutput = new ObjectOutputStream(socket.getOutputStream());				
				//when the client connects, the first thing sent will be the username, so we grab that now
				userName = (String) oInput.readObject();
				displayMessage(userName + " has connected");
			}catch(IOException e){
				System.out.println("Could not create Input/Output stream: " + e);
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.out.println("Error reading username... for some reason");
				e.printStackTrace();
			}			
			date = sdf.format(new Date());
		}

		public void run(){
			//TODO
		}
		private void close(){
			//TODO
		}
		private boolean writeMessage(String msg){
			//TODO
			return true;
		}


		
	}
	
	
	
}
