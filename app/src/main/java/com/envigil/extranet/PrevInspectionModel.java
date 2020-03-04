package com.envigil.extranet;

public class PrevInspectionModel {


    int InspectionID;
    String InspectionDate, EmployeeFirstName, StartTime, EndTime;

    public PrevInspectionModel(String inspectionDate, String employeeFirstName, String startTime, String endTime) {
        InspectionDate = inspectionDate;
        EmployeeFirstName = employeeFirstName;
        StartTime = startTime;
        EndTime = endTime;
    }

    public String getEmployeeFirstName() {
        return EmployeeFirstName;
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        EmployeeFirstName = employeeFirstName;
    }

    public String getInspectionDate() {
        return InspectionDate;
    }

    public void setInspectionDate(String inspectionDate) {
        InspectionDate = inspectionDate;
    }



    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }
    public int getInspectionID() {
        return InspectionID;
    }

    public void setInspectionID(int inspectionID) {
        InspectionID = inspectionID;
    }


}
