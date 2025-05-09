package entites.orders_package;

import java.util.UUID;

public class MealItem {
    private final String mealItemID;
    private final String name;
    private final double price;

    public MealItem( String name, double price) {
 
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        this.mealItemID = "MITM-" + UUID.randomUUID().toString().substring(0, 8);
        this.name = name != null ? name : "";
        this.price = price;
    }

    // Getters
    public String getMealItemID() { return mealItemID; }
    public String getName() { return name; }
    public double getPrice() { return price; }
}