package sample.domain.PairKeys;

import java.util.Objects;

public class GradePairKey {

    private Integer idStudent;
    private Integer idHomework;


    public GradePairKey(Integer idStudent, Integer idHomework) {
        this.idStudent = idStudent;
        this.idHomework = idHomework;

    }

    public Integer getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(Integer idStudent) {
        this.idStudent = idStudent;
    }

    public Integer getIdHomework() {
        return idHomework;
    }

    public void setIdHomework(Integer idHomework) {
        this.idHomework = idHomework;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradePairKey that = (GradePairKey) o;
        return Objects.equals(idStudent, that.idStudent) &&
                Objects.equals(idHomework, that.idHomework);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idStudent, idHomework);
    }
}
