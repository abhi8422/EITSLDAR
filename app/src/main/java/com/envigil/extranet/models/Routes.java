package com.envigil.extranet.models;
/* Creaed By : Mayuri Sagar Bhamare
   Date : 21-07-2019 12:41
   Project Name : MyApplication */

public class Routes {

    private int RouteID;
    String Facility;
    String Rule;
    String Route;
    String Status;
    boolean Inspected;
    private boolean isSelected;
    private String mStringFacility;
    private String mStringRule;
    private String mStringRoute;
    private boolean isInspected;
    private String mStringWorkorder;
    private String mStringDownBy;
    private String mStringDate;
    private String mStringComponents;
    private String mStringPercComplete;
    private String mStringBackground;
    private String mStringDAEP;

    public int getRouteID() {
        return RouteID;
    }

    public void setRouteID(int routeID) {
        RouteID = routeID;
    }

    public String getFacility() {
        return Facility;
    }

    public void setFacility(String facility) {
        Facility = facility;
    }

    public String getRule() {
        return Rule;
    }

    public void setRule(String rule) {
        Rule = rule;
    }

    public String getRoute() {
        return Route;
    }

    public void setRoute(String route) {
        Route = route;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getmStringFacility() {
        return mStringFacility;
    }

    public void setmStringFacility(String mStringFacility) {
        this.mStringFacility = mStringFacility;
    }

    public String getmStringRule() {
        return mStringRule;
    }

    public void setmStringRule(String mStringRule) {
        this.mStringRule = mStringRule;
    }

    public String getmStringRoute() {
        return mStringRoute;
    }

    public void setmStringRoute(String mStringRoute) {
        this.mStringRoute = mStringRoute;
    }

    public boolean isInspected() {
        return isInspected;
    }

    public void setInspected(boolean inspected) {
        isInspected = inspected;
    }

    public String getmStringWorkorder() {
        return mStringWorkorder;
    }

    public void setmStringWorkorder(String mStringWorkorder) {
        this.mStringWorkorder = mStringWorkorder;
    }

    public String getmStringBackground() {
        return mStringBackground;
    }

    public void setmStringBackground(String mStringBackground) {
        this.mStringBackground = mStringBackground;
    }

    public String getmStringDownBy() {
        return mStringDownBy;
    }

    public void setmStringDownBy(String mStringDownBy) {
        this.mStringDownBy = mStringDownBy;
    }

    public String getmStringDate() {
        return mStringDate;
    }

    public void setmStringDate(String mStringDate) {
        this.mStringDate = mStringDate;
    }

    public String getmStringComponents() {
        return mStringComponents;
    }

    public void setmStringComponents(String mStringComponents) {
        this.mStringComponents = mStringComponents;
    }

    public String getmStringPercComplete() {
        return mStringPercComplete;
    }

    public void setmStringPercComplete(String mStringPercComplete) {
        this.mStringPercComplete = mStringPercComplete;
    }

    public String getmStringDAEP() {
        return mStringDAEP;
    }

    public void setmStringDAEP(String mStringDAEP) {
        this.mStringDAEP = mStringDAEP;
    }
}
