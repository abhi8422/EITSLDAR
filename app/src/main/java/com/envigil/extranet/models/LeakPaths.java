package com.envigil.extranet.models;

public class LeakPaths {
    int CompID;
    int LeakPathTypeID;

    public LeakPaths(int compID, int leakPathTypeID) {
        CompID = compID;
        LeakPathTypeID = leakPathTypeID;
    }

    public int getCompID() {
        return CompID;
    }

    public void setCompID(int compID) {
        CompID = compID;
    }

    public int getLeakPathTypeID() {
        return LeakPathTypeID;
    }

    public void setLeakPathTypeID(int leakPathTypeID) {
        LeakPathTypeID = leakPathTypeID;
    }
}
