package entites.menu_package;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Menu {
    private String menuID;
    private String title;
    private String description;
    private final List<MenuSection> sections;

    public Menu( String title, String description) {
    
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Menu title cannot be null or empty");
        }
        
        this.menuID = "MENU-" + UUID.randomUUID().toString().substring(0, 8);
        this.title = title;
        this.description = (description != null) ? description : "";
        this.sections = new ArrayList<>();
    }

    // Getters
    public String getMenuID() { return menuID; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<MenuSection> getSections() { return new ArrayList<>(sections); }

    // Setters with validation
    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Menu title cannot be null or empty");
        }
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = (description != null) ? description : "";
    }
	public void setMenuID(String id) {
	      if (id == null || id.isBlank()) {
	            throw new IllegalArgumentException("Menu ID cannot be null or empty");
	        }
	        this.menuID = id;
	    }

    // Section management
    public void addMenuSection(MenuSection section) {
        if (section == null) {
            throw new IllegalArgumentException("Menu section cannot be null");
        }
        sections.add(section);
    }

    public boolean removeMenuSection(String sectionId) {
        return sections.removeIf(section -> section.getMenuSectionID().equals(sectionId));
    }


		
		
	}
