package com.example.zem.patientcareapp.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zem on 11/23/2015.
 */
public class PatientTreatmentsController extends DbHelper {

    DbHelper dbhelper;
    SQLiteDatabase sql_db;

    //PATIENT_TREATMENTS TABLE
    public static final String TBL_PATIENT_TREATMENTS = "patient_treatments",
            SERVER_TREATMENTS_ID = "treatments_id",
            TREATMENTS_PATIENT_RECORDS_ID = "patient_records_id",
            TREATMENTS_MEDICINE_NAME = "medicine_name",
            TREATMENTS_GENERIC_NAME = "generic_name",
            TREATMENTS_DOSAGE = "dosage";

    //SQL to create "patient_treatments"
     public static final String CREATE_TABLE = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TBL_PATIENT_TREATMENTS, AI_ID, SERVER_TREATMENTS_ID, TREATMENTS_PATIENT_RECORDS_ID, TREATMENTS_MEDICINE_NAME, TREATMENTS_GENERIC_NAME, TREATMENTS_DOSAGE, CREATED_AT, UPDATED_AT, DELETED_AT);


    public PatientTreatmentsController(Context context) {
        super(context);
        dbhelper = new DbHelper(context);
        sql_db = dbhelper.getWritableDatabase();
    }

    public boolean savePatientTreatments(ArrayList<HashMap<String, String>> listOfTreatments, String request) {
//        SQLiteDatabase db = getWritableDatabase();
        ContentValues val = new ContentValues();
        long rowID = 0;

        for (int x = 0; x < listOfTreatments.size(); x++) {
            val.put(SERVER_TREATMENTS_ID, listOfTreatments.get(x).get("treatmentts_id"));
            val.put(TREATMENTS_PATIENT_RECORDS_ID, listOfTreatments.get(x).get("patient_records_id"));
            val.put(TREATMENTS_MEDICINE_NAME, listOfTreatments.get(x).get("medicine_name"));
            val.put(TREATMENTS_GENERIC_NAME, listOfTreatments.get(x).get("generic_name"));
            val.put(TREATMENTS_DOSAGE, listOfTreatments.get(x).get("dosage"));

            if (request.equals("insert"))
                rowID = sql_db.insert(TBL_PATIENT_TREATMENTS, null, val);
        }

        sql_db.close();
        return rowID > 0;
    }

    
}
