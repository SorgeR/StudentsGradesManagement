package sample.domain.DTOS;

public class WeekNumberOfExemptionsDTO {

    private Integer week;
    private Integer numberOfExemptions;

    public WeekNumberOfExemptionsDTO(Integer week, Integer numberOfExemptions) {
        this.week = week;
        this.numberOfExemptions = numberOfExemptions;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public Integer getNumberOfExemptions() {
        return numberOfExemptions;
    }

}
