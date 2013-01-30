

	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;

	//connecting to database
	public class ConnectDB {	  	  	  
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
			  
	          stmt=connect("jdbc:mysql://localhost:3307/psi", "root", "root");		  
	          ResultSet rs = null;
	          String email = null; 
			  String SQL = "SELECT * FROM psi.carwash "+
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
				return "No Record Found";
			}		  

			  return email;
		  }
	}

