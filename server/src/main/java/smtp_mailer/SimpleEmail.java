package smtp_mailer;

import java.util.Properties;

import javax.mail.Session;

import org.tone.notekeeper.ServerConfig;

public class SimpleEmail {
	
	public static Boolean sendMail (String toEmail, String sub, String mess) {
		
	    System.out.println("SimpleEmail Start");
	    
	    Properties props = System.getProperties();
	    
	    props.put("mail.smtp.host", ServerConfig.smtpHost);
	    props.put("mail.smtp.port", ServerConfig.smtpPort);

	    Session session = Session.getInstance(props, null);
	    
	    return EmailUtil.sendEmail(session, toEmail, sub, mess);
	}

}