package olp.database;

import java.sql.*;

public class Initialization {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/olp";
        String user = "root";
        String password = "";

        // Parameters for the student and the course they want to add
        String targetStudentId = "S049091";
        String targetSubject = "CS";
        String targetCourseNo = "320";
        String targetSectionNo = "1";
        String targetMajor = "Computer Science";
        String targetMinor = "Civil Engineering";
        Double targetCreditLimit = 30.0;


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(url, user, password)) {

                try (Statement stmt = conn.createStatement()) {
                    //sql de daha oluşturmadıysak diye student tablosu
                    stmt.execute("CREATE TABLE IF NOT EXISTS Students (" +
                            "student_id VARCHAR(50) PRIMARY KEY, " +
                            "major VARCHAR(100), " +
                            "minor VARCHAR(100), " +
                            "remaining_credits DOUBLE," +
                            "credit_limit DOUBLE)");

                    //dml de eklemediysek örnek student tablosu insertleri
                    stmt.execute("INSERT IGNORE INTO Students VALUES ('S049091', 'Computer Science', NULL, 60.0, 18.0)");

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

                    stmt.execute("CREATE TABLE IF NOT EXISTS AcademicHistory  (" +
                            "history_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "student_id VARCHAR(50), " +
                            "SUBJECT VARCHAR(10), " +
                            "COURSENO VARCHAR(10), " +
                            "UNIQUE (student_id, SUBJECT, COURSENO), " +
                            "FOREIGN KEY (student_id) REFERENCES Students(student_id))");

                    stmt.execute("INSERT IGNORE INTO AcademicHistory (student_id, SUBJECT, COURSENO) VALUES ('S049091', 'CS', '202')");


                    //dml de eklemediysek örnek course tablosu insertleri
                    stmt.execute("INSERT IGNORE INTO Courses VALUES ('CS', '320', '1', 'Software Engineering', 'Engineering', 6.0, 'Hasan Sözer', '2023-Fall', NULL, 'CS 202', 'Description', 'MWF 10:00-11:00')");
                    stmt.execute("INSERT IGNORE INTO Courses VALUES ('CS', '202', '1', 'Data Structures', 'Engineering', 6.0, 'Hüseyi Ulusoy', '2023-Spring', NULL, NULL, 'Intro', 'TR 14:00-15:30')");

                    // öğrenciye önereceğimiz potensiyel planların tablosu
                    stmt.execute("CREATE TABLE IF NOT EXISTS StudentCourse (" +
                            "student_id VARCHAR(50)," +
                            "COURSENO VARCHAR(10), " +
                            "SUBJECT VARCHAR(100),"+
                            "SECTIONNO VARCHAR(10), " +
                            "TITLE VARCHAR(255), " +
                            "CREDITS DOUBLE, " +
                            "SCHEDULEFORPRINT VARCHAR(100), " +
                            " FOREIGN KEY (student_id) REFERENCES Students(student_id)," +
                            " FOREIGN KEY (SUBJECT, COURSENO, SECTIONNO) REFERENCES Courses(SUBJECT, COURSENO, SECTIONNO)" +
                            ")"
                    );


                }

                System.out.println("Choose the Subject of the course that you definitely want to enroll " + targetMajor + " ===");
                String courseQuery = "SELECT * FROM Courses WHERE Subject = ?";
                try (PreparedStatement stmt = conn.prepareStatement(courseQuery)) {
                    stmt.setString(1, targetSubject);
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            System.out.printf("%s %s: %s (Prereq: %s)%n",
                                    rs.getString("SUBJECT"),
                                    rs.getString("COURSENO"),
                                    rs.getString("TITLE"),
                                    rs.getString("PREREQUISITE"));
                        }
                    }
                }

                System.out.println("\n=== Attempting to Add Course: " + targetSubject + " " + targetCourseNo + " ===");

                boolean canEnroll = true;

                String checkPrerequisite = "SELECT PREREQUISITE FROM Courses WHERE SUBJECT = ? AND COURSENO = ?";
                try (PreparedStatement ps = conn.prepareStatement(checkPrerequisite)) {
                    ps.setString(1, targetSubject);
                    ps.setString(2, targetCourseNo);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            String prereq = rs.getString("PREREQUISITE");
                            if (prereq != null && !prereq.trim().isEmpty()) {
                                System.out.println("Course has prerequisite: " + prereq);
                                String[] parts = prereq.trim().split("\\s+");
                                if (parts.length >= 2) {
                                    String preSubject = parts[0];
                                    String preCourseNo = parts[1];

                                    String checkHistoryQuery = "SELECT * FROM AcademicHistory WHERE student_id = ? AND SUBJECT = ? AND COURSENO = ?";
                                    try (PreparedStatement psHist = conn.prepareStatement(checkHistoryQuery)) {
                                        psHist.setString(1, targetStudentId);
                                        psHist.setString(2, preSubject);
                                        psHist.setString(3, preCourseNo);
                                        try (ResultSet rsHist = psHist.executeQuery()) {
                                            if (rsHist.next()) {
                                                System.out.println(" - Prerequisite met (Found in AcademicHistory).");
                                            } else {
                                                System.out.println("Enrollment failed: Prerequisite (" + prereq + ") not taken.");
                                                canEnroll = false;
                                            }
                                        }
                                    }
                                }
                            } else {
                                System.out.println(" - No prerequisite for this course.");
                            }
                        }
                    }
                }

                if (canEnroll) {
                    double newCourseCredits = 0.0;
                    String getCourseCreditsSql = "SELECT CREDITS FROM Courses WHERE SUBJECT=? AND COURSENO=? AND SECTIONNO=?";
                    try(PreparedStatement ps = conn.prepareStatement(getCourseCreditsSql)){
                        ps.setString(1, targetSubject);
                        ps.setString(2, targetCourseNo);
                        ps.setString(3, targetSectionNo);
                        try(ResultSet rs = ps.executeQuery()){
                            if(rs.next()){
                                newCourseCredits = rs.getDouble("CREDITS");
                            }
                        }
                    }


                    double studentLimit = 0.0;
                    String getStudentLimitSql = "SELECT credit_limit FROM Students WHERE student_id=?";
                    try(PreparedStatement ps = conn.prepareStatement(getStudentLimitSql)){
                        ps.setString(1, targetStudentId);
                        try(ResultSet rs = ps.executeQuery()){
                            if(rs.next()){
                                studentLimit = rs.getDouble("credit_limit");
                            }
                        }
                    }


                    double currentDraftCredits = 0.0;
                    String getDraftCreditsSql = "SELECT SUM(CREDITS) as total FROM StudentCourse WHERE student_id=?";
                    try(PreparedStatement ps = conn.prepareStatement(getDraftCreditsSql)){
                        ps.setString(1, targetStudentId);
                        try(ResultSet rs = ps.executeQuery()){
                            if(rs.next()){
                                currentDraftCredits = rs.getDouble("total");
                            }
                        }
                    }

                    System.out.println("Credit Check: Limit=" + studentLimit + ", Current=" + currentDraftCredits + ", New Course=" + newCourseCredits);

                    if (currentDraftCredits + newCourseCredits > studentLimit) {
                        System.out.println("Credit Limit Exceeded");
                        canEnroll = false;
                    } else {
                        System.out.println("Credit Check Passed.");
                    }
                }

                if (canEnroll) {
                    String fetchCourseDetails = "SELECT TITLE, CREDITS, SCHEDULEFORPRINT FROM Courses WHERE SUBJECT=? AND COURSENO=? AND SECTIONNO=?";
                    String title = "";
                    double credits = 0.0;
                    String schedule = "";

                    try (PreparedStatement ps = conn.prepareStatement(fetchCourseDetails)) {
                        ps.setString(1, targetSubject);
                        ps.setString(2, targetCourseNo);
                        ps.setString(3, targetSectionNo);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                title = rs.getString("TITLE");
                                credits = rs.getDouble("CREDITS");
                                schedule = rs.getString("SCHEDULEFORPRINT");
                            }
                        }
                    }

                    String enrollQuery = "INSERT INTO StudentCourse(student_id, SUBJECT, COURSENO, SECTIONNO, TITLE, CREDITS, SCHEDULEFORPRINT) Values (?,?,?,?,?,?,?)";
                    try (PreparedStatement ps = conn.prepareStatement(enrollQuery)) {
                        ps.setString(1, targetStudentId);
                        ps.setString(2, targetSubject);
                        ps.setString(3, targetCourseNo);
                        ps.setString(4, targetSectionNo);
                        ps.setString(5, title);
                        ps.setDouble(6, credits);
                        ps.setString(7, schedule);

                        int result = ps.executeUpdate();
                        if (result > 0) {
                            System.out.println("Enrollment successful for " + targetStudentId);
                        }
                    }
                }

                String insertStudent = "INSERT IGNORE INTO Students(student_id, major, minor,credit_limit) Values(?,?,?,?)";
                try (PreparedStatement smt = conn.prepareStatement(insertStudent)) {
                    smt.setString(1, targetStudentId);
                    smt.setString(2, targetMajor);
                    smt.setString(3, targetMinor);
                    smt.setDouble(4, targetCreditLimit);
                    smt.executeUpdate();
                }

                // srs olp 1.3 ders plandan ders silme
                System.out.println("Ders kaldırılıyor" + targetSubject + " " + targetCourseNo + " ===");
                String removeQuery = "DELETE FROM StudentCourse WHERE student_id = ? AND SUBJECT = ? AND COURSENO = ? AND SECTIONNO = ?";
                try (PreparedStatement ps = conn.prepareStatement(removeQuery)) {
                    ps.setString(1, targetStudentId);
                    ps.setString(2, targetSubject);
                    ps.setString(3, targetCourseNo);
                    ps.setString(4, targetSectionNo);
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Success: Course removed from Draft Plan.");
                    } else {
                        System.out.println("Message: Course not found in Draft Plan to remove.");
                    }
                }

            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}