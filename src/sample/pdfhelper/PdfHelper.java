package sample.pdfhelper;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.test.annotations.WrapToTest;
import sample.domain.DTOS.HomeworkAverageDTO;
import sample.domain.DTOS.PassedStudentsDTO;
import sample.domain.DTOS.StudentAverageDTO;
import sample.domain.DTOS.TeacherAverageDTO;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@WrapToTest
public class PdfHelper extends AbstractPdf {

    DecimalFormat df2 = new DecimalFormat(".##");
    public PdfHelper() {
    }

    public void writePassedNotPassedStudents(List<PassedStudentsDTO> passedStudentsDTOS) throws FileNotFoundException {
        Table table = new Table(4);
        table.setWidth(500).setMarginBottom(10);
        table.addHeaderCell(new Cell().setBold().add(new Paragraph("Student Name").setFontSize(15)));
        table.addHeaderCell(new Cell().setBold().add(new Paragraph("Teacher Name").setFontSize(15)));
        table.addHeaderCell(new Cell().setBold().add(new Paragraph("Grade").setFontSize(15)));
        table.addHeaderCell(new Cell().setBold().add(new Paragraph("Passed").setFontSize(15)));

        passedStudentsDTOS.forEach(x -> {
                    table.addCell(new Cell().add(new Paragraph(x.getStudentName())));
                    table.addCell(new Cell().add(new Paragraph(x.getTeacherName())));
                    table.addCell(new Cell().add(new Paragraph(df2.format(x.getGrade()))));
                    table.addCell(new Cell().add(new Paragraph(x.getPassed() ? "Passed" : "Failed")));
                }
        );

        super.exportPDF("./PassedStudents.pdf", "Passed/Not passed Students", table);

    }

    public void writeTopTeachersToPDF(List<TeacherAverageDTO> passedStudentsDTOS) throws FileNotFoundException {
        Table table = new Table(3);
        table.setWidth(500).setMarginBottom(10);
        table.addHeaderCell(new Cell().setBold().add(new Paragraph("Position").setFontSize(15)));
        table.addHeaderCell(new Cell().setBold().add(new Paragraph("Teacher Name").setFontSize(15)));
        table.addHeaderCell(new Cell().setBold().add(new Paragraph("Grade").setFontSize(15)));

        AtomicInteger position = new AtomicInteger(1);

        passedStudentsDTOS.forEach(x -> {
                    table.addCell(new Cell().add(new Paragraph(position.toString())));
                    position.getAndIncrement();
                    table.addCell(new Cell().add(new Paragraph(x.getTeacherName())));
                    table.addCell(new Cell().add(new Paragraph(df2.format(x.getGrade()))));

                }
        );

        super.exportPDF("./TopTeachers.pdf", "Top Teachers", table);


    }

    public void writeTop30StudentsToPDF(List<StudentAverageDTO> passedStudentsDTOS) throws FileNotFoundException {
        Table table = new Table(4);
        table.setWidth(500).setMarginBottom(10);
        table.addHeaderCell(new Cell().setBold().add(new Paragraph("Position").setFontSize(15)));
        table.addHeaderCell(new Cell().setBold().add(new Paragraph("Student Name").setFontSize(15)));
        table.addHeaderCell(new Cell().setBold().add(new Paragraph("Teacher Name").setFontSize(15)));
        table.addHeaderCell(new Cell().setBold().add(new Paragraph("Grade").setFontSize(15)));

        AtomicInteger position = new AtomicInteger(1);

        passedStudentsDTOS.forEach(x -> {
                    table.addCell(new Cell().add(new Paragraph(position.toString())));
                    position.getAndIncrement();
                    table.addCell(new Cell().add(new Paragraph(x.getStudentName())));
                    table.addCell(new Cell().add(new Paragraph(x.getTeachersName())));
                    table.addCell(new Cell().add(new Paragraph(df2.format(x.getFinalGrade()))));

                }
        );
        super.exportPDF("./Top30Students.pdf", "Top 30 Students", table);
    }

    public void writeTopHardestHomeworks(List<HomeworkAverageDTO> passedStudentsDTOS) throws FileNotFoundException {
        Table table = new Table(5);
        table.setWidth(500).setMarginBottom(10);
        table.addHeaderCell(new Cell().setBold().add(new Paragraph("Position").setFontSize(15)));
        table.addHeaderCell(new Cell().setBold().add(new Paragraph("Homework number").setFontSize(15)));
        table.addHeaderCell(new Cell().setBold().add(new Paragraph("Received Week").setFontSize(15)));
        table.addHeaderCell(new Cell().setBold().add(new Paragraph("Deadline Week").setFontSize(15)));
        table.addHeaderCell(new Cell().setBold().add(new Paragraph("Average").setFontSize(15)));

        AtomicInteger position = new AtomicInteger(1);

        passedStudentsDTOS.forEach(x -> {
                    table.addCell(new Cell().add(new Paragraph(position.toString())));
                    position.getAndIncrement();
                    table.addCell(new Cell().add(new Paragraph(x.getNumber().toString())));
                    table.addCell(new Cell().add(new Paragraph(x.getReceivedWeek().toString())));
                    table.addCell(new Cell().add(new Paragraph(x.getDeadlineWeek().toString())));
                    table.addCell(new Cell().add(new Paragraph(df2.format(x.getGrade()))));

                }
        );

        super.exportPDF("./HardestHomeworks.pdf", "Hardest Homeworks", table);
    }


}