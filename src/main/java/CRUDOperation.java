import java.sql.*;

public class CRUDOperation {
    // READ: View all orders
    public static void showOrders(Connection conn) {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM orders")) {
            System.out.println("\n--- LSMS Order List ---");
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.println("ID: " + rs.getInt("order_id") + 
                                   " | Amount: P" + rs.getDouble("total_amount") + 
                                   " | Status: " + rs.getString("status"));
            }
            if (!hasData) System.out.println("No orders found in database.");
        } catch (SQLException e) {
            System.out.println("Query Error: " + e.getMessage());
        }
    }

    // CREATE: Book a new service
    public static void bookOrder(Connection conn) {
        System.out.println("\n--- Book New Service ---");
        double amount = (double) InputHelper.readInt("Enter Total Amount");
        String status = "Pending";

        String query = "INSERT INTO orders (customer_id, total_amount, status) VALUES (1, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDouble(1, amount);
            pstmt.setString(2, status);
            pstmt.executeUpdate();
            System.out.println("Order saved successfully!");
        } catch (SQLException e) {
            System.out.println("Insert Error: " + e.getMessage());
        }
    }

    // UPDATE: Change the status of an order
    public static void updateStatus(Connection conn) {
        int id = InputHelper.readInt("Enter Order ID to update");
        System.out.println("New Status: 1. Processing | 2. Ready | 3. Delivered");
        int statusChoice = InputHelper.readInt("Choose Status");
        
        String status = "Pending";
        if (statusChoice == 1) status = "Processing";
        else if (statusChoice == 2) status = "Ready";
        else if (statusChoice == 3) status = "Delivered";

        String query = "UPDATE orders SET status = ? WHERE order_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            int rows = pstmt.executeUpdate();
            
            if (rows > 0) System.out.println("Status updated to " + status + "!");
            else System.out.println("Order ID not found.");
        } catch (SQLException e) {
            System.out.println("Update Error: " + e.getMessage());
        }
    }

    // DELETE: Remove an order from the database
    public static void deleteOrder(Connection conn) {
        int id = InputHelper.readInt("Enter Order ID to DELETE");
        String query = "DELETE FROM orders WHERE order_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            
            if (rows > 0) System.out.println("Order #" + id + " has been deleted.");
            else System.out.println("Order ID not found.");
        } catch (SQLException e) {
            System.out.println("Delete Error: " + e.getMessage());
        }
    }

    public static void processPayment(Connection conn) {
    System.out.println("\n--- Process Payment ---");
    int orderId = InputHelper.readInt("Enter Order ID to pay (e.g., 101)");

    // 1. Fetch details to display like the flowchart
    String query = "SELECT o.order_id, u.first_name, u.last_name, o.weight_kg, o.service_type " +
                   "FROM orders o JOIN users u ON o.customer_id = u.user_id " +
                   "WHERE o.order_id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setInt(1, orderId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
            double kg = rs.getDouble("weight_kg");
            double pricePerKg = 75.0; // From your flowchart
            double total = kg * pricePerKg;

            // Display details exactly like the flowchart
            System.out.println("\nCustomer: " + fullName);
            System.out.println("Items: " + kg + "kg (P" + pricePerKg + "/kg) Wash & Fold");
            System.out.println("Total Amount: P" + total);

            System.out.println("\n[1] Pay with Cash\n[2] Pay with Card\n[3] Cancel");
            int payChoice = InputHelper.readInt("Action");

            if (payChoice == 1 || payChoice == 2) {
                // Update database to marked as paid
                String updateQuery = "UPDATE orders SET is_paid = TRUE WHERE order_id = ?";
                PreparedStatement upstmt = conn.prepareStatement(updateQuery);
                upstmt.setInt(1, orderId);
                upstmt.executeUpdate();
                System.out.println("Payment Successful!"); // Flowchart final step
            }
        } else {
            System.out.println("Order ID not found.");
        }
    } catch (SQLException e) {
        System.out.println("Payment Error: " + e.getMessage());
    }
}
}