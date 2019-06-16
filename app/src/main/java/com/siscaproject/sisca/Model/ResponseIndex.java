package com.siscaproject.sisca.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseIndex<Model> {
    @SerializedName("rows")
    private ArrayList<Model> rows;

    @SerializedName("data")
    private ArrayList<Model> data;

    @SerializedName("total")
    private int total;

    public ArrayList getRows() {
        return rows;
    }

    public void setRows(ArrayList rows) {
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<Model> getData() {
        return data;
    }

    public void setData(ArrayList<Model> data) {
        this.data = data;
    }
}
