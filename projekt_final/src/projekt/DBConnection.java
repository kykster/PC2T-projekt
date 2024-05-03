package projekt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mydatabase";
    private static final String JMENO = "jmeno";
    private static final String HESLO = "heslo";

    private Connection connection;

    public DBConnection() {
        try {
            connection = DriverManager.getConnection(JDBC_URL, JMENO, HESLO);
            System.out.println("Připojen do databáze.");
        } catch (SQLException e) {
            System.out.println("Chyba při připojování k databázi: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void zavritConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Odpojen z databáze.");
            } catch (SQLException e) {
                System.out.println("Chyba při odpojení od databáze: " + e.getMessage());
            }
        }
    }
	public void odpojitzSQLDB() {
	    if (connection != null) {
	        try {
	            connection.close();
	            System.out.println("Odpojen z databáze.");
	        } catch (SQLException e) {
	            System.out.println("Chyba při odpojení od databáze: " + e.getMessage());
	        }
	    }
	}
}
