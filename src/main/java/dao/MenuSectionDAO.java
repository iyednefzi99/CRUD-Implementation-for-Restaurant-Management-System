package dao;

import java.sql.*;
import java.util.*;

import entites.menu_package.MenuItem;
import entites.menu_package.MenuSection;

public class MenuSectionDAO implements BaseDAO<MenuSection>  {
    private final MenuItemDAO menuItemDAO = new MenuItemDAO();

    @Override
    public Optional<MenuSection> get(String id) {
        String sql = "SELECT * FROM menu_sections WHERE section_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    MenuSection section = new MenuSection(
                        rs.getString("title"),
                        rs.getString("description")
                    );
                    section.setMenuSectionID(rs.getString("section_id"));
                    
                    // Load menu items
                    String itemsSql = "SELECT * FROM menu_items WHERE section_id = ?";
                    try (PreparedStatement itemsStmt = conn.prepareStatement(itemsSql)) {
                        itemsStmt.setString(1, id);
                        try (ResultSet itemsRs = itemsStmt.executeQuery()) {
                            while (itemsRs.next()) {
                                Optional<MenuItem> itemOpt = menuItemDAO.get(itemsRs.getString("item_id"));
                                itemOpt.ifPresent(section::addMenuItem);
                            }
                        }
                    }
                    
                    return Optional.of(section);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<MenuSection> getAll() {
        List<MenuSection> sections = new ArrayList<>();
        String sql = "SELECT * FROM menu_sections";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                MenuSection section = new MenuSection(
                    rs.getString("title"),
                    rs.getString("description")
                );
                section.setMenuSectionID(rs.getString("section_id"));
                
                // Load menu items
                String itemsSql = "SELECT * FROM menu_items WHERE section_id = ?";
                try (PreparedStatement itemsStmt = conn.prepareStatement(itemsSql)) {
                    itemsStmt.setString(1, section.getMenuSectionID());
                    try (ResultSet itemsRs = itemsStmt.executeQuery()) {
                        while (itemsRs.next()) {
                            Optional<MenuItem> itemOpt = menuItemDAO.get(itemsRs.getString("item_id"));
                            itemOpt.ifPresent(section::addMenuItem);
                        }
                    }
                }
                
                sections.add(section);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sections;
    }

    @Override
    public boolean save(MenuSection section) {
        String sql = "INSERT INTO menu_sections (section_id, menu_id, title, description) " +
                     "VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, section.getMenuSectionID());
            stmt.setString(2, "MENU-001"); // Default menu ID, should be parameterized
            stmt.setString(3, section.getTitle());
            stmt.setString(4, section.getDescription());
            
            if (stmt.executeUpdate() == 0) {
                return false;
            }
            
            // Save menu items
            for (MenuItem item : section.getMenuItems()) {
                if (!menuItemDAO.save(item)) {
                    return false;
                }
            }
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(MenuSection section) {
        String sql = "UPDATE menu_sections SET title = ?, description = ? WHERE section_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, section.getTitle());
            stmt.setString(2, section.getDescription());
            stmt.setString(3, section.getMenuSectionID());
            
            if (stmt.executeUpdate() == 0) {
                return false;
            }
            
            // Update items (this is simplified - in production you'd need to handle adds/removes)
            for (MenuItem item : section.getMenuItems()) {
                if (!menuItemDAO.update(item)) {
                    return false;
                }
            }
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String sectionId) {
        // First delete all items (this will cascade if foreign keys are set up properly)
        String deleteItemsSql = "DELETE FROM menu_items WHERE section_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteItemsSql)) {
            
            stmt.setString(1, sectionId);
            stmt.executeUpdate();
            
            // Then delete the section
            String sql = "DELETE FROM menu_sections WHERE section_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(sql)) {
                deleteStmt.setString(1, sectionId);
                return deleteStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}