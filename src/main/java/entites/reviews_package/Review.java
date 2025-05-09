package entites.reviews_package;

import java.time.LocalDateTime;
import java.util.Objects;

import entites.restaurant_package.Restaurant;

public class Review {
    private  String reviewId;
    private String title;
    private String description;
    private int stars; // 1 to 5
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private String customerId;
    private String restaurantId;

    public Review(String title, String description, int stars, 
                 String customerId, String restaurantId) {
        this.reviewId = "REV-" + LocalDateTime.now().toEpochSecond(null);
        this.setTitle(title);
        this.setDescription(description);
        this.setStars(stars);
        this.customerId = Objects.requireNonNull(customerId, "Customer ID cannot be null");
        this.restaurantId = Objects.requireNonNull(restaurantId, "Restaurant ID cannot be null");
        this.creationDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
    }

    // Validation and setters
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Review title cannot be empty");
        }
        if (title.length() > 100) {
            throw new IllegalArgumentException("Title cannot exceed 100 characters");
        }
        this.title = title.trim();
        this.updateDate = LocalDateTime.now();
    }

    public void setDescription(String description) {
        this.description = description != null ? description.trim() : "";
        this.updateDate = LocalDateTime.now();
    }

    public void setStars(int stars) {
        if (stars < 1 || stars > 5) {
            throw new IllegalArgumentException("Stars must be between 1 and 5");
        }
        this.stars = stars;
        this.updateDate = LocalDateTime.now();
    }

    // Business methods
    public boolean isPositiveReview() {
        return stars >= 4;
    }

    public boolean isCriticalReview() {
        return stars <= 2;
    }

    public String getSummary() {
        return String.format("%s (%d/5): %.50s%s", 
            title, 
            stars,
            description,
            description.length() > 50 ? "..." : "");
    }

    // Getters
    public String getReviewId() { return reviewId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getStars() { return stars; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public LocalDateTime getUpdateDate() { return updateDate; }
    public String getCustomerId() { return customerId; }
    public String getRestaurantId() { return restaurantId; }

    @Override
    public String toString() {
        return String.format("Review[%s] by %s for %s: %d stars", 
            reviewId, customerId, restaurantId, stars);
    }

    public void attachToRestaurant(Restaurant restaurant) {
        if (restaurant != null) {
            restaurant.addReview(this);
        }
    }

	public void setReviewId(String id) {
	      if (id == null || id.isBlank()) {
	            throw new IllegalArgumentException("reviewId cannot be null or empty");
	        }
	        this.reviewId = id;
	    }

	public void setCreationDate(LocalDateTime lDTime) {
		this.creationDate=lDTime;
		
	}

	public void setUpdateDate(LocalDateTime lDTime) {
		// TODO Auto-generated method stub
		this.updateDate=lDTime;
		
	}
	

		
	
}