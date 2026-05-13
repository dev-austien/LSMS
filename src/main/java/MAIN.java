import java.sql.*;

public class MAIN {
    public static void main(String[] args) {
        Connection myConn = DBConnection.getConnection();
        if (myConn == null) return;

        while (true) {
            System.out.println("\n--- START ---");
            System.out.println("1. Login\n2. Register\n3. Exit");
            int choice = InputHelper.readInt("Action");

            if (choice == 1) {
                String role = AuthService.login(myConn);
                if (role == null) {
                    System.out.println("Incorrect Username or Password!");
                } else if (role.equals("Worker")) {
                    showWorkerMenu(myConn);
                } else {
                    showCustomerMenu(myConn);
                }
            } else if (choice == 3) break;
        }
    }

    public static void showWorkerMenu(Connection conn) {
    while (true) {
        System.out.println("\n--- WORKER MENU ---");
        System.out.println("1. Show Current Orders\n2. Update Order Status\n3. Register New Customer\n4. Process Payment\n5. Logout");
        int choice = InputHelper.readInt("Action");
        
        if (choice == 1) CRUDOperation.showOrders(conn);
        else if (choice == 2) CRUDOperation.updateStatus(conn);
        else if (choice == 3) AuthService.register(conn); // Link to your register logic
        else if (choice == 4) CRUDOperation.processPayment(conn); // The new payment logic
        else if (choice == 5) break;
    }
}

    public static void showCustomerMenu(Connection conn) {
        while (true) {
            System.out.println("\n--- CUSTOMER MENU ---");
            System.out.println("1. Show Orders\n2. Order History\n3. Logout");
            int choice = InputHelper.readInt("Action");
            if (choice == 1) CRUDOperation.showOrders(conn);
            else if (choice == 3) break;
        }
    }
}