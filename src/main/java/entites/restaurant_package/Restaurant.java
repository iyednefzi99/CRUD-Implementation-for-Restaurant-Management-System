package entites.restaurant_package;


import java.util.ArrayList;
import java.util.List;

import entites.employees_package.Employee;
import entites.menu_package.Menu;
import entites.reservations_package.Reservation;
import entites.reviews_package.Review;
import entites.tables_package.Table;
import entites.tables_package.TableChart;
import entites.utils_package.Address;

public class Restaurant {
    private String name;
    private Address location;
    private TableChart tableChart;
    private List<Employee> staff;
    private List<Menu> menus;
    private List<Reservation> reservations;
    private List<Review> reviews;

    public Restaurant(String name, Address location) {
        this.name = name;
        this.location = location;
        this.tableChart = new TableChart();
        this.staff = new ArrayList<>();
        this.menus = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    // Getters and setters
    public String getName() { return name; }
    public Address getLocation() { return location; }
    public TableChart getTableChart() { return tableChart; }
    public List<Employee> getStaff() { return new ArrayList<>(staff); }
    public List<Menu> getMenus() { return new ArrayList<>(menus); }
    public List<Reservation> getReservations() { return new ArrayList<>(reservations); }
    public List<Review> getReviews() { return new ArrayList<>(reviews); }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant name cannot be empty");
        }
        this.name = name;
    }

    public void setLocation(Address location) {
        if (location == null) throw new IllegalArgumentException("Location cannot be null");
        this.location = location;
    }

    // Staff management
    public void hireEmployee(Employee employee) {
        if (employee == null) throw new IllegalArgumentException("Employee cannot be null");
        staff.add(employee);
    }

    public boolean terminateEmployee(Employee employee) {
        return staff.remove(employee);
    }

    // Menu management
    public void addMenu(Menu menu) {
        if (menu == null) throw new IllegalArgumentException("Menu cannot be null");
        menus.add(menu);
    }

    public boolean removeMenu(Menu menu) {
        return menus.remove(menu);
    }

    // Reservation management
    public void addReservation(Reservation reservation) {
        if (reservation == null) throw new IllegalArgumentException("Reservation cannot be null");
        reservations.add(reservation);
    }

    public boolean cancelReservation(Reservation reservation) {
        return reservations.remove(reservation);
    }

    // Review management
    public void addReview(Review review) {
        if (review == null) throw new IllegalArgumentException("Review cannot be null");
        reviews.add(review);
    }

    // Business operations
    public double calculateAverageRating() {
        if (reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToInt(Review::getStars)  // Changed from getRating to getStars
                .average()
                .orElse(0.0);
    }
    public int availableSeats() {
        return tableChart.getTables().stream()
                .filter(table -> !table.isOccupied())
                .mapToInt(Table::getMaxCapacity)
                .sum();
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", location=" + location +
                ", staffSize=" + staff.size() +
                ", menuCount=" + menus.size() +
                ", averageRating=" + calculateAverageRating() +
                '}';
    }
}