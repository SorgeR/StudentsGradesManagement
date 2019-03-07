package sample.validator;

import sample.domain.Grade;

public class ValidatorGrade  implements Validator<Grade> {
    @Override
    public void validate(Grade g) throws ValidationException {
        String err="";

        if(g.getGrade()>10 || g.getGrade()<1){
            err+="The grade must be in this interval [1;10]!\n";
        }

        if(g.getWeek()<1 || g.getWeek()>14){
            err+="The week must be in this interval [1;14]\n";
        }


        if(!err.isEmpty()){
            throw new ValidationException(err);
        }
    }
}
