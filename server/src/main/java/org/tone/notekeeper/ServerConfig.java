package org.tone.notekeeper;

public class ServerConfig {
	
	//SMTP Settings
	public static final String fromName = "your_name";
	public static final String fromEmail = "your_email";
	public static final String smtpHost = "smtp.emailhost.com";
	public static final String smtpPort = "587";
	public static final String username = "your_email_username"; //Email User
	public static final String password = "your_email_password"; //Email PW

	//JWT Settings
	public static final String jwtSecret = "your_secret_key";
	public static final Integer jwtExpIn = 86400000; //Time to jwt expiry in millisecsonds
	public static final String jwtIssuer = "Notekeeper";

	//MySql Settings
	public static final String dbUrl = "jdbc:mysql://localhost:3306/notekeeper?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	public static final String dbUser = "db_user";
	public static final String dbPassword = "db_password";

}
