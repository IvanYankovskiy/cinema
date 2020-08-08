package com.world.cinema.core.jdbc;

import com.world.cinema.core.jdbc.fields.ConditionalFieldDetails;
import com.world.cinema.core.jdbc.fields.FieldDetails;
import com.world.cinema.core.jdbc.fields.IdFieldDetails;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StatementBuilder {

    public static final String insertSqlTemplate = "insert into :table_name ";
    public static final String selectAllForClass = "select * from :table_name";
    public static final String selectEntityById = "select * from :table_name where :id = ?";
    public static final String selectByParameters = "select * from :table_name where :parameters";
    public static final String updateSqlById = "update :table_name set :updatable_fields where :id = ?";


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

    public String buildSelectByIdStatement(String tableName, String idFieldName) {
        return selectEntityById.replace(":table_name", tableName)
                .replace(":id", idFieldName);
    }

    public String buildSelectByParametersConnectedByAnd(String tableName, List<ConditionalFieldDetails> conditionalFields) {
        StringBuilder sb = new StringBuilder();
        int numOfValues = conditionalFields.size();
        int currentIndex = 0;
        for (ConditionalFieldDetails fieldDetails : conditionalFields) {
            sb.append(fieldDetails.getTableFieldName());
            sb.append(fieldDetails.getSign());
            sb.append("?");
            if (currentIndex < numOfValues -1)
                sb.append(" and ");
            currentIndex++;
        }
        return selectByParameters.replace(":table_name", tableName)
                .replace(":parameters", sb.toString());

    }


    public String buildUpdateById(String tableName, Map<String, FieldDetails> fields) {
        String baseSql = updateSqlById.replace(":table_name", tableName);
        StringBuilder sb = new StringBuilder(baseSql);
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





}
