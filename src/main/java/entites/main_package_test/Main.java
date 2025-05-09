package entites.main_package_test;

import java.time.LocalDate;

import entites.customers_package.Customer;
import entites.employees_package.Waiter;
import entites.menu_package.MenuItem;
import entites.orders_package.Order;
import entites.orders_package.OrderStatus;
import entites.tables_package.Table;
import entites.tables_package.TableStatus;

public class Main {
    public static void main(String[] args) {
        // 1. Create test data
        Waiter john = new Waiter(
            "W001", 
            "John Smith", 
            "555-0101",
            "john", 
            "password123", 
            "john@restaurant.com",
            LocalDate.of(2020, 5, 15)
        );
        
        Customer customer = new Customer(
            "C100",
            "Alice Johnson",
            "555-0202",
            "alice",
            "alice123",
            "alice@email.com"
        );
        
        Table table5 = new Table( 4, 1);
        table5.setStatus(TableStatus.FREE);
        
        MenuItem pasta = new MenuItem(
        
            "Spaghetti Carbonara",
            "Classic pasta with egg, cheese, and pancetta",
            14.99
        );
        
        MenuItem salad = new MenuItem(
        
            "Caesar Salad",
            "Romaine lettuce with croutons and parmesan",
            9.99
        );
        
        MenuItem wine = new MenuItem(
         
            "House Red Wine",
            "Glass of our signature blend",
            7.50
        );

        // 2. Test Waiter operations
        System.out.println("\n=== TESTING WAITER OPERATIONS ===");
        
        // Create new order
        System.out.println("\nCreating new order...");
        Order order = john.createOrder(table5, customer);
        System.out.println("Created order: " + order.getOrderID());
        System.out.println("Table status: " + table5.getStatus());
        
        // Add items to order
        System.out.println("\nAdding items to order...");
        john.addMenuItemToOrder(order, pasta, 2, "Extra cheese");
        john.addMenuItemToOrder(order, salad, 1, "No croutons");
        john.addMenuItemToOrder(order, wine, 3, "");
        
        // Print order summary
        System.out.println("\nOrder summary:");
        order.printOrderSummary();
        
        // Calculate total
        System.out.printf("\nOrder total: $%.2f%n", john.calculateOrderTotal(order));
        
        // Update order status
        System.out.println("\nUpdating order status...");
        john.updateOrderStatus(order, OrderStatus.IN_PROGRESS);
        john.updateOrderStatus(order, OrderStatus.READY_FOR_SERVING);
        john.updateOrderStatus(order, OrderStatus.COMPLETED);
        
        // Check table status after completion
        System.out.println("\nAfter order completion:");
        System.out.println("Order status: " + order.getStatus());
        System.out.println("Table status: " + table5.getStatus());
        
        // 3. Test error cases
        System.out.println("\n=== TESTING ERROR CASES ===");
        try {
            System.out.println("\nAttempting to create order with null table...");
            john.createOrder(null, customer);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught exception: " + e.getMessage());
        }
        
        try {
            System.out.println("\nAttempting to add null item to order...");
            john.addMenuItemToOrder(order, null, 1, "");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught exception: " + e.getMessage());
        }
        
        System.out.println("\n=== TEST COMPLETE ===");
    }
}