package com.envigil.extranet.models;

public class GetAllRooutesPojo {
    public int getRouteID() {
        return RouteID;
    }

    public void ID(int routeID) {
        RouteID = routeID;
    }

    int RouteID;
    float Background,percent;
    String Title;
    int WorkID,Count;
    String Facility;
    String RuleNumber;
    String DownDate;
    String Inspected;
    String DAEP;
    String inspdate;



    public GetAllRooutesPojo(int routeID, String title, int workID, String facility, String ruleNumber, String downDate,String inspected) {
        RouteID = routeID;
        Title = title;
        WorkID = workID;
        Facility = facility;
        RuleNumber = ruleNumber;
        DownDate = downDate;
        Inspected=inspected;
    }

    public String getInspdate() {
        return inspdate;
    }

    public void setInspdate(String inspdate) {
        this.inspdate = inspdate;
    }

    public int getWorkID() {
        return WorkID;
    }

    public void setWorkID(int workID) {
        WorkID = workID;
    }

    public String getFacility() {
        return Facility;
    }

    public void setFacility(String facility) {
        Facility = facility;
    }

    public String getRuleNumber() {
        return RuleNumber;
    }

    public void setRuleNumber(String ruleNumber) {
        RuleNumber = ruleNumber;
    }

    public String getDownDate() {
        return DownDate;
    }

    public void setDownDate(String downDate) {
        DownDate = downDate;
    }
    public String getInspected() {
        return Inspected;
    }

    public void setInspected(String inspected) {
        Inspected = inspected;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String isDAEP() {
        return DAEP;
    }

    public void setDAEP(String DAEP) {
        this.DAEP = DAEP;
    }

    public float getBackground() {
        return Background;
    }

    public void setBackground(float background) {
        Background = background;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }
}
