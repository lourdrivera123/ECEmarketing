package com.example.zem.patientcareapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Esel on 5/5/2015.
 */
public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "PatientCare";
    public static final int DB_VERSION = 1;

    //PATIENTS_TABLE
    public static final String TBL_PATIENTS = "patients";
    public static final String PTNT_ID = "id";
    public static final String PTNT_SERVER_ID = "server_id";
    public static final String PTNT_FNAME = "fname";
    public static final String PTNT_MNAME = "mname";
    public static final String PTNT_LNAME = "lname";
    public static final String PTNT_USERNAME = "username";
    public static final String PTNT_PASSWORD = "password";
    public static final String PTNT_OCCUPATION = "occupation";
    public static final String PTNT_BIRTHDATE = "birthdate";
    public static final String PTNT_SEX = "sex";
    public static final String PTNT_CIVIL_STATUS = "civil_status";
    public static final String PTNT_HEIGHT = "height";
    public static final String PTNT_WEIGHT = "weight";
    public static final String PTNT_UNIT_NO = "unit_floor_room_no";
    public static final String PTNT_BUILDING = "building";
    public static final String PTNT_LOT_NO = "lot_no";
    public static final String PTNT_BLOCK_NO = "block_no";
    public static final String PTNT_PHASE_NO = "phase_no";
    public static final String PTNT_HOUSE_NO = "address_house_no";
    public static final String PTNT_STREET = "address_street";
    public static final String PTNT_BARANGAY = "address_barangay";
    public static final String PTNT_CITY = "address_city_municipality";
    public static final String PTNT_PROVINCE = "_address_province";
    public static final String PTNT_REGION = "address_region";
    public static final String PTNT_ZIP = "address_zip";
    public static final String PTNT_TEL_NO = "tel_no";
    public static final String PTNT_CELL_NO = "cell_no";
    public static final String PTNT_EMAIL = "email";
    public static final String PTNT_PHOTO = "photo";
    public static final String PTNT_CREATED_AT = "created_at";
    public static final String PTNT_UPDATED_AT = "updated_at";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql2 = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, " +
                        "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, " +
                        "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TBL_PATIENTS, PTNT_ID, PTNT_SERVER_ID, PTNT_FNAME, PTNT_MNAME, PTNT_LNAME, PTNT_USERNAME, PTNT_PASSWORD, PTNT_OCCUPATION,
                PTNT_BIRTHDATE, PTNT_SEX, PTNT_CIVIL_STATUS, PTNT_HEIGHT, PTNT_WEIGHT, PTNT_UNIT_NO, PTNT_BUILDING, PTNT_LOT_NO, PTNT_BLOCK_NO,
                PTNT_PHASE_NO, PTNT_HOUSE_NO, PTNT_STREET, PTNT_BARANGAY, PTNT_CITY, PTNT_PROVINCE, PTNT_REGION, PTNT_ZIP, PTNT_TEL_NO, PTNT_CELL_NO,
                PTNT_EMAIL, PTNT_PHOTO, PTNT_CREATED_AT, PTNT_UPDATED_AT);

        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + DB_NAME;
        db.execSQL(sql);
    }

    public boolean LoginUser(String uname, String password) {
        int login = 0;
        SQLiteDatabase db = getWritableDatabase();
        String sql1 = "SELECT * FROM " + TBL_PATIENTS + " WHERE " + PTNT_USERNAME + " = '" + uname + "' and " + PTNT_PASSWORD + " = '" + password + "'";
        Cursor cur = db.rawQuery(sql1, null);
        cur.moveToFirst();

        if (cur.getCount() > 0) {
            login = 1;
        }
        return login > 0;
    }

    public int checkUserIfRegistered(String username) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_PATIENTS + " WHERE " + PTNT_USERNAME + " = '" + username + "'";
        Cursor cur = db.rawQuery(sql, null);
        cur.moveToFirst();
        int check = 0;

        if (cur.getCount() > 0) {
            check = 1;
        }

        return check;
    }

    public boolean insertPatient(int server_id, String created_at, Patient patient) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PTNT_SERVER_ID, server_id);
        values.put(PTNT_FNAME, patient.getFname());
        values.put(PTNT_MNAME, patient.getMname());
        values.put(PTNT_LNAME, patient.getLname());
        values.put(PTNT_USERNAME, patient.getUsername());
        values.put(PTNT_PASSWORD, patient.getPassword());
        values.put(PTNT_OCCUPATION, patient.getOccupation());
        values.put(PTNT_BIRTHDATE, patient.getBirthdate());
        values.put(PTNT_SEX, patient.getSex());
        values.put(PTNT_CIVIL_STATUS, patient.getCivil_status());
        values.put(PTNT_HEIGHT, patient.getHeight());
        values.put(PTNT_WEIGHT, patient.getWeight());
        values.put(PTNT_UNIT_NO, patient.getUnit_floor_room_no());
        values.put(PTNT_BUILDING, patient.getBuilding());
        values.put(PTNT_LOT_NO, patient.getLot_no());
        values.put(PTNT_BLOCK_NO, patient.getBlock_no());
        values.put(PTNT_PHASE_NO, patient.getPhase_no());
        values.put(PTNT_HOUSE_NO, patient.getAddress_house_no());
        values.put(PTNT_STREET, patient.getAddress_street());
        values.put(PTNT_BARANGAY, patient.getAddress_barangay());
        values.put(PTNT_CITY, patient.getAddress_city_municipality());
        values.put(PTNT_PROVINCE, patient.getAddress_province());
        values.put(PTNT_REGION, patient.getAddress_region());
        values.put(PTNT_ZIP, patient.getAddress_zip());
        values.put(PTNT_TEL_NO, patient.getTel_no());
        values.put(PTNT_CELL_NO, patient.getCell_no());
        values.put(PTNT_EMAIL, patient.getEmail());
        values.put(PTNT_PHOTO, patient.getPhoto());
        values.put(PTNT_CREATED_AT, created_at);
        values.put(PTNT_UPDATED_AT, created_at);

        long insert_patient = db.insert(TBL_PATIENTS, null, values);

        return insert_patient > 0;
    }

}