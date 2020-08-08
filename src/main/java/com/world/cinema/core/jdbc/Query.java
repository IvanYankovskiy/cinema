package com.world.cinema.core.jdbc;

import com.world.cinema.core.jdbc.fields.ConditionalFieldDetails;
import com.world.cinema.core.jdbc.fields.FieldDetails;
import com.world.cinema.core.jdbc.fields.IdFieldDetails;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

public class Query {

    private final String sql;

    private final Collection<FieldDetails> nonConditionalFields;

    private final Collection<ConditionalFieldDetails> conditionalFields;

    public Query(String sql, Collection<FieldDetails> nonConditionalFields, Collection<ConditionalFieldDetails> conditionalFields) {
        this.sql = sql;
        this.nonConditionalFields = nonConditionalFields;
        this.conditionalFields = conditionalFields;
    }

    public String getSql() {
        return sql;
    }

    public Collection<FieldDetails> getNonConditionalFields() {
        return nonConditionalFields;
    }

    public Collection<ConditionalFieldDetails> getConditionalFields() {
        return conditionalFields;
    }

    public void setValuesToPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
        for (FieldDetails fieldDetail : nonConditionalFields) {
            setParameterToPreparedStatement(fieldDetail, preparedStatement);
        }
    }

    private void setParameterToPreparedStatement(FieldDetails fieldDetails, PreparedStatement preparedStatement) throws SQLException {
        Class<?> type = fieldDetails.getClazz();
        Object value = fieldDetails.getValue();
        if (fieldDetails instanceof IdFieldDetails && Objects.isNull(value)) {
            return;
        }
        preparedStatement.setObject(fieldDetails.getStatementIndex(), (type.cast(value)));
    }
}
