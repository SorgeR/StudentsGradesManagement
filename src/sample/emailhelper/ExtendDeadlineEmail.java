package sample.emailhelper;

import sample.domain.Student;
import sample.service.ServiceStudent;

public class ExtendDeadlineEmail extends Thread implements SendEmailInterface {


    private Integer idHomework;
    private Integer oldDeadline;
    private Integer newDeadline;
    private Student student;

    public ExtendDeadlineEmail(Integer idStudent,
                               Integer idHomework,
                               Integer oldDeadline,
                               Integer newDeadline,
                               ServiceStudent serviceStudent) {
        this.idHomework = idHomework;
        this.oldDeadline = oldDeadline;
        this.newDeadline = newDeadline;
        this.student = serviceStudent.findStudent(idStudent);
    }

    @Override
    public void run() {
        SendEmailFunctionHelper.sendEmail(student.getEmail(), createMessage(),"Extended Deadline Notification!");
    }

    @Override
    public String createMessage() {

        String message = "Hello,\n\nHomework's number "
                + idHomework
                + " deadline was extended from week number " + oldDeadline+" to week number "+newDeadline+" !"
                + "\n\nAll the best,\n" + student.getTeachersName();

        return message;
    }
}