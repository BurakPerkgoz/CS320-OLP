package olp.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {

    @Test
    public void testGettersAndSetters() {
        Course course = new Course();
        
        course.setId(100L);
        assertEquals(100L, course.getId());
        
        course.setTitle("Computer Science");
        assertEquals("Computer Science", course.getTitle());
        
        course.setDescription("Introduction to CS");
        assertEquals("Introduction to CS", course.getDescription());
        
        course.setSubject("CS");
        assertEquals("CS", course.getSubject());
        
        course.setCredits(3.0);
        assertEquals(3.0, course.getCredits());
        
        course.setCourseNo("101");
        assertEquals("101", course.getCourseNo());
        
        course.setSectionNo("01");
        assertEquals("01", course.getSectionNo());
        
        course.setInstructorFullName("John Doe");
        assertEquals("John Doe", course.getInstructorFullName());
        
        course.setPartOfTerm("Fall");
        assertEquals("Fall", course.getPartOfTerm());
        
        course.setCorequisite("CS 102");
        assertEquals("CS 102", course.getCorequisite());
        
        course.setPrerequisite("MATH 101");
        assertEquals("MATH 101", course.getPrerequisite());
        
        course.setFaculty("Engineering");
        assertEquals("Engineering", course.getFaculty());
    }

    @Test
    public void testSetScheduleForPrint() {
        Course course = new Course();
        
        course.setScheduleForPrint("Pazartesi | 09:00 - 10:3012:00 - 13:30Salı | 14:00 - 15:30");
        
        assertNotNull(course.getScheduleForPrint());
        assertTrue(course.getScheduleForPrint().contains(", "));
    }

    @Test
    public void testSetScheduleForPrintWithDigitFollowedByLetter() {
        Course course = new Course();
        
        course.setScheduleForPrint("Pazartesi9Salı");
        
        assertEquals("Pazartesi9, Salı", course.getScheduleForPrint());
    }

    @Test
    public void testSetScheduleForPrintEmpty() {
        Course course = new Course();
        
        course.setScheduleForPrint("");
        
        assertEquals("", course.getScheduleForPrint());
    }

    @Test
    public void testToString() {
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Test Course");
        course.setSubject("TST");
        course.setCourseNo("101");
        course.setCredits(3.0);
        
        String result = course.toString();
        
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("title='Test Course'"));
        assertTrue(result.contains("subject='TST'"));
        assertTrue(result.contains("courseNo='101'"));
        assertTrue(result.contains("credits=3.0"));
    }

    @Test
    public void testCourseWithNullValues() {
        Course course = new Course();
        
        assertNull(course.getTitle());
        assertNull(course.getDescription());
        assertNull(course.getSubject());
        assertEquals(0.0, course.getCredits());
    }
}

