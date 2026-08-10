public class Main {
    public static void main(String[] args) {
        UserAuthentication authentication =
                new UserAuthentication();
        ClientFreelancerProfile clientProfile =
                new ClientFreelancerProfile(authentication);
        ClientFreelancerProfile freelancerProfile =
                new ClientFreelancerProfile(authentication);
        authentication.displayMainMenu();
        clientProfile.clientModule();
        freelancerProfile.freelancerModule();
    }
}