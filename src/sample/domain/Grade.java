package sample.domain;

import sample.domain.DTOS.StudentGradeDTO;
import sample.domain.PairKeys.GradePairKey;
import sample.domain.interfaces.HasID;

import java.util.List;

public class Grade implements HasID<GradePairKey> {

    private GradePairKey gradePair;
    private Double grade;
    private Integer week;
    private String feedback;

    public Grade(Integer idStudent,Integer idHomework, Double grade, Integer week, String feedback) {
        this.gradePair = new GradePairKey(idStudent,idHomework);
        this.grade = grade;
        this.week = week;
        this.feedback = feedback;
    }

    @Override
    public GradePairKey getID() {
        return this.gradePair;
    }

    @Override
    public void setID(GradePairKey gradePairKey) {
        this.gradePair=gradePairKey;
    }

    public Integer getStudentId(){
        return this.gradePair.getIdStudent();
    }

    public void setStudentId(Integer studentId){
        this.gradePair.setIdStudent(studentId);
    }

    public void setHomeworkId(Integer homeworkId){
        this.gradePair.setIdHomework(homeworkId);
    }


    public Integer getHomeworkId(){
        return this.gradePair.getIdHomework();
    }




    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }


}
