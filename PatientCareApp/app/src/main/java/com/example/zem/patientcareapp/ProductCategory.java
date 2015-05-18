package com.example.zem.patientcareapp;

import java.io.Serializable;

/**
 * Created by Dexter B. on 5/18/2015.
 */
public class ProductCategory implements Serializable {
    public ProductCategory(){

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name = "";
}
