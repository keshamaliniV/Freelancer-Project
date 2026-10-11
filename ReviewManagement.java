
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ReviewManagement {

    static Scanner sc = new Scanner(System.in);

    public static void addReview() {
        System.out.print("Enter Project ID: ");
        int projectId = sc.nextInt();

        System.out.print("Enter Rating (1-5): ");
        int rating = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Feedback: ");
        String feedback = sc.nextLine().trim();

        if (projectId <= 0 || rating < 1 || rating > 5
                || feedback.isEmpty()) {
            System.out.println("Invalid input!");
            return;
        }

        String checkSql =
            "SELECT project_id FROM projects " +
            "WHERE project_id = ? AND project_status = 'COMPLETED'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement check = con.prepareStatement(checkSql)) {

            check.setInt(1, projectId);

            try (ResultSet rs = check.executeQuery()) {
                if (!rs.next()) {
                    System.out.println(
                        "Project not found or not completed yet."
                    );
                    return;
                }
            }

            String duplicateSql =
                "SELECT review_id FROM reviews WHERE project_id = ?";

            try (PreparedStatement duplicate =
                     con.prepareStatement(duplicateSql)) {

                duplicate.setInt(1, projectId);

                try (ResultSet rs = duplicate.executeQuery()) {
                    if (rs.next()) {
                        System.out.println(
                            "A review already exists for this project."
                        );
                        return;
                    }
                }
            }

            String sql =
                "INSERT INTO reviews " +
                "(review_date, rating, project_id, feedback) " +
                "VALUES (CURRENT_DATE, ?, ?, ?)";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, rating);
                ps.setInt(2, projectId);
                ps.setString(3, feedback);

                if (ps.executeUpdate() > 0) {
                    System.out.println("Review submitted successfully!");
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void viewReviews() {
        String sql =
            "SELECT review_id, review_date, rating, project_id, feedback " +
            "FROM reviews ORDER BY review_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println("\n-------------------------");
                System.out.println("Review ID: " +
                                   rs.getInt("review_id"));
                System.out.println("Project ID: " +
                                   rs.getInt("project_id"));
                System.out.println("Rating: " +
                                   rs.getInt("rating") + "/5");
                System.out.println("Feedback: " +
                                   rs.getString("feedback"));
                System.out.println("Review Date: " +
                                   rs.getDate("review_date"));
            }

            if (!found) {
                System.out.println("No reviews available.");
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== REVIEWS & RATINGS ===");
            System.out.println("1. Add Review");
            System.out.println("2. View Reviews");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addReview();
                    break;
                case 2:
                    viewReviews();
                    break;
                case 3:
                    System.out.println("Exiting Reviews & Ratings.");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}

