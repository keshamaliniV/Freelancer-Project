
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestJobBrowsing {

    public static void main(String[] args) {

        String sql = "SELECT * FROM jobs";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("================================");
            System.out.println("       AVAILABLE JOBS");
            System.out.println("================================");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("Job ID: "
                        + rs.getInt("job_id"));

                System.out.println("Job Details:");

                System.out.println("Title: "+ rs.getString("job_title"));

                System.out.println("Description: "
                        + rs.getString("description"));

                System.out.println("--------------------------------");
            }

            if (!found) {
                System.out.println("No jobs available.");
            }

        } catch (SQLException e) {
            System.out.println("Database error: "
                    + e.getMessage());
        }
    }
}
