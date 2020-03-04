package com.envigil.extranet.models;

public class StreamTypes {
    int StrTypeID;
    String StrTypeName;

    public StreamTypes(int strTypeID, String strTypeName, String strTypeAbbr) {
        StrTypeID = strTypeID;
        StrTypeName = strTypeName;
        StrTypeAbbr = strTypeAbbr;
    }

    String StrTypeAbbr;

    public int getStrTypeID() {
        return StrTypeID;
    }

    public void setStrTypeID(int strTypeID) {
        StrTypeID = strTypeID;
    }

    public String getStrTypeName() {
        return StrTypeName;
    }

    public void setStrTypeName(String strTypeName) {
        StrTypeName = strTypeName;
    }

    public String getStrTypeAbbr() {
        return StrTypeAbbr;
    }

    public void setStrTypeAbbr(String strTypeAbbr) {
        StrTypeAbbr = strTypeAbbr;
    }
}
