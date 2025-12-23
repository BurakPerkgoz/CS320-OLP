package olp.utils;

import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class PrerequisiteParserTest {

    @Test
    public void testNormalizeSingleCourse() {
        String result = PrerequisiteParser.normalize("CS 101");
        assertEquals("CS 101", result);
    }

    @Test
    public void testNormalizeRemovesCredits() {
        String result = PrerequisiteParser.normalize("60 AKTS");
        assertFalse(result.contains("AKTS"));
    }

    @Test
    public void testNormalizeRemovesECTS() {
        String result = PrerequisiteParser.normalize("30 ECTS");
        assertFalse(result.contains("ECTS"));
    }

    @Test
    public void testNormalizeRemovesGNO() {
        String result = PrerequisiteParser.normalize("Min. 2.5 GNO");
        assertFalse(result.contains("GNO"));
    }

    @Test
    public void testNormalizeConvertsVEYAtoOR() {
        String result = PrerequisiteParser.normalize("CS 101 VEYA CS 102");
        assertTrue(result.contains("OR"));
        assertFalse(result.contains("VEYA"));
    }

    @Test
    public void testNormalizeConvertsSlashToOR() {
        String result = PrerequisiteParser.normalize("CS 101 / CS 102");
        assertTrue(result.contains("OR"));
        assertFalse(result.contains("/"));
    }

    @Test
    public void testNormalizeConvertsVEtoAND() {
        String result = PrerequisiteParser.normalize("CS 101 VE CS 102");
        assertTrue(result.contains("AND"));
        assertFalse(result.contains("VE"));
    }

    @Test
    public void testNormalizeConvertsCommaToAND() {
        String result = PrerequisiteParser.normalize("CS 101, CS 102");
        assertTrue(result.contains("AND"));
    }

    @Test
    public void testNormalizeConvertsAmpersandToAND() {
        String result = PrerequisiteParser.normalize("CS 101 & CS 102");
        assertTrue(result.contains("AND"));
        assertFalse(result.contains("&"));
    }

    @Test
    public void testParseSingleCourse() {
        PrerequisiteParser.Expr expr = PrerequisiteParser.parsePrerequisite("CS 101");
        assertNotNull(expr);
        assertTrue(expr instanceof PrerequisiteParser.CourseExpr);
        assertEquals("CS 101", ((PrerequisiteParser.CourseExpr) expr).course);
    }

    @Test
    public void testParseAndExpression() {
        PrerequisiteParser.Expr expr = PrerequisiteParser.parsePrerequisite("CS 101 AND CS 102");
        assertNotNull(expr);
        assertTrue(expr instanceof PrerequisiteParser.AndExpr);
    }

    @Test
    public void testParseOrExpression() {
        PrerequisiteParser.Expr expr = PrerequisiteParser.parsePrerequisite("CS 101 OR CS 102");
        assertNotNull(expr);
        assertTrue(expr instanceof PrerequisiteParser.OrExpr);
    }

    @Test
    public void testParseWithParentheses() {
        PrerequisiteParser.Expr expr = PrerequisiteParser.parsePrerequisite("(CS 101 OR CS 102) AND CS 103");
        assertNotNull(expr);
        assertTrue(expr instanceof PrerequisiteParser.AndExpr);
    }

    @Test
    public void testSatisfiesSingleCourse() {
        PrerequisiteParser.Expr expr = PrerequisiteParser.parsePrerequisite("CS 101");
        Set<String> completed = new HashSet<>();
        completed.add("CS 101");
        
        assertTrue(PrerequisiteParser.satisfies(expr, completed));
    }

    @Test
    public void testNotSatisfiesSingleCourse() {
        PrerequisiteParser.Expr expr = PrerequisiteParser.parsePrerequisite("CS 101");
        Set<String> completed = new HashSet<>();
        
        assertFalse(PrerequisiteParser.satisfies(expr, completed));
    }

    @Test
    public void testSatisfiesAndExpression() {
        PrerequisiteParser.Expr expr = PrerequisiteParser.parsePrerequisite("CS 101 AND CS 102");
        Set<String> completed = new HashSet<>();
        completed.add("CS 101");
        completed.add("CS 102");
        
        assertTrue(PrerequisiteParser.satisfies(expr, completed));
    }

    @Test
    public void testNotSatisfiesAndExpression() {
        PrerequisiteParser.Expr expr = PrerequisiteParser.parsePrerequisite("CS 101 AND CS 102");
        Set<String> completed = new HashSet<>();
        completed.add("CS 101");
        
        assertFalse(PrerequisiteParser.satisfies(expr, completed));
    }

    @Test
    public void testSatisfiesOrExpression() {
        PrerequisiteParser.Expr expr = PrerequisiteParser.parsePrerequisite("CS 101 OR CS 102");
        Set<String> completed = new HashSet<>();
        completed.add("CS 101");
        
        assertTrue(PrerequisiteParser.satisfies(expr, completed));
    }

    @Test
    public void testNotSatisfiesOrExpression() {
        PrerequisiteParser.Expr expr = PrerequisiteParser.parsePrerequisite("CS 101 OR CS 102");
        Set<String> completed = new HashSet<>();
        
        assertFalse(PrerequisiteParser.satisfies(expr, completed));
    }

    @Test
    public void testComplexExpression() {
        PrerequisiteParser.Expr expr = PrerequisiteParser.parsePrerequisite("(CS 101 OR CS 102) AND (MATH 101 OR MATH 102)");
        Set<String> completed = new HashSet<>();
        completed.add("CS 101");
        completed.add("MATH 102");
        
        assertTrue(PrerequisiteParser.satisfies(expr, completed));
    }

    @Test
    public void testComplexExpressionNotSatisfied() {
        PrerequisiteParser.Expr expr = PrerequisiteParser.parsePrerequisite("(CS 101 OR CS 102) AND (MATH 101 OR MATH 102)");
        Set<String> completed = new HashSet<>();
        completed.add("CS 101");
        
        assertFalse(PrerequisiteParser.satisfies(expr, completed));
    }

    @Test
    public void testCaseInsensitiveParsing() {
        PrerequisiteParser.Expr expr = PrerequisiteParser.parsePrerequisite("cs 101");
        assertNotNull(expr);
        assertTrue(expr instanceof PrerequisiteParser.CourseExpr);
        assertEquals("CS 101", ((PrerequisiteParser.CourseExpr) expr).course);
    }

    @Test
    public void testMultipleCourses() {
        PrerequisiteParser.Expr expr = PrerequisiteParser.parsePrerequisite("CS 101 AND CS 102 AND CS 103");
        Set<String> completed = new HashSet<>();
        completed.add("CS 101");
        completed.add("CS 102");
        completed.add("CS 103");
        
        assertTrue(PrerequisiteParser.satisfies(expr, completed));
    }

    @Test
    public void testCourseCodeWithSpace() {
        PrerequisiteParser.Expr expr = PrerequisiteParser.parsePrerequisite("CS 101");
        assertNotNull(expr);
        assertEquals("CS 101", ((PrerequisiteParser.CourseExpr) expr).course);
    }

    @Test
    public void testCourseCodeWithoutSpace() {
        PrerequisiteParser.Expr expr = PrerequisiteParser.parsePrerequisite("CS101");
        assertNotNull(expr);
        assertEquals("CS 101", ((PrerequisiteParser.CourseExpr) expr).course);
    }
}

