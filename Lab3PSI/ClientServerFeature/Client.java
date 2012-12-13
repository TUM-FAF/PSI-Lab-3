package ClientServerFeature;

import java.io.*;
import java.net.*;

public class Client{
	Socket requestSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
	

	public final String run(String serverMsg)
	{ 
		String serverMessage = null;
		try
		{
			//1. creating a socket to connect to the server
			int port = 2004;
			requestSocket = new Socket("localhost", port);
			System.out.println("Connected to localhost at port 2004");
			//2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			//3: Communicating with the server
			
			do{
				try{
					message = (String) in.readObject();
					receivedMessage(message);
					sendMessage(serverMsg);
					serverMessage = "bye";
					sendMessage(message);
					}
			
				catch (ClassNotFoundException classNot) {
					System.err.println("data received in unknown format");
				}
			} while (!serverMessage.equals("bye"));
		}
						
		catch (UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch (IOException ioException){
			ioException.printStackTrace();
		}
		finally {
			//4: Closing connection
			try {
				in.close();
				out.close();
				requestSocket.close(); 
				}
			
			catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
		return message;
	}
	
	void receivedMessage(String msg){
		System.out.println("client>" + msg);	
	}
			
	final void sendMessage (String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("client--->" + msg);
		}
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	
}