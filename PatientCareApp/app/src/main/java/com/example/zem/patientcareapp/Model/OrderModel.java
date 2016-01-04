package com.example.zem.patientcareapp.Model;

import java.io.Serializable;

/**
 * Created by Zem on 11/18/2015.
 */
public class OrderModel implements Serializable {

    private int patient_id = 0, branch_id = 0;
    private double coupon_discount=0.0, points_discount = 0.0;
    private String recipient_name = "", recipient_address = "", recipient_contactNumber = "", delivery_sched="", mode_of_delivery = "", payment_method = "", action ="";

    public OrderModel(){}

    public boolean isValid(){
        if(this.getRecipient_name() != "" && this.getRecipient_address() != "" && this.getRecipient_contactNumber() != "" && this.getMode_of_delivery() != "" && this.getPayment_method() != "" )
            return true;

        return false;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public String getRecipient_name() {
        return this.recipient_name;
    }

    public void setRecipient_name(String recipient_name) {
        this.recipient_name = recipient_name;
    }

    public String getRecipient_address() {
        return this.recipient_address;
    }

    public void setRecipient_address(String recipient_address) {
        this.recipient_address = recipient_address;
    }

    public String getRecipient_contactNumber() {
        return this.recipient_contactNumber;
    }

    public void setRecipient_contactNumber(String recipient_contactNumber) {
        this.recipient_contactNumber = recipient_contactNumber;
    }

    public String getDelivery_sched() {
        return this.delivery_sched;
    }

    public void setDelivery_sched(String delivery_sched) {
        this.delivery_sched = delivery_sched;
    }

    public String getMode_of_delivery() {
        return this.mode_of_delivery;
    }

    public void setMode_of_delivery(String mode_of_delivery) {
        this.mode_of_delivery = mode_of_delivery;
    }

    public String getPayment_method() {
        return this.payment_method;
    }
    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public double getCoupon_discount() {
        return this.coupon_discount;
    }

    public void setCoupon_discount(double coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public double getPoints_discount() {
        return points_discount;
    }

    public void setPoints_discount(double points_discount) {
        this.points_discount = points_discount;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}