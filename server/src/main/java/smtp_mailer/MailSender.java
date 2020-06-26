package smtp_mailer;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.tone.notekeeper.ServerConfig;

public class MailSender {
	
	private static String fromEmail = ServerConfig.fromEmail;
	private static String smtpHost = ServerConfig.smtpHost;
	private static String smtpPort = ServerConfig.smtpPort;
	private static String username = ServerConfig.username;
	private static String password = ServerConfig.password;
	// Most essentials together (but almost everything is optional):

	// ConfigLoader.loadProperties("simplejavamail.properties"); // optional default
	// ConfigLoader.loadProperties("overrides.properties"); // optional extra
	
	public static Boolean sendMail(String toEmail, String subject, String mess) throws InterruptedException {
	
	      // Get system properties
		  Properties props = System.getProperties();

	      // Setup mail server
	      props.put("mail.smtp.host", smtpHost);
	      props.put("mail.smtp.port", smtpPort);
	      props.put("mail.smtp.auth", "true");
	      
	      props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
	      // props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
		  // props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
	      
	      Authenticator auth = new Authenticator() {
				//override the getPasswordAuthentication method
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
	      };
	      
	      System.out.println(mess);
	     
	      // Get the default Session object.
	      System.out.println("before: " + auth);
	      
	      Session session = Session.getInstance(props, auth);
	      
	      System.out.println(session);

	      try {
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(fromEmail));

	         // Set To: header field of the header.
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

	         // Set Subject: header field
	         message.setSubject(subject);

	         // Now set the actual message
	         message.setText(mess);
	         
	         System.out.println(message);

	         // Send message
	         Transport.send(message);
	         System.out.println("Sent message successfully....");
	         return true;
	      } catch (MessagingException mex) {
	         mex.printStackTrace();
	         return false;
	      }
	}
}

