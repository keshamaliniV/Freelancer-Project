
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class JobPosting {
    private final Scanner sc = new Scanner(System.in);
    public void displayMenu(int clientId) {
        int choice;
        do {
            System.out.println("\n================================");
            System.out.println("     JOB POSTING MANAGEMENT");
            System.out.println("================================");
            System.out.println("1. Post New Job");
            System.out.println("2. View My Jobs");
            System.out.println("3. Update Job");
            System.out.println("4. Delete Job");
            System.out.println("5. Close Job");
            System.out.println("6. View Job by ID");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            choice = readInt();

            switch (choice) {
                case 1:
                    postJob(clientId);
                    break;
                case 2:
                    viewMyJobs(clientId);
                    break;
                case 3:
                    updateJob(clientId);
                    break;
                case 4:
                    deleteJob(clientId);
                    break;
                case 5:
                    closeJob(clientId);
                    break;
                case 6:
                    viewJobById(clientId);
                    break;
                case 0:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 0);
    }
    public void postJob(int clientId) {
        System.out.println("\n===== POST NEW JOB =====");

        System.out.print("Job title: ");
        String title = sc.nextLine().trim();

        System.out.print("Job description: ");
        String description = sc.nextLine().trim();

        if (title.isEmpty() || description.isEmpty()) {
            System.out.println("Title and description are required.");
            return;
        }

        System.out.print("Budget: ");
        double budget;

        try {
            budget = Double.parseDouble(sc.nextLine().trim());
            if (budget <= 0) {
                System.out.println("Budget must be greater than zero.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid numeric budget.");
            return;
        }

        System.out.print("Deadline (YYYY-MM-DD): ");
        String deadlineInput = sc.nextLine().trim();

        try {
            Date deadline = Date.valueOf(deadlineInput);

            if (deadline.before(new Date(System.currentTimeMillis()))) {
                System.out.println("Deadline cannot be in the past.");
                return;
            }

            String sql = "INSERT INTO jobs " +
                    "(client_id, job_title, description, budget, deadline, status) " +
                    "VALUES (?, ?, ?, ?, ?, 'OPEN')";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(
                         sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, clientId);
                ps.setString(2, title);
                ps.setString(3, description);
                ps.setDouble(4, budget);
                ps.setDate(5, deadline);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            System.out.println("\nJob posted successfully!");
                            System.out.println("Generated Job ID: " +
                                    keys.getInt(1));
                        }
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date. Use YYYY-MM-DD.");
        } catch (SQLException e) {
            System.out.println("Unable to post job: " + e.getMessage());
        }
    }
    public void viewMyJobs(int clientId) {
        String sql = "SELECT * FROM jobs WHERE client_id = ? " +
                "ORDER BY job_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, clientId);

            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;

                System.out.println("\n===== MY JOBS =====");

                while (rs.next()) {
                    found = true;
                    printJob(rs);
                }

                if (!found) {
                    System.out.println("You have not posted any jobs.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Unable to retrieve jobs: " + e.getMessage());
        }
    }
    public void updateJob(int clientId) {
        System.out.print("Enter Job ID to update: ");
        int jobId = readInt();

        if (!isOwnedByClient(jobId, clientId)) {
            System.out.println("Job not found or you do not own this job.");
            return;
        }

        System.out.println("1. Update title");
        System.out.println("2. Update description");
        System.out.println("3. Update budget");
        System.out.println("4. Update deadline");
        System.out.print("Choose field: ");

        int choice = readInt();
        String column;
        String value;

        switch (choice) {
            case 1:
                column = "job_title";
                System.out.print("New title: ");
                value = sc.nextLine().trim();
                if (value.isEmpty()) {
                    System.out.println("Title cannot be empty.");
                    return;
                }
                break;

            case 2:
                column = "description";
                System.out.print("New description: ");
                value = sc.nextLine().trim();
                if (value.isEmpty()) {
                    System.out.println("Description cannot be empty.");
                    return;
                }
                break;

            case 3:
                column = "budget";
                System.out.print("New budget: ");
                value = sc.nextLine().trim();

                try {
                    if (Double.parseDouble(value) <= 0) {
                        System.out.println("Budget must be positive.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid budget.");
                    return;
                }
                break;

            case 4:
                column = "deadline";
                System.out.print("New deadline (YYYY-MM-DD): ");
                value = sc.nextLine().trim();

                try {
                    Date date = Date.valueOf(value);
                    if (date.before(new Date(System.currentTimeMillis()))) {
                        System.out.println("Deadline cannot be in the past.");
                        return;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid date format.");
                    return;
                }
                break;

            default:
                System.out.println("Invalid choice.");
                return;
        }
        String sql = "UPDATE jobs SET " + column +
                " = ? WHERE job_id = ? AND client_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (column.equals("budget")) {
                ps.setDouble(1, Double.parseDouble(value));
            } else if (column.equals("deadline")) {
                ps.setDate(1, Date.valueOf(value));
            } else {
                ps.setString(1, value);
            }

            ps.setInt(2, jobId);
            ps.setInt(3, clientId);

            if (ps.executeUpdate() > 0) {
                System.out.println("Job updated successfully!");
            } else {
                System.out.println("No changes made.");
            }
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }
    public void deleteJob(int clientId) {
        System.out.print("Enter Job ID to delete: ");
        int jobId = readInt();

        if (!isOwnedByClient(jobId, clientId)) {
            System.out.println("Job not found or you do not own this job.");
            return;
        }

        System.out.print("Confirm deletion? Type YES: ");
        String confirmation = sc.nextLine().trim();

        if (!confirmation.equalsIgnoreCase("YES")) {
            System.out.println("Deletion cancelled.");
            return;
        }

        String sql = "DELETE FROM jobs WHERE job_id = ? AND client_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, clientId);

            if (ps.executeUpdate() > 0) {
                System.out.println("Job deleted successfully.");
            } else {
                System.out.println("Job could not be deleted.");
            }
        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
            System.out.println(
                    "If applications reference this job, handle those records first.");
        }
    }
    public void closeJob(int clientId) {
        System.out.print("Enter Job ID to close: ");
        int jobId = readInt();

        String sql = "UPDATE jobs SET status = 'CLOSED' " +
                "WHERE job_id = ? AND client_id = ? AND status = 'OPEN'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, clientId);

            if (ps.executeUpdate() > 0) {
                System.out.println("Job closed successfully.");
            } else {
                System.out.println(
                        "Open job not found, or it does not belong to you.");
            }
        } catch (SQLException e) {
            System.out.println("Unable to close job: " + e.getMessage());
        }
    }
    public void viewJobById(int clientId) {
        System.out.print("Enter Job ID: ");
        int jobId = readInt();

        String sql = "SELECT * FROM jobs WHERE job_id = ? AND client_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, clientId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    printJob(rs);
                } else {
                    System.out.println("Job not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Unable to view job: " + e.getMessage());
        }
    }
    private boolean isOwnedByClient(int jobId, int clientId) {
        String sql = "SELECT job_id FROM jobs " +
                "WHERE job_id = ? AND client_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, clientId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Ownership check failed: " + e.getMessage());
            return false;
        }
    }
    private void printJob(ResultSet rs) throws SQLException {
        System.out.println("\n------------------------------");
        System.out.println("Job ID: " + rs.getInt("job_id"));
        System.out.println("Title: " + rs.getString("job_title"));
        System.out.println("Description: " + rs.getString("description"));
        System.out.println("Budget: " + rs.getDouble("budget"));
        System.out.println("Deadline: " + rs.getDate("deadline"));
        System.out.println("Status: " + rs.getString("status"));
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
