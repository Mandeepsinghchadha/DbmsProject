package com.teamcool.touristum.data.model;

public class Package {
    private String PackageID,AgencyName,PackageType,Days,Nights,City,PackagePrice,AgencyID,cityID;

    public Package(String packageID, String agencyName, String packageType, String days, String nights, String city, String packagePrice,String agencyID,String cityID) {
        PackageID = packageID;
        AgencyName = agencyName;
        PackageType = packageType;
        Days = days;
        Nights = nights;
        City = city;
        PackagePrice = packagePrice;
        this.cityID = cityID;
        AgencyID = agencyID;
    }

    public String getPackageID() {
        return PackageID;
    }

    public String getAgencyName() {
        return AgencyName;
    }

    public String getPackageType() {
        return PackageType;
    }

    public String getDays() {
        return Days;
    }

    public String getNights() {
        return Nights;
    }

    public String getCity() {
        return City;
    }

    public String getPackagePrice() {
        return PackagePrice;
    }

    public void setPackageID(String packageID) {
        PackageID = packageID;
    }

    public void setAgencyName(String agencyName) {
        AgencyName = agencyName;
    }

    public void setPackageType(String packageType) {
        PackageType = packageType;
    }

    public void setDays(String days) {
        Days = days;
    }

    public void setNights(String nights) {
        Nights = nights;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setPackagePrice(String packagePrice) {
        PackagePrice = packagePrice;
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
