package com.apprating.dao;

import com.apprating.model.Rating;
import com.apprating.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingDAO {

    public boolean addRating(Rating rating) {
        String sql = "INSERT INTO ratings (user_id, app_id, rating_value, review_comment) VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE rating_value = VALUES(rating_value), review_comment = VALUES(review_comment), rated_on = CURRENT_TIMESTAMP";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rating.getUserId());
            ps.setInt(2, rating.getAppId());
            ps.setInt(3, rating.getRatingValue());
            ps.setString(4, rating.getReviewComment());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRating(int ratingId) {
        String sql = "DELETE FROM ratings WHERE rating_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ratingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRatingByUserAndApp(int userId, int appId) {
        String sql = "DELETE FROM ratings WHERE user_id = ? AND app_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, appId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Rating getRatingByUserAndApp(int userId, int appId) {
        String sql = "SELECT * FROM ratings WHERE user_id = ? AND app_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, appId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractRating(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Rating> getRatingsByAppId(int appId) {
        List<Rating> ratings = new ArrayList<>();
        String sql = "SELECT r.*, u.username FROM ratings r " +
                     "JOIN users u ON r.user_id = u.user_id " +
                     "WHERE r.app_id = ? ORDER BY r.rated_on DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Rating rating = extractRating(rs);
                rating.setUsername(rs.getString("username"));
                ratings.add(rating);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ratings;
    }

    public List<Rating> getRatingsByUserId(int userId) {
        List<Rating> ratings = new ArrayList<>();
        String sql = "SELECT r.*, a.app_name FROM ratings r " +
                     "JOIN applications a ON r.app_id = a.app_id " +
                     "WHERE r.user_id = ? ORDER BY r.rated_on DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Rating rating = extractRating(rs);
                rating.setAppName(rs.getString("app_name"));
                ratings.add(rating);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ratings;
    }

    public List<Rating> getAllRatings() {
        List<Rating> ratings = new ArrayList<>();
        String sql = "SELECT r.*, u.username, a.app_name FROM ratings r " +
                     "JOIN users u ON r.user_id = u.user_id " +
                     "JOIN applications a ON r.app_id = a.app_id " +
                     "ORDER BY r.rated_on DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Rating rating = extractRating(rs);
                rating.setUsername(rs.getString("username"));
                rating.setAppName(rs.getString("app_name"));
                ratings.add(rating);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ratings;
    }

    public double calculateAverageRating(int appId) {
        String sql = "SELECT COALESCE(AVG(rating_value), 0) AS avg_rating FROM ratings WHERE app_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Math.round(rs.getDouble("avg_rating") * 10.0) / 10.0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public int getTotalRatingCount() {
        String sql = "SELECT COUNT(*) FROM ratings";
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

    public double getOverallAverageRating() {
        String sql = "SELECT COALESCE(AVG(rating_value), 0) FROM ratings";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return Math.round(rs.getDouble(1) * 10.0) / 10.0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private Rating extractRating(ResultSet rs) throws SQLException {
        Rating rating = new Rating();
        rating.setRatingId(rs.getInt("rating_id"));
        rating.setUserId(rs.getInt("user_id"));
        rating.setAppId(rs.getInt("app_id"));
        rating.setRatingValue(rs.getInt("rating_value"));
        rating.setReviewComment(rs.getString("review_comment"));
        rating.setRatedOn(rs.getString("rated_on"));
        return rating;
    }
}
