package sample.domain;

import sample.domain.interfaces.HasID;

import java.util.Objects;

public class Homework implements HasID<Integer> {

    private Integer id;
    private String description;
    private Integer deadlineWeek;
    private Integer receivedWeek;


    public Homework(Integer id, String description, Integer deadlineWeek, Integer receivedWeek) {
        this.id = id;
        this.description = description;
        this.deadlineWeek = deadlineWeek;
        this.receivedWeek = receivedWeek;
    }

    public Homework(String description, Integer deadlineWeek, Integer receivedWeek) {
        this.description = description;
        this.deadlineWeek = deadlineWeek;
        this.receivedWeek = receivedWeek;
    }

    @Override
    public Integer getID() {
        return this.id;
    }

    @Override
    public void setID(Integer integer) {
        this.id=integer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDeadlineWeek() {
        return deadlineWeek;
    }

    public void setDeadlineWeek(Integer deadlineWeek) {
        this.deadlineWeek = deadlineWeek;
    }

    public Integer getReceivedWeek() {
        return receivedWeek;
    }

    public void setReceivedWeek(Integer receivedWeek) {
        this.receivedWeek = receivedWeek;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Homework homework = (Homework) o;
        return Objects.equals(id, homework.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
