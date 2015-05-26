package com.example.zem.patientcareapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /* Returns db row column value */
    public static String curGetStr(Cursor cursor, String columnName){
        return cursor.getString(cursor.getColumnIndex(columnName));
    }
}
