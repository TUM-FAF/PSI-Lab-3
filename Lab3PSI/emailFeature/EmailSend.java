package emailFeature;
import java.io.InputStream;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class EmailSend {
public final String sendEmail (final String email, final String sum) throws Throwable
{
        Properties props = new Properties();
InputStream in = null;
       try {
       in = this.getClass().getResourceAsStream("mail.properties");
       props.load(in);
       } finally {
          if (null != in) {
        in.close();
}
}
        final String username = props.getProperty("mail.login.user");
        final String password = props.getProperty("mail.login.password");
        Session session = Session.getInstance(props,
        
        new Authenticator(){
		protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication(username, password);
		      }
		      }
		);
			ClassEmailMessage message = new ClassEmailMessage();
			String responce = message.sendEmail(session, email, sum);		
		return responce;
	}

   





}
