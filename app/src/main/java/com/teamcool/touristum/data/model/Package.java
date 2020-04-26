package com.teamcool.touristum.data.model;

public class Package {
    private String PackageID,AgencyName,PackageType,Days,Nights,City,Price,AgencyID,cityID;

    public Package(String packageID, String agencyName, String packageType, String days, String nights, String city, String packagePrice, String agencyID, String cityID) {
        PackageID = packageID;
        AgencyName = agencyName;
        PackageType = packageType;
        Days = days;
        Nights = nights;
        City = city;
        Price = packagePrice;
        this.cityID = cityID;
        AgencyID = agencyID;
    }

    public String getPackageID() {
        return PackageID;
    }

    public void setPackageID(String packageID) {
        PackageID = packageID;
    }

    public String getAgencyName() {
        return AgencyName;
    }

    public void setAgencyName(String agencyName) {
        AgencyName = agencyName;
    }

    public String getPackageType() {
        return PackageType;
    }

    public void setPackageType(String packageType) {
        PackageType = packageType;
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

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getAgencyID() {
        return AgencyID;
    }

    public void setAgencyID(String agencyID) {
        AgencyID = agencyID;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }
}
