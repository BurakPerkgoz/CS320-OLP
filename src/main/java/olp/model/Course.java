package olp.model;

import java.util.Objects;

public class Course {
    private final String id;
    private final String subject;
    private final String courseNo;
    private final String sectionNo;
    private final String title;
    private final String faculty;
    private final String instructor;
    private final double credits;
    private final String partOfTerm;
    private final String corequisite;
    private final String prerequisite;
    private final String description;
    private final String scheduleForPrint;

    public Course(
            String id,
            String subject,
            String courseNo,
            String sectionNo,
            String title,
            String faculty,
            String instructor,
            double credits,
            String partOfTerm,
            String corequisite,
            String prerequisite,
            String description,
            String scheduleForPrint
    ) {
        this.id = id;
        this.subject = subject;
        this.courseNo = courseNo;
        this.sectionNo = sectionNo;
        this.title = title;
        this.faculty = faculty;
        this.instructor = instructor;
        this.credits = credits;
        this.partOfTerm = partOfTerm;
        this.corequisite = corequisite;
        this.prerequisite = prerequisite;
        this.description = description;
        this.scheduleForPrint = scheduleForPrint;
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

    public String getFaculty() {
        return faculty;
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

    public String getCorequisite() {
        return corequisite;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public String getDescription() {
        return description;
    }

    public String getScheduleForPrint() {
        return scheduleForPrint;
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

