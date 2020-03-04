package com.envigil.extranet.models;

public class InventoryRules {
    int RouteID;
    int InvID;

    public InventoryRules(int routeID, int invID, int ruleID, int comp_mon_cat_id) {
        RouteID = routeID;
        InvID = invID;
        RuleID = ruleID;
        this.comp_mon_cat_id = comp_mon_cat_id;
    }

    int RuleID;
    int comp_mon_cat_id;

    public int getRouteID() {
        return RouteID;
    }

    public void ID(int routeID) {
        RouteID = routeID;
    }

    public int getInvID() {
        return InvID;
    }

    public void setInvID(int invID) {
        InvID = invID;
    }

    public int getRuleID() {
        return RuleID;
    }

    public void setRuleID(int ruleID) {
        RuleID = ruleID;
    }

    public int getComp_mon_cat_id() {
        return comp_mon_cat_id;
    }

    public void setComp_mon_cat_id(int comp_mon_cat_id) {
        this.comp_mon_cat_id = comp_mon_cat_id;
    }
}
