package sample.domain.DTOS;

public class HomeworkDurationDTO {

    private Integer weeks;
    private Integer numberOfHomeworksWithWeeks;

    public HomeworkDurationDTO(Integer weeks, Integer numberOfHomeworksWithWeeks) {
        this.weeks = weeks;
        this.numberOfHomeworksWithWeeks = numberOfHomeworksWithWeeks;
    }

    public Integer getWeeks() {
        return weeks;
    }

    public Integer getNumberOfHomeworksWithWeeks() {
        return numberOfHomeworksWithWeeks;
    }

}
