package com.teamcool.touristum.data.model;

import java.io.Serializable;

public class Booking implements Serializable {

    private String BookingID,ClientName,PackageType,City,vehicleID;
    private String VehicleName,VehicleType,DateOfBooking,FromDate;
    private String Days,Nights,Price, agencyName, agencyID, cityID, clientID, packageID;

    public Booking(String bookingID, String clientName, String packageType, String city, String vehicleName, String vehicleType, String dateOfBooking, String fromDate, String days, String nights, String price, String agencyName, String vehicleID, String agencyID, String cityID, String packageID, String clientID) {
        BookingID = bookingID;
        ClientName = clientName;
        PackageType = packageType;
        City = city;
        VehicleName = vehicleName;
        VehicleType = vehicleType;
        DateOfBooking = dateOfBooking;
        FromDate = fromDate;
        Days = days;
        Nights = nights;
        Price = price;
        this.vehicleID = vehicleID;
        this.agencyName = agencyName;
        this.agencyID = agencyID;
        this.cityID = cityID;
        this.packageID = packageID;
        this.clientID = clientID;
    }

    public String getBookingID() {
        return BookingID;
    }

    public void setBookingID(String bookingID) {
        BookingID = bookingID;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getPackageType() {
        return PackageType;
    }

    public void setPackageType(String packageType) {
        PackageType = packageType;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getVehicleName() {
        return VehicleName;
    }

    public void setVehicleName(String vehicleName) {
        VehicleName = vehicleName;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    public String getDateOfBooking() {
        return DateOfBooking;
    }

    public void setDateOfBooking(String dateOfBooking) {
        DateOfBooking = dateOfBooking;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getDays() {
        return Days;
    }

    public void setDays(String days) {
        Days = days;
    }

    public String getNights() {
        return Nights;
    }

    public void setNights(String nights) {
        Nights = nights;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getAgencyID() {
        return agencyID;
    }

    public void setAgencyID(String agencyID) {
        this.agencyID = agencyID;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getPackageID() {
        return packageID;
    }

    public void setPackageID(String packageID) {
        this.packageID = packageID;
    }
}
