package com.world.cinema.core.jdbc;

import com.world.cinema.core.jdbc.fields.FieldDetails;
import com.world.cinema.core.jdbc.fields.IdFieldDetails;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

public class PreparedStatementSetter {

    PreparedStatement pstmt;

    public PreparedStatementSetter(PreparedStatement pstmt) {
        this.pstmt = pstmt;
    }

    public void setStatementValues(Map<String, FieldDetails> fieldDetailsMap) throws SQLException {
        int index = 1;
        for (FieldDetails field : fieldDetailsMap.values()) {
            getTypeAndSetValue(index, field);
            if (field instanceof IdFieldDetails && Objects.isNull(field.getValue()))
                continue;
            index++;
        }
    }

    private void getTypeAndSetValue(int parameterIndex, FieldDetails fieldDetails) throws SQLException {
        Class<?> type = fieldDetails.getClazz();
        Object value = fieldDetails.getValue();
        if (fieldDetails instanceof IdFieldDetails && Objects.isNull(value)) {
            return;
        }
        this.pstmt.setObject(parameterIndex, (type.cast(value)));
    }
}
