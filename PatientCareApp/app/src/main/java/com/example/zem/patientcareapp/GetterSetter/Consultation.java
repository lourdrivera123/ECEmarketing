package com.example.zem.patientcareapp.GetterSetter;

/**
 * Created by User PC on 7/15/2015.
 */
public class Consultation {

    int patientID, isAlarmed, isFinished;
    String doctor, clinic, date, partOfDay, time;

    public Consultation() {

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

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPartOfDay(String partOfDay) {
        this.partOfDay = partOfDay;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getDoctor() {
        return doctor;
    }

    public String getClinic() {
        return clinic;
    }

    public String getDate() {
        return date;
    }

    public String getPartOfDay() {
        return partOfDay;
    }

    public String getTime() {
        return time;
    }
}
