package com.envigil.extranet.models;
/* Creaed By : Mayuri Sagar Bhamare
   Date : 22-07-2019 11:48
   Project Name : MyApplication */

import java.util.Date;

public class Subarea {

    int AreaID;
    int RouteID;
    int SubID;
    String SubName;
    String AreaName;

    public String getSubDesc() {
        return SubDesc;
    }

    public void setSubDesc(String subDesc) {
        SubDesc = subDesc;
    }

    String SubDesc;
    boolean Inspected;
    int SubOrder;
    Date TimeStamp;
    boolean BackgroundEntered;
    float Background;

    public Subarea(int areaID, int routeID, int subID, String subName, boolean inspected, int subOrder, Date timeStamp, boolean backgroundEntered, float background) {
        AreaID = areaID;
        RouteID = routeID;
        SubID = subID;
        SubName = subName;
        Inspected = inspected;
        SubOrder = subOrder;
        TimeStamp = timeStamp;
        BackgroundEntered = backgroundEntered;
        Background = background;
    }

    public Subarea() {
    }

    public int getAreaID() {
        return AreaID;
    }

    public void setAreaID(int areaID) {
        AreaID = areaID;
    }

    public int getRouteID() {
        return RouteID;
    }

    public void ID(int routeID) {
        RouteID = routeID;
    }

    public int getSubID() {
        return SubID;
    }

    public void setSubID(int subID) {
        SubID = subID;
    }

    public String getSubName() {
        return SubName;
    }

    public void setSubName(String subName) {
        SubName = subName;
    }

    public boolean isInspected() {
        return Inspected;
    }

    public void setInspected(boolean inspected) {
        Inspected = inspected;
    }

    public int getSubOrder() {
        return SubOrder;
    }

    public void setSubOrder(int subOrder) {
        SubOrder = subOrder;
    }

    public Date getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        TimeStamp = timeStamp;
    }

    public boolean isBackgroundEntered() {
        return BackgroundEntered;
    }

    public void setBackgroundEntered(boolean backgroundEntered) {
        BackgroundEntered = backgroundEntered;
    }

    public float getBackground() {
        return Background;
    }

    public void setBackground(float background) {
        Background = background;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }
}
