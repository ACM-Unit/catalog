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
                Modification modification = new Modification(car, rs.getString("m.name"), rs.getString("m.enginetype"), rs.getString("m.enginemodel"), rs.getString("m.enginecapacity"), rs.getString("m.power"), rs.getString("m.drive"), rs.getString("m.date"), rs.getString("m.reference"), rs.getInt("m.id"), rs.getString("m.description"), rs.getString("m.startYear"), rs.getString("m.endYear"), rs.getString("m.bodyType"), rs.getString("m.driveType"),
                        rs.getString("m.capacity"), rs.getString("m.capacityTax"), rs.getString("m.capacityTech"), rs.getString("m.fuelMixture"), rs.getString("m.fuelType"),
                        rs.getString("m.numberOfCylinders"), rs.getString("m.numberOfValves"), rs.getString("m.constructionInterval"), rs.getString("m.engineCode"), rs.getString("m.startYearMonth"), rs.getString("m.endYearMonth"));
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
    public List<Modification> getAllExceptFilled() {
        return null;
    }

    @Override
    public Modification save(Modification modification) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO autoplus.modification (parent, name, enginetype, enginemodel, enginecapacity, power, drive, date, reference, description, startYear, endYear, bodyType, driveType, capacity, capacityTax, capacityTech, fuelMixture, fuelType, numberOfCylinders, numberOfValves, constructionInterval, engineCode, startYearMonth, endYearMonth) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
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
            ps.setInt(10, modification.getId());
            ps.setString(11, modification.getDescription());
            ps.setString(12, modification.getStartYear());
            ps.setString(13, modification.getEndYear());
            ps.setString(14, modification.getBodyType());
            ps.setString(15, modification.getDriveType());
            ps.setString(16, modification.getCapacity());
            ps.setString(17, modification.getCapacityTax());
            ps.setString(18, modification.getCapacityTech());
            ps.setString(19, modification.getFuelMixture());
            ps.setString(20, modification.getFuelType());
            ps.setString(21, modification.getNumberOfCylinders());
            ps.setString(22, modification.getNumberOfValves());
            ps.setString(23, modification.getConstructionInterval());
            ps.setString(24, modification.getEngineCode());
            ps.setString(25, modification.getStartYearMonth());
            ps.setString(26, modification.getEndYearMonth());
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
    public void update(String reference, String[] params) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE autoplus.modification set id = ?, description = ?, startYear = ?, endYear = ?, bodyType = ?, driveType = ?, capacity = ?, capacityTax = ?, capacityTech = ?, engineType = ?, fuelMixture = ?, fuelType = ?, numberOfCylinders = ?, numberOfValves = ?, power = ?, constructionInterval = ?, engineCode = ?, startYearMonth = ?, endYearMonth = ? where reference = ?",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, Integer.valueOf(params[0]));
            ps.setString(2, params[1]);
            ps.setString(3, params[2]);
            ps.setString(4, params[3]);
            ps.setString(5, params[4]);
            ps.setString(6, params[5]);
            ps.setString(7, params[6]);
            ps.setString(8, params[7]);
            ps.setString(9, params[8]);
            ps.setString(10, params[9]);
            ps.setString(11, params[10]);
            ps.setString(12, params[11]);
            ps.setString(13, params[12]);
            ps.setString(14, params[13]);
            ps.setString(15, params[14]);
            ps.setString(16, params[15]);
            ps.setString(17, params[16]);
            ps.setString(18, params[17]);
            ps.setString(19, params[18]);
            ps.setString(20, reference);
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
