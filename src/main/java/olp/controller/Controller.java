package olp.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import olp.database.Connection;
import olp.model.CourseDetailsModel;
import olp.model.CourseModel;
import olp.database.FacultyType;
import olp.model.GraduationTableModel;
import olp.utils.AI;
import olp.utils.PdfGenerator;
import olp.utils.PdfServiceImpl;
import olp.utils.PrerequisiteParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @GetMapping("/get-student")
    public ResponseEntity<String> getStudent(HttpSession session) throws SQLException {

        return ResponseEntity.ok(session.getAttribute("name_surname").toString());
    }

    @GetMapping("/clear-program")
    public ResponseEntity<String> clearProgram(HttpSession session) throws SQLException {

        if (session.getAttribute("student_id") == null) {
            return ResponseEntity.ok("user not logged in.");
        }

        Connection.deleteTakenCourses((String) session.getAttribute("student_id"));
        Connection.deleteCurrentCourses((String) session.getAttribute("student_id"));

        return ResponseEntity.ok("program is cleared");
    }

    @GetMapping("/get-program")
    public ResponseEntity<String> getProgram(HttpSession session) throws SQLException {

        if (session.getAttribute("student_id") == null) {
            return ResponseEntity.ok("user not logged in.");
        }

        List<Course> courses = new ArrayList<>();

        String extra_taken_courses = Connection.getTakenCourses((String) session.getAttribute("student_id"));

        if (extra_taken_courses != null && !extra_taken_courses.isEmpty()) {
            if (extra_taken_courses.contains(",")) {
                for (String extra_course_id : extra_taken_courses.split(",")) {
                    Course extra_course = Connection.getCourseByID(Integer.parseInt(extra_course_id));
                    courses.add(extra_course);
                }
            }
        }

        String extra_current_courses = Connection.getCurrentCourses((String) session.getAttribute("student_id"));

        if (extra_current_courses != null && !extra_current_courses.isEmpty()) {
            if (extra_current_courses.contains(",")) {
                for (String extra_course_id : extra_current_courses.split(",")) {
                    Course extra_course = Connection.getCourseByID(Integer.parseInt(extra_course_id));
                    courses.add(extra_course);
                }
            }
        }

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

    @GetMapping("/export-pdf")
    public void exportPDF(
            HttpSession session,
            HttpServletResponse response
    ) {
        try {
            response.setContentType("application/pdf");
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=courses.pdf"
            );

            PdfGenerator.generateCurriculumPdf(
                    session.getAttribute("major").toString(),
                    session.getAttribute("student_id").toString(),
                    response.getOutputStream()
            );

            response.flushBuffer();

        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }
    }

    @PostMapping(
            value = "/export-program-pdf",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> exportProgramPDF(
            @RequestParam("img") String imageBase64
    ) throws IOException {

        String cleanBase64 = imageBase64.replace("data:image/png;base64,", "");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=program.pdf")
                .body(PdfServiceImpl.createPdfFromBase64(cleanBase64));
    }

    @PostMapping(value = "/ask-ai",
            consumes=MediaType.MULTIPART_FORM_DATA_VALUE,
            produces=MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> askAI(HttpSession session,
                                                  @RequestParam("text") String text
    ) throws SQLException, IOException, InterruptedException {

        return ResponseEntity.ok(AI.askAI((String) session.getAttribute("major"), String.valueOf(session.getAttribute("credit")), (String) session.getAttribute("semester"), text));

    }

    @PostMapping(value = "/took-course",
            consumes=MediaType.MULTIPART_FORM_DATA_VALUE,
            produces=MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> addTookenCourse(HttpSession session,
                                                  @RequestParam("id") long id,
                                                  @RequestParam("toggle") String toggle
    ) throws SQLException {


        if (session.getAttribute("student_id") == null) {
            return ResponseEntity.ok("user not logged in.");
        }

        Course target_course = Connection.getCourseByID(id);

        if (target_course == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("course not found.");
        }

        int credits = 0;

        String extra_taken_courses = Connection.getTakenCourses((String) session.getAttribute("student_id"));

        if (extra_taken_courses != null && !extra_taken_courses.isEmpty()) {
            if (extra_taken_courses.contains(",")) {
                for (String extra_course_id : extra_taken_courses.split(",")) {
                    Course extra_course = Connection.getCourseByID(Integer.parseInt(extra_course_id));
                    if (extra_course != null) {
                        credits += extra_course.getCredits();
                    }
                }
            }
        }

        String extra_current_courses = Connection.getCurrentCourses((String) session.getAttribute("student_id"));

        if (extra_current_courses != null && !extra_current_courses.isEmpty()) {
            if (extra_current_courses.contains(",")) {
                for (String extra_course_id : extra_current_courses.split(",")) {
                    Course extra_course = Connection.getCourseByID(Integer.parseInt(extra_course_id));
                    if (extra_course != null) {
                        credits += extra_course.getCredits();
                    }
                }
            }
        }

        if ((credits + target_course.getCredits()) > (Integer)(session.getAttribute("credit"))) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Credit limit is exceeded");
        }

        String prerequirements = target_course.getPrerequisite();

        if (prerequirements != null && !prerequirements.isEmpty()) {

            String[] prerequirement_array = prerequirements.split(" ");

            String needed_credits = "";

            for (int i = 0; i < prerequirement_array.length; ++i) {
                if (prerequirement_array[i].contains("AKTS") || prerequirement_array[i].contains("ECTS")) {
                    needed_credits = prerequirement_array[i - 1];
                    break;
                }
            }

            if (needed_credits != null && !needed_credits.isEmpty()) {
                if (Integer.parseInt((String) session.getAttribute("total_credit")) <  Integer.parseInt(needed_credits)) {

                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("Credit requirement not matched for this course, your credits : " + session.getAttribute("total_credit") + ", required credits : " + needed_credits);
                }
            }


            if (!prerequirements.contains("Dil sınavından başarılı sonucuna sahip olmak")) {
                PrerequisiteParser.Expr expr = PrerequisiteParser.parsePrerequisite(prerequirements);

                Set<String> student_taken_courses = new HashSet<>();

                String taken_courses = Connection.getTakenCourses((String) session.getAttribute("student_id"));

                if (taken_courses != null && taken_courses.contains(",")) {
                    String[] token_course_list =  taken_courses.split(",");

                    for (String taken_course_id : token_course_list) {
                        Course taken_course =  Connection.getCourseByID(Long.parseLong(taken_course_id));
                        student_taken_courses.add(taken_course.getSubject() + " " + taken_course.getCourseNo());
                    }

                    if (!PrerequisiteParser.satisfies(expr, student_taken_courses)) {
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body("Prerequirements are not met.");

                    }
                }

                String current_courses = Connection.getCurrentCourses((String) session.getAttribute("student_id"));

                if (current_courses != null && current_courses.contains(",")) {
                    String[] current_course_list =  current_courses.split(",");

                    for (String current_course_id : current_course_list) {
                        Course current_course =  Connection.getCourseByID(Long.parseLong(current_course_id));
                        student_taken_courses.add(current_course.getSubject() + " " + current_course.getCourseNo());
                    }

                    if (!PrerequisiteParser.satisfies(expr, student_taken_courses)) {
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body("Prerequirements are not met.");
                    }
                }
            }
        }

        if (toggle.equals("add")) {
            Connection.addTakenCourse((String) session.getAttribute("student_id"), String.valueOf(id));
        }
        else {
            Connection.removeTakenCourse((String) session.getAttribute("student_id"), String.valueOf(id));
        }

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
            @RequestParam("password") String password,
            @RequestParam("credit") int credit,
            @RequestParam("total_credits") int total_credits,
            @RequestParam("major") String major,
            @RequestParam("semester") String semester,
            @RequestParam("action") String action,
            HttpSession session) throws SQLException {

        session.setAttribute("student_id", studentId);
        session.setAttribute("name_surname", nameSurname);
        session.setAttribute("credit", credit);
        session.setAttribute("total_credits", total_credits);
        session.setAttribute("major", major);
        session.setAttribute("semester", semester);

        if (!Connection.studentExists(studentId)) {
            List<Course> courses = Connection.getCoursesOfMajor(major, semester);

            StringBuilder currentCourses = new StringBuilder();

            for (Course course : courses) {
                currentCourses.append(course.getId() + ",");
            }

            currentCourses.deleteCharAt(currentCourses.length() - 1);

            Connection.insertStudent(studentId, major, credit, "", semester, nameSurname, currentCourses.toString(), total_credits);
        }

        System.out.println("Student ID: " + studentId);
        System.out.println("Name: " + nameSurname);
        System.out.println("Credit: " + credit);
        System.out.println("Total Credit: " + total_credits);
        System.out.println("Major: " + major);
        System.out.println("Semester: " + semester);

        return "Post login success";
    }


    @PostMapping(value="/get-courses",
            consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces=MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> handleCourses(
            @RequestParam Map<String, String> params,
            @RequestParam(name="category[]", required=false) List<Integer> facultyIds,
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

    public static Set<String> extractCourses(String text) {
        Set<String> courses = new LinkedHashSet<>();

        Pattern p = Pattern.compile("\\b[A-Z]{2,5}\\s?\\d{3}\\b");
        Matcher m = p.matcher(text.toUpperCase());

        while (m.find()) {
            courses.add(m.group().replaceAll("\\s+", " "));
        }
        return courses;
    }
}