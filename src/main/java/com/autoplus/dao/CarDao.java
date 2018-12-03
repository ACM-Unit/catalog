package com.autoplus.dao;

import com.autoplus.entity.Car;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDao implements Dao<Car> {
    private DataSource dataSource;

    public CarDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Car get(long id) {
        return null;
    }
    @Override
    public Car getOne(String brand, String model, String type) {
        Connection connection = null;
        Car car =null;
        try {
            String query;
            if(type==null){
                query = "select * from car where brand = ? and model = ? and type is null";
            }else{
                query = "select * from car where brand = ? and model = ? and type = ?";
            }
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, brand);
            ps.setString(2, model);
            if(type!=null) ps.setString(3, type);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                car = new Car(rs.getString("brand"), rs.getString("model"), rs.getString("type"), rs.getString("reference"), rs.getString("year"));
                car.setId(rs.getInt("id"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, null, null);
        }
        return car;
    }

    @Override
    public List<Car> getAll() {
        Connection connection = null;
        List<Car> cars = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("select * from car");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Car car = new Car(rs.getString("brand"), rs.getString("model"), rs.getString("type"), rs.getString("reference"), rs.getString("year"));
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, null, null);
        }
        return cars;
    }

    @Override
    public Car save(Car car) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO autoplus.car (id, brand, model, type, reference, year) VALUES (NULL, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, car.getBrand());
            ps.setString(2, car.getModel());
            ps.setString(3, car.getType());
            ps.setString(4, car.getReference());
            ps.setString(5, car.getYear());
            int i = ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    car.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating category failed, no ID obtained.");
                }
            }
        }
        catch (MySQLIntegrityConstraintViolationException e) {
            System.out.println("dublicate");
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, null, null);
        }
        return car;
    }

    @Override
    public void update(Car car, String[] params) {

    }

    @Override
    public void delete(Car car) {

    }

    @Override
    public int[] getLast() {
        Connection connection = null;
        int[] lastCar = new int[3];
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("select (select count(distinct brand) from car) last_brand, \n" +
                            "       (select count(distinct model) from car where brand = (select brand from car order by id desc LIMIT 1 )) as last_model,\n" +
                            "       (select count(distinct type) from car where model = (select model from car where brand = (select brand from car order by id desc LIMIT 1 ) order by id desc LIMIT 1) and brand = (select brand from car order by id desc LIMIT 1 )) as last_modify \n" +
                            "from dual");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lastCar[0] = rs.getInt("last_brand");
                lastCar[1] = rs.getInt("last_model");
                lastCar[2] = rs.getInt("last_modify");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, null, null);
        }
        return lastCar;
    }

    @Override
    public Car getLastObject() {
        Connection connection = null;
        Car lastCar = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM car ORDER BY id DESC LIMIT 1;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lastCar = new Car(rs.getString("brand"), rs.getString("model"), rs.getString("type"), rs.getString("reference"), rs.getString("year"));
                lastCar.setId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, null, null);
        }
        System.out.println(lastCar);
        return lastCar;
    }
    @Override
    public List<String> getLastModels(Car car) {
        Connection connection = null;
        List<String> list = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM car WHERE brand = ?");
            ps.setString(1, car.getBrand());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String model = rs.getString("model");
                list.add(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, null, null);
        }
        return list;
    }

    @Override
    public List<String> getLastTypes(Car car) {
        Connection connection = null;
        List<String> list = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM car WHERE brand = ? and model = ?");
            ps.setString(1, car.getBrand());
            ps.setString(2, car.getModel());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String type = rs.getString("type");
                list.add(type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, null, null);
        }
        return list;
    }

    @Override
    public List<String> getLastModification(int parent) {
        return null;
    }
}
