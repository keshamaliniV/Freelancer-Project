
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class NotificationManagement {

    static Scanner sc = new Scanner(System.in);

    public static void viewApplicationNotifications() {
        System.out.print("Enter Freelancer ID: ");
        int freelancerId = sc.nextInt();
        sc.nextLine();

        String sql =
            "SELECT application_id, job_id, status " +
            "FROM applications WHERE freelancer_id = ? " +
            "ORDER BY applied_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, freelancerId);

            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;

                while (rs.next()) {
                    found = true;
                    String status = rs.getString("status");

                    System.out.println("\n--- Application Notification ---");
                    System.out.println("Application ID: " +
                                       rs.getInt("application_id"));
                    System.out.println("Job ID: " + rs.getInt("job_id"));

                    if ("HIRED".equalsIgnoreCase(status)) {
                        System.out.println(
                            "Congratulations! Your application was accepted."
                        );
                    } else if ("REJECTED".equalsIgnoreCase(status)) {
                        System.out.println(
                            "Your application was rejected."
                        );
                    } else {
                        System.out.println(
                            "Your application is waiting for review."
                        );
                    }

                    System.out.println("Current Status: " + status);
                }

                if (!found) {
                    System.out.println("No application notifications found.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void viewProjectNotifications() {
        System.out.print("Enter Freelancer ID: ");
        int freelancerId = sc.nextInt();
        sc.nextLine();

        String sql =
            "SELECT p.project_id, p.project_status " +
            "FROM projects p " +
            "JOIN applications a ON p.application_id = a.application_id " +
            "WHERE a.freelancer_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, freelancerId);

            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;

                while (rs.next()) {
                    found = true;
                    String status = rs.getString("project_status");

                    System.out.println("\n--- Project Notification ---");
                    System.out.println("Project ID: " +
                                       rs.getInt("project_id"));

                    if ("COMPLETED".equalsIgnoreCase(status)) {
                        System.out.println("Your project is completed!");
                    } else if ("IN_PROGRESS".equalsIgnoreCase(status)) {
                        System.out.println("Your project is in progress.");
                    } else {
                        System.out.println("Your project has not started yet.");
                    }

                    System.out.println("Current Status: " + status);
                }

                if (!found) {
                    System.out.println("No project notifications found.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== NOTIFICATIONS & ALERTS ===");
            System.out.println("1. Application Notifications");
            System.out.println("2. Project Notifications");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    viewApplicationNotifications();
                    break;
                case 2:
                    viewProjectNotifications();
                    break;
                case 3:
                    System.out.println("Exiting Notifications.");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
