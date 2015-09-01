package com.example.zem.patientcareapp.GetterSetter;

/**
 * Created by Dexter B. on 5/22/2015.
 */
public class Basket {
    int patienId, productId, basketId, id;
    int quantity, isApproved, prescriptionId;
    String createdAt, updatedAt;


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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }


    public int getId() {
        return id;
    }

    public int getBasketId() {
        return basketId;
    }

    public int getPatienId() {
        return patienId;
    }

    public int getIsApproved() {
        return isApproved;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
