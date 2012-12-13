package ClientServerFeature;

import java.util.HashMap;
import java.util.List;


import feature1.ConnectToDB;

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
