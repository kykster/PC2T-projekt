package projekt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mydatabase";
    private static final String JMENO = "username";
    private static final String HESLO = "password";

    private Connection connection;

    public DBConnection() {
        try {
            connection = DriverManager.getConnection(JDBC_URL, JMENO, HESLO);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Disconnected from the database.");
            } catch (SQLException e) {
                System.out.println("Error closing the connection: " + e.getMessage());
            }
        }
    }

	public void connectToSQLDB() {
	}

	public void disconnectFromSQLDB() {
	    if (connection != null) {
	        try {
	            connection.close();
	            System.out.println("Disconnected from the database.");
	        } catch (SQLException e) {
	            System.out.println("Error closing the connection: " + e.getMessage());
	        }
	    }
	}
}
