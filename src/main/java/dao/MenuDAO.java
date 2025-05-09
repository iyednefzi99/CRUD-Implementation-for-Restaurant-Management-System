package dao;

import java.sql.*;
import java.util.*;

import entites.menu_package.Menu;
import entites.menu_package.MenuSection;

public class MenuDAO implements BaseDAO<Menu> {
    private final MenuSectionDAO menuSectionDAO = new MenuSectionDAO();

    @Override
    public Optional<Menu> get(String id) {
        String sql = "SELECT * FROM menus WHERE menu_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Menu menu = new Menu(
                        rs.getString("title"),
                        rs.getString("description")
                    );
                    menu.setMenuID(rs.getString("menu_id"));
                    
                    // Load menu sections
                    String sectionsSql = "SELECT * FROM menu_sections WHERE menu_id = ?";
                    try (PreparedStatement sectionsStmt = conn.prepareStatement(sectionsSql)) {
                        sectionsStmt.setString(1, id);
                        try (ResultSet sectionsRs = sectionsStmt.executeQuery()) {
                            while (sectionsRs.next()) {
                                Optional<MenuSection> sectionOpt = menuSectionDAO.get(sectionsRs.getString("section_id"));
                                sectionOpt.ifPresent(menu::addMenuSection);
                            }
                        }
                    }
                    
                    return Optional.of(menu);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Menu> getAll() {
        List<Menu> menus = new ArrayList<>();
        String sql = "SELECT * FROM menus";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Menu menu = new Menu(
                    rs.getString("title"),
                    rs.getString("description")
                );
                menu.setMenuID(rs.getString("menu_id"));
                
                // Load menu sections
                String sectionsSql = "SELECT * FROM menu_sections WHERE menu_id = ?";
                try (PreparedStatement sectionsStmt = conn.prepareStatement(sectionsSql)) {
                    sectionsStmt.setString(1, menu.getMenuID());
                    try (ResultSet sectionsRs = sectionsStmt.executeQuery()) {
                        while (sectionsRs.next()) {
                            Optional<MenuSection> sectionOpt = menuSectionDAO.get(sectionsRs.getString("section_id"));
                            sectionOpt.ifPresent(menu::addMenuSection);
                        }
                    }
                }
                
                menus.add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menus;
    }

    @Override
    public boolean save(Menu menu) {
        String sql = "INSERT INTO menus (menu_id, title, description) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, menu.getMenuID());
            stmt.setString(2, menu.getTitle());
            stmt.setString(3, menu.getDescription());
            
            if (stmt.executeUpdate() == 0) {
                return false;
            }
            
            // Save menu sections
            for (MenuSection section : menu.getSections()) {
                if (!menuSectionDAO.save(section)) {
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
    public boolean update(Menu menu) {
        String sql = "UPDATE menus SET title = ?, description = ? WHERE menu_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, menu.getTitle());
            stmt.setString(2, menu.getDescription());
            stmt.setString(3, menu.getMenuID());
            
            if (stmt.executeUpdate() == 0) {
                return false;
            }
            
            // Update sections (this is simplified - in production you'd need to handle adds/removes)
            for (MenuSection section : menu.getSections()) {
                if (!menuSectionDAO.update(section)) {
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
    public boolean delete(String menuId) {
        // First delete all sections (this will cascade to items if foreign keys are set up properly)
        String deleteSectionsSql = "DELETE FROM menu_sections WHERE menu_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteSectionsSql)) {
            
            stmt.setString(1, menuId);
            stmt.executeUpdate();
            
            // Then delete the menu
            String sql = "DELETE FROM menus WHERE menu_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(sql)) {
                deleteStmt.setString(1, menuId);
                return deleteStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}