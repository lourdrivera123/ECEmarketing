package com.example.zem.patientcareapp.GetterSetter;

/**
 * Created by Dexter B. on 6/2/2015.
 */
public class Clinic {
    String name, contactNumber, addressUnitBuildingNo, addressStreet, addressBarangay, addressCityMunicipality, addressProvince, addressRegion,
            addressZip, createdAt, updatedAt, deletedAt;
    int id, clinicsId;

    public Clinic() {

    }

    /* SETTERS */

    public void setId(int id) {
        this.id = id;
    }

    public void setClinicsId(int clinics_id) {
        this.clinicsId = clinics_id;
    }

    public void setAddressUnitBuildingNo(String addressHouseNo) {
        this.addressUnitBuildingNo = addressHouseNo;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setAddressBarangay(String addressBarangay) {
        this.addressBarangay = addressBarangay;
    }

    public void setAddressCityMunicipality(String addressCityMunicipality) {
        this.addressCityMunicipality = addressCityMunicipality;
    }

    public void setAddressProvince(String addressProvince) {
        this.addressProvince = addressProvince;
    }

    public void setAddressRegion(String addressRegion) {
        this.addressRegion = addressRegion;
    }

    public void setAddressZip(String addressZip) {
        this.addressZip = addressZip;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }


    /* GETTERS */

    public int getId() {
        return id;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public int getClinicsId() {
        return clinicsId;
    }

    public String getName() {
        return name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getAddressUnitBuildingNo() {
        return addressUnitBuildingNo;
    }

    public String getAddressBarangay() {
        return addressBarangay;
    }

    public String getAddressCityMunicipality() {
        return addressCityMunicipality;
    }

    public String getAddressProvince() {
        return addressProvince;
    }

    public String getAddressRegion() {
        return addressRegion;
    }

    public String getAddressZip() {
        return addressZip;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }
}
