package sample.changeevent;

import sample.domain.Homework;

public class HomeworkChangeEvent implements Event {

    private ChangeEventType type;
    private Homework data;
    private Homework oldData;

    public HomeworkChangeEvent(ChangeEventType type, Homework data, Homework oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public HomeworkChangeEvent(ChangeEventType type, Homework data) {
        this.type = type;
        this.data = data;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Homework getData() {
        return data;
    }

    public Homework getOldData() {
        return oldData;
    }
}