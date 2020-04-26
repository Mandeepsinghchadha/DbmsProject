package com.teamcool.touristum.data.model;

public class DriverTrips {
    private String clientName, clientNumber,  startDate,endDate, startLocation, endLocation, distance;

    public DriverTrips() {
    }

    public DriverTrips(String clientName, String clientNumber, String startDate,String endDate,String startLocation, String endLocation,String distance) {
        this.clientName = clientName;
        this.clientNumber = clientNumber;
        this.startDate = startDate;
        this.endDate=endDate;
        this.startLocation=startLocation;
        this.endLocation=endLocation;
        this.distance=distance;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public String getDistance() {
        return distance;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}