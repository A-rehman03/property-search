package models;

public class User {

    private int id;
    private String username;
    private String email;
    private String password;
    private String role;          // "admin" or "user"
    private String phoneNumber;

    public User() {}

    public User(int id, String username, String email, String password, String role, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phoneNumber = phoneNumber;
    }

    public User(String username, String email, String password, String phone, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phone;
        this.role = role;
    }


    // Getters & Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public byte[] getName() {
        return null;
    }

    public void setName(byte[] name) {
        this.username = new String(name);
    }

    public byte[] getPhone() {
        return null;
    }

    public void setPhone(byte[] phone) {
        this.phoneNumber = new String(phone);
    }
}
