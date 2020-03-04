package com.envigil.extranet;

public class Downlodedpojo {

    String title,workorderdes,facilitydes,ruledes,downloaddes;

    public Downlodedpojo(String title, String workorderdes, String facilitydes, String ruledes, String downloaddes) {
        this.title = title;
        this.workorderdes = workorderdes;
        this.facilitydes = facilitydes;
        this.ruledes = ruledes;
        this.downloaddes = downloaddes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWorkorderdes() {
        return workorderdes;
    }

    public void setWorkorderdes(String workorderdes) {
        this.workorderdes = workorderdes;
    }

    public String getFacilitydes() {
        return facilitydes;
    }

    public void setFacilitydes(String facilitydes) {
        this.facilitydes = facilitydes;
    }

    public String getRuledes() {
        return ruledes;
    }

    public void setRuledes(String ruledes) {
        this.ruledes = ruledes;
    }

    public String getDownloaddes() {
        return downloaddes;
    }

    public void setDownloaddes(String downloaddes) {
        this.downloaddes = downloaddes;
    }
}
