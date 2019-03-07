package sample.service;

import sample.domain.DTOS.StudentExemptionDTO;
import sample.domain.DTOS.WeekNumberOfExemptionsDTO;
import sample.domain.Exemption;
import sample.domain.PairKeys.ExemptionPairKey;
import sample.domain.Student;
import sample.domain.WeekHelper;
import sample.repository.CrudRepository;
import sample.repository.RepositoryExemption;
import sample.validator.ServiceException;
import sample.validator.ValidatorExemption;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ServiceExemption {

    private ServiceStudent students;
    private CrudRepository<ExemptionPairKey, Exemption> repositoryExemption;

    public ServiceExemption(ServiceStudent students) {
        this.students = students;
        this.repositoryExemption = new RepositoryExemption(new ValidatorExemption());
    }

    public Exemption saveExemption(Exemption exemption) {

        if(students.findStudent(exemption.getStudentId())==null){
            throw new ServiceException("the student does not exist!");
        }
        if(exemption.getWeek()> WeekHelper.currentWeek) {
            throw new ServiceException("the week of the exemption must be smaller than the current week!");
        }
        return this.repositoryExemption.save(exemption);
    }

    Integer numberOfExemptionsInIntervalOfWeeks(Integer studentID, Integer startWeek, Integer endWeek) {
        ArrayList<Exemption> all = (ArrayList<Exemption>) this.repositoryExemption.findAll();
        return all.stream()
                .filter((x) -> x.getWeek().compareTo(startWeek) >= 0 &&
                        x.getWeek().compareTo(endWeek) < 0 &&
                        x.getStudentId().equals(studentID)
                )
                .collect(Collectors.toList())
                .size();
    }

    public List<StudentExemptionDTO> studentExemptionDTOList() {
        List<Exemption> exemption = (List<Exemption>) repositoryExemption.findAll();

        return exemption.stream()
                .filter(x -> students.findStudent(x.getStudentId()) != null)
                .map(x -> {
                    Student student = students.findStudent(x.getStudentId());
                    if (student != null) {
                        return new StudentExemptionDTO(student.getID(), student.getName(), x.getWeek(), x.getReason());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<WeekNumberOfExemptionsDTO> getWeekNumberOfExemptionsDTOS() {
        List<Exemption> exemption = (List<Exemption>) repositoryExemption.findAll();

        return exemption.stream()
                .filter(x -> students.findStudent(x.getStudentId()) != null)
                .collect(Collectors.groupingBy(Exemption::getWeek))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, x -> x.getValue().size()))
                .entrySet()
                .stream()
                .map(x -> new WeekNumberOfExemptionsDTO(x.getKey(), x.getValue()))
                .collect(Collectors.toList());

    }


}
