package com.apprating.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/app_rating_db";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASS = "";
    private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static final String H2_URL = "jdbc:h2:file:./data/app_rating_db;MODE=MySQL;DB_CLOSE_DELAY=-1";
    private static final String H2_USER = "sa";
    private static final String H2_PASS = "";
    private static final String H2_DRIVER = "org.h2.Driver";

    private static boolean useH2 = false;
    private static Connection connection = null;

    static {
        try {
            Class.forName(MYSQL_DRIVER);
            connection = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
        } catch (Exception e) {
            System.out.println("MySQL not available, falling back to H2 embedded database");
            try {
                Class.forName(H2_DRIVER);
                connection = DriverManager.getConnection(H2_URL, H2_USER, H2_PASS);
                useH2 = true;
            } catch (Exception e2) {
                System.err.println("Failed to connect to any database");
                e2.printStackTrace();
            }
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                if (useH2) {
                    connection = DriverManager.getConnection(H2_URL, H2_USER, H2_PASS);
                } else {
                    connection = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isUsingH2() {
        return useH2;
    }
}
