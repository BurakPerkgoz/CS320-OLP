package olp.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CourseTableModel implements Model {
    private final List<Course> courses;

    public CourseTableModel(List<Course> courses) {
        this.courses = courses == null ? List.of() : List.copyOf(courses);
    }

    public List<Course> getCourses() {
        return Collections.unmodifiableList(courses);
    }

    @Override
    public String render() {
        StringBuilder html = new StringBuilder();
        html.append("""
                <table class="course-table">
                    <thead>
                        <tr>
                            <th>Subject</th>
                            <th>Course</th>
                            <th>Section</th>
                            <th>Title</th>
                            <th>Faculty</th>
                            <th>Instructor</th>
                            <th>Credits</th>
                            <th>Term</th>
                            <th>Corequisite</th>
                            <th>Prerequisite</th>
                            <th>Schedule</th>
                        </tr>
                    </thead>
                    <tbody>
                """);

        for (Course course : courses) {
            html.append("""
                        <tr data-course-id="%s" data-description="%s">
                            <td>%s</td>
                            <td>%s</td>
                            <td>%s</td>
                            <td>%s</td>
                            <td>%s</td>
                            <td>%s</td>
                            <td>%.1f</td>
                            <td>%s</td>
                            <td>%s</td>
                            <td>%s</td>
                            <td>%s</td>
                        </tr>
                    """.formatted(
                    escape(course.getId()),
                    escape(course.getDescription()),
                    escape(course.getSubject()),
                    escape(course.getCourseNo()),
                    escape(course.getSectionNo()),
                    escape(course.getTitle()),
                    escape(course.getFaculty()),
                    escape(course.getInstructor()),
                    course.getCredits(),
                    escape(course.getPartOfTerm()),
                    escape(course.getCorequisite()),
                    escape(course.getPrerequisite()),
                    escape(course.getScheduleForPrint())
            ));
        }

        html.append("""
                    </tbody>
                </table>
                """);

        return html.toString();
    }

    protected String escape(String input) {
        String safe = Objects.requireNonNullElse(input, "");
        return safe
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}

