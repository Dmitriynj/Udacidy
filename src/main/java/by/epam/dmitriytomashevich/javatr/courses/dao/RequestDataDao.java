package by.epam.dmitriytomashevich.javatr.courses.dao;

import by.epam.dmitriytomashevich.javatr.courses.dao.exception.DAOException;
import by.epam.dmitriytomashevich.javatr.courses.db.ConnectionPool;
import by.epam.dmitriytomashevich.javatr.courses.domain.RequestData;
import by.epam.dmitriytomashevich.javatr.courses.logic.builder.EntityBuilder;
import by.epam.dmitriytomashevich.javatr.courses.logic.builder.RequestDataBuilder;

import java.sql.*;
import java.util.List;

public class RequestDataDao implements AbstractDao<Long, RequestData> {
    private ConnectionPool pool = ConnectionPool.getInstance();
    private EntityBuilder<RequestData> builder = new RequestDataBuilder();

    private static final String INSERT = "INSERT INTO udacidy.request_data\n" +
            "    (section_id, request_id)\n" +
            "VALUES (?, ?);";

    @Override
    public List<RequestData> findAll() throws DAOException {
        return null;
    }

    @Override
    public RequestData findById(Long id) throws DAOException {
        return null;
    }

    @Override
    public void deleteById(Long id) throws DAOException {
    }

    @Override
    public Long create(RequestData entity) throws DAOException {
        Long requestFormId = null;
        Connection connection = null;
        try {
            connection = pool.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, entity.getSectionId());
            statement.setLong(2, entity.getRequestId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating conversation failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    requestFormId = generatedKeys.getLong(1);
                }
                else {
                    throw new SQLException("Creating conversation failed, no ID obtained.");
                }
            }
            connection.commit();
            return requestFormId;
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }

    @Override
    public void update(RequestData entity) throws DAOException {

    }
}