package com.siscaproject.sisca.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Depreciation {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("months")
    @Expose
    private String months;

    public Depreciation(String name, String months) {
        this.name = name;
        this.months = months;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMonths() {
        return months;
    }

    @Override
    public String toString() {
        return name;
    }
}
