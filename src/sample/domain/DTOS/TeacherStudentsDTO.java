package sample.domain.DTOS;

public class TeacherStudentsDTO {

    private Integer numberOfStudents;
    private String teachersName;

    public TeacherStudentsDTO(Integer numberOfStudents, String teachersName) {
        this.numberOfStudents = numberOfStudents;
        this.teachersName = teachersName;
    }

    public Integer getNumberOfStudents() {
        return numberOfStudents;
    }

    public String getTeachersName() {
        return teachersName;
    }

}
