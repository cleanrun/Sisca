package com.siscaproject.sisca.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Depreciation {
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

    public String getName() {
        return name;
    }

    public String getMonths() {
        return months;
    }
}
