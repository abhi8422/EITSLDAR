package com.envigil.extranet.models;

public class ShowLeaksPojo {
    String tagNO,subArea,serviceType,componentName,areaName;
    String repairType;
    String leakPathName;
    String leakCritical;
    String leakEssential;
    String RepairTypeName;
    String RepairDate;

    public String getLeakDate() {
        return LeakDate;
    }

    public void setLeakDate(String leakDate) {
        LeakDate = leakDate;
    }

    String LeakDate;
    float componentSize,leakRate,repairRate;
    int leakTypeID;
    int RouteID;

    public int getLeakID() {
        return LeakID;
    }

    public void setLeakID(int leakID) {
        LeakID = leakID;
    }

    int LeakID;

    public String getRepairTypeName() {
        return RepairTypeName;
    }

    public void setRepairTypeName(String repairTypeName) {
        RepairTypeName = repairTypeName;
    }

    public String getRepairDate() {
        return RepairDate;
    }

    public void setRepairDate(String repairDate) {
        RepairDate = repairDate;
    }

    public int getEmpID() {
        return EmpID;
    }

    public void setEmpID(int empID) {
        EmpID = empID;
    }

    int EmpID;


    public int getCompID() {
        return CompID;
    }

    public void setCompID(int compID) {
        CompID = compID;
    }

    int CompID;

    public int getInvId() {
        return InvId;
    }

    public void setInvId(int invId) {
        InvId = invId;
    }

    int InvId;
    String Path;
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

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }


    public int getRouteID() {
        return RouteID;
    }

    public void setRouteID(int routeID) {
        RouteID = routeID;
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
