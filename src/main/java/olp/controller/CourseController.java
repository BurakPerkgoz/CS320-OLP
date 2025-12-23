package olp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // SRS-OLP-4
    @GetMapping
    public List<Map<String, Object>> searchCourses(
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String courseNo,
            @RequestParam(required = false) String sectionNo,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String faculty,
            @RequestParam(required = false) Double credits,
            @RequestParam(required = false) String scheduleForPrint) {
        
        StringBuilder sql = new StringBuilder("SELECT * FROM Courses WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (subject != null && !subject.isBlank()) {
            sql.append(" AND SUBJECT = ? ");
            params.add(subject);
        }
        if (title != null && !title.isBlank()) {
            sql.append(" AND TITLE LIKE ? ");
            params.add("%" + title + "%");
        }
        if (faculty != null && !faculty.isBlank()) {
            sql.append(" AND FACULTY = ? ");
            params.add(faculty);
        }
        if (scheduleForPrint != null && !scheduleForPrint.isBlank()) {
            sql.append(" AND SCHEDULEFORPRINT = ? ");
            params.add(scheduleForPrint);
        }
        if (credits != null && !credits.isBlank()) {
            sql.append(" AND CREDITS = ? ");
            params.add(credits);
        }
        if (prerequisite != null && !prerequisite.isBlank()) {
            sql.append(" AND PREREQUISITE = ? ");
            params.add(prerequisite);
        }
        if(credits != null && !credits.isBlank()) {
            sql.append(" AND CREDITS = ? ");
            params.add(credits);
        }

        
        return jdbcTemplate.queryForList(sql.toString(), params.toArray());
    }

    @PostMapping("/enroll")
    public String enroll(@RequestBody Map<String, String> payload) {
        String studentId = payload.get("studentId");
        String subject = payload.get("subject");
        String courseNo = payload.get("courseNo");
        String sectionNo = payload.get("sectionNo");

        // 1. Check Prerequisites
        String prereqSql = "SELECT PREREQUISITE FROM Courses WHERE SUBJECT = ? AND COURSENO = ?";
        List<String> prereqs = jdbcTemplate.query(prereqSql, (rs, rowNum) -> rs.getString("PREREQUISITE"), subject, courseNo);

        if (!prereqs.isEmpty()) {
            String prereq = prereqs.get(0);
            if (prereq != null && !prereq.trim().isEmpty()) {
                String[] parts = prereq.trim().split("\\s+");
                if (parts.length >= 2) {
                    String preSubject = parts[0];
                    String preCourseNo = parts[1];

                    String checkHistorySql = "SELECT COUNT(*) FROM AcademicHistory WHERE student_id = ? AND SUBJECT = ? AND COURSENO = ?";
                    Integer count = jdbcTemplate.queryForObject(checkHistorySql, Integer.class, studentId, preSubject, preCourseNo);

                    if (count == null || count == 0) {
                        return "Enrollment Failed: Prerequisite (" + prereq + ") not completed.";
                    }
                }
            }
        }

        // 2. Check Credit Limit
        // Get Student's Credit Limit
        String limitSql = "SELECT credit_limit FROM Students WHERE student_id = ?";
        Double creditLimit;
        try {
            creditLimit = jdbcTemplate.queryForObject(limitSql, Double.class, studentId);
        } catch (Exception e) {
            creditLimit = 18.0; // Default if student not found or null
        }
        if (creditLimit == null) creditLimit = 18.0;

        // Get Current Enrolled Credits (Draft Plan)
        String currentCreditsSql = "SELECT SUM(CREDITS) FROM StudentCourse WHERE student_id = ?";
        Double currentCredits;
        try {
            currentCredits = jdbcTemplate.queryForObject(currentCreditsSql, Double.class, studentId);
        } catch (Exception e) {
            currentCredits = 0.0;
        }
        if (currentCredits == null) currentCredits = 0.0;

        // Get New Course Credits
        String courseCreditsSql = "SELECT CREDITS FROM Courses WHERE SUBJECT=? AND COURSENO=? AND SECTIONNO=?";
        List<Double> courseCreditsList = jdbcTemplate.query(courseCreditsSql, (rs, rowNum) -> rs.getDouble("CREDITS"), subject, courseNo, sectionNo);
        
        if (courseCreditsList.isEmpty()) return "Course not found";
        Double newCourseCredits = courseCreditsList.get(0);

        if (currentCredits + newCourseCredits > creditLimit) {
            return "Enrollment Failed: Credit limit exceeded. Limit: " + creditLimit + ", Current: " + currentCredits + ", New: " + newCourseCredits;
        }

        // Fetch details for insertion
        String detailsSql = "SELECT TITLE, CREDITS, SCHEDULEFORPRINT FROM Courses WHERE SUBJECT=? AND COURSENO=? AND SECTIONNO=?";
        List<Map<String, Object>> courses = jdbcTemplate.queryForList(detailsSql, subject, courseNo, sectionNo);
        
        if (courses.isEmpty()) return "Course not found";
        
        Map<String, Object> course = courses.get(0);
        String title = (String) course.get("TITLE");
        Double credits = ((Number) course.get("CREDITS")).doubleValue();
        String schedule = (String) course.get("SCHEDULEFORPRINT");

        // Check if already enrolled
        String checkDuplicateSql = "SELECT COUNT(*) FROM StudentCourse WHERE student_id = ? AND SUBJECT = ? AND COURSENO = ?";
        Integer dupCount = jdbcTemplate.queryForObject(checkDuplicateSql, Integer.class, studentId, subject, courseNo);
        if (dupCount != null && dupCount > 0) {
            return "Enrollment Failed: Already enrolled in this course.";
        }

        String sql = "INSERT INTO StudentCourse(student_id, SUBJECT, COURSENO, SECTIONNO, TITLE, CREDITS, SCHEDULEFORPRINT) Values (?,?,?,?,?,?,?)";
        int result = jdbcTemplate.update(sql, studentId, subject, courseNo, sectionNo, title, credits, schedule);
        
        return result > 0 ? "Enrollment Successful" : "Enrollment Failed";
    }

    @DeleteMapping("/drop")
    public String drop(@RequestBody Map<String, String> payload) {
        String studentId = payload.get("studentId");
        String subject = payload.get("subject");
        String courseNo = payload.get("courseNo");
        String sectionNo = payload.get("sectionNo");

        String sql = "DELETE FROM StudentCourse WHERE student_id = ? AND SUBJECT = ? AND COURSENO = ? AND SECTIONNO = ?";
        int result = jdbcTemplate.update(sql, studentId, subject, courseNo, sectionNo);
        
        return result > 0 ? "Dropped Successfully" : "Course not found in plan";
    }

    @PostMapping("/history/add")
    public String addToHistory(@RequestBody Map<String, String> payload) {
        String studentId = payload.get("studentId");
        String subject = payload.get("subject");
        String courseNo = payload.get("courseNo");
    
        if (studentId == null || subject == null || courseNo == null) {
                return "Missing required fields: studentId, subject, courseNo";
        }
    
        // Check if already in history
        String checkSql = "SELECT COUNT(*) FROM AcademicHistory WHERE student_id = ? AND SUBJECT = ? AND COURSENO = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, studentId, subject, courseNo);
        if (count != null && count > 0) {
            return "Course already in academic history.";
        }
    
        String sql = "INSERT INTO AcademicHistory (student_id, SUBJECT, COURSENO) VALUES (?, ?, ?)";
        int result = jdbcTemplate.update(sql, studentId, subject, courseNo);
        
        return result > 0 ? "Course added to Academic History successfully." : "Failed to add course.";
    }
}
