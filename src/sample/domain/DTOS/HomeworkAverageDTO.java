package sample.domain.DTOS;

public class HomeworkAverageDTO {

    private Integer number;
    private Integer receivedWeek;
    private Integer deadlineWeek;
    private String description;
    private Double grade;

    public HomeworkAverageDTO(Integer number, Integer receivedWeek, Integer deadlineWeek, String description, Double grade) {
        this.number = number;
        this.receivedWeek = receivedWeek;
        this.deadlineWeek = deadlineWeek;
        this.description = description;
        this.grade = grade;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getReceivedWeek() {
        return receivedWeek;
    }

    public Integer getDeadlineWeek() {
        return deadlineWeek;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
}
