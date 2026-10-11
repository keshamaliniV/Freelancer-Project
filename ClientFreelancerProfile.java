import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
public class ClientFreelancerProfile{
    private UserAuthentication userAuthentication;
    public ClientFreelancerProfile(UserAuthentication userAuthentication){
    this.userAuthentication = userAuthentication;
    }
    Scanner sc=new Scanner(System.in);
    public void clientModule(){
        while(true){
            System.out.println();
            System.out.println("=======================================");
            System.out.println("          CLIENT PROFILE               ");
            System.out.println("=======================================");
            System.out.println("1. View Profile");
            System.out.println("2. Edit Profile");
            System.out.println("3. Exit");
            System.out.print("Enter Choice : ");
            int choice=sc.nextInt();
            sc.nextLine();
            switch(choice){
                case 1:
                    viewClientProfile();
                    break;
                case 2:
                    editClientProfile();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
    private void viewClientProfile(){
        int userId = userAuthentication.getLoggedInUserId();
        String sql=
                "SELECT u.user_id, u.name, u.username, u.email, " +
                "c.company_name, c.location, c.contact_number " +
                "FROM users u " +
                "LEFT JOIN clients c ON u.user_id = c.user_id " +
                "WHERE u.user_id = ? AND u.role = 'CLIENT'";
        try(Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, userId);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                System.out.println();
                System.out.println("========== CLIENT PROFILE ==========");
                System.out.println("User ID        : " + rs.getInt("user_id"));
                System.out.println("Name           : " + rs.getString("name"));
                System.out.println("Username       : " + rs.getString("username"));
                System.out.println("Email          : " + rs.getString("email"));
                System.out.println("Company Name   : " + rs.getString("company_name"));
                System.out.println("Location       : " + rs.getString("location"));
                System.out.println("Contact Number : " + rs.getString("contact_number"));
            }
            else{
                System.out.println("Client not found.");
            }
        }
        catch(Exception e){
            System.out.println("Error while loading client profile.");
            e.printStackTrace();
        }
    }
    private void editClientProfile(){
        int userId=userAuthentication.getLoggedInUserId();
        System.out.println();
        System.out.println("========== EDIT CLIENT PROFILE ==========");
        System.out.print("Enter Company Name : ");
        String companyName=sc.nextLine();
        System.out.print("Enter Location : ");
        String location=sc.nextLine();
        System.out.print("Enter Contact Number : ");
        String contactNumber=sc.nextLine();
        String checkSql=
                "SELECT user_id FROM clients WHERE user_id = ?";
        String insertSql=
                "INSERT INTO clients " +
                "(user_id, company_name, location, contact_number) " +
                "VALUES (?, ?, ?, ?)";
        String updateSql=
                "UPDATE clients SET company_name = ?, " +
                "location = ?, contact_number = ? " +
                "WHERE user_id = ?";
        try(Connection con=DBConnection.getConnection();
             PreparedStatement checkPs=con.prepareStatement(checkSql)){
            checkPs.setInt(1, userId);
            ResultSet rs=checkPs.executeQuery();
            if(rs.next()){
                try(PreparedStatement ps =con.prepareStatement(updateSql)){
                    ps.setString(1, companyName);
                    ps.setString(2, location);
                    ps.setString(3, contactNumber);
                    ps.setInt(4, userId);
                    ps.executeUpdate();
                    System.out.println("Client Profile Updated Successfully!");
                }
            }
            else{
                try(PreparedStatement ps=con.prepareStatement(insertSql)){
                    ps.setInt(1, userId);
                    ps.setString(2, companyName);
                    ps.setString(3, location);
                    ps.setString(4, contactNumber);
                    ps.executeUpdate();
                    System.out.println("Client Profile Added Successfull!");
                }
            }
        }
        catch (Exception e){
            System.out.println("Error while saving client profile.");
            e.printStackTrace();
        }
    }
    public void freelancerModule(){
        while(true){
            System.out.println();
            System.out.println("=======================================");
            System.out.println("        FREELANCER PROFILE");
            System.out.println("=======================================");
            System.out.println("1. View Profile");
            System.out.println("2. Edit Profile");
            System.out.println("3. Exit");
            System.out.print("Enter Choice : ");
            int choice=sc.nextInt();
            sc.nextLine();
            switch (choice){
                case 1:
                    viewFreelancerProfile();
                    break;
                case 2:
                    editFreelancerProfile();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
    private void viewFreelancerProfile(){
        int userId = userAuthentication.getLoggedInUserId();
        String sql=
                "SELECT u.user_id, u.name, u.username, u.email, " +
                "f.skills, f.experience, f.portfolio " +
                "FROM users u " +
                "LEFT JOIN freelancers f ON u.user_id = f.user_id " +
                "WHERE u.user_id = ? AND u.role = 'FREELANCER'";
        try(Connection con=DBConnection.getConnection();
             PreparedStatement ps=con.prepareStatement(sql)){
            ps.setInt(1, userId);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                System.out.println();
                System.out.println("========== FREELANCER PROFILE ==========");
                System.out.println("User ID     : " + rs.getInt("user_id"));
                System.out.println("Name        : " + rs.getString("name"));
                System.out.println("Username    : " + rs.getString("username"));
                System.out.println("Email       : " + rs.getString("email"));
                System.out.println("Skills      : " + rs.getString("skills"));
                System.out.println("Experience  : " + rs.getString("experience"));
                System.out.println("Portfolio   : " + rs.getString("portfolio"));
            }
            else{
                System.out.println("Freelancer not found.");
            }
        }
        catch(Exception e){
            System.out.println("Error while loading freelancer profile.");
            e.printStackTrace();
        }
    }
    private void editFreelancerProfile(){
        int userId = userAuthentication.getLoggedInUserId();
        System.out.print("Enter Skills : ");
        String skills=sc.nextLine();
        System.out.print("Enter Experience : ");
        String experience=sc.nextLine();
        System.out.print("Enter Portfolio : ");
        String portfolio=sc.nextLine();
        String checkSql="SELECT user_id FROM freelancers WHERE user_id = ?";
        String insertSql="INSERT INTO freelancers "+"(user_id, skills, experience, portfolio) "+"VALUES (?, ?, ?, ?)";
        String updateSql="UPDATE freelancers SET skills = ?, "+"experience = ?, portfolio = ? "+"WHERE user_id = ?";
        try(Connection con = DBConnection.getConnection();
             PreparedStatement checkPs=con.prepareStatement(checkSql)) {
            checkPs.setInt(1, userId);
            ResultSet rs=checkPs.executeQuery();
            if(rs.next()){
                try(PreparedStatement ps=con.prepareStatement(updateSql)){
                    ps.setString(1, skills);
                    ps.setString(2, experience);
                    ps.setString(3, portfolio);
                    ps.setInt(4, userId);
                    ps.executeUpdate();
                    System.out.println("Freelancer Profile Updated Successfully!");
                }
            }
            else{
                try(PreparedStatement ps=con.prepareStatement(insertSql)){
                    ps.setInt(1, userId);
                    ps.setString(2, skills);
                    ps.setString(3, experience);
                    ps.setString(4, portfolio);
                    ps.executeUpdate();
                    System.out.println("Freelancer Profile Added Successfully!");
                }
            }
        }
        catch(Exception e){
            System.out.println("Error while saving freelancer profile.");
            e.printStackTrace();
        }
    }
}
