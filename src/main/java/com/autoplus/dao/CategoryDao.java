package com.autoplus.dao;

import com.autoplus.entity.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao implements Dao<Category> {
    private DataSource dataSource;
    @Override
    public Category get(long id) {
        return null;
    }

    @Override
    public List<Category> getAll() {
        Connection connection = null;
        List<Category> list = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM category c, category p where c.parent = p.id");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category p = new Category(rs.getString("p.title"), rs.getString("p.reference"), null);
                p.setId(rs.getInt("p.id"));
                Category c = new Category(rs.getString("c.title"), rs.getString("c.reference"), p);
                c.setId(rs.getInt("c.id"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, null, null);
        }
        return list;
    }

    public CategoryDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Category save(Category category) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO autoplus.category (id, title, reference, parent) VALUES (NULL, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, category.getTitle());
            ps.setString(3, category.getReference());
            ps.setInt(2, category.getParent().getId());
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
