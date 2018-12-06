package com.autoplus;

import com.autoplus.dbConnection.ConnectionPool;
import com.autoplus.services.CarService;
import com.autoplus.services.CategoryService;

import javax.sql.DataSource;

public class App {
    private static final String ATTRIBUTE_NAME = "config";
    private static DataSource dataSource;
    private static ConnectionPool jdbcObj;
    public static volatile CarService carService;
    public static volatile CategoryService service;

    public static void main(String[] args) throws Exception {
        jdbcObj = new ConnectionPool();
        dataSource = jdbcObj.setUpPool();
        jdbcObj.printDbStatus();
        service = new CategoryService(dataSource);
        carService = new CarService(dataSource);
        service.getScript();
        /*jdbcObj = new ConnectionPool();
        try {
            dataSource = jdbcObj.setUpPool();
            jdbcObj.printDbStatus();
            carService = new CarService(dataSource);
            service = new CategoryService(dataSource);
            Thread t = new Thread(() -> {
                while(true){
                    try {
                        Thread.sleep(120*1000);
                        CACHE_SOCKET = service.getRandomProxy();
                        System.out.println("change socket: " + CACHE_SOCKET.getIp()+":"+CACHE_SOCKET.getPort());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.setDaemon(true);
            t.start();
            service.getAllCategories();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

}
