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
import java.util.Map;
import java.util.TreeMap;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class BaseDaoTest {

    @InjectMocks
    BaseDao baseDao;

    @Mock
    DataSource dataSource;

    @Mock
    DataExtractor dataExtractor;

    @Test
    public void test_buildInsertStatement() {
        String tableName = "TICKET";
        Map<String, FieldDetails> fields = new TreeMap<>();
        fields.put("movie", new FieldDetails("Inception", String.class) );
        fields.put("date", new FieldDetails(LocalDate.now(), LocalDate.class));
        fields.put("cost", new FieldDetails(10, Integer.class));

        String expected = "insert into TICKET (movie,date,cost) VALUES (?,?,?)";

        //when
        String result = baseDao.buildInsertStatement(tableName, fields);

        //then
        Assertions.assertEquals(expected, result);
    }


}