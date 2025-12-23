package olp.utils;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class PdfServiceImpl {

    public static byte[] createPdfFromBase64(String base64Image) throws IOException {
        String cleanBase64 = base64Image.replace("data:image/png;base64,", "");
        byte[] imageBytes = Base64.getDecoder().decode(cleanBase64);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, outputStream);

        document.open();

        Image image = Image.getInstance(imageBytes);
        image.scaleToFit(
                document.getPageSize().getWidth(),
                document.getPageSize().getHeight()
        );
        image.setAlignment(Image.ALIGN_CENTER);

        document.add(image);
        document.close();

        return outputStream.toByteArray();
    }
}