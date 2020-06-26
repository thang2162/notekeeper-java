package db_connector;


import com.mysql.cj.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.tone.notekeeper.ServerConfig;

import java.sql.ResultSet;

/**
 * Connect to Database
 * @author hany.said
 */
public class ConnectionFactory {
    public static final String URL = ServerConfig.dbUrl;
    public static final String USER = ServerConfig.dbUser;
    public static final String PASS = ServerConfig.dbPassword;
    /**
     * Get a connection to database
     * @return Connection object
     */
    public static Connection getConnection()
    {
      try {
          DriverManager.registerDriver(new Driver());
          return DriverManager.getConnection(URL, USER, PASS);
      } catch (SQLException ex) {
          throw new RuntimeException("Error connecting to the database", ex);
      }
    }
    /**
     * Test Connection
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection connection = ConnectionFactory.getConnection();
        
        ResultSet rs = connection.prepareStatement("show tables").executeQuery();
        
        while(rs.next()){
            String s = rs.getString(1);
            System.out.println(s);
        }
    }
}
