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
                    try (Statement stmt = conn.createStatement()) {
                        //sql de daha oluşturmadıysak diye student tablosu
                        stmt.execute("CREATE TABLE IF NOT EXISTS Students (" +
                                "student_id VARCHAR(50) PRIMARY KEY, " +
                                "major VARCHAR(100), " +
                                "minor VARCHAR(100), " +
                                "credit_limit INT)");

                        //dml de eklemediysek örnek student tablosu insertleri
                        stmt.execute("INSERT IGNORE INTO Students VALUES ('S049091', 'Computer Science', NULL, 18)");

                        // sql de daha oluşturmadıysak diye course tablosu(sis deki tablo bilgileri baz alınarak)
                        stmt.execute("CREATE TABLE IF NOT EXISTS Courses (" +
                                "SUBJECT VARCHAR(10), " +
                                "COURSENO VARCHAR(10), " +
                                "SECTIONNO VARCHAR(10), " +
                                "TITLE VARCHAR(255), " +
                                "FACULTY VARCHAR(100), " +
                                "CREDITS DOUBLE, " +
                                "INSTRUCTOR VARCHAR(100), " +
                                "PARTOFTERM VARCHAR(50), " +
                                "COREQUISITE VARCHAR(50), " +
                                "PREREQUISITE VARCHAR(50), " +
                                "DESCRIPTION TEXT, " +
                                "SCHEDULEFORPRINT VARCHAR(100), " +
                                "PRIMARY KEY (SUBJECT, COURSENO, SECTIONNO))");

                        //dml de eklemediysek örnek course tablosu insertleri
                        stmt.execute("INSERT IGNORE INTO Courses VALUES ('CS', '320', '1', 'Software Engineering', 'Engineering', 6.0, 'Hasan Sözer', '2023-Fall', NULL, 'CS 202', 'Description', 'MWF 10:00-11:00')");
                        stmt.execute("INSERT IGNORE INTO Courses VALUES ('CS', '202', '1', 'Data Structures', 'Engineering', 6.0, 'Hüseyi Ulusoy', '2023-Spring', NULL, NULL, 'Intro', 'TR 14:00-15:30')");

                        // öğrenciye önereceğimiz potensiyel planların tablosu
                        stmt.execute("CREATE TABLE IF NOT EXISTS StudentCourse (" +
                                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                "student_id VARCHAR(50), " +
                                "SUBJECT VARCHAR(10), " +
                                "COURSENO VARCHAR(10), " +
                                "SECTIONNO VARCHAR(10), " +
                                "FOREIGN KEY (student_id) REFERENCES Students(student_id), " +
                                "FOREIGN KEY (SUBJECT, COURSENO, SECTIONNO) REFERENCES Courses(SUBJECT, COURSENO, SECTIONNO))");


                    }

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
                String enrollQuery="INSERT INTO StudentCourse(student_id,Subject,CourseNo,SectionNo) Values (?,?,?,?)";
                try(PreparedStatement ps=conn.prepareStatement(enrollQuery)){


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