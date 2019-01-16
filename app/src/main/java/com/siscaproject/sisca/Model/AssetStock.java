package com.siscaproject.sisca.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssetStock {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("company_id")
    @Expose
    private String company_id;

    @SerializedName("category_id")
    @Expose
    private String category_id;

    @SerializedName("manufacturer_id")
    @Expose
    private String manufacturer_id;

    @SerializedName("location_id")
    @Expose
    private String location_id;

    @SerializedName("model_no")
    @Expose
    private String model_no;

    @SerializedName("item_no")
    @Expose
    private String item_no;

    @SerializedName("order_no")
    @Expose
    private String order_no;

    @SerializedName("purchase_date")
    @Expose
    private String purchase_date;

    @SerializedName("purchase_cost")
    @Expose
    private String purchase_cost;

    @SerializedName("quantity")
    @Expose
    private String quantity;

    @SerializedName("image")
    @Expose
    private String image;

    public AssetStock(String name, String company_id, String category_id, String manufacturer_id,
                      String location_id, String model_no, String item_no, String order_no, String purchase_date,
                      String purchase_cost, String quantity, String image) {
        this.name = name;
        this.company_id = company_id;
        this.category_id = category_id;
        this.manufacturer_id = manufacturer_id;
        this.location_id = location_id;
        this.model_no = model_no;
        this.item_no = item_no;
        this.order_no = order_no;
        this.purchase_date = purchase_date;
        this.purchase_cost = purchase_cost;
        this.quantity = quantity;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getCompany_id() {
        return company_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getManufacturer_id() {
        return manufacturer_id;
    }

    public String getLocation_id() {
        return location_id;
    }

    public String getModel_no() {
        return model_no;
    }

    public String getItem_no() {
        return item_no;
    }

    public String getOrder_no() {
        return order_no;
    }

    public String getPurchase_date() {
        return purchase_date;
    }

    public String getPurchase_cost() {
        return purchase_cost;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getImage() {
        return image;
    }
}
