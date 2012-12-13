package emailFeature;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
