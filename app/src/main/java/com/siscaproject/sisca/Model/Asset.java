package com.siscaproject.sisca.Model;

public class Asset {
    private int photo;
    private String name, id, desc, idLocation, status;

    public Asset(int photo, String name, String id, String desc, String idLocation) {
        this.photo = photo;
        this.name = name;
        this.id = id;
        this.desc = desc;
        this.idLocation = idLocation;
    }

    public int getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public String getIdLocation() {
        return idLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
