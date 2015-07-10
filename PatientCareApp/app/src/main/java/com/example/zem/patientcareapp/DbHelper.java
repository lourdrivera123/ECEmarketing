package com.example.zem.patientcareapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.zem.patientcareapp.GetterSetter.ClinicDoctor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.List;


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
            PTNT_MOBILE_NO = "mobile_no",
            PTNT_EMAIL = "email_address",
            PTNT_PHOTO = "photo";

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
            DOC_FULLNAME = "fullname",
            DOC_SPECIALTY_NAME = "name",
            DOC_PRC_NO = "prc_no",
            DOC_SUB_SPECIALTY_ID = "sub_specialty_id",
            DOC_PHOTO = "photo",
            DOC_AFFILIATIONS = "affiliations";

    //SPECIALTIES_TABLE
    public static final String TBL_SPECIALTIES = "specialties",
            SPECIALTY_ID = "id",
            SERVER_SPECIALTY_ID = "specialty_id",
            SPECIALTY_NAME = "name";

    //SPECIALTIES_TABLE
    public static final String TBL_SUB_SPECIALTIES = "sub_specialties",
            SUB_SPECIALTY_ID = "id",
            SERVER_SUB_SPECIALTY_ID = "sub_specialty_id",
            SUB_SPECIALTY_FOREIGN_ID = "specialty_id",
            SUB_SPECIALTY_NAME = "name";

    // PRODUCT_CATEGORIES TABLE
    public static final String PROD_CAT_NAME = "name",
            TBL_PRODUCT_CATEGORIES = "product_categories",
            PRODUCT_CATEGORIES_ID = "id",
            SERVER_PRODUCT_CATEGORY_ID = "product_category_id";

    // PRODUCT_SUBCATEGORIES TABLE
    public static final String TBL_PRODUCT_SUBCATEGORIES = "product_subcategories",
            PROD_SUBCAT_NAME = "name",
            PROD_SUBCAT_CATEGORY_ID = "category_id",
            PRODUCT_SUBCATEGORIES_ID = "id",
            SERVER_PRODUCT_SUBCATEGORY_ID = "product_subcategory_id";

    // PRODUCTS TABLE
    public static final String TBL_PRODUCTS = "products",
            PRODUCT_ID = "id",
            PRODUCT_SUBCATEGORY_ID = "subcategory_id",
            SERVER_PRODUCT_ID = "product_id",
            PRODUCT_NAME = "name",
            PRODUCT_GENERIC_NAME = "generic_name",
            PRODUCT_DESCRIPTION = "description",
            PRODUCT_PRESCRIPTION_REQUIRED = "presciption_required",
            PRODUCT_PRICE = "price",
            PRODUCT_UNIT = "unit",
            PRODUCT_PACKING = "packing",
            PRODUCT_QTY_PER_PACKING = "qty_per_packing",
            PRODUCT_SKU = "sku",
            PRODUCT_PHOTO = "photo";

    //DOSAGE_FORMAT_AND_STRENGTH TABLE
    public static final String TBL_DOSAGE = "dosage_format_and_strength",
            DOSAGE_ID = "id",
            SERVER_DOSAGE_ID = "dosage_id",
            DOSAGE_PROD_ID = "product_id",
            DOSAGE_NAME = "name";

    // BASKET TABLE
    public static final String TBL_BASKETS = "baskets",
            BASKET_ID = "id",
            SERVER_BASKET_ID = "basket_id",
            BASKET_PATIENT_ID = "patient_id",
            BASKET_PRODUCT_ID = "product_id",
            BASKET_QUANTITY = "quantity";

    // PATIENT_RECORDS TABLE
    public static final String TBL_PATIENT_RECORDS = "patient_records",
            RECORDS_ID = "id",
            SERVER_RECORDS_ID = "record_id",
            RECORDS_DOCTOR_ID = "doctor_id",
            RECORDS_DOCTOR_NAME = "doctor_name",
            RECORDS_PATIENT_ID = "patient_id",
            RECORDS_COMPLAINT = "complaints",
            RECORDS_FINDINGS = "findings",
            RECORDS_DATE = "record_date",
            RECORDS_NOTE = "note";

    // TREATMENTS TABLE
    public static final String TBL_TREATMENTS = "treatments",
            TREATMENTS_ID = "id",
            TREATMENTS_RECORD_ID = "patient_record_id",
            SERVER_TREATMENTS_ID = "treatments_id",
            TREATMENTS_MEDICINE_NAME = "medicine_name",
            TREATMENTS_GENERIC_NAME = "generic_name",
            TREATMENTS_QUANITY = "quantity",
            TREATMENTS_PRESCRIPTION = "prescription";


    // CLINICS TABLE
    public static final String TBL_CLINICS = "clinics",
            CLINIC_NAME = "name",
            CLINIC_CONTACT_NO = "contact_no",
            CLINIC_ADDRESSS_UNIT_BUILDING_NO = "address_unit_building_no",
            CLINIC_ADDRESS_STREET = "address_street",
            CLINIC_ADDRESS_BARANGAY = "address_barangay",
            CLINIC_ADDRESS_CITY_MUNICIPALITY = "address_city_municipality",
            CLINIC_ADDRESS_PROVINCE = "address_province",
            CLINIC_ADDRESS_REGION = "address_region",
            CLINIC_ADDRESS_ZIP = "address_zip",
            CLINICS_ID = "id",
            SERVER_CLINICS_ID = "clinics_id";

    // CLINIC_DOCTOR TABLE
    public static final String TBL_CLINIC_DOCTOR = "clinic_doctor",
            CD_ID = "id",
            CD_SERVER_ID = "clinic_doctor_id",
            CD_CLINIC_ID = "clinic_id",
            CD_DOCTOR_ID = "doctor_id",
            CD_CLINIC_SCHED = "clinic_sched",
            CD_IS_ACTIVE = "is_active";

    // SECRETARIES TABLE
    public static final String SEC_FNAME = "fname",
            SEC_MNAME = "mname",
            SEC_LNAME = "lname",
            SEC_ADDRESS_HOUSE_NO = "address_house_no",
            SEC_ADDRESS_STREET = "address_street",
            SEC_ADDRESS_BARANGAY = "address_barangay",
            SEC_ADDRESS_CITY_MUNICIPALITY = "address_city_municipality",
            SEC_ADDRESS_PROVINCE = "address_province",
            SEC_ADDRESS_REGION = "address_region",
            SEC_ADDRESS_ZIP = "address_zip",
            SEC_CELL_NO = "cell_no",
            SEC_TEL_NO = "tel_no",
            SEC_EMAIL = "email",
            SEC_PHOTO = "photo",
            TBL_SECRETARIES = "secretaries",
            SECRETARIES_ID = "id",
            SERVER_SECRETARIES_ID = "secretaries_id";

    // CLINIC_SECRETARY TABLE
    public static final String CS_SECRETARY_ID = "secretary_id",
            CS_IS_ACTIVE = "is_active",
            TBL_CLINIC_SECRETARY = "clinic_secretary",
            CS_CLINIC_ID = "clinic_id";

    // DOCTOR_SECRETARY TABLE
    public static final String TBL_DOCTOR_SECRETARY = "doctor_secretary",
            DS_SECRETARY_ID = "secretary_id",
            DS_DOCTOR_ID = "doctor_id",
            DS_IS_ACTIVE = "is_active";

    // PROMO DISCOUNT TABLE
    public static final String PROMO_D_PRODUCT_ID = "product_id",
            PROMO_D_NAME = "name",
            PROMO_D_START_DATE = "start_date",
            PROMO_D_END_DATE = "end_date",
            PROMO_D_TYPE = "type",
            PROMO_D_QUANTITY_REQUIRED = "quantity_required",
            PROMO_D_LESS = "less",
            TBL_PROMO_DISCOUNTS = "promo_discounts",
            PROMO_DISCOUNTS_ID = "id",
            SERVER_PROMO_DISCOUNTS_ID = "promo_discounts_id";

    // PROMO FREE PRODUCTS TABLE
    public static final String PROMO_FP_PRODUCT_ID = "product_id",
            PROMO_FP_PROMO_ID = "promo_id",
            PROMO_FP_NO_OF_UNITS_FREE = "no_of_units_free",
            TBL_PROMO_FREE_PRODUCTS = "promo_free_products",
            PROMO_FREE_PRODUCTS_ID = "id",
            SERVER_PROMO_FREE_PRODUCTS_ID = "promo_free_products_id";

    // UPLOADS ON PRESCRIPTIONS
    public static final String TBL_PATIENT_PRESCRIPTIONS = "patient_prescriptions",
            PRESCRIPTIONS_ID = "id",
            PRESCRIPTIONS_SERVER_ID = "prescriptions_id",
            PRESCRIPTIONS_PATIENT_ID = "patient_id",
            PRESCRIPTIONS_FILENAME = "filename",
            PRESCRIPTIONS_APPROVED = "is_approved";

    public static final String CREATED_AT = "created_at", DELETED_AT = "deleted_at", UPDATED_AT = "updated_at";

    public static String doctors_string_xml = "", products_string_xml = "";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL to create table "doctors"
        String sql_create_tbl_doctors = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, %s TEXT, " +
                        "%s TEXT, %s TEXT, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TBL_DOCTORS, DOC_ID, DOC_DOC_ID, DOC_LNAME, DOC_MNAME, DOC_FNAME, DOC_PRC_NO,
                DOC_SUB_SPECIALTY_ID, DOC_PHOTO, DOC_AFFILIATIONS, CREATED_AT, UPDATED_AT, DELETED_AT);

        String sql_create_tbl_specialties = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, " +
                        "%s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TBL_SPECIALTIES, SPECIALTY_ID, SERVER_SPECIALTY_ID, SPECIALTY_NAME, CREATED_AT, UPDATED_AT, DELETED_AT);

        String sql_create_tbl_sub_specialties = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, %s INTEGER, " +
                        "%s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TBL_SUB_SPECIALTIES, SUB_SPECIALTY_ID, SERVER_SUB_SPECIALTY_ID, SUB_SPECIALTY_FOREIGN_ID, SUB_SPECIALTY_NAME,
                CREATED_AT, UPDATED_AT, DELETED_AT);

        // SQL to create table "tbl_updates"
        String sql_create_tbl_updates = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s INTEGER)",
                TBL_UPDATES, UPDATE_ID, UPDATE_TBL_NAME, UPDATE_TIMESTAMP, UPDATE_SEEN);

        // SQL to create table "patients"
        String sql_create_tbl_patients = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, " +
                        "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, " +
                        "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TBL_PATIENTS, PTNT_ID, PTNT_PATIENT_ID, PTNT_FNAME, PTNT_MNAME, PTNT_LNAME, PTNT_USERNAME, PTNT_PASSWORD, PTNT_OCCUPATION,
                PTNT_BIRTHDATE, PTNT_SEX, PTNT_CIVIL_STATUS, PTNT_HEIGHT, PTNT_WEIGHT, PTNT_UNIT_NO, PTNT_BUILDING, PTNT_LOT_NO, PTNT_BLOCK_NO,
                PTNT_PHASE_NO, PTNT_HOUSE_NO, PTNT_STREET, PTNT_BARANGAY, PTNT_CITY, PTNT_PROVINCE, PTNT_REGION, PTNT_ZIP, PTNT_TEL_NO, PTNT_MOBILE_NO,
                PTNT_EMAIL, PTNT_PHOTO, CREATED_AT, UPDATED_AT, DELETED_AT);

        // SQL to create table "product_categories"
        String sql_create_tbl_product_categories = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " %s INTEGER UNIQUE, %s TEXT, %s  TEXT , %s  TEXT , %s TEXT  )",
                TBL_PRODUCT_CATEGORIES, PRODUCT_CATEGORIES_ID, SERVER_PRODUCT_CATEGORY_ID, PROD_CAT_NAME, CREATED_AT,
                UPDATED_AT, DELETED_AT);

        // SQL to create table "product_subcategories"
        String sql_create_tbl_product_subcategories = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " %s INTEGER UNIQUE, %s TEXT, %s INTEGER, %s  TEXT , %s  TEXT , %s  TEXT  )",
                TBL_PRODUCT_SUBCATEGORIES, PRODUCT_SUBCATEGORIES_ID, SERVER_PRODUCT_SUBCATEGORY_ID, PROD_SUBCAT_NAME,
                PROD_SUBCAT_CATEGORY_ID, CREATED_AT, UPDATED_AT, DELETED_AT);

        // SQL to create table "products"
        String sql_create_tbl_products = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, " +
                        "%s TEXT, %s TEXT,  %s TEXT, %s TEXT , %s INTEGER, %s DOUBLE, %s TEXT, %s TEXT, %s INTEGER, %s  TEXT , %s  TEXT , %s  TEXT,  %s  TEXT , %s  TEXT  )",
                TBL_PRODUCTS, PRODUCT_ID, SERVER_PRODUCT_ID, PRODUCT_SUBCATEGORY_ID, PRODUCT_NAME, PRODUCT_GENERIC_NAME,
                PRODUCT_DESCRIPTION, PRODUCT_PRESCRIPTION_REQUIRED, PRODUCT_PRICE, PRODUCT_UNIT, PRODUCT_PACKING, PRODUCT_QTY_PER_PACKING,
                PRODUCT_SKU, PRODUCT_PHOTO, CREATED_AT, UPDATED_AT, DELETED_AT);


        // SQL TO CREATE TABLE "TBL_DOSAGE"
        String sql_create_dosage_table = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
                TBL_DOSAGE, DOSAGE_ID, SERVER_DOSAGE_ID, DOSAGE_PROD_ID, DOSAGE_NAME, CREATED_AT, UPDATED_AT);

        // SQL to create table "baskets"
        String sql_create_tbl_baskets = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, " +
                        "%s INTEGER, %s INTEGER, %s DOUBLE, %s  TEXT , %s  TEXT , %s  TEXT  )",
                TBL_BASKETS, BASKET_ID, SERVER_BASKET_ID, BASKET_PATIENT_ID, BASKET_PRODUCT_ID, BASKET_QUANTITY,
                CREATED_AT, UPDATED_AT, DELETED_AT);

        // SQL to create PATIENT_RECORDS TABLE
        String sql_create_patient_records = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT )",
                TBL_PATIENT_RECORDS, RECORDS_ID, SERVER_RECORDS_ID, RECORDS_DOCTOR_ID, RECORDS_DOCTOR_NAME, RECORDS_PATIENT_ID,
                RECORDS_COMPLAINT, RECORDS_FINDINGS, RECORDS_DATE, RECORDS_NOTE, CREATED_AT, UPDATED_AT, DELETED_AT);

        // SQL TO create TREATMENTS TABLE
        String sql_create_treatments_table = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT )",
                TBL_TREATMENTS, TREATMENTS_ID, TREATMENTS_RECORD_ID, SERVER_TREATMENTS_ID, TREATMENTS_MEDICINE_NAME,
                TREATMENTS_GENERIC_NAME, TREATMENTS_QUANITY, TREATMENTS_PRESCRIPTION, CREATED_AT, UPDATED_AT, DELETED_AT);

        // SQL to create table "clinics"
        String sql_create_clinics_table = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT," +
                        " %s TEXT, %s  TEXT , %s  TEXT , %s  TEXT  )",
                TBL_CLINICS, CLINICS_ID, SERVER_CLINICS_ID, CLINIC_NAME, CLINIC_CONTACT_NO, CLINIC_ADDRESSS_UNIT_BUILDING_NO, CLINIC_ADDRESS_STREET, CLINIC_ADDRESS_BARANGAY, CLINIC_ADDRESS_CITY_MUNICIPALITY,
                CLINIC_ADDRESS_PROVINCE, CLINIC_ADDRESS_REGION, CLINIC_ADDRESS_ZIP, CREATED_AT, UPDATED_AT, DELETED_AT);

        // SQL to create table "clinic_doctor"
        String sql_create_clinic_doctor_table = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INT )",
                TBL_CLINIC_DOCTOR, CD_ID, CD_SERVER_ID, CD_CLINIC_ID, CD_DOCTOR_ID, CD_CLINIC_SCHED, CD_IS_ACTIVE);

        // SQL to create table "secretaries"
        String sql_create_secretaries_table = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, " +
                        "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s  TEXT , %s  TEXT , %s  TEXT  )",
                TBL_SECRETARIES, SECRETARIES_ID, SERVER_SECRETARIES_ID, SEC_FNAME, SEC_MNAME, SEC_LNAME, SEC_ADDRESS_HOUSE_NO,
                SEC_ADDRESS_STREET, SEC_ADDRESS_BARANGAY, SEC_ADDRESS_CITY_MUNICIPALITY, SEC_ADDRESS_PROVINCE, SEC_ADDRESS_REGION, SEC_ADDRESS_ZIP,
                SEC_CELL_NO, SEC_TEL_NO, SEC_EMAIL, SEC_PHOTO, CREATED_AT, UPDATED_AT, DELETED_AT);

        // SQL to create table "clinic_secretary"
        String sql_create_clinic_secretary_table = String.format("CREATE TABLE %s ( %s INTEGER, %s INTEGER, %s INT )",
                TBL_CLINIC_SECRETARY, CS_CLINIC_ID, CS_SECRETARY_ID, CS_IS_ACTIVE);

        // SQL to create table "doctor_secretary"
        String sql_create_doctor_secretary_table = String.format("CREATE TABLE %s ( %s INTEGER, %s INTEGER, %s INT )",
                TBL_DOCTOR_SECRETARY, DS_DOCTOR_ID, DS_SECRETARY_ID, DS_IS_ACTIVE);

        // SQL to create table "promo_discount"
        String sql_create_promo_discount_table = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s TEXT, %s  TEXT , %s  TEXT , " +
                        "%s INTEGER, %s INTEGER, %s DOUBLE, %s  TEXT , %s  TEXT , %s  TEXT  )",
                TBL_PROMO_DISCOUNTS, PROMO_DISCOUNTS_ID, SERVER_PROMO_DISCOUNTS_ID, PROMO_D_PRODUCT_ID, PROMO_D_NAME, PROMO_D_START_DATE, PROMO_D_END_DATE,
                PROMO_D_TYPE, PROMO_D_QUANTITY_REQUIRED, PROMO_D_LESS, CREATED_AT, UPDATED_AT, DELETED_AT);

        // SQL to create table "promo_free_products"
        String sql_create_promo_free_products_table = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s  TEXT , %s  TEXT , %s  TEXT  )",
                TBL_PROMO_FREE_PRODUCTS, PROMO_FREE_PRODUCTS_ID, SERVER_PROMO_FREE_PRODUCTS_ID, PROMO_FP_PRODUCT_ID, PROMO_FP_PROMO_ID, PROMO_FP_NO_OF_UNITS_FREE, CREATED_AT, UPDATED_AT, DELETED_AT);

        String sql_create_prescriptions_upload = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s TEXT, %s INTEGER, %s TEXT, %s TEXT)",
                TBL_PATIENT_PRESCRIPTIONS, PRESCRIPTIONS_ID, PRESCRIPTIONS_SERVER_ID, PRESCRIPTIONS_PATIENT_ID, PRESCRIPTIONS_FILENAME, PRESCRIPTIONS_APPROVED, CREATED_AT, DELETED_AT);

        db.execSQL(sql_create_tbl_doctors);
        db.execSQL(sql_create_tbl_specialties);
        db.execSQL(sql_create_tbl_sub_specialties);
        db.execSQL(sql_create_tbl_updates);
        db.execSQL(sql_create_tbl_patients);
        db.execSQL(sql_create_tbl_baskets);
        db.execSQL(sql_create_tbl_product_categories);
        db.execSQL(sql_create_tbl_product_subcategories);
        db.execSQL(sql_create_tbl_products);
        db.execSQL(sql_create_dosage_table);
        db.execSQL(sql_create_patient_records);
        db.execSQL(sql_create_treatments_table);
        db.execSQL(sql_create_clinics_table);
        db.execSQL(sql_create_clinic_doctor_table);
        db.execSQL(sql_create_secretaries_table);
        db.execSQL(sql_create_clinic_secretary_table);
        db.execSQL(sql_create_doctor_secretary_table);
        db.execSQL(sql_create_promo_discount_table);
        db.execSQL(sql_create_promo_free_products_table);
        db.execSQL(sql_create_prescriptions_upload);

        insertTableNamesToUpdates(TBL_DOCTORS, db);
        insertTableNamesToUpdates(TBL_SPECIALTIES, db);
        insertTableNamesToUpdates(TBL_SUB_SPECIALTIES, db);
        insertTableNamesToUpdates(TBL_PRODUCTS, db);
        insertTableNamesToUpdates(TBL_PRODUCT_CATEGORIES, db);
        insertTableNamesToUpdates(TBL_PRODUCT_SUBCATEGORIES, db);
        insertTableNamesToUpdates(TBL_BASKETS, db);
        insertTableNamesToUpdates(TBL_DOSAGE, db);
        insertTableNamesToUpdates(TBL_PATIENT_RECORDS, db);
        insertTableNamesToUpdates(TBL_TREATMENTS, db);
        insertTableNamesToUpdates(TBL_CLINICS, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + DB_NAME;
        db.execSQL(sql);
    }

    //INSERT METHODS
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
        values.put(PTNT_MOBILE_NO, patient.getMobile_no());
        values.put(PTNT_EMAIL, patient.getEmail());
        values.put(PTNT_PHOTO, patient.getPhoto());
        values.put(CREATED_AT, created_at);

        long insert_patient = db.insert(TBL_PATIENTS, null, values);
        db.close();
        return insert_patient > 0;
    }

    public boolean insertBasket(Basket basket) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String datenow = dateFormat.format(date);

        int patient_id = this.getCurrentLoggedInPatient().getServerID();

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SERVER_BASKET_ID, basket.getBasketId());
        values.put(BASKET_PATIENT_ID, patient_id);
        values.put(BASKET_PRODUCT_ID, basket.getProductId());
        values.put(BASKET_QUANTITY, basket.getQuantity());
        values.put(CREATED_AT, datenow);

        long row = db.insert(TBL_BASKETS, null, values);
        db.close();
        return row > 0;
    }

    /* SYNC CHECKER and etc. */
    public boolean insertTableNamesToUpdates(String table_name, SQLiteDatabase db) {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ContentValues values = new ContentValues();
        values.put(UPDATE_TBL_NAME, table_name);
        values.put(UPDATE_TIMESTAMP, formatter.format(now));
        values.put(UPDATE_SEEN, 0);

        long rowID = db.insert(TBL_UPDATES, null, values);
        return rowID > 0;
    }

    public ArrayList<Integer> insertTreatment(ArrayList<HashMap<String, String>> items, long recordID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        ArrayList<Integer> arrayOfID = new ArrayList();

        db.delete(TBL_TREATMENTS, TREATMENTS_RECORD_ID + "=" + recordID, null);
        for (int x = 0; x < items.size(); x++) {
            values.put(TREATMENTS_RECORD_ID, recordID);
            values.put(TREATMENTS_MEDICINE_NAME, items.get(x).get("medicine_name"));
            values.put(TREATMENTS_GENERIC_NAME, items.get(x).get("generic_name"));
            values.put(TREATMENTS_QUANITY, items.get(x).get("quantity"));
            values.put(TREATMENTS_PRESCRIPTION, items.get(x).get("prescription"));

            long rowID = db.insert(TBL_TREATMENTS, null, values);
            arrayOfID.add((int) rowID);
        }
        db.close();
        return arrayOfID;
    }

    public boolean insertDosage(Dosage dosage) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SERVER_DOSAGE_ID, dosage.getDosage_id());
        values.put(DOSAGE_PROD_ID, dosage.getProduct_id());
        values.put(DOSAGE_NAME, dosage.getName());

        long rowID = db.insert(TBL_DOSAGE, null, values);

        return rowID > 0;
    }

    /* Insert new product category */
    public boolean insertProductCategory(ProductCategory category) throws SQLiteConstraintException {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        long rowID = 0;
        try {
            values.put(PROD_CAT_NAME, category.getName());
            values.put(SERVER_PRODUCT_CATEGORY_ID, category.getCategoryId());
            values.put(CREATED_AT, category.getCreatedAt());
            values.put(UPDATED_AT, category.getUpdatedAt());
            values.put(DELETED_AT, category.getDeletedAt());

            rowID = db.insert(TBL_PRODUCT_CATEGORIES, null, values);
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
        db.close();
        return rowID > 0;
    }

    public boolean insertUploadOnPrescription(Integer patientID, String filename) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PRESCRIPTIONS_SERVER_ID, 0);
        values.put(PRESCRIPTIONS_PATIENT_ID, patientID);
        values.put(PRESCRIPTIONS_FILENAME, filename);
        values.put(PRESCRIPTIONS_APPROVED, 0);

        long rowID = db.insert(TBL_PATIENT_PRESCRIPTIONS, null, values);
        db.close();
        return rowID > 0;
    }
    //////////////////////////END OF INSERT METHODS///////////////////////////

    /////////////////////////SAVE METHODS (INSERT AND UPDATE)///////////////////////
    public long savePatientRecord(PatientRecord record, String request) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        long rowID = 0;
        values.put(SERVER_RECORDS_ID, record.getRecordID());
        values.put(RECORDS_PATIENT_ID, record.getPatientID());
        values.put(RECORDS_COMPLAINT, record.getComplaints());
        values.put(RECORDS_FINDINGS, record.getFindings());
        values.put(RECORDS_DATE, record.getDate());
        values.put(RECORDS_DOCTOR_ID, record.getDoctorID());
        values.put(RECORDS_DOCTOR_NAME, record.getDoctorName());
        values.put(RECORDS_NOTE, record.getNote());
        values.put(CREATED_AT, record.getCreated_at());
        values.put(UPDATED_AT, record.getUpdated_at());
        values.put(DELETED_AT, record.getDeleted_at());

        if (request.equals("insert")) {
            rowID = db.insert(TBL_PATIENT_RECORDS, null, values);
        } else if (request.equals("update")) {
            rowID = db.update(TBL_PATIENT_RECORDS, values, RECORDS_ID + "=" + record.getRecordID(), null);

        }
        db.close();
        return rowID;
    }

    public boolean savePromoFreeProducts(PromoFreeProducts promoFreeProducts, String type) {
        long row;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SERVER_PROMO_FREE_PRODUCTS_ID, promoFreeProducts.getPromoFreeProductsId());
        values.put(PROMO_FP_PROMO_ID, promoFreeProducts.getPromoId());
        values.put(PROMO_FP_PRODUCT_ID, promoFreeProducts.getPromoFreeProductsId());
        values.put(PROMO_FP_NO_OF_UNITS_FREE, promoFreeProducts.getNumberOfUnitsFree());
        values.put(CREATED_AT, promoFreeProducts.getCreatedAt());
        values.put(UPDATED_AT, promoFreeProducts.getUpdatedAt());
        values.put(DELETED_AT, promoFreeProducts.getDeletedAt());

        if (type.equals("insert")) {
            row = db.insert(TBL_PROMO_FREE_PRODUCTS, null, values);
        } else {
            row = db.update(TBL_PROMO_FREE_PRODUCTS, values, SERVER_PROMO_FREE_PRODUCTS_ID + "=" + promoFreeProducts.getPromoFreeProductsId(), null);
        }
        return row > 0;
    }

    public boolean saveClinic(Clinic clinic, String type) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SERVER_CLINICS_ID, clinic.getClinicsId());
        values.put(CLINIC_NAME, clinic.getName());
        values.put(CLINIC_CONTACT_NO, clinic.getContactNumber());
        values.put(CLINIC_ADDRESSS_UNIT_BUILDING_NO, clinic.getAddressUnitBuildingNo());
        values.put(CLINIC_ADDRESS_STREET, clinic.getAddressStreet());
        values.put(CLINIC_ADDRESS_BARANGAY, clinic.getAddressBarangay());
        values.put(CLINIC_ADDRESS_CITY_MUNICIPALITY, clinic.getAddressCityMunicipality());
        values.put(CLINIC_ADDRESS_PROVINCE, clinic.getAddressProvince());
        values.put(CLINIC_ADDRESS_REGION, clinic.getAddressRegion());
        values.put(CLINIC_ADDRESS_ZIP, clinic.getAddressZip());
        values.put(CREATED_AT, clinic.getCreatedAt());
        values.put(UPDATED_AT, clinic.getUpdatedAt());
        values.put(DELETED_AT, clinic.getDeletedAt());

        long row = 0;

        if (type.equals("insert")) {
            row = db.insert(TBL_CLINICS, null, values);
        } else if (type.equals("update")) {
            row = db.update(TBL_CLINICS, values, SERVER_CLINICS_ID + " = " + clinic.getClinicsId(), null);
        }

        db.close();
        return row > 0;
    }

    public boolean saveClinicDoctor(ClinicDoctor cd, String request) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CD_SERVER_ID, cd.getServerID());
        values.put(CD_DOCTOR_ID, cd.getDoctorID());
        values.put(CD_CLINIC_ID, cd.getClinicID());
        values.put(CD_CLINIC_SCHED, cd.getSchedule());
        values.put(CD_IS_ACTIVE, cd.getIsActive());

        long rowID = 0;

        if (request.equals("insert"))
            rowID = db.insert(TBL_CLINIC_DOCTOR, null, values);
        else if (request.equals("update"))
            rowID = db.update(TBL_CLINIC_DOCTOR, values, CD_SERVER_ID + " = " + cd.getServerID(), null);

        db.close();

        return rowID > 0;
    }

    public boolean saveTreatments(Treatments treatments, String request) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        long rowID = 0;

        values.put(SERVER_TREATMENTS_ID, treatments.getTreatments_id());
        values.put(TREATMENTS_RECORD_ID, treatments.getPatient_record_id());
        values.put(TREATMENTS_MEDICINE_NAME, treatments.getMedicine_name());
        values.put(TREATMENTS_GENERIC_NAME, treatments.getGeneric_name());
        values.put(TREATMENTS_QUANITY, treatments.getQuantity());
        values.put(TREATMENTS_PRESCRIPTION, treatments.getPrescription());
        values.put(CREATED_AT, treatments.getCreated_at());
        values.put(UPDATED_AT, treatments.getUpdated_at());
        values.put(DELETED_AT, treatments.getDeleted_at());

        if (request.equals("insert")) {
            rowID = db.insert(TBL_TREATMENTS, null, values);
        } else if (request.equals("update")) {
            rowID = db.update(TBL_TREATMENTS, values, SERVER_TREATMENTS_ID + "=" + treatments.getTreatments_id(), null);

        }
        return rowID > 0;
    }

    public boolean saveDoctor(Doctor doctor, String request) {
        long rowID = 0;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DOC_DOC_ID, doctor.getDoc_id());
        values.put(DOC_LNAME, doctor.getLname());
        values.put(DOC_MNAME, doctor.getMname());
        values.put(DOC_FNAME, doctor.getFname());
        values.put(DOC_PRC_NO, doctor.getPrc_no());
        values.put(DOC_SUB_SPECIALTY_ID, doctor.getSub_specialty_id());
        values.put(DOC_PHOTO, doctor.getPhoto());
        values.put(DOC_AFFILIATIONS, doctor.getAffiliation());
        values.put(CREATED_AT, doctor.getCreated_at());
        values.put(UPDATED_AT, doctor.getUpdated_at());
        values.put(DELETED_AT, doctor.getDeleted_at());

        if (request.equals("insert")) {
            rowID = db.insert(TBL_DOCTORS, null, values);

        } else if (request.equals("update")) {
            rowID = db.update(TBL_DOCTORS, values, DOC_ID + "=" + doctor.getDoc_id(), null);
        }
        return rowID > 0;
    }

    public boolean saveSpecialty(Specialty specialty, String request) {
        long rowID = 0;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SERVER_SPECIALTY_ID, specialty.getSpecialty_id());
        values.put(SPECIALTY_NAME, specialty.getName());
        values.put(CREATED_AT, specialty.getCreated_at());
        values.put(UPDATED_AT, specialty.getUpdated_at());
        values.put(DELETED_AT, specialty.getDeleted_at());

        if (request.equals("insert")) {
            rowID = db.insert(TBL_SPECIALTIES, null, values);
        } else if (request.equals("update")) {
            rowID = db.update(TBL_SPECIALTIES, values, SPECIALTY_ID + "=" + specialty.getSpecialty_id(), null);
        }
        return rowID > 0;
    }

    public boolean saveSubSpecialty(SubSpecialty sub_specialty, String request) {
        long rowID = 0;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SERVER_SUB_SPECIALTY_ID, sub_specialty.getSub_specialty_id());
        values.put(SUB_SPECIALTY_FOREIGN_ID, sub_specialty.getSpecialty_id());
        values.put(SUB_SPECIALTY_NAME, sub_specialty.getName());
        values.put(CREATED_AT, sub_specialty.getCreated_at());
        values.put(UPDATED_AT, sub_specialty.getUpdated_at());
        values.put(DELETED_AT, sub_specialty.getDeleted_at());

        if (request.equals("insert")) {
            rowID = db.insert(TBL_SUB_SPECIALTIES, null, values);
        } else if (request.equals("update")) {
            rowID = db.update(TBL_SUB_SPECIALTIES, values, SUB_SPECIALTY_ID + "=" + sub_specialty.getSpecialty_id(), null);
        }

        return rowID > 0;
    }
    //END OF SAVE METHODS

    //to be worked out
    public boolean updateLastUpdatedTable(String table_name, String server_timestamp) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UPDATE_TIMESTAMP, server_timestamp);

        int rowID = db.update(TBL_UPDATES, values, UPDATE_TBL_NAME + "= '" + table_name + "'", null);
        db.close();
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
        values.put(PTNT_MOBILE_NO, patient.getMobile_no());
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
            patient.setId(cur.getInt(cur.getColumnIndex(PTNT_ID)));
            patient.setServerID(cur.getInt(cur.getColumnIndex(PTNT_PATIENT_ID)));
            patient.setFname(cur.getString(cur.getColumnIndex(PTNT_FNAME)));
            patient.setLname(cur.getString(cur.getColumnIndex(PTNT_LNAME)));
            patient.setUsername(cur.getString(cur.getColumnIndex(PTNT_USERNAME)));
            patient.setPassword(cur.getString(cur.getColumnIndex(PTNT_PASSWORD)));
            patient.setOccupation(cur.getString(cur.getColumnIndex(PTNT_OCCUPATION)));
            patient.setBirthdate(cur.getString(cur.getColumnIndex(PTNT_BIRTHDATE)));
            patient.setSex(cur.getString(cur.getColumnIndex(PTNT_SEX)));
            patient.setCivil_status(cur.getString(cur.getColumnIndex(PTNT_CIVIL_STATUS)));
            patient.setHeight(cur.getString(cur.getColumnIndex(PTNT_HEIGHT)));
            patient.setWeight(cur.getString(cur.getColumnIndex(PTNT_WEIGHT)));
            patient.setUnit_floor_room_no(cur.getInt(cur.getColumnIndex(PTNT_UNIT_NO)));
            patient.setBuilding(cur.getString(cur.getColumnIndex(PTNT_BUILDING)));
            patient.setLot_no(cur.getInt(cur.getColumnIndex(PTNT_LOT_NO)));
            patient.setBlock_no(cur.getInt(cur.getColumnIndex(PTNT_BLOCK_NO)));
            patient.setPhase_no(cur.getInt(cur.getColumnIndex(PTNT_PHASE_NO)));
            patient.setAddress_house_no(cur.getInt(cur.getColumnIndex(PTNT_HOUSE_NO)));
            patient.setAddress_street(cur.getString(cur.getColumnIndex(PTNT_STREET)));
            patient.setAddress_barangay(cur.getString(cur.getColumnIndex(PTNT_BARANGAY)));
            patient.setAddress_city_municipality(cur.getString(cur.getColumnIndex(PTNT_CITY)));
            patient.setAddress_province(cur.getString(cur.getColumnIndex(PTNT_PROVINCE)));
            patient.setAddress_region(cur.getString(cur.getColumnIndex(PTNT_REGION)));
            patient.setAddress_zip(cur.getString(cur.getColumnIndex(PTNT_ZIP)));
            patient.setTel_no(cur.getString(cur.getColumnIndex(PTNT_TEL_NO)));
            patient.setMobile_no(cur.getString(cur.getColumnIndex(PTNT_MOBILE_NO)));
            patient.setEmail(cur.getString(cur.getColumnIndex(PTNT_EMAIL)));
            patient.setPhoto(cur.getString(cur.getColumnIndex(PTNT_PHOTO)));
        }
        cur.close();
        db.close();

        return patient;
    }

