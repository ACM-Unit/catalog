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
            ps.setString(2, category.getReference());
            ps.setObject(3, category.getParent()==null?null:category.getParent().getId());
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
        Connection connection = null;
        int[] lastCar = new int[3];
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("select (select count(*) from category where parent is null) last_category,\n" +
                    "                   (select count(*) from category where parent = (select id from category where parent is null order by id desc LIMIT 1)) last_subcategory,\n" +
                    "                     (select count(*) from category where parent = (select id from category where parent = (select id from category where parent is null order by id desc LIMIT 1)  order by id desc LIMIT 1)) as last_final\n" +
                    "                     from dual");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lastCar[0] = rs.getInt("last_category")==0?0:rs.getInt("last_category")-1;
                lastCar[1] = rs.getInt("last_subcategory")==0?0:rs.getInt("last_subcategory")-1;
                lastCar[2] = rs.getInt("last_final")==0?0:rs.getInt("last_final")-1;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, null, null);
        }
        return lastCar;
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
