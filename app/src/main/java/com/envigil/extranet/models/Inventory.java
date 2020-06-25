package com.envigil.extranet.models;

public class Inventory {
   int RouteID;
   int SubID;
   int InvID;
   int CompID;
   int CompTypeID;
    int StrTypeID;
    int StrID;
   String InvTag;
   String InvLocation;
   int InvOrder;
   float InvSize;
   float Total;
   String ScanSeconds;
   boolean Critical;
   String Subarea;
   String CompnentType;
   String StreamType;
   boolean Inspected;
   boolean InvSkipped;
   int ReasonSkipp;
    float mIntBackground;
    float Reading;
//    int insSki;

    public Inventory(int routeID, int subID, int invID, int compID, int compTypeID,
                     int strTypeID, int strID, String invTag,String invLocation,
                     int invOrder, float invSize, float total, String scanSeconds,
                     boolean critical) {
        RouteID = routeID;
        SubID = subID;
        InvID = invID;
        CompID = compID;
        CompTypeID = compTypeID;
        StrTypeID = strTypeID;
        StrID = strID;
        InvTag = invTag;
        InvOrder = invOrder;
        InvSize = invSize;
        Total = total;
        ScanSeconds = scanSeconds;
        Critical = critical;
        InvLocation = invLocation;

    }

    public boolean isCritical() {
        return Critical;
    }

    public boolean isInspected() {
        return Inspected;
    }

    public boolean isInvSkipped() {
        return InvSkipped;
    }

    public boolean getInspected() {
        return Inspected;
    }

    public void setInspected(boolean inspected) {
        Inspected = inspected;
    }

    public int getRouteID() {
        return RouteID;
    }

    public void ID(int routeID) {
        RouteID = routeID;
    }

    public int getSubID() {
        return SubID;
    }

    public void setSubID(int subID) {
        SubID = subID;
    }

    public int getInvID() {
        return InvID;
    }

    public void setInvID(int invID) {
        InvID = invID;
    }

    public int getCompID() {
        return CompID;
    }

    public void setCompID(int compID) {
        CompID = compID;
    }

    public int getCompTypeID() {
        return CompTypeID;
    }

    public void setCompTypeID(int compTypeID) {
        CompTypeID = compTypeID;
    }

    public int getStrTypeID() {
        return StrTypeID;
    }

    public void setStrTypeID(int strTypeID) {
        StrTypeID = strTypeID;
    }

    public int getStrID() {
        return StrID;
    }

    public void setStrID(int strID) {
        StrID = strID;
    }

    public String getInvTag() {
        return InvTag;
    }

    public void setInvTag(String invTag) {
        InvTag = invTag;
    }

    public String getInvLocation() {
        return InvLocation;
    }

    public void setInvLocation(String invLocation) {
        InvLocation = invLocation;
    }

    public int getInvOrder() {
        return InvOrder;
    }

    public void setInvOrder(int invOrder) {
        InvOrder = invOrder;
    }

    public float getInvSize() {
        return InvSize;
    }

    public void setInvSize(float invSize) {
        InvSize = invSize;
    }

    public float getTotal() {
        return Total;
    }

    public void setTotal(float total) {
        Total = total;
    }

    public String getScanSeconds() {
        return ScanSeconds;
    }

    public void setScanSeconds(String scanSeconds) {
        ScanSeconds = scanSeconds;
    }

    public boolean getCritical() {
        return Critical;
    }

    public void setCritical(boolean critical) {
        Critical = critical;
    }

    public String getSubarea() {
        return Subarea;
    }

    public void setSubarea(String subarea) {
        Subarea = subarea;
    }

    public String getCompnentType() {
        return CompnentType;
    }

    public void setCompnentType(String compnentType) {
        CompnentType = compnentType;
    }

    public String getStreamType() {
        return StreamType;
    }

    public void setStreamType(String streamType) {
        StreamType = streamType;
    }

    public boolean getInvSkipped() {
        return InvSkipped;
    }

    public void setInvSkipped(boolean invSkipped) {
        InvSkipped = invSkipped;
    }

    public float getmIntBackground() {
        return mIntBackground;
    }

    public void setmIntBackground(float mIntBackgroung) {
        this.mIntBackground = mIntBackgroung;
    }

    public float getReading() {
        return Reading;
    }

    public void setReading(float reading) {
        Reading = reading;
    }

//    public int getInsSki() {
//        return insSki;
//    }
//
//    public void setInsSki(int insSki) {
//        this.insSki = insSki;
//    }

    public int getReasonSkipp() {
        return ReasonSkipp;
    }

    public void setReasonSkipp(int reasonSkipp) {
        ReasonSkipp = reasonSkipp;
    }
}
