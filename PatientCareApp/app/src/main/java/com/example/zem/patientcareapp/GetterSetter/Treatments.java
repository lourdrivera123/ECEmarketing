package com.example.zem.patientcareapp.GetterSetter;

import java.io.Serializable;

/**
 * Created by Zem on 6/10/2015.
 */
public class Treatments implements Serializable {
    private int id = 0, patient_record_id = 0, treatments_id = 0;
    private String medicine_name = "", generic_name = "", quantity = "", prescription = "", created_at = "", updated_at = "", deleted_at = "";

    public Treatments(){

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPatient_record_id(int patient_record_id) {
        this.patient_record_id = patient_record_id;
    }

    public void setTreatments_id(int treatments_id) {
        this.treatments_id = treatments_id;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public void setGeneric_name(String generic_name) {
        this.generic_name = generic_name;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public int getId() {
        return id;
    }

    public int getPatient_record_id() {
        return patient_record_id;
    }

    public int getTreatments_id() {
        return treatments_id;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public String getGeneric_name() {
        return generic_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrescription() {
        return prescription;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }
}
