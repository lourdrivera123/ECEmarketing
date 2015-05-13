package com.example.zem.patientcareapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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

    //Updates Table
    public static final String TBL_UPDATES = "updates";
    public static final String UPDATE_ID = "id";
    public static final String UPDATE_TBL_NAME = "tbl_name";
    public static final String UPDATE_TIMESTAMP = "timestamp";
    public static final String UPDATE_SEEN = "seen";

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

    // PRODUCTS TABLE
    public static final String PRODUCT_NAME = "name";
    public static final String PRODUCT_GENERIC_NAME = "generic_name";
    public static final String PRODUCT_DESCRIPTION = "description";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_UNIT = "unit";
    public static final String PRODUCT_CREATED_AT = "created_at";
    public static final String PRODUCT_UPDATED_AT = "updated_at";
    public static final String PRODUCT_DELETED_AT = "deleted_at";
    public static final String TBL_PRODUCTS= "products";
    public static final String PRODUCTS_ID = "id";

    // BASKET TABLE
    public static final String TBL_BASKETS = "baskets";
    public static final String BASKET_ID = "id";
    public static final String BASKET_PATIENT_ID = "patient_id";
    public static final String BASKET_PRODUCT_ID = "product_id";
    public static final String BASKET_QUANTITY = "quantity";
    public static final String BASKET_CREATED_AT = "created_at";
    public static final String BASKET_UPDATED_AT = "updated_at";
    public static final String BASKET_DELETED_AT = "deleted_at";


    //Doctor string xml
    String doctor_string_xml = "";
    public static String doctors_string_xml = "";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql1 = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, " +
                        "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, " +
                        "%s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s INTEGER)",
                TBL_DOCTORS, DOC_ID, DOC_DOC_ID, DOC_LNAME, DOC_MNAME, DOC_FNAME, DOC_PRC_NO, DOC_ADDRESS_HOUSE_NO, DOC_ADDRESS_STREET, DOC_ADDRESS_BARANGAY,
                DOC_ADDRESS_CITY, DOC_ADDRESS_PROVINCE, DOC_ADDRESS_REGION, DOC_ADDRESS_COUNTRY, DOC_ZIP, DOC_SPECIALTY, DOC_SUB_SPECIALTY, DOC_CELL_NO, DOC_TEL_NO,
                DOC_PHOTO, DOC_CLINIC_SCHED, DOC_AFFILIATIONS, DOC_CLINIC_ID, DOC_EMAIL, DOC_SEC_ID);

        String sql2 = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s INTEGER)",
                TBL_UPDATES, UPDATE_ID, UPDATE_TBL_NAME, UPDATE_TIMESTAMP, UPDATE_SEEN);

        String sql3 = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, " +
                        "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, " +
                        "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TBL_PATIENTS, PTNT_ID, PTNT_SERVER_ID, PTNT_FNAME, PTNT_MNAME, PTNT_LNAME, PTNT_USERNAME, PTNT_PASSWORD, PTNT_OCCUPATION,
                PTNT_BIRTHDATE, PTNT_SEX, PTNT_CIVIL_STATUS, PTNT_HEIGHT, PTNT_WEIGHT, PTNT_UNIT_NO, PTNT_BUILDING, PTNT_LOT_NO, PTNT_BLOCK_NO,
                PTNT_PHASE_NO, PTNT_HOUSE_NO, PTNT_STREET, PTNT_BARANGAY, PTNT_CITY, PTNT_PROVINCE, PTNT_REGION, PTNT_ZIP, PTNT_TEL_NO, PTNT_CELL_NO,
                PTNT_EMAIL, PTNT_PHOTO, PTNT_CREATED_AT, PTNT_UPDATED_AT);

        // SQL to create table "products"
        String sql_create_tbl_products = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT , %s TEXT, " +
                        "%s DOUBLE, %s TEXT, %s  TEXT , %s  TEXT , %s  TEXT  )",
                TBL_PRODUCTS, PRODUCTS_ID, PRODUCT_NAME, PRODUCT_GENERIC_NAME, PRODUCT_DESCRIPTION, PRODUCT_PRICE, PRODUCT_UNIT,
                PRODUCT_CREATED_AT, PRODUCT_UPDATED_AT, PRODUCT_DELETED_AT);

        // SQL to create table "baskets"
        String sql_create_tbl_baskets = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, " +
                        "%s INTEGER, %s DOUBLE, %s  TEXT , %s  TEXT , %s  TEXT  )",
                TBL_BASKETS, BASKET_ID, BASKET_PATIENT_ID, BASKET_PRODUCT_ID, BASKET_QUANTITY, BASKET_CREATED_AT, BASKET_UPDATED_AT,
                BASKET_DELETED_AT);

        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql_create_tbl_baskets);
        db.execSQL(sql_create_tbl_products);


        insertTableNamesToUpdates(TBL_DOCTORS, db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + DB_NAME;
        db.execSQL(sql);
    }

    public boolean insertTableNamesToUpdates(String table_name, SQLiteDatabase db) {
//        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UPDATE_TBL_NAME, table_name);
        values.put(UPDATE_TIMESTAMP, "2015-11-05");
        values.put(UPDATE_SEEN, 0);

        long rowID = db.insert(TBL_UPDATES, null, values);

        return rowID > 0;

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

    public boolean updateDoctor(Doctor doctor_object) {
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

//        long rowID = db.update();

        int rowID = db.update(TBL_DOCTORS, values, DOC_ID + "=" + doctor_object.getDoc_id(), null);

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

            System.out.print("Lname: " + cur.getString(1));

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

    public String getDoctorsStringXml() {
        return doctors_string_xml;
    }


    public JSONArray getAllDoctorsJSONArray() {

        SQLiteDatabase db = getWritableDatabase();

        String sql = "SELECT * FROM " + TBL_DOCTORS;

        Cursor cursor = db.rawQuery(sql, null);

        JSONArray resultSet = new JSONArray();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        if (cursor.getString(i) != null) {
                            System.out.print("json array of all doctors: " + cursor.getString(i));
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {
//                        Log.d("TAG_NAME", e.getMessage()  );
                        System.out.print("error in doctors: " + e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
//        Log.d("TAG_NAME", resultSet.toString() );
        System.out.print("json array of all doctors: " + resultSet.toString());
        return resultSet;
    }

    public String getLastUpdate(String table_name) {
        SQLiteDatabase db = getWritableDatabase();

        String sql = "SELECT * FROM " + TBL_UPDATES + " WHERE " + UPDATE_TBL_NAME + "= '" + table_name + "'";


        String last_update_date = "";

        Cursor cur = db.rawQuery(sql, null);
//        cur.moveToFirst();

        while (cur.moveToNext()) {
            last_update_date = cur.getString(2);
        }

        cur.close();
        db.close();

        return last_update_date;
    }

}