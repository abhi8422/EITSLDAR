package com.envigil.extranet.models;

import java.util.Date;

public class LeakRepairs {
    int LeakRepairID;
    int LeakID;
    int EmpID;
    int LeakRepairTypeID;
    float LeakRepairRate;
    Date LeakRepairDate;



    public LeakRepairs() {
    }

    public LeakRepairs(int leakRepairID, int leakID, int empID, int leakRepairTypeID, float leakRepairRate, Date leakRepairDate) {
        LeakRepairID = leakRepairID;
        LeakID = leakID;
        EmpID = empID;
        LeakRepairTypeID = leakRepairTypeID;
        LeakRepairRate = leakRepairRate;
        LeakRepairDate = leakRepairDate;
    }

    public int getLeakRepairID() {
        return LeakRepairID;
    }

    public void setLeakRepairID(int leakRepairID) {
        LeakRepairID = leakRepairID;
    }

    public int getLeakID() {
        return LeakID;
    }

    public void setLeakID(int leakID) {
        LeakID = leakID;
    }

    public int getEmpID() {
        return EmpID;
    }

    public void setEmpID(int empID) {
        EmpID = empID;
    }

    public int getLeakRepairTypeID() {
        return LeakRepairTypeID;
    }

    public void setLeakRepairTypeID(int leakRepairTypeID) {
        LeakRepairTypeID = leakRepairTypeID;
    }

    public float getLeakRepairRate() {
        return LeakRepairRate;
    }

    public void setLeakRepairRate(float leakRepairRate) {
        LeakRepairRate = leakRepairRate;
    }

    public Date getLeakRepairDate() {
        return LeakRepairDate;
    }

    public void setLeakRepairDate(Date leakRepairDate) {
        LeakRepairDate = leakRepairDate;
    }
}
