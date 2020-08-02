package com.world.cinema.core.jdbc;

import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

public class BaseDAO {

    private DataSource dataSource;

    private DataExtractor dataExtractor;

    public static final String insertSqlTemplate = "insert into :table_name ";

    @Autowired
    public BaseDAO(DataSource dataSource, DataExtractor dataExtractor) {
        this.dataSource = dataSource;
        this.dataExtractor = dataExtractor;
    }

    public Integer insert(Object entity) throws IllegalAccessException {
        String tableName = dataExtractor.extractTableName(entity);
        Map<String, FieldDetails> fieldDetailsMap = dataExtractor.extractFieldNamesAndValues(entity);
        String sql = buildInsertStatement(tableName, fieldDetailsMap);
        Integer generatedKey = performModificationQuery(fieldDetailsMap, sql);
        if (generatedKey != null) return generatedKey;

        return null;
    }

    public String buildInsertStatement(String tableName, Map<String, FieldDetails> fields) {
        String baseSql = insertSqlTemplate.replace(":table_name", tableName);
        StringBuilder sb = new StringBuilder(baseSql);
        sb.append("(");
        sb.append(String.join(",", fields.keySet()));
        sb.append(")");
        sb.append(" VALUES (");
        int numOfValues = fields.values().size();
        int currentIndex = 0;
        for (FieldDetails fieldDetails : fields.values()) {
            if (fieldDetails instanceof IdFieldDetails) {
                String sequenceName = ((IdFieldDetails) fieldDetails).getSequenceName();
                sb.append(Objects.isNull(sequenceName) || sequenceName.isEmpty() ? "?" : "nextval('" + sequenceName + "')");
            } else
                sb.append("?");

            if (currentIndex < numOfValues -1)
                sb.append(",");
            currentIndex++;
        }
        sb.append(")");
        return sb.toString();
    }

    private Integer performModificationQuery(Map<String, FieldDetails> fieldDetailsMap, String sql) {
        try(Connection connection = dataSource.getConnection()) {
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                PreparedStatementSetter valueSetter = new PreparedStatementSetter(pstmt);
                valueSetter.setStatementValues(fieldDetailsMap);
                int generatedKey = pstmt.executeUpdate();
                connection.commit();
                return generatedKey;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

}
