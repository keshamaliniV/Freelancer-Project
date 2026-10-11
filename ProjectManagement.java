

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ProjectManagement {

    static Scanner sc = new Scanner(System.in);

    public static void viewProjects() {
        String sql = "SELECT * FROM projects";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println("\n------------------------");
                System.out.println("Project ID: " +
                                   rs.getInt("project_id"));
                System.out.println("Application ID: " +
                                   rs.getInt("application_id"));
                System.out.println("Start Date: " +
                                   rs.getDate("start_date"));
                System.out.println("End Date: " +
                                   rs.getDate("end_date"));
                System.out.println("Status: " +
                                   rs.getString("project_status"));
            }

            if (!found) {
                System.out.println("No projects found.");
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void createProject() {
        System.out.print("Enter Application ID: ");
        int applicationId = sc.nextInt();
        sc.nextLine();

        String checkSql =
            "SELECT application_id FROM applications " +
            "WHERE application_id = ? AND status = 'HIRED'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement check =
                 con.prepareStatement(checkSql)) {

            check.setInt(1, applicationId);

            try (ResultSet rs = check.executeQuery()) {
                if (!rs.next()) {
                    System.out.println(
                        "Invalid application or application is not HIRED."
                    );
                    return;
                }
            }

            String duplicateSql =
                "SELECT project_id FROM projects WHERE application_id = ?";

            try (PreparedStatement duplicate =
                     con.prepareStatement(duplicateSql)) {

                duplicate.setInt(1, applicationId);

                try (ResultSet rs = duplicate.executeQuery()) {
                    if (rs.next()) {
                        System.out.println(
                            "Project already exists for this application."
                        );
                        return;
                    }
                }
            }

            String insertSql =
                "INSERT INTO projects " +
                "(application_id, start_date, project_status) " +
                "VALUES (?, CURRENT_DATE, 'NOT_STARTED')";

            try (PreparedStatement ps =
                     con.prepareStatement(insertSql)) {

                ps.setInt(1, applicationId);

                if (ps.executeUpdate() > 0) {
                    System.out.println("Project created successfully!");
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void updateProjectStatus() {
        System.out.print("Enter Project ID: ");
        int projectId = sc.nextInt();

        System.out.println("1. NOT_STARTED");
        System.out.println("2. IN_PROGRESS");
        System.out.println("3. COMPLETED");
        System.out.print("Choose new status: ");

        int choice = sc.nextInt();
        sc.nextLine();

        String status;

        switch (choice) {
            case 1:
                status = "NOT_STARTED";
                break;
            case 2:
                status = "IN_PROGRESS";
                break;
            case 3:
                status = "COMPLETED";
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }

        String sql;

        if (status.equals("COMPLETED")) {
            sql = "UPDATE projects SET project_status = ?, " +
                  "end_date = CURRENT_DATE WHERE project_id = ?";
        } else {
            sql = "UPDATE projects SET project_status = ?, " +
                  "end_date = NULL WHERE project_id = ?";
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, projectId);

            if (ps.executeUpdate() > 0) {
                System.out.println("Project status updated to " + status);
            } else {
                System.out.println("Project ID not found.");
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== PROJECT MANAGEMENT ===");
            System.out.println("1. View Projects");
            System.out.println("2. Create Project");
            System.out.println("3. Update Project Status");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    viewProjects();
                    break;
                case 2:
                    createProject();
                    break;
                case 3:
                    updateProjectStatus();
                    break;
                case 4:
                    System.out.println("Exiting Project Management.");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
