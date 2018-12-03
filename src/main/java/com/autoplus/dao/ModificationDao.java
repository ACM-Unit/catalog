package com.autoplus.dao;

import com.autoplus.entity.Car;
import com.autoplus.entity.Modification;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import javax.sql.DataSource;
import java.sql.*;
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
                Modification modification = new Modification(car, rs.getString("m.name"), rs.getString("m.enginetype"), rs.getString("m.enginemodel"), rs.getString("m.enginecapacity"), rs.getString("m.power"), rs.getString("m.drive"), rs.getString("m.date"), rs.getString("m.reference"));
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
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO autoplus.modification (parent, name, enginetype, enginemodel, enginecapacity, power, drive, date, reference) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, modification.getParent().getId());
            ps.setString(2, modification.getName());
            ps.setString(3, modification.getEngineType());
            ps.setString(4, modification.getEngineModel());
            ps.setString(5, modification.getEngineCapacity());
            ps.setString(6, modification.getPower());
            ps.setString(7, modification.getDrive());
            ps.setString(8, modification.getDate());
            ps.setString(9, modification.getReference());
            ps.executeUpdate();

        }
        catch (MySQLIntegrityConstraintViolationException e) {
            System.out.println("dublicate");
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, null, null);
        }
        return modification;
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
            PreparedStatement ps = connection.prepareStatement("SELECT name FROM modification WHERE parent = ?");
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

    @Override
    public Modification getOne(String brand, String model, String type) {
        return null;
    }
}
