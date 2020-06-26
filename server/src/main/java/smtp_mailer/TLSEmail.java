package smtp_mailer;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.tone.notekeeper.ServerConfig;


public class TLSEmail {

	/**
	   Outgoing Mail (SMTP) Server
	   requires TLS or SSL: smtp.gmail.com (use authentication)
	   Use Authentication: Yes
	   Port for TLS/STARTTLS: 587
	 */
	public static Boolean sendMail(String toEmail, String sub, String mess) {
		
		System.out.println("TLSEmail Start");
		Properties props = new Properties();
		props.put("mail.smtp.host", ServerConfig.smtpHost); //SMTP Host
		props.put("mail.smtp.port", ServerConfig.smtpPort); //TLS Port
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
		
                //create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(ServerConfig.username, ServerConfig.password);
			}
		};
		
		
		Session session = Session.getInstance(props, auth);
		
		return EmailUtil.sendEmail(session, toEmail, sub, mess);
		
		
	}

	
}