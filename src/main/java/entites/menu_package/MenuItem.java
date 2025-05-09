package entites.menu_package;

import java.util.UUID;

public class MenuItem {
    private  String menuItemID;
    private String title;
    private String description;
    private double price;

    public MenuItem( String title, String description, double price) {
  

        this.menuItemID = "ITEM-" + UUID.randomUUID().toString().substring(0, 8);
        this.title = title;
        this.description = (description != null) ? description : "";
        this.price = price;
    }

    // Getters
    public String getMenuItemID() { return menuItemID; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }

    // Setters with validation
    public void setTitle(String title) {
     
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = (description != null) ? description : "";
    }
	public void setMenuItemID(String id ) {
	     
	        this.menuItemID = id;
	    }

    public boolean updatePrice(double newPrice) {
        if (newPrice <= 0) return false;
        this.price = newPrice;
        return true;
    }

    public boolean adjustPriceByPercentage(double percentage) {
        double newPrice = price * (1 + percentage/100);
        if (newPrice <= 0) return false;
        this.price = newPrice;
        return true;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id='" + menuItemID + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                '}';
    }


	
}