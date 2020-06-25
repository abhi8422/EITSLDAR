package com.envigil.extranet.models;

public class WorkOrderRules {
    int RouteID;
    int RuleID;
    int RuleNumber;

    public int getRouteID() {
        return RouteID;
    }

    public void ID(int routeID) {
        RouteID = routeID;
    }

    public int getRuleID() {
        return RuleID;
    }

    public void setRuleID(int ruleID) {
        RuleID = ruleID;
    }

    public int getRuleNumber() {
        return RuleNumber;
    }

    public void setRuleNumber(int ruleNumber) {
        RuleNumber = ruleNumber;
    }
}
