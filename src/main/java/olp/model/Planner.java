package olp.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Planner implements Model {
    private final Map<String, Course> plannedCourses = new LinkedHashMap<>();

    public Planner() {
    }

    public Planner(List<Course> initialCourses) {
        if (initialCourses != null) {
            for (Course course : initialCourses) {
                plannedCourses.put(course.getId(), course);
            }
        }
    }

    public void addCourse(Course course) {
        if (course != null) {
            plannedCourses.put(course.getId(), course);
        }
    }

    public void removeCourse(String courseId) {
        plannedCourses.remove(courseId);
    }

    public Optional<Course> findCourse(String courseId) {
        return Optional.ofNullable(plannedCourses.get(courseId));
    }

    public List<Course> getCourses() {
        return new ArrayList<>(plannedCourses.values());
    }

    @Override
    public String render() {
        CourseTableModel tableModel = new CourseTableModel(getCourses());
        double totalCredits = plannedCourses.values().stream()
                .mapToDouble(Course::getCredits)
                .sum();

        return tableModel.render() + """
                <div class="planner-summary">
                    Total planned credits: %.1f
                </div>
                """.formatted(totalCredits);
    }
}

