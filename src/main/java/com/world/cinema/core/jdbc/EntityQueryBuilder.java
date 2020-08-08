package com.world.cinema.core.jdbc;

import com.world.cinema.core.jdbc.fields.FieldDetails;
import com.world.cinema.core.jdbc.fields.IdFieldDetails;

import java.util.*;

public class EntityQueryBuilder {

    public static final String insertSqlTemplate = "insert into :table_name ";
    public static final String selectAllForClass = "select * from :table_name";
    public static final String selectEntityById = "select * from :table_name where :id = ?";
    public static final String selectByParameters = "select * from :table_name where :parameters";
    public static final String updateSqlById = "update :table_name set :updatable_fields where :id = ?";


    public Query buildInsertStatement(String tableName, final Collection<FieldDetails> fields) {
        String baseSql = insertSqlTemplate.replace(":table_name", tableName);
        StringBuilder fieldNamesBuilder = new StringBuilder("(");
        StringBuilder fieldValuesPlaceholderBuilder = new StringBuilder(" VALUES (");
        int numOfValues = fields.size();
        int currentIndex = 1;
        int iteration = 1;
        for (FieldDetails fieldDetails : fields) {
            fieldNamesBuilder.append(fieldDetails.getTableFieldName());
            if (fieldDetails instanceof IdFieldDetails && Objects.isNull(fieldDetails.getValue())) {
                String sequenceName = ((IdFieldDetails) fieldDetails).getSequenceName();
                fieldValuesPlaceholderBuilder.append("nextval('")
                        .append(sequenceName)
                        .append("')");
            } else {
                fieldValuesPlaceholderBuilder.append("?");
                fieldDetails.setStatementIndex(currentIndex);
            }

            if (iteration < numOfValues) {
                fieldNamesBuilder.append(",");
                fieldValuesPlaceholderBuilder.append(",");
            }
            iteration++;
            if (fieldDetails instanceof IdFieldDetails && Objects.isNull(fieldDetails.getValue()))
                continue;
            currentIndex++;
        }
        fieldNamesBuilder.append(")");
        fieldValuesPlaceholderBuilder.append(")");
        String finalSql = baseSql
                .concat(fieldNamesBuilder.toString())
                .concat(fieldValuesPlaceholderBuilder.toString());
        return new Query(finalSql, fields, new ArrayList<>());
    }

}
