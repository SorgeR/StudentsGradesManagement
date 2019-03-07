package sample.domain;

import sample.domain.PairKeys.ExemptionPairKey;
import sample.domain.interfaces.HasID;

import java.util.Objects;

public class Exemption implements HasID<ExemptionPairKey> {

    private ExemptionPairKey weekStudent;
    private String reason;


    public Exemption(Integer studentId,Integer week, String reason) {
        this.weekStudent = new ExemptionPairKey(studentId,week);
        this.reason = reason;
    }

    public Integer getWeek(){
        return this.weekStudent.getWeek();
    }

    public void setWeek(Integer week){
        this.weekStudent.setWeek(week);
    }

    public Integer getStudentId(){
        return this.weekStudent.getIdStudent();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public ExemptionPairKey getID() {
        return weekStudent;
    }

    @Override
    public void setID(ExemptionPairKey exemptionPairKey) {
        this.weekStudent=exemptionPairKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exemption exemption = (Exemption) o;
        return Objects.equals(weekStudent, exemption.weekStudent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weekStudent);
    }
}
