package com.envigil.extranet.models;

public class Permits {
    int FacID;
    int PermID;

    public Permits() {
    }

    public Permits(int facID, int permID) {
        FacID = facID;
        PermID = permID;
    }


    public int getFacID() {
        return FacID;
    }

    public void setFacID(int facID) {
        FacID = facID;
    }

    public int getPermID() {
        return PermID;
    }

    public void setPermID(int permID) {
        PermID = permID;
    }
}
