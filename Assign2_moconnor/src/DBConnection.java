
/**
 * The DB class of Multi-Threaded Client Server application
 * 
 * 
 * @author Matthew O'Connor
 * @version 12/10/2018 
 * BSc Applied Computing Year 4
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

	/** The name of the MySQL account to use (or empty for anonymous) */
	private final String userName = "root";

	/** The password for the MySQL account (or empty for anonymous) */
	private final String password = "root";

	/** The name of the computer running MySQL */
	private final String serverName = "localhost";

	/** The port of the MySQL server (default is 3306) */
	private final int portNumber = 3306;

	/** The name of the database we are testing with (this default is installed with MySQL) */
	private final String dbName = "Assign2";

	/** The name of the table we are testing with */
	private final String tableName = "myStudents";

	private Statement myStmt;
	private ResultSet myRs;


	/**
	 * Create a DBConnection instance. Initialise its state so that it is ready for use.
	 */
	public DBConnection() {

	}


	/**
	 * Run a SQL command which does not return a recordset: CREATE/INSERT/UPDATE/DELETE/DROP/etc.
	 * 
	 * @throws SQLException
	 *             If something goes wrong
	 */
	public boolean executeUpdate(String command) throws SQLException {

		try {

			myStmt.executeUpdate(command); // This will throw a SQLException if it fails
			return true;
		}
		finally {

			// This will run whether we throw an exception or not
			if (myStmt != null) {
				myStmt.close();
			}
		}
	}


	public void connect() {

		try {

			// connection to database
			System.out.println("....Attempting to connect to Database....\n");
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://" + serverName + ":" + portNumber + "/" + dbName + "?autoReconnect=true&useSSL=false", userName,
					password);
			// &useSSL=false will suppress warning on phpMyAdmin

			// create statement
			myStmt = myConn.createStatement();

			// execute sql query
			myRs = myStmt.executeQuery("select * from " + tableName);

		}
		catch (Exception exc) {
			exc.printStackTrace();
		}

	}


	// Getter for Results set
	public ResultSet getRs() {
		return myRs;
	}


	// Getter for Statement
	public Statement getmyStmt() {
		return myStmt;
	}
}
