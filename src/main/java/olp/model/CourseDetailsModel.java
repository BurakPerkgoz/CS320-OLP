package olp.model;

import olp.controller.Course;
import olp.database.Connection;

import java.sql.SQLException;

public class CourseDetailsModel {

    public static String buildHtml(long id) throws SQLException {
        Course course = Connection.getCourseByID(id);

        StringBuilder html = new StringBuilder();

        html.append("<ul class=\"dtlms-course-detail-info\">");

        if (course != null) {
            append(html, "Course ID", course.getId());
            append(html, "Subject", course.getSubject());
            append(html, "Course No", course.getCourseNo());
            append(html, "Section", course.getSectionNo());
            append(html, "Title", course.getTitle());
            append(html, "Faculty", course.getFaculty());
            append(html, "Credits", course.getCredits());
            append(html, "Instructor", course.getInstructorFullName());
            append(html, "Part of Term", course.getPartOfTerm());
            append(html, "Corequisite", course.getCorequisite());
            append(html, "Prerequisite", course.getPrerequisite());

            html.append("<li>")
                    .append("<span class=\"info-description\"></span>")
                    .append("<label>Description:</label>")
                    .append("<span>")
                    .append(escape(course.getDescription()))
                    .append("</span>")
                    .append("</li>");

            html.append("<li>")
                    .append("<span class=\"info-schedule\"></span>")
                    .append("<label>Schedule:</label>")
                    .append("<pre>")
                    .append(escape(course.getScheduleForPrint()))
                    .append("</pre>")
                    .append("</li>");
        }

        html.append("</ul>");

        return html.toString();
    }

    private static void append(StringBuilder html, String label, Object value) {
        if (value == null || value.toString().isBlank()) return;

        html.append("<li>")
                .append("<span></span>")
                .append("<label>").append(label).append(":</label>")
                .append("<span>")
                .append(escape(value.toString()))
                .append("</span>")
                .append("</li>");
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
