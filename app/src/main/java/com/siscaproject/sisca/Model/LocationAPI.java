package com.siscaproject.sisca.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LocationAPI {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("parent_id")
    @Expose
    private int parent_id;

    @SerializedName("manager_id")
    @Expose
    private int manager_id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("address")
    @Expose
    private String address;

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

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("deleted_at")
    @Expose
    private String deleted_at;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("asset_count")
    @Expose
    private int asset_count;

    @SerializedName("asset")
    @Expose
    private ArrayList<AssetMutasi> asset;

    @SerializedName("manager")
    @Expose
    private User manager;

    public LocationAPI(int id, int parent_id, int manager_id, String name, String address, String state, String country, String phone, String zip, String image, String deleted_at, String created_at, String updated_at, int asset_count) {
        this.id = id;
        this.parent_id = parent_id;
        this.manager_id = manager_id;
        this.name = name;
        this.address = address;
        this.state = state;
        this.country = country;
        this.phone = phone;
        this.zip = zip;
        this.image = image;
        this.deleted_at = deleted_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.asset_count = asset_count;
    }

    public int getId() {
        return id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public int getManager_id() {
        return manager_id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
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

    public String getImage() {
        return image;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public int getAsset_count() {
        return asset_count;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public ArrayList<AssetMutasi> getAsset() {
        return asset;
    }

    public void setAsset(ArrayList<AssetMutasi> asset) {
        this.asset = asset;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }
}
