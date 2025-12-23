package olp.model;

import olp.controller.Course;
import olp.database.Connection;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraduationTableModel {

    public static String buildHtml(String student_id, String major) throws SQLException {

        StringBuilder result = new StringBuilder();

        for (int i = 1; i < 9; ++i) {
            StringBuilder html = new StringBuilder("""
            <div class="dtlms-tabs-horizontal-content">
            <div class="dtlms-title">%s - %s</div>
            <div class="dtlms-course-curriculum-toggle-group-holder">
                <div class="dtlms-toggle-group-set">
                    <div class="dtlms-toggle-content">
                        <div class="block">
                            <ul class="dtlms-curriculum-list">""");



            List<Course> courses = Connection.getCoursesOfMajor(major, String.valueOf(i));

            for (Course course : courses) {
                boolean isTook = Connection.getTakenCourses(student_id).contains(String.valueOf(course.getId()));  // Assuming you have a method that tells if the course has been "took"

                String checkboxFill = isTook ? "currentColor" : "none";
                String checkboxStroke = isTook ? "none" : "currentColor";
                String courseTakeDrop = isTook ? "Drop this course" : "Take this course";

                String singleCourse = """
                <li class="preview-item">
                    <div class="dtlms-curriculum-meta-icon"><span class="fas fa-book"></span></div>
                    <div class="dtlms-curriculum-meta-title"><a href="#" onclick="getCourseDetails('%s')">%s %s - %s</a></div>
                    <div class="dtlms-curriculum-meta-items">
                        <div class="yith-wcwl-add-button">
                            <a onclick="toggleCourse('%s', this)" class="add_to_wishlist single_add_to_wishlist" rel="nofollow">
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24" width="24" height="24">
                                    <rect id="checkbox" x="3" y="3" width="18" height="18" rx="3" stroke="currentColor" stroke-width="2" fill="%s" stroke="%s"/>
                                </svg>
                                <span>%s</span>
                            </a>
                        </div>
                    </div>
                </li>""".formatted(course.getId(), course.getSubject(), course.getCourseNo(), course.getTitle(), course.getId(), checkboxFill, checkboxStroke, courseTakeDrop);

                html.append(singleCourse);
            }

            html.append("""
                </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div id="dtlms-ajax-load-image" style="display:none;">
                                <div class="dtlms-loader-inner">
                                    <div class="dtlms-loading"></div>
                                    <div class="dtlms-pad">
                                        <div class="dtlms-line dtlms-line1"></div>
                                        <div class="dtlms-line dtlms-line2"></div>
                                        <div class="dtlms-line dtlms-line3"></div>
                                    </div>
                                </div>
                            </div>
                        </div>""");

            result.append(html.toString().formatted("Bilgisayar Mühendisliği", mapQuarterToSemester(i)));
        }

        return result.toString();
    }

    private static String mapQuarterToSemester(int quarter) {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("Q1", "1. Yıl - Güz");
        mapping.put("Q2", "1. Yıl - Bahar");
        mapping.put("Q3", "2. Yıl - Güz");
        mapping.put("Q4", "2. Yıl - Bahar");
        mapping.put("Q5", "3. Yıl - Güz");
        mapping.put("Q6", "3. Yıl - Bahar");
        mapping.put("Q7", "4. Yıl - Güz");
        mapping.put("Q8", "4. Yıl - Bahar");
        return mapping.get("Q"+quarter);
    }
}

