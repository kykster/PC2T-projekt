package projekt;

public class DBConnectionManager {

    private static DBConnectionManager instance;
    private DBConnection dbConnection;

    private DBConnectionManager() {
    }

    public static synchronized DBConnectionManager getInstance() {
        if (instance == null) {
            instance = new DBConnectionManager();
        }
        return instance;
    }

    public DBConnection getDBConnection() {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }
}