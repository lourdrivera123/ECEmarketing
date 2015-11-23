package com.example.zem.patientcareapp.Model;

import java.io.Serializable;

/**
 * Created by User PC on 5/21/2015.
 */
public class Medicine implements Serializable {
    int id = 0, serverID = 0, dosageID = 0;
    double price = 0.0;
    private String medicine_name = "", generic_name = "", description = "", unit = "", photo = "", dosage_format = "";

    public Medicine() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setServerID(int serverID) {
        this.serverID = serverID;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public void setGeneric_name(String generic_name) {
        this.generic_name = generic_name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setDosage_format(String dosage_format) {
        this.dosage_format = dosage_format;
    }

    public int getId() {
        return id;
    }

    public int getServerID() {
        return serverID;
    }

    public double getPrice() {
        return price;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public String getGeneric_name() {
        return generic_name;
    }

    public String getDescription() {
        return description;
    }

    public String getUnit() {
        return unit;
    }

    public String getPhoto() {
        return photo;
    }

    public String getDosage_format() {
        return dosage_format;
    }
}
