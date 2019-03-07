package sample.domain.DTOS;

public class PassedNumberDTO {

    private boolean passed;
    private Integer numberOfPassed;

    public PassedNumberDTO(boolean passed, Integer numberOfPassed) {
        this.passed = passed;
        this.numberOfPassed = numberOfPassed;
    }

    public boolean getPassed() {
        return passed;
    }

    public Integer getNumberOfPassed() {
        return numberOfPassed;
    }

}
