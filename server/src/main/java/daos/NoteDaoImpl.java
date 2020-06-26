package daos;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db_connector.ConnectionFactory;
import models.Note;

public class NoteDaoImpl implements NoteDao {
	
Connection connection = ConnectionFactory.getConnection();
	

	private Note extractNoteFromResultSet(ResultSet rs) throws SQLException {
		Note note = new Note();
		note.setCreatedOn( rs.getTimestamp("CreatedOn") );
		note.setTitle( rs.getString("title") );
		note.setNote( rs.getString("note") );
		note.setEmail( rs.getString("email") );
		return note;
	}
	
	public List<Note> getAllNotes (String email) {
		try {
			
			System.out.println("NoteDao - get all");
			
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Note where email=\"" + email + "\"");
			
			List<Note> notes = new ArrayList<Note>();
			
			while(rs.next()) {
				Note note = extractNoteFromResultSet(rs);
				notes.add(note);
			}
			
			return notes;
		} catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return null;
	}
	
	public Boolean newNote (Note note) {
		 try {
		        PreparedStatement ps = connection.prepareStatement("INSERT INTO Note VALUES (NULL, ?, ?, ?, ?)");
		        ps.setTimestamp(1, note.getCreatedOn());
		        ps.setString(2, note.getEmail());
		        ps.setString(3, note.getTitle());
		        ps.setString(4, note.getNote());
		        int i = ps.executeUpdate();
		      if(i == 1) {
		        return true;
		      }
		    } catch (SQLException ex) {
		        ex.printStackTrace();
		    }
		    return false;
	}
	

	public Boolean updateNote(Note note) {
		try {
		    PreparedStatement ps = connection.prepareStatement("UPDATE Note SET CreatedOn=?, title=?, note=? WHERE id=?");
		    ps.setTimestamp(1, note.getCreatedOn());
	        ps.setString(2, note.getTitle());
	        ps.setString(3, note.getNote());
	        ps.setInt(4, note.getId());
		    int i = ps.executeUpdate();
		  if(i == 1) {
			  return true;
		  }
		} catch (SQLException ex) {
		    ex.printStackTrace();
		}
			return false;
	}
	

	public Boolean deleteNote(Integer id) {
	    try {
	        Statement stmt = connection.createStatement();
	        int i = stmt.executeUpdate("DELETE FROM Note WHERE id=" + id);
	      if(i == 1) {
	    	  return true;
	      }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    	return false;
		}

	public Boolean deleteAllNotes(String email) {
		try {
	        Statement stmt = connection.createStatement();
	        int i = stmt.executeUpdate("DELETE FROM Note WHERE email=\"" + email + "\"");
	      if(i == 1) {
	    	  return true;
	      }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    	return false;
	}

}
