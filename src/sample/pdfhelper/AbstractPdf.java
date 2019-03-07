package sample.pdfhelper;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileNotFoundException;

public abstract class AbstractPdf {


    void exportPDF(String route, String title, Table givenTable) throws FileNotFoundException {
        File file = new File(route);
        file.getParentFile().mkdirs();
        PdfDocument pdfDocument;


        pdfDocument = new PdfDocument(new PdfWriter(route));
        Document document = new Document(pdfDocument);

        Paragraph headerParagraph = new Paragraph(title);
        headerParagraph.setTextAlignment(TextAlignment.CENTER);
        headerParagraph.setBold();
        headerParagraph.setFontSize(24);
        document.add(headerParagraph);

        givenTable.setTextAlignment(TextAlignment.CENTER);
        givenTable.setHorizontalAlignment(HorizontalAlignment.CENTER);

        document.add(givenTable);
        document.close();


    }
}
