package com.envigil.extranet.models;

public class Employees {
int    EmpID;
int    CompID;
String EmpFirst;
String EmpLast;

    public Employees(int empID, int compID, String empFirst, String empLast) {
        EmpID = empID;
        CompID = compID;
        EmpFirst = empFirst;
        EmpLast = empLast;
    }

    public int getEmpID() {
        return EmpID;
    }

    public void setEmpID(int empID) {
        EmpID = empID;
    }

    public int getCompID() {
        return CompID;
    }

    public void setCompID(int compID) {
        CompID = compID;
    }

    public String getEmpFirst() {
        return EmpFirst;
    }

    public void setEmpFirst(String empFirst) {
        EmpFirst = empFirst;
    }

    public String getEmpLast() {
        return EmpLast;
    }

    public void setEmpLast(String empLast) {
        EmpLast = empLast;
    }
}
