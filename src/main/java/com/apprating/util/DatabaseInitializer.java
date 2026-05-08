package com.apprating.util;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {
        if (!DBConnection.isUsingH2()) {
            return;
        }
        System.out.println("Initializing H2 embedded database...");
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) NOT NULL UNIQUE, " +
                    "email VARCHAR(100) NOT NULL UNIQUE, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "role VARCHAR(20) NOT NULL DEFAULT 'user', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            stmt.execute("CREATE TABLE IF NOT EXISTS applications (" +
                    "app_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "app_name VARCHAR(100) NOT NULL, " +
                    "developer_name VARCHAR(100) NOT NULL, " +
                    "category VARCHAR(50) NOT NULL, " +
                    "version VARCHAR(20) NOT NULL, " +
                    "description TEXT, " +
                    "release_date DATE, " +
                    "status VARCHAR(20) NOT NULL DEFAULT 'active', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            stmt.execute("CREATE TABLE IF NOT EXISTS ratings (" +
                    "rating_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "app_id INT NOT NULL, " +
                    "rating_value INT NOT NULL CHECK (rating_value >= 1 AND rating_value <= 5), " +
                    "review_comment TEXT, " +
                    "rated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (app_id) REFERENCES applications(app_id) ON DELETE CASCADE, " +
                    "UNIQUE (user_id, app_id))");

            stmt.execute("MERGE INTO users (user_id, username, email, password, role) KEY(user_id) VALUES " +
                    "(1, 'admin', 'admin@appratingsystem.com', 'admin123', 'admin')");
            stmt.execute("MERGE INTO users (user_id, username, email, password, role) KEY(user_id) VALUES " +
                    "(2, 'user1', 'user1@appratingsystem.com', 'user123', 'user')");
            stmt.execute("MERGE INTO users (user_id, username, email, password, role) KEY(user_id) VALUES " +
                    "(3, 'john_doe', 'john@example.com', 'pass123', 'user')");
            stmt.execute("MERGE INTO users (user_id, username, email, password, role) KEY(user_id) VALUES " +
                    "(4, 'jane_smith', 'jane@example.com', 'pass456', 'user')");

            int appCount;
            try (var rs = stmt.executeQuery("SELECT COUNT(*) FROM applications")) {
                rs.next();
                appCount = rs.getInt(1);
            }

            if (appCount == 0) {
                stmt.execute("INSERT INTO applications (app_name, developer_name, category, version, description, release_date, status) VALUES " +
                        "('WhatsApp Messenger', 'Meta Platforms', 'Communication', '2.24.1', 'Free messaging and calling app used worldwide.', '2024-01-15', 'active')");
                stmt.execute("INSERT INTO applications (app_name, developer_name, category, version, description, release_date, status) VALUES " +
                        "('Instagram', 'Meta Platforms', 'Social Media', '298.0', 'Photo and video sharing social networking app.', '2024-02-10', 'active')");
                stmt.execute("INSERT INTO applications (app_name, developer_name, category, version, description, release_date, status) VALUES " +
                        "('Spotify', 'Spotify AB', 'Music', '8.8.0', 'Digital music streaming service with millions of tracks.', '2024-03-05', 'active')");
                stmt.execute("INSERT INTO applications (app_name, developer_name, category, version, description, release_date, status) VALUES " +
                        "('Google Maps', 'Google LLC', 'Navigation', '24.10.0', 'Web mapping service with real-time navigation.', '2024-01-20', 'active')");
                stmt.execute("INSERT INTO applications (app_name, developer_name, category, version, description, release_date, status) VALUES " +
                        "('YouTube', 'Google LLC', 'Video', '19.08.0', 'Video sharing platform for watching and uploading content.', '2024-02-28', 'active')");
                stmt.execute("INSERT INTO applications (app_name, developer_name, category, version, description, release_date, status) VALUES " +
                        "('Microsoft Excel', 'Microsoft Corporation', 'Productivity', '2401', 'Spreadsheet application for data analysis and calculations.', '2024-03-15', 'active')");
                stmt.execute("INSERT INTO applications (app_name, developer_name, category, version, description, release_date, status) VALUES " +
                        "('Twitter X', 'X Corp', 'Social Media', '10.20.0', 'Social networking and microblogging platform.', '2024-04-01', 'active')");
                stmt.execute("INSERT INTO applications (app_name, developer_name, category, version, description, release_date, status) VALUES " +
                        "('Netflix', 'Netflix Inc.', 'Entertainment', '8.120.0', 'Streaming service for movies and TV shows.', '2024-04-10', 'active')");
                stmt.execute("INSERT INTO applications (app_name, developer_name, category, version, description, release_date, status) VALUES " +
                        "('Zoom', 'Zoom Video Communications', 'Communication', '5.17.0', 'Video conferencing and remote meeting platform.', '2024-05-01', 'active')");
                stmt.execute("INSERT INTO applications (app_name, developer_name, category, version, description, release_date, status) VALUES " +
                        "('Amazon Shopping', 'Amazon', 'Shopping', '24.5.0', 'E-commerce platform for online shopping.', '2024-05-15', 'active')");
            }

            int ratingCount;
            try (var rs = stmt.executeQuery("SELECT COUNT(*) FROM ratings")) {
                rs.next();
                ratingCount = rs.getInt(1);
            }

            if (ratingCount == 0) {
                stmt.execute("INSERT INTO ratings (user_id, app_id, rating_value, review_comment) VALUES " +
                        "(2, 1, 4, 'Great messaging app, very reliable and fast.')");
                stmt.execute("INSERT INTO ratings (user_id, app_id, rating_value, review_comment) VALUES " +
                        "(3, 1, 5, 'Best communication app ever!')");
                stmt.execute("INSERT INTO ratings (user_id, app_id, rating_value, review_comment) VALUES " +
                        "(4, 1, 4, 'Works well for daily messaging needs.')");
                stmt.execute("INSERT INTO ratings (user_id, app_id, rating_value, review_comment) VALUES " +
                        "(2, 2, 3, 'Good but too many ads now.')");
                stmt.execute("INSERT INTO ratings (user_id, app_id, rating_value, review_comment) VALUES " +
                        "(3, 2, 4, 'Love the stories feature and reels.')");
                stmt.execute("INSERT INTO ratings (user_id, app_id, rating_value, review_comment) VALUES " +
                        "(4, 2, 5, 'Best social media platform for photos.')");
                stmt.execute("INSERT INTO ratings (user_id, app_id, rating_value, review_comment) VALUES " +
                        "(2, 3, 5, 'Amazing music streaming service.')");
                stmt.execute("INSERT INTO ratings (user_id, app_id, rating_value, review_comment) VALUES " +
                        "(3, 3, 4, 'Great playlist recommendations.')");
                stmt.execute("INSERT INTO ratings (user_id, app_id, rating_value, review_comment) VALUES " +
                        "(4, 3, 5, 'Perfect for music lovers!')");
                stmt.execute("INSERT INTO ratings (user_id, app_id, rating_value, review_comment) VALUES " +
                        "(2, 4, 5, 'Indispensable for navigation.')");
                stmt.execute("INSERT INTO ratings (user_id, app_id, rating_value, review_comment) VALUES " +
                        "(3, 4, 4, 'Very accurate directions.')");
                stmt.execute("INSERT INTO ratings (user_id, app_id, rating_value, review_comment) VALUES " +
                        "(4, 4, 5, 'Saved me countless times while driving.')");
                stmt.execute("INSERT INTO ratings (user_id, app_id, rating_value, review_comment) VALUES " +
                        "(2, 5, 4, 'Great content but too many ads.')");
                stmt.execute("INSERT INTO ratings (user_id, app_id, rating_value, review_comment) VALUES " +
                        "(3, 5, 5, 'My go-to app for entertainment.')");
                stmt.execute("INSERT INTO ratings (user_id, app_id, rating_value, review_comment) VALUES " +
                        "(4, 5, 4, 'Excellent video quality and recommendations.')");
            }

            System.out.println("H2 database initialized successfully.");
        } catch (Exception e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
