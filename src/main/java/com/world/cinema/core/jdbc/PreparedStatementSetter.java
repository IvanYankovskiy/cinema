package com.world.cinema.core.jdbc;

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
        int index = 0;
        for (FieldDetails field : fieldDetailsMap.values()) {
            getTypeAndSetValue(index, field);
            index++;
        }
    }

    private void getTypeAndSetValue(int parameterIndex, FieldDetails fieldDetails) throws SQLException {
        Class type = fieldDetails.getClazz();
        Object value = fieldDetails.getValue();
        if (fieldDetails instanceof IdFieldDetails) {
            if (Objects.isNull(value))
                return;

        }
        this.pstmt.setObject(parameterIndex, (type.cast(value)));
    }
}
