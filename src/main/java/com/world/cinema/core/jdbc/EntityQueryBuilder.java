package com.world.cinema.core.jdbc;

import com.world.cinema.core.jdbc.fields.ConditionalFieldDetails;
import com.world.cinema.core.jdbc.fields.FieldDetails;
import com.world.cinema.core.jdbc.fields.IdFieldDetails;

import java.util.*;

public class EntityQueryBuilder {

    private DataExtractor dataExtractor = new DataExtractor();

    public static final String TABLE_NAME = ":table_name";
    public static final String insertSqlTemplate = "insert into :table_name ";
    public static final String selectSqlByIds = "select * from :table_name where :id_field_name in (:ids)";

    public Query buildInsert(String tableName, final Collection<FieldDetails> fields) {
        String baseSql = insertSqlTemplate.replace(TABLE_NAME, tableName);
        StringBuilder fieldNamesBuilder = new StringBuilder("(");
        StringBuilder fieldValuesPlaceholderBuilder = new StringBuilder(" VALUES (");
        int numOfValues = fields.size();
        int currentIndex = 1;
        int iteration = 1;
        for (FieldDetails fieldDetails : fields) {
            fieldNamesBuilder.append(fieldDetails.getFieldNameAsInDb());
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

    public <T, I> Query buildSelectByIds(Class<T> clazz, List<I> ids) {
        String tableName = dataExtractor.extractTableNameFromClass(clazz);
        FieldDetails idColumn = dataExtractor.extractIdColumnNameFromClass(clazz);

        List<ConditionalFieldDetails> conditionalFieldDetails = prepareConditionFieldsForInPartWithoutNames(1, ids);
        String idsValuesPlaceholder = populatePlaceholdersForInConditionParameters(conditionalFieldDetails.size());
        String finalSql = selectSqlByIds
                .replace(TABLE_NAME, tableName)
                .replace(":id_field_name", idColumn.getFieldNameAsInDb())
                .replace(":ids", idsValuesPlaceholder);
        return new Query(finalSql, new ArrayList<>(), conditionalFieldDetails);
    }

    private <T> List<ConditionalFieldDetails> prepareConditionFieldsForInPartWithoutNames(int statementIndex, List<T> values) {
        List<ConditionalFieldDetails> conditionFields = new ArrayList<>();
        for (T value : values) {
            ConditionalFieldDetails conditionalFieldDetails = new ConditionalFieldDetails();
            conditionalFieldDetails.setClazz(value.getClass());
            conditionalFieldDetails.setValue(value);
            conditionalFieldDetails.setStatementIndex(statementIndex);
            conditionFields.add(conditionalFieldDetails);
            statementIndex++;
        }
        return conditionFields;
    }

    private String populatePlaceholdersForInConditionParameters(int conditionFieldsCollectionSize) {
        List<String> placeholdersQuestionMarks = new ArrayList<>();
        for (int index = 0; index < conditionFieldsCollectionSize; index++) {
            placeholdersQuestionMarks.add("?");
        }
        return String.join(",", placeholdersQuestionMarks);
    }

}
