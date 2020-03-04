package com.envigil.extranet;

public class DataPojo {

    String tags, locations, compId, componentSize, inspectionStatus;

    public DataPojo(String tags, String locations, String compId, String componentSize, String inspectionStatus) {
        this.tags = tags;
        this.locations = locations;
        this.compId = compId;
        this.componentSize = componentSize;
        this.inspectionStatus = inspectionStatus;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getComponentSize() {
        return componentSize;
    }

    public void setComponentSize(String componentSize) {
        this.componentSize = componentSize;
    }

    public String getInspectionStatus() {
        return inspectionStatus;
    }

    public void setInspectionStatus(String inspectionStatus) {
        this.inspectionStatus = inspectionStatus;
    }
}
