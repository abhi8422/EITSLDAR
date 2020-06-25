package com.envigil.extranet.models;

public class LeakRepairTypesPojo {
    int LeakRepairTypeID;
    String LeakRepairTypeName;

    public LeakRepairTypesPojo(int leakRepairTypeID, String leakRepairTypeName) {
        LeakRepairTypeID = leakRepairTypeID;
        LeakRepairTypeName = leakRepairTypeName;
    }

    String LeakRepairTypeAbbr;

    public int getLeakRepairTypeID() {
        return LeakRepairTypeID;
    }

    public void setLeakRepairTypeID(int leakRepairTypeID) {
        LeakRepairTypeID = leakRepairTypeID;
    }

    public String getLeakRepairTypeName() {
        return LeakRepairTypeName;
    }

    public void setLeakRepairTypeName(String leakRepairTypeName) {
        LeakRepairTypeName = leakRepairTypeName;
    }

    public String getLeakRepairTypeAbbr() {
        return LeakRepairTypeAbbr;
    }

    public void setLeakRepairTypeAbbr(String leakRepairTypeAbbr) {
        LeakRepairTypeAbbr = leakRepairTypeAbbr;
    }
}
