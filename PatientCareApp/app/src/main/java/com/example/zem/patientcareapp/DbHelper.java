package com.example.zem.patientcareapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Esel on 5/5/2015.
 */
public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "PatientCare";
    public static final int DB_VERSION = 1;

    //PATIENTS_TABLE
    public static final String TBL_PATIENTS = "patients",
            PTNT_ID = "id",
            PTNT_PATIENT_ID = "patient_id",
            PTNT_FNAME = "fname",
            PTNT_MNAME = "mname",
            PTNT_LNAME = "lname",
            PTNT_USERNAME = "username",
            PTNT_PASSWORD = "password",
            PTNT_OCCUPATION = "occupation",
            PTNT_BIRTHDATE = "birthdate",
            PTNT_SEX = "sex",
            PTNT_CIVIL_STATUS = "civil_status",
            PTNT_HEIGHT = "height",
            PTNT_WEIGHT = "weight",
            PTNT_UNIT_NO = "unit_floor_room_no",
            PTNT_BUILDING = "building",
            PTNT_LOT_NO = "lot_no",
            PTNT_BLOCK_NO = "block_no",
            PTNT_PHASE_NO = "phase_no",
            PTNT_HOUSE_NO = "address_house_no",
            PTNT_STREET = "address_street",
            PTNT_BARANGAY = "address_barangay",
            PTNT_CITY = "address_city_municipality",
            PTNT_PROVINCE = "address_province",
            PTNT_REGION = "address_region",
            PTNT_ZIP = "address_zip",
            PTNT_TEL_NO = "tel_no",
            PTNT_CELL_NO = "cell_no",
            PTNT_EMAIL = "email",
            PTNT_PHOTO = "photo",
            PTNT_CREATED_AT = "created_at",
            PTNT_UPDATED_AT = "updated_at";

    //Updates Table
    public static final String TBL_UPDATES = "updates",
            UPDATE_ID = "id",
            UPDATE_TBL_NAME = "tbl_name",
            UPDATE_TIMESTAMP = "timestamp",
            UPDATE_SEEN = "seen";

    //DOCTORS_TABLE
    public static final String TBL_DOCTORS = "doctors",
            DOC_ID = "id",
            DOC_DOC_ID = "doc_id",
            DOC_LNAME = "lname",
            DOC_MNAME = "mname",
            DOC_FNAME = "fname",
            DOC_USERNAME = "username",
            DOC_PASSWORD = "password",
            DOC_PRC_NO = "prc_no",
            DOC_ADDRESS_HOUSE_NO = "address_house_no",
            DOC_ADDRESS_STREET = "address_street",
            DOC_ADDRESS_BARANGAY = "address_barangay",
            DOC_ADDRESS_CITY = "address_city_municipality",
            DOC_ADDRESS_PROVINCE = "address_province",
            DOC_ADDRESS_REGION = "address_region",
            DOC_ADDRESS_COUNTRY = "address_country",
            DOC_ZIP = "address_zip",
            DOC_SPECIALTY = "specialty",
            DOC_SUB_SPECIALTY = "sub_specialty",
            DOC_CELL_NO = "cellNo",
            DOC_TEL_NO = "telNo",
            DOC_PHOTO = "photo",
            DOC_CLINIC_SCHED = "clinic_sched",
            DOC_AFFILIATIONS = "affiliations",
            DOC_CLINIC_ID = "clinic_id",
            DOC_EMAIL = "email",
            DOC_SEC_ID = "secretary_id";

    // PRODUCT_CATEGORIES TABLE
    public static final String PROD_CAT_NAME = "name",
            PROD_CAT_CREATED_AT = "created_at",
            PROD_CAT_UPDATED_AT = "updated_at",
            PROD_CAT_DELETED_AT = "deleted_at",
            TBL_PRODUCT_CATEGORIES = "product_categories",
            PRODUCT_CATEGORIES_ID = "id",
            SERVER_PRODUCT_CATEGORY_ID = "product_category_id";

    // PRODUCT_SUBCATEGORIES TABLE
    public static final String PROD_SUBCAT_NAME = "name",
            PROD_SUBCAT_CATEGORY_ID = "category_id",
            PROD_SUBCAT_CREATED_AT = "created_at",
            PROD_SUBCAT_UPDATED_AT = "updated_at",
            PROD_SUBCAT_DELETED_AT = "deleted_at",
            TBL_PRODUCT_SUBCATEGORIES = "product_subcategories",
            PRODUCT_SUBCATEGORIES_ID = "id",
            SERVER_PRODUCT_SUBCATEGORY_ID = "product_subcategory_id";

    // PRODUCTS TABLE
    public static final String TBL_PRODUCTS = "products",
            PRODUCTS_ID = "id",
            PRODUCT_SUBCATEGORY_ID = "subcategory_id",
            SERVER_PRODUCT_ID = "product_id",
            PRODUCT_NAME = "name",
            PRODUCT_GENERIC_NAME = "generic_name",
            PRODUCT_DESCRIPTION = "description",
            PRODUCT_PRESCRIPTION_REQUIRED = "presciption_required",
            PRODUCT_PRICE = "price",
            PRODUCT_UNIT = "unit",
            PRODUCT_PHOTO = "photo",
            PRODUCT_CREATED_AT = "created_at",
            PRODUCT_UPDATED_AT = "updated_at",
            PRODUCT_DELETED_AT = "deleted_at";

    //DOSAGE_FORMAT_AND_STRENGTH TABLE
    public static final String TBL_DOSAGE = "dosage_format_and_strength",
            DOSAGE_ID = "id",
            SERVER_DOSAGE_ID = "dosage_id",
            DOSAGE_PROD_ID = "product_id",
            DOSAGE_NAME = "name",
            DOSAGE_CREATED_AT = "created_at",
            DOSAGE_UPDATED_AT = "updated_at";

    // BASKET TABLE
    public static final String TBL_BASKETS = "baskets",
            BASKET_ID = "id",
            SERVER_BASKET_ID = "basket_id",
            BASKET_PATIENT_ID = "patient_id",
            BASKET_PRODUCT_ID = "product_id",
            BASKET_QUANTITY = "quantity",
            BASKET_CREATED_AT = "created_at",
            BASKET_UPDATED_AT = "updated_at",
            BASKET_DELETED_AT = "deleted_at";

    //string xml
    String doctor_string_xml = "", product_string_xml = "";
    public static String doctors_string_xml = "", products_string_xml = "";

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
                TBL_PATIENTS, PTNT_ID, PTNT_PATIENT_ID, PTNT_FNAME, PTNT_MNAME, PTNT_LNAME, PTNT_USERNAME, PTNT_PASSWORD, PTNT_OCCUPATION,
                PTNT_BIRTHDATE, PTNT_SEX, PTNT_CIVIL_STATUS, PTNT_HEIGHT, PTNT_WEIGHT, PTNT_UNIT_NO, PTNT_BUILDING, PTNT_LOT_NO, PTNT_BLOCK_NO,
                PTNT_PHASE_NO, PTNT_HOUSE_NO, PTNT_STREET, PTNT_BARANGAY, PTNT_CITY, PTNT_PROVINCE, PTNT_REGION, PTNT_ZIP, PTNT_TEL_NO, PTNT_CELL_NO,
                PTNT_EMAIL, PTNT_PHOTO, PTNT_CREATED_AT, PTNT_UPDATED_AT);

        // SQL to create table "product_categories"
        String sql_create_tbl_product_categories = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " %s INTEGER UNIQUE, %s TEXT, %s  TEXT , %s  TEXT , %s TEXT  )",
                TBL_PRODUCT_CATEGORIES, PRODUCT_CATEGORIES_ID, SERVER_PRODUCT_CATEGORY_ID, PROD_CAT_NAME, PROD_CAT_CREATED_AT,
                PROD_CAT_UPDATED_AT, PROD_CAT_DELETED_AT);

        // SQL to create table "product_subcategories"
        String sql_create_tbl_product_subcategories = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " %s INTEGER UNIQUE, %s TEXT, %s INTEGER, %s  TEXT , %s  TEXT , %s  TEXT  )",
                TBL_PRODUCT_SUBCATEGORIES, PRODUCT_SUBCATEGORIES_ID, SERVER_PRODUCT_SUBCATEGORY_ID, PROD_SUBCAT_NAME, PROD_SUBCAT_CATEGORY_ID,
                PROD_SUBCAT_CREATED_AT, PROD_SUBCAT_UPDATED_AT, PROD_SUBCAT_DELETED_AT);

        // SQL to create table "products"
        String sql_create_tbl_products = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, " +
                        "%s TEXT, %s TEXT,  %s TEXT, %s TEXT , %s INTEGER, %s DOUBLE, %s TEXT, %s  TEXT , %s  TEXT,  %s  TEXT , %s  TEXT  )",
                TBL_PRODUCTS, PRODUCTS_ID, SERVER_PRODUCT_ID, PRODUCT_SUBCATEGORY_ID, PRODUCT_NAME, PRODUCT_GENERIC_NAME, PRODUCT_DESCRIPTION,
                PRODUCT_PRESCRIPTION_REQUIRED, PRODUCT_PRICE, PRODUCT_UNIT, PRODUCT_PHOTO, PRODUCT_CREATED_AT, PRODUCT_UPDATED_AT, PRODUCT_DELETED_AT);

        //SQL TO CREATE TABLE "TBL_DOSAGE"
        String sql_create_dosage_table = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
                TBL_DOSAGE, DOSAGE_ID, SERVER_DOSAGE_ID, DOSAGE_PROD_ID, DOSAGE_NAME, DOSAGE_CREATED_AT, DOSAGE_UPDATED_AT);

        // SQL to create table "baskets"
        String sql_create_tbl_baskets = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, " +
                        "%s INTEGER REFERENCES " + TBL_PATIENTS + "(" + PTNT_PATIENT_ID + "), %s INTEGER REFERENCES " + TBL_BASKETS + "(" + SERVER_BASKET_ID + ")," +
                        " %s DOUBLE, %s  TEXT , %s  TEXT , %s  TEXT  )",
                TBL_BASKETS, BASKET_ID, SERVER_BASKET_ID, BASKET_PATIENT_ID, BASKET_PRODUCT_ID, BASKET_QUANTITY,
                BASKET_CREATED_AT, BASKET_UPDATED_AT, BASKET_DELETED_AT);

        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql_create_tbl_baskets);
        db.execSQL(sql_create_tbl_product_categories);
        db.execSQL(sql_create_tbl_product_subcategories);
        db.execSQL(sql_create_tbl_products);
        db.execSQL(sql_create_dosage_table);

        insertTableNamesToUpdates(TBL_DOCTORS, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + DB_NAME;
        db.execSQL(sql);
    }

    /* SYNC CHECKER and etc. */
        public boolean insertTableNamesToUpdates(String table_name, SQLiteDatabase db) {
            //        SQLiteDatabase db = getWritableDatabase();


            Date now = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            ContentValues values = new ContentValues();
            values.put(UPDATE_TBL_NAME, table_name);
            values.put(UPDATE_TIMESTAMP, formatter.format(now));
            values.put(UPDATE_SEEN, 0);

        long rowID = db.insert(TBL_UPDATES, null, values);

        return rowID > 0;
    }

    public String getLastUpdate(String table_name) {
        SQLiteDatabase db = getWritableDatabase();

        String sql = "SELECT * FROM " + TBL_UPDATES + " WHERE " + UPDATE_TBL_NAME + "= '" + table_name + "'";
        String last_update_date = "";
        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            last_update_date = cur.getString(2);
        }

        cur.close();
        db.close();

        return last_update_date;
    }

    /*  USERS TABLE */
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

    public void populateDiagnosis() {

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

    public boolean insertPatient(JSONObject patient_json_object_mysql, Patient patient) {
        int patient_id = 0;
        String created_at = "";

        try {
            patient_id = patient_json_object_mysql.getInt("id");
            created_at = patient_json_object_mysql.getString("created_at");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PTNT_PATIENT_ID, patient_id);
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

        long insert_patient = db.insert(TBL_PATIENTS, null, values);

        return insert_patient > 0;
    }

    public boolean updatePatient(Patient patient, int ID, String photo) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
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
        values.put(PTNT_PHOTO, photo);

        int rowID = db.update(TBL_PATIENTS, values, PTNT_ID + "=" + ID, null);

        return rowID > 0;
    }

    public Patient getloginPatient(String username) {

        SQLiteDatabase db = getWritableDatabase();
        Patient patient = new Patient();

        String sql = "SELECT * FROM " + TBL_PATIENTS + " WHERE " + PTNT_USERNAME + " = '" + username + "'";
        Cursor cur = db.rawQuery(sql, null);
        cur.moveToFirst();

        if (cur.getCount() > 0) {
            patient.setId(cur.getInt(0));
            patient.setServerID(cur.getInt(1));
            patient.setFname(cur.getString(2));
            patient.setLname(cur.getString(4));
            patient.setUsername(cur.getString(5));
            patient.setPassword(cur.getString(6));
            patient.setOccupation(cur.getString(7));
            patient.setBirthdate(cur.getString(8));
            patient.setSex(cur.getString(9));
            patient.setCivil_status(cur.getString(10));
            patient.setHeight(cur.getString(11));
            patient.setWeight(cur.getString(12));
            patient.setUnit_floor_room_no(cur.getInt(13));
            patient.setBuilding(cur.getString(14));
            patient.setLot_no(cur.getInt(15));
            patient.setBlock_no(cur.getInt(16));
            patient.setPhase_no(cur.getInt(17));
            patient.setAddress_house_no(cur.getInt(18));
            patient.setAddress_street(cur.getString(19));
            patient.setAddress_barangay(cur.getString(20));
            patient.setAddress_city_municipality(cur.getString(21));
            patient.setAddress_province(cur.getString(22));
            patient.setAddress_region(cur.getString(23));
            patient.setAddress_zip(cur.getString(24));
            patient.setTel_no(cur.getString(25));
            patient.setCell_no(cur.getString(26));
            patient.setEmail(cur.getString(27));
            patient.setPhoto(cur.getString(28));
        }

        cur.close();
        db.close();

        return patient;
    }

    //DOCTORS..DOCTORS..DOCTORS..DOCTORS..DOCTORS

    /* INSERT and UPDATE and other SQL's & functions for DOCTORS TABLE */
    public boolean insertDoctor(Doctor doctor_object) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DOC_DOC_ID, doctor_object.getDoc_id());
        values.put(DOC_LNAME, doctor_object.getLname());
        values.put(DOC_MNAME, doctor_object.getMname());
        values.put(DOC_FNAME, doctor_object.getFname());
        values.put(DOC_USERNAME, doctor_object.getUsername());
        values.put(DOC_PASSWORD, doctor_object.getPassword());
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

        int rowID = db.update(TBL_DOCTORS, values, DOC_ID + "=" + doctor_object.getDoc_id(), null);

        return rowID > 0;
    }

    public boolean insertDosage(Dosage dosage){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SERVER_DOSAGE_ID, dosage.getDosage_id());
        values.put(DOSAGE_PROD_ID, dosage.getProduct_id());
        values.put(DOSAGE_NAME, dosage.getName());

        long rowID = db.insert(TBL_DOSAGE, null, values);

        return rowID > 0;
    }

    public ArrayList<Doctor> getAllDoctors() {

        ArrayList<Doctor> doctors = new ArrayList<Doctor>();

        SQLiteDatabase db = getWritableDatabase();

        String sql = "SELECT * FROM " + TBL_DOCTORS;

        Cursor cur = db.rawQuery(sql, null);

        String i_lname, i_fname, i_mname, i_specialty, i_photo, i_uname;
        while (cur.moveToNext()) {
            i_lname = Helpers.curGetStr(cur, DOC_LNAME);
            i_fname = Helpers.curGetStr(cur, DOC_FNAME);
            i_mname = Helpers.curGetStr(cur, DOC_MNAME);
            i_specialty = Helpers.curGetStr(cur, DOC_SPECIALTY);
            i_photo = Helpers.curGetStr(cur, DOC_PHOTO);
            i_uname = Helpers.curGetStr(cur, DOC_USERNAME);

            //for the id
            int id = cur.getInt(0);

            Doctor doctor = new Doctor();
            doctor.setID(id);
            doctor.setLname(i_lname);
            doctor.setMname(i_mname);
            doctor.setFname(i_fname);

            doctors.add(doctor);

            String doctor_temporary_string_xml = "<doctor>\n" +
                    "<id>" + cur.getString(0) + "</id>\n" +
                    "<fullname> Dr. " + i_fname + " " + i_lname + "</fullname>\n" +
                    "<username>"+ i_uname + "</username>"+
                    "<specialty>" + i_specialty + "</specialty>\n" +
                    "<photo>" + i_photo + "</photo>\n" +
                    "</doctor>";

            doctor_string_xml += doctor_temporary_string_xml;
        }


        cur.close();
        db.close();

        doctors_string_xml = "<list>" + doctor_string_xml + "</list>";

        return doctors;
    }

    public ArrayList<Product> getAllProducts() {

        ArrayList<Product> products = new ArrayList<Product>();

        SQLiteDatabase db = getWritableDatabase();

        String sql = "SELECT * FROM " + TBL_PRODUCTS;

        Cursor cur = db.rawQuery(sql, null);

        int i_name, i_description, i_price;

        while (cur.moveToNext()) {
            i_name = cur.getColumnIndex("name");
            i_description = cur.getColumnIndex("description");
            i_price = cur.getColumnIndex("price");

            //for the id
            int id = cur.getInt(0);

            Product product = new Product();
            product.setId(id);
            product.setName(cur.getString(i_name));
            product.setDescription(cur.getString(i_description));
            product.setPrice(cur.getDouble(i_price));

//            System.out.print("Lname: " + cur.getString(i_lname));

            products.add(product);

            String product_temporary_string_xml = "<entry>\n" +
                    "<id>" + cur.getString(0) + "</id>\n" +
                    "<name>" + cur.getString(i_name) + "</name>\n" +
                    "<description>" + cur.getString(i_description) + "</description>\n" +
                    "<price> Php "+ cur.getDouble(i_price)+ "</price>\n" +
                    "<photo>" + "http://api.androidhive.info/music/images/rihanna.png" + "</photo>\n" +
                    "</entry>";

            product_string_xml += product_temporary_string_xml;
        }

        cur.close();
        db.close();

        products_string_xml = "<list>" + product_string_xml + "</list>";

        return products;
    }

    public ArrayList<String> getDoctorName() {
        ArrayList<String> doctors = new ArrayList<String>();

        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_DOCTORS;
        Cursor cur = db.rawQuery(sql, null);

        String fullname, fname, lname;
        while (cur.moveToNext()) {
            int id = cur.getInt(0);

            lname = cur.getString(2);
            fname = cur.getString(4);
            fullname = fname + " " + lname;
            doctors.add(fullname);
        }

        cur.close();
        db.close();

        return doctors;
    }

    public String getDoctorsStringXml() {
        return doctors_string_xml;
    }

    public String getProductsStringXml() {
        return products_string_xml;
    }


    public JSONArray getAllJSONArrayFrom(String tbl_name) {

        SQLiteDatabase db = getWritableDatabase();

        String sql = "SELECT * FROM " + tbl_name;

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
                        System.out.print("error in doctors: " + e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        System.out.print("json array of all doctors: " + resultSet.toString());
        return resultSet;
    }

    public JSONArray getAllProductsJSONArray() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_PRODUCTS;
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
                            System.out.print("json array of all products: " + cursor.getString(i));
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {
                        System.out.print("error in products: " + e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        System.out.print("json array of all products: " + resultSet.toString());
        return resultSet;
    }

    /* INSERT and UPDATE and other SQL for PRODUCT_CATEGORIES TABLE  */
        /* Returns all categories */
        public ArrayList<ProductCategory> getAllProductCategories() {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "SELECT * FROM " + TBL_PRODUCT_CATEGORIES;

            ArrayList<ProductCategory> categories = new ArrayList<ProductCategory>();
            Cursor cur = db.rawQuery(sql, null);
            while (cur.moveToNext()) {
                ProductCategory c = new ProductCategory();
                c.setCategoryId(cur.getInt(1));
                c.setName(cur.getString(2));
                c.setCreatedAt(cur.getString(3));
                c.setUpdatedAt(cur.getString(4));
                c.setDeletedAt(cur.getString(5));
                categories.add(c);
            }
            return categories;
        }

        public List<String> getAllProductCategoriesArray(){

            List<String> list = new ArrayList<String>();
            SQLiteDatabase db = getWritableDatabase();
            String sql = "SELECT * FROM "+TBL_PRODUCT_CATEGORIES;
            Cursor cur = db.rawQuery(sql, null);
            int x = 0;

            cur.moveToFirst();
            while(cur.isAfterLast() == false){
                list.add(x, cur.getString(2));
                x++;
                cur.moveToNext();
            }
            db.close();
            return list;
        }

        /* Insert new product category */
        public boolean insertProductCategory(ProductCategory category) throws SQLiteConstraintException {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            long rowID = 0;
            try{


                values.put(PROD_CAT_NAME, category.getName());
                values.put(SERVER_PRODUCT_CATEGORY_ID, category.getCategoryId());
                values.put(PROD_CAT_CREATED_AT, category.getCreatedAt());
                values.put(PROD_CAT_UPDATED_AT, category.getUpdatedAt());
                values.put(PROD_CAT_DELETED_AT, category.getDeletedAt());

               rowID = db.insert(TBL_PRODUCT_CATEGORIES, null, values);


            }catch(SQLiteConstraintException e){
                e.printStackTrace();
            }
            db.close();
            return rowID > 0;
        }

        public int categoryGetIdByName(String name){
            int id = 0;
            SQLiteDatabase db = getWritableDatabase();
            String sql = "SELECT id FROM "+TBL_PRODUCT_CATEGORIES+" WHERE name='"+name+"'";
            Cursor cur = db.rawQuery(sql, null);
            int x = 0;

            cur.moveToFirst();
            while(cur.isAfterLast() == false){
                id = cur.getInt(0);
                cur.moveToNext();
            }
            db.close();
            return id;
        }

        public String[] getAllProductSubCategoriesArray(int categoryId){
            List<String> list = new ArrayList<String>();
            SQLiteDatabase db = getWritableDatabase();
            String sql = "SELECT * FROM "+TBL_PRODUCT_SUBCATEGORIES+" WHERE category_id='"+categoryId+"' ORDER BY name";
            Cursor cur = db.rawQuery(sql, null);
            int x = 0;

            cur.moveToFirst();
            while(cur.isAfterLast() == false){
                list.add(x, cur.getString(2));
                x++;
                cur.moveToNext();
            }
            db.close();
            String[] arr = new String[ list.size() ];
            return list.toArray(arr);
        }

    /* Updates product category */
    public boolean updateProductCategory(ProductCategory category) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PROD_CAT_NAME, category.getName());
        values.put(PROD_CAT_CREATED_AT, category.getCreatedAt());
        values.put(PROD_CAT_UPDATED_AT, category.getUpdatedAt());
        values.put(PROD_CAT_DELETED_AT, category.getDeletedAt());

        long rowID = db.update(TBL_PRODUCTS, values, PRODUCT_CATEGORIES_ID + "=" + category.getCategoryId(), null);
        db.close();
        return rowID > 0;
    }

    /* INSERT and UPDATE and other SQL for PRODUCT_SUBCATEGORIES TABLE */

        // Returns all product subcategories for a specific category
        public ArrayList<ProductSubCategory> getAllProductSubCategories(int productCategoryId) {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "SELECT * FROM " + TBL_PRODUCT_CATEGORIES + " WHERE category_id=" + productCategoryId + " AND deleted_at='null'";

            ArrayList<ProductSubCategory> subCategories = new ArrayList<ProductSubCategory>();
            Cursor cur = db.rawQuery(sql, null);
            while (cur.moveToNext()) {
                ProductSubCategory s = new ProductSubCategory();
                s.setName(cur.getString(1));
                subCategories.add(s);
            }

            return subCategories;
        }

        // Inserts a new record for subcategories
        public boolean insertProductSubCategory(ProductSubCategory subCategory) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(PROD_SUBCAT_NAME, subCategory.getName());
            values.put(PROD_SUBCAT_CATEGORY_ID, subCategory.getCategoryId());
            values.put(SERVER_PRODUCT_SUBCATEGORY_ID, subCategory.getId());
            values.put(PROD_SUBCAT_CREATED_AT, subCategory.getCreatedAt());
            values.put(PROD_SUBCAT_UPDATED_AT, subCategory.getUpdatedAt());
            values.put(PROD_SUBCAT_DELETED_AT, subCategory.getDeletedAt());

            long rowID = db.insert(TBL_PRODUCT_SUBCATEGORIES, null, values);
            db.close();
            return rowID > 0;
        }

        /* Updates product subcategory */
        public boolean updateProductSubCategory(ProductSubCategory subCategory) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(PROD_CAT_NAME, subCategory.getName());
            values.put(PROD_SUBCAT_CATEGORY_ID, subCategory.getCategoryId());
            values.put(PROD_CAT_CREATED_AT, subCategory.getCreatedAt());
            values.put(PROD_CAT_UPDATED_AT, subCategory.getUpdatedAt());
            values.put(PROD_CAT_DELETED_AT, subCategory.getDeletedAt());

            long rowID = db.update(TBL_PRODUCT_SUBCATEGORIES, values, PRODUCT_SUBCATEGORIES_ID + "=" + subCategory.getId(), null);
            db.close();
            return rowID > 0;
        }

        /* returns subcategory id by subcategory name */
        public ProductSubCategory getSubCategoryByName(String name, int categoryId){
        ProductSubCategory subCategory = new ProductSubCategory();
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM "+TBL_PRODUCT_SUBCATEGORIES+" where name='"+name+"' and category_id='"+categoryId+"'";
        Cursor cur = db.rawQuery(sql, null);
        while(cur.moveToNext()){
            subCategory.setId(cur.getInt(0));
            subCategory.setName(cur.getString(2));
            subCategory.setCategoryId(Integer.parseInt(cur.getString(3)));
            subCategory.setCreatedAt(cur.getString(4));
            subCategory.setUpdatedAt(cur.getString(5));
            subCategory.setDeletedAt(cur.getString(6));
        }

        return subCategory;
    }

    /* INSERT and UPDATE and other SQL's & functions for PRODUCTS TABLE */

