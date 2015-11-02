package com.example.zem.patientcareapp.GetterSetter;

/**
 * Created by User PC on 7/15/2015.
 */
public class Consultation {

    int id, serverID, patientID, doctorID, clinicID, isAlarmed, isFinished, is_approved;
    String date, time, alarmedTime, created_at, updated_at;

    public Consultation() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public void setIsAlarmed(int isAlarmed) {
        this.isAlarmed = isAlarmed;
    }

    public void setIsFinished(int isFinished) {
        this.isFinished = isFinished;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setServerID(int serverID) {
        this.serverID = serverID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public void setClinicID(int clinicID) {
        this.clinicID = clinicID;
    }

    public void setAlarmedTime(String alarmedTime) {
        this.alarmedTime = alarmedTime;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setIs_approved(int is_approved) {
        this.is_approved = is_approved;
    }

    //GET METHODS
    public int getId() {
        return id;
    }

    public int getPatientID() {
        return patientID;
    }

    public int getIsAlarmed() {
        return isAlarmed;
    }

    public int getIsFinished() {
        return isFinished;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getServerID() {
        return serverID;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public int getClinicID() {
        return clinicID;
    }

    public String getAlarmedTime() {
        return alarmedTime;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public int getIs_approved() {
        return is_approved;
    }
}
