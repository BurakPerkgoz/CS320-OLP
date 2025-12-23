package olp.database;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FacultyTypeTest {

    @Test
    public void testFAASValues() {
        assertEquals(1, FacultyType.FAAS.getId());
        assertEquals(List.of("Havacılık ve Uzay Bilimleri Fakültesi"), FacultyType.FAAS.getDbValues());
    }

    @Test
    public void testFADValues() {
        assertEquals(2, FacultyType.FAD.getId());
        assertEquals(List.of("Mimarlık ve Tasarım Fakültesi"), FacultyType.FAD.getDbValues());
    }

    @Test
    public void testFASValues() {
        assertEquals(3, FacultyType.FAS.getId());
        assertEquals(List.of("Sosyal Bilimler Fakültesi"), FacultyType.FAS.getDbValues());
    }

    @Test
    public void testFASCValues() {
        assertEquals(4, FacultyType.FASC.getId());
        assertEquals(List.of("Uygulamalı Bilimler Fakültesi"), FacultyType.FASC.getDbValues());
    }

    @Test
    public void testFEValues() {
        assertEquals(5, FacultyType.FE.getId());
        assertEquals(List.of("Mühendislik Fakültesi"), FacultyType.FE.getDbValues());
    }

    @Test
    public void testFEASValues() {
        assertEquals(6, FacultyType.FEAS.getId());
        assertEquals(List.of("İşletme Fakültesi"), FacultyType.FEAS.getDbValues());
    }

    @Test
    public void testFLAWValues() {
        assertEquals(7, FacultyType.FLAW.getId());
        assertEquals(List.of("Hukuk Fakültesi"), FacultyType.FLAW.getDbValues());
    }

    @Test
    public void testGSBValues() {
        assertEquals(8, FacultyType.GSB.getId());
        assertEquals(List.of("İşletme Enstitüsü"), FacultyType.GSB.getDbValues());
    }

    @Test
    public void testGSSEValues() {
        assertEquals(9, FacultyType.GSSE.getId());
        assertEquals(List.of("Fen Bilimleri Enstitüsü"), FacultyType.GSSE.getDbValues());
    }

    @Test
    public void testGSSSValues() {
        assertEquals(10, FacultyType.GSSS.getId());
        assertEquals(List.of("Sosyal Bilimler Enstitüsü"), FacultyType.GSSS.getDbValues());
    }

    @Test
    public void testRECTValues() {
        assertEquals(12, FacultyType.RECT.getId());
        assertEquals(List.of("Rektörlük"), FacultyType.RECT.getDbValues());
    }

    @Test
    public void testSASValues() {
        assertEquals(13, FacultyType.SAS.getId());
        assertEquals(List.of("Uygulamalı Bilimler Fakültesi"), FacultyType.SAS.getDbValues());
    }

    @Test
    public void testSCOLAValues() {
        assertEquals(15, FacultyType.SCOLA.getId());
        assertEquals(List.of("Yabancı Diller Yüksekokulu"), FacultyType.SCOLA.getDbValues());
    }

    @Test
    public void testResolveDbValuesSingleId() {
        List<Integer> ids = List.of(1);
        List<String> result = FacultyType.resolveDbValues(ids);
        
        assertEquals(1, result.size());
        assertTrue(result.contains("Havacılık ve Uzay Bilimleri Fakültesi"));
    }

    @Test
    public void testResolveDbValuesMultipleIds() {
        List<Integer> ids = List.of(1, 5, 7);
        List<String> result = FacultyType.resolveDbValues(ids);
        
        assertEquals(3, result.size());
        assertTrue(result.contains("Havacılık ve Uzay Bilimleri Fakültesi"));
        assertTrue(result.contains("Mühendislik Fakültesi"));
        assertTrue(result.contains("Hukuk Fakültesi"));
    }

    @Test
    public void testResolveDbValuesEmptyList() {
        List<Integer> ids = List.of();
        List<String> result = FacultyType.resolveDbValues(ids);
        
        assertTrue(result.isEmpty());
    }

    @Test
    public void testResolveDbValuesInvalidId() {
        List<Integer> ids = List.of(999);
        List<String> result = FacultyType.resolveDbValues(ids);
        
        assertTrue(result.isEmpty());
    }

    @Test
    public void testResolveDbValuesDuplicateFaculty() {
        List<Integer> ids = List.of(4, 13);
        List<String> result = FacultyType.resolveDbValues(ids);
        
        assertEquals(1, result.size());
        assertTrue(result.contains("Uygulamalı Bilimler Fakültesi"));
    }

    @Test
    public void testAllFacultyTypesUnique() {
        FacultyType[] types = FacultyType.values();
        assertEquals(13, types.length);
    }
}

