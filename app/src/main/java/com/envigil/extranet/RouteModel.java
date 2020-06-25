package com.envigil.extranet;

public class RouteModel {

    String SubAreaRead, Inspection, BackgrndRead;

    public RouteModel() {
    }

    public RouteModel(String subArea, String inspection, String backgrndRead) {
        SubAreaRead = subArea;
        Inspection = inspection;
        BackgrndRead = backgrndRead;
    }

    public String getSubAreaRead() {
        return SubAreaRead;
    }

    public void setSubAreaRead(String subArea) {
        SubAreaRead = subArea;
    }

    public String getInspection() {
        return Inspection;
    }

    public void setInspection(String inspection) {
        Inspection = inspection;
    }

    public String getBackgrndRead() {
        return BackgrndRead;
    }

    public void setBackgrndRead(String backgrndRead) {
        BackgrndRead = backgrndRead;
    }
}
