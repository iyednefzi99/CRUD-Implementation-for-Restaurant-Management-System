package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import entites.customers_package.Customer;
import entites.employees_package.Employee;
import entites.menu_package.MenuItem;
import entites.orders_package.Order;
import entites.orders_package.OrderStatus;
import entites.tables_package.Table;

public class OrderDAO implements BaseDAO<Order> {
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final TableDAO tableDAO = new TableDAO();
    private final MenuItemDAO menuItemDAO = new MenuItemDAO();

    @Override
    public Optional<Order> get(String id) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Optional<Customer> customerOpt = customerDAO.get(rs.getString("customer_id"));
                    Optional<Employee> waiterOpt = employeeDAO.get(rs.getString("waiter_id"));
                    Optional<Table> tableOpt = tableDAO.get(rs.getString("table_id"));
                    
                    if (customerOpt.isEmpty() || waiterOpt.isEmpty() || tableOpt.isEmpty()) {
                        return Optional.empty();
                    }
                    
                    Order order = new Order(
                        tableOpt.get(),
                        customerOpt.get(),
                        waiterOpt.get()
                    );
                    
                    order.updateStatus(OrderStatus.valueOf(rs.getString("status")));
                    
                    // Load order items
                    String itemsSql = "SELECT * FROM order_items WHERE order_id = ?";
                    try (PreparedStatement itemsStmt = conn.prepareStatement(itemsSql)) {
                        itemsStmt.setString(1, id);
                        try (ResultSet itemsRs = itemsStmt.executeQuery()) {
                            while (itemsRs.next()) {
                                Optional<MenuItem> itemOpt = menuItemDAO.get(itemsRs.getString("item_id"));
                                if (itemOpt.isPresent()) {
                                    order.addMenuItem(
                                        itemOpt.get(),
                                        itemsRs.getInt("quantity"),
                                        itemsRs.getString("special_request")
                                    );
                                }
                            }
                        }
                    }
                    
                    return Optional.of(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Optional<Customer> customerOpt = customerDAO.get(rs.getString("customer_id"));
                Optional<Employee> waiterOpt = employeeDAO.get(rs.getString("waiter_id"));
                Optional<Table> tableOpt = tableDAO.get(rs.getString("table_id"));
                
                if (customerOpt.isEmpty() || waiterOpt.isEmpty() || tableOpt.isEmpty()) {
                    continue;
                }
                
                Order order = new Order(
                    tableOpt.get(),
                    customerOpt.get(),
                    waiterOpt.get()
                );
                
                order.updateStatus(OrderStatus.valueOf(rs.getString("status")));
                
                // Load order items
                String itemsSql = "SELECT * FROM order_items WHERE order_id = ?";
                try (PreparedStatement itemsStmt = conn.prepareStatement(itemsSql)) {
                    itemsStmt.setString(1, rs.getString("order_id"));
                    try (ResultSet itemsRs = itemsStmt.executeQuery()) {
                        while (itemsRs.next()) {
                            Optional<MenuItem> itemOpt = menuItemDAO.get(itemsRs.getString("item_id"));
                            if (itemOpt.isPresent()) {
                                order.addMenuItem(
                                    itemOpt.get(),
                                    itemsRs.getInt("quantity"),
                                    itemsRs.getString("special_request")
                                );
                            }
                        }
                    }
                }
                
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public boolean save(Order order) {
        String sql = "INSERT INTO orders (order_id, customer_id, waiter_id, table_id, status, creation_time) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, order.getOrderID());
            stmt.setString(2, order.getCustomer().getId());
            stmt.setString(3, order.getWaiter().getId());
            stmt.setString(4, order.getTable().getTableID());
            stmt.setString(5, order.getStatus().name());
            stmt.setTimestamp(6, Timestamp.valueOf(order.getCreationTime()));
            
            if (stmt.executeUpdate() == 0) {
                return false;
            }
            
            // Save order items
            String itemsSql = "INSERT INTO order_items (order_id, item_id, quantity, special_request) " +
                             "VALUES (?, ?, ?, ?)";
            try (PreparedStatement itemsStmt = conn.prepareStatement(itemsSql)) {
                for (Order.OrderItem item : order.getItems()) {
                    itemsStmt.setString(1, order.getOrderID());
                    itemsStmt.setString(2, item.getMenuItem().getMenuItemID());
                    itemsStmt.setInt(3, item.getQuantity());
                    itemsStmt.setString(4, item.getSpecialRequest());
                    itemsStmt.addBatch();
                }
                itemsStmt.executeBatch();
            }
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Order order) {
        String sql = "UPDATE orders SET customer_id = ?, waiter_id = ?, table_id = ?, status = ? " +
                     "WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, order.getCustomer().getId());
            stmt.setString(2, order.getWaiter().getId());
            stmt.setString(3, order.getTable().getTableID());
            stmt.setString(4, order.getStatus().name());
            stmt.setString(5, order.getOrderID());
            
            if (stmt.executeUpdate() == 0) {
                return false;
            }
            
            // Delete existing items and re-add them
            String deleteItemsSql = "DELETE FROM order_items WHERE order_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteItemsSql)) {
                deleteStmt.setString(1, order.getOrderID());
                deleteStmt.executeUpdate();
            }
            
            // Add current items
            String itemsSql = "INSERT INTO order_items (order_id, item_id, quantity, special_request) " +
                             "VALUES (?, ?, ?, ?)";
            try (PreparedStatement itemsStmt = conn.prepareStatement(itemsSql)) {
                for (Order.OrderItem item : order.getItems()) {
                    itemsStmt.setString(1, order.getOrderID());
                    itemsStmt.setString(2, item.getMenuItem().getMenuItemID());
                    itemsStmt.setInt(3, item.getQuantity());
                    itemsStmt.setString(4, item.getSpecialRequest());
                    itemsStmt.addBatch();
                }
                itemsStmt.executeBatch();
            }
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String orderId) {
        // First delete order items
        String deleteItemsSql = "DELETE FROM order_items WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteItemsSql)) {
            
            stmt.setString(1, orderId);
            stmt.executeUpdate();
            
            // Then delete the order
            String sql = "DELETE FROM orders WHERE order_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(sql)) {
                deleteStmt.setString(1, orderId);
                return deleteStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}