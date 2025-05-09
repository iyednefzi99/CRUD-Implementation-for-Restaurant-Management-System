package dao;

import java.sql.*;
import java.util.*;

import entites.menu_package.MenuItem;

public class MenuItemDAO implements BaseDAO<MenuItem>  {

    @Override
    public Optional<MenuItem> get(String id) {
        String sql = "SELECT * FROM menu_items WHERE item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    MenuItem item = new MenuItem(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDouble("price")
                    );
                    item.setMenuItemID(rs.getString("item_id"));
                    return Optional.of(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<MenuItem> getAll() {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT * FROM menu_items";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                MenuItem item = new MenuItem(
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getDouble("price")
                );
                item.setMenuItemID(rs.getString("item_id"));
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public boolean save(MenuItem item) {
        String sql = "INSERT INTO menu_items (item_id, section_id, title, description, price) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, item.getMenuItemID());
            stmt.setString(2, "SECTION-001"); // Default section, should be parameterized
            stmt.setString(3, item.getTitle());
            stmt.setString(4, item.getDescription());
            stmt.setDouble(5, item.getPrice());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(MenuItem item) {
        String sql = "UPDATE menu_items SET title = ?, description = ?, price = ? " +
                     "WHERE item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, item.getTitle());
            stmt.setString(2, item.getDescription());
            stmt.setDouble(3, item.getPrice());
            stmt.setString(4, item.getMenuItemID());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String itemId) {
        String sql = "DELETE FROM menu_items WHERE item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, itemId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}