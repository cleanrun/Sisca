package com.siscaproject.sisca.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Manufacturer {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("support_url")
    @Expose
    private String support_url;

    @SerializedName("support_phone")
    @Expose
    private String support_phone;

    @SerializedName("support_email")
    @Expose
    private String support_email;

    @SerializedName("image")
    @Expose
    private String image;

    public Manufacturer(String name, String url, String support_url, String support_phone, String support_email, String image) {
        this.name = name;
        this.url = url;
        this.support_url = support_url;
        this.support_phone = support_phone;
        this.support_email = support_email;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getSupport_url() {
        return support_url;
    }

    public String getSupport_phone() {
        return support_phone;
    }

    public String getSupport_email() {
        return support_email;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return name;
    }
}
