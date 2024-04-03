package ca.event.solosphere.core.model;

public class LikedEvent {

    private String userID;
    private String eventID;

    public LikedEvent() {
    }

    public LikedEvent(String userID, String eventID) {
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
