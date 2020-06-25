package com.envigil.extranet.models;

public class LeakPathTypes {
    int LeakPathTypeID;
    String LeakPathTypeName;
    String LeakPathTypeAbbr;
    int LeakPathTypeOrder;

    public LeakPathTypes() {
    }

    public LeakPathTypes(int leakPathTypeID, String leakPathTypeName, String leakPathTypeAbbr, int leakPathTypeOrder) {
        LeakPathTypeID = leakPathTypeID;
        LeakPathTypeName = leakPathTypeName;
        LeakPathTypeAbbr = leakPathTypeAbbr;
        LeakPathTypeOrder = leakPathTypeOrder;
    }



    public int getLeakPathTypeID() {
        return LeakPathTypeID;
    }

    public void setLeakPathTypeID(int leakPathTypeID) {
        LeakPathTypeID = leakPathTypeID;
    }

    public String getLeakPathTypeName() {
        return LeakPathTypeName;
    }

    public void setLeakPathTypeName(String leakPathTypeName) {
        LeakPathTypeName = leakPathTypeName;
    }

    public String getLeakPathTypeAbbr() {
        return LeakPathTypeAbbr;
    }

    public void setLeakPathTypeAbbr(String leakPathTypeAbbr) {
        LeakPathTypeAbbr = leakPathTypeAbbr;
    }

    public int getLeakPathTypeOrder() {
        return LeakPathTypeOrder;
    }

    public void setLeakPathTypeOrder(int leakPathTypeOrder) {
        LeakPathTypeOrder = leakPathTypeOrder;
    }
}
