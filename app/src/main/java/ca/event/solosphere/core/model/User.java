package ca.event.solosphere.core.model;

public class User{

    String fullName;
    String phone;
    String email;
    String key;

    public User() {

    }

    public User(String fullName, String phone, String email, String key) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.key = key;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
