package com.envigil.extranet.models;

public class ReasonSkipped {

    int ReasonSkippedID;
    String ReasonSkipped;

    public ReasonSkipped(int reasonSkippedID, String reasonSkipped) {
        ReasonSkipped = reasonSkipped;
        ReasonSkippedID = reasonSkippedID;
    }

    String ReasonSkippedAddress;

    public int getReasonSkippedID() {
        return ReasonSkippedID;
    }

    public void setReasonSkippedID(int reasonSkippedID) {
        ReasonSkippedID = reasonSkippedID;
    }

    public String getReasonSkipped() {
        return ReasonSkipped;
    }

    public void setReasonSkipped(String reasonSkipped) {
        ReasonSkipped = reasonSkipped;
    }

    public String getReasonSkippedAddress() {
        return ReasonSkippedAddress;
    }

    public void setReasonSkippedAddress(String reasonSkippedAddress) {
        ReasonSkippedAddress = reasonSkippedAddress;
    }
}