//    /* Returns all products */
//    public ArrayList<Product> getAllProducts() {
//        SQLiteDatabase db = getWritableDatabase();
//        String sql = "SELECT * FROM " + TBL_PRODUCTS + " WHERE deleted_at IS NULL";
//
//        ArrayList<Product> products = new ArrayList<Product>();
//
//        Cursor cur = db.rawQuery(sql, null);
//        while (cur.moveToNext()) {
//            Product p = new Product();
//            p.setName(cur.getString(1));
//            p.setDosageFormatAndStrength(cur.getString(2));
//            p.setGenericName(cur.getString(3));
//            p.setDescription(cur.getString(4));
//            p.setPrice(cur.getDouble(5));
//            p.setUnit(cur.getString(6));
//
//            products.add(p);
//        }
//
//        return products;
//    }

    /**
     * Returns list of products with a specific subcategory
     * @param subCategoryId = 0 // to return all products
     */
    public ArrayList<HashMap<String, String>> getProductsBySubCategory(int subCategoryId){
        ArrayList<HashMap<String, String>> products = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String sql;
        if(subCategoryId == 0){
            sql = "SELECT * FROM "+TBL_PRODUCTS;
        }else{
            sql = "SELECT * FROM "+TBL_PRODUCTS+" WHERE subcategory_id='"+subCategoryId+"'";
        }

        Cursor cur = db.rawQuery(sql, null);
        System.out.println("\nPANGITKA: SQL_getProductsBySubCategory: "+sql);

        cur.moveToFirst();
        while(cur.isAfterLast() == false){
            HashMap<String, String> map = new HashMap<>();
            map.put(PRODUCTS_ID, cur.getString(1));
            map.put(PRODUCT_SUBCATEGORY_ID, cur.getString(2));
            map.put(PRODUCT_NAME, cur.getString(3));
            map.put(PRODUCT_GENERIC_NAME, cur.getString(4));
            map.put(PRODUCT_DESCRIPTION, cur.getString(5));
            map.put(PRODUCT_PRESCRIPTION_REQUIRED, cur.getString(6));
            map.put(PRODUCT_PRICE, cur.getString(7));
            map.put(PRODUCT_UNIT, cur.getString(8));
            map.put(PRODUCT_PHOTO, cur.getString(9));
            map.put(PRODUCT_CREATED_AT, cur.getString(10));
            map.put(PRODUCT_UPDATED_AT, cur.getString(11));
            map.put(PRODUCT_DELETED_AT, cur.getString(12));

            products.add(map);
            cur.moveToNext();
        }
        cur.close();
        db.close();
        return products;
    }

    /* Returns product information
    * @param int id
    * */
    public Product getProductById(int id){
        Product prod = new Product();
        SQLiteDatabase db = getWritableDatabase();
        String sql = "Select * from "+TBL_PRODUCTS+" where product_id='"+id+"'";

        Cursor cur = db.rawQuery(sql, null);
        cur.moveToFirst();
        while(!cur.isAfterLast()){
            prod.setId(cur.getInt(cur.getColumnIndex(PRODUCTS_ID)));
            prod.setProductId(cur.getInt(cur.getColumnIndex(SERVER_PRODUCT_ID)));
            prod.setSubCategoryId(cur.getInt(cur.getColumnIndex(PRODUCT_SUBCATEGORIES_ID)));
            prod.setName(cur.getString(cur.getColumnIndex(PRODUCT_NAME)));
            prod.setGenericName(cur.getString(cur.getColumnIndex(PRODUCT_GENERIC_NAME)));
            prod.setDescription(cur.getString(cur.getColumnIndex(PRODUCT_DESCRIPTION)));
            prod.setPrescriptionRequired(cur.getInt(cur.getColumnIndex(PRODUCT_PRESCRIPTION_REQUIRED)));
            prod.setPrice(cur.getDouble(cur.getColumnIndex(PRODUCT_PRICE)));
            prod.setUnit(cur.getString(cur.getColumnIndex(PRODUCT_UNIT)));
            prod.setPhoto(cur.getString(cur.getColumnIndex(PRODUCT_PHOTO)));
            prod.setCreatedAt(cur.getString(cur.getColumnIndex(PRODUCT_CREATED_AT)));
            prod.setUpdatedAt(cur.getString(cur.getColumnIndex(PRODUCT_UPDATED_AT)));
            prod.setDeletedAt(cur.getString(cur.getColumnIndex(PRODUCT_DELETED_AT)));
            cur.moveToNext();
        }
        cur.close();
        db.close();
        return prod;
    }

    /* Create a record for "products" table here */
    public boolean insertProduct(Product product) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));

        values.put(SERVER_PRODUCT_ID, product.getProductId());
        values.put(PRODUCT_SUBCATEGORY_ID, product.getSubCategoryId());
        values.put(PRODUCT_NAME, product.getName());
        values.put(PRODUCT_GENERIC_NAME, product.getGenericName());
        values.put(PRODUCT_DESCRIPTION, product.getDescription());
        values.put(PRODUCT_PRESCRIPTION_REQUIRED, product.getPrescriptionRequired());
        values.put(PRODUCT_PHOTO, product.getPhoto());
        values.put(PRODUCT_PRICE, product.getPrice());
        values.put(PRODUCT_UNIT, product.getUnit());

        values.put(PRODUCT_CREATED_AT, product.getCreatedAt());
        values.put(PRODUCT_UPDATED_AT, product.getUpdatedAt());

        long rowID = db.insert(TBL_PRODUCTS, null, values);
        db.close();
        return rowID > 0;
    }

    public boolean updateProduct(Product product) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));

        values.put(PRODUCT_NAME, product.getName());
        values.put(PRODUCT_GENERIC_NAME, product.getGenericName());
        values.put(PRODUCT_DESCRIPTION, product.getDescription());
        values.put(PRODUCT_PHOTO, product.getPhoto());
        values.put(PRODUCT_PRICE, product.getPrice());
        values.put(PRODUCT_UNIT, product.getUnit());

        values.put(PRODUCT_CREATED_AT, product.getCreatedAt());
        values.put(PRODUCT_UPDATED_AT, product.getUpdatedAt());

        long rowID = db.update(TBL_PRODUCTS, values, PRODUCTS_ID + "=" + product.getId(), null);
        db.close();
        return rowID > 0;
    }


    /* INSERT UPDATE DELETE SQLs for BASKET TABLE */
        /* Create new record for "basket" table */
        public boolean insertBasket(Basket basket){

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String datenow = dateFormat.format(date);

            int patient_id = this.getCurrentLoggedInPatient().getServerID();

            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(BASKET_PATIENT_ID, patient_id);
            values.put(BASKET_PRODUCT_ID, basket.getProductId());
            values.put(BASKET_QUANTITY, basket.getQuantity());
            values.put(BASKET_CREATED_AT, datenow);

            long row = db.insert(TBL_BASKETS, null, values);
            db.close();
            return row > 0;
        }

        /* Returns basket
        * @param Integer id
        * */
        public Basket getBasket(int productId){
            Basket basket = new Basket();

//            String sql = "Select * from "+TBL_BASKETS+" where basket_id="+productId+" and patient_id="+this.getCurrentLoggedInPatient().getServerID();
            String sql = "Select * from "+TBL_BASKETS+" where id="+productId+" and patient_id="+this.getCurrentLoggedInPatient().getServerID();
            System.out.println("\ngetBasket: "+sql);
            SQLiteDatabase db = getWritableDatabase();
            Cursor cur = db.rawQuery(sql, null);

            cur.moveToFirst();
            while(!cur.isAfterLast()){
                basket.setId(cur.getInt(0));
                basket.setBasketId(cur.getInt( cur.getColumnIndex(SERVER_BASKET_ID) ));
                basket.setPatienId(cur.getInt( cur.getColumnIndex(BASKET_PATIENT_ID) ));
                basket.setProductId(cur.getInt( cur.getColumnIndex(BASKET_PRODUCT_ID) ));
                basket.setQuantity(cur.getDouble( cur.getColumnIndex(BASKET_QUANTITY) ));
                basket.setCreatedAt(cur.getString( cur.getColumnIndex(BASKET_CREATED_AT) ));
                basket.setUpdatedAt(cur.getString( cur.getColumnIndex(BASKET_UPDATED_AT) ));
                cur.moveToNext();
            }
            cur.close();
            db.close();
            return basket;
        }

        /* Update basket
        *  @param Basket basket;
        * */
        public boolean updateBasket(Basket basket){
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String datenow = dateFormat.format(date);

            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(BASKET_QUANTITY, basket.getQuantity());
            values.put(BASKET_UPDATED_AT, datenow);

//            long row = db.update(TBL_BASKETS, values, SERVER_BASKET_ID+"="+basket.getBasketId(), null);
            long row = db.update(TBL_BASKETS, values, BASKET_ID+"="+basket.getId(), null);
            db.close();
            return row > 0;
        }

        /* Returns all basket items */
        public ArrayList<HashMap<String, String>> getAllBasketItems(){
            ArrayList<HashMap<String, String>> items = new ArrayList<>();

            String sql = "Select b.id, b.basket_id, p.name, p.price, b.quantity, p.unit from "+TBL_BASKETS+" as b " +
                    "inner join "+TBL_PRODUCTS+" as p on p.product_id = b.product_id where b.patient_id="+this.getCurrentLoggedInPatient().getServerID()+"";

            SQLiteDatabase db = getWritableDatabase();
            Cursor cur = db.rawQuery(sql, null);

            cur.moveToFirst();
            while(!cur.isAfterLast()){
                HashMap<String, String> map = new HashMap<>();
                map.put(BASKET_ID, cur.getString(cur.getColumnIndex(BASKET_ID)));
                map.put(SERVER_BASKET_ID, cur.getString(cur.getColumnIndex(SERVER_BASKET_ID)));
                map.put(PRODUCT_NAME, cur.getString(cur.getColumnIndex(PRODUCT_NAME)) );
                map.put(PRODUCT_PRICE, String.valueOf( cur.getInt( cur.getColumnIndex(PRODUCT_PRICE) ) ));
                map.put(BASKET_QUANTITY, String.valueOf( cur.getInt( cur.getColumnIndex(BASKET_QUANTITY) ) ));
                map.put(PRODUCT_UNIT, String.valueOf(cur.getInt( cur.getColumnIndex(PRODUCT_UNIT) ) ));
                items.add(map);
                cur.moveToNext();
            }
            cur.close();
            db.close();
            return items;
        }

        /** Deletes an item from table "baskets"
        *   @param "id"
        * */
        public boolean deleteBasketItem(int basketId){
            SQLiteDatabase db = getWritableDatabase();
//          long row = db.delete(TBL_BASKETS, SERVER_BASKET_ID+"="+basketId, null);
            long row = db.delete(TBL_BASKETS, BASKET_ID+"="+basketId, null); /* Use above query when it's connected to server; Partial pa ni*/

            return row > 0;
        }



    /* Returns currently loggedin patient */
    public Patient getCurrentLoggedInPatient(){
        Patient patient = this.getloginPatient(HomeTileActivity.getUname());
        return patient;
    }
}