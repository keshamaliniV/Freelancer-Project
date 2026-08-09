import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBConnection{
    private static final String URL =
            "jdbc:mysql://localhost:3306/freelancer_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Kesha@6906";
    public static Connection getConnection(){
        try{
            Connection connection =
                    DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database Connected Successfully!");
            return connection;
        }
        catch (SQLException e){
            System.out.println("Database Connection Failed!");
            e.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args) {
    Connection con = getConnection();

    if (con != null) {
        System.out.println("Connection Test Successful!");
    }
    }
}