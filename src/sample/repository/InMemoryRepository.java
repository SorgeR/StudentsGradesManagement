package sample.repository;

import sample.domain.interfaces.HasID;
import sample.validator.ValidationException;
import sample.validator.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRepository<ID,E extends HasID<ID>> implements CrudRepository<ID,E>  {

    protected Map<ID,E> entities;
    private Validator<E> validator;

    public InMemoryRepository(Validator<E> validator) {
        this.entities = new HashMap<>();
        this.validator = validator;
    }

    @Override
    public E findOne(ID id) {
        if(id==null){
            throw new IllegalArgumentException("id must be not null!");
        }
        return this.entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        List<E> entitiesArr=new ArrayList<>();
        this.entities.forEach((ID,E)->entitiesArr.add(E));
        return entitiesArr;
    }

    @Override
    public E save(E entity) throws ValidationException {
        if(entity==null){
            throw new IllegalArgumentException("entity must be not null!");
        }
        validator.validate(entity);

        if(this.entities.get(entity.getID())==null){
            this.entities.put(entity.getID(),entity);
            return null;
        }
        else{
            return entity;
        }

    }

    @Override
    public E delete(ID id) {
        if(id==null){
            throw new IllegalArgumentException("id must be not null!");
        }
        return this.entities.remove(id);
    }

    @Override
    public E update(E entity) {
        if(entity==null){
            throw new IllegalArgumentException("entity must be not null!");
        }
        this.validator.validate(entity);

        if(this.findOne(entity.getID())==null){
            return entity;
        }

        else{
            this.entities.put(entity.getID(),entity);
            return null;
        }
    }
}
