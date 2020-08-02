package com.world.cinema.core.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Slf4j
public class BaseDAO {

    private DataSource dataSource;

    private DataExtractor dataExtractor;

    @Autowired
    public BaseDAO(DataSource dataSource, DataExtractor dataExtractor) {
        this.dataSource = dataSource;
        this.dataExtractor = dataExtractor;
    }

    public <T> Integer insert(T entity) throws IllegalAccessException {
        String tableName = dataExtractor.extractTableName(entity);
        Map<String, FieldDetails> fieldDetailsMap = dataExtractor.extractFieldNamesAndValues(entity);
        StatementBuilder stmntBuilder = new StatementBuilder();
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
        StatementBuilder stmntBuilder = new StatementBuilder();
        String sql = stmntBuilder.buildInsertStatement(tableName, filedDetailsCollection.get(0));
        return performBatchModificationQuery(filedDetailsCollection, sql);
    }

    public <T> List<T> selectAll(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        String tableName = dataExtractor.extractTableNameFromClass(clazz);
        StatementBuilder statementBuilder = new StatementBuilder();
        String sql = statementBuilder.buildSelectAllStatement(tableName);
        return selectAll(sql, clazz);
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

    private <T> List<T> selectAll(String sql, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        List<T> queryResults = new ArrayList<>();
        try(Connection connection = dataSource.getConnection()) {
            try (Statement pstmt = connection.createStatement()) {
                ResultSet resultSet = pstmt.executeQuery(sql);
                while (resultSet.next()) {
                    T entity = dataExtractor.createEntityFromResult(clazz, resultSet);
                    queryResults.add(entity);
                }
                return queryResults;
            }
        } catch (SQLException throwables) {
            log.error("Error during selecting all", throwables);
        }
        return null;
    }

}
