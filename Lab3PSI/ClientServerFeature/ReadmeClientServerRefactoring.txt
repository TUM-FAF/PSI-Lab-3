1. Code inspection
The chosen toolkit to check code is: checkstyle pug-in for Eclipse because it is easy to use and configure. 
Sonar is also a good tool but it is more restrictive, hard to run, doesn’t like my project’s name and is not easy to configure.
2. Refactoring
1) Extract Method
Server Side:
//lines of codes to create socket and wait for the connections
//receiving the message from the client
try{
			message = (String)in.readObject();
			System.out.println("client>" + message);
			if (message.equals("bye"))
			sendMessage("bye");
}

The code was changed to this:

try{
			message = (String)in.readObject();
			receivedMessage(message);
			if (message.equals("bye"))
			sendMessage("bye");
				}

void receivedMessage(String msg){
		System.out.println("client>" + msg);	
	}


	
2) Split Temporary Variable
Server Side:
//lines of codes to create socket and wait for the connections
//receiving the message from the client 
// message is a temporal variable that is used more than ones.
do{
				try{
					message = (String) in.readObject();
					msg = message;
					receivedMessage(msg);
					sendMessage(serverMsg);
					message = "bye";
					sendMessage(message);
					}
			
				catch (ClassNotFoundException classNot) {
			System.err.println("data received in unknown format");
				}
			} while (!message.equals("bye"));
The code was changed to:
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
Benefits: The temporary variable message was used often and was getting in the way of other refactoring such as: implementing Local Extension for Server Class. Also it was confusing the code for other developers. 
Therefore after implementing this refactoring the code became more flexible and understandable.


3) Introduce Local Extension
 Before:
		do{
				try{
				message = (String)in.readObject();
				receivedMessage(message);
                                                           //a lot of code to process message
				if (message.equals("bye"))
				sendMessage("bye");
			}
			catch(ClassNotFoundException classnot){
			System.err.println("Data received in unknown format");
				}
			}
			while(!message.equals("bye"));
		}
		

After:

public class Server extends ServerOperations{
//Creating sockets and waiting for connections lines of code
do
{
try
{
message = (String)in.readObject();
receivedMessage(message);
ProcessData(message);
if (message.equals("bye"))
sendMessage("bye");
}
catch(ClassNotFoundException classnot){
System.err.println("Data received in unknown format");
}
}
while(!message.equals("bye"));
}

public class ServerOperations {
 public String returnConfiramtion(String number)
 {
	 ConnectToDB email = new ConnectToDB();
	 String driverEmail = email.getEmail(number);	 
	return driverEmail;	 
 }
 
public String ProcessData(String line) {	
	ServerOperations obj = new ServerOperations();   
	String[] parts = new String[2];
	parts=line.split(" ");
	String line1 = parts[0];
	String line2="";
	String respond= "bad request";
	if (parts.length==2)
	{line2 = parts[1];}   	
	else
	{line2="";}    		   	
    if (line1.equals("email"))
    	{        	        	         	   	
    	respond = obj.returnConfiramtion(line2);       		
    	} 
	return respond;
} 
}
Benefits: The Server side class is more flexible to changes and in case if we cannot modify the server class we can easily change the class responsible for operation: ServerOperations. 

  

