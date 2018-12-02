package com.autoplus;

import com.autoplus.dbConnection.ConnectionPool;
import com.autoplus.services.CarService;
import javax.sql.DataSource;
import java.io.IOException;


import static com.autoplus.Constants.CACHE_SOCKET;

public class App{
    private static final String ATTRIBUTE_NAME = "config";
    private static DataSource dataSource;
    private static ConnectionPool jdbcObj;
    public static volatile CarService carService;

    public static void main(String[] args) {
        jdbcObj = new ConnectionPool();
        try {
            dataSource = jdbcObj.setUpPool();
            jdbcObj.printDbStatus();
            carService = new CarService(dataSource);
            Thread t = new Thread(() -> {
                while(true){
                    try {
                        Thread.sleep(120*1000);
                        CACHE_SOCKET = carService.getRandomProxy();
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
            carService.getAllCars();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
