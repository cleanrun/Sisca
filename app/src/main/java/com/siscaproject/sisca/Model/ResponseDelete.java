package com.siscaproject.sisca.Model;

import com.google.gson.annotations.SerializedName;

public class ResponseDelete {
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }
}
