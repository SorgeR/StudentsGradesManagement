package sample.domain.DTOS;

public class TeacherAverageDTO {

    private String teacherName;
    private Double grade;

    public TeacherAverageDTO(String teacherName, Double grade) {
        this.teacherName = teacherName;
        this.grade = grade;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
}
