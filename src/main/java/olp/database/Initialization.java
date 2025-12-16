package olp.database;

import java.sql.*;

public class Initialization {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/olp";
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(url, user, password)) {


                String courseQuery = "SELECT * FROM Courses WHERE Subject = ? AND CourseNo = ?";
                try (PreparedStatement stmt = conn.prepareStatement(courseQuery)) {
                    stmt.setString(1, "CS");
                    stmt.setString(2, "320");

                    try (ResultSet rs = stmt.executeQuery()) {
                        System.out.println("=== COURSES ===");
                        while (rs.next()) {
                            String subject = rs.getString("Subject");
                            String courseNo = rs.getString("CourseNo");
                            String sectionNo = rs.getString("SectionNo");
                            String title = rs.getString("Title");
                            String faculty = rs.getString("Faculty");
                            double credits = rs.getDouble("Credits");
                            String instructor = rs.getString("InstructorFullName");
                            String partOfTerm = rs.getString("PartOfTerm");

                            System.out.printf("%s %s-%s: %s (%s) %.1f credits, Instructor: %s, Term: %s%n",
                                    subject, courseNo, sectionNo, title, faculty, credits, instructor, partOfTerm);
                        }
                    }
                }

                String studentQuery = "SELECT * FROM Students WHERE student_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(studentQuery)) {

                    stmt.setString(1, "S049091");

                    try (ResultSet rs = stmt.executeQuery()) {
                        System.out.println("\n=== STUDENTS ===");
                        while (rs.next()) {
                            String studentId = rs.getString("student_id");
                            String major = rs.getString("major");
                            String minor = rs.getString("minor");
                            int creditLimit = rs.getInt("credit_limit");

                            System.out.printf("Student ID: %s, Major: %s, Minor: %s, Credit Limit: %d%n",
                                    studentId, major, minor != null ? minor : "None", creditLimit);
                        }
                    }
                }

            }

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database error.");
            e.printStackTrace();
        }
    }

}