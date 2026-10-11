
public class Main {
    public static void main(String[] args) {
        UserAuthentication authentication =
                new UserAuthentication();
        ClientFreelancerProfile clientProfile =
                new ClientFreelancerProfile(authentication);
        ClientFreelancerProfile freelancerProfile =
                new ClientFreelancerProfile(authentication);
        JobPosting jobPosting = new JobPosting();
        JobBrowsing jobBrowsing = new JobBrowsing();
        authentication.displayMainMenu();
        clientProfile.clientModule();
        freelancerProfile.freelancerModule();
        jobPosting.displayMenu(1);
        jobBrowsing.displayMenu();
    }
}
