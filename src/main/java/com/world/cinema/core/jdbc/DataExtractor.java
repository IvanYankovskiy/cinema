package com.world.cinema.core.jdbc;

import com.world.cinema.core.jdbc.annotations.ColumnName;
import com.world.cinema.core.jdbc.annotations.Id;
import com.world.cinema.core.jdbc.annotations.TableName;
import com.world.cinema.core.jdbc.exception.NoPrimaryKeyColumnException;
import com.world.cinema.core.jdbc.exception.TableNameNotSupportedException;
import com.world.cinema.core.jdbc.fields.FieldDetails;
import com.world.cinema.core.jdbc.fields.IdFieldDetails;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                FieldDetails fieldDetail;
                if (declaredField.isAnnotationPresent(Id.class)) {
                    Id idAnnotation = declaredField.getAnnotation(Id.class);
                    fieldDetail = new IdFieldDetails(idAnnotation.sequenceName());
                } else {
                    fieldDetail = new FieldDetails();
                }
                fieldDetail.setClazz(declaredField.getType());
                fieldDetail.setValue(declaredField.get(entity));


                fieldsDetails.put(annotation.value(), fieldDetail);
            }
        }
        return fieldsDetails;
    }

    public <T> T createEntityFromResult(Class<T> clazz, ResultSet rs) throws IllegalAccessException, InstantiationException, SQLException {
        T entity = clazz.newInstance();
        for (Field declaredField : clazz.getDeclaredFields()) {
            declaredField.setAccessible(true);
            if (declaredField.isAnnotationPresent(ColumnName.class)) {
                ColumnName annotation = declaredField.getAnnotation(ColumnName.class);
                String value = annotation.value();
                Class<?> entityFieldType = declaredField.getType();
                Object resultSetFieldValue = rs.getObject(value);
                if (entityFieldType.isInstance(resultSetFieldValue)) {
                    declaredField.set(entity, entityFieldType.cast(resultSetFieldValue));
                } else {
                    declaredField.set(entity, null);
                }
            }
        }
        return entity;
    }

    public String extractTableName(Object entity) {
        Class<?> entityClass = entity.getClass();
        return extractTableNameFromClass(entityClass);
    }

    public FieldDetails extractIdColumnNameFromClass(Class<?> entityClass) {
        for (Field declaredField : entityClass.getDeclaredFields()) {
            declaredField.setAccessible(true);
            if (declaredField.isAnnotationPresent(Id.class) && declaredField.isAnnotationPresent(ColumnName.class)) {
                ColumnName annotation = declaredField.getAnnotation(ColumnName.class);
                Id idAnnotation = declaredField.getAnnotation(Id.class);
                FieldDetails fieldDetail = new IdFieldDetails(idAnnotation.sequenceName());
                fieldDetail.setClazz(declaredField.getType());
                fieldDetail.setValue(null);
                fieldDetail.setFieldName(annotation.value());
                return fieldDetail;
            }
        }
        throw new NoPrimaryKeyColumnException(entityClass);
    }

    public String extractTableNameFromClass(Class<?> entityClass) {
        if (entityClass.isAnnotationPresent(TableName.class)) {
            TableName annotation = entityClass.getAnnotation(TableName.class);
            return annotation.value();
        } else
            throw new TableNameNotSupportedException(entityClass);
    }

}