//    public boolean updateDoctor(Doctor doctor_object) {
//        SQLiteDatabase db = getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(DOC_DOC_ID, doctor_object.getDoc_id());
//        values.put(DOC_LNAME, doctor_object.getLname());
//        values.put(DOC_MNAME, doctor_object.getMname());
//        values.put(DOC_FNAME, doctor_object.getFname());
//        values.put(DOC_PRC_NO, doctor_object.getPrc_no());
//        values.put(DOC_ADDRESS_HOUSE_NO, doctor_object.getAddress_house_no());
//        values.put(DOC_ADDRESS_STREET, doctor_object.getAddress_street());
//        values.put(DOC_ADDRESS_BARANGAY, doctor_object.getAddress_barangay());
//        values.put(DOC_ADDRESS_CITY, doctor_object.getAddress_city_municipality());
//        values.put(DOC_ADDRESS_PROVINCE, doctor_object.getAddress_province());
//        values.put(DOC_ADDRESS_REGION, doctor_object.getAddress_region());
//        values.put(DOC_ADDRESS_COUNTRY, doctor_object.getCountry());
//        values.put(DOC_ZIP, doctor_object.getAddress_zip());
//        values.put(DOC_SPECIALTY, doctor_object.getSpecialty());
//        values.put(DOC_SUB_SPECIALTY, doctor_object.getSub_specialty());
//        values.put(DOC_CELL_NO, doctor_object.getCell_no());
//        values.put(DOC_TEL_NO, doctor_object.getTel_no());
//        values.put(DOC_PHOTO, doctor_object.getPhoto());
//        values.put(DOC_CLINIC_SCHED, doctor_object.getClinic_sched());
//        values.put(DOC_AFFILIATIONS, doctor_object.getAffiliation());
//        values.put(DOC_CLINIC_ID, doctor_object.getClinic_id());
//        values.put(DOC_EMAIL, doctor_object.getEmail());
//        values.put(DOC_SEC_ID, doctor_object.getSecretary_id());
//
//        int rowID = db.update(TBL_DOCTORS, values, DOC_ID + "=" + doctor_object.getDoc_id(), null);
//
//        return rowID > 0;
//    }

    public ArrayList<HashMap<String, String>> getAllProducts() {
        ArrayList<HashMap<String, String>> products = new ArrayList();
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_PRODUCTS;
        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {

            HashMap<String, String> map = new HashMap();
            map.put(PRODUCT_ID, cur.getString(cur.getColumnIndex(PRODUCT_ID)));
            map.put(SERVER_PRODUCT_ID, cur.getString(cur.getColumnIndex(SERVER_PRODUCT_ID)));
            map.put(PRODUCT_NAME, cur.getString(cur.getColumnIndex(PRODUCT_NAME)));
            map.put(PRODUCT_DESCRIPTION, cur.getString(cur.getColumnIndex(PRODUCT_DESCRIPTION)));
            map.put(PRODUCT_PRICE, cur.getString(cur.getColumnIndex(PRODUCT_PRICE)));
            map.put(PRODUCT_PHOTO, cur.getString(cur.getColumnIndex(PRODUCT_PHOTO)));
            map.put(PRODUCT_SKU, cur.getString(cur.getColumnIndex(PRODUCT_SKU)));
            map.put(PRODUCT_UNIT, cur.getString(cur.getColumnIndex(PRODUCT_UNIT)));
            map.put(PRODUCT_PACKING, cur.getString(cur.getColumnIndex(PRODUCT_PACKING)));
            map.put(PRODUCT_QTY_PER_PACKING, cur.getString(cur.getColumnIndex(PRODUCT_QTY_PER_PACKING)));
            products.add(map);
        }
        cur.close();
        db.close();
        return products;
    }

    public ArrayList<String> getMedicine() {
        ArrayList<String> medicine = new ArrayList();
        String sql = "SELECT p.name, generic_name, d.name FROM products as p LEFT OUTER JOIN dosage_format_and_strength as d ON d.product_id = p.product_id";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        String med, name, joinedMedicine = "";

        while (cur.moveToNext()) {
            med = cur.getString(0);
            if (cur.getString(2) == null) {
                joinedMedicine = med + "";
            } else {
                name = cur.getString(2);
                joinedMedicine = med + " (" + name + ")";
            }
            medicine.add(joinedMedicine);
        }
        cur.close();
        db.close();
        return medicine;
    }

    public Medicine getSpecificMedicine(String med_name, Medicine medicine) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_PRODUCTS + " WHERE " + PRODUCT_NAME + " = '" + med_name + "'";
        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            medicine.setId(cur.getInt(cur.getColumnIndex(PRODUCT_ID)));
            medicine.setServerID(cur.getInt(cur.getColumnIndex(SERVER_PRODUCT_ID)));
            medicine.setMedicine_name(cur.getString(cur.getColumnIndex(PRODUCT_NAME)));
            medicine.setGeneric_name(cur.getString(cur.getColumnIndex(PRODUCT_GENERIC_NAME)));
            medicine.setDescription(cur.getString(cur.getColumnIndex(PRODUCT_DESCRIPTION)));
            medicine.setPrice(cur.getDouble(cur.getColumnIndex(PRODUCT_PRICE)));
            medicine.setUnit(cur.getString(cur.getColumnIndex(PRODUCT_UNIT)));
            medicine.setPhoto(cur.getString(cur.getColumnIndex(PRODUCT_PHOTO)));
        }
        cur.close();
        db.close();

        return medicine;
    }

    public ArrayList<HashMap<String, String>> getDoctorName() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_DOCTORS;
        Cursor cur = db.rawQuery(sql, null);
        HashMap<String, String> map;
        ArrayList<HashMap<String, String>> doctors = new ArrayList<HashMap<String, String>>();

        String fullname, fname, lname;
        while (cur.moveToNext()) {
            lname = Helpers.curGetStr(cur, DOC_LNAME);
            fname = Helpers.curGetStr(cur, DOC_FNAME);
            fullname = fname + " " + lname;

            map = new HashMap();
            map.put("ID", String.valueOf(cur.getInt(0)));
            map.put("fullname", fullname);
            doctors.add(map);
        }
        cur.close();
        db.close();

        return doctors;
    }

    public String getDoctorsStringXml() {
        return doctors_string_xml;
    }

    public JSONArray getAllJSONArrayFrom(String tbl_name) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + tbl_name;
        Cursor cursor = db.rawQuery(sql, null);
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
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
        return resultSet;
    }

    public JSONArray getAllProductsJSONArray() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_PRODUCTS;
        Cursor cursor = db.rawQuery(sql, null);
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

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

    public List<String> getAllProductCategoriesArray() {
        List<String> list = new ArrayList();
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_PRODUCT_CATEGORIES;
        Cursor cur = db.rawQuery(sql, null);
        int x = 0;
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            list.add(x, cur.getString(2));
            x++;
            cur.moveToNext();
        }
        cur.close();
        db.close();
        return list;
    }

    public int categoryGetIdByName(String name) {
        int id = 0;
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT id FROM " + TBL_PRODUCT_CATEGORIES + " WHERE name='" + name + "'";
        Cursor cur = db.rawQuery(sql, null);

        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            id = cur.getInt(0);
            cur.moveToNext();
        }
        db.close();
        return id;
    }

    public String[] getAllProductSubCategoriesArray(int categoryId) {
        List<String> list = new ArrayList();
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_PRODUCT_SUBCATEGORIES + " WHERE category_id='" + categoryId + "' ORDER BY name";
        Cursor cur = db.rawQuery(sql, null);
        int x = 0;

        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            list.add(x, cur.getString(2));
            x++;
            cur.moveToNext();
        }
        db.close();
        String[] arr = new String[list.size()];
        return list.toArray(arr);
    }

    /* Updates product category */
    public boolean updateProductCategory(ProductCategory category) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PROD_CAT_NAME, category.getName());
        values.put(CREATED_AT, category.getCreatedAt());
        values.put(UPDATED_AT, category.getUpdatedAt());
        values.put(DELETED_AT, category.getDeletedAt());

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
        values.put(CREATED_AT, subCategory.getCreatedAt());
        values.put(UPDATED_AT, subCategory.getUpdatedAt());
        values.put(DELETED_AT, subCategory.getDeletedAt());

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
        values.put(CREATED_AT, subCategory.getCreatedAt());
        values.put(UPDATED_AT, subCategory.getUpdatedAt());
        values.put(DELETED_AT, subCategory.getDeletedAt());

        long rowID = db.update(TBL_PRODUCT_SUBCATEGORIES, values, PRODUCT_SUBCATEGORIES_ID + "=" + subCategory.getId(), null);
        db.close();
        return rowID > 0;
    }

    /* returns subcategory id by subcategory name */
    public ProductSubCategory getSubCategoryByName(String name, int categoryId) {
        ProductSubCategory subCategory = new ProductSubCategory();
        SQLiteDatabase db = getWritableDatabase();
        name = name.replace("'", "''");
        String sql = "SELECT * FROM " + TBL_PRODUCT_SUBCATEGORIES + " where name='" + name + "' and category_id='" + categoryId + "'";
        Cursor cur = db.rawQuery(sql, null);
        while (cur.moveToNext()) {
            subCategory.setId(cur.getInt(cur.getColumnIndex(PRODUCT_SUBCATEGORIES_ID)));
            subCategory.setName(cur.getString(cur.getColumnIndex(PROD_SUBCAT_NAME)));
            subCategory.setCategoryId(Integer.parseInt(cur.getString(cur.getColumnIndex(PROD_SUBCAT_CATEGORY_ID))));
            subCategory.setCreatedAt(cur.getString(cur.getColumnIndex(CREATED_AT)));
            subCategory.setUpdatedAt(cur.getString(cur.getColumnIndex(UPDATED_AT)));
            subCategory.setDeletedAt(cur.getString(cur.getColumnIndex(DELETED_AT)));
        }
        return subCategory;
    }

    /* INSERT and UPDATE and other SQL's & functions for PRODUCTS TABLE */

    /**
     * Returns list of products with a specific subcategory
     *
     * @param subCategoryId = 0 // to return all products
     */
    public ArrayList<HashMap<String, String>> getProductsBySubCategory(int subCategoryId) {
        ArrayList<HashMap<String, String>> products = new ArrayList();
        SQLiteDatabase db = getWritableDatabase();
        String sql;

        if (subCategoryId == 0) {
            sql = "SELECT * FROM " + TBL_PRODUCTS;
        } else {
            sql = "SELECT * FROM " + TBL_PRODUCTS + " WHERE subcategory_id='" + subCategoryId + "'";
        }

        Cursor cur = db.rawQuery(sql, null);
        cur.moveToFirst();

        while (!cur.isAfterLast()) {
            HashMap<String, String> map = new HashMap();
            map.put(PRODUCT_ID, cur.getString(cur.getColumnIndex(PRODUCT_ID)));
            map.put(PRODUCT_SUBCATEGORY_ID, cur.getString(cur.getColumnIndex(PRODUCT_SUBCATEGORY_ID)));
            map.put(PRODUCT_NAME, cur.getString(cur.getColumnIndex(PRODUCT_NAME)));
            map.put(PRODUCT_GENERIC_NAME, cur.getString(cur.getColumnIndex(PRODUCT_GENERIC_NAME)));
            map.put(PRODUCT_DESCRIPTION, cur.getString(cur.getColumnIndex(PRODUCT_DESCRIPTION)));
            map.put(PRODUCT_PRESCRIPTION_REQUIRED, cur.getString(cur.getColumnIndex(PRODUCT_PRESCRIPTION_REQUIRED)));
            map.put(PRODUCT_PRICE, cur.getString(cur.getColumnIndex(PRODUCT_PRICE)));
            map.put(PRODUCT_UNIT, cur.getString(cur.getColumnIndex(PRODUCT_UNIT)));
            map.put(PRODUCT_PHOTO, cur.getString(cur.getColumnIndex(PRODUCT_PHOTO)));
            map.put(CREATED_AT, cur.getString(cur.getColumnIndex(CREATED_AT)));
            map.put(UPDATED_AT, cur.getString(cur.getColumnIndex(UPDATED_AT)));
            map.put(DELETED_AT, cur.getString(cur.getColumnIndex(DELETED_AT)));

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
    public Product getProductById(int id) {
        Product prod = new Product();
        SQLiteDatabase db = getWritableDatabase();
        String sql = "Select * from " + TBL_PRODUCTS + " where product_id='" + id + "'";

        Cursor cur = db.rawQuery(sql, null);
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            prod.setId(cur.getInt(cur.getColumnIndex(PRODUCT_ID)));
            prod.setProductId(cur.getInt(cur.getColumnIndex(SERVER_PRODUCT_ID)));
            prod.setSubCategoryId(cur.getInt(cur.getColumnIndex(PRODUCT_SUBCATEGORIES_ID)));
            prod.setName(cur.getString(cur.getColumnIndex(PRODUCT_NAME)));
            prod.setGenericName(cur.getString(cur.getColumnIndex(PRODUCT_GENERIC_NAME)));
            prod.setDescription(cur.getString(cur.getColumnIndex(PRODUCT_DESCRIPTION)));
            prod.setPrescriptionRequired(cur.getInt(cur.getColumnIndex(PRODUCT_PRESCRIPTION_REQUIRED)));
            prod.setPrice(cur.getDouble(cur.getColumnIndex(PRODUCT_PRICE)));
            prod.setUnit(cur.getString(cur.getColumnIndex(PRODUCT_UNIT)));
            prod.setPacking(cur.getString(cur.getColumnIndex(PRODUCT_PACKING)));
            prod.setQtyPerPacking(cur.getInt(cur.getColumnIndex(PRODUCT_QTY_PER_PACKING)));
            prod.setPhoto(cur.getString(cur.getColumnIndex(PRODUCT_PHOTO)));
            prod.setCreatedAt(cur.getString(cur.getColumnIndex(CREATED_AT)));
            prod.setUpdatedAt(cur.getString(cur.getColumnIndex(UPDATED_AT)));
            prod.setDeletedAt(cur.getString(cur.getColumnIndex(DELETED_AT)));
            cur.moveToNext();
        }
        cur.close();
        db.close();
        return prod;
    }

    public int getProductServerIdById(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "Select * from " + TBL_PRODUCTS + " where id='" + id + "'";

        Cursor cur = db.rawQuery(sql, null);
        cur.moveToFirst();
        int pID = 0;
        while (!cur.isAfterLast()) {
            pID = cur.getInt(cur.getColumnIndex(PRODUCT_ID));
            cur.moveToNext();
        }
        cur.close();
        db.close();
        return pID;

    }

    /* Create a record for "products" table here */
    public boolean saveProduct(Product product, String request) {
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
        values.put(PRODUCT_SKU, product.getSku());
        values.put(PRODUCT_UNIT, product.getUnit());
        values.put(PRODUCT_PACKING, product.getPacking());
        values.put(PRODUCT_QTY_PER_PACKING, product.getQtyPerPacking());
        values.put(CREATED_AT, product.getCreatedAt());
        values.put(UPDATED_AT, product.getUpdatedAt());
        values.put(DELETED_AT, product.getDeletedAt());

        long rowID = 0;

        if (request.equals("insert")) {
            rowID = db.insert(TBL_PRODUCTS, null, values);

        } else if (request.equals("update")) {
            rowID = db.update(TBL_PRODUCTS, values, PRODUCT_ID + "=" + product.getProductId(), null);
        }
        db.close();
        return rowID > 0;
    }

    /**
     * @param product
     */
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

        values.put(CREATED_AT, product.getCreatedAt());
        values.put(UPDATED_AT, product.getUpdatedAt());

        long rowID = db.update(TBL_PRODUCTS, values, PRODUCT_ID + "=" + product.getId(), null);
        db.close();
        return rowID > 0;
    }

    //////////////////////////////GET METHODS/////////////////////////////
    public ArrayList<HashMap<String, String>> getPatientRecord(int patientID) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select * FROM " + TBL_PATIENT_RECORDS + " WHERE " + RECORDS_PATIENT_ID + " = " + patientID + " ORDER BY " + RECORDS_DATE + " DESC";
        Cursor cur = db.rawQuery(sql, null);
        ArrayList<HashMap<String, String>> arrayOfRecords = new ArrayList();
        HashMap<String, String> map;

        while (cur.moveToNext()) {
            map = new HashMap();
            map.put(RECORDS_ID, String.valueOf(cur.getInt(cur.getColumnIndex(RECORDS_ID))));
            map.put(RECORDS_COMPLAINT, cur.getString(cur.getColumnIndex(RECORDS_COMPLAINT)));
            map.put(RECORDS_FINDINGS, cur.getString(cur.getColumnIndex(RECORDS_FINDINGS)));
            map.put(RECORDS_DATE, cur.getString(cur.getColumnIndex(RECORDS_DATE)));
            map.put(RECORDS_DOCTOR_NAME, cur.getString(cur.getColumnIndex(RECORDS_DOCTOR_NAME)));
            map.put(RECORDS_DOCTOR_ID, String.valueOf(cur.getInt(cur.getColumnIndex(RECORDS_DOCTOR_ID))));
            arrayOfRecords.add(map);
        }
        cur.close();
        db.close();

        return arrayOfRecords;
    }

    public PatientRecord getPatientRecordByRecordID(int recordID, int userID) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_PATIENT_RECORDS + " WHERE " + RECORDS_PATIENT_ID + " = " + userID + " AND " + RECORDS_ID + " = " + recordID;
        Cursor cur = db.rawQuery(sql, null);
        cur.moveToFirst();
        PatientRecord record = new PatientRecord();

        if (cur.getCount() > 0) {
            record.setDoctorID(cur.getInt(cur.getColumnIndex(RECORDS_DOCTOR_ID)));
            record.setDoctorName(cur.getString(cur.getColumnIndex(RECORDS_DOCTOR_NAME)));
            record.setPatientID(cur.getInt(cur.getColumnIndex(RECORDS_PATIENT_ID)));
            record.setComplaints(cur.getString(cur.getColumnIndex(RECORDS_COMPLAINT)));
            record.setFindings(cur.getString(cur.getColumnIndex(RECORDS_FINDINGS)));
            record.setDate(cur.getString(cur.getColumnIndex(RECORDS_DATE)));
        }

        return record;
    }

    public ArrayList<HashMap<String, String>> getTreatmentRecord(int recordID) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_TREATMENTS + " WHERE " + TREATMENTS_RECORD_ID + " = " + recordID;
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<HashMap<String, String>> arrayOfTreatments = new ArrayList();
        HashMap<String, String> map;

        while (cursor.moveToNext()) {
            map = new HashMap();
            map.put("medicine_name", cursor.getString(cursor.getColumnIndex(TREATMENTS_MEDICINE_NAME)));
            map.put("generic_name", cursor.getString(cursor.getColumnIndex(TREATMENTS_GENERIC_NAME)));
            map.put("quantity", cursor.getString(cursor.getColumnIndex(TREATMENTS_QUANITY)));
            map.put("prescription", cursor.getString(cursor.getColumnIndex(TREATMENTS_PRESCRIPTION)));
            arrayOfTreatments.add(map);
        }
        cursor.close();
        db.close();

        return arrayOfTreatments;
    }

    public ArrayList<HashMap<String, String>> getTreatmentByRecordID(int recordID) {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<HashMap<String, String>> treatments = new ArrayList();
        String sql = "SELECT * FROM " + TBL_TREATMENTS + " WHERE " + TREATMENTS_RECORD_ID + " = " + recordID;
        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap();
            map.put(TREATMENTS_ID, String.valueOf(cur.getInt(cur.getColumnIndex(TREATMENTS_ID))));
            map.put(TREATMENTS_MEDICINE_NAME, cur.getString(cur.getColumnIndex(TREATMENTS_MEDICINE_NAME)));
            map.put(TREATMENTS_GENERIC_NAME, cur.getString(cur.getColumnIndex(TREATMENTS_GENERIC_NAME)));
            map.put(TREATMENTS_QUANITY, cur.getString(cur.getColumnIndex(TREATMENTS_QUANITY)));
            map.put(TREATMENTS_PRESCRIPTION, cur.getString(cur.getColumnIndex(TREATMENTS_PRESCRIPTION)));
            treatments.add(map);
        }
        cur.close();
        db.close();

        return treatments;
    }

    public ArrayList<HashMap<String, String>> getAllDoctors() {
        ArrayList<HashMap<String, String>> doctors = new ArrayList();

        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT d.*, s.name FROM " + TBL_DOCTORS + " as d inner join " + TBL_SUB_SPECIALTIES + " as ss on d.sub_specialty_id = ss.sub_specialty_id inner join " + TBL_SPECIALTIES + " as s on ss.specialty_id = s.specialty_id";
        Cursor cur = db.rawQuery(sql, null);

        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            HashMap<String, String> map = new HashMap();
            map.put(DOC_ID, cur.getString(cur.getColumnIndex(DOC_ID)));
            map.put(DOC_DOC_ID, cur.getString(cur.getColumnIndex(DOC_DOC_ID)));
            map.put(DOC_FNAME, cur.getString(cur.getColumnIndex(DOC_FNAME)));
            map.put(DOC_LNAME, cur.getString(cur.getColumnIndex(DOC_LNAME)));
            map.put(DOC_FULLNAME, "" + cur.getString(cur.getColumnIndex(DOC_FNAME)) + " " + cur.getString(cur.getColumnIndex(DOC_LNAME)));
            map.put(DOC_SPECIALTY_NAME, cur.getString(cur.getColumnIndex(DOC_SPECIALTY_NAME)));
            map.put(DOC_MNAME, cur.getString(cur.getColumnIndex(DOC_MNAME)));
            map.put(DOC_SUB_SPECIALTY_ID, cur.getString(cur.getColumnIndex(DOC_SUB_SPECIALTY_ID)));
            map.put(DOC_PHOTO, String.valueOf(cur.getInt(cur.getColumnIndex(DOC_PHOTO))));
            doctors.add(map);

            cur.moveToNext();
        }

        cur.close();
        db.close();

        return doctors;
    }

    public Doctor getDoctorByID(int doctorID) {
        SQLiteDatabase db = getWritableDatabase();
        String sqlgetDoctorByID = "SELECT d.*, s.name , ss.name as sub_name FROM " + TBL_DOCTORS + " as d inner join " + TBL_SUB_SPECIALTIES + " as ss on d.sub_specialty_id = ss.sub_specialty_id inner join " + TBL_SPECIALTIES + " as s on ss.specialty_id = s.specialty_id where d.id = " + doctorID;
        Cursor cur = db.rawQuery(sqlgetDoctorByID, null);
        cur.moveToFirst();
        Doctor doctor = new Doctor();

        if (cur.getCount() > 0) {
            doctor.setLname(cur.getString(cur.getColumnIndex(DOC_LNAME)));
            doctor.setMname(cur.getString(cur.getColumnIndex(DOC_MNAME)));
            doctor.setFname(cur.getString(cur.getColumnIndex(DOC_FNAME)));
            doctor.setPrc_no(cur.getInt(cur.getColumnIndex(DOC_PRC_NO)));
            doctor.setSpecialty(cur.getString(cur.getColumnIndex(SPECIALTY_NAME)));
            doctor.setSub_specialty(cur.getString(cur.getColumnIndex("sub_name")));
            doctor.setSub_specialty_id(cur.getInt(cur.getColumnIndex(DOC_SUB_SPECIALTY_ID)));
            doctor.setPhoto(cur.getString(cur.getColumnIndex(DOC_PHOTO)));
            doctor.setAffiliation(cur.getString(cur.getColumnIndex(DOC_AFFILIATIONS)));
            doctor.setCreated_at(cur.getString(cur.getColumnIndex(CREATED_AT)));
            doctor.setUpdated_at(cur.getString(cur.getColumnIndex(UPDATED_AT)));
            doctor.setDeleted_at(cur.getString(cur.getColumnIndex(DELETED_AT)));
        }
        cur.close();
        db.close();

        return doctor;
    }

    public Basket getBasket(int productId) {
        Basket basket = new Basket();

        String sql = "Select * from " + TBL_BASKETS + " where product_id=" + productId + " and patient_id=" + this.getCurrentLoggedInPatient().getServerID();
        System.out.println("\ngetBasket: " + sql);
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);

        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            basket.setId(cur.getInt(0));
            basket.setBasketId(cur.getInt(cur.getColumnIndex(SERVER_BASKET_ID)));
            basket.setPatienId(cur.getInt(cur.getColumnIndex(BASKET_PATIENT_ID)));
            basket.setProductId(cur.getInt(cur.getColumnIndex(BASKET_PRODUCT_ID)));
            basket.setQuantity(cur.getDouble(cur.getColumnIndex(BASKET_QUANTITY)));
            basket.setCreatedAt(cur.getString(cur.getColumnIndex(CREATED_AT)));
            basket.setUpdatedAt(cur.getString(cur.getColumnIndex(UPDATED_AT)));
            cur.moveToNext();
        }
        cur.close();
        db.close();
        return basket;
    }

    public ArrayList<HashMap<String, String>> getAllBasketItems() {
        ArrayList<HashMap<String, String>> items = new ArrayList();

        String sql = "Select b.id, b.basket_id, p.product_id, p.name, p.price, p.sku, b.quantity, p.unit from " + TBL_BASKETS + " as b " +
                "inner join " + TBL_PRODUCTS + " as p on p.product_id = b.product_id where b.patient_id=" + this.getCurrentLoggedInPatient().getServerID() + "";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);

        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            HashMap<String, String> map = new HashMap();
            map.put(BASKET_ID, cur.getString(cur.getColumnIndex(BASKET_ID)));
            map.put(SERVER_PRODUCT_ID, cur.getString(cur.getColumnIndex(SERVER_PRODUCT_ID)));
            map.put(SERVER_BASKET_ID, cur.getString(cur.getColumnIndex(SERVER_BASKET_ID)));
            map.put(PRODUCT_NAME, cur.getString(cur.getColumnIndex(PRODUCT_NAME)));
            map.put(PRODUCT_PRICE, String.valueOf(cur.getDouble(cur.getColumnIndex(PRODUCT_PRICE))));
            map.put(BASKET_QUANTITY, String.valueOf(cur.getInt(cur.getColumnIndex(BASKET_QUANTITY))));
            map.put(PRODUCT_UNIT, cur.getString(cur.getColumnIndex(PRODUCT_UNIT)));
            map.put(PRODUCT_SKU, cur.getString(cur.getColumnIndex(PRODUCT_SKU)));
            items.add(map);
            cur.moveToNext();
        }

        cur.close();
        db.close();
        return items;
    }

    public Patient getCurrentLoggedInPatient() {
        Patient patient = this.getloginPatient(HomeTileActivity.getUname());
        return patient;
    }

    public ArrayList<String> getUploadedPrescriptionsByUserID(int patientID) {
        ArrayList<String> list_of_filename = new ArrayList();
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_PATIENT_PRESCRIPTIONS + " WHERE " + PRESCRIPTIONS_PATIENT_ID + " = " + patientID;
        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            list_of_filename.add(cur.getString(cur.getColumnIndex(PRESCRIPTIONS_FILENAME)));
        }
        cur.close();
        db.close();

        return list_of_filename;
    }

