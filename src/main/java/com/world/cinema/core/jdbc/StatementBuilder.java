package com.world.cinema.core.jdbc;

import java.util.Map;
import java.util.Objects;

public class StatementBuilder {

    public static final String insertSqlTemplate = "insert into :table_name ";
    public static final String selectAllForClass = "select * from :table_name";


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
            if (fieldDetails instanceof IdFieldDetails && Objects.isNull(fieldDetails.getValue())) {
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

    public String buildSelectAllStatement(String tableName) {
        return selectAllForClass.replace(":table_name", tableName);
    }




}
