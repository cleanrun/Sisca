package com.siscaproject.sisca.Model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class User {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("username")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("locale")
    @Expose
    private String locale;

    public User(int id, String name, String email, String created_at, String updated_at, String locale) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.locale = locale;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getLocale() {
        return locale;
    }
}
