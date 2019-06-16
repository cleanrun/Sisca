package com.siscaproject.sisca.Model;

import java.util.List;

public class LocationModel {
    private String name, id, picName;
    private int asetSize;
    private List<AssetModel> assetList;

    public LocationModel(String name, String id, String picName) {
        this.name = name;
        this.id = id;
        this.picName = picName;
    }

    public LocationModel(String name, String id, String picName, List<AssetModel> assetList) {
        this.name = name;
        this.id = id;
        this.picName = picName;
        this.assetList = assetList;
    }

    public LocationModel(String name, String id, int asetSize) {
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

    public void setAssetList(List<AssetModel> assetList) {
        this.assetList = assetList;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<AssetModel> getAssetList() {
        return assetList;
    }
}
