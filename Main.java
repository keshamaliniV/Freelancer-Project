
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        UserAuthentication authentication =
                new UserAuthentication();

        ClientFreelancerProfile profile =
                new ClientFreelancerProfile(authentication);

        JobPosting jobPosting = new JobPosting();
        JobBrowsing jobBrowsing = new JobBrowsing();

        while (true) {
            System.out.println("\n================================");
            System.out.println("  FREELANCER HIRING PLATFORM");
            System.out.println("================================");
            System.out.println("1. Register / Login");
            System.out.println("2. Client Profile");
            System.out.println("3. Freelancer Profile");
            System.out.println("4. Job Posting");
            System.out.println("5. Browse Jobs");
            System.out.println("6. Apply for Job");
            System.out.println("7. Manage Applications");
            System.out.println("8. Project Management");
            System.out.println("9. Reviews & Ratings");
            System.out.println("10. Admin Management");
            System.out.println("11. Notifications & Alerts");
            System.out.println("12. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    authentication.displayMainMenu();
                    break;

                case 2:
                    profile.clientModule();
                    break;

                case 3:
                    profile.freelancerModule();
                    break;

                case 4:
                    jobPosting.displayMenu(1);
                    break;

                case 5:
                    jobBrowsing.displayMenu();
                    break;

                case 6:
                    JobApplication.main(args);
                    break;

                case 7:
                    ManageApplications.main(args);
                    break;

                case 8:
                    ProjectManagement.main(args);
                    break;

                case 9:
                    ReviewManagement.main(args);
                    break;

                case 10:
                    AdminManagement.main(args);
                    break;

                case 11:
                    NotificationManagement.main(args);
                    break;

                case 12:
                    System.out.println(
                            "Thank you for using Freelancer Hiring Platform!"
                    );
                    return;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
