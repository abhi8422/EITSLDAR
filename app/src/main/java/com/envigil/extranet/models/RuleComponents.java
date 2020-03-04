package com.envigil.extranet.models;

public class RuleComponents {
    int RuleID;
    int CompTypeID;
    int CompID;
    int RuleCompID;

    public RuleComponents(int ruleID, int compTypeID, int compID, int ruleCompID) {
        RuleID = ruleID;
        CompTypeID = compTypeID;
        CompID = compID;
        RuleCompID = ruleCompID;
    }

    public RuleComponents() {

    }

    public int getRuleID() {
        return RuleID;
    }

    public void setRuleID(int ruleID) {
        RuleID = ruleID;
    }

    public int getCompTypeID() {
        return CompTypeID;
    }

    public void setCompTypeID(int compTypeID) {
        CompTypeID = compTypeID;
    }

    public int getCompID() {
        return CompID;
    }

    public void setCompID(int compID) {
        CompID = compID;
    }

    public int getRuleCompID() {
        return RuleCompID;
    }

    public void setRuleCompID(int ruleCompID) {
        RuleCompID = ruleCompID;
    }
}
