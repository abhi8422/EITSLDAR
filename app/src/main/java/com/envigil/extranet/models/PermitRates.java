package com.envigil.extranet.models;

public class PermitRates {
     int PermID, LeakTypeID, PermRateID;
    float PermRateStart, PermRateTime;

    public PermitRates(int permID, int leakTypeID, int permRateID, float permRateStart, float permRateTime) {
        PermID = permID;
        LeakTypeID = leakTypeID;
        PermRateID = permRateID;
        PermRateStart = permRateStart;
        PermRateTime = permRateTime;
    }

    public int getPermID() {
        return PermID;
    }

    public void setPermID(int permID) {
        PermID = permID;
    }

    public int getLeakTypeID() {
        return LeakTypeID;
    }

    public void setLeakTypeID(int leakTypeID) {
        LeakTypeID = leakTypeID;
    }

    public int getPermRateID() {
        return PermRateID;
    }

    public void setPermRateID(int permRateID) {
        PermRateID = permRateID;
    }

    public float getPermRateStart() {
        return PermRateStart;
    }

    public void setPermRateStart(float permRateStart) {
        PermRateStart = permRateStart;
    }

    public float getPermRateTime() {
        return PermRateTime;
    }

    public void setPermRateTime(float permRateTime) {
        PermRateTime = permRateTime;
    }
}
