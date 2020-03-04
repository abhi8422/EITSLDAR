package com.envigil.extranet.models;

public class PermitInventory {
    int InvID, PermID, RouteID;

    public PermitInventory(int invID, int permID, int routeID) {
        InvID = invID;
        PermID = permID;
        RouteID = routeID;
    }

    public int getInvID() {
        return InvID;
    }

    public void setInvID(int invID) {
        InvID = invID;
    }

    public int getPermID() {
        return PermID;
    }

    public void setPermID(int permID) {
        PermID = permID;
    }

    public int getRouteID() {
        return RouteID;
    }

    public void ID(int routeID) {
        RouteID = routeID;
    }
}
