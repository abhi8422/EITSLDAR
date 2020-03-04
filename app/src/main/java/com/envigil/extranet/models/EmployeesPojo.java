package com.envigil.extranet.models;

public class EmployeesPojo {
    int    EmpID;
    int    CompID;
    String EmpFirst;
    String EmpLast;

    public EmployeesPojo(int empID, String empFirst, String empLast) {
        EmpID = empID;
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
