package entites.orders_package;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import entites.customers_package.Customer;
import entites.employees_package.Employee;
import entites.menu_package.MenuItem;
import entites.tables_package.Table;

public class Order {
    private final String orderID;
    private OrderStatus status;
    private final List<OrderItem> items;
    private final Customer customer;
    private final Employee waiter;
    private final Table table;
    private final LocalDateTime creationTime;

    public Order(Table table, Customer customer, Employee waiter) {
        if (table == null || customer == null || waiter == null) {
            throw new IllegalArgumentException("Table, customer and waiter cannot be null");
        }
        
        this.orderID = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        this.status = OrderStatus.NEW;
        this.items = new ArrayList<>();
        this.customer = customer;
        this.waiter = waiter;
        this.table = table;
        this.creationTime = LocalDateTime.now();
    }

    public static class OrderItem {
        private final MenuItem menuItem;
        private final int quantity;
        private final String specialRequest;
        
        public OrderItem(MenuItem menuItem, int quantity, String specialRequest) {
            this.menuItem = menuItem;
            this.quantity = quantity;
            this.specialRequest = specialRequest != null ? specialRequest : "";
        }
        
        // Getters
        public MenuItem getMenuItem() { return menuItem; }
        public int getQuantity() { return quantity; }
        public String getSpecialRequest() { return specialRequest; }
        public double getSubtotal() { return menuItem.getPrice() * quantity; }
    }

    public boolean addMenuItem(MenuItem item, int quantity, String specialRequest) {
        if (item == null) {
            throw new IllegalArgumentException("Menu item cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        return items.add(new OrderItem(item, quantity, specialRequest));
    }

    public boolean removeMenuItem(String itemId) {
        return items.removeIf(item -> item.getMenuItem().getMenuItemID().equals(itemId));
    }

    public boolean updateStatus(OrderStatus newStatus) {
        if (newStatus == null) {
            return false;
        }
        this.status = newStatus;
        return true;
    }

    // Getters
    public String getOrderID() { return orderID; }
    public OrderStatus getStatus() { return status; }
    public List<OrderItem> getItems() { return new ArrayList<>(items); }
    public Customer getCustomer() { return customer; }
    public Employee getWaiter() { return waiter; }
    public Table getTable() { return table; }
    public LocalDateTime getCreationTime() { return creationTime; }

    public double calculateTotal() {
        return items.stream()
                   .mapToDouble(OrderItem::getSubtotal)
                   .sum();
    }

    public void printOrderSummary() {
        System.out.println("=== Order Summary ===");
        System.out.println("Order ID: " + orderID);
        System.out.println("Table: " + table.getTableID());
        System.out.println("Customer: " + customer.getName());
        System.out.println("Waiter: " + waiter.getName());
        System.out.println("Status: " + status);
        System.out.println("Items:");
        items.forEach(item -> {
            System.out.printf("- %d x %s (%.2f each)%n",
                item.getQuantity(), 
                item.getMenuItem().getTitle(),
                item.getMenuItem().getPrice());
            if (!item.getSpecialRequest().isEmpty()) {
                System.out.println("  Special request: " + item.getSpecialRequest());
            }
        });
        System.out.printf("Total: %.2f%n", calculateTotal());
        System.out.println("====================");
    }
}