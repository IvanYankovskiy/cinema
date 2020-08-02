package com.world.cinema.core.jdbc;

import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class BaseDAO {

    private DataSource dataSource;

    private DataExtractor dataExtractor;

    private StatementBuilder stmntBuilder;

    @Autowired
    public BaseDAO(DataSource dataSource, DataExtractor dataExtractor, StatementBuilder stmntBuilder) {
        this.dataSource = dataSource;
        this.dataExtractor = dataExtractor;
        this.stmntBuilder = stmntBuilder;
    }

    public Integer insert(Object entity) throws IllegalAccessException {
        String tableName = dataExtractor.extractTableName(entity);
        Map<String, FieldDetails> fieldDetailsMap = dataExtractor.extractFieldNamesAndValues(entity);
        String sql = stmntBuilder.buildInsertStatement(tableName, fieldDetailsMap);
        Integer generatedKey = performModificationQuery(fieldDetailsMap, sql);
        return generatedKey;
    }

    public <T> boolean insertMultiple(List<T> collection) throws IllegalAccessException {
        Iterator<T> iterator = collection.iterator();
        String tableName;
        if (iterator.hasNext()) {
            tableName = dataExtractor.extractTableName(iterator.next());
        } else
            return false;
        List<Map<String, FieldDetails>> filedDetailsCollection = new ArrayList<>(collection.size());
        for (T entity : collection) {
            filedDetailsCollection.add(dataExtractor.extractFieldNamesAndValues(entity));
        }
        String sql = stmntBuilder.buildInsertStatement(tableName, filedDetailsCollection.get(0));
        return performBatchModificationQuery(filedDetailsCollection, sql);
    }

    private boolean performBatchModificationQuery(List<Map<String, FieldDetails>> preparedObjectCollection, String sql) {
        try(Connection connection = dataSource.getConnection()) {
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                PreparedStatementSetter valueSetter = new PreparedStatementSetter(pstmt);
                for (Map<String, FieldDetails> fieldDetailsMap : preparedObjectCollection) {
                    valueSetter.setStatementValues(fieldDetailsMap);
                    pstmt.addBatch();
                }
                int[] batchQueryResults = pstmt.executeBatch();
                boolean isSucceed = Arrays.stream(batchQueryResults).noneMatch(r -> r < 0);
                if (isSucceed)
                    connection.commit();
                else
                    connection.rollback();
                return isSucceed;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    private Integer performModificationQuery(Map<String, FieldDetails> fieldDetailsMap, String sql) {
        try(Connection connection = dataSource.getConnection()) {
            try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                PreparedStatementSetter valueSetter = new PreparedStatementSetter(pstmt);
                valueSetter.setStatementValues(fieldDetailsMap);
                pstmt.executeUpdate();
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                int id = 0;
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt("id");
                }
                connection.commit();

                return id;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }
        return null;
    }

}
