package dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import entites.reviews_package.Review;

public class ReviewDAO implements BaseDAO<Review> {

    @Override
    public Optional<Review> get(String id) {
        String sql = "SELECT * FROM reviews WHERE review_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Review review = new Review(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("stars"),
                        rs.getString("customer_id"),
                        rs.getString("restaurant_id")
                    );
                    review.setReviewId(rs.getString("review_id"));
                    review.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
                    review.setUpdateDate(rs.getTimestamp("update_date").toLocalDateTime());
                    return Optional.of(review);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Review> getAll() {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Review review = new Review(
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getInt("stars"),
                    rs.getString("customer_id"),
                    rs.getString("restaurant_id")
                );
                review.setReviewId(rs.getString("review_id"));
                review.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
                review.setUpdateDate(rs.getTimestamp("update_date").toLocalDateTime());
                reviews.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public boolean save(Review review) {
        String sql = "INSERT INTO reviews (review_id, title, description, stars, " +
                     "customer_id, restaurant_id, creation_date, update_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, review.getReviewId());
            stmt.setString(2, review.getTitle());
            stmt.setString(3, review.getDescription());
            stmt.setInt(4, review.getStars());
            stmt.setString(5, review.getCustomerId());
            stmt.setString(6, review.getRestaurantId());
            stmt.setTimestamp(7, Timestamp.valueOf(review.getCreationDate()));
            stmt.setTimestamp(8, Timestamp.valueOf(review.getUpdateDate()));
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Review review) {
        String sql = "UPDATE reviews SET title = ?, description = ?, stars = ?, " +
                     "customer_id = ?, restaurant_id = ?, update_date = ? " +
                     "WHERE review_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, review.getTitle());
            stmt.setString(2, review.getDescription());
            stmt.setInt(3, review.getStars());
            stmt.setString(4, review.getCustomerId());
            stmt.setString(5, review.getRestaurantId());
            stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(7, review.getReviewId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String reviewId) {
        String sql = "DELETE FROM reviews WHERE review_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, reviewId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}