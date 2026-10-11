import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class JobApplication {

    static Scanner sc = new Scanner(System.in);
    public static boolean isJobValid(Connection con, int jobId)
            throws SQLException {

        String sql = "SELECT job_id FROM jobs WHERE job_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, jobId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    public static boolean isFreelancerValid(
            Connection con, int freelancerId) throws SQLException {

        String sql = "SELECT freelancer_id FROM freelancers "
                   + "WHERE freelancer_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, freelancerId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    public static boolean alreadyApplied(
            Connection con, int jobId, int freelancerId)
            throws SQLException {

        String sql = "SELECT application_id FROM applications "
                   + "WHERE job_id = ? AND freelancer_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, jobId);
            ps.setInt(2, freelancerId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    public static void applyForJob() {

        try (Connection con = DBConnection.getConnection()) {

            System.out.print("Enter Job ID: ");
            int jobId = sc.nextInt();

            System.out.print("Enter Freelancer ID: ");
            int freelancerId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter your proposal: ");
            String proposal = sc.nextLine().trim();

            System.out.print("Enter expected budget: ");
            double budget = sc.nextDouble();
            sc.nextLine();

            if (jobId <= 0 || freelancerId <= 0
                    || proposal.isEmpty() || budget <= 0) {
                System.out.println("Invalid input!");
                return;
            }

            if (!isJobValid(con, jobId)) {
                System.out.println("Job does not exist!");
                return;
            }

            if (!isFreelancerValid(con, freelancerId)) {
                System.out.println("Freelancer does not exist!");
                return;
            }

            if (alreadyApplied(con, jobId, freelancerId)) {
                System.out.println("You have already applied for this job!");
                return;
            }

            String sql = "INSERT INTO applications "
                    + "(job_id, freelancer_id, proposal, "
                    + "expected_budget, status, applied_date) "
                    + "VALUES (?, ?, ?, ?, 'PENDING', CURRENT_DATE)";

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, jobId);
                ps.setInt(2, freelancerId);
                ps.setString(3, proposal);
                ps.setDouble(4, budget);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println("Application submitted successfully!");
                    System.out.println("Status: PENDING");
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
    public static void viewMyApplications() {

        try (Connection con = DBConnection.getConnection()) {

            System.out.print("Enter Freelancer ID: ");
            int freelancerId = sc.nextInt();
            sc.nextLine();

            String sql = "SELECT application_id, job_id, proposal, "
                    + "expected_budget, status, applied_date "
                    + "FROM applications WHERE freelancer_id = ?";

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, freelancerId);

                try (ResultSet rs = ps.executeQuery()) {

                    boolean found = false;

                    while (rs.next()) {
                        found = true;

                        System.out.println("\n----------------------");
                        System.out.println("Application ID: "
                                + rs.getInt("application_id"));
                        System.out.println("Job ID: "
                                + rs.getInt("job_id"));
                        System.out.println("Proposal: "
                                + rs.getString("proposal"));
                        System.out.println("Expected Budget: "
                                + rs.getDouble("expected_budget"));
                        System.out.println("Status: "
                                + rs.getString("status"));
                        System.out.println("Applied Date: "
                                + rs.getDate("applied_date"));
                    }

                    if (!found) {
                        System.out.println("No applications found.");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
    public static void main(String[] args) {

        while (true) {
            System.out.println("\n=== JOB APPLICATION MODULE ===");
            System.out.println("1. Apply for Job");
            System.out.println("2. View My Applications");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    applyForJob();
                    break;

                case 2:
                    viewMyApplications();
                    break;

                case 3:
                    System.out.println("Exiting Job Application Module.");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}

