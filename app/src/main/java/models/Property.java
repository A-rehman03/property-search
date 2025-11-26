package models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Property implements Parcelable {

    private int id;
    private int adminId;
    private String title;
    private String description;
    private double price;
    private String address;
    private String type;
    private String phoneNumber;
    private List<String> imagePaths;

    public Property() {}

    public Property(int id, int adminId, String title, String description, double price, String address, String type, String phoneNumber, List<String> imagePaths) {
        this.id = id;
        this.adminId = adminId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.address = address;
        this.type = type;
        this.phoneNumber = phoneNumber;
        this.imagePaths = imagePaths;
    }

    protected Property(Parcel in) {
        id = in.readInt();
        adminId = in.readInt();
        title = in.readString();
        description = in.readString();
        price = in.readDouble();
        address = in.readString();
        type = in.readString();
        phoneNumber = in.readString();
        imagePaths = in.createStringArrayList();
    }

    public static final Creator<Property> CREATOR = new Creator<Property>() {
        @Override
        public Property createFromParcel(Parcel in) {
            return new Property(in);
        }

        @Override
        public Property[] newArray(int size) {
            return new Property[size];
        }
    };

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public List<String> getImagePaths() { return imagePaths; }
    public void setImagePaths(List<String> imagePaths) { this.imagePaths = imagePaths; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(adminId);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeString(address);
        dest.writeString(type);
        dest.writeString(phoneNumber);
        dest.writeStringList(imagePaths);
    }

    public String getImageUrl() {
        if (imagePaths != null && !imagePaths.isEmpty()) {
            return imagePaths.get(0);
        }
        return null;
    }

    public String getLocation() {
        return address;


    }
}
