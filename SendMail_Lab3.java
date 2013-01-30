import java.util.Properties;
 
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
public class SendMail_Lab3 {
 
    private String from;
    private String to;
    private String subject;
    private String text;
 
    public SendMail_Lab3(String from, String to, String subject, String text){
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.text = text;
    }
 
    public String send(){
 
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
 
        Session mailSession = Session.getDefaultInstance(props);
        Message simpleMessage = new MimeMessage(mailSession);
 
        InternetAddress fromAddress = null;
        InternetAddress toAddress = null;
        try {
            fromAddress = new InternetAddress(from);
            toAddress = new InternetAddress(to);
        } catch (AddressException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
        try {
            simpleMessage.setFrom(fromAddress);
        } catch (MessagingException e){
        	return "Current Login and Password incorrect";
        }
        try{
            simpleMessage.setRecipient(RecipientType.TO, toAddress);
        }
        catch (MessagingException e){
        	return "Cannot Connect to mailbox";
        }
        try{    
        	simpleMessage.setSubject(subject);
        }
        catch (MessagingException e){
        	return "Cannot set the Subject";
        }
        try{   
            simpleMessage.setText(text);
        }
        catch (MessagingException e){
        	return "Cannot set the text";
        }
        try{   
        Transport.send(simpleMessage);
        } catch (MessagingException e) {
        	return "Cannot send the message";
        }
		return "The message was succesfuly sent";
    }
}