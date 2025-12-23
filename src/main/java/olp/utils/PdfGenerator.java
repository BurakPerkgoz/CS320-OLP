package olp.utils;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;
import olp.controller.Course;
import olp.database.Connection;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class PdfGenerator {

    public static void generateCurriculumPdf(
            String major,
            String studentId,
            OutputStream outputStream
    ) throws Exception {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        Font semesterFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font courseFont = new Font(Font.HELVETICA, 11);
        Font checkboxFont = new Font(Font.HELVETICA, 12, Font.BOLD);

        for (int quarter = 1; quarter <= 8; quarter++) {

            Paragraph title = new Paragraph(
                    MajorMapper.toDisplayName(major) + " - " + mapQuarterToSemester(quarter),
                    semesterFont
            );
            title.setSpacingBefore(20);
            title.setSpacingAfter(10);
            document.add(title);

            List<Course> courses =
                    Connection.getCoursesOfMajor(major, String.valueOf(quarter));


            for (Course course : courses) {
                boolean isTaken = Connection.getTakenCourses(studentId).contains(String.valueOf(course.getId()));

                String checkbox = isTaken ? "✔" : "☐";

                Paragraph courseLine = new Paragraph();
                courseLine.setSpacingAfter(6);

                courseLine.add(new Chunk(checkbox + "  ", checkboxFont));
                courseLine.add(new Chunk(
                        course.getSubject() + " " +
                                course.getCourseNo() + " - " +
                                course.getTitle(),
                        courseFont
                ));

                document.add(courseLine);
            }
        }

        document.close();
    }

    private static String mapQuarterToSemester(int quarter) {
        return switch (quarter) {
            case 1 -> "1. Yıl - Güz";
            case 2 -> "1. Yıl - Bahar";
            case 3 -> "2. Yıl - Güz";
            case 4 -> "2. Yıl - Bahar";
            case 5 -> "3. Yıl - Güz";
            case 6 -> "3. Yıl - Bahar";
            case 7 -> "4. Yıl - Güz";
            case 8 -> "4. Yıl - Bahar";
            default -> "";
        };
    }
}