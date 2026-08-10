import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
public class UserAuthentication{
    private int loggedInUserId=-1;
    Scanner sc=new Scanner(System.in);
    public int getLoggedInUserId(){
    return loggedInUserId;
    }
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
    String companyName = "";
    String location = "";
    Client(String name,
           String username,
           String password,
           String email){
        super(name,username,password,email);
    }
}
   class Freelancer extends User{
    String skills="";
    String location = "";
    String experience = "";
    String portfolio = "";
    Freelancer(String name,
               String username,
               String password,
               String email,
               String skills,
               String location,
               String experience,
               String portfolio
            ){
        super(name,username,password,email);
        this.skills=skills;
        this.location = location;
        this.experience = experience;
        this.portfolio = portfolio;
        }
    @Override
    void displayProfile(){
        super.displayProfile();
        System.out.println("Skills : "+skills);
        System.out.println("Location : " + location);
        System.out.println("Experience : " + experience);
        System.out.println("Portfolio / Projects : " + portfolio);
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
                if(loggedInUserId>0){
                    return;
                }
                    break;
                case 2:
                registerFreelancer();
                if(loggedInUserId>0){
                    return;
                }
                    break;
                case 3:
                    login();
                    return;
                case 4:
                    System.out.println("Thank You...");
                    System.exit(0);
                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
    void registerClient(){
        Connection con=DBConnection.getConnection();
        System.out.println("\n========== CLIENT REGISTRATION ==========");
        System.out.print("Enter Name : ");
        String name=sc.nextLine();
        System.out.print("Enter Username : ");
        String username=sc.nextLine();
        try{
           String sql="SELECT username FROM users WHERE username = ?";
           PreparedStatement pst=con.prepareStatement(sql);
           pst.setString(1, username);
           ResultSet rs=pst.executeQuery();
           if(rs.next()){
            System.out.println("Username already exists in database");
            return;
        }
        rs.close();
        pst.close();
        }
        catch(SQLException e){
            System.out.println("Database Error");
            e.printStackTrace();
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
        try {
        String sql = "INSERT INTO users (name, username, password, email, role) VALUES (?, ?, ?, ?, 'CLIENT')";
        PreparedStatement pst =
        con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, name);
        pst.setString(2, username);
        pst.setString(3, password);
        pst.setString(4, email);
        pst.executeUpdate();
        ResultSet keys=pst.getGeneratedKeys();
        if(keys.next()){
        loggedInUserId=keys.getInt(1);
        }
        keys.close();
        pst.close();
        clients.add(client);
        System.out.println("Client saved to database successfully!");
        System.out.println();
        System.out.println("Client Registered Successfully");
        }
        catch(SQLException e){
            if(e.getErrorCode()==1062){
               System.out.println("Username or Email already exists in database"); 
            }
            else{
                System.out.println("Database Insert Error");
                e.printStackTrace();
            }
            return;
        }
        System.out.println();
    }
    void registerFreelancer(){
        Connection con=DBConnection.getConnection();
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
        System.out.print("Enter Location : ");
        String location = sc.nextLine();
        System.out.print("Enter Experience : ");
        String experience = sc.nextLine();
        System.out.print("Enter Portfolio / Projects : ");
        String portfolio = sc.nextLine();
        Freelancer freelancer=new Freelancer(
                name,
                username,
                password,
                email,
                skills,
                location,
                experience,
                portfolio
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
    String username = sc.nextLine();
    System.out.print("Enter Password : ");
    String password = sc.nextLine();
    String sql =
            "SELECT user_id, name FROM users " +
            "WHERE username = ? AND password = ? AND role = 'CLIENT'";
    try(Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            loggedInUserId = rs.getInt("user_id");
            String name = rs.getString("name");
            System.out.println();
            System.out.println("Login Successful");
            System.out.println("Welcome " + name);
            return;
        }
        System.out.println("Invalid Username or Password");
    }
    catch(Exception e){
        System.out.println("Database Login Error");
        e.printStackTrace();
    }
    }
    void freelancerLogin(){
    System.out.println("\n========== FREELANCER LOGIN ==========");
    System.out.print("Enter Username : ");
    String username=sc.nextLine();
    System.out.print("Enter Password : ");
    String password=sc.nextLine();
    String sql =
            "SELECT user_id, name FROM users " +
            "WHERE username = ? AND password = ? AND role = 'FREELANCER'";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)){
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs=ps.executeQuery();
        if(rs.next()){
            loggedInUserId = rs.getInt("user_id");
            String name = rs.getString("name");
            System.out.println();
            System.out.println("Login Successful");
            System.out.println("Welcome " + name);
            ClientFreelancerProfile profile=new ClientFreelancerProfile(this);
            profile.freelancerModule();
            return;
        }
        System.out.println("Invalid Username or Password");
    }
    catch(Exception e){
        System.out.println("Database Login Error");
        e.printStackTrace();
    }
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
}    

