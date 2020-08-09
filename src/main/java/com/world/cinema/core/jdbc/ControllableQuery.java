package com.world.cinema.core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class ControllableQuery implements AutoCloseable {

    private Query query;

    private Connection connection;

    public ControllableQuery(Query query, Connection connection) {
        this.query = query;
        this.connection = connection;
    }


    /**
     * Use for performing custom SQL queries with parameters (Update, insert, delete queries). No batch support
     * @return number of updated rows, wich are matched by query condition,
     * even if a new value of updatable field has the same value!
     */
    public int executeUpdate() {
        try (PreparedStatement pstmt = connection.prepareStatement(query.getSql())) {
            query.setValuesToPreparedStatement(pstmt);
            return pstmt.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            if (Objects.nonNull(connection)) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    public void commitTransaction() throws SQLException {
        if (Objects.nonNull(connection)) {
            connection.commit();
        }
    }

    public void rollbackTransaction() throws SQLException {
        if (Objects.nonNull(connection)) {
            connection.rollback();
        }
    }

    @Override
    public void close() throws Exception {
        if (Objects.nonNull(connection)) {
            connection.close();
        }
    }
}
