package com.example.zem.patientcareapp.Model;

import java.io.Serializable;

/**
 * Created by User PC on 5/22/2015.
 */
public class PatientRecord implements Serializable {

    public PatientRecord() {

    }

    int patientID = 0, doctorID = 0, record_id = 0, clinicID = 0;

    String complaints = "", findings = "", date = "", doctorName = "", clinicName = "", created_at = "", updated_at = "", deleted_at = "";

    public void setRecordID(int record_id) {
        this.record_id = record_id;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public void setComplaints(String complaints) {
        this.complaints = complaints;
    }

    public void setFindings(String findings) {
        this.findings = findings;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public void setClinicID(int clinicID) {
        this.clinicID = clinicID;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
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

    //GETTER
    public int getPatientID() {
        return patientID;
    }

    public int getRecordID() {
        return record_id;
    }

    public String getComplaints() {
        return complaints;
    }

    public String getFindings() {
        return findings;
    }

    public String getDate() {
        return date;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public int getClinicID() {
        return clinicID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getClinicName() {
        return clinicName;
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