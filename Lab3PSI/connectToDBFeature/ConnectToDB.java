package connectToDBFeature;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//connecting to database
public class ConnectToDB {	  	  	  
 public Statement connect(String hostConnect,String User,String Password) {
		  Connection con = null;
		  Statement stmt = null;
		    try{
		        //connect to database
		        con = DriverManager.getConnection(hostConnect, User, Password); 
		        //loading data
		        stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
		        ResultSet.CONCUR_UPDATABLE );		        
		         }
		catch (SQLException err) 
		    {			
			System.out.println("Connection Failed! Check output console");
			err.printStackTrace();
		    }
		    if(con!=null) {
			System.out.println("Connected to database");
			} else {
			System.out.println("Failed to make connection!");
		}
		return stmt;
		}
 
	  
public final String getEmail(final String carnumber) {		
		  Statement stmt;
		  //MyClass test= new MyClass();
          stmt=connect("jdbc:postgresql://127.0.0.1:5432/cars", "postgres", "admin");		  
          ResultSet rs = null;
          String email = null; 
		  String SQL = "SELECT email FROM owners "+
		  "WHERE car_number='"+carnumber+"'";
	       
		  try {
				rs = stmt.executeQuery(SQL);
			} catch (SQLException e) {
				return "error in SQL";
			}
		  	
		  try {
			if (rs.next()){
				email = rs.getString("email");
			}
		} catch (SQLException e) {
			return "no record found";
		}		  
		  		  	        
		  return email;
	  }
}