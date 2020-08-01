package com.world.cinema.core.jdbc;

import java.sql.PreparedStatement;

public class PreparedStatementSetter {

    PreparedStatement pstmt;

    public PreparedStatementSetter(PreparedStatement pstmt) {
        this.pstmt = pstmt;
    }

}
