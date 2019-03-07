package sample.domain.DTOS;

public class StudentAverageDTO {

    private String studentName;
    private String teachersName;
    private Double finalGrade;

    public StudentAverageDTO(String studentName, String teachersName, Double finalGrade) {
        this.studentName = studentName;
        this.teachersName = teachersName;
        this.finalGrade = finalGrade;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getTeachersName() {
        return teachersName;
    }

    public Double getFinalGrade() {
        return finalGrade;
    }

}
