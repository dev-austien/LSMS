import java.sql.Connection;

public class MAIN {
    public static void main(String[] args) {

        Connection myConn = DBConnection.getConnection();

        if (myConn != null) {
            System.out.println("Status: You are ready to code your LSMS CRUD operations!");
        } else {
            System.out.println("Status: Database connection failed.");
        }
    }
}