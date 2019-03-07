package sample.validator;

import sample.domain.Exemption;

public class ValidatorExemption  implements Validator<Exemption> {

    @Override
    public void validate(Exemption entity) throws ValidationException {
        String err="";
        if(entity.getStudentId()==null || entity.getStudentId()<0){
            err+="id can not be negative or null!\n";
        }

        if(entity.getWeek()==null || entity.getWeek()<0){
            err+="week can not be negative or null!\n";
        }

        if(entity.getReason()==null || entity.getReason().equals("")){
            err+="reason can not be empty or null!";
        }

        if(!err.isEmpty()){
            throw new ValidationException(err);
        }
    }
}
