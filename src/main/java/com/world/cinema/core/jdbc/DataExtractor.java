package com.world.cinema.core.jdbc;

import com.world.cinema.core.jdbc.annotations.ColumnName;
import com.world.cinema.core.jdbc.annotations.TableName;
import com.world.cinema.core.jdbc.exception.TableNameNotSupportedException;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

public class DataExtractor {

    public Map<String, FieldDetails> extractFieldNamesAndValues(Object entity) throws IllegalAccessException {
        TreeMap<String, FieldDetails> fieldsDetails = new TreeMap<>();
        Class<?> entityClass = entity.getClass();
        for (Field declaredField : entityClass.getDeclaredFields()) {
            declaredField.setAccessible(true);
            if (declaredField.isAnnotationPresent(ColumnName.class)) {
                ColumnName annotation = declaredField.getAnnotation(ColumnName.class);
                FieldDetails fieldDetail = new FieldDetails();
                fieldDetail.setType(declaredField.getType());
                fieldDetail.setValue(declaredField.get(entity));
                fieldsDetails.put(annotation.value(), fieldDetail);
            }
        }
        return fieldsDetails;
    }

    public String extractTableName(Object entity) {
        Class<?> entityClass = entity.getClass();
        if (entityClass.isAnnotationPresent(TableName.class)) {
            TableName annotation = entityClass.getAnnotation(TableName.class);
            return annotation.value();
        } else
            throw new TableNameNotSupportedException(entityClass);
    }

}
