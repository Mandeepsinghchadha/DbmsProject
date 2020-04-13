package com.teamcool.touristum.data.model;

public class Agency {
    private String AgencyID,AgencyName,AgencyAddress,AgencyContact,NoOfPackages;

    public Agency(String agencyID, String agencyName, String agencyAddress, String agencyContact, String noOfPackages) {
        AgencyID = agencyID;
        AgencyName = agencyName;
        AgencyAddress = agencyAddress;
        AgencyContact = agencyContact;
        NoOfPackages = noOfPackages;
    }

    public String getAgencyID() {
        return AgencyID;
    }

    public String getAgencyName() {
        return AgencyName;
    }

    public String getAgencyAddress() {
        return AgencyAddress;
    }

    public String getAgencyContact() {
        return AgencyContact;
    }

    public String getNoOfPackages() {
        return NoOfPackages;
    }
}
