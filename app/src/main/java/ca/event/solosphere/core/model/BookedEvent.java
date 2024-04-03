package ca.event.solosphere.core.model;

public class BookedEvent {

    private String userID;
    private String eventID;
    private int quantity;

    public BookedEvent() {
    }

    public BookedEvent(String userID, String eventID) {
        this.userID = userID;
        this.eventID = eventID;
    }

    public String getUserID() {
        return userID;
    }

    public String getEventID() {
        return eventID;
    }
}
