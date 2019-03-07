package sample.service;

import sample.changeevent.ChangeEventType;
import sample.changeevent.HomeworkChangeEvent;
import sample.domain.DTOS.HomeworkDurationDTO;
import sample.domain.Homework;
import sample.emailhelper.ExtendDeadlineEmail;
import sample.emailhelper.SaveHomeworkEmail;
import sample.observer.Observable;
import sample.observer.Observer;
import sample.repository.AbstractRepository;
import sample.repository.RepositoryHomework;
import sample.validator.ServiceException;
import sample.validator.ValidatorHomework;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServiceHomework implements Observable<HomeworkChangeEvent> {

    private AbstractRepository<Integer,Homework> repositoryHomework;
    private ServiceStudent serviceStudent=new ServiceStudent();
    private List<Observer<HomeworkChangeEvent>> observers=new ArrayList<>();


    public ServiceHomework() {
        this.repositoryHomework = new RepositoryHomework(new ValidatorHomework());
    }

    public Homework saveHomework(Homework homework)
    {
        for(int i=1;i<15;++i){
            if(this.repositoryHomework.findOne(i)==null){
                homework.setID(i);
                break;
            }
        }
        Homework h=this.repositoryHomework.save(homework);
        if(h==null)
        {
            serviceStudent.findActiveStudents().forEach(x->{
                    (new SaveHomeworkEmail(x.getID(),homework.getID(),this,serviceStudent)).start();
            });
            notifyObservers(new HomeworkChangeEvent(ChangeEventType.ADD,homework));
        }
        return h;

    }

    public Homework findHomework(Integer homeworkID){
        return this.repositoryHomework.findOne(homeworkID);
    }

    Iterable<Homework> findHomeworks(){
        return this.repositoryHomework.findAll();
    }


    public List<Homework> getSortedHomeworksById(){
        return  ((List<Homework>)this.findHomeworks()).stream()
                .sorted(Comparator.comparing(Homework::getID))
                .collect(Collectors.toList());
    }



    public Homework extendDeadline(Integer idHomework,Integer newDeadline,Integer currentWeek){
        Homework homework=this.repositoryHomework.findOne(idHomework);
        if(idHomework==null || newDeadline==null || currentWeek==null)
        {
            throw new ServiceException("invalid data!");
        }

        if(homework==null)
        {
            throw new ServiceException("the homework does not exist!");
        }

        if(newDeadline<currentWeek){
            throw new ServiceException("the deadline week can not be extended to less than the current week");
        }

        Integer oldDeadline=homework.getDeadlineWeek();
        homework.setDeadlineWeek(newDeadline);
        Homework h=this.repositoryHomework.update(homework);
        if(h==null){
            serviceStudent.findActiveStudents().forEach(x->{
                (new ExtendDeadlineEmail(x.getID(),idHomework,oldDeadline,homework.getDeadlineWeek(),serviceStudent)).start();
            });

            notifyObservers(new HomeworkChangeEvent(ChangeEventType.UPDATE,homework));
        }
        return h;
    }

    public List<HomeworkDurationDTO> numberOfHomeworksGroupedByNumberOfWeeks(){
        List<Homework> homeworkList= (List<Homework>) this.repositoryHomework.findAll();

        return new ArrayList<>(homeworkList.stream()
                .collect(Collectors.toMap(x -> x, x -> x.getDeadlineWeek() - x.getReceivedWeek()))
                .entrySet())
                .stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue))
                .entrySet()
                .stream()
                .map(x->new HomeworkDurationDTO(x.getKey(),x.getValue().size()))
                .collect(Collectors.toList());

    }

    @Override
    public void addObserver(Observer<HomeworkChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<HomeworkChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(HomeworkChangeEvent t) {
        observers.forEach(x->x.update(t));
    }

}
