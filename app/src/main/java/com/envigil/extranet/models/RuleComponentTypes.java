package com.envigil.extranet.models;

public class RuleComponentTypes {

    int RuleID;
    int CompTypeID;

    public RuleComponentTypes() {
    }

    public RuleComponentTypes(int ruleID, int compTypeID, int ruleCompTypeID) {
        RuleID = ruleID;
        CompTypeID = compTypeID;
        RuleCompTypeID = ruleCompTypeID;
    }

    int RuleCompTypeID;

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

    public int getRuleCompTypeID() {
        return RuleCompTypeID;
    }

    public void setRuleCompTypeID(int ruleCompTypeID) {
        RuleCompTypeID = ruleCompTypeID;
    }
}
