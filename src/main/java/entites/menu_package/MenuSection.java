package entites.menu_package;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MenuSection {
    private  String menuSectionID;
    private String title;
    private String description;
    private final List<MenuItem> menuItems;

    

    public MenuSection( String title, String description) {
       
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Section title cannot be null or empty");
        }

        this.menuSectionID = "SEC-" + UUID.randomUUID().toString().substring(0, 8);
        this.title = title;
        this.description = (description != null) ? description : "";
        this.menuItems = new ArrayList<>();
    }
    
    public MenuSection( String title) {
    	this( title, "");
    }

    // Getters
    public String getMenuSectionID() { return menuSectionID; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<MenuItem> getMenuItems() { return new ArrayList<>(menuItems); }

    // Setters
    public void setTitle(String title) {
     
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = (description != null) ? description : "";
    }
    public void setMenuSectionID(String id) {
	        this.menuSectionID = id;
	    }
    // Menu Item management
    public void addMenuItem(MenuItem item) {
        if (item == null) throw new IllegalArgumentException("Menu item cannot be null");
        if (menuItems.stream().anyMatch(i -> i.getMenuItemID().equals(item.getMenuItemID()))) {
            throw new IllegalArgumentException("Menu item with ID " + item.getMenuItemID() + " already exists");
        }
        menuItems.add(item);
    }

    public boolean removeMenuItem(String itemID) {
        return menuItems.removeIf(item -> item.getMenuItemID().equals(itemID));
    }

 /* class order constraint*/   public boolean containsItem(String itemID) {
        return menuItems.stream().anyMatch(item -> item.getMenuItemID().equals(itemID));
    }

    @Override
    public String toString() {
        return "MenuSection{" +
                "id='" + menuSectionID + '\'' +
                ", title='" + title + '\'' +
                ", items=" + menuItems.size() +
                '}';
    }

	

		
	
}