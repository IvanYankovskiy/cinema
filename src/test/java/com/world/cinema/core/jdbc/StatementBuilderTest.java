package com.world.cinema.core.jdbc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class StatementBuilderTest {

    @InjectMocks
    private StatementBuilder statementBuilder;

    @Test
    public void test_buildInsertStatement() {
        String tableName = "TICKET";
        Map<String, FieldDetails> fields = new TreeMap<>();
        fields.put("movie", new FieldDetails("Inception", String.class) );
        fields.put("date", new FieldDetails(LocalDate.now(), LocalDate.class));
        fields.put("cost", new FieldDetails(10, Integer.class));

        String expected = "insert into TICKET (" + String.join(",", fields.keySet()) +") VALUES (?,?,?)";

        //when
        String result = statementBuilder.buildInsertStatement(tableName, fields);

        //then
        Assertions.assertEquals(expected, result);
    }

    /**
     * This is the dirtiest implementation. Of course there must be a special class, where I can order keys in ArrayList
     * and FieldDetails in ArrayList with 1 to 1 matching, but it's too late now :)
     */
    @Test
    public void test_buildInsertStatement_withIdField_sequenceProvided() {
        String tableName = "TICKET";
        Map<String, FieldDetails> fields = new TreeMap<>();
        fields.put("id", new IdFieldDetails(null, Integer.class, "movie_id_seq"));
        fields.put("movie", new FieldDetails("Inception", String.class) );
        fields.put("date", new FieldDetails(LocalDate.now(), LocalDate.class));
        fields.put("cost", new FieldDetails(10, Integer.class));

        Iterator<String> keyIterator = fields.keySet().iterator();
        StringBuilder fieldNameSB = new StringBuilder();
        StringBuilder valuePlaceHoldersSB = new StringBuilder();
        while (keyIterator.hasNext()) {
            String nextName = keyIterator.next();
            fieldNameSB.append(nextName);
            FieldDetails fieldDetails = fields.get(nextName);
            if (fieldDetails instanceof IdFieldDetails) {
                IdFieldDetails idFieldDetails = (IdFieldDetails) fieldDetails;
                if (Objects.isNull(idFieldDetails.getValue())) {
                    valuePlaceHoldersSB.append("nextval('" + idFieldDetails.getSequenceName() + "')");
                } else {
                    valuePlaceHoldersSB.append("?");
                }
            } else
                valuePlaceHoldersSB.append("?");
            if (keyIterator.hasNext()) {
                fieldNameSB.append(",");
                valuePlaceHoldersSB.append(",");
            }
        }
        String expected = "insert into TICKET (" + fieldNameSB.toString() + ") VALUES ("
                + valuePlaceHoldersSB.toString() + ")";

        //when
        String result = statementBuilder.buildInsertStatement(tableName, fields);

        //then
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void test_buildInsertStatement_withIdField_idIsProvided() {
        String tableName = "TICKET";
        Map<String, FieldDetails> fields = new TreeMap<>();
        fields.put("id", new IdFieldDetails(12, Integer.class, "movie_id_seq"));
        fields.put("movie", new FieldDetails("Inception", String.class) );
        fields.put("date", new FieldDetails(LocalDate.now(), LocalDate.class));
        fields.put("cost", new FieldDetails(10, Integer.class));

        Iterator<String> keyIterator = fields.keySet().iterator();
        StringBuilder fieldNameSB = new StringBuilder();
        StringBuilder valuePlaceHoldersSB = new StringBuilder();
        while (keyIterator.hasNext()) {
            String nextName = keyIterator.next();
            fieldNameSB.append(nextName);
            FieldDetails fieldDetails = fields.get(nextName);
            if (fieldDetails instanceof IdFieldDetails) {
                IdFieldDetails idFieldDetails = (IdFieldDetails) fieldDetails;
                if (Objects.isNull(idFieldDetails.getValue())) {
                    valuePlaceHoldersSB.append("nextval('" + idFieldDetails.getSequenceName() + "')");
                } else {
                    valuePlaceHoldersSB.append("?");
                }
            } else
                valuePlaceHoldersSB.append("?");
            if (keyIterator.hasNext()) {
                fieldNameSB.append(",");
                valuePlaceHoldersSB.append(",");
            }
        }
        String expected = "insert into TICKET (" + fieldNameSB.toString() + ") VALUES ("
                + valuePlaceHoldersSB.toString() + ")";

        //when
        String result = statementBuilder.buildInsertStatement(tableName, fields);

        //then
        Assertions.assertEquals(expected, result);
    }
}