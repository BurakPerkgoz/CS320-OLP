package olp.controller;

import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    @Test
    public void testExtractCoursesSingleCourse() {
        Set<String> courses = Controller.extractCourses("CS 101");
        
        assertEquals(1, courses.size());
        assertTrue(courses.contains("CS 101"));
    }

    @Test
    public void testExtractCoursesMultipleCourses() {
        Set<String> courses = Controller.extractCourses("CS 101 and MATH 201");
        
        assertEquals(2, courses.size());
        assertTrue(courses.contains("CS 101"));
        assertTrue(courses.contains("MATH 201"));
    }

    @Test
    public void testExtractCoursesWithoutSpace() {
        Set<String> courses = Controller.extractCourses("CS101");
        
        assertEquals(1, courses.size());
        assertTrue(courses.contains("CS 101"));
    }

    @Test
    public void testExtractCoursesLowerCase() {
        Set<String> courses = Controller.extractCourses("cs 101");
        
        assertEquals(1, courses.size());
        assertTrue(courses.contains("CS 101"));
    }

    @Test
    public void testExtractCoursesEmptyString() {
        Set<String> courses = Controller.extractCourses("");
        
        assertTrue(courses.isEmpty());
    }

    @Test
    public void testExtractCoursesNoMatches() {
        Set<String> courses = Controller.extractCourses("This is just text without any course codes");
        
        assertTrue(courses.isEmpty());
    }

    @Test
    public void testExtractCoursesDifferentSubjects() {
        Set<String> courses = Controller.extractCourses("CS 101, MATH 201, PHYS 301");
        
        assertEquals(3, courses.size());
        assertTrue(courses.contains("CS 101"));
        assertTrue(courses.contains("MATH 201"));
        assertTrue(courses.contains("PHYS 301"));
    }

    @Test
    public void testExtractCoursesLongSubjectCode() {
        Set<String> courses = Controller.extractCourses("ENGR 101 and COMPE 202");
        
        assertEquals(2, courses.size());
        assertTrue(courses.contains("ENGR 101"));
        assertTrue(courses.contains("COMPE 202"));
    }

    @Test
    public void testExtractCoursesDuplicates() {
        Set<String> courses = Controller.extractCourses("CS 101 and CS 101");
        
        assertEquals(1, courses.size());
        assertTrue(courses.contains("CS 101"));
    }

    @Test
    public void testExtractCoursesInSentence() {
        Set<String> courses = Controller.extractCourses(
            "You need to take CS 101 before you can enroll in CS 201 or MATH 101"
        );
        
        assertEquals(3, courses.size());
        assertTrue(courses.contains("CS 101"));
        assertTrue(courses.contains("CS 201"));
        assertTrue(courses.contains("MATH 101"));
    }

    @Test
    public void testExtractCoursesWithSpecialCharacters() {
        Set<String> courses = Controller.extractCourses("Prerequisites: CS 101, MATH 201.");
        
        assertEquals(2, courses.size());
        assertTrue(courses.contains("CS 101"));
        assertTrue(courses.contains("MATH 201"));
    }

    @Test
    public void testExtractCoursesPreservesOrder() {
        Set<String> courses = Controller.extractCourses("CS 101, MATH 201, PHYS 301");
        
        assertEquals(3, courses.size());
    }

    @Test
    public void testExtractCoursesTwoLetterSubject() {
        Set<String> courses = Controller.extractCourses("CS 101");
        
        assertEquals(1, courses.size());
        assertTrue(courses.contains("CS 101"));
    }

    @Test
    public void testExtractCoursesFiveLetterSubject() {
        Set<String> courses = Controller.extractCourses("COMPE 101");
        
        assertEquals(1, courses.size());
        assertTrue(courses.contains("COMPE 101"));
    }

    @Test
    public void testExtractCoursesInvalidShortSubject() {
        Set<String> courses = Controller.extractCourses("A 101");
        
        assertTrue(courses.isEmpty());
    }

    @Test
    public void testExtractCoursesInvalidLongSubject() {
        Set<String> courses = Controller.extractCourses("ABCDEF 101");
        
        assertTrue(courses.isEmpty());
    }

    @Test
    public void testExtractCoursesThreeDigitCode() {
        Set<String> courses = Controller.extractCourses("CS 999");
        
        assertEquals(1, courses.size());
        assertTrue(courses.contains("CS 999"));
    }

    @Test
    public void testExtractCoursesInvalidTwoDigitCode() {
        Set<String> courses = Controller.extractCourses("CS 99");
        
        assertTrue(courses.isEmpty());
    }

    @Test
    public void testExtractCoursesInvalidFourDigitCode() {
        Set<String> courses = Controller.extractCourses("CS 1234");
        
        assertTrue(courses.isEmpty());
    }
}

