package ca.event.solosphere.core.model;

public class Attendees {

    String fullName;
    String profilePicture;
    String uid;
    String currentState;

    public Attendees() {

    }

    public Attendees(String fullName, String profilePicture, String uid) {
        this.fullName = fullName;
        this.profilePicture = profilePicture;
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    @Override
    public String toString() {
        return "Attendees{" +
                "fullName='" + fullName + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", uid='" + uid + '\'' +
                ", currentState='" + currentState + '\'' +
                '}';
    }
}
