package com.envigil.extranet.models;

public class Streams {
    int StrTypeID;
    int StrID;

    public Streams(int strTypeID, int strID, String strName, String strAbbr) {
        StrTypeID = strTypeID;
        StrID = strID;
        StrName = strName;
        StrAbbr = strAbbr;
    }

    String StrName;
    String StrAbbr;

    public int getStrTypeID() {
        return StrTypeID;
    }

    public void setStrTypeID(int strTypeID) {
        StrTypeID = strTypeID;
    }

    public int getStrID() {
        return StrID;
    }

    public void setStrID(int strID) {
        StrID = strID;
    }

    public String getStrName() {
        return StrName;
    }

    public void setStrName(String strName) {
        StrName = strName;
    }

    public String getStrAbbr() {
        return StrAbbr;
    }

    public void setStrAbbr(String strAbbr) {
        StrAbbr = strAbbr;
    }
}
