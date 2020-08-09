package com.world.cinema.core.jdbc;

import com.world.cinema.core.jdbc.fields.FieldDetails;
import com.world.cinema.core.jdbc.fields.IdFieldDetails;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

class EntityQueryBuilderTest {

    private EntityQueryBuilder builder;

    @BeforeEach
    public void init() {
        this.builder = new EntityQueryBuilder();
    }

    @Test
    void test_buildInsertStatement_withSequence() {
        String tableName = "TICKET";
        List<FieldDetails> fields = new ArrayList<>();
        fields.add(new IdFieldDetails("id", null, Integer.class, "sequence_name"));
        fields.add(new FieldDetails("movie", "Inception", String.class) );
        fields.add(new FieldDetails("date", LocalDate.now(), LocalDate.class));
        fields.add(new FieldDetails("cost", 10, Integer.class));

        String expectedSql = "insert into TICKET (id,movie,date,cost) VALUES (nextval('sequence_name'),?,?,?)";

        //when
        Query query = builder.buildInsert(tableName, fields);
        Assertions.assertNotNull(query);
        Assert.assertEquals(expectedSql, query.getSql());
        Collection<FieldDetails> nonConditionalFields = query.getNonConditionalFields();
        Assert.assertTrue(nonConditionalFields.stream()
                .anyMatch(fieldDetails -> {
                    return fieldDetails.getFieldNameAsInDb().equals("id")
                            && Objects.isNull(fieldDetails.getStatementIndex());
                }));
        Assert.assertTrue(nonConditionalFields.stream()
                .anyMatch(fieldDetails -> {
                    return fieldDetails.getFieldNameAsInDb().equals("movie")
                            && Integer.valueOf(1).equals(fieldDetails.getStatementIndex());
                }));
        Assert.assertTrue(nonConditionalFields.stream()
                .anyMatch(fieldDetails -> {
                    return fieldDetails.getFieldNameAsInDb().equals("date")
                            && Integer.valueOf(2).equals(fieldDetails.getStatementIndex());
                }));
        Assert.assertTrue(nonConditionalFields.stream()
                .anyMatch(fieldDetails -> {
                    return fieldDetails.getFieldNameAsInDb().equals("cost")
                            && Integer.valueOf(3).equals(fieldDetails.getStatementIndex());
                }));
    }

    @Test
    void test_buildInsertStatement_withSequence_twoFields() {
        String tableName = "TICKET";
        List<FieldDetails> fields = new ArrayList<>();
        fields.add(new IdFieldDetails("id", null, Integer.class, "sequence_name"));
        fields.add(new FieldDetails("movie", "Inception", String.class) );

        String expectedSql = "insert into TICKET (id,movie) VALUES (nextval('sequence_name'),?)";

        //when
        Query query = builder.buildInsert(tableName, fields);
        Assertions.assertNotNull(query);
        Assert.assertEquals(expectedSql, query.getSql());
        Collection<FieldDetails> nonConditionalFields = query.getNonConditionalFields();
        Assert.assertTrue(nonConditionalFields.stream()
                .anyMatch(fieldDetails -> {
                    return fieldDetails.getFieldNameAsInDb().equals("id")
                            && Objects.isNull(fieldDetails.getStatementIndex());
                }));
        Assert.assertTrue(nonConditionalFields.stream()
                .anyMatch(fieldDetails -> {
                    return fieldDetails.getFieldNameAsInDb().equals("movie")
                            && Integer.valueOf(1).equals(fieldDetails.getStatementIndex());
                }));
    }


}