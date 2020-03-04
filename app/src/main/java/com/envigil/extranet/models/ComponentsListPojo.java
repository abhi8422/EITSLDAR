package com.envigil.extranet.models;

public class ComponentsListPojo {
    int CompID;
    int CompTypeID;
    String CompName,SubName,AreaName,CompTypeName;
    float InvSize,Backread,InvReading;
    String Inspected;
    int InvID;
    int SubID,RouteId;
    String InvLocation,InvTag;
    boolean Critical;

    public float getReading() {
        return Reading;
    }

    float Reading;
    int SkippedID;
    public int getCompTypeID() {
        return CompTypeID;
    }

    public void setCompTypeID(int compTypeID) {
        CompTypeID = compTypeID;
    }

    public int getSkippedID() {
        return SkippedID;
    }

    public void setSkippedID(int skippedID) {
        SkippedID = skippedID;
    }

    public int getSubID() {
        return SubID;
    }

    public void setSubID(int subID) {
        SubID = subID;
    }

    public boolean getCritical() {
        return Critical;
    }

    public void setCritical(boolean critical) {
        Critical = critical;
    }

    public String getCompName() {
        return CompName;
    }

    public void setCompName(String compName) {
        CompName = compName;
    }

    public String getInvLocation() {
        return InvLocation;
    }

    public void setInvLocation(String invLocation) {
        InvLocation = invLocation;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public ComponentsListPojo(int compID,int subID,String compName, String invTag, String inspected, float invSize, String invLocation) {
        CompID = compID;
        SubID = subID;
        CompName = compName;

        InvTag = invTag;
        InvSize = invSize;
        Inspected = inspected;
        InvLocation = invLocation;
    }

    public int getRouteId() {
        return RouteId;
    }

    public void setRouteId(int routeId) {
        RouteId = routeId;
    }

    public int getCompID() {
        return CompID;
    }

    public void setCompID(int compID) {
        CompID = compID;
    }

    public String getInvTag() {
        return InvTag;
    }

    public void setInvTag(String invTag) {
        InvTag = invTag;
    }

    public float getInvSize() {
        return InvSize;
    }

    public void setInvSize(float invSize) {
        InvSize = invSize;
    }

    public String getInspected() {
        return Inspected;
    }

    public void setInspected(String inspected) {
        Inspected = inspected;
    }

    public String getComponentLocation() {
        return InvLocation;
    }

    public void setComponentLocation(String invLocation) {
        InvLocation = invLocation;
    }

    public int getInvID() {
        return InvID;
    }

    public void setInvID(int invID) {
        InvID = invID;
    }

    public float getBackread() {
        return Backread;
    }

    public void setBackread(float backread) {
        Backread = backread;
    }

    public String getSubName() {
        return SubName;
    }

    public void setSubName(String subName) {
        SubName = subName;
    }

    public float getInvReading() {
        return InvReading;
    }
    public void setInvReading(float invReading) {
        InvReading = invReading;
    }

    public void setReading(float reading) {
        Reading = reading;
    }

    public String getCompTypeName() {
        return CompTypeName;
    }

    public void setCompTypeName(String compTypeName) {
        CompTypeName = compTypeName;
    }
}
