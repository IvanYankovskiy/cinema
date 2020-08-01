package com.world.cinema.core.jdbc;

import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class BaseDao {

    private DataSource dataSource;

    private DataExtractor dataExtractor;

    private final String insertSqlTemplate = "insert into :table_name ";

    @Autowired
    public BaseDao(DataSource dataSource, DataExtractor dataExtractor) {
        this.dataSource = dataSource;
        this.dataExtractor = dataExtractor;
    }

    public void executeInsert(String sql, Object entity) throws IllegalAccessException {
        String tableName = dataExtractor.extractTableName(entity);
        Map<String, FieldDetails> fieldDetailsMap = dataExtractor.extractFieldNamesAndValues(entity);
        try(Connection connection = dataSource.getConnection()) {
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {


            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String buildInsertStatement(String tableName, Map<String, FieldDetails> fields) {
        String baseSql = insertSqlTemplate.replace(":table_name", tableName);
        StringBuilder sb = new StringBuilder(baseSql);
        sb.append("(");
        sb.append(String.join(",", fields.keySet()));
        sb.append(")");
        return sb.toString();
    }

}
