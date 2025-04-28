import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    
    static {
        // Load the properties from the configuration file
        try (FileInputStream input = new FileInputStream("src/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            URL = prop.getProperty("db.url", URL);
            USER = prop.getProperty("db.username", USER);
            PASSWORD = prop.getProperty("db.password", PASSWORD);
        } catch (IOException e) {
            System.out.println("Error loading config.properties file");
            e.printStackTrace();
        }
    }
   
   
    public static Connection getConnection()
    {
        try 
        {
            Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);
            System.out.println("Connection Successful");
            return conn;
        }
        catch(SQLException e)
        {
            System.out.println("Connection Failed!");
            e.printStackTrace();
            return null;
        }
    }
}