//    public ArrayList<HashMap<String, String>> getAllDoctorClinic() {
//        ArrayList<HashMap<String, String>> listOfDoctorClinic = new ArrayList();
//        SQLiteDatabase db = getWritableDatabase();
//
//        String sql = "";
//    }
    /////////////////////////END OF GET METHODS/////////////////////////////////

    /////////////////////////UPDATE METHODS////////////////////////////////////
    public boolean updateBasket(Basket basket) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String datenow = dateFormat.format(date);

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(BASKET_QUANTITY, basket.getQuantity());
        values.put(UPDATED_AT, datenow);

        long row = db.update(TBL_BASKETS, values, SERVER_BASKET_ID + "=" + basket.getBasketId(), null);
        db.close();
        return row > 0;
    }
    ////////////////////////END OF UPDATE METHODS//////////////////////////////////

    /////////////////////////DELETE METHODS///////////////////////////////////////
    public boolean deleteBasketItem(int basketId) {
        SQLiteDatabase db = getWritableDatabase();
        long row = db.delete(TBL_BASKETS, SERVER_BASKET_ID + "=" + basketId, null);
        db.close();
        return row > 0;

    }
    ////////////////////////////END OF DELETE METHODS///////////////////////////

    public long deletePatientRecord(int recordID) {
        SQLiteDatabase db = getWritableDatabase();
        long deleted_record_ID = db.delete(TBL_PATIENT_RECORDS, RECORDS_ID + " = " + recordID, null);
        db.close();

        return deleted_record_ID;
    }

    public long deleteTreatmentByRecordID(long recordID) {
        SQLiteDatabase db = getWritableDatabase();
        long deleted_treatment_ID = db.delete(TBL_TREATMENTS, TREATMENTS_RECORD_ID + " = " + recordID, null);
        db.close();

        return deleted_treatment_ID;
    }


    /* PROMODISCOUNTS TABLE */
    public boolean savePromoDiscount(PromoDiscount promoDiscount, String type) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SERVER_PROMO_DISCOUNTS_ID, promoDiscount.getPromoDiscountId());
        values.put(PROMO_D_NAME, promoDiscount.getName());
        values.put(PROMO_D_LESS, promoDiscount.getLess());
        values.put(PROMO_D_PRODUCT_ID, promoDiscount.getProductId());
        values.put(PROMO_D_QUANTITY_REQUIRED, promoDiscount.getQuantityRequired());
        values.put(PROMO_D_START_DATE, promoDiscount.getStartDate());
        values.put(PROMO_D_END_DATE, promoDiscount.getEndDate());
        values.put(PROMO_D_TYPE, promoDiscount.getType());
        values.put(CREATED_AT, promoDiscount.getCreatedAt());
        values.put(UPDATED_AT, promoDiscount.getUpdatedAt());
        values.put(DELETED_AT, promoDiscount.getDeletedAt());

        long row;

        if (type.equals("insert")) {
            row = db.insert(TBL_PROMO_DISCOUNTS, null, values);
        } else {
            row = db.update(TBL_PROMO_DISCOUNTS, values, PROMO_DISCOUNTS_ID + "=" + promoDiscount.getPromoDiscountId(), null);
        }
        db.close();
        return row > 0;
    }
}