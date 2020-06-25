package com.envigil.extranet.models;

public class LeakRepairTypes {
    int LeakRepairTypeID;
    String LeakRepairTypeName;

    public LeakRepairTypes(int leakRepairTypeID, String leakRepairTypeName, String leakRepairTypeAbbr) {
        LeakRepairTypeID = leakRepairTypeID;
        LeakRepairTypeName = leakRepairTypeName;
        LeakRepairTypeAbbr = leakRepairTypeAbbr;
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
