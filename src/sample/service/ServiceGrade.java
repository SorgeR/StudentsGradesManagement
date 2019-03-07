package sample.service;


import sample.domain.DTOS.*;
import sample.domain.Grade;
import sample.domain.Homework;
import sample.domain.PairKeys.GradePairKey;
import sample.domain.Student;
import sample.domain.WeekHelper;
import sample.emailhelper.SaveGradeEmail;
import sample.pdfhelper.PdfHelper;
import sample.repository.CrudRepository;
import sample.repository.RespositoryGrade;
import sample.validator.ServiceException;
import sample.validator.ValidatorGrade;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ServiceGrade {

    private CrudRepository<GradePairKey, Grade> repositoryGrade;
    private ServiceStudent serviceStudent;
    private ServiceHomework serviceHomework;
    private ServiceExemption serviceExemption;
    private static final Double beingLatePoints = 2.5;
    private PdfHelper pdfHelper;

    public ServiceGrade(ServiceStudent serviceStudent,
                        ServiceHomework serviceHomework,
                        ServiceExemption serviceExemption) {
        this.repositoryGrade = new RespositoryGrade(new ValidatorGrade());
        this.serviceStudent = serviceStudent;
        this.serviceHomework = serviceHomework;
        this.serviceExemption = serviceExemption;
        pdfHelper=new PdfHelper();
    }

    public Double penaltyPointsOfStudentInAWeekOnAHomework(Integer idStudent,
                                                           Integer idHomework,
                                                           Integer week) {
        Homework homework = serviceHomework.findHomework(idHomework);
        Student student = serviceStudent.findStudent(idStudent);

        if (student == null) {
            throw new ServiceException("inexistent student");
        }
        if (homework == null) {
            throw new ServiceException("inexistent homework");
        }
        if (week > homework.getDeadlineWeek()) {
            Integer lateWeeks = week - homework.getDeadlineWeek();
            Integer numberOfExemptions = serviceExemption.numberOfExemptionsInIntervalOfWeeks(idStudent,
                    homework.getDeadlineWeek(),
                    week);
            lateWeeks -= numberOfExemptions;

            if (lateWeeks > 2) {
                return 10.0;
            }

            if (lateWeeks > 0) {
                return lateWeeks * beingLatePoints;
            }
        }
        return 0.0;
    }

    private Double maximumMarkForAStudentInAWeekForAHomework(Integer idStudent,
                                                             Integer idHomework,
                                                             Integer week) {

        Double penaltyPoints = this.penaltyPointsOfStudentInAWeekOnAHomework(idStudent,
                idHomework,
                week);
        if (penaltyPoints == 10.0) {
            return 0.0;
        } else {
            return 10.0 - penaltyPoints;
        }

    }

    public Grade saveGrade(Grade grade) {
        Homework homework = serviceHomework.findHomework(grade.getHomeworkId());
        Student student = serviceStudent.findStudent(grade.getStudentId());

        if (student == null) {
            throw new ServiceException("inexistent student");
        }
        if (homework == null) {
            throw new ServiceException("inexistent homework");
        }
        if (grade.getWeek() > WeekHelper.currentWeek) {
            throw new ServiceException("the grade's week can not be greater than the current week");

        }
        if(grade.getWeek()<homework.getReceivedWeek())
        {
            throw new ServiceException("the homework is not available yet!");
        }

        Double maximumGrade = this.maximumMarkForAStudentInAWeekForAHomework(grade.getStudentId(), grade.getHomeworkId(), grade.getWeek());
        Double penaltyPoints = this.penaltyPointsOfStudentInAWeekOnAHomework(grade.getStudentId(), grade.getHomeworkId(), grade.getWeek());

        if (grade.getGrade() > maximumGrade) {

            if (penaltyPoints > 5 || grade.getGrade()-penaltyPoints<1) {
                grade.setGrade(1d);
            } else {
                grade.setGrade(grade.getGrade() - penaltyPoints);
            }

        }

        Grade g=this.repositoryGrade.save(grade);
        if(g==null)
        {
            (new SaveGradeEmail(grade.getStudentId(),grade.getHomeworkId(),this,serviceStudent)).start();
        }
        return g;

    }

    public Grade findGrade(int idStudent, int idHomework) {
        return repositoryGrade.findOne(new GradePairKey(idStudent, idHomework));
    }

    private List<StudentGradeDTO> getStudentsAndTheirGrades() {
        List<Grade> grades = (List<Grade>) this.repositoryGrade.findAll();
        List<StudentGradeDTO> gradesofStudent =(List<StudentGradeDTO>) grades.stream()
                .collect(Collectors.groupingBy(Grade::getStudentId))
                .entrySet()
                .stream()
                .map(x -> {
                            Student student = this.serviceStudent.findStudent(x.getKey());
                            if (student != null) {
                                return new StudentGradeDTO(x.getKey(),
                                        student.getName(),
                                        (ArrayList) x.getValue()
                                                .stream()
                                                .sorted(Comparator.comparing(Grade::getHomeworkId))
                                                .collect(Collectors.toList()));
                            } else {
                                return null;
                            }
                        }
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<Student> students = (List<Student>) this.serviceStudent.findActiveStudents();
        students.forEach(x -> {
            if (gradesofStudent.stream().noneMatch(y -> y.getStudentId().equals(x.getID()))) {
                gradesofStudent.add(new StudentGradeDTO(x.getID(), x.getName(), new ArrayList<>()));
            }
        });

        return gradesofStudent.stream()
                .sorted((o1, o2) -> {
                    if (o1.getGrades().size() > o2.getGrades().size()) {
                        return -1;
                    } else {
                        if (o1.getGrades().size() == o2.getGrades().size()) {
                            return o1.getName().compareTo(o2.getName());
                        } else {
                            return 1;
                        }
                    }
                })
                .collect(Collectors.toList());


    }

    public List<StudentGradeDTO> filterStudentGradesDTOListByStudentName(String studentName) {
        return this.getStudentsAndTheirGrades()
                .stream()
                .filter(x -> x.getName().startsWith(studentName))
                .collect(Collectors.toList());
    }

    public List<StudentGradeDTO> filterStudentsFromGivenGroupAndName(String studentName,Integer group){
        return this.filterStudentGradesDTOListByStudentName(studentName)
                .stream()
                .filter(x->{
                    Student s=serviceStudent.findStudent(x.getStudentId());
                    return s.getGroup().equals(group);
                })
                .collect(Collectors.toList());
    }

    private List<PassedStudentsDTO> passedOrNotPassedStudents() {
        List<Grade> grades= (List<Grade>) this.repositoryGrade.findAll();

        return grades.stream()
                .filter(x->serviceHomework.findHomework(x.getHomeworkId())!=null)
                .collect(Collectors.groupingBy(Grade::getStudentId))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, x->getMedium(x.getValue())))
                .entrySet()
                .stream()
                .map(x->{
                    Student student=this.serviceStudent.findStudent(x.getKey());
                    if(student==null){
                        return null;
                    }
                    return new PassedStudentsDTO(student.getName(),student.getTeachersName(), x.getValue() >= 5,x.getValue());
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<PassedNumberDTO> getPassedNotPassedNumberOfStudents() {
        List<PassedStudentsDTO> passedStudentsDTOS=passedOrNotPassedStudents();

        return passedStudentsDTOS.stream()
                .collect(Collectors.groupingBy(PassedStudentsDTO::getPassed))
                .entrySet()
                .stream()
                .map(x->new PassedNumberDTO(x.getKey(),x.getValue().size()))
                .collect(Collectors.toList());
    }

    private double getMedium(List<Grade> grades){
        Integer gainedWeight=0;
        AtomicReference<Integer> totalWeight= new AtomicReference<>(0);
        serviceHomework.findHomeworks().forEach(x-> totalWeight.updateAndGet(v -> v + x.getDeadlineWeek() - x.getReceivedWeek()));

        double finalGrade=0;
        for(Grade grade: grades){
            Homework homework = serviceHomework.findHomework(grade.getID().getIdHomework());
            Integer weight = homework.getDeadlineWeek() - homework.getReceivedWeek();
            finalGrade+=grade.getGrade()*weight;
            gainedWeight+=weight;
        }

        return (finalGrade + (totalWeight.get() - gainedWeight)) / totalWeight.get();
    }

    public void exportPassedNotPassedToPdf() throws FileNotFoundException {
        pdfHelper.writePassedNotPassedStudents(passedOrNotPassedStudents());
    }

    private List<StudentAverageDTO> getTop30Students(){
        List<Grade> grades= (List<Grade>) this.repositoryGrade.findAll();

        return grades.stream()
                .filter(x->serviceHomework.findHomework(x.getHomeworkId())!=null)
                .collect(Collectors.groupingBy(Grade::getStudentId))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, x->getMedium(x.getValue())
                ))
                .entrySet()
                .stream()
                .map(x->{
                    Student student=this.serviceStudent.findStudent(x.getKey());
                    if(student==null){
                        return null;
                    }
                    return new StudentAverageDTO(student.getName(),student.getTeachersName(),x.getValue());
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(StudentAverageDTO::getFinalGrade).reversed())
                .limit(30)
                .collect(Collectors.toList());
    }

    public void exportTop30ToPDF() throws FileNotFoundException {
        this.pdfHelper.writeTop30StudentsToPDF(this.getTop30Students());
    }

    private List<TeacherAverageDTO> getTopOfTeachers(){
        return this.getTop30Students().stream()
                .collect(Collectors.groupingBy(StudentAverageDTO::getTeachersName))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, x->x.getValue().stream()
                                                            .mapToDouble(StudentAverageDTO::getFinalGrade)
                                                            .average()
                                                            .getAsDouble()))
                .entrySet()
                .stream()
                .map(x->new TeacherAverageDTO(x.getKey(),x.getValue()))
                .collect(Collectors.toList())
                .stream()
                .sorted(Comparator.comparing(TeacherAverageDTO::getGrade).reversed())
                .collect(Collectors.toList());

    }

    public void exportTopTeachersToPDF() throws FileNotFoundException {
        pdfHelper.writeTopTeachersToPDF(this.getTopOfTeachers());
    }

    public List<HomeworkAverageDTO> averageGradeOnHomeworks() {
        List<Grade> grades= (List<Grade>) this.repositoryGrade.findAll();

        return grades.stream()
                .filter(x->serviceHomework.findHomework(x.getHomeworkId())!=null)
                .filter(x->serviceStudent.findStudent(x.getStudentId())!=null)
                .collect(Collectors.groupingBy(Grade::getHomeworkId))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, y->y.getValue().stream().mapToDouble(x->x.getGrade()).average().getAsDouble()))
                .entrySet()
                .stream()
                .map(x->{
                    Homework h=serviceHomework.findHomework(x.getKey());
                    if(h==null){
                        return null;
                    }
                    return new HomeworkAverageDTO(h.getID(),h.getReceivedWeek(),h.getDeadlineWeek(),h.getDescription(),x.getValue());
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(HomeworkAverageDTO::getGrade))
                .collect(Collectors.toList());
    }

    public void exportTopHardestHomeworksToPDF() throws FileNotFoundException {
        this.pdfHelper.writeTopHardestHomeworks(averageGradeOnHomeworks());
    }


}
