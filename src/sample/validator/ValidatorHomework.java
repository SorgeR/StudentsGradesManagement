package sample.validator;

import sample.domain.Homework;

public class ValidatorHomework implements Validator<Homework> {
    @Override
    public void validate(Homework h) throws ValidationException {
        String err="";

        if(h.getID()==null || h.getID()<0){
            err+="The id of the homework must be not null and greater than 0!\n";
        }

        if(h.getDescription()==null || h.getDescription().equals("")){
            err+="The description of the homework must be not null and not empty!\n";
        }

        if(h.getDeadlineWeek()==null || h.getDeadlineWeek()<0 || h.getDeadlineWeek()>14){
            err+="The deadline of the homework must be not null and in [0,14]\n";
        }

        if(h.getDeadlineWeek()<h.getReceivedWeek())
        {
            err+="The deadline must be greater than the received week!";
        }

        if(h.getReceivedWeek()==null || h.getReceivedWeek()<0 || h.getReceivedWeek()>14){
            err+="The received week of the homework must be not null and in [0,14]\n";
        }

        if(!err.isEmpty()){
            throw new ValidationException(err);
        }
    }
}
