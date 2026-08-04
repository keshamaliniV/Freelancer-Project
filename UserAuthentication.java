import java.util.ArrayList;
import java.util.Scanner;
public class UserAuthentication{
    Scanner sc=new Scanner(System.in);
    class User{
        protected String name;
        protected String username;
        protected String password;
        protected String email;
        User(String name,String username,String password,String email){
            this.name=name;
            this.username=username;
            this.password=password;
            this.email=email;
        }
        void displayProfile(){
            System.out.println("Name : "+name);
            System.out.println("Username : "+username);
            System.out.println("Email : "+email);
        }
    }
    class Client extends User{
    Client(String name,
           String username,
           String password,
           String email){
        super(name,username,password,email);
    }
}
   class Freelancer extends User{
    String skills;
    Freelancer(String name,
               String username,
               String password,
               String email,
               String skills){
        super(name,username,password,email);
        this.skills=skills;
    }
    @Override
    void displayProfile(){
        super.displayProfile();
        System.out.println("Skills : "+skills);
    }
}
    ArrayList<Client> clients=new ArrayList<>();
    ArrayList<Freelancer> freelancers=new ArrayList<>();
    void displayMainMenu(){
        while(true){
            System.out.println();
            System.out.println("=======================================");
            System.out.println("     FREELANCER HIRING PLATFORM");
            System.out.println("=======================================");
            System.out.println("1. Register as Client");
            System.out.println("2. Register as Freelancer");
            System.out.println("3. Login");
            System.out.println("4. Exit");
            System.out.print("Enter Choice : ");
            int choice=sc.nextInt();
            sc.nextLine();
            switch(choice){
                case 1:
                    registerClient();
                    break;
                case 2:
                    registerFreelancer();
                    break;
                case 3:
                    login();
                    break;
                case 4:
                    System.out.println("Thank You...");
                    System.exit(0);
                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
    void registerClient(){
        System.out.println("\n========== CLIENT REGISTRATION ==========");
        System.out.print("Enter Name : ");
        String name=sc.nextLine();
        System.out.print("Enter Username : ");
        String username=sc.nextLine();
        for(Client c : clients) {
            if(c.username.equalsIgnoreCase(username)){
                System.out.println("Username already exists");
                return;
            }
        }
        System.out.print("Enter Password : ");
        String password=sc.nextLine();
        while(password.length() < 8) {
            System.out.println("Password must contain minimum 8 characters");
            System.out.print("Enter Password Again : ");
            password=sc.nextLine();
        }
        System.out.print("Enter Email : ");
        String email=sc.nextLine();
        Client client=new Client(
                name,
                username,
                password,
                email
        );
        clients.add(client);
        System.out.println();
        System.out.println("Client Registered Successfully");
    }
    void registerFreelancer(){
        System.out.println("\n========== FREELANCER REGISTRATION ==========");
        System.out.print("Enter Name : ");
        String name=sc.nextLine();
        System.out.print("Enter Username : ");
        String username=sc.nextLine();
        for(Freelancer f:freelancers) {
            if(f.username.equalsIgnoreCase(username)) {
                System.out.println("Username already exists");
                return;
            }
        }
        System.out.print("Enter Password : ");
        String password=sc.nextLine();
        while(password.length() < 8){
            System.out.println("Password must contain minimum 8 characters");
            System.out.print("Enter Password Again : ");
            password=sc.nextLine();
        }
        System.out.print("Enter Email : ");
        String email=sc.nextLine();
        System.out.print("Enter Skills : ");
        String skills=sc.nextLine();
        Freelancer freelancer=new Freelancer(
                name,
                username,
                password,
                email,
                skills
        );
        freelancers.add(freelancer);
        System.out.println();
        System.out.println("Freelancer Registered Successfully");
        System.out.println("Total Freelancers : " + freelancers.size());
    }
    void login(){
        while(true){
            System.out.println("\n========== LOGIN ==========");
            System.out.println("1. Client Login");
            System.out.println("2. Freelancer Login");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter Choice : ");
            int choice=sc.nextInt();
            sc.nextLine();
            switch(choice){
                case 1:
                    clientLogin();
                    break;
                case 2:
                    freelancerLogin();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
    void clientLogin(){
        System.out.println("\n========== CLIENT LOGIN ==========");
        System.out.print("Enter Username : ");
        String username=sc.nextLine();
        System.out.print("Enter Password : ");
        String password=sc.nextLine();
        for(Client c:clients){
            if(c.username.equals(username) &&
               c.password.equals(password)){
                System.out.println();
                System.out.println("Login Successful");
                System.out.println("Welcome " + c.name);
                clientDashboard(c);
                return;
            }
        }
        System.out.println("Invalid Username or Password");
    }
    void freelancerLogin() {
        System.out.println("\n========== FREELANCER LOGIN ==========");
        System.out.print("Enter Username : ");
        String username=sc.nextLine();
        System.out.print("Enter Password : ");
        String password=sc.nextLine();
        for(Freelancer f:freelancers){
            if(f.username.equals(username) &&
               f.password.equals(password)){
                System.out.println();
                System.out.println("Login Successful");
                System.out.println("Welcome " + f.name);
                freelancerDashboard(f);
                return;
            }
        }
        System.out.println("Invalid Username or Password");
    }
    void clientDashboard(Client client){
        while(true){
            System.out.println();
            System.out.println("========== CLIENT DASHBOARD ==========");
            System.out.println("Welcome " + client.name);
            System.out.println("1. View Profile");
            System.out.println("2. Logout");
            System.out.print("Enter Choice : ");
            int choice=sc.nextInt();
            sc.nextLine();
            switch(choice){
                case 1:
                    System.out.println();
                    System.out.println("===== CLIENT PROFILE =====");
                    client.displayProfile();
                    break;
                case 2:
                    System.out.println("Logged Out Successfully");
                    return;
                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
    void freelancerDashboard(Freelancer freelancer){
        while(true){
            System.out.println();
            System.out.println("========== FREELANCER DASHBOARD ==========");
            System.out.println("Welcome " + freelancer.name);
            System.out.println("1. View Profile");
            System.out.println("2. Logout");
            System.out.print("Enter Choice : ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch(choice){
                case 1:
                    System.out.println();
                    System.out.println("===== FREELANCER PROFILE =====");
                    freelancer.displayProfile();
                    break;
                case 2:
                    System.out.println("Logged Out Successfully");
                    return;
                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
    public static void main(String[] args){
        UserAuthentication obj = new UserAuthentication();
        obj.displayMainMenu();
    }
}