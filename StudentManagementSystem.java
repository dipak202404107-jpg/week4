
import java.sql.*;
import java.util.Scanner;

public class StudentManagementSystem {

    private static final String URL =
            "jdbc:mysql://localhost:3306/studentdb";
    private static final String USER = "root";

    // Change this to your own MySQL password
    private static final String PASSWORD = "your_password";

    private static final Scanner sc = new Scanner(System.in);

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    static void addStudent() {
        String sql =
                "INSERT INTO students (id, name, course, marks) VALUES (?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Student ID: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("Enter Student Name: ");
            String name = sc.nextLine().trim();

            System.out.print("Enter Course: ");
            String course = sc.nextLine().trim();

            System.out.print("Enter Marks: ");
            double marks = Double.parseDouble(sc.nextLine());

            if (id <= 0 || name.isEmpty() || course.isEmpty()
                    || !Double.isFinite(marks)
                    || marks < 0 || marks > 100) {
                System.out.println("Invalid student details.");
                return;
            }

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, course);
            ps.setDouble(4, marks);

            ps.executeUpdate();
            System.out.println("Student added successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numeric values.");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Student ID already exists.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    static void viewStudents() {
        String sql = "SELECT * FROM students ORDER BY id";

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n--- Student Records ---");

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Course: " + rs.getString("course"));
                System.out.println("Marks: " + rs.getDouble("marks"));
                System.out.println("----------------------");
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    static void updateStudent() {
        String sql =
                "UPDATE students SET name=?, course=?, marks=? WHERE id=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Student ID to update: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("Enter New Name: ");
            String name = sc.nextLine().trim();

            System.out.print("Enter New Course: ");
            String course = sc.nextLine().trim();

            System.out.print("Enter New Marks: ");
            double marks = Double.parseDouble(sc.nextLine());

            if (name.isEmpty() || course.isEmpty()
                    || !Double.isFinite(marks)
                    || marks < 0 || marks > 100) {
                System.out.println("Invalid student details.");
                return;
            }

            ps.setString(1, name);
            ps.setString(2, course);
            ps.setDouble(3, marks);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student not found.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numeric values.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    static void deleteStudent() {
        String sql = "DELETE FROM students WHERE id=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Student ID to delete: ");
            int id = Integer.parseInt(sc.nextLine());

            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student not found.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid ID.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(
                "MySQL JDBC driver missing. Add the MySQL Connector/J JAR."
            );
            return;
        }

        while (true) {
            System.out.println("\n=== Student Management System ===");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    addStudent();
                    break;
                case "2":
                    viewStudents();
                    break;
                case "3":
                    updateStudent();
                    break;
                case "4":
                    deleteStudent();
                    break;
                case "5":
                    System.out.println("Program closed.");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
