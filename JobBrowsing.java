
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class JobBrowsing {
    private final Scanner sc = new Scanner(System.in);
    public void displayMenu() {
        int choice;

        do {
            System.out.println("\n===== JOB BROWSING MENU =====");
            System.out.println("1. View All Open Jobs");
            System.out.println("2. Search Jobs by Keyword");
            System.out.println("3. Filter Jobs by Budget");
            System.out.println("4. View Job Details");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            choice = readInt();

            switch (choice) {
                case 1:
                    viewOpenJobs();
                    break;
                case 2:
                    searchJobs();
                    break;
                case 3:
                    filterByBudget();
                    break;
                case 4:
                    viewJobDetails();
                    break;
                case 0:
                    System.out.println("Returning to previous menu...");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 0);
    }
    public void viewOpenJobs() {
        String sql = "SELECT * FROM jobs " +
                "WHERE status = 'OPEN' ORDER BY job_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            displayResults(rs);

        } catch (SQLException e) {
            System.out.println("Error loading jobs: " + e.getMessage());
        }
    }
    public void searchJobs() {
        System.out.print("Enter job title or keyword: ");
        String keyword = sc.nextLine().trim();

        if (keyword.isEmpty()) {
            System.out.println("Keyword cannot be empty.");
            return;
        }

        String sql = "SELECT * FROM jobs " +
                "WHERE status = 'OPEN' " +
                "AND (job_title LIKE ? OR description LIKE ?) " +
                "ORDER BY job_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String search = "%" + keyword + "%";
            ps.setString(1, search);
            ps.setString(2, search);

            try (ResultSet rs = ps.executeQuery()) {
                displayResults(rs);
            }

        } catch (SQLException e) {
            System.out.println("Search error: " + e.getMessage());
        }
    }
    public void filterByBudget() {
        double minimum;
        double maximum;

        System.out.print("Enter minimum budget: ");

        try {
            minimum = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid minimum budget.");
            return;
        }

        System.out.print("Enter maximum budget: ");

        try {
            maximum = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid maximum budget.");
            return;
        }

        if (minimum < 0 || maximum < minimum) {
            System.out.println("Invalid budget range.");
            return;
        }

        String sql = "SELECT * FROM jobs " +
                "WHERE status = 'OPEN' AND budget BETWEEN ? AND ? " +
                "ORDER BY budget ASC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, minimum);
            ps.setDouble(2, maximum);

            try (ResultSet rs = ps.executeQuery()) {
                displayResults(rs);
            }

        } catch (SQLException e) {
            System.out.println("Budget filter error: " + e.getMessage());
        }
    }
    public void viewJobDetails() {
        System.out.print("Enter Job ID: ");
        int jobId = readInt();

        if (jobId <= 0) {
            System.out.println("Invalid Job ID.");
            return;
        }

        String sql = "SELECT * FROM jobs WHERE job_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jobId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    printJob(rs);
                } else {
                    System.out.println("Job not found.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error loading job: " + e.getMessage());
        }
    }
    private void displayResults(ResultSet rs) throws SQLException {
        boolean found = false;

        System.out.println("\n===== JOB RESULTS =====");

        while (rs.next()) {
            found = true;
            printJob(rs);
        }

        if (!found) {
            System.out.println("No matching jobs found.");
        }
    }
    private void printJob(ResultSet rs) throws SQLException {
        System.out.println("\n------------------------------");
        System.out.println("Job ID      : " + rs.getInt("job_id"));
        System.out.println("Client ID   : " + rs.getInt("client_id"));
        System.out.println("Title       : " + rs.getString("job_title"));
        System.out.println("Description : " + rs.getString("description"));
        System.out.println("Budget      : " + rs.getDouble("budget"));
        System.out.println("Deadline    : " + rs.getDate("deadline"));
        System.out.println("Status      : " + rs.getString("status"));
        System.out.println("------------------------------");
    }
    private int readInt() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
