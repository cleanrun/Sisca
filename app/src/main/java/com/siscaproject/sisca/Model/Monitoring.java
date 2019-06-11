package com.siscaproject.sisca.Model;

public class Monitoring {
    private String idAsset, status;
    private boolean isChecked;

    public Monitoring(String idAsset, String status, boolean isChecked) {
        this.idAsset = idAsset;
        this.status = status;
        this.isChecked = isChecked;
    }

    public String getIdAsset() {
        return idAsset;
    }

    public void setIdAsset(String idAsset) {
        this.idAsset = idAsset;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
