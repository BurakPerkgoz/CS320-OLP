package olp.database;

import java.util.List;

public enum FacultyType {

    FAAS(1, "FAAS", List.of("Havacılık ve Uzay Bilimleri Fakültesi")),
    FAD(2, "FAD", List.of("Mimarlık ve Tasarım Fakültesi")),
    FAS(3, "FAS", List.of("Sosyal Bilimler Fakültesi")),
    FASC(4, "FASC", List.of("Uygulamalı Bilimler Fakültesi")),
    FE(5, "FE", List.of("Mühendislik Fakültesi")),
    FEAS(6, "FEAS", List.of("İşletme Fakültesi")),
    FLAW(7, "FLAW", List.of("Hukuk Fakültesi")),
    GSB(8, "GSB", List.of("İşletme Enstitüsü")),
    GSSE(9, "GSSE", List.of("Fen Bilimleri Enstitüsü")),
    GSSS(10, "GSSS", List.of("Sosyal Bilimler Enstitüsü")),
    RECT(12, "RECT", List.of("Rektörlük")),
    SAS(13, "SAS", List.of("Uygulamalı Bilimler Fakültesi")),
    SCOLA(15, "SCOLA", List.of("Yabancı Diller Yüksekokulu"));

    private final int id;
    private final String code;
    private final List<String> dbValues;

    FacultyType(int id, String code, List<String> dbValues) {
        this.id = id;
        this.code = code;
        this.dbValues = dbValues;
    }

    public int getId() {
        return id;
    }

    public List<String> getDbValues() {
        return dbValues;
    }

    public static List<String> resolveDbValues(List<Integer> ids) {
        return ids.stream()
                .flatMap(id ->
                        java.util.Arrays.stream(values())
                                .filter(f -> f.id == id)
                                .flatMap(f -> f.dbValues.stream())
                )
                .distinct()
                .toList();
    }
}