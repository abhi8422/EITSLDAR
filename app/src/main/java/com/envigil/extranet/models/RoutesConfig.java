package com.envigil.extranet.models;

import java.util.Date;

public class RoutesConfig {

    int    RouteID;
    int    WorkID;
    int    FacID;
    int    RuleTypeID;
    int    RuleID;
    String Facility;
    String RuleNumber;
    String RouteDateStamp;
    String RouteName;
    String RouteDesc;
    String RouteStatus;
    boolean DAEP;
    boolean OverwriteStaticTables;
    boolean Inspected;
    boolean BackgroundEntered;
    float Background;
    String InspectionDate;
    int    compCount;
    int    togo;
    boolean isSelected;

    public RoutesConfig() {
    }

    public RoutesConfig(int routeID, int workID, int facID, int ruleTypeID, int ruleID, String facility, String ruleNumber, String routeDateStamp, String routeName, String routeDesc, String routeStatus, boolean DAEP, boolean overwriteStaticTables, boolean inspected, boolean backgroundEntered, float background, String inspectionDate, int compCount, int togo, boolean isSelected) {
        RouteID = routeID;
        WorkID = workID;
        FacID = facID;
        RuleTypeID = ruleTypeID;
        RuleID = ruleID;
        Facility = facility;
        RuleNumber = ruleNumber;
        RouteDateStamp = routeDateStamp;
        RouteName = routeName;
        RouteDesc = routeDesc;
        RouteStatus = routeStatus;
        this.DAEP = DAEP;
        OverwriteStaticTables = overwriteStaticTables;
        Inspected = inspected;
        BackgroundEntered = backgroundEntered;
        Background = background;
        InspectionDate = inspectionDate;
        this.compCount = compCount;
        this.togo = togo;
        this.isSelected = isSelected;
    }

    public int getRouteID() {
        return RouteID;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void ID(int routeID) {
        RouteID = routeID;
    }

    public int getWorkID() {
        return WorkID;
    }

    public void setWorkID(int workID) {
        WorkID = workID;
    }

    public int getFacID() {
        return FacID;
    }

    public void setFacID(int facID) {
        FacID = facID;
    }

    public int getRuleTypeID() {
        return RuleTypeID;
    }

    public void setRuleTypeID(int ruleTypeID) {
        RuleTypeID = ruleTypeID;
    }

    public int getRuleID() {
        return RuleID;
    }

    public void setRuleID(int ruleID) {
        RuleID = ruleID;
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

    public String getRouteDateStamp() {
        return RouteDateStamp;
    }

    public void DateStamp(String routeDateStamp) {
        RouteDateStamp = routeDateStamp;
    }

    public String getRouteName() {
        return RouteName;
    }

    public void Name(String routeName) {
        RouteName = routeName;
    }

    public String getRouteDesc() {
        return RouteDesc;
    }

    public void Desc(String routeDesc) {
        RouteDesc = routeDesc;
    }

    public String getRouteStatus() {
        return RouteStatus;
    }

    public void setRouteStatus(String routeStatus) {
        RouteStatus = routeStatus;
    }

    public boolean getDAEP() {
        return DAEP;
    }

    public void setDAEP(boolean DAEP) {
        this.DAEP = DAEP;
    }

    public boolean getOverwriteStaticTables() {
        return OverwriteStaticTables;
    }

    public void setOverwriteStaticTables(boolean overwriteStaticTables) {
        OverwriteStaticTables = overwriteStaticTables;
    }

    public boolean getInspected() {
        return Inspected;
    }

    public void setInspected(boolean inspected) {
        Inspected = inspected;
    }

    public boolean getBackgroundEntered() {
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

    public String getInspectionDate() {
        return InspectionDate;
    }

    public void setInspectionDate(String inspectionDate) {
        InspectionDate = inspectionDate;
    }

    public int getCompCount() {
        return compCount;
    }

    public void setCompCount(int compCount) {
        this.compCount = compCount;
    }

    public int getTogo() {
        return togo;
    }

    public void setTogo(int togo) {
        this.togo = togo;
    }
}
