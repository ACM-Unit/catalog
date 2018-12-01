package com.autoplus;

import com.autoplus.dbConnection.ConnectionPool;
import com.autoplus.entity.Socket;
import com.autoplus.services.CarService;
import com.autoplus.services.CategoryService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

public class App extends Thread{
    private static final String ATTRIBUTE_NAME = "config";
    private static DataSource dataSource;
    private static ConnectionPool jdbcObj;
    public static Socket CACHE_SOCKET = null;
    public static volatile CarService carService;

    public static void main(String[] args) {
        jdbcObj = new ConnectionPool();
        try {
            dataSource = jdbcObj.setUpPool();
            jdbcObj.printDbStatus();
            carService = new CarService(dataSource);
            carService.getAllCars();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true){
            try {
                sleep(120*1000);
                CACHE_SOCKET = carService.getRandomProxy();
                System.out.println("change socket: " + CACHE_SOCKET);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
