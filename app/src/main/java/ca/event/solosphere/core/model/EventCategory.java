package ca.event.solosphere.core.model;

public class EventCategory {
    private int categoryImage;
    private String categoryTitle;

    public EventCategory(int categoryImage, String categoryTitle) {
        this.categoryImage = categoryImage;
        this.categoryTitle = categoryTitle;
    }

    public int getCategoryImage() {
        return categoryImage;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }
}
