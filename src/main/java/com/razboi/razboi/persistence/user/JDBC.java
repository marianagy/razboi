package com.razboi.razboi.persistence.user;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class JDBC {
    private static final Logger logger = LogManager.getLogger();
    private Properties jdbcProps;
    private Connection instance = null;

    public JDBC(Properties props) {
        jdbcProps = props;
    }

    public JDBC(){
        jdbcProps = new Properties();
        FileReader fileReader = null;
        try {
            fileReader = new FileReader("C:\\Users\\Dan\\IdeaProjects\\razboi-nou\\src\\main\\resources\\bd.config");

            jdbcProps.load(fileReader);
        } catch (Exception e) {
            logger.error(e);

            System.out.println(System.getProperty("user.dir"));

        }
    }
    private Connection getNewConnection() {
        logger.traceEntry();
        String driver = jdbcProps.getProperty("jdbc.driver");
        String url = jdbcProps.getProperty("jdbc.url");
        String user = jdbcProps.getProperty("jdbc.persistence.user");
        String pass = jdbcProps.getProperty("jdbc.pass");
        logger.info("trying to connect to database ... {}", url);
        logger.info("persistence.user: {}", user);
        logger.info("pass: {}", pass);
        Connection con = null;
        try {
            Class.forName(driver);
            logger.info("Loaded driver ...{}", driver);
            if (user != null && pass != null)
                con = DriverManager.getConnection(url, user, pass);
            else
                con = DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            logger.error(e);
            System.out.println("Error loading driver " + e);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting connection " + e);
        }
        return con;
    }

    public Connection getConnection() {
        logger.traceEntry();
        try {
            if (instance == null || instance.isClosed())
                instance = getNewConnection();

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB: " + e);
        }
        logger.traceExit(instance);
        return instance;
    }
}
