package com.autoplus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by viko0417 on 12/8/2017.
 */
public interface Dao<T> {
    T get(long id);

    List<T> getAll();

    T save(T t);

    void update(T t, String[] params);

    void delete(T t);
    default void close(Connection connection, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("SQL exception while close connection");
        }
    }
    int[] getLast();
    T getLastObject();
    List<String> getLastModels(T t);
    List<String> getLastTypes(T t);
    List<String> getLastModification(int parent);
}
