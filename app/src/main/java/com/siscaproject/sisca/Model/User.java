package com.siscaproject.sisca.Model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class User {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("first_name")
    @Expose
    private String first_name;

    @SerializedName("last_name")
    @Expose
    private String last_name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("website")
    @Expose
    private String website;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("manager_id")
    @Expose
    private String manager_id;

    @SerializedName("location_id")
    @Expose
    private String location_id;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("job_title")
    @Expose
    private String job_title;

    @SerializedName("company_id")
    @Expose
    private String company_id;

    @SerializedName("employee_num")
    @Expose
    private String employee_num;

    @SerializedName("image")
    @Expose
    private String image;

    public User(String name, String first_name, String last_name, String email, String password,
                String address, String website, String country, String manager_id, String location_id,
                String phone, String job_title, String company_id, String employee_num, String image) {
        this.name = name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.website = website;
        this.country = country;
        this.manager_id = manager_id;
        this.location_id = location_id;
        this.phone = phone;
        this.job_title = job_title;
        this.company_id = company_id;
        this.employee_num = employee_num;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getWebsite() {
        return website;
    }

    public String getCountry() {
        return country;
    }

    public String getManager_id() {
        return manager_id;
    }

    public String getLocation_id() {
        return location_id;
    }

    public String getPhone() {
        return phone;
    }

    public String getJob_title() {
        return job_title;
    }

    public String getCompany_id() {
        return company_id;
    }

    public String getEmployee_num() {
        return employee_num;
    }

    public String getImage() {
        return image;
    }
}
