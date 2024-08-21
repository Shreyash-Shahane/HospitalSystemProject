package HospitalManagmentSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagmentSystem {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/HospitalManagmentSystem";
    private static final String username = "root";
    private static final String password = "SnS@8838";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            paitent paitent = new paitent(connection, scanner);
            doctor doctor = new doctor(connection);
            while (true) {
                System.out.println("HOSPITAL MANAGMENT SYSTEM");
                System.out.println("1. Add new patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.err.println("4. Book Appointments");
                System.out.println("5. Delete Patient");
                System.out.println("6. Exit");
                System.out.println("Enter your choice");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        // Add new patient
                        paitent.addpaitent();
                        System.out.println();
                        break;
                    case 2:
                        // View patients
                        paitent.viewPaitents();
                        System.out.println();
                        break;
                    case 3:
                        // View doctors
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        // Book Appointments
                        bookAppointment(paitent, doctor, connection, scanner);
                        System.out.println();
                        break;
                    case 5:
                        // Book Appointments
                        paitent.deletepaitent();
                        System.out.println();
                        break;
                    case 6:
                        // Exit
                        return;
                    default:
                        System.out.println("Enter Valid choice !!!");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(paitent paitent, doctor doctor, Connection connection, Scanner scanner) {
        System.out.println("Enter Paitent Id : ");
        int paitentId = scanner.nextInt();
        System.out.println("Enter Doctor Id : ");
        int doctorId = scanner.nextInt();
        System.out.println("Enter Appointment Date (YYYY-MM-DD) : ");
        String appointmentDate = scanner.next();
        if (paitent.getPaitentById(paitentId) && doctor.getDoctorById(doctorId)) {
            if (checkDoctorAvailability(doctorId, appointmentDate, connection)) {
                String query = "INSERT INTO appointments(paitent_id, doctor_id, appointment_date) VALUES(?, ?, ?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, paitentId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rowsaffected = preparedStatement.executeUpdate();
                    if (rowsaffected > 0) {
                        System.out.println(" Appointment Booked !");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Doctor not available on this date");
            }
        } else {
            System.out.println("Either Doctor or Patient does not exist");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmemtDate, Connection connection) {

        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmemtDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
