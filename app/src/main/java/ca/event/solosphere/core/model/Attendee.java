package ca.event.solosphere.core.model;

public class Attendee {

    String userID;
    String eventID;
    boolean isCheckedIn;
    int totalTickets;

    public Attendee(){}

    public Attendee(String userID, String eventID, boolean isCheckedIn, int totalTickets) {
        this.userID = userID;
        this.eventID = eventID;
        this.isCheckedIn = isCheckedIn;
        this.totalTickets = totalTickets;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public boolean isCheckedIn() {
        return isCheckedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        isCheckedIn = checkedIn;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    @Override
    public String toString() {
        return "Attendee{" +
                "userID='" + userID + '\'' +
                ", eventID='" + eventID + '\'' +
                ", isCheckedIn=" + isCheckedIn +
                ", totalTickets=" + totalTickets +
                '}';
    }
}
