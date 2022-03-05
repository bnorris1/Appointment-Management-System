package utilities;
import java.sql.Connection;
import java.sql.DriverManager;

public abstract class JDBConnection {

    private static final String jdbcUrl = "jdbc:mysql://localhost:3306/client_schedule?connectionTimeZone=SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "sqlUser";
    private static final String password = "Passw0rd!";
    public static Connection connection;

    public static void openConnection() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(jdbcUrl, userName, password);
            System.out.println("Connection established!");
        }
        catch (Exception e) {System.out.println("Error:" + e.getMessage());}
    }

    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch (Exception e) {System.out.println("Error:" + e.getMessage());}
    }
}