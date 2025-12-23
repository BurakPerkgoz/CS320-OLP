package olp.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpSession;
import olp.database.Connection;
import olp.model.CourseDetailsModel;
import olp.model.CourseModel;
import olp.database.FacultyType;
import olp.model.GraduationTableModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Controller
public class Controller {

    private String lastOrderBy = "";
    private String lastOrderDir = "ASC";

    @GetMapping("/")
    public String home(Model model, HttpSession session) {

        if (session.getAttribute("student_id") == null) {
            return "forward:/login.html";
        }

        model.addAttribute("student_id", session.getAttribute("student_id"));
        model.addAttribute("name_surname", session.getAttribute("name_surname"));
        model.addAttribute("credit", session.getAttribute("credit"));
        model.addAttribute("major", session.getAttribute("major"));
        model.addAttribute("semester", session.getAttribute("semester"));

        System.out.println("Student ID: " + session.getAttribute("student_id"));
        System.out.println("Name: " + session.getAttribute("name_surname"));
        System.out.println("Credit: " + session.getAttribute("credit"));
        System.out.println("Major: " + session.getAttribute("major"));
        System.out.println("Semester: " + session.getAttribute("semester"));

        return "forward:/courses.html";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        return "forward:/login.html";
    }

    @GetMapping("/graduation")
    public String graduation(HttpSession session) {

        if (session.getAttribute("student_id") == null) {
            return "forward:/login.html";
        }

        return "forward:/graduation.html";
    }

    @GetMapping("/program")
    public String program(HttpSession session) {

        if (session.getAttribute("student_id") == null) {
            return "forward:/login.html";
        }

        return "forward:/program.html";
    }



    @GetMapping("/get-program")
    public ResponseEntity<String> getProgram(HttpSession session) throws SQLException {

        if (session.getAttribute("student_id") == null) {
            return ResponseEntity.ok("user not logged in.");
        }

        List<Course> courses = Connection.getCoursesOfMajor((String) session.getAttribute("major"), (String) session.getAttribute("semester"));

        JsonArray jsonArray = new JsonArray();

        for (Course cours : courses) {

            if (cours.getScheduleForPrint() == null) {
                continue;
            }

            String[] timelines = cours.getScheduleForPrint().contains(", ") ? cours.getScheduleForPrint().split(", ") : new String[] { cours.getScheduleForPrint() };

            for (String timeline : timelines) {
                JsonObject obj = new JsonObject();
                obj.addProperty("title", cours.getTitle() + "\n" + cours.getCourseNo() + "." + cours.getSectionNo());

                JsonArray daysOfWeek = new JsonArray();

                if (timeline.contains("Pazartesi")) {
                    daysOfWeek.add(0);
                } else if (timeline.contains("Salı")) {
                    daysOfWeek.add(1);
                } else if (timeline.contains("Çarşamba")) {
                    daysOfWeek.add(2);
                } else if (timeline.contains("Perşembe")) {
                    daysOfWeek.add(3);
                } else if (timeline.contains("Cuma")) {
                    daysOfWeek.add(4);
                } else if (timeline.contains("Cumartesi")) {
                    daysOfWeek.add(5);
                } else if (timeline.contains("Pazar")) {
                    daysOfWeek.add(6);
                }

                if (timeline.contains(" | ")) {
                    String[] startEndTimes = timeline.split(" \\| ")[1].split(" - ");

                    obj.add("daysOfWeek", daysOfWeek);
                    obj.addProperty("startTime", startEndTimes[0]);
                    obj.addProperty("endTime", startEndTimes[1]);

                    jsonArray.add(obj);
                }
                else {
                    System.out.println(cours.getTitle() + "\n" + cours.getCourseNo());
                }
            }
        }

        System.out.println(jsonArray);
        return ResponseEntity.ok(jsonArray.toString());
    }

