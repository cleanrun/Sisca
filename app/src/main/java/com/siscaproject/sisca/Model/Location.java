package com.siscaproject.sisca.Model;

import java.util.List;

public class Location {
    private String name, id, picName;
    private int asetSize;
    private List<Asset> assetList;

    public Location(String name, String id, String picName) {
        this.name = name;
        this.id = id;
        this.picName = picName;
    }

    public Location(String name, String id, String picName, List<Asset> assetList) {
        this.name = name;
        this.id = id;
        this.picName = picName;
        this.assetList = assetList;
    }

    public Location(String name, String id, int asetSize) {
        this.name = name;
        this.id = id;
        this.asetSize = asetSize;
    }

    public int getAsetSize() {
        return asetSize;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public void setAssetList(List<Asset> assetList) {
        this.assetList = assetList;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<Asset> getAssetList() {
        return assetList;
    }
}
