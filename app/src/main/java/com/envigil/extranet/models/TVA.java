package com.envigil.extranet.models;

public class TVA {
    int TVAID;
    int TVAEmpID;

    public TVA(int TVAID, int TVAEmpID, String TVAName) {
        this.TVAID = TVAID;
        this.TVAEmpID = TVAEmpID;
        this.TVAName = TVAName;
    }

    String TVAName;

    public int getTVAID() {
        return TVAID;
    }

    public void setTVAID(int TVAID) {
        this.TVAID = TVAID;
    }

    public int getTVAEmpID() {
        return TVAEmpID;
    }

    public void setTVAEmpID(int TVAEmpID) {
        this.TVAEmpID = TVAEmpID;
    }

    public String getTVAName() {
        return TVAName;
    }

    public void setTVAName(String TVAName) {
        this.TVAName = TVAName;
    }
}
