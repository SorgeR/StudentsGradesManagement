package sample.domain.DTOS;

public class PassedStudentsDTO {

    private String studentName;
    private String teacherName;
    private Boolean passed;
    private Double grade;

    public PassedStudentsDTO(String studentName, String teacherName, Boolean passed, Double grade) {
        this.studentName = studentName;
        this.teacherName = teacherName;
        this.passed = passed;
        this.grade = grade;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public Boolean getPassed() {
        return passed;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
}
