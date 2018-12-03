package com.autoplus.dao;

import com.autoplus.entity.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class CategoryDao implements Dao<Category> {
    private DataSource dataSource;
    @Override
    public Category get(long id) {
        return null;
    }

    @Override
    public List<Category> getAll() {
        return null;
    }

    public CategoryDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Category save(Category category) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO autoplus.category (id, name, parent_id, reference) VALUES (NULL, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, category.getTitle());
            ps.setInt(2, category.getParent().getId());
            ps.setString(3, category.getUrl());
            int i = ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    category.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating category failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, null, null);
        }
        return category;
    }
    @Override
    public void update(Category category, String[] params) {
    }
    @Override
    public void delete(Category category) {
    }

    @Override
    public int[] getLast() {
        return new int[0];
    }

    @Override
    public Category getLastObject() {
        return null;
    }

    @Override
    public List<String> getLastModels(Category category) {
        return null;
    }

    @Override
    public List<String> getLastTypes(Category category) {
        return null;
    }

    @Override
    public List<String> getLastModification(int parent) {
        return null;
    }

    @Override
    public Category getOne(String brand, String model, String type) {
        return null;
    }
}
