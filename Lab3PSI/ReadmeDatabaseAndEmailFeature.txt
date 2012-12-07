4) Inline Method
// a code example from function getEmail() where we have two return statements that //basically means the same thing that there is no such record. 
	        try {
	        	rs.next();}
	        catch (SQLException e) {
	        	return "no record next";
	        }
	        try {
				email = rs.getString("email");
			} catch (SQLException e) {
				return "No record found";
			}

		  return email;
	
The code was changed to this:

try   {
if (rs.next()){
email = rs.getString("email");
}
} 
catch (SQLException e) 
{
return "no record found";
}
Benefits: The functions which have seemed badly factored are grouped together to short the code.   


5) Replace Method with Method Object

Before:
//code responsible for setting the email from an external file and send it to someone
try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("administrator"));
			message.setRecipients(Message.RecipientType.TO,
			InternetAddress.parse(email));
			message.setSubject("You've been amendet ");
			message.setText("The sum is: "+Sum);			
			Transport.send(message);
	        
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		
		
		}
		return "Message send";	
	}

After:
ClassEmailMessage message = new ClassEmailMessage();
			String responce = message.sendEmail(session, email, sum);		
		return responce;


public class ClassEmailMessage {
public String sendEmail (Session session,String email, String sum)
{	
	Message message = new MimeMessage(session);
	try {
		message.setFrom(new InternetAddress("administrator"));
	} catch (MessagingException e) {
	return "Cannot Login with current user and password";
	}
	try {
		message.setRecipients(Message.RecipientType.TO,
		InternetAddress.parse(email));
	} catch (MessagingException e) {
		return "Cannot connect to mailbox";
	}
	try {
		message.setSubject("You've been amendet ");
	} catch (MessagingException e) {
		return "Cannot set the subject";
	}
	try {
		message.setText("The sum is:"+sum);
	} catch (MessagingException e1) {
		return "Cannot set the text";
	}
	try {
		Transport.send(message);
	} catch (MessagingException e) {
		return "Cannot send the message";
	}

   return "the message was sent";
}
}

Benefits: By extracting pieces out of a large method, the things became much more comprehensible.
