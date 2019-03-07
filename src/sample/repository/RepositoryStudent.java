package sample.repository;

import sample.domain.Student;
import sample.domain.databasehelper.DatabaseHelper;
import sample.validator.Validator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepositoryStudent extends AbstractRepository<Integer, Student> {

    public RepositoryStudent(Validator<Student> validator) {

        super(validator, DatabaseHelper.STUDENT_TABLE_NAME);
    }



    @Override
    public void insertIntoTable(Student entity) throws SQLException {
        Connection connection = getConnection();
        String query = "INSERT INTO Students(id,name,groups,subgroup,email,teachersName,deleted)" +
                "VALUES("+entity.getID()+",'" + entity.getName() + "'," +
                entity.getGroup() + "," +
                entity.getSubgroup() + ",'" +
                entity.getEmail() + "','" +
                entity.getTeachersName() + "'," +
                "0)";
        connection.prepareStatement(query).execute();
    }

    @Override
    public void deleteFromTable(Integer integer) {

    }

    @Override
    public void updateInTable(Student entity) throws SQLException {
        Connection connection = getConnection();
        String query = "UPDATE Students SET " +
                "name='" + entity.getName() + "'," +
                "groups=" + entity.getGroup() + "," +
                "subgroup=" + entity.getSubgroup() + "," +
                "email='" + entity.getEmail() + "'," +
                "teachersName='" + entity.getTeachersName() + "'," +
                "deleted=" + (entity.getDeleted()==true ? 1:0) +
                " WHERE Id=" + entity.getID();
        connection.prepareStatement(query).execute();
    }

    @Override
    public Student getEntityFromResultSet(ResultSet resultSet) throws SQLException {

        return new Student(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getBoolean(7));
    }
}
