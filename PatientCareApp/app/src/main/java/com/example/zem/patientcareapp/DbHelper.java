package com.example.zem.patientcareapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Esel on 5/5/2015.
 */
public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "PatientCare";
    public static final int DB_VERSION = 1;

    //DOCTORS_TABLE
    public static final String TBL_DOCTORS = "doctors";
    public static final String DOC_ID = "id";
    public static final String DOC_LNAME = "lname";
    public static final String DOC_MNAME = "mname";
    public static final String DOC_FNAME = "fname";
    public static final String DOC_PRC_NO = "prc_no";
    public static final String DOC_ADDRESS_HOUSE_NO = "add_houseNo";
    public static final String DOC_ADDRESS_STREET = "add_street";
    public static final String DOC_ADDRESS_BARANGAY = "add_brgy";
    public static final String DOC_ADDRESS_CITY = "add_city";
    public static final String DOC_ADDRESS_PROVINCE = "add_province";
    public static final String DOC_ADDRESS_REGION = "add_region";
    public static final String DOC_ZIP = "zip";
    public static final String DOC_SPECIALTY = "specialty";
    public static final String DOC_SUB_SPECIALTY = "sub_specialty";
    public static final String DOC_CELL_NO = "cellNo";
    public static final String DOC_TEL_NO = "telNo";
    public static final String DOC_PHOTO = "photo";
    public static final String DOC_CLINIC_SCHED = "clinic_sched";
    public static final String DOC_AFFILIATIONS = "affiliations";
    public static final String DOC_CLINIC_ID = "clinic_id";
    public static final String DOC_EMAIL = "email";
    public static final String DOC_SEC_ID = "secretary_id";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, " +
                        "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, " +
                        "%s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s INTEGER)",
                TBL_DOCTORS, DOC_ID, DOC_LNAME, DOC_MNAME, DOC_FNAME, DOC_PRC_NO, DOC_ADDRESS_HOUSE_NO, DOC_ADDRESS_STREET, DOC_ADDRESS_BARANGAY,
                DOC_ADDRESS_CITY, DOC_ADDRESS_PROVINCE, DOC_ADDRESS_REGION, DOC_ZIP, DOC_SPECIALTY, DOC_SUB_SPECIALTY, DOC_CELL_NO, DOC_TEL_NO,
                DOC_PHOTO, DOC_CLINIC_SCHED, DOC_AFFILIATIONS, DOC_CLINIC_ID, DOC_EMAIL, DOC_SEC_ID);

        db.execSQL(sql1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS "+DB_NAME;
        db.execSQL(sql);
    }

    public boolean insertDoctor() {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DOC_LNAME, "Esel");
        values.put(DOC_MNAME, "Bordado");
        values.put(DOC_FNAME, "Barnes");
        values.put(DOC_PRC_NO, 123);
        values.put(DOC_ADDRESS_HOUSE_NO, "FDFASDA");
        values.put(DOC_ADDRESS_STREET, "fdsfa");
        values.put(DOC_ADDRESS_BARANGAY, "dfdaf");
        values.put(DOC_ADDRESS_CITY, "Davao");
        values.put(DOC_ADDRESS_PROVINCE, "davao del sur");
        values.put(DOC_ADDRESS_REGION, "region XI");
        values.put(DOC_ZIP, "8000");
        values.put(DOC_SPECIALTY, "fhdkfsa");
        values.put(DOC_SUB_SPECIALTY, "fdfa");
        values.put(DOC_CELL_NO, "fdfsa");
        values.put(DOC_TEL_NO, "fdfsa");
        values.put(DOC_PHOTO, "fdsafa");
        values.put(DOC_CLINIC_SCHED, "fdsaf");
        values.put(DOC_AFFILIATIONS, "fdasf");
        values.put(DOC_CLINIC_ID, "fdsfafa");
        values.put(DOC_EMAIL, "fdfsafda");
        values.put(DOC_SEC_ID, 1);

        long rowID = db.insert(TBL_DOCTORS, null, values);

        return rowID > 0;
    }

}