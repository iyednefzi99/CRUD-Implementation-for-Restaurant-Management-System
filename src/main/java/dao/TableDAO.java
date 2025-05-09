package dao;

import java.sql.*;
import java.util.*;

import entites.tables_package.Table;
import entites.tables_package.TableStatus;

public class TableDAO implements BaseDAO<Table> {

    @Override
    public Optional<Table> get(String id) {
        String sql = "SELECT * FROM tables WHERE table_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Table table = new Table(
                        rs.getInt("max_capacity"),
                        rs.getInt("location_identifier")
                    );
                    table.setTableID(rs.getString("table_id"));
                    table.setStatus(TableStatus.valueOf(rs.getString("status")));
                    return Optional.of(table);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Table> getAll() {
        List<Table> tables = new ArrayList<>();
        String sql = "SELECT * FROM tables";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Table table = new Table(
                    rs.getInt("max_capacity"),
                    rs.getInt("location_identifier")
                );
                table.setTableID(rs.getString("table_id"));
                table.setStatus(TableStatus.valueOf(rs.getString("status")));
                tables.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    @Override
    public boolean save(Table table) {
        String sql = "INSERT INTO tables (table_id, max_capacity, location_identifier, status) " +
                     "VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, table.getTableID());
            stmt.setInt(2, table.getMaxCapacity());
            stmt.setInt(3, table.getLocationIdentifier());
            stmt.setString(4, table.getStatus().name());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Table table) {
        String sql = "UPDATE tables SET max_capacity = ?, location_identifier = ?, status = ? " +
                     "WHERE table_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, table.getMaxCapacity());
            stmt.setInt(2, table.getLocationIdentifier());
            stmt.setString(3, table.getStatus().name());
            stmt.setString(4, table.getTableID());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String tableId) {
        String sql = "DELETE FROM tables WHERE table_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tableId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}