package olp.database;

import java.sql.*;

public class Initialization {



    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/olp";
        String user = "root";
        String password = "";

        String query = "SELECT * FROM Courses WHERE Subject = ? AND CourseNo = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, "CS");
                stmt.setString(2, "320");

                try (ResultSet rs = stmt.executeQuery()) {
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

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database error.");
            e.printStackTrace();
        }
    }

}
