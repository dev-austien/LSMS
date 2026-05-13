import java.sql.*;

public class AuthService {
    public static String login(Connection conn) {
        System.out.println("\n--- LOGIN ---");
        String user = InputHelper.readString("Username");
        String pass = InputHelper.readString("Password");

        String query = "SELECT role FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("role"); // Returns 'Worker' or 'Customer'
            }
        } catch (SQLException e) {
            System.out.println("Auth Error: " + e.getMessage());
        }
        return null; // Login failed
    }

    public static void register(Connection conn) {
    System.out.println("\n--- REGISTER ---");
    String first = InputHelper.readString("First Name");
    String last = InputHelper.readString("Last Name");
    String user = InputHelper.readString("Username");
    String pass = InputHelper.readString("Password");
    System.out.println("Role: 1. Worker | 2. Customer");
    int roleChoice = InputHelper.readInt("Select Role");
    String role = (roleChoice == 1) ? "Worker" : "Customer";

    String query = "INSERT INTO users (first_name, last_name, username, password, role) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, first);
        pstmt.setString(2, last);
        pstmt.setString(3, user);
        pstmt.setString(4, pass);
        pstmt.setString(5, role);
        pstmt.executeUpdate();
        System.out.println("Registration Successful! You can now login.");
    } catch (SQLException e) {
        System.out.println("Registration Error: " + e.getMessage());
    }
}
}
