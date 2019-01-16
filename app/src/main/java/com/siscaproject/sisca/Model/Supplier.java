package com.siscaproject.sisca.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Supplier {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("fax")
    @Expose
    private String fax;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("contact")
    @Expose
    private String contact;

    @SerializedName("notes")
    @Expose
    private String notes;

    @SerializedName("zip")
    @Expose
    private String zip;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("image")
    @Expose
    private String image;

    public Supplier(String name, String address, String city, String state, String country, String phone, String fax,
                    String email, String contact, String notes, String zip, String url, String image) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.phone = phone;
        this.fax = fax;
        this.email = email;
        this.contact = contact;
        this.notes = notes;
        this.zip = zip;
        this.url = url;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
    }

    public String getFax() {
        return fax;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public String getNotes() {
        return notes;
    }

    public String getZip() {
        return zip;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }
}
