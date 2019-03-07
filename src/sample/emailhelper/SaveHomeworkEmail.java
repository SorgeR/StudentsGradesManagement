package sample.emailhelper;

import sample.domain.Homework;
import sample.domain.Student;
import sample.service.ServiceHomework;
import sample.service.ServiceStudent;

public class SaveHomeworkEmail extends Thread implements SendEmailInterface {

    private Integer idStudent;
    private Integer idHomework;
    private ServiceHomework serviceHomework;
    private Student student;

    public SaveHomeworkEmail(Integer idStudent,
                          Integer idHomework,
                          ServiceHomework serviceHomework,
                          ServiceStudent serviceStudent){
        this.idStudent=idStudent;
        this.idHomework=idHomework;
        this.serviceHomework=serviceHomework;
        this.student=serviceStudent.findStudent(idStudent);
    }

    @Override
    public void run() {
        SendEmailFunctionHelper.sendEmail(student.getEmail(),createMessage(),"New Homework Notification!");
    }

    @Override
    public String createMessage() {
        Homework g=serviceHomework.findHomework(idHomework);
        String message="";
        if(g!=null){
            message+="Hello,\n\nHomework number "
                    +g.getID().toString()
                    +" is now available!\n"
                    +"It's deadline is week number "+g.getDeadlineWeek()+"\n"
                    +"Description: "+g.getDescription()+"\n\nGood Luck!"
                    +"\n\nAll the best,\n"+student.getTeachersName();
        }
        return message;
    }
}
