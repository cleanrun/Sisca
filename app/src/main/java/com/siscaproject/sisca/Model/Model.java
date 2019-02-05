package com.siscaproject.sisca.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("model_number")
    @Expose
    private String model_number;

    @SerializedName("manufacturer_id")
    @Expose
    private String manufacturer_id;

    @SerializedName("category_id")
    @Expose
    private String category_id;

    @SerializedName("depreciation_id")
    @Expose
    private String depreciation_id;

    @SerializedName("notes")
    @Expose
    private String notes;

    @SerializedName("image")
    @Expose
    private String image;

    public Model(String id, String name, String model_number, String manufacturer_id, String category_id,
                 String depreciation_id, String notes, String image) {
        this.id = id;
        this.name = name;
        this.model_number = model_number;
        this.manufacturer_id = manufacturer_id;
        this.category_id = category_id;
        this.depreciation_id = depreciation_id;
        this.notes = notes;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getModel_number() {
        return model_number;
    }

    public String getManufacturer_id() {
        return manufacturer_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getDepreciation_id() {
        return depreciation_id;
    }

    public String getNotes() {
        return notes;
    }

    public String getImage() {
        return image;
    }
}
