package sample.validator;

public interface Validator<E> {

    void validate(E entity) throws ValidationException;

}
