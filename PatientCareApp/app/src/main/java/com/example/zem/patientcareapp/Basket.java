package com.example.zem.patientcareapp;

/**
 * Created by Dexter B. on 5/22/2015.
 */
public class Basket {
    int patienId, productId, basketId, id;
    double quantity;
    String createdAt, updatedAt;

    // SETTERS


    public void setId(int id) {
        this.id = id;
    }

    public void setPatienId(int patienId) {
        this.patienId = patienId;
    }

    public void setBasketId(int basketId) {
        this.basketId = basketId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    // GETTERS


    public int getId() {
        return id;
    }

    public int getBasketId() {
        return basketId;
    }

    public int getPatienId() {
        return patienId;
    }

    public int getProductId() {
        return productId;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
