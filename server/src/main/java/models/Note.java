package models;

import java.sql.Timestamp;

public class Note {
	
	private Timestamp CreatedOn;
	private String title;
	private String note;
	private String email;
	private Integer id;
	
	/* public Note (Date CreatedOn, String email) {
		this.CreatedOn = CreatedOn;
		this.email = email;
	}
	
	public Note (Date CreatedOn, String email, String title) {
		this.CreatedOn = CreatedOn;
		this.email = email;
		this.title = title;
	} */
	
	public Note () {
		
	}
	
	public Note (Timestamp CreatedOn, String email, String title, String note) {
		this.CreatedOn = CreatedOn;
		this.email = email;
		this.title = title;
		this.note = note;
	}
	
	public Note (Timestamp CreatedOn, String email, String title, String note, Integer id) {
		this.CreatedOn = CreatedOn;
		this.email = email;
		this.title = title;
		this.note = note;
		this.id = id;
	}
	
	public void setCreatedOn (Timestamp CreatedOn) {
		this.CreatedOn = CreatedOn;
	}
	
	public void setTitle (String title) {
		this.title = title;
	}
	
	public void setNote (String note) {
		this.note = note;
	}
	
	public void setEmail (String email) {
		this.email = email;
	}
	
	public void setId (Integer id) {
		this.id = id;
	}
	
	public Timestamp getCreatedOn () {
		return CreatedOn;
	}
	
	public String getTitle () {
		return title;
	}
	
	public String getNote () {
		return note;
	}
	
	public String getEmail () {
		return email;
	}
	
	public Integer getId () {
		return id;
	}

}
