package ca.event.solosphere.core.model;

import java.io.Serializable;

public class Event implements Serializable {
    private String orgID;
    private String eventID;
    private String eventImage;
    private String name;
    private String desc;
    private double price;
    private int totalSpots;
    private String category;
    private String location;
    private String city;
    private String state;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;

    private int attendees;

    public Event() {
    }

    public Event(String orgID, String eventID, String eventImage, String name, String desc, double price, int totalSpots, String category, String location, String city, String state, String startDate, String startTime, String endDate, String endTime, int attendees) {
        this.orgID = orgID;
        this.eventID = eventID;
        this.eventImage = eventImage;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.totalSpots = totalSpots;
        this.category = category;
        this.location = location;
        this.city = city;
        this.state = state;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.attendees = attendees;
    }

    public String getOrgID() {
        return orgID;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTotalSpots() {
        return totalSpots;
    }

    public void setTotalSpots(int totalSpots) {
        this.totalSpots = totalSpots;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getAttendees() {
        return attendees;
    }

    public void setAttendees(int attendees) {
        this.attendees = attendees;
    }

    @Override
    public String toString() {
        return "Event{" +
                "orgID='" + orgID + '\'' +
                ", eventID='" + eventID + '\'' +
                ", eventImage='" + eventImage + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", price=" + price +
                ", totalSpots=" + totalSpots +
                ", category='" + category + '\'' +
                ", location='" + location + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", startDate='" + startDate + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endDate='" + endDate + '\'' +
                ", endTime='" + endTime + '\'' +
                ", attendees=" + attendees +
                '}';
    }
}
