package sample.emailhelper;
import sample.domain.Grade;
import sample.domain.Student;
import sample.service.ServiceGrade;
import sample.service.ServiceStudent;

public class SaveGradeEmail extends Thread implements SendEmailInterface{

    private Integer idStudent;
    private Integer idHomework;
    private ServiceGrade serviceGrade;
    private Student student;

    public SaveGradeEmail(Integer idStudent,
                          Integer idHomework,
                          ServiceGrade serviceGrade,
                          ServiceStudent serviceStudent){
        this.idStudent=idStudent;
        this.idHomework=idHomework;
        this.serviceGrade=serviceGrade;
        this.student=serviceStudent.findStudent(idStudent);
    }

    @Override
    public void run() {
        try {
            SendEmailFunctionHelper.sendEmail(student.getEmail(), createMessage(),"New Grade Notification!");
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
    }

    @Override
    public String createMessage() {
        Grade g=serviceGrade.findGrade(idStudent,idHomework);
        String message="";
        if(g!=null){
            message+="Hello,\n\nYou got the grade "
                    +g.getGrade()
                    +" on homework number "
                    +idHomework+" !\n"+
                    "Feedback: "+g.getFeedback()
                    +"\n\nAll the best,\n"+student.getTeachersName();
        }
        return message;
    }
}
