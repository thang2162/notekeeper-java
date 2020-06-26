package models;

import java.sql.Timestamp;

public class User {
	
	private Integer id;
	private Timestamp CreatedOn;
	private String email;
	private String password;
	private String resetKey;
	
	public User () {
		
	}
	
	public User (Timestamp CreatedOn, String email, String password) {
		this.CreatedOn = CreatedOn;
		this.email = email;
		this.password = password;
	}
	
	public User (Timestamp CreatedOn, String email, String password, Integer id) {
		this.CreatedOn = CreatedOn;
		this.email = email;
		this.password = password;
		this.id = id;
	}
	
	public User (Timestamp CreatedOn, String email, String password, Integer id, String resetKey) {
		this.CreatedOn = CreatedOn;
		this.email = email;
		this.password = password;
		this.id = id;
		this.resetKey = resetKey;
	}
	
	public void setCreatedOn (Timestamp CreatedOn) {
		this.CreatedOn = CreatedOn;
	}
	
	public void setId (Integer id) {
		this.id = id;
	}
	
	public void setPassword (String password) {
		this.password = password;
	}
	
	public void setEmail (String email) {
		this.email = email;
	}
	
	public void setResetKey (String resetKey) {
		this.resetKey = resetKey;
	}
	
	public Timestamp getCreatedOn () {
		return CreatedOn;
	}
	
	public String getPassword () {
		return password;
	}
	
	public Integer getId () {
		return id;
	}
	
	public String getEmail () {
		return email;
	}
	
	public String getResetKey () {
		return resetKey;
	}

}
