package grice.c195.helper;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * The JDBC class is responsible for opening and closing the connection to the database.
 * It provides static methods that can be called from other classes to open and close the connection.
 * The class uses a Connection object to store the connection to the database, which can be retrieved with getConnection().
 */
public abstract class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    public static Connection connection;  // Connection Interface

    /**
     * The openConnection method is used to open the connection to the database.
     */
    public static void openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            // Password
            String password = "Passw0rd!";
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    /**
     * The closeConnection method is used to close the connection to the database.
     */
    public static void closeConnection() {
        try {
            connection.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

