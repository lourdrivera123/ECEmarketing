package com.example.zem.patientcareapp.GetterSetter;

import android.graphics.Bitmap;

/**
 * Created by User PC on 6/22/2015.
 */

public class ImageItem {
    private Bitmap image;

    public ImageItem(Bitmap image) {
        super();
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
