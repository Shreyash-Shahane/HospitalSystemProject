package HospitalManagmentSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class paitent {
    private Connection connection;
    private Scanner scanner ;

    public paitent(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }
    
    public void addpaitent(){
        System.out.print("Enter paitent Name: ");
        String name = scanner.next();
        System.out.print("Enter paitent Age: ");
        int age = scanner.nextInt();
        System.out.print("Enter Paitent Gender: ");
        String gender = scanner.next();

        try {
            String query = "INSERT INTO paitents (name, age, gender) VALUES(? ,? , ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows>0){
                System.out.println(" Paitent added successfully");
            }
            else {
                System.out.println("Failed to add patient");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void viewPaitents(){
        String query = "SELECT * FROM  paitents";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Paitents: ");
            System.out.println("+------------+-------------------------+---------+----------------+");
            System.out.println("| Paitent Id | Name                    | Age     | Gender         |");
            System.out.println("+------------+-------------------------+---------+----------------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-12s|%-20s|%-10s|%-12s\n",id, name, age, gender);
                System.out.println("+------------+-------------------------+---------+----------------+");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getPaitentById(int id){
        String query = "SELECT * FROM paitents WHERE id =?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
