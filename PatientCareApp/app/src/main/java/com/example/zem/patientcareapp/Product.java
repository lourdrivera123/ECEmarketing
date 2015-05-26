package com.example.zem.patientcareapp;

/**
 * Created by Dexter B. on 5/14/2015.
 */
public class Product {
    String name, dosageFormatAndStrength, genericName, description, unit, photo, createdAt, updatedAt, deletedAt;
    int productId;
    int id;
    double price;
    public Product(){

    }

    public String getName(){
        return name;
    }
    public int getId(){
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public String getGenericName(){
        return genericName;
    }

    public String getDescription(){
        return description;
    }

    public String getUnit(){
        return unit;
    }

    public double getPrice(){
        return price;
    }

    public String getPhoto(){
        return photo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPhoto(String photo){
        this.photo = photo;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setId(int id){
        this.id = id;
    }
}
