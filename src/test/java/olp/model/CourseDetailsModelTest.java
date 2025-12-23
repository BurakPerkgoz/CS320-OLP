package olp.model;

import olp.controller.Course;
import olp.database.Connection;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class CourseDetailsModelTest {

    @Test
    public void testBuildHtmlWithValidCourse() throws SQLException {
        Course course = new Course();
        course.setId(1L);
        course.setSubject("CS");
        course.setCourseNo("101");
        course.setSectionNo("01");
        course.setTitle("Computer Science");
        course.setFaculty("Engineering");
        course.setCredits(3.0);
        course.setInstructorFullName("John Doe");
        course.setPartOfTerm("Fall");
        course.setCorequisite("CS 100");
        course.setPrerequisite("MATH 101");
        course.setDescription("Introduction to Computer Science");
        course.setScheduleForPrint("Pazartesi | 09:00 - 10:30");
        
        try (MockedStatic<Connection> mockedConnection = Mockito.mockStatic(Connection.class)) {
            mockedConnection.when(() -> Connection.getCourseByID(1L)).thenReturn(course);
            
            String html = CourseDetailsModel.buildHtml(1L);
            
            assertNotNull(html);
            assertTrue(html.contains("dtlms-course-detail-info"));
            assertTrue(html.contains("Course ID"));
            assertTrue(html.contains("Subject"));
            assertTrue(html.contains("CS"));
            assertTrue(html.contains("Computer Science"));
            assertTrue(html.contains("John Doe"));
            assertTrue(html.contains("Engineering"));
            assertTrue(html.contains("3.0"));
        }
    }

    @Test
    public void testBuildHtmlWithNullCourse() throws SQLException {
        try (MockedStatic<Connection> mockedConnection = Mockito.mockStatic(Connection.class)) {
            mockedConnection.when(() -> Connection.getCourseByID(999L)).thenReturn(null);
            
            String html = CourseDetailsModel.buildHtml(999L);
            
            assertNotNull(html);
            assertTrue(html.contains("dtlms-course-detail-info"));
            assertFalse(html.contains("Course ID"));
        }
    }

    @Test
    public void testBuildHtmlWithMinimalCourse() throws SQLException {
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Minimal Course");
        
        try (MockedStatic<Connection> mockedConnection = Mockito.mockStatic(Connection.class)) {
            mockedConnection.when(() -> Connection.getCourseByID(1L)).thenReturn(course);
            
            String html = CourseDetailsModel.buildHtml(1L);
            
            assertNotNull(html);
            assertTrue(html.contains("Minimal Course"));
        }
    }

    @Test
    public void testBuildHtmlEscapesSpecialCharacters() throws SQLException {
        Course course = new Course();
        course.setId(1L);
        course.setTitle("<script>alert('xss')</script>");
        course.setDescription("Test & <html>");
        
        try (MockedStatic<Connection> mockedConnection = Mockito.mockStatic(Connection.class)) {
            mockedConnection.when(() -> Connection.getCourseByID(1L)).thenReturn(course);
            
            String html = CourseDetailsModel.buildHtml(1L);
            
            assertNotNull(html);
            assertTrue(html.contains("&lt;script&gt;"));
            assertTrue(html.contains("&amp;"));
            assertFalse(html.contains("<script>"));
        }
    }

    @Test
    public void testBuildHtmlWithBlankValues() throws SQLException {
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Test");
        course.setSubject("");
        course.setCorequisite("   ");
        
        try (MockedStatic<Connection> mockedConnection = Mockito.mockStatic(Connection.class)) {
            mockedConnection.when(() -> Connection.getCourseByID(1L)).thenReturn(course);
            
            String html = CourseDetailsModel.buildHtml(1L);
            
            assertNotNull(html);
            assertTrue(html.contains("Test"));
        }
    }
}

