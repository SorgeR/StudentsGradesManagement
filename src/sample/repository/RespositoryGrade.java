package sample.repository;

import sample.domain.Grade;
import sample.domain.PairKeys.GradePairKey;
import sample.domain.Student;
import sample.domain.databasehelper.DatabaseHelper;
import sample.validator.Validator;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RespositoryGrade extends AbstractRepository<GradePairKey, Grade> {

    public RespositoryGrade(Validator<Grade> validator) {
        super(validator, DatabaseHelper.GRADE_TABLE_NAME);
    }

    @Override
    public void insertIntoTable(Grade entity) throws SQLException {
        Connection connection = getConnection();
        String query = "INSERT INTO Grades(idStudent,idHomework,grade,week,feedback)" +
                "VALUES(" + entity.getStudentId() + "," +
                entity.getHomeworkId() + "," +
                entity.getGrade()+","+
                entity.getWeek() + ",'" +
                entity.getFeedback() + "')";
        connection.prepareStatement(query).execute();
    }

    @Override
    public void deleteFromTable(GradePairKey gradePairKey) throws SQLException {
        Connection connection = getConnection();
        String query = "DELETE FROM Grades" +
                " WHERE IdStudent=" + gradePairKey.getIdStudent() + "and idHomework=" + gradePairKey.getIdHomework();
        connection.prepareStatement(query).execute();
    }

    @Override
    public void updateInTable(Grade entity) throws SQLException {
        Connection connection = getConnection();
        String query = "UPDATE Grades SET" +
                "grade=" + entity.getGrade() + "," +
                "week=" + entity.getWeek() + "," +
                "feedback='" + entity.getFeedback() + "'" +
                " WHERE IdStudent=" + entity.getStudentId() + "and idHomework=" + entity.getHomeworkId();
        connection.prepareStatement(query).execute();
    }

    @Override
    public Grade getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        return new Grade(resultSet.getInt(1),
                resultSet.getInt(2),
                resultSet.getDouble(3),
                resultSet.getInt(4),
                resultSet.getString(5));
    }
}
