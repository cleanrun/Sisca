package com.siscaproject.sisca.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Asset {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("asset_tag")
    @Expose
    private String asset_tag;

    @SerializedName("company_id")
    @Expose
    private String company_id;

    @SerializedName("model_id")
    @Expose
    private String model_id;

    @SerializedName("label_id")
    @Expose
    private String label_id;

    @SerializedName("serial")
    @Expose
    private String serial;

    @SerializedName("purchase_date")
    @Expose
    private String purchase_date;

    @SerializedName("supplier_id")
    @Expose
    private String supplier_id;

    @SerializedName("order_number")
    @Expose
    private String order_number;

    @SerializedName("purchase_cost")
    @Expose
    private String purchase_cost;

    @SerializedName("warranty_months")
    @Expose
    private String warranty_months;

    @SerializedName("notes")
    @Expose
    private String notes;

    @SerializedName("location_id")
    @Expose
    private String location_id;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("assigned_to")
    @Expose
    private String assigned_to;

    public Asset(String name, String asset_tag, String company_id, String model_id, String label_id, String serial,
                 String purchase_date, String supplier_id, String order_number, String purchase_cost, String warranty_months,
                 String notes, String location_id, String image, String assigned_to) {
        this.name = name;
        this.asset_tag = asset_tag;
        this.company_id = company_id;
        this.model_id = model_id;
        this.label_id = label_id;
        this.serial = serial;
        this.purchase_date = purchase_date;
        this.supplier_id = supplier_id;
        this.order_number = order_number;
        this.purchase_cost = purchase_cost;
        this.warranty_months = warranty_months;
        this.notes = notes;
        this.location_id = location_id;
        this.image = image;
        this.assigned_to = assigned_to;
    }

    public String getName() {
        return name;
    }

    public String getAsset_tag() {
        return asset_tag;
    }

    public String getCompany_id() {
        return company_id;
    }

    public String getModel_id() {
        return model_id;
    }

    public String getLabel_id() {
        return label_id;
    }

    public String getSerial() {
        return serial;
    }

    public String getPurchase_date() {
        return purchase_date;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public String getPurchase_cost() {
        return purchase_cost;
    }

    public String getWarranty_months() {
        return warranty_months;
    }

    public String getNotes() {
        return notes;
    }

    public String getLocation_id() {
        return location_id;
    }

    public String getImage() {
        return image;
    }

    public String getAssigned_to() {
        return assigned_to;
    }
}
