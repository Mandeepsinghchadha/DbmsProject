package com.teamcool.touristum.data.model;

public class Hotel {
    private String HotelID,HotelName,HotelCity,HotelLocation,HotelRating,AvailableRooms,cityID, locationID;

    public Hotel(String hotelID, String hotelName, String hotelCity, String hotelLocation, String hotelRating, String availableRooms, String cityID, String locationID) {
        HotelID = hotelID;
        HotelName = hotelName;
        HotelCity = hotelCity;
        HotelLocation = hotelLocation;
        HotelRating = hotelRating;
        AvailableRooms = availableRooms;
        this.cityID = cityID;
        this.locationID = locationID;
    }

    public String getHotelName() {
        return HotelName;
    }

    public String getHotelCity() {
        return HotelCity;
    }

    public String getHotelLocation() {
        return HotelLocation;
    }

    public String getHotelRating() {
        return HotelRating;
    }

    public String getAvailableRooms() {
        return AvailableRooms;
    }

    public String getHotelID() {
        return HotelID;
    }

    public void setHotelID(String hotelID) {
        HotelID = hotelID;
    }

    public void setHotelName(String hotelName) {
        HotelName = hotelName;
    }

    public void setHotelCity(String hotelCity) {
        HotelCity = hotelCity;
    }

    public void setHotelLocation(String hotelLocation) {
        HotelLocation = hotelLocation;
    }

    public void setHotelRating(String hotelRating) {
        HotelRating = hotelRating;
    }

    public void setAvailableRooms(String availableRooms) {
        AvailableRooms = availableRooms;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }
}
