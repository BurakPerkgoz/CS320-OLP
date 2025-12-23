package olp.model;

import olp.controller.Course;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CourseModelTest {

    @Test
    public void testBuildCoursesHtmlEmpty() {
        List<Course> courses = new ArrayList<>();
        
        String html = CourseModel.buildCoursesHtml(courses, 0, 12, 0);
        
        assertNotNull(html);
        assertTrue(html.contains("dtlms-courses-listing-items"));
        assertFalse(html.contains("dtlms-pagination"));
    }

    @Test
    public void testBuildCoursesHtmlWithCourses() {
        List<Course> courses = new ArrayList<>();
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Test Course");
        course.setCourseNo("101");
        course.setSectionNo("01");
        course.setSubject("TST");
        course.setFaculty("Engineering");
        course.setPartOfTerm("Fall");
        course.setInstructorFullName("John Doe");
        course.setPrerequisite("None");
        course.setDescription("Test Description");
        course.setCredits(3.0);
        course.setScheduleForPrint("Pazartesi | 09:00 - 10:30");
        courses.add(course);
        
        String html = CourseModel.buildCoursesHtml(courses, 0, 12, 1);
        
        assertNotNull(html);
        assertTrue(html.contains("Test Course"));
        assertTrue(html.contains("101"));
        assertTrue(html.contains("TST"));
        assertTrue(html.contains("John Doe"));
        assertFalse(html.contains("dtlms-pagination"));
    }

    @Test
    public void testBuildCoursesHtmlWithPagination() {
        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Course course = new Course();
            course.setId(i);
            course.setTitle("Course " + i);
            course.setCourseNo(String.valueOf(100 + i));
            course.setSectionNo("01");
            course.setSubject("TST");
            course.setFaculty("Engineering");
            course.setPartOfTerm("Fall");
            course.setInstructorFullName("Instructor " + i);
            course.setPrerequisite("None");
            course.setDescription("Description " + i);
            course.setCredits(3.0);
            course.setScheduleForPrint("Pazartesi | 09:00 - 10:30");
            courses.add(course);
        }
        
        String html = CourseModel.buildCoursesHtml(courses, 0, 12, 25);
        
        assertNotNull(html);
        assertTrue(html.contains("dtlms-pagination"));
        assertTrue(html.contains("page-numbers"));
    }

    @Test
    public void testBuildCoursesHtmlWithFreeCourse() {
        List<Course> courses = new ArrayList<>();
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Free Course");
        course.setCourseNo("101");
        course.setSectionNo("01");
        course.setSubject("TST");
        course.setFaculty("Engineering");
        course.setPartOfTerm("Fall");
        course.setInstructorFullName("John Doe");
        course.setDescription("Test Description");
        course.setCredits(0.0);
        course.setScheduleForPrint("Pazartesi | 09:00 - 10:30");
        courses.add(course);
        
        String html = CourseModel.buildCoursesHtml(courses, 0, 12, 1);
        
        assertNotNull(html);
        assertTrue(html.contains("dtlms-free"));
        assertTrue(html.contains("Login To Take Course"));
    }

    @Test
    public void testBuildCoursesHtmlWithPaidCourse() {
        List<Course> courses = new ArrayList<>();
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Paid Course");
        course.setCourseNo("101");
        course.setSectionNo("01");
        course.setSubject("TST");
        course.setFaculty("Engineering");
        course.setPartOfTerm("Fall");
        course.setInstructorFullName("John Doe");
        course.setDescription("Test Description");
        course.setCredits(5.0);
        course.setScheduleForPrint("Pazartesi | 09:00 - 10:30");
        courses.add(course);
        
        String html = CourseModel.buildCoursesHtml(courses, 0, 12, 1);
        
        assertNotNull(html);
        assertTrue(html.contains("dtlms-cost"));
        assertTrue(html.contains("Add to Cart"));
    }

    @Test
    public void testBuildCoursesHtmlWithCorequisite() {
        List<Course> courses = new ArrayList<>();
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Test Course");
        course.setCourseNo("101");
        course.setSectionNo("01");
        course.setSubject("TST");
        course.setFaculty("Engineering");
        course.setPartOfTerm("Fall");
        course.setInstructorFullName("John Doe");
        course.setDescription("Test Description");
        course.setPrerequisite("TST 100");
        course.setCorequisite("TST 102");
        course.setCredits(3.0);
        course.setScheduleForPrint("Pazartesi | 09:00 - 10:30");
        courses.add(course);
        
        String html = CourseModel.buildCoursesHtml(courses, 0, 12, 1);
        
        assertNotNull(html);
        assertTrue(html.contains("Prerequisite"));
        assertTrue(html.contains("Corequisite"));
    }

    @Test
    public void testBuildCoursesHtmlSecondPage() {
        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Course course = new Course();
            course.setId(i);
            course.setTitle("Course " + i);
            course.setCourseNo(String.valueOf(100 + i));
            course.setSectionNo("01");
            course.setSubject("TST");
            course.setFaculty("Engineering");
            course.setPartOfTerm("Fall");
            course.setInstructorFullName("Instructor " + i);
            course.setDescription("Description " + i);
            course.setCredits(3.0);
            course.setScheduleForPrint("Pazartesi | 09:00 - 10:30");
            courses.add(course);
        }
        
        String html = CourseModel.buildCoursesHtml(courses, 12, 12, 50);
        
        assertNotNull(html);
        assertTrue(html.contains("dtlms-pagination"));
        assertTrue(html.contains("Prev"));
        assertTrue(html.contains("Next"));
    }
}

