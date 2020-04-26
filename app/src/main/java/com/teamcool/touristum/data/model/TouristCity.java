package com.teamcool.touristum.data.model;

public class TouristCity {
    private String CityID,CityName,Rating;

    public String getCityID() {
        return CityID;
    }

    public void setCityID(String cityID) {
        CityID = cityID;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public TouristCity(String cityID, String cityName, String rating) {
        CityID = cityID;
        CityName = cityName;
        Rating = rating;
    }
}
