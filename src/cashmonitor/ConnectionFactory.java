package cashmonitor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Gustavo
 */
public class ConnectionFactory {
    
   private Connection con;
   private static final String user="root";
   private static final String pass="mysql";
   private static final String url="jdbc:mysql://localhost:3306/cashmonitor?useSSL=false";
   
   public Connection getConnection() throws SQLException{
       //Establishment of connection with MySQL database
       this.con = DriverManager.getConnection(url, user, pass);
       return this.con;
   }
   
   public void closeConnection() throws SQLException{
       //Closing the connection
       this.con.close();
   }
}
