
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AdminManagement {

    static Scanner sc = new Scanner(System.in);

    public static void viewUsers() {
        String sql = "SELECT * FROM users";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println("\n--------------------");

                ResultSet meta = rs;
                System.out.println("User Details:");

                for (int i = 1; i <= meta.getMetaData().getColumnCount(); i++) {
                    System.out.println(
                        meta.getMetaData().getColumnName(i) + ": " +
                        meta.getString(i)
                    );
                }
            }

            if (!found) {
                System.out.println("No users found.");
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void viewJobs() {
        String sql = "SELECT job_id, job_title, description FROM jobs";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println("\n--------------------");
                System.out.println("Job ID: " + rs.getInt("job_id"));
                System.out.println("Title: " + rs.getString("job_title"));
                System.out.println("Description: " +
                                   rs.getString("description"));
            }

            if (!found) {
                System.out.println("No jobs found.");
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void viewStatistics() {
        String[] tables = {"users", "jobs", "applications", "projects", "reviews"};

        try (Connection con = DBConnection.getConnection()) {
            for (String table : tables) {
                String sql = "SELECT COUNT(*) FROM " + table;

                try (PreparedStatement ps = con.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        System.out.println(
                            table + " count: " + rs.getInt(1)
                        );
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== ADMIN MANAGEMENT ===");
            System.out.println("1. View Users");
            System.out.println("2. View Jobs");
            System.out.println("3. View Statistics");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    viewUsers();
                    break;
                case 2:
                    viewJobs();
                    break;
                case 3:
                    viewStatistics();
                    break;
                case 4:
                    System.out.println("Exiting Admin Management.");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
