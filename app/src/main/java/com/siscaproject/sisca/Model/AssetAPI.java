package com.siscaproject.sisca.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssetAPI {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("asset_id")
    @Expose
    private String asset_id;

    @SerializedName("category_id")
    @Expose
    private String category_id;

    @SerializedName("location_id")
    @Expose
    private String location_id;

    @SerializedName("pic_id")
    @Expose
    private String pic_id;

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

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("history")
    @Expose
    private String history;

    @SerializedName("deleted_at")
    @Expose
    private String deleted_at;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    public AssetAPI(int id, String asset_id, String category_id, String location_id, String pic_id, String name, String description, String date_purchase, String price, String condition, String image, String history, String deleted_at, String created_at, String updated_at) {
        this.id = id;
        this.asset_id = asset_id;
        this.category_id = category_id;
        this.location_id = location_id;
        this.pic_id = pic_id;
        this.name = name;
        this.description = description;
        this.date_purchase = date_purchase;
        this.price = price;
        this.condition = condition;
        this.image = image;
        this.history = history;
        this.deleted_at = deleted_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public String getAsset_id() {
        return asset_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getLocation_id() {
        return location_id;
    }

    public String getPic_id() {
        return pic_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDate_purchase() {
        return date_purchase;
    }

    public String getPrice() {
        return price;
    }

    public String getCondition() {
        return condition;
    }

    public String getImage() {
        return image;
    }

    public String getHistory() {
        return history;
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
}
