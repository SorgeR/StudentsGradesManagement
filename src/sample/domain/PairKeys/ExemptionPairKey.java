package sample.domain.PairKeys;

import java.util.Objects;

public class ExemptionPairKey {

    private Integer idStudent;
    private Integer week;


    public ExemptionPairKey(Integer idStudent, Integer week) {
        this.idStudent = idStudent;
        this.week = week;
    }

    public Integer getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(Integer idStudent) {
        this.idStudent = idStudent;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExemptionPairKey that = (ExemptionPairKey) o;
        return Objects.equals(idStudent, that.idStudent) &&
                Objects.equals(week, that.week);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idStudent, week);
    }
}
