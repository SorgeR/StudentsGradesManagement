package sample.validator;

import org.apache.commons.text.WordUtils;
import sample.domain.Student;

public class ValidatorStudent implements Validator<Student> {
    @Override
    public void validate(Student s) throws ValidationException {
        String err="";




        if(s.getID()==null || s.getID()<0){
            err+="Invalid student ID!\n";
        }

        if(s.getName()==null || s.getName().equals("") || !s.getName().matches("^[a-zA-Z- ]+$")){
            err+="Invalid student name!\n";
        }
        else{
            s.setName(WordUtils.capitalizeFully(s.getName(),'-',' '));
        }


        if(s.getEmail()==null || !s.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,6}$")){
            err+="The email of the student can not be null and must have the email format!\n";
        }

        if(s.getGroup()==null || s.getGroup()<0){
            err+="The group can not be null or negative!\n";
        }

        if(s.getSubgroup()==null || (s.getSubgroup()!=1 && s.getSubgroup()!=2)) {
            err += "The subgroup can not be null or different from 1 or 2!\n";
        }

        if(s.getTeachersName()==null || s.getTeachersName().equals("") || !s.getName().matches("^[a-zA-Z- ]+$")){
            err+="The teacher can not be null or empty!\n";
        }
        else{
            s.setTeachersName(WordUtils.capitalizeFully(s.getTeachersName(),'-',' '));
        }

        if(!err.isEmpty()){
            throw new ValidationException(err);
        }
    }



}
