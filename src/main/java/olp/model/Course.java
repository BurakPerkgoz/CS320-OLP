package olp.model;

import java.util.Objects;

public class Course {
    private final String id;
    private final String subject;
    private final String courseNo;
    private final String sectionNo;
    private final String title;
    private final String instructor;
    private final double credits;
    private final String partOfTerm;

    public Course(
            String id,
            String subject,
            String courseNo,
            String sectionNo,
            String title,
            String instructor,
            double credits,
            String partOfTerm
    ) {
        this.id = id;
        this.subject = subject;
        this.courseNo = courseNo;
        this.sectionNo = sectionNo;
        this.title = title;
        this.instructor = instructor;
        this.credits = credits;
        this.partOfTerm = partOfTerm;
    }

    public String getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public String getSectionNo() {
        return sectionNo;
    }

    public String getTitle() {
        return title;
    }

    public String getInstructor() {
        return instructor;
    }

    public double getCredits() {
        return credits;
    }

    public String getPartOfTerm() {
        return partOfTerm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

