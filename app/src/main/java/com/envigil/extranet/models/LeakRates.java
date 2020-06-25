package com.envigil.extranet.models;

import java.util.Date;

public class LeakRates {
    int LeakTypeID;
    int RuleCompTypeID;
    int StrTypeID;
    int LeakRateID;
    int StrID;
    float LeakRateStart;
    float LeakRateEnd;
    int LeakRateTime;

    public LeakRates(int leakTypeID, int ruleCompTypeID, int strTypeID, int leakRateID, int strID, float leakRateStart, float leakRateEnd, int leakRateTime) {
        LeakTypeID = leakTypeID;
        RuleCompTypeID = ruleCompTypeID;
        StrTypeID = strTypeID;
        LeakRateID = leakRateID;
        StrID = strID;
        LeakRateStart = leakRateStart;
        LeakRateEnd = leakRateEnd;
        LeakRateTime = leakRateTime;
    }



    public int getLeakTypeID() {
        return LeakTypeID;
    }

    public void setLeakTypeID(int leakTypeID) {
        LeakTypeID = leakTypeID;
    }

    public int getRuleCompTypeID() {
        return RuleCompTypeID;
    }

    public void setRuleCompTypeID(int ruleCompTypeID) {
        RuleCompTypeID = ruleCompTypeID;
    }

    public int getStrTypeID() {
        return StrTypeID;
    }

    public void setStrTypeID(int strTypeID) {
        StrTypeID = strTypeID;
    }

    public int getLeakRateID() {
        return LeakRateID;
    }

    public void setLeakRateID(int leakRateID) {
        LeakRateID = leakRateID;
    }

    public int getStrID() {
        return StrID;
    }

    public void setStrID(int strID) {
        StrID = strID;
    }

    public float getLeakRateStart() {
        return LeakRateStart;
    }

    public void setLeakRateStart(float leakRateStart) {
        LeakRateStart = leakRateStart;
    }

    public float getLeakRateEnd() {
        return LeakRateEnd;
    }

    public void setLeakRateEnd(float leakRateEnd) {
        LeakRateEnd = leakRateEnd;
    }

    public int getLeakRateTime() {
        return LeakRateTime;
    }

    public void setLeakRateTime(int leakRateTime) {
        LeakRateTime = leakRateTime;
    }
}
