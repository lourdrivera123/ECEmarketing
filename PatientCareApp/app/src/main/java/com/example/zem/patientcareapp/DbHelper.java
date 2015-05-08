package com.example.zem.patientcareapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Esel on 5/5/2015.
 */
public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "PatientCare";
    public static final int DB_VERSION = 1;

    //DOCTORS_TABLE
    public static final String TBL_DOCTORS = "doctors";
    public static final String DOC_ID = "id";
    public static final String DOC_DOC_ID = "doc_id";
    public static final String DOC_LNAME = "lname";
    public static final String DOC_MNAME = "mname";
    public static final String DOC_FNAME = "fname";
    public static final String DOC_PRC_NO = "prc_no";
    public static final String DOC_ADDRESS_HOUSE_NO = "address_house_no";
    public static final String DOC_ADDRESS_STREET = "address_street";
    public static final String DOC_ADDRESS_BARANGAY = "address_barangay";
    public static final String DOC_ADDRESS_CITY = "address_city_municipality";
    public static final String DOC_ADDRESS_PROVINCE = "address_province";
    public static final String DOC_ADDRESS_REGION = "address_region";
    public static final String DOC_ADDRESS_COUNTRY = "address_country";
    public static final String DOC_ZIP = "address_zip";
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

    //Doctor string xml
    String doctor_string_xml = "";
    public static String doctors_string_xml = "";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, " +
                        "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, " +
                        "%s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s INTEGER)",
                TBL_DOCTORS, DOC_ID, DOC_DOC_ID, DOC_LNAME, DOC_MNAME, DOC_FNAME, DOC_PRC_NO, DOC_ADDRESS_HOUSE_NO, DOC_ADDRESS_STREET, DOC_ADDRESS_BARANGAY,
                DOC_ADDRESS_CITY, DOC_ADDRESS_PROVINCE, DOC_ADDRESS_REGION, DOC_ADDRESS_COUNTRY, DOC_ZIP, DOC_SPECIALTY, DOC_SUB_SPECIALTY, DOC_CELL_NO, DOC_TEL_NO,
                DOC_PHOTO, DOC_CLINIC_SCHED, DOC_AFFILIATIONS, DOC_CLINIC_ID, DOC_EMAIL, DOC_SEC_ID);

        db.execSQL(sql1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + DB_NAME;
        db.execSQL(sql);
    }

    public boolean insertDoctor(Doctor doctor_object) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DOC_DOC_ID, doctor_object.getDoc_id());
        values.put(DOC_LNAME, doctor_object.getLname());
        values.put(DOC_MNAME, doctor_object.getMname());
        values.put(DOC_FNAME, doctor_object.getFname());
        values.put(DOC_PRC_NO, doctor_object.getPrc_no());
        values.put(DOC_ADDRESS_HOUSE_NO, doctor_object.getAddress_house_no());
        values.put(DOC_ADDRESS_STREET, doctor_object.getAddress_street());
        values.put(DOC_ADDRESS_BARANGAY, doctor_object.getAddress_barangay());
        values.put(DOC_ADDRESS_CITY, doctor_object.getAddress_city_municipality());
        values.put(DOC_ADDRESS_PROVINCE, doctor_object.getAddress_province());
        values.put(DOC_ADDRESS_REGION, doctor_object.getAddress_region());
        values.put(DOC_ADDRESS_COUNTRY, doctor_object.getCountry());
        values.put(DOC_ZIP, doctor_object.getAddress_zip());
        values.put(DOC_SPECIALTY, doctor_object.getSpecialty());
        values.put(DOC_SUB_SPECIALTY, doctor_object.getSub_specialty());
        values.put(DOC_CELL_NO, doctor_object.getCell_no());
        values.put(DOC_TEL_NO, doctor_object.getTel_no());
        values.put(DOC_PHOTO, doctor_object.getPhoto());
        values.put(DOC_CLINIC_SCHED, doctor_object.getClinic_sched());
        values.put(DOC_AFFILIATIONS, doctor_object.getAffiliation());
        values.put(DOC_CLINIC_ID, doctor_object.getClinic_id());
        values.put(DOC_EMAIL, doctor_object.getEmail());
        values.put(DOC_SEC_ID, doctor_object.getSecretary_id());

        long rowID = db.insert(TBL_DOCTORS, null, values);

        return rowID > 0;
    }

    public ArrayList<Doctor> getAllDoctors() {

        ArrayList<Doctor> doctors = new ArrayList<Doctor>();

        SQLiteDatabase db = getWritableDatabase();

        String sql = "SELECT * FROM " + TBL_DOCTORS;

        Cursor cur = db.rawQuery(sql, null);
//        cur.moveToFirst();

        while (cur.moveToNext()) {

            //for the id
            int id = cur.getInt(0);
//            //for task name
//            String taskName = cur.getString(1);
//            //for notes
//            String notes = cur.getString(2);
//            //for deadline
//            String deadLine = cur.getString(3);
//            //for the priority
//            int priority = cur.getInt(4);
//            //for status
//            int status = cur.getInt(5);

            Doctor doctor = new Doctor();
            doctor.setID(id);
            doctor.setLname(cur.getString(1));
            doctor.setMname(cur.getString(2));
            doctor.setFname(cur.getString(3));

            System.out.print("Lname: "+cur.getString(1));

            doctors.add(doctor);

            String doctor_temporary_string_xml = "<doctor>\n" +
                    "<id>" + cur.getString(0) + "</id>\n" +
                    "<fullname> Dr. " + cur.getString(3) + " " + cur.getString(1) + "</fullname>\n" +
                    "<specialty>" + cur.getString(13) + "</specialty>\n" +
                    "<photo>" + cur.getString(17) + "</photo>\n" +
                    "</doctor>";

            doctor_string_xml += doctor_temporary_string_xml;
        }



        cur.close();
        db.close();

        Log.d("The Doctor XML String: ", doctors_string_xml);

            doctors_string_xml = "<list>" + doctor_string_xml + "</list>";

        return doctors;
    }

    public String getDoctorsStringXml(){
        return doctors_string_xml;
    }

}