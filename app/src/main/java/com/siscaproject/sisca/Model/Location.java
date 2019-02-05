package com.siscaproject.sisca.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("parent_id")
    @Expose
    private String parent_id;

    @SerializedName("manager_id")
    @Expose
    private String manager_id;

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

    @SerializedName("zip")
    @Expose
    private String zip;

    public Location(String id, String name, String parent_id, String manager_id, String address, String city,
                    String state, String country, String phone, String zip) {
        this.id = id;
        this.name = name;
        this.parent_id = parent_id;
        this.manager_id = manager_id;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.phone = phone;
        this.zip = zip;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getParent_id() {
        return parent_id;
    }

    public String getManager_id() {
        return manager_id;
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

    public String getZip() {
        return zip;
    }

    @Override
    public String toString() {
        return name;
    }
}
