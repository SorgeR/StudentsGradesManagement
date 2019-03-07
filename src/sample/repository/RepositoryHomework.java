package sample.repository;

import sample.domain.Homework;
import sample.domain.databasehelper.DatabaseHelper;
import sample.validator.Validator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepositoryHomework extends AbstractRepository<Integer, Homework> {

    public RepositoryHomework(Validator<Homework> validator) {
        super(validator, DatabaseHelper.HOMEWORK_TABLE_NAME);
    }


    @Override
    public void insertIntoTable(Homework entity) throws SQLException {
        Connection connection = getConnection();
        String query = "INSERT INTO Homeworks (id,description,deadlineWeek,receivedWeek)\n" +
                "VALUES(" + entity.getID() + ",'" +
                entity.getDescription() + "'," +
                entity.getDeadlineWeek() + "," +
                entity.getReceivedWeek() + ")";
        connection.prepareStatement(query).execute();
    }

    @Override
    public void deleteFromTable(Integer integer) throws SQLException {
        Connection connection = getConnection();
        String query = "DELETE FROM Homeworks WHERE Id=" + integer;
        connection.prepareStatement(query).execute();
    }

    @Override
    public void updateInTable(Homework entity) throws SQLException {
        Connection connection = getConnection();
        String query = "UPDATE Homeworks " +
                "SET " +
                "description='"+entity.getDescription() +"'"+
                ",deadlineWeek="+entity.getDeadlineWeek() +
                ",receivedWeek="+entity.getReceivedWeek()+
                " WHERE id="+entity.getID();

        connection.prepareStatement(query).execute();
    }

    @Override
    public Homework getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        return new Homework(resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getInt(3),
                resultSet.getInt(4));
    }
}
