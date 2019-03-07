package sample.domain.DTOS;

public class StudentExemptionDTO {

    private Integer studentId;
    private String studentName;
    private Integer week;
    private String reason;

    public StudentExemptionDTO(Integer studentId, String studentName, Integer week, String reason) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.week = week;
        this.reason = reason;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public String getReason() {
        return reason;
    }

}
