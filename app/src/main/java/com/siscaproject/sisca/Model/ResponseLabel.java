package com.siscaproject.sisca.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseLabel {
    @SerializedName("rows")
    private ArrayList<Label> rows;

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
}
