package com.envigil.extranet.models;

public class Components {
int    CompID;
String CompName;
String CompAbbr;
int CompOrder;

    public Components(int compID, String compName, String compAbbr, int compOrder) {
        CompID = compID;
        CompName = compName;
        CompAbbr = compAbbr;
        CompOrder = compOrder;
    }

    public int getCompID() {
        return CompID;
    }

    public void setCompID(int compID) {
        CompID = compID;
    }

    public String getCompName() {
        return CompName;
    }

    public void setCompName(String compName) {
        CompName = compName;
    }

    public String getCompAbbr() {
        return CompAbbr;
    }

    public void setCompAbbr(String compAbbr) {
        CompAbbr = compAbbr;
    }

    public int getCompOrder() {
        return CompOrder;
    }

    public void setCompOrder(int compOrder) {
        CompOrder = compOrder;
    }
}
