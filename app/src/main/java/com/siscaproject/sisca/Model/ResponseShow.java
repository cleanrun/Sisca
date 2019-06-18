package com.siscaproject.sisca.Model;

import com.google.gson.annotations.SerializedName;

public class ResponseShow<Model> {
    @SerializedName("data")
    private Model data;

    public Model getData() {
        return data;
    }

    public void setData(Model data) {
        this.data = data;
    }
}
