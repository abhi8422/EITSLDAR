package com.envigil.extranet.models;

import java.time.LocalDateTime;
import java.util.Date;

public class Leaks {

    String InvTag;
    float LeakTime;
    Date LeakDate;
    int LeakID,CompID,InvID,Status;
    int LeakPathTypeID, LeakTypeID;
    Boolean LeakBit1, LeakBit5,LeakBit2,LeakBit4 ;
    Float LeakFloat1, LeakRate, LeakLAT, LeakLNG;
    byte[] image;

    public String getInvTag() {
        return InvTag;
    }

    public void setInvTag(String invTag) {
        InvTag = invTag;
    }

    public int getInvID() {
        return InvID;
    }

    public void setInvID(int invID) {
        InvID = invID;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public Leaks(int compID, int leakID, int leakPathTypeID, int leakTypeID, Boolean leakBit1, Boolean leakBit5, Float leakFloat1, float leakTime, Float leakRate, Date leakDate, int status, Float leakLAT, Float leakLNG, byte[] image) {
        CompID = compID;
        LeakID = leakID;
        LeakPathTypeID = leakPathTypeID;
        LeakTypeID = leakTypeID;
        LeakBit1 = leakBit1;
        LeakBit5 = leakBit5;
        LeakFloat1 = leakFloat1;
        LeakTime = leakTime;
        LeakRate = leakRate;
        LeakDate = leakDate;
        Status = status;
        LeakLAT = leakLAT;
        LeakLNG = leakLNG;
        this.image = image;
    }

    public int getCompID() {
        return CompID;
    }

    public void setCompID(int compID) {
        CompID = compID;
    }

    public int getLeakID() {
        return LeakID;
    }

    public void setLeakID(int leakID) {
        LeakID = leakID;
    }

    public int getLeakPathTypeID() {
        return LeakPathTypeID;
    }

    public void setLeakPathTypeID(int leakPathTypeID) {
        LeakPathTypeID = leakPathTypeID;
    }

    public int getLeakTypeID() {
        return LeakTypeID;
    }

    public void setLeakTypeID(int leakTypeID) {
        LeakTypeID = leakTypeID;
    }

    public Boolean getLeakBit1() {
        return LeakBit1;
    }

    public void setLeakBit1(Boolean leakBit1) {
        LeakBit1 = leakBit1;
    }

    public Boolean getLeakBit5() {
        return LeakBit5;
    }

    public void setLeakBit5(Boolean leakBit5) {
        LeakBit5 = leakBit5;
    }

    public Float getLeakFloat1() {
        return LeakFloat1;
    }

    public void setLeakFloat1(Float leakFloat1) {
        LeakFloat1 = leakFloat1;
    }

    public float getLeakTime() {
        return LeakTime;
    }

    public void setLeakTime(float leakTime) {
        LeakTime = leakTime;
    }

    public Float getLeakRate() {
        return LeakRate;
    }

    public void setLeakRate(Float leakRate) {
        LeakRate = leakRate;
    }

    public Date getLeakDate() {
        return LeakDate;
    }

    public void setLeakDate(Date leakDate) {
        LeakDate = leakDate;
    }

    public Float getLeakLAT() {
        return LeakLAT;
    }

    public void setLeakLAT(Float leakLAT) {
        LeakLAT = leakLAT;
    }

    public Float getLeakLNG() {
        return LeakLNG;
    }

    public void setLeakLNG(Float leakLNG) {
        LeakLNG = leakLNG;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}