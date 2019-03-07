package sample.repository;

import sample.domain.Exemption;
import sample.domain.PairKeys.ExemptionPairKey;
import sample.domain.databasehelper.DatabaseHelper;
import sample.validator.Validator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepositoryExemption extends AbstractRepository<ExemptionPairKey, Exemption> {

    public RepositoryExemption(Validator<Exemption> validator) {
        super(validator, DatabaseHelper.EXEMPTION_TABLE_NAME);
    }

    @Override
    public void insertIntoTable(Exemption entity) throws SQLException {
        Connection connection = getConnection();
        String query = "INSERT INTO Exemptions(idStudent,idWeek,reason)" +
                "VALUES(" + entity.getStudentId() + "," + entity.getWeek() + ",'" + entity.getReason() + "')";
        connection.prepareStatement(query).execute();
    }

    @Override
    public void deleteFromTable(ExemptionPairKey exemptionPairKey) throws SQLException {
        Connection connection = getConnection();
        String query = "DELETE FROM Exemptions" +
                " WHERE IdStudent=" + exemptionPairKey.getIdStudent() + "and idWeek=" + exemptionPairKey.getWeek();
        connection.prepareStatement(query).execute();
    }

    @Override
    public void updateInTable(Exemption entity) throws SQLException {
        Connection connection = getConnection();
        String query = "UPDATE Exemptions SET " +
                "reason='" + entity.getReason() + "'" +
                "WHERE IdStudent=" + entity.getStudentId() + "and idWeek=" + entity.getWeek();
        connection.prepareStatement(query).execute();
    }

    @Override
    public Exemption getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        return new Exemption(resultSet.getInt(1),
                resultSet.getInt(2),
                resultSet.getString(3));
    }
}
