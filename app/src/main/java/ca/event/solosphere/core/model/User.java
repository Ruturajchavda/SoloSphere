package ca.event.solosphere.core.model;

import java.io.Serializable;

public class User implements Serializable {

    String fullName;
    String phone;
    String email;
    String profilePicture;
    String uid;
    String deviceToken;
    String userType;
    String currentState;
    String lastMessage;

    public User() {

    }

    public User(String fullName, String phone, String email, String profilePicture, String uid, String deviceToken, String userType) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.profilePicture = profilePicture;
        this.uid = uid;
        this.deviceToken = deviceToken;
        this.userType = userType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", uid='" + uid + '\'' +
                ", deviceToken='" + deviceToken + '\'' +
                ", userType='" + userType + '\'' +
                ", currentState='" + currentState + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                '}';
    }
}
