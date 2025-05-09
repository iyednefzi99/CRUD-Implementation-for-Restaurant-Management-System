package entites.employees_package;

import java.time.LocalDate;

import entites.customers_package.Customer;
import entites.menu_package.MenuItem;
import entites.orders_package.Order;
import entites.orders_package.OrderStatus;
import entites.tables_package.Table;
import entites.tables_package.TableStatus;

public class Waiter extends Employee {
    
    public Waiter(String id, String name, String phone,
                String username, String password, String email) {
        super(id, name, phone, username, password, email);
    }

    public Waiter(String id, String name, String phone,
                String username, String password, String email,
                LocalDate dateJoined) {
        super(id, name, phone, username, password, email, dateJoined);
    }

    @Override
    public String getRole() {
        return "Waiter";
    }

    public Order createOrder(Table table, Customer customer) {
        if (table == null || customer == null) {
            throw new IllegalArgumentException("Table and customer cannot be null");
        }
        
        System.out.printf("Creating new order for table %s, customer %s%n",
                         table.getTableID(), customer.getName());
        Order newOrder = new Order(table, customer, this);
        table.setStatus(TableStatus.OCCUPIED);
        return newOrder;
    }

    public boolean addMenuItemToOrder(Order order, MenuItem item, int quantity, String specialRequest) {
        if (order == null || item == null) {
            throw new IllegalArgumentException("Order and item cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        System.out.printf("Adding %d x %s (%.2f) to order #%s%n",
                         quantity, item.getTitle(), item.getPrice(), order.getOrderID());
        if (specialRequest != null && !specialRequest.isEmpty()) {
            System.out.println("Special request: " + specialRequest);
        }
        return order.addMenuItem(item, quantity, specialRequest);
    }

    public boolean updateOrderStatus(Order order, OrderStatus newStatus) {
        if (order == null || newStatus == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        
        System.out.printf("Updating order #%s from %s to %s%n",
                         order.getOrderID(), order.getStatus(), newStatus);
        order.updateStatus(newStatus);
        
        if (newStatus == OrderStatus.COMPLETED || newStatus == OrderStatus.CANCELLED) {
            order.getTable().setStatus(TableStatus.FREE);
        }
        return true;
    }

    public double calculateOrderTotal(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        return order.calculateTotal();
    }
}