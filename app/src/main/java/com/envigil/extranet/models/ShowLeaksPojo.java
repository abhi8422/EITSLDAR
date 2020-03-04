package com.envigil.extranet.models;

public class ShowLeaksPojo {
    String tagNO,subArea,serviceType,componentName,areaName;
    String repairType,leakPathName,leakCritical,leakEssential;
    float componentSize,leakRate,repairRate;
    int leakTypeID;

    public ShowLeaksPojo(String tagNO, String subArea, String areaName, String serviceType, String componentName, String repairType, String leakPathName, float componentSize, float leakRate, float repairRate, int leakTypeID, String leakCritical, String leakEssential) {
        this.tagNO = tagNO;
        this.subArea = subArea;
        this.serviceType = serviceType;
        this.componentName = componentName;
        this.repairType = repairType;
        this.componentSize = componentSize;
        this.leakRate = leakRate;
        this.repairRate = repairRate;
        this.leakPathName = leakPathName;
        this.leakTypeID = leakTypeID;
        this.leakCritical = leakCritical;
        this.leakEssential = leakEssential;
        this.areaName = areaName;
    }

    public String getTagNO() {
        return tagNO;
    }

    public void setTagNO(String tagNO) {
        this.tagNO = tagNO;
    }

    public String getSubArea() {
        return subArea;
    }

    public void setSubArea(String subArea) {
        this.subArea = subArea;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getRepairType() {
        return repairType;
    }

    public void setRepairType(String repairType) {
        this.repairType = repairType;
    }

    public float getComponentSize() {
        return componentSize;
    }

    public void setComponentSize(float componentSize) {
        this.componentSize = componentSize;
    }

    public float getLeakRate() {
        return leakRate;
    }

    public void setLeakRate(float leakRate) {
        this.leakRate = leakRate;
    }

    public float getRepairRate() {
        return repairRate;
    }

    public void setRepairRate(float repairRate) {
        this.repairRate = repairRate;
    }

    public String getLeakPathName() {
        return leakPathName;
    }

    public void setLeakPathName(String leakPathName) {
        this.leakPathName = leakPathName;
    }

    public int getLeakTypeID() {
        return leakTypeID;
    }

    public void setLeakTypeID(int leakTypeID) {
        this.leakTypeID = leakTypeID;
    }

    public String isLeakCritical() {
        return leakCritical;
    }

    public void setLeakCritical(String leakCritical) {
        this.leakCritical = leakCritical;
    }

    public String isLeakEssential() {
        return leakEssential;
    }

    public void setLeakEssential(String leakEssential) {
        this.leakEssential = leakEssential;
    }
}
