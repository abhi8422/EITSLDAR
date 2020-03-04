package com.envigil.extranet.models;

public class TVAPojo {
    int TVAID,EmpId;
    String TVAName;

    public int getTVAID() {
        return TVAID;
    }

    public void setTVAID(int TVAID) {
        this.TVAID = TVAID;
    }

    public int getEmpId() {
        return EmpId;
    }

    public void setEmpId(int empId) {
        EmpId = empId;
    }

    public String getTVAName() {
        return TVAName;
    }

    public void setTVAName(String TVAName) {
        this.TVAName = TVAName;
    }

    public TVAPojo(int TVAID, String TVAName) {
        this.TVAID = TVAID;
        this.TVAName = TVAName;
    }
}
