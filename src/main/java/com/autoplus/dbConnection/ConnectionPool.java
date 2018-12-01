package com.autoplus.dbConnection;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;


import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Class that describes a pool for storing a connection and issuing on demand
 * Created by Viacheslav Koshchii on 11/7/2017.
 */
public class ConnectionPool {

    private static String JDBC_DRIVER;
    private static String JDBC_DB_URL;
    private static String JDBC_USER;
    private static String JDBC_PASS;
    private static int POOL_SIZE = 5;

    private static GenericObjectPool gPool = null;

    /**
     * Constructor without parameters that create object of connection pool
     */
    public ConnectionPool() {
        Properties props = new Properties();
        FileInputStream in = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            in = new FileInputStream(classLoader.getResource("prop.properties").getFile());
            props.load(in);
            in.close();
            JDBC_USER = props.getProperty("user");
            JDBC_PASS = props.getProperty("password");
            JDBC_DRIVER = props.getProperty("driver");
            JDBC_DB_URL = props.getProperty("url");
        } catch (FileNotFoundException e) {
            System.out.println("File with properties not found");
        } catch (IOException e) {
            System.out.println("could not get data from properties file");
        }
    }

    /**
     * get DataSource from Connection pool
     *
     * @return
     * @throws Exception
     */
    public DataSource setUpPool() throws Exception {
        Class.forName(JDBC_DRIVER);
        gPool = new GenericObjectPool();
        gPool.setMaxActive(POOL_SIZE);
        ConnectionFactory cf = new DriverManagerConnectionFactory(JDBC_DB_URL, JDBC_USER, JDBC_PASS);
        PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, gPool, null, null, false, true);
        return new PoolingDataSource(gPool);
    }

    public GenericObjectPool getConnectionPool() {
        return gPool;
    }

    /**
     * This Method Is Used To Print The Connection Pool Status
     */
    public void printDbStatus() {
        System.out.println("Max.: " + getConnectionPool().getMaxActive() + "; Active: " + getConnectionPool().getNumActive() + "; Idle: " + getConnectionPool().getNumIdle());
    }

    public void close() throws Exception {
        gPool.close();
    }
}