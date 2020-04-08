package com.envigil.extranet;

public class SubAreasPojo {
    int SubId;
    int RouteId,cnt;
    Integer inspected;
    String SubArea,AreaName,SubDesc;
    float Background;
    String Date;
    float per;


    public float getPer() {
        return per;
    }

    public void setPer(float per) {
        this.per = per;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public SubAreasPojo(int subId, Integer inspected, String subArea, float background, String date) {
        SubId = subId;
        this.inspected = inspected;
        SubArea = subArea;
        Background = background;
        Date = date;
    }

    public String getSubDesc() {
        return SubDesc;
    }

    public void setSubDesc(String subDesc) {
        SubDesc = subDesc;
    }

    public int getRouteId() {
        return RouteId;
    }

    public void setRouteId(int routeId) {
        RouteId = routeId;
    }

    public int getSubId() {
        return SubId;
    }

    public void setSubId(int subId) {
        SubId = subId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }


    public Integer getInspected() {
        return inspected;
    }

    public void setInspected(Integer inspected) {
        this.inspected = inspected;
    }

    public String getSubArea() {
        return SubArea;
    }

    public void setSubArea(String subArea) {
        SubArea = subArea;
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
