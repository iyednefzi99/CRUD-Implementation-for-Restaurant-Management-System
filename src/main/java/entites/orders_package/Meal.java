package entites.orders_package;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Meal {
    private final String mealID;
    private int quantity;
    private final List<MealItem> items;

    public Meal( int quantity) {
     
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.mealID = "MEAL-" + UUID.randomUUID().toString().substring(0, 8);
        this.quantity = quantity;
        this.items = new ArrayList<>();
    }

    public boolean addMealItem(MealItem item) {
        if (item == null) return false;
        if (items.stream().anyMatch(i -> i.getMealItemID().equals(item.getMealItemID()))) {
            return false;
        }
        items.add(item);
        return true;
    }

    public boolean updateQuantity(int newQuantity) {
        if (newQuantity <= 0) return false;
        this.quantity = newQuantity;
        return true;
    }

    // Getters
    public String getMealID() { return mealID; }
    public int getQuantity() { return quantity; }
    public List<MealItem> getItems() { return new ArrayList<>(items); }

    public double calculateSubtotal() {
        return items.stream()
                   .mapToDouble(MealItem::getPrice)
                   .sum() * quantity;
    }
}
