package olp.database;

import olp.controller.Course;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

public class Connection {

    private static final String URL = "jdbc:mysql://localhost:3306/olp";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static java.sql.Connection connection;

    public static java.sql.Connection init() throws SQLException, ClassNotFoundException {
        if (connection == null) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    public static Course mapMajorCourseToGeneralCourse(String subject, String code) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM courses WHERE Subject = '" + subject + "' AND CourseNo = '" + code + "'")) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Course c = new Course();
                    c.setId(rs.getLong("Id"));
                    c.setTitle(rs.getString("Title"));
                    c.setDescription(rs.getString("Description"));
                    c.setSubject(rs.getString("Subject"));
                    c.setCredits(rs.getDouble("Credits"));
                    c.setCourseNo(rs.getString("CourseNo"));
                    c.setSectionNo(rs.getString("SectionNo"));
                    c.setInstructorFullName(rs.getString("InstructorFullName"));
                    c.setPartOfTerm(rs.getString("PartOfTerm"));
                    c.setCorequisite(rs.getString("Corequisite"));
                    c.setPrerequisite(rs.getString("Prerequisite"));
                    c.setScheduleForPrint(rs.getString("ScheduleForPrint"));
                    c.setFaculty(rs.getString("Faculty"));
                    return c;
                }
            }
        }

        return null;
    }

    public static boolean studentExists(String studentId) {
        String query = "SELECT COUNT(*) FROM students WHERE student_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void insertStudent(String studentId, String major, int creditLimit, String takenCourses,
                                     String semester, String nameSurname, String currentCourses, int totalCredits) {
        String query = "INSERT INTO students (student_id, major, credit_limit, tooken_courses, semester, name_surname, current_courses, total_credits) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, studentId);
            statement.setString(2, major);
            statement.setInt(3, creditLimit);
            statement.setString(4, takenCourses);
            statement.setString(5, semester);
            statement.setString(6, nameSurname);
            statement.setString(7, currentCourses);
            statement.setInt(8, totalCredits);

            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateStudent(String studentId, String major, int creditLimit, String takenCourses,
                                     String semester, String nameSurname, String currentCourses) {
        String query = "UPDATE students SET major = ?, credit_limit = ?, tooken_courses = ?, semester = ?, " +
                "name_surname = ?, current_courses = ? WHERE student_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, major);
            statement.setInt(2, creditLimit);
            statement.setString(3, takenCourses);
            statement.setString(4, semester);
            statement.setString(5, nameSurname);
            statement.setString(6, currentCourses);
            statement.setString(7, studentId);

            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getTakenCourses(String studentId) {
        String query = "SELECT tooken_courses FROM students WHERE student_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("tooken_courses");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteTakenCourses(String studentId) {
        String query = "UPDATE students SET tooken_courses = '' WHERE student_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, studentId);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentCourses(String studentId) {
        String query = "SELECT current_courses FROM students WHERE student_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("current_courses");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteCurrentCourses(String studentId) {
        String query = "UPDATE students SET current_courses = '' WHERE student_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, studentId);

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addTakenCourse(String studentId, String newCourseId) {
        String currentCourses = getTakenCourses(studentId);
        if (currentCourses == null || currentCourses.isEmpty()) {
            currentCourses = newCourseId;
        } else {
            currentCourses += "," + newCourseId;
        }

        updateTakenCourses(studentId, currentCourses);
    }

    public static void addCurrentCourse(String studentId, String newCourseId) {
        String currentCourses = getCurrentCourses(studentId);
        if (currentCourses == null || currentCourses.isEmpty()) {
            currentCourses = newCourseId;
        } else {
            currentCourses += "," + newCourseId;
        }

        updateCurrentCourses(studentId, currentCourses);
    }

    public static void removeTakenCourse(String studentId, String courseIdToRemove) {
        String currentCourses = getTakenCourses(studentId);
        if (currentCourses != null && !currentCourses.isEmpty()) {
            String[] courses = currentCourses.split(",");
            StringBuilder updatedCourses = new StringBuilder();

            for (String course : courses) {
                if (!course.equals(courseIdToRemove)) {
                    if (updatedCourses.length() > 0) {
                        updatedCourses.append(",");
                    }
                    updatedCourses.append(course);
                }
            }

            updateTakenCourses(studentId, updatedCourses.toString());
        }
    }

    public static void removeCurrentCourse(String studentId, String courseIdToRemove) {
        String currentCourses = getCurrentCourses(studentId);
        if (currentCourses != null && !currentCourses.isEmpty()) {
            String[] courses = currentCourses.split(",");
            StringBuilder updatedCourses = new StringBuilder();

            for (String course : courses) {
                if (!course.equals(courseIdToRemove)) {
                    if (updatedCourses.length() > 0) {
                        updatedCourses.append(",");
                    }
                    updatedCourses.append(course);
                }
            }

            updateCurrentCourses(studentId, updatedCourses.toString());
        }
    }

    private static void updateTakenCourses(String studentId, String newCourses) {
        String query = "UPDATE students SET tooken_courses = ? WHERE student_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, newCourses);
            statement.setString(2, studentId);
            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated in taken courses.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateCurrentCourses(String studentId, String newCourses) {
        String query = "UPDATE students SET current_courses = ? WHERE student_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, newCourses);
            statement.setString(2, studentId);
            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated in current courses.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Course> getCoursesOfMajor(String major, String quarter) throws SQLException {

        boolean hasQuarter = quarter != null && !quarter.isBlank();

        StringBuilder sql = new StringBuilder("""
            SELECT *
            FROM""");

        sql.append(" ");

        sql.append(major);

        if (hasQuarter) {
            sql.append(" WHERE SEMESTER = 'Q");

            sql.append(quarter);

            sql.append("'");
        }

        List<Course> courses = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("TITLE");
                    double credits = rs.getDouble("CREDITS");

                    if (title.contains("Seçmeli")) {
                        Course secmeliCourse = new Course();

                        secmeliCourse.setTitle(title);
                        secmeliCourse.setCredits(credits);

                        courses.add(secmeliCourse);

                        continue;
                    }

                    String code = rs.getString("CODE");

                    String subject = code.split(" ")[0];
                    code = code.split(" ")[1];


                    Course course = mapMajorCourseToGeneralCourse(subject, code);

                    if (course == null) {
                        Course secCourse = new Course();

                        secCourse.setTitle(title);
                        secCourse.setCredits(credits);
                        secCourse.setSubject(subject);
                        secCourse.setCourseNo(code);

                        courses.add(secCourse);
                    }
                    else {
                        courses.add(course);
                    }
                }
            }
        }

        return courses;
    }

    public static Course getCourseByID(long id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM courses WHERE Id = " + id)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Course c = new Course();
                    c.setId(rs.getLong("Id"));
                    c.setTitle(rs.getString("Title"));
                    c.setDescription(rs.getString("Description"));
                    c.setSubject(rs.getString("Subject"));
                    c.setCredits(rs.getDouble("Credits"));
                    c.setCourseNo(rs.getString("CourseNo"));
                    c.setSectionNo(rs.getString("SectionNo"));
                    c.setInstructorFullName(rs.getString("InstructorFullName"));
                    c.setPartOfTerm(rs.getString("PartOfTerm"));
                    c.setCorequisite(rs.getString("Corequisite"));
                    c.setPrerequisite(rs.getString("Prerequisite"));
                    c.setScheduleForPrint(rs.getString("ScheduleForPrint"));
                    c.setFaculty(rs.getString("Faculty"));
                    return c;
                }
            }
        }

        return null;
    }

    public static Course getCourseFromMajorByID(String major, long id) throws SQLException {

        System.out.println("SELECT * FROM " + major + " WHERE Id = " + id);

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM " + major + " WHERE Id = " + id)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String codeAll = rs.getString("CODE");

                    System.out.println(codeAll);

                    return mapMajorCourseToGeneralCourse(codeAll.split(" ")[0], codeAll.split(" ")[1]);
                }
            }
        }

        return null;
    }

    public static List<Course> getCourses(
            int offset,
            int limit,
            String searchText,
            List<String> faculties,
            String orderBy,
            String orderDir
    ) throws SQLException {

        boolean hasSearch = searchText != null && !searchText.isBlank();
        boolean hasFaculty = faculties != null && !faculties.isEmpty();

        StringBuilder sql = new StringBuilder("""
            SELECT *
            FROM Courses
            WHERE 1=1
        """);

        if (hasSearch) sql.append(" AND (CourseNo LIKE ? OR Subject LIKE ?)");
        if (hasFaculty) {
            sql.append(" AND Faculty IN (");
            sql.append("?,".repeat(faculties.size()));
            sql.setLength(sql.length() - 1);
            sql.append(")");
        }

        sql.append(" ORDER BY ").append(resolveOrderBy(orderBy, orderDir));
        sql.append(" LIMIT ? OFFSET ?");

        System.out.println(sql);

        List<Course> courses = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {

            int idx = 1;
            if (hasSearch) {
                String like = "%" + searchText + "%";
                stmt.setString(idx++, like);
                stmt.setString(idx++, like);
            }

            if (hasFaculty) {
                for (String f : faculties) stmt.setString(idx++, f);
            }

            stmt.setInt(idx++, limit);
            stmt.setInt(idx, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Course c = new Course();
                    c.setId(rs.getLong("Id"));
                    c.setTitle(rs.getString("Title"));
                    c.setDescription(rs.getString("Description"));
                    c.setSubject(rs.getString("Subject"));
                    c.setCredits(rs.getDouble("Credits"));
                    c.setCourseNo(rs.getString("CourseNo"));
                    c.setSectionNo(rs.getString("SectionNo"));
                    c.setInstructorFullName(rs.getString("InstructorFullName"));
                    c.setPartOfTerm(rs.getString("PartOfTerm"));
                    c.setCorequisite(rs.getString("Corequisite"));
                    c.setPrerequisite(rs.getString("Prerequisite"));
                    c.setScheduleForPrint(rs.getString("ScheduleForPrint"));
                    c.setFaculty(rs.getString("Faculty"));
                    courses.add(c);
                }
            }
        }
        return courses;
    }

    public static int getTotalCourseCount(String searchText, List<String> faculties) throws SQLException {
        boolean hasSearch = searchText != null && !searchText.isBlank();
        boolean hasFaculty = faculties != null && !faculties.isEmpty();

        StringBuilder sql = new StringBuilder("""
            SELECT COUNT(*)
            FROM Courses
            WHERE 1=1
        """);

        if (hasSearch) sql.append(" AND (CourseNo LIKE ? OR Subject LIKE ? or Title LIKE ?)");
        if (hasFaculty) {
            sql.append(" AND Faculty IN (");
            sql.append("?,".repeat(faculties.size()));
            sql.setLength(sql.length() - 1);
            sql.append(")");
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int idx = 1;
            if (hasSearch) {
                String like = "%" + searchText + "%";
                stmt.setString(idx++, like);
                stmt.setString(idx++, like);
                stmt.setString(idx++, like);
            }
            if (hasFaculty) {
                for (String f : faculties) stmt.setString(idx++, f);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private static String resolveOrderBy(String orderBy, String sortDir) {
        String direction = "ASC".equalsIgnoreCase(sortDir) ? "ASC" : "DESC";

        return switch (orderBy) {
            case "credits" -> "Credits " + direction;
            case "course-code" -> "CourseNo " + direction;
            case "alphabetical" -> "Title " + direction;
            default -> "Id " + direction;
        };
    }
}
