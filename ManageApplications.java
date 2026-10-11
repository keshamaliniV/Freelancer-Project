
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ManageApplications {

    static Scanner sc = new Scanner(System.in);

    public static void viewApplications() {
        System.out.print("Enter Client ID: ");
        int clientId = sc.nextInt();
        sc.nextLine();

        String sql = "SELECT a.application_id, a.job_id, a.freelancer_id, " +
                     "a.proposal, a.expected_budget, a.status, a.applied_date, " +
                     "j.job_title FROM applications a " +
                     "JOIN jobs j ON a.job_id = j.job_id " +
                     "WHERE j.client_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, clientId);

            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;

                while (rs.next()) {
                    found = true;
                    System.out.println("\n------------------------------");
                    System.out.println("Application ID: " +
                                       rs.getInt("application_id"));
                    System.out.println("Job ID: " + rs.getInt("job_id"));
                    System.out.println("Job Title: " +
                                       rs.getString("job_title"));
                    System.out.println("Freelancer ID: " +
                                       rs.getInt("freelancer_id"));
                    System.out.println("Proposal: " +
                                       rs.getString("proposal"));
                    System.out.println("Expected Budget: " +
                                       rs.getDouble("expected_budget"));
                    System.out.println("Status: " +
                                       rs.getString("status"));
                    System.out.println("Applied Date: " +
                                       rs.getDate("applied_date"));
                }

                if (!found) {
                    System.out.println("No applications found for your jobs.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void updateApplicationStatus() {
        System.out.print("Enter Client ID: ");
        int clientId = sc.nextInt();

        System.out.print("Enter Application ID: ");
        int applicationId = sc.nextInt();

        System.out.println("1. Accept Application");
        System.out.println("2. Reject Application");
        System.out.print("Choose option: ");
        int choice = sc.nextInt();
        sc.nextLine();

        String newStatus;

        if (choice == 1) {
            newStatus = "HIRED";
        } else if (choice == 2) {
            newStatus = "REJECTED";
        } else {
            System.out.println("Invalid choice!");
            return;
        }

        String sql = "UPDATE applications a " +
                     "JOIN jobs j ON a.job_id = j.job_id " +
                     "SET a.status = ? " +
                     "WHERE a.application_id = ? " +
                     "AND j.client_id = ? AND a.status = 'PENDING'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, applicationId);
            ps.setInt(3, clientId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Application status updated to " +
                                   newStatus + " successfully!");
            } else {
                System.out.println(
                    "Application not found, not yours, or already processed."
                );
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== MANAGE APPLICATIONS ===");
            System.out.println("1. View Applications");
            System.out.println("2. Accept / Reject Application");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    viewApplications();
                    break;
                case 2:
                    updateApplicationStatus();
                    break;
                case 3:
                    System.out.println("Exiting Manage Applications.");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
