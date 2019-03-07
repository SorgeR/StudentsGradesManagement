package sample.observer;


import sample.changeevent.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
