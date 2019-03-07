package sample.domain.DTOS;

public class StudentHomeworkGradeDTO {

    private String studentName;
    private Double grade;

    public StudentHomeworkGradeDTO(String studentName, Double grade) {
        this.studentName = studentName;
        this.grade = grade;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getGrade() {
        return String.valueOf(grade);
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
}
