package ca.event.solosphere.core.model;

import java.io.Serializable;

public class Attendee implements Serializable {

    String userID;
    String eventID;
    String isCheckedIn;
    int totalTickets;

    public Attendee(){}

    public Attendee(String userID, String eventID, String isCheckedIn, int totalTickets) {
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

    public String getIsCheckedIn() {
        return isCheckedIn;
    }

    public void setIsCheckedIn(String isCheckedIn) {
        this.isCheckedIn = isCheckedIn;
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
                ", isCheckedIn='" + isCheckedIn + '\'' +
                ", totalTickets=" + totalTickets +
                '}';
    }
}
