package com.world.cinema;

import com.world.cinema.core.jdbc.DataExtractor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TableCleaner {

    private DataSource dataSource;

    private DataExtractor dataExtractor;

    @Autowired
    public TableCleaner(DataSource dataSource, DataExtractor dataExtractor) {
        this.dataSource = dataSource;
        this.dataExtractor = dataExtractor;
    }

    /**
     * Created for cleaning of data in tables before each test with containerized <b>shared<b/> database instance.
     * Should be extended with ability to pass string table names.
     * @param entityClasses - list of entityClasses annotated by {@link com.world.cinema.core.jdbc.annotations.TableName}
     */
    public void truncateAllTables(Class<?> ... entityClasses) {
        List<Class<?>> entityClassesList = Arrays.stream(entityClasses).collect(Collectors.toList());
        Set<String> tableNames = new HashSet<>();
        for (Class clazz : entityClassesList) {
            tableNames.add(dataExtractor.extractTableNameFromClass(clazz));
        }
        String tablesString = String.join(",", tableNames);
        String sql = "TRUNCATE " + tablesString + " RESTART IDENTITY CASCADE";
        try(Connection conn = dataSource.getConnection();) {
            try (Statement stmnt = conn.createStatement()) {
                stmnt.execute(sql);
                conn.commit();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
