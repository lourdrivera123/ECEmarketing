package com.example.zem.patientcareapp;

import java.io.Serializable;

/**
 * Created by User PC on 5/22/2015.
 */
public class PatientRecord implements Serializable {

    public PatientRecord() {

    }

    int patientID = 0, treatmentID = 0, doctorID = 0;

    String complaints = "", findings = "", date = "", doctorName = "";

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public void setTreatmentID(int treatmentID) {
        this.treatmentID = treatmentID;
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

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getPatientID() {
        return patientID;
    }

    public int getTreatmentID() {
        return treatmentID;
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

    public String getDoctorName() {
        return doctorName;
    }
}
