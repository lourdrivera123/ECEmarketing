package com.example.zem.patientcareapp.Controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Zem on 11/23/2015.
 */
public class OrderPreferenceController extends DbHelper {

    DbHelper dbhelper;
    SQLiteDatabase sql_db;

    public OrderPreferenceController(Context context) {
        super(context);
        dbhelper = new DbHelper(context);
        sql_db = dbhelper.getWritableDatabase();
    }

//    public OrderModel getCustomerOrderPreference( int patient_id ){
//        SQLiteDatabase db = getWritableDatabase();
//        String sql = "SELECT * FROM " + TBL_PATIENT_PRESCRIPTIONS + " WHERE " + PATIENT_ID + " = " + patient_id;
//    }



}
