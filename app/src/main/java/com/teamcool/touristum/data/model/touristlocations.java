package com.teamcool.touristum.data.model;

public class touristlocations {
    private String LocationID,CityID,LocationName,Rating;

    public touristlocations(String locationID, String cityID, String locationName, String rating) {
        LocationID = locationID;
        CityID = cityID;
        LocationName = locationName;
        Rating = rating;
    }

    public void setLocationID(String locationID) {
        LocationID = locationID;
    }

    public void setCityID(String cityID) {
        CityID = cityID;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getLocationID() {
        return LocationID;
    }

    public String getCityID() {
        return CityID;
    }

    public String getLocationName() {
        return LocationName;
    }

    public String getRating() {
        return Rating;
    }
}
