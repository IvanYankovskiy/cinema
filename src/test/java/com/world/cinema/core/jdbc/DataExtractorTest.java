package com.world.cinema.core.jdbc;

import com.world.cinema.core.jdbc.annotations.ColumnName;
import com.world.cinema.core.jdbc.annotations.TableName;
import com.world.cinema.core.jdbc.exception.TableNameNotSupportedException;
import com.world.cinema.domain.CinemaHall;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DataExtractorTest {

    private DataExtractor dataExtractor = new DataExtractor();

    @Test
    void test_extractFieldNamesAndValues_whenCorrectPOJO_thenFullMap() throws IllegalAccessException {
        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setId(1);
        cinemaHall.setName("Main hall");
        Set<String> expectedFieldNames = new HashSet<>();
        Set<FieldDetails> expectedFieldDetails = new HashSet<>();
        expectedFieldDetails.add(new FieldDetails(cinemaHall.getId(), Integer.class));
        expectedFieldDetails.add(new FieldDetails(cinemaHall.getName(), String.class));

        Class<?> hallClass = cinemaHall.getClass();
        for (Field declaredField : hallClass.getDeclaredFields()) {
            declaredField.setAccessible(true);
            if (declaredField.isAnnotationPresent(ColumnName.class)) {
                ColumnName annotation = declaredField.getAnnotation(ColumnName.class);
                expectedFieldNames.add(annotation.value());
            }
        }

        Map<String, FieldDetails> entityFieldsDetailsMap = dataExtractor.extractFieldNamesAndValues(cinemaHall);
        assertNotNull(entityFieldsDetailsMap);
        assertEquals(expectedFieldNames, entityFieldsDetailsMap.keySet());
        Collection<FieldDetails> values = entityFieldsDetailsMap.values();
        assertEquals(expectedFieldDetails, new HashSet<>(values));
    }

    @Test
    void test_extractTableName_whenAnnotationIsPresentThen_returnStringTableName() {
        TestEntity testEntity = new TestEntity();
        String s = dataExtractor.extractTableName(testEntity);
        assertEquals("table_name", s);
    }

    @Test
    void test_extractTableName_whenNoAnnotationThenThrowTableNameNotSupportedException() {
        NonAnnotatedClass testEntity = new NonAnnotatedClass();
        Assertions.assertThrows(TableNameNotSupportedException.class, () -> {
            dataExtractor.extractTableName(testEntity);
        });
    }


    @TableName("table_name")
    class TestEntity {

        private Integer id;

    }

    class NonAnnotatedClass {

    }
}