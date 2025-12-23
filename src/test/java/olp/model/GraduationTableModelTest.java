package olp.model;

import olp.controller.Course;
import olp.database.Connection;
import olp.utils.MajorMapper;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

public class GraduationTableModelTest {

    @Test
    public void testBuildHtmlWithCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        Course course = new Course();
        course.setId(1L);
        course.setSubject("CS");
        course.setCourseNo("101");
        course.setTitle("Computer Science");
        courses.add(course);
        
        try (MockedStatic<Connection> mockedConnection = Mockito.mockStatic(Connection.class);
             MockedStatic<MajorMapper> mockedMapper = Mockito.mockStatic(MajorMapper.class)) {
            
            mockedConnection.when(() -> Connection.getCoursesOfMajor(anyString(), anyString()))
                    .thenReturn(courses);
            mockedConnection.when(() -> Connection.getTakenCourses(anyString()))
                    .thenReturn("1");
            mockedConnection.when(() -> Connection.getCurrentCourses(anyString()))
                    .thenReturn("");
            mockedMapper.when(() -> MajorMapper.toDisplayName(anyString()))
                    .thenReturn("Bilgisayar Mühendisliği");
            
            String html = GraduationTableModel.buildHtml("12345", "bilgisayar");
            
            assertNotNull(html);
            assertTrue(html.contains("dtlms-tabs-horizontal-content"));
            assertTrue(html.contains("Computer Science"));
            assertTrue(html.contains("CS 101"));
        }
    }

    @Test
    public void testBuildHtmlWithNoCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        
        try (MockedStatic<Connection> mockedConnection = Mockito.mockStatic(Connection.class);
             MockedStatic<MajorMapper> mockedMapper = Mockito.mockStatic(MajorMapper.class)) {
            
            mockedConnection.when(() -> Connection.getCoursesOfMajor(anyString(), anyString()))
                    .thenReturn(courses);
            mockedConnection.when(() -> Connection.getTakenCourses(anyString()))
                    .thenReturn("");
            mockedConnection.when(() -> Connection.getCurrentCourses(anyString()))
                    .thenReturn("");
            mockedMapper.when(() -> MajorMapper.toDisplayName(anyString()))
                    .thenReturn("Bilgisayar Mühendisliği");
            
            String html = GraduationTableModel.buildHtml("12345", "bilgisayar");
            
            assertNotNull(html);
            assertTrue(html.contains("dtlms-tabs-horizontal-content"));
        }
    }

    @Test
    public void testBuildHtmlWithTakenCourse() throws SQLException {
        List<Course> courses = new ArrayList<>();
        Course course = new Course();
        course.setId(1L);
        course.setSubject("CS");
        course.setCourseNo("101");
        course.setTitle("Computer Science");
        courses.add(course);
        
        try (MockedStatic<Connection> mockedConnection = Mockito.mockStatic(Connection.class);
             MockedStatic<MajorMapper> mockedMapper = Mockito.mockStatic(MajorMapper.class)) {
            
            mockedConnection.when(() -> Connection.getCoursesOfMajor(anyString(), anyString()))
                    .thenReturn(courses);
            mockedConnection.when(() -> Connection.getTakenCourses("12345"))
                    .thenReturn("1,2,3");
            mockedConnection.when(() -> Connection.getCurrentCourses("12345"))
                    .thenReturn("");
            mockedMapper.when(() -> MajorMapper.toDisplayName(anyString()))
                    .thenReturn("Bilgisayar Mühendisliği");
            
            String html = GraduationTableModel.buildHtml("12345", "bilgisayar");
            
            assertNotNull(html);
            assertTrue(html.contains("currentColor"));
            assertTrue(html.contains("Drop this course"));
        }
    }

    @Test
    public void testBuildHtmlWithCurrentCourse() throws SQLException {
        List<Course> courses = new ArrayList<>();
        Course course = new Course();
        course.setId(1L);
        course.setSubject("CS");
        course.setCourseNo("101");
        course.setTitle("Computer Science");
        courses.add(course);
        
        try (MockedStatic<Connection> mockedConnection = Mockito.mockStatic(Connection.class);
             MockedStatic<MajorMapper> mockedMapper = Mockito.mockStatic(MajorMapper.class)) {
            
            mockedConnection.when(() -> Connection.getCoursesOfMajor(anyString(), anyString()))
                    .thenReturn(courses);
            mockedConnection.when(() -> Connection.getTakenCourses("12345"))
                    .thenReturn("");
            mockedConnection.when(() -> Connection.getCurrentCourses("12345"))
                    .thenReturn("1,2,3");
            mockedMapper.when(() -> MajorMapper.toDisplayName(anyString()))
                    .thenReturn("Bilgisayar Mühendisliği");
            
            String html = GraduationTableModel.buildHtml("12345", "bilgisayar");
            
            assertNotNull(html);
            assertTrue(html.contains("currentColor"));
            assertTrue(html.contains("Drop this course"));
        }
    }

    @Test
    public void testBuildHtmlWithUntakenCourse() throws SQLException {
        List<Course> courses = new ArrayList<>();
        Course course = new Course();
        course.setId(5L);
        course.setSubject("CS");
        course.setCourseNo("201");
        course.setTitle("Advanced CS");
        courses.add(course);
        
        try (MockedStatic<Connection> mockedConnection = Mockito.mockStatic(Connection.class);
             MockedStatic<MajorMapper> mockedMapper = Mockito.mockStatic(MajorMapper.class)) {
            
            mockedConnection.when(() -> Connection.getCoursesOfMajor(anyString(), anyString()))
                    .thenReturn(courses);
            mockedConnection.when(() -> Connection.getTakenCourses("12345"))
                    .thenReturn("1,2,3");
            mockedConnection.when(() -> Connection.getCurrentCourses("12345"))
                    .thenReturn("");
            mockedMapper.when(() -> MajorMapper.toDisplayName(anyString()))
                    .thenReturn("Bilgisayar Mühendisliği");
            
            String html = GraduationTableModel.buildHtml("12345", "bilgisayar");
            
            assertNotNull(html);
            assertTrue(html.contains("Take this course"));
        }
    }

    @Test
    public void testBuildHtmlAllSemesters() throws SQLException {
        List<Course> courses = new ArrayList<>();
        
        try (MockedStatic<Connection> mockedConnection = Mockito.mockStatic(Connection.class);
             MockedStatic<MajorMapper> mockedMapper = Mockito.mockStatic(MajorMapper.class)) {
            
            mockedConnection.when(() -> Connection.getCoursesOfMajor(anyString(), anyString()))
                    .thenReturn(courses);
            mockedConnection.when(() -> Connection.getTakenCourses(anyString()))
                    .thenReturn("");
            mockedConnection.when(() -> Connection.getCurrentCourses(anyString()))
                    .thenReturn("");
            mockedMapper.when(() -> MajorMapper.toDisplayName(anyString()))
                    .thenReturn("Test Major");
            
            String html = GraduationTableModel.buildHtml("12345", "test");
            
            assertNotNull(html);
            assertTrue(html.contains("1. Yıl - Güz"));
            assertTrue(html.contains("1. Yıl - Bahar"));
            assertTrue(html.contains("2. Yıl - Güz"));
            assertTrue(html.contains("2. Yıl - Bahar"));
            assertTrue(html.contains("3. Yıl - Güz"));
            assertTrue(html.contains("3. Yıl - Bahar"));
            assertTrue(html.contains("4. Yıl - Güz"));
            assertTrue(html.contains("4. Yıl - Bahar"));
        }
    }
}

