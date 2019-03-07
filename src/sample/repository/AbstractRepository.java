package sample.repository;

import sample.domain.interfaces.HasID;
import sample.validator.ValidationException;
import sample.validator.Validator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractRepository<ID,E extends HasID<ID>> extends InMemoryRepository<ID,E> {


    public String tableName;

    public AbstractRepository(Validator<E> validator, String tableName) {
        super(validator);
        this.tableName = tableName;
        loadData();
    }


    Connection getConnection(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection connectionObj=DriverManager.getConnection( "jdbc:sqlserver://localhost:1433;databaseName=LaboratoriesManager;integratedSecurity=true");
            if(connectionObj!=null){
                return connectionObj;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract void  insertIntoTable(E entity) throws SQLException;

    public abstract void  deleteFromTable(ID id) throws SQLException;

    public abstract void updateInTable(E entity) throws SQLException;

    public abstract E getEntityFromResultSet(ResultSet resultSet) throws SQLException;

    @Override
    public E save(E entity) throws ValidationException {
        E newEntity=super.save(entity);
        if(newEntity==null){
            try {
                this.insertIntoTable(entity);
            }
            catch (SQLException ex){
                ex.printStackTrace();
            }
        }
        return newEntity;
    }

    @Override
    public E delete(ID id) {
        E oldEntity=super.delete(id);
        if(oldEntity!=null){
            try{
            this.deleteFromTable(id);
            }
            catch (SQLException ex){
                ex.printStackTrace();
            }
        }
        return oldEntity;
    }

    @Override
    public E update(E entity) {
        E updateEntity=super.update(entity);
        if(updateEntity==null){
            try{
            this.updateInTable(entity);
            }
            catch (SQLException ex){
                ex.printStackTrace();
            }
        }
        return updateEntity;
    }

    private void loadData(){

        Connection connection = getConnection();
        ResultSet resultSet ;
        String query="SELECT * FROM "+tableName;
        try {
            resultSet = connection.prepareStatement(query).executeQuery();
            while (resultSet.next()) {
                E entity=this.getEntityFromResultSet(resultSet);
                entities.put(entity.getID(),entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
