package com.example.zem.patientcareapp.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.zem.patientcareapp.DbHelper;
import com.example.zem.patientcareapp.Model.Patient;

/**
 * Created by Zem on 11/23/2015.
 */
public class PatientController extends DbHelper {

    DbHelper dbhelper;
    SQLiteDatabase sql_db;

    public PatientController(Context context) {
        super(context);
        dbhelper = new DbHelper(context);
        sql_db = dbhelper.getWritableDatabase();
    }

    
}
