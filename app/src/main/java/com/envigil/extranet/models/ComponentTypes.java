package com.envigil.extranet.models;

public class ComponentTypes {
    int CompTypeID;
    String CompTypeName;

    public ComponentTypes(int compTypeID, String compTypeName, String compAbbr) {
        CompTypeID = compTypeID;
        CompTypeName = compTypeName;
        CompAbbr = compAbbr;
    }

    String CompAbbr;

    public int getCompTypeID() {
        return CompTypeID;
    }

    public void setCompTypeID(int compTypeID) {
        CompTypeID = compTypeID;
    }

    public String getCompTypeName() {
        return CompTypeName;
    }

    public void setCompTypeName(String compTypeName) {
        CompTypeName = compTypeName;
    }

    public String getCompAbbr() {
        return CompAbbr;
    }

    public void setCompAbbr(String compAbbr) {
        CompAbbr = compAbbr;
    }
}