    @GetMapping(value = "/get-graduation", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getGraduation(HttpSession session) throws SQLException {

        if (session.getAttribute("student_id") == null) {
            return ResponseEntity.ok("user not logged in.");
        }

        return ResponseEntity.ok(GraduationTableModel.buildHtml((String) session.getAttribute("student_id"),(String) session.getAttribute("major")));
    }

    @PostMapping(value = "/took-course",
            consumes=MediaType.MULTIPART_FORM_DATA_VALUE,
            produces=MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> addTookenCourse(HttpSession session,
                                                  @RequestParam("id") long id
    ) throws SQLException {

        if (session.getAttribute("student_id") == null) {
            return ResponseEntity.ok("user not logged in.");
        }

        Connection.addTakenCourse((String) session.getAttribute("student_id"), String.valueOf(id));

        return ResponseEntity.ok("course is added to tooken list.");
    }

    @PostMapping(value = "/get-course-details",
            consumes=MediaType.MULTIPART_FORM_DATA_VALUE,
            produces=MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getCourseDetails(@RequestParam("id") long id) throws SQLException {
        return ResponseEntity.ok(CourseDetailsModel.buildHtml(id));
    }

    @PostMapping(value="/student-login",
            consumes=MediaType.MULTIPART_FORM_DATA_VALUE,
            produces=MediaType.TEXT_HTML_VALUE)
    public String handleFormSubmission(
            @RequestParam("student_id") String studentId,
            @RequestParam("name_surname") String nameSurname,
            @RequestParam("credit") int credit,
            @RequestParam("major") String major,
            @RequestParam("semester") String semester,
            @RequestParam("action") String action,
            HttpSession session) throws SQLException {

        session.setAttribute("student_id", studentId);
        session.setAttribute("name_surname", nameSurname);
        session.setAttribute("credit", credit);
        session.setAttribute("major", major);
        session.setAttribute("semester", semester);

        if (!Connection.studentExists(studentId)) {
            List<Course> courses = Connection.getCoursesOfMajor(major, semester);

            StringBuilder currentCourses = new StringBuilder();

            for (Course course : courses) {
                currentCourses.append(course.getId() + ",");
            }

            currentCourses.deleteCharAt(currentCourses.length() - 1);

            Connection.insertStudent(studentId, major, credit, "", semester, nameSurname, currentCourses.toString());
        }

        System.out.println("Student ID: " + studentId);
        System.out.println("Name: " + nameSurname);
        System.out.println("Credit: " + credit);
        System.out.println("Major: " + major);
        System.out.println("Semester: " + semester);

        // Process data (e.g., save to database or other business logic)

        return "Post login success";
    }


    @PostMapping(value="/get-courses",
            consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces=MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> handleCourses(
            @RequestParam Map<String, String> params,
            @RequestParam(name="coursefilter-category", required=false) List<Integer> facultyIds,
            HttpSession session
    ) {

        int postPerPage = Integer.parseInt(params.getOrDefault("post_per_page","12"));
        int offset = Integer.parseInt(params.getOrDefault("offset","0"));
        String searchText = params.getOrDefault("search_text","").trim();
        String orderBy = params.getOrDefault("order_by","Id");
        String orderDir = "ASC";

        if (lastOrderBy.equals(orderBy)) {
            orderDir = lastOrderDir.equals("ASC") ? "DESC" : "ASC";
            lastOrderDir = orderDir;
        }

        lastOrderBy = orderBy;

        List<String> faculties =
                facultyIds == null ? List.of() : FacultyType.resolveDbValues(facultyIds);

        try {
            int total = Connection.getTotalCourseCount(searchText, faculties);
            String html = CourseModel.buildCoursesHtml(
                    Connection.getCourses(offset, postPerPage, searchText, faculties, orderBy, orderDir),
                    offset, postPerPage, total
            );
            return ResponseEntity.ok(html);
        } catch (Exception e) {
            return ResponseEntity.ok(e.toString());
        }
    }


}