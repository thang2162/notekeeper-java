package daos;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Statement;

import db_connector.ConnectionFactory;

import models.User;

public class UserDaoImpl implements UserDao {

	private User extractUserFromResultSet(ResultSet rs) throws SQLException {
		User user = new User();
		user.setCreatedOn( rs.getTimestamp("CreatedOn") );
		user.setEmail( rs.getString("email") );
		user.setPassword( rs.getString("password") );
		user.setId( rs.getInt("id") );
		System.out.println(rs.getString("resetKey"));
		if(rs.getString("resetKey") != null) {
			user.setResetKey( rs.getString("resetKey") );
		}
		
		return user;
	}
	
	public User getUser (String email) {
		
		Connection connection = ConnectionFactory.getConnection();
			
		System.out.println(connection);
		
		try {
	        PreparedStatement ps = connection.prepareStatement("SELECT * FROM User WHERE email=?" /*AND pass=?"*/);
	        ps.setString(1, email);
	        // ps.setString(2, pass);
	        ResultSet rs = ps.executeQuery();
	        if(rs.next())
	        {
	        	return extractUserFromResultSet(rs);
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return null;
	}
	
	public Boolean newUser (User user) {
		
		System.out.println("UserDao - new user");
		
		Connection connection = ConnectionFactory.getConnection();
		
		System.out.println(connection);
		
		 try {
		        PreparedStatement ps = connection.prepareStatement("INSERT INTO User VALUES (NULL, ?, ?, ?, NULL)");
		        ps.setTimestamp(1, user.getCreatedOn());
		        ps.setString(2, user.getEmail());
		        ps.setString(3, user.getPassword());
		        int i = ps.executeUpdate();
		      if(i == 1) {
		        return true;
		      }
		    } catch (SQLException ex) {
		        ex.printStackTrace();
		    }
		    return false;
	}
	

	public Boolean updatePassword(User user, String new_password) {
		
		Connection connection = ConnectionFactory.getConnection();
		
		System.out.println(connection);
		
		
		try {
		    PreparedStatement ps = connection.prepareStatement("UPDATE User SET password=? WHERE email=?");
		    ps.setString(1, new_password);
	        ps.setString(2, user.getEmail());
		    int i = ps.executeUpdate();
		  if(i == 1) {
			  return true;
		  }
		} catch (SQLException ex) {
		    ex.printStackTrace();
		}
			return false;
	}
	
	public Boolean resetPassword(User user, String new_password) {
		
		Connection connection = ConnectionFactory.getConnection();
		
		System.out.println(connection);
		
		
		try {
			 PreparedStatement ps = connection.prepareStatement("UPDATE User SET resetkey=?, password=? WHERE email=?");
			 ps.setNull(1, java.sql.Types.NULL);;
			 ps.setString(2, new_password);
		     ps.setString(3, user.getEmail());
		    int i = ps.executeUpdate();
		  if(i == 1) {
			  return true;
		  }
		} catch (SQLException ex) {
		    ex.printStackTrace();
		}
			return false;
	}
	
	public Boolean updateResetKey(String email, String resetKey) {
		
		Connection connection = ConnectionFactory.getConnection();
		
		System.out.println(connection);
		
		
		try {
		    PreparedStatement ps = connection.prepareStatement("UPDATE User SET resetKey=? WHERE email=?");
		    ps.setString(1, resetKey);
	        ps.setString(2, email);
		    int i = ps.executeUpdate();
		  if(i == 1) {
			  return true;
		  }
		} catch (SQLException ex) {
		    ex.printStackTrace();
		}
			return false;
	}

	public Boolean deleteUser(Integer id) {
		
		Connection connection = ConnectionFactory.getConnection();
		
		System.out.println(connection);
		
		
	    try {
	        Statement stmt = connection.createStatement();
	        int i = stmt.executeUpdate("DELETE FROM User WHERE id=" + id);
	      if(i == 1) {
	    	  return true;
	      }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    	return false;
		}
}
