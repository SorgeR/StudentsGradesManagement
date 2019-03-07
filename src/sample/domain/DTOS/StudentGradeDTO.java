package sample.domain.DTOS;

import javafx.beans.property.SimpleStringProperty;
import sample.domain.Grade;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Optional;

public class StudentGradeDTO {

    private Integer studentId;
    private String studentName;
    private ArrayList<Grade> studentGrades;

    public StudentGradeDTO(Integer studentId, String studentName, ArrayList<Grade> studentGrades) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentGrades = studentGrades;
    }

    @Override
    public String toString() {
        return "StudentGradeDTO{" +
                "studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", studentGrades=" + studentGrades +
                '}';
    }

    public Integer getStudentId() {
        return studentId;
    }


    public String getName() {
        return this.studentName;
    }

    public ArrayList<Grade> getGrades(){
        return this.studentGrades;
    }

    public  String getGradeH1(){
        Optional<Grade> o=studentGrades.stream().filter(x->x.getHomeworkId()==1).findFirst();
        if(o.isPresent()){
            return String.valueOf(o.get().getGrade());
        }
        else return null;
    }
    public String getGradeH2(){
        Optional<Grade> o=studentGrades.stream().filter(x->x.getHomeworkId()==2).findFirst();
        if(o.isPresent()){
            return String.valueOf(o.get().getGrade());
        }
        else return null;
    }
    public String getGradeH3(){
        Optional<Grade> o=studentGrades.stream().filter(x->x.getHomeworkId()==3).findFirst();
        if(o.isPresent()){
            return String.valueOf(o.get().getGrade());
        }
        else return null;
    }
    public String getGradeH4(){
        Optional<Grade> o=studentGrades.stream().filter(x->x.getHomeworkId()==4).findFirst();
        if(o.isPresent()){
            return String.valueOf(o.get().getGrade());
        }
        else return null;
    }
    public String getGradeH5(){
        Optional<Grade> o=studentGrades.stream().filter(x->x.getHomeworkId()==5).findFirst();
        if(o.isPresent()){
            return String.valueOf(o.get().getGrade());
        }
        else return null;
    }
    public String getGradeH6(){
        Optional<Grade> o=studentGrades.stream().filter(x->x.getHomeworkId()==6).findFirst();
        if(o.isPresent()){
            return String.valueOf(o.get().getGrade());
        }
        else return null;
    }
    public String getGradeH7(){
        Optional<Grade> o=studentGrades.stream().filter(x->x.getHomeworkId()==7).findFirst();
        if(o.isPresent()){
            return String.valueOf(o.get().getGrade());
        }
        else return null;
    }
    public String getGradeH8(){
        Optional<Grade> o=studentGrades.stream().filter(x->x.getHomeworkId()==8).findFirst();
        if(o.isPresent()){
            return String.valueOf(o.get().getGrade());
        }
        else return null;
    }
    public String getGradeH9(){
        Optional<Grade> o=studentGrades.stream().filter(x->x.getHomeworkId()==9).findFirst();
        if(o.isPresent()){
            return String.valueOf(o.get().getGrade());
        }
        else return null;
    }
    public String getGradeH10(){
        Optional<Grade> o=studentGrades.stream().filter(x->x.getHomeworkId()==10).findFirst();
        if(o.isPresent()){
            return String.valueOf(o.get().getGrade());
        }
        else return null;
    }
    public String getGradeH11(){
        Optional<Grade> o=studentGrades.stream().filter(x->x.getHomeworkId()==11).findFirst();
        if(o.isPresent()){
            return String.valueOf(o.get().getGrade());
        }
        else return null;
    }
    public String getGradeH12(){
        Optional<Grade> o=studentGrades.stream().filter(x->x.getHomeworkId()==12).findFirst();
        if(o.isPresent()){
            return String.valueOf(o.get().getGrade());
        }
        else return null;
    }
    public String getGradeH13(){
        Optional<Grade> o=studentGrades.stream().filter(x->x.getHomeworkId()==13).findFirst();
        if(o.isPresent()){
            return String.valueOf(o.get().getGrade());
        }
        else return null;
    }
    public String getGradeH14(){
        Optional<Grade> o=studentGrades.stream().filter(x->x.getHomeworkId()==14).findFirst();
        if(o.isPresent()){
            return String.valueOf(o.get().getGrade());
        }
        else return null;
    }
}
