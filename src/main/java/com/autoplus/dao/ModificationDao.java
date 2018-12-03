package com.autoplus.dao;

import com.autoplus.entity.Car;
import com.autoplus.entity.Modification;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 02.12.2018.
 */
public class ModificationDao implements Dao<Modification> {

    private DataSource dataSource;

    public ModificationDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Modification get(long id) {
        return null;
    }

    @Override
    public List<Modification> getAll() {
        Connection connection = null;
        List<Modification> modifications = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("select * from car c, modification m where m.parent = c.id");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Car car = new Car(rs.getString("c.brand"), rs.getString("c.model"), rs.getString("c.type"), rs.getString("c.reference"), rs.getString("c.year"));
                Modification modification = new Modification(car, rs.getString("m.name"), rs.getString("m.enginetype"), rs.getString("m.enginemodel"), rs.getString("m.enginecapacity"), rs.getString("m.power"), rs.getString("m.drive"), rs.getString("m.date"));
                modifications.add(modification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, null, null);
        }
        return modifications;
    }

    @Override
    public Modification save(Modification modification) {
        return null;
    }

    @Override
    public void update(Modification modification, String[] params) {

    }

    @Override
    public void delete(Modification modification) {

    }

    @Override
    public int[] getLast() {
        return new int[0];
    }

    @Override
    public Modification getLastObject() {
        return null;
    }

    @Override
    public List<String> getLastModels(Modification modification) {
        return null;
    }

    @Override
    public List<String> getLastTypes(Modification modification) {
        return null;
    }

    @Override
    public List<String> getLastModification(int parent) {
        Connection connection = null;
        List<String> list = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM modification WHERE parent = ?");
            ps.setInt(1, parent);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                list.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, null, null);
        }
        return list;
    }
}
