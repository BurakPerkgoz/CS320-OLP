package olp.controller;

public class Course {

    private long id;
    private String title;
    private String description;
    private String subject;
    private double credits;
    private String courseNo;
    private String sectionNo;
    private String instructorFullName;
    private String partOfTerm;
    private String corequisite;
    private String prerequisite;
    private String scheduleForPrint;
    private String faculty;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public double getCredits() { return credits; }
    public void setCredits(double credits) { this.credits = credits; }

    public String getCourseNo() { return courseNo; }
    public void setCourseNo(String courseNo) { this.courseNo = courseNo; }

    public String getSectionNo() { return sectionNo; }
    public void setSectionNo(String sectionNo) { this.sectionNo = sectionNo; }

    public String getInstructorFullName() { return instructorFullName; }
    public void setInstructorFullName(String instructorFullName) {
        this.instructorFullName = instructorFullName;
    }

    public String getPartOfTerm() { return partOfTerm; }
    public void setPartOfTerm(String partOfTerm) { this.partOfTerm = partOfTerm; }

    public String getCorequisite() { return corequisite; }
    public void setCorequisite(String corequisite) { this.corequisite = corequisite; }

    public String getPrerequisite() { return prerequisite; }
    public void setPrerequisite(String prerequisite) { this.prerequisite = prerequisite; }

    public String getScheduleForPrint() { return scheduleForPrint; }
    public void setScheduleForPrint(String scheduleForPrint) {
        StringBuilder formatted = new StringBuilder();
        char lastChar = 0;

        for (char c : scheduleForPrint.toCharArray()) {
            if (lastChar != 0 && Character.isAlphabetic(c) && Character.isDigit(lastChar)) {
                formatted.append(lastChar).append(", ");
            } else if (lastChar != 0) {
                formatted.append(lastChar);
            }
            lastChar = c;
        }

        if (lastChar != 0) {
            formatted.append(lastChar);
        }
        this.scheduleForPrint = formatted.toString();
    }

    public String getFaculty() { return faculty; }
    public void setFaculty(String faculty) { this.faculty = faculty; }
}