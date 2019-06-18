package com.siscaproject.sisca.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// Harusnya namanya AssetAPI, sementara aja

public class AssetMutasi {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("asset_id")
    @Expose
    private String asset_id;

    @SerializedName("asset_rfid")
    @Expose
    private String asset_rfid;

    @SerializedName("category_id")
    @Expose
    private int category_id;

    @SerializedName("location_id")
    @Expose
    private int location_id;

    @SerializedName("pic_id")
    @Expose
    private int pic_id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("date_purchase")
    @Expose
    private String date_purchase;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("condition")
    @Expose
    private String condition;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("location")
    @Expose
    private LocationAPI location;

    @SerializedName("pic")
    @Expose
    private User pic;

    /*
    @SerializedName("category")
    @Expose
    private Category category
    */

    public AssetMutasi(int id, String asset_id, String asset_rfid, int category_id, int location_id, int pic_id, String name, String description, String date_purchase, String price, String condition, String created_at, String updated_at, LocationAPI location, User pic) {
        this.id = id;
        this.asset_id = asset_id;
        this.asset_rfid = asset_rfid;
        this.category_id = category_id;
        this.location_id = location_id;
        this.pic_id = pic_id;
        this.name = name;
        this.description = description;
        this.date_purchase = date_purchase;
        this.price = price;
        this.condition = condition;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.location = location;
        this.pic = pic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(String asset_id) {
        this.asset_id = asset_id;
    }

    public String getAsset_rfid() {
        return asset_rfid;
    }

    public void setAsset_rfid(String asset_rfid) {
        this.asset_rfid = asset_rfid;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public int getPic_id() {
        return pic_id;
    }

    public void setPic_id(int pic_id) {
        this.pic_id = pic_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate_purchase() {
        return date_purchase;
    }

    public void setDate_purchase(String date_purchase) {
        this.date_purchase = date_purchase;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public LocationAPI getLocation() {
        return location;
    }

    public void setLocation(LocationAPI location) {
        this.location = location;
    }

    public User getPic() {
        return pic;
    }

    public void setPic(User pic) {
        this.pic = pic;
    }
}
