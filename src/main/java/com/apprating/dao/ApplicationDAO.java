package com.apprating.dao;

import com.apprating.model.Application;
import com.apprating.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {

    public boolean addApplication(Application app) {
        String sql = "INSERT INTO applications (app_name, developer_name, category, version, description, release_date, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, app.getAppName());
            ps.setString(2, app.getDeveloperName());
            ps.setString(3, app.getCategory());
            ps.setString(4, app.getVersion());
            ps.setString(5, app.getDescription());
            ps.setString(6, app.getReleaseDate());
            ps.setString(7, app.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteApplication(int appId) {
        String sql = "DELETE FROM applications WHERE app_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Application getApplicationById(int appId) {
        String sql = "SELECT * FROM applications WHERE app_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Application app = new Application();
                app.setAppId(rs.getInt("app_id"));
                app.setAppName(rs.getString("app_name"));
                app.setDeveloperName(rs.getString("developer_name"));
                app.setCategory(rs.getString("category"));
                app.setVersion(rs.getString("version"));
                app.setDescription(rs.getString("description"));
                app.setReleaseDate(rs.getString("release_date"));
                app.setStatus(rs.getString("status"));
                app.setCreatedAt(rs.getString("created_at"));
                return app;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Application> getAllApplications() {
        List<Application> apps = new ArrayList<>();
        String sql = "SELECT * FROM applications ORDER BY app_name ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Application app = extractApplication(rs);
                apps.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apps;
    }

    public List<Application> getAllApplicationsWithRatings() {
        List<Application> apps = new ArrayList<>();
        String sql = "SELECT a.*, " +
                     "COALESCE(ROUND(AVG(r.rating_value), 1), 0) AS avg_rating, " +
                     "COUNT(r.rating_id) AS total_ratings " +
                     "FROM applications a " +
                     "LEFT JOIN ratings r ON a.app_id = r.app_id " +
                     "WHERE a.status = 'active' " +
                     "GROUP BY a.app_id " +
                     "ORDER BY a.app_name ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Application app = extractApplication(rs);
                app.setAverageRating(rs.getDouble("avg_rating"));
                app.setTotalRatings(rs.getInt("total_ratings"));
                apps.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apps;
    }

    public List<Application> searchApplications(String keyword) {
        List<Application> apps = new ArrayList<>();
        String sql = "SELECT a.*, " +
                     "COALESCE(ROUND(AVG(r.rating_value), 1), 0) AS avg_rating, " +
                     "COUNT(r.rating_id) AS total_ratings " +
                     "FROM applications a " +
                     "LEFT JOIN ratings r ON a.app_id = r.app_id " +
                     "WHERE a.status = 'active' AND (a.app_name LIKE ? OR a.category LIKE ? OR a.developer_name LIKE ?) " +
                     "GROUP BY a.app_id " +
                     "ORDER BY a.app_name ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Application app = extractApplication(rs);
                app.setAverageRating(rs.getDouble("avg_rating"));
                app.setTotalRatings(rs.getInt("total_ratings"));
                apps.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apps;
    }

    public int getApplicationCount() {
        String sql = "SELECT COUNT(*) FROM applications WHERE status = 'active'";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Application extractApplication(ResultSet rs) throws SQLException {
        Application app = new Application();
        app.setAppId(rs.getInt("app_id"));
        app.setAppName(rs.getString("app_name"));
        app.setDeveloperName(rs.getString("developer_name"));
        app.setCategory(rs.getString("category"));
        app.setVersion(rs.getString("version"));
        app.setDescription(rs.getString("description"));
        app.setReleaseDate(rs.getString("release_date"));
        app.setStatus(rs.getString("status"));
        app.setCreatedAt(rs.getString("created_at"));
        return app;
    }
}
