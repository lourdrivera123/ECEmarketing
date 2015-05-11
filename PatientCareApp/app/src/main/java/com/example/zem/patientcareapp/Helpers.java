package com.example.zem.patientcareapp;

import android.content.Context;

/**
 * Created by User PC on 5/7/2015.
 */
public class Helpers {
    public void Helpers(){

    }

    public static float dpFromPx(final int px, final Context context) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final int dp, final Context context) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
