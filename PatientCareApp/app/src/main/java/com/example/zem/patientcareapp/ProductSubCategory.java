package com.example.zem.patientcareapp;

import java.io.Serializable;

/**
 * Created by User PC on 5/18/2015.
 */
public class ProductSubCategory implements Serializable {
    String name = "";
    int categoryId;

    public ProductSubCategory(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
