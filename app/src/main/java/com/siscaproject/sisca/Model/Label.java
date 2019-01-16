package com.siscaproject.sisca.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Label {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("notes")
    @Expose
    private String notes;

    public Label(String name, String type, String notes) {
        this.name = name;
        this.type = type;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getNotes() {
        return notes;
    }
}
