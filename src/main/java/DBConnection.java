import java.sql.*;

public class DBConnection {
    public static Connection getConnection() {
        try {
            // Check: XAMPP MySQL must be green/running!
            String url = "jdbc:mysql://localhost:3306/lsms_db"; 
            return DriverManager.getConnection(url, "root", "");
        } catch (SQLException e) {
            System.out.println("DB Error: " + e.getMessage());
            return null;
        }
    }
}