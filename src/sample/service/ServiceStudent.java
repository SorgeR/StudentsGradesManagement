package sample.service;

import sample.changeevent.ChangeEventType;
import sample.changeevent.StudentChangeEvent;
import sample.domain.DTOS.GroupStudentsDTO;
import sample.domain.DTOS.TeacherStudentsDTO;
import sample.domain.Student;
import sample.observer.Observable;
import sample.observer.Observer;
import sample.repository.AbstractRepository;
import sample.repository.RepositoryStudent;
import sample.validator.ValidatorStudent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ServiceStudent implements Observable<StudentChangeEvent> {

    private AbstractRepository<Integer, Student> repositoryStudent;
    private List<Observer<StudentChangeEvent>> observers=new ArrayList<>();

    public ServiceStudent() {
        this.repositoryStudent = new RepositoryStudent(new ValidatorStudent());

    }

    public Student saveStudent(Student student) {


        for(int i=1;i<1000;++i){
            if(this.repositoryStudent.findOne(i)==null){
                student.setID(i);
                break;
            }
        }
        Student s = this.repositoryStudent.save(student);
        if (s == null) {
            StudentChangeEvent changeEvent=new StudentChangeEvent(ChangeEventType.ADD,student);
            notifyObservers(changeEvent);
        }
        return s;
    }


    public Student findStudent(Integer idStudent) {
        List<Student> s = (List<Student>) this.findActiveStudents();

        Optional<Student> studentOptional= s.stream().filter(x->x.getID().equals(idStudent)).findFirst();
        if(studentOptional.isPresent()){
            return studentOptional.get();
        }
        return null;
    }

    public Student deleteStudent(Integer idStudent) {
        Student student = this.repositoryStudent.findOne(idStudent);
        if (student != null) {
            student.setDeleted(true);
            this.repositoryStudent.update(student);
            notifyObservers(new StudentChangeEvent(ChangeEventType.DELETE,student));
            return student;
        }
        return null;
    }

    public Iterable<Student> findActiveStudents() {
        ArrayList<Student> allStudents = (ArrayList<Student>) this.repositoryStudent.findAll();

        return allStudents
                .stream()
                .filter((s) -> (s.getDeleted().equals(false)))
                .collect(Collectors.toList());

    }

    public Student updateStudent(Student student) {
        Student st = this.repositoryStudent.update(student);
        if(st==null){
            notifyObservers(new StudentChangeEvent(ChangeEventType.UPDATE,student));
        }
        return st;
    }

    public List<TeacherStudentsDTO> getNumberOfStudentsForEachTeacher()
    {
        ArrayList<Student> students= (ArrayList<Student>) this.findActiveStudents();
        return students.stream()
                .collect(Collectors.groupingBy(Student::getTeachersName))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, x->x.getValue().size()))
                .entrySet()
                .stream()
                .map(x->new TeacherStudentsDTO(x.getValue(),x.getKey()))
                .collect(Collectors.toList());
    }

    public List<GroupStudentsDTO> getNumbersOfStudentsForEachGroup()
    {
        ArrayList<Student> students= (ArrayList<Student>) this.findActiveStudents();
        return students.stream()
                .collect(Collectors.groupingBy(Student::getGroup))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, x->x.getValue().size()))
                .entrySet()
                .stream()
                .map(x->new GroupStudentsDTO(x.getValue(),x.getKey()))
                .collect(Collectors.toList());
    }

    public List<Student> filterStudents(String name,
                                                 Integer group,
                                                 Integer subgroup,
                                                 String email,
                                                 String teachersName)
    {
        ArrayList<Student> students= (ArrayList<Student>) findActiveStudents();

        return students.stream()
                .filter(x-> name == null || x.getName().startsWith(name))
                .filter(x-> group == null || x.getGroup().equals(group))
                .filter(x-> subgroup == null || x.getSubgroup().equals(subgroup))
                .filter(x-> email == null || x.getEmail().startsWith(email))
                .filter(x-> teachersName == null || x.getTeachersName().startsWith(teachersName))
                .collect(Collectors.toList());

    }


    public List<Student> filterStudentsByName(String name)
    {
        ArrayList<Student> students= (ArrayList<Student>) this.findActiveStudents();

        return students.stream()
                .filter(x-> name == null || x.getName().startsWith(name))
                .collect(Collectors.toList());

    }



    public List<Integer> getGroups(){
        List<Student> students= (List<Student>) this.findActiveStudents();
        return students.stream()
                .collect(Collectors.groupingBy(Student::getGroup))
                .entrySet()
                .stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

    }

    @Override
    public void addObserver(Observer<StudentChangeEvent> e) {
        this.observers.add(e);
    }

    @Override
    public void removeObserver(Observer<StudentChangeEvent> e) {
        this.observers.remove(e);
    }

    @Override
    public void notifyObservers(StudentChangeEvent t) {
        this.observers.forEach(x->x.update(t));
    }


}
