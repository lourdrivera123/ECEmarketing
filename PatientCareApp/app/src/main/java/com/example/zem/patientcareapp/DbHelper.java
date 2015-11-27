package com.example.zem.patientcareapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.zem.patientcareapp.Fragment.AccountFragment;
import com.example.zem.patientcareapp.GetterSetter.Basket;
import com.example.zem.patientcareapp.GetterSetter.Clinic;
import com.example.zem.patientcareapp.GetterSetter.ClinicDoctor;
import com.example.zem.patientcareapp.GetterSetter.Consultation;
import com.example.zem.patientcareapp.GetterSetter.Doctor;
import com.example.zem.patientcareapp.GetterSetter.Dosage;
import com.example.zem.patientcareapp.GetterSetter.FreeProducts;
import com.example.zem.patientcareapp.GetterSetter.Medicine;
import com.example.zem.patientcareapp.GetterSetter.Messages;
import com.example.zem.patientcareapp.GetterSetter.Patient;
import com.example.zem.patientcareapp.GetterSetter.PatientRecord;
import com.example.zem.patientcareapp.GetterSetter.Product;
import com.example.zem.patientcareapp.GetterSetter.ProductCategory;
import com.example.zem.patientcareapp.GetterSetter.ProductSubCategory;
import com.example.zem.patientcareapp.GetterSetter.Settings;
import com.example.zem.patientcareapp.GetterSetter.Specialty;
import com.example.zem.patientcareapp.GetterSetter.SubSpecialty;

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
    Helpers helper = new Helpers();

    //PATIENTS_TABLE
    public static final String TBL_PATIENTS = "patients",
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
            PTNT_OPTIONAL_ADDRESS = "optional_address",
            PTNT_STREET = "address_street",
            PTNT_BRGY_ID = "address_barangay_id",
            PTNT_BARANGAY = "address_barangay",
            PTNT_CITY = "address_city_municipality",
            PTNT_PROVINCE = "address_province",
            PTNT_REGION = "address_region",
            PTNT_TEL_NO = "tel_no",
            PTNT_MOBILE_NO = "mobile_no",
            PTNT_EMAIL = "email_address",
            PTNT_PHOTO = "photo",
            PTNT_REFERRAL_ID = "referral_id",
            PTNT_REFERRED_BY_USER = "referred_byUser",
            PTNT_REFERRED_BY_DOCTOR = "referred_byDoctor";

    //Updates Table
    public static final String TBL_UPDATES = "updates",
            UPDATE_TBL_NAME = "tbl_name",
            UPDATE_TIMESTAMP = "timestamp",
            UPDATE_SEEN = "seen";

    //DOCTORS_TABLE
    public static final String TBL_DOCTORS = "doctors",
            DOC_DOC_ID = "doc_id",
            DOC_LNAME = "lname",
            DOC_MNAME = "mname",
            DOC_FNAME = "fname",
            DOC_PRC_NO = "prc_no",
            DOC_SUB_SPECIALTY_ID = "sub_specialty_id",
            DOC_AFFILIATIONS = "affiliations",
            DOC_EMAIL = "email",
            DOC_REFERRAL_ID = "referral_id";

    //SPECIALTIES_TABLE
    public static final String TBL_SPECIALTIES = "specialties",
            SERVER_SPECIALTY_ID = "specialty_id",
            SPECIALTY_NAME = "name";

    //SPECIALTIES_TABLE
    public static final String TBL_SUB_SPECIALTIES = "sub_specialties",
            SERVER_SUB_SPECIALTY_ID = "sub_specialty_id",
            SUB_SPECIALTY_FOREIGN_ID = "specialty_id",
            SUB_SPECIALTY_NAME = "name";

    // PRODUCT_CATEGORIES TABLE
    public static final String PROD_CAT_NAME = "name",
            TBL_PRODUCT_CATEGORIES = "product_categories",
            SERVER_PRODUCT_CATEGORY_ID = "product_category_id";

    // PRODUCT_SUBCATEGORIES TABLE
    public static final String TBL_PRODUCT_SUBCATEGORIES = "product_subcategories",
            PROD_SUBCAT_NAME = "name",
            PROD_SUBCAT_CATEGORY_ID = "category_id",
            SERVER_PRODUCT_SUBCATEGORY_ID = "product_subcategory_id";

    // PRODUCTS TABLE
    public static final String TBL_PRODUCTS = "products",
            PRODUCT_SUBCATEGORY_ID = "subcategory_id",
            SERVER_PRODUCT_ID = "product_id",
            PRODUCT_NAME = "name",
            PRODUCT_GENERIC_NAME = "generic_name",
            PRODUCT_DESCRIPTION = "description",
            PRODUCT_PRESCRIPTION_REQUIRED = "prescription_required",
            PRODUCT_PRICE = "price",
            PRODUCT_UNIT = "unit",
            PRODUCT_PACKING = "packing",
            PRODUCT_QTY_PER_PACKING = "qty_per_packing",
            PRODUCT_SKU = "sku",
            PRODUCT_CRITICAL_STOCK = "critical_stock";

    //DOSAGE_FORMAT_AND_STRENGTH TABLE
    public static final String TBL_DOSAGE = "dosage_format_and_strength",
            SERVER_DOSAGE_ID = "dosage_id",
            DOSAGE_PROD_ID = "product_id",
            DOSAGE_NAME = "name";

    // BASKET TABLE
    public static final String TBL_BASKETS = "baskets",
            SERVER_BASKET_ID = "basket_id",
            BASKET_PRODUCT_ID = "product_id",
            BASKET_QUANTITY = "quantity",
            BASKET_PRESCRIPTION_ID = "prescription_id",
            BASKET_IS_APPROVED = "is_approved";

    //PATIENT_RECORDS TABLE
    public static final String TBL_PATIENT_RECORDS = "patient_records",
            SERVER_RECORDS_ID = "record_id",
            RECORDS_DOCTOR_ID = "doctor_id",
            RECORDS_CLINIC_ID = "clinic_id",
            RECORDS_DOCTOR_NAME = "doctor_name",
            RECORDS_CLINIC_NAME = "clinic_name",
            RECORDS_COMPLAINT = "complaints",
            RECORDS_FINDINGS = "findings",
            RECORDS_DATE = "record_date";

    //PATIENT_TREATMENTS TABLE
    public static final String TBL_PATIENT_TREATMENTS = "patient_treatments",
            SERVER_TREATMENTS_ID = "treatments_id",
            TREATMENTS_PATIENT_RECORDS_ID = "patient_records_id",
            TREATMENTS_MEDICINE_NAME = "medicine_name",
            TREATMENTS_GENERIC_NAME = "generic_name",
            TREATMENTS_DOSAGE = "dosage";

    // CLINICS TABLE
    public static final String TBL_CLINICS = "clinics",
            CLINIC_NAME = "name",
            CLINIC_CONTACT_NO = "contact_no",
            CLINIC_UNIT_NO = "unit_floor_room_no",
            CLINIC_BUILDING = "building",
            CLINIC_LOT_NO = "lot_no",
            CLINIC_BLOCK_NO = "block_no",
            CLINIC_PHASE_NO = "phase_no",
            CLINIC_HOUSE_NO = "address_house_no",
            CLINIC_STREET = "address_street",
            CLINIC_BARANGAY = "address_barangay",
            CLINIC_CITY = "address_city_municipality",
            CLINIC_PROVINCE = "address_province",
            CLINIC_REGION = "address_region",
            CLINIC_ZIP = "address_zip",
            SERVER_CLINICS_ID = "clinics_id";

    // CLINIC_DOCTOR TABLE
    public static final String TBL_CLINIC_DOCTOR = "clinic_doctor",
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

    // PROMOS TABLE
    public static final String TBL_PROMO = "promo",
            SERVER_PROMO_ID = "promo_id",
            PROMO_NAME = "name",
            PROMO_START_DATE = "start_date",
            PROMO_END_DATE = "end_date",
            PROMO_CREATED_AT = "created_at",
            PROMO_UPDATED_AT = "updated_at",
            PROMO_DELETED_AT = "deleted_at";

    // DISCOUNTS or FREE PRODUCTS PROMOTION TABLE
    public static final String TBL_DISCOUNTS_FREE_PRODUCTS = "discounts_free_products",
            SERVER_DFP_ID = "dfp_id",
            DFP_PROMO_ID = "promo_id",
            DFP_PRODUCT_ID = "product_id",
            DFP_TYPE = "type",                          // type = 1 or 0 , 1 for Free Products and 0 for Discount
            DFP_QUANTITY_REQUIRED = "quantity_required",
            DFP_LESS = "less";

    // FREE PRODUCTS TABLE
    public static final String TBL_FREE_PRODUCTS = "free_products",
            SERVER_FP_ID = "free_products_id",
            FP_DFP_ID = "dfp_id",               // foreign key ID from discounts_free_products table
            FP_PRODUCT_ID = "product_id",       // the ID of the free item
            FP_QTY_FREE = "quantity_free",      // how many items are for free
            FP_CREATED_AT = "created_at",
            FP_UPDATED_AT = "updated_at",
            FP_DELETED_AT = "deleted_at";

    // UPLOADS ON PRESCRIPTIONS
    public static final String TBL_PATIENT_PRESCRIPTIONS = "patient_prescriptions",
            PRESCRIPTIONS_SERVER_ID = "prescriptions_id",
            PRESCRIPTIONS_FILENAME = "filename",
            PRESCRIPTIONS_APPROVED = "is_approved";

    //CONSULTATION
    public static final String TBL_PATIENT_CONSULTATIONS = "consultations",
            CONSULT_SERVER_ID = "consultation_id",
            CONSULT_DOCTOR_ID = "doctor_id",
            CONSULT_CLINIC_ID = "clinic_id",
            CONSULT_DATE = "date",
            CONSULT_TIME = "time",
            CONSULT_IS_ALARMED = "is_alarm",
            CONSULT_ALARMED_TIME = "alarm_time",
            CONSULT_IS_APPROVED = "is_approved",
            CONSULT_COMMENT_DOCTOR = "comment_doctor",
            CONSULT_PTNT_IS_APPROVED = "patient_is_approved",
            CONSULT_COMMENT_PTNT = "comment_patient";

    //OVERLAY
    public static final String TBL_OVERLAYS = "overlays",
            OVERLAY_TITLE = "title";

    //ORDERS TABLE
    public static final String TBL_ORDERS = "orders",
            SERVER_ORDERS_ID = "orders_id",
            ORDERS_RECIPIENT_NAME = "recipient_name",
            ORDERS_RECIPIENT_ADDRESS = "recipient_address",
            ORDERS_RECIPIENT_NUMBER = "recipient_contactNumber",
            ORDERS_DELIVERY_SCHED = "delivery_sched",
            ORDERS_ECE_BRANCH = "branch_id",
            ORDERS_MODE_OF_DELIVERY = "modeOfDelivery",
            ORDERS_STATUS = "status",
            ORDERS_CREATED_AT = "created_at";

    //ORDERS TABLE
    public static final String TBL_ORDER_DETAILS = "order_details",
            SERVER_ORDER_DETAILS_ID = "order_details_id",
            ORDER_DETAILS_ORDER_ID = "order_id",
            ORDER_DETAILS_PRODUCT_ID = "product_id",
            ORDER_DETAILS_PRESCRIPTION_ID = "prescription_id",
            ORDER_DETAILS_QUANTITY = "quantity",
            ORDER_DETAILS_TYPE = "type",
            ORDER_DETAILS_QTY_FULFILLED = "qty_fulfilled";

    //BRANCHES TABLE
    public static final String TBL_BRANCHES = "branches",
            SERVER_BRANCHES_ID = "branches_id",
            BRANCHES_NAME = "name",
            BRANCHES_ADDITIONAL_ADDRESS = "additional_address",
            BRANCHES_BRGY_ID = "barangay_id",
            BRANCHES_BARANGAY = "address_barangay",
            BRANCHES_CITY = "address_city_municipality",
            BRANCHES_PROVINCE = "address_province",
            BRANCHES_REGION = "address_region",
            BRANCHES_STATUS = "status";

    //REFERRAL_SETTINGS TABLES
    public static final String TBL_SETTINGS = "settings",
            SETTINGS_SERVER_ID = "serverID",
            SETTINGS_POINTS = "points",
            SETTINGS_POINTS_TO_PESO = "points_to_peso",
            SETTINGS_LVL_LIMIT = "level_limit",
            SETTINGS_REFERRAL_COMM = "referral_commission",
            SETTINGS_COMM_VARIATION = "commission_variation",
            SETTINGS_DELIVERY_CHARGE = "delivery_charge",
            SETTINGS_DELIVERY_MINIMUM = "delivery_minimum";

    //MESSAGES
    public static final String TBl_MSGS = "messages",
            MSGS_SERVER_ID = "serverID",
            MSGS_SUBJECT = "subject",
            MSGS_CONTENT = "content";

    //FAVORITES
    public static final String TBL_FAVORITES = "favorites",
            FAVE_PRODUCT_ID = "product_id",
            FAVE_USER_ID = "user_id";


    public static final String CREATED_AT = "created_at", DELETED_AT = "deleted_at", UPDATED_AT = "updated_at", AI_ID = "id",
            PATIENT_ID = "patient_id", IS_READ = "isRead";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL to create table "doctors"
        String sql_create_tbl_doctors = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TBL_DOCTORS, AI_ID, DOC_DOC_ID, DOC_LNAME, DOC_MNAME, DOC_FNAME, DOC_PRC_NO, DOC_SUB_SPECIALTY_ID, DOC_AFFILIATIONS, DOC_EMAIL, DOC_REFERRAL_ID, CREATED_AT, UPDATED_AT, DELETED_AT);

        String sql_create_tbl_specialties = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TBL_SPECIALTIES, AI_ID, SERVER_SPECIALTY_ID, SPECIALTY_NAME, CREATED_AT, UPDATED_AT, DELETED_AT);

        String sql_create_tbl_sub_specialties = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TBL_SUB_SPECIALTIES, AI_ID, SERVER_SUB_SPECIALTY_ID, SUB_SPECIALTY_FOREIGN_ID, SUB_SPECIALTY_NAME, CREATED_AT, UPDATED_AT, DELETED_AT);

        // SQL to create table "tbl_updates"
        String sql_create_tbl_updates = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s INTEGER)",
                TBL_UPDATES, AI_ID, UPDATE_TBL_NAME, UPDATE_TIMESTAMP, UPDATE_SEEN);

        // SQL to create table "patients"
        String sql_create_tbl_patients = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TBL_PATIENTS, AI_ID, PATIENT_ID, PTNT_FNAME, PTNT_MNAME, PTNT_LNAME, PTNT_USERNAME, PTNT_PASSWORD, PTNT_OCCUPATION, PTNT_BIRTHDATE, PTNT_SEX, PTNT_CIVIL_STATUS, PTNT_HEIGHT, PTNT_WEIGHT, PTNT_OPTIONAL_ADDRESS, PTNT_STREET, PTNT_BRGY_ID, PTNT_BARANGAY, PTNT_CITY, PTNT_PROVINCE, PTNT_REGION, PTNT_TEL_NO, PTNT_MOBILE_NO, PTNT_EMAIL, PTNT_PHOTO, PTNT_REFERRAL_ID, PTNT_REFERRED_BY_USER, PTNT_REFERRED_BY_DOCTOR, CREATED_AT, UPDATED_AT, DELETED_AT);

        // SQL to create table "product_categories"
        String sql_create_tbl_product_categories = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, %s TEXT, %s  TEXT , %s  TEXT , %s TEXT  )",
                TBL_PRODUCT_CATEGORIES, AI_ID, SERVER_PRODUCT_CATEGORY_ID, PROD_CAT_NAME, CREATED_AT, UPDATED_AT, DELETED_AT);

        // SQL to create table "product_subcategories"
        String sql_create_tbl_product_subcategories = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, %s TEXT, %s INTEGER, %s  TEXT , %s  TEXT , %s  TEXT  )",
                TBL_PRODUCT_SUBCATEGORIES, AI_ID, SERVER_PRODUCT_SUBCATEGORY_ID, PROD_SUBCAT_NAME, PROD_SUBCAT_CATEGORY_ID, CREATED_AT, UPDATED_AT, DELETED_AT);

        // SQL to create table "products"
        String sql_create_tbl_products = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, %s TEXT, %s TEXT,  %s TEXT, %s TEXT , %s INTEGER, %s DOUBLE, %s TEXT, %s TEXT, %s INTEGER, %s  TEXT , %s  INTEGER , %s  TEXT,  %s  TEXT , %s  TEXT  )",
                TBL_PRODUCTS, AI_ID, SERVER_PRODUCT_ID, PRODUCT_SUBCATEGORY_ID, PRODUCT_NAME, PRODUCT_GENERIC_NAME, PRODUCT_DESCRIPTION, PRODUCT_PRESCRIPTION_REQUIRED, PRODUCT_PRICE, PRODUCT_UNIT, PRODUCT_PACKING, PRODUCT_QTY_PER_PACKING, PRODUCT_SKU, PRODUCT_CRITICAL_STOCK, CREATED_AT, UPDATED_AT, DELETED_AT);

        // SQL TO CREATE TABLE "TBL_DOSAGE"
        String sql_create_dosage_table = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
                TBL_DOSAGE, AI_ID, SERVER_DOSAGE_ID, DOSAGE_PROD_ID, DOSAGE_NAME, CREATED_AT, UPDATED_AT);

        // SQL to create table "baskets"
        String sql_create_tbl_baskets = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER UNIQUE, %s INTEGER, %s INTEGER, %s DOUBLE, %s INTEGER, %s INTEGER, %s  TEXT , %s  TEXT , %s  TEXT  )",
                TBL_BASKETS, AI_ID, SERVER_BASKET_ID, PATIENT_ID, BASKET_PRODUCT_ID, BASKET_QUANTITY, BASKET_PRESCRIPTION_ID, BASKET_IS_APPROVED, CREATED_AT, UPDATED_AT, DELETED_AT);

        // SQL to create "patient_records"
        String sql_create_patient_records = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT )",
                TBL_PATIENT_RECORDS, AI_ID, SERVER_RECORDS_ID, RECORDS_DOCTOR_ID, RECORDS_CLINIC_ID, RECORDS_DOCTOR_NAME, RECORDS_CLINIC_NAME, PATIENT_ID, RECORDS_COMPLAINT, RECORDS_FINDINGS, RECORDS_DATE, CREATED_AT, UPDATED_AT, DELETED_AT);

        //SQL to create "patient_treatments"
        String sql_patient_treatments = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TBL_PATIENT_TREATMENTS, AI_ID, SERVER_TREATMENTS_ID, TREATMENTS_PATIENT_RECORDS_ID, TREATMENTS_MEDICINE_NAME, TREATMENTS_GENERIC_NAME, TREATMENTS_DOSAGE, CREATED_AT, UPDATED_AT, DELETED_AT);

        // SQL to create table "clinics"
        String sql_create_clinics_table = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s  TEXT , %s  TEXT , %s  TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT  )",
                TBL_CLINICS, AI_ID, SERVER_CLINICS_ID, CLINIC_NAME, CLINIC_CONTACT_NO, CLINIC_UNIT_NO, CLINIC_BUILDING, CLINIC_LOT_NO, CLINIC_BLOCK_NO, CLINIC_PHASE_NO, CLINIC_HOUSE_NO, CLINIC_STREET, CLINIC_BARANGAY, CLINIC_CITY, CLINIC_PROVINCE, CLINIC_REGION, CLINIC_ZIP, CREATED_AT, UPDATED_AT, DELETED_AT);

        // SQL to create table "clinic_doctor"
        String sql_create_clinic_doctor_table = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INT )",
                TBL_CLINIC_DOCTOR, AI_ID, CD_SERVER_ID, CD_CLINIC_ID, CD_DOCTOR_ID, CD_CLINIC_SCHED, CD_IS_ACTIVE);

        // SQL to create table "secretaries"
        String sql_create_secretaries_table = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s  TEXT , %s  TEXT , %s  TEXT  )",
                TBL_SECRETARIES, AI_ID, SERVER_SECRETARIES_ID, SEC_FNAME, SEC_MNAME, SEC_LNAME, SEC_ADDRESS_HOUSE_NO, SEC_ADDRESS_STREET, SEC_ADDRESS_BARANGAY, SEC_ADDRESS_CITY_MUNICIPALITY, SEC_ADDRESS_PROVINCE, SEC_ADDRESS_REGION, SEC_ADDRESS_ZIP, SEC_CELL_NO, SEC_TEL_NO, SEC_EMAIL, SEC_PHOTO, CREATED_AT, UPDATED_AT, DELETED_AT);

        // SQL to create table "clinic_secretary"
        String sql_create_clinic_secretary_table = String.format("CREATE TABLE %s ( %s INTEGER, %s INTEGER, %s INT )",
                TBL_CLINIC_SECRETARY, CS_CLINIC_ID, CS_SECRETARY_ID, CS_IS_ACTIVE);

        // SQL to create table "doctor_secretary"
        String sql_create_doctor_secretary_table = String.format("CREATE TABLE %s ( %s INTEGER, %s INTEGER, %s INT )",
                TBL_DOCTOR_SECRETARY, DS_DOCTOR_ID, DS_SECRETARY_ID, DS_IS_ACTIVE);

        // SQL to create table "promo"
        String sql_create_promo_table = String.format("CREATE TABLE %s( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT )",
                TBL_PROMO, AI_ID, SERVER_PROMO_ID, PROMO_NAME, PROMO_START_DATE, PROMO_END_DATE, PROMO_CREATED_AT, PROMO_UPDATED_AT, PROMO_DELETED_AT);

        // SQL to create table discounts_free_products
        String sql_create_discounts_products = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s DOUBLE, %s TEXT, %s TEXT, %s TEXT)",
                TBL_DISCOUNTS_FREE_PRODUCTS, AI_ID, SERVER_DFP_ID, DFP_PROMO_ID, DFP_PRODUCT_ID, DFP_TYPE, DFP_QUANTITY_REQUIRED, DFP_LESS, CREATED_AT, UPDATED_AT, DELETED_AT);

        // SQL to create tale "free_products"
        String sql_create_free_products_table = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
                TBL_FREE_PRODUCTS, AI_ID, SERVER_FP_ID, FP_DFP_ID, FP_PRODUCT_ID, FP_QTY_FREE, FP_CREATED_AT, FP_UPDATED_AT, FP_DELETED_AT);

        String sql_create_prescriptions_upload = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s TEXT, %s INTEGER, %s TEXT, %s TEXT)",
                TBL_PATIENT_PRESCRIPTIONS, AI_ID, PRESCRIPTIONS_SERVER_ID, PATIENT_ID, PRESCRIPTIONS_FILENAME, PRESCRIPTIONS_APPROVED, CREATED_AT, DELETED_AT);

        //SQL to create PATIENT CONSULTATIONS
        String sql_create_consultations = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s INTEGER, %s INTEGER, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
                TBL_PATIENT_CONSULTATIONS, AI_ID, CONSULT_SERVER_ID, PATIENT_ID, CONSULT_DOCTOR_ID, CONSULT_CLINIC_ID, CONSULT_DATE, CONSULT_TIME, CONSULT_IS_ALARMED, CONSULT_ALARMED_TIME, CONSULT_IS_APPROVED, IS_READ, CONSULT_COMMENT_DOCTOR, CONSULT_PTNT_IS_APPROVED, CONSULT_COMMENT_PTNT, CREATED_AT, UPDATED_AT);

        String sql_overlay = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER)",
                TBL_OVERLAYS, AI_ID, OVERLAY_TITLE, IS_READ);

        String settings = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s DOUBLE, %s DOUBLE, %s INTEGER, %s DOUBLE, %s DOUBLE, %s DOUBLE, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
                TBL_SETTINGS, AI_ID, SETTINGS_SERVER_ID, SETTINGS_POINTS, SETTINGS_POINTS_TO_PESO, SETTINGS_LVL_LIMIT, SETTINGS_REFERRAL_COMM, SETTINGS_COMM_VARIATION, SETTINGS_DELIVERY_CHARGE, SETTINGS_DELIVERY_MINIMUM, CREATED_AT, UPDATED_AT, DELETED_AT);

        String sql_create_orders = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT," +
                        " %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TBL_ORDERS, AI_ID, SERVER_ORDERS_ID, PATIENT_ID, ORDERS_RECIPIENT_NAME, ORDERS_RECIPIENT_ADDRESS,
                ORDERS_RECIPIENT_NUMBER, ORDERS_DELIVERY_SCHED, ORDERS_ECE_BRANCH, ORDERS_MODE_OF_DELIVERY, ORDERS_STATUS, CREATED_AT,
                UPDATED_AT, DELETED_AT);

        String sql_create_branches = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TBL_BRANCHES, AI_ID, SERVER_BRANCHES_ID, BRANCHES_NAME, BRANCHES_ADDITIONAL_ADDRESS, BRANCHES_BRGY_ID, BRANCHES_BARANGAY, BRANCHES_CITY, BRANCHES_PROVINCE, BRANCHES_REGION, BRANCHES_STATUS, CREATED_AT,
                UPDATED_AT, DELETED_AT);

        String sql_create_order_details = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
                TBL_ORDER_DETAILS, AI_ID, SERVER_ORDER_DETAILS_ID, ORDER_DETAILS_ORDER_ID, ORDER_DETAILS_PRODUCT_ID, ORDER_DETAILS_PRESCRIPTION_ID, ORDER_DETAILS_QUANTITY, ORDER_DETAILS_TYPE, ORDER_DETAILS_QTY_FULFILLED, CREATED_AT, UPDATED_AT, DELETED_AT);

        //sql to create messages table
        String sql_create_messages = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT)",
                TBl_MSGS, AI_ID, MSGS_SERVER_ID, PATIENT_ID, MSGS_SUBJECT, MSGS_CONTENT, IS_READ, CREATED_AT, UPDATED_AT, DELETED_AT);

        //sql to create "favorites"
        String sql_favorites = "CREATE TABLE favorites (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, product_id INTEGER, FOREIGN KEY(product_id) REFERENCES products(product_id));";

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
        db.execSQL(sql_create_clinics_table);
        db.execSQL(sql_create_clinic_doctor_table);
        db.execSQL(sql_create_secretaries_table);
        db.execSQL(sql_create_clinic_secretary_table);
        db.execSQL(sql_create_doctor_secretary_table);
        db.execSQL(sql_create_promo_table);
        db.execSQL(sql_create_discounts_products);
        db.execSQL(sql_create_free_products_table);
        db.execSQL(sql_create_prescriptions_upload);
        db.execSQL(sql_create_consultations);
        db.execSQL(sql_overlay);
        db.execSQL(sql_create_orders);
        db.execSQL(settings);
        db.execSQL(sql_create_branches);
        db.execSQL(sql_create_order_details);
        db.execSQL(sql_create_messages);
        db.execSQL(sql_patient_treatments);
        db.execSQL(sql_favorites);

        insertTableNamesToUpdates(TBL_DOCTORS, db);
        insertTableNamesToUpdates(TBL_SPECIALTIES, db);
        insertTableNamesToUpdates(TBL_SUB_SPECIALTIES, db);
        insertTableNamesToUpdates(TBL_PRODUCTS, db);
        insertTableNamesToUpdates(TBL_PRODUCT_CATEGORIES, db);
        insertTableNamesToUpdates(TBL_PRODUCT_SUBCATEGORIES, db);
        insertTableNamesToUpdates(TBL_BASKETS, db);
        insertTableNamesToUpdates(TBL_DOSAGE, db);
        insertTableNamesToUpdates(TBL_PATIENT_RECORDS, db);
        insertTableNamesToUpdates(TBL_CLINICS, db);
        insertTableNamesToUpdates(TBL_PATIENT_PRESCRIPTIONS, db);
        insertTableNamesToUpdates(TBL_SETTINGS, db);
        insertTableNamesToUpdates(TBL_BRANCHES, db);
        insertTableNamesToUpdates(TBL_ORDERS, db);
        insertTableNamesToUpdates(TBl_MSGS, db);
        insertTableNamesToUpdates(TBL_PATIENT_CONSULTATIONS, db);
        insertTableNamesToUpdates(TBL_PATIENT_RECORDS, db);
        insertTableNamesToUpdates(TBL_PATIENT_TREATMENTS, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + DB_NAME;
        db.execSQL(sql);
    }

    /////////////////////////////INSERT METHODS///////////////////////////////////////
    public boolean insertBasket(Basket basket) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String datenow = dateFormat.format(date);

        int patient_id = this.getCurrentLoggedInPatient().getServerID();

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SERVER_BASKET_ID, basket.getBasketId());
        values.put(PATIENT_ID, patient_id);
        values.put(BASKET_PRODUCT_ID, basket.getProductId());
        values.put(BASKET_QUANTITY, basket.getQuantity());
        values.put(BASKET_PRESCRIPTION_ID, basket.getPrescriptionId());
        values.put(BASKET_IS_APPROVED, basket.getIsApproved());
        values.put(CREATED_AT, datenow);

        long row = db.insert(TBL_BASKETS, null, values);
        db.close();
        return row > 0;
    }

    public boolean insertFaveProduct(int user_id, int product_id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues val = new ContentValues();

        val.put(FAVE_PRODUCT_ID, product_id);
        val.put(FAVE_USER_ID, user_id);

        long row = db.insert(TBL_FAVORITES, null, val);

        db.close();
        return row > 0;
    }

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

    public boolean insertDosage(Dosage dosage) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SERVER_DOSAGE_ID, dosage.getDosage_id());
        values.put(DOSAGE_PROD_ID, dosage.getProduct_id());
        values.put(DOSAGE_NAME, dosage.getName());

        long rowID = db.insert(TBL_DOSAGE, null, values);

        db.close();
        return rowID > 0;
    }

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

    public boolean insertUploadOnPrescription(Integer patientID, String filename, int serverID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PRESCRIPTIONS_SERVER_ID, serverID);
        values.put(PATIENT_ID, patientID);
        values.put(PRESCRIPTIONS_FILENAME, filename);
        values.put(PRESCRIPTIONS_APPROVED, 0);

        long rowID = db.insert(TBL_PATIENT_PRESCRIPTIONS, null, values);
        db.close();
        return rowID > 0;
    }
    //////////////////////////END OF INSERT METHODS///////////////////////////

    /////////////////////////SAVE METHODS (INSERT AND UPDATE)///////////////////////
    public boolean savePatient(JSONObject patient_json_object_mysql, Patient patient, String request) {
        int patient_id = 0;
        String created_at = "";

        if (request.equals("update")) {
            patient_id = patient.getServerID();
        } else {
            try {
                patient_id = patient_json_object_mysql.getInt("id");
                created_at = patient_json_object_mysql.getString("created_at");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PATIENT_ID, patient_id);
        values.put(PTNT_FNAME, patient.getFname());
        values.put(PTNT_MNAME, patient.getMname());
        values.put(PTNT_LNAME, patient.getLname());
        values.put(PTNT_USERNAME, patient.getUsername());
        values.put(PTNT_OCCUPATION, patient.getOccupation());
        values.put(PTNT_BIRTHDATE, patient.getBirthdate());
        values.put(PTNT_SEX, patient.getSex());
        values.put(PTNT_CIVIL_STATUS, patient.getCivil_status());
        values.put(PTNT_HEIGHT, patient.getHeight());
        values.put(PTNT_WEIGHT, patient.getWeight());
        values.put(PTNT_OPTIONAL_ADDRESS, patient.getOptional_address());
        values.put(PTNT_STREET, patient.getAddress_street());
        values.put(PTNT_BRGY_ID, patient.getBarangay_id());
        values.put(PTNT_BARANGAY, patient.getBarangay());
        values.put(PTNT_CITY, patient.getMunicipality());
        values.put(PTNT_PROVINCE, patient.getProvince());
        values.put(PTNT_REGION, patient.getRegion());
        values.put(PTNT_TEL_NO, patient.getTel_no());
        values.put(PTNT_MOBILE_NO, patient.getMobile_no());
        values.put(PTNT_EMAIL, patient.getEmail());
        values.put(PTNT_PHOTO, patient.getPhoto());
        values.put(PTNT_REFERRAL_ID, patient.getReferral_id());
        values.put(PTNT_REFERRED_BY_USER, patient.getReferred_byUser());
        values.put(PTNT_REFERRED_BY_DOCTOR, patient.getReferred_byDoctor());
        values.put(CREATED_AT, created_at);

        long rowID = 0;

        if (request.equals("insert")) {
            values.put(PTNT_PASSWORD, patient.getPassword());
            rowID = db.insert(TBL_PATIENTS, null, values);
        } else if (request.equals("update")) {
            if (AccountFragment.checkIfChangedPass > 0)
                values.put(PTNT_PASSWORD, patient.getPassword());

            rowID = db.update(TBL_PATIENTS, values, PATIENT_ID + "=" + patient_id, null);
        }

        db.close();

        return rowID > 0;
    }

    public boolean savePatientRecord(PatientRecord record, String request) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        long rowID = 0;

        values.put(SERVER_RECORDS_ID, record.getRecordID());
        values.put(PATIENT_ID, record.getPatientID());
        values.put(RECORDS_DOCTOR_ID, record.getDoctorID());
        values.put(RECORDS_CLINIC_ID, record.getClinicID());
        values.put(RECORDS_COMPLAINT, record.getComplaints());
        values.put(RECORDS_FINDINGS, record.getFindings());
        values.put(RECORDS_DATE, record.getDate());
        values.put(RECORDS_DOCTOR_NAME, record.getDoctorName());
        values.put(RECORDS_CLINIC_NAME, record.getClinicName());
        values.put(CREATED_AT, record.getCreated_at());
        values.put(UPDATED_AT, record.getUpdated_at());
        values.put(DELETED_AT, record.getDeleted_at());

        if (request.equals("insert")) {
            rowID = db.insert(TBL_PATIENT_RECORDS, null, values);
        }

        db.close();
        return rowID > 0;
    }

    public boolean savePatientTreatments(ArrayList<HashMap<String, String>> listOfTreatments, String request) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues val = new ContentValues();
        long rowID = 0;

        for (int x = 0; x < listOfTreatments.size(); x++) {
            val.put(SERVER_TREATMENTS_ID, listOfTreatments.get(x).get("treatmentts_id"));
            val.put(TREATMENTS_PATIENT_RECORDS_ID, listOfTreatments.get(x).get("patient_records_id"));
            val.put(TREATMENTS_MEDICINE_NAME, listOfTreatments.get(x).get("medicine_name"));
            val.put(TREATMENTS_GENERIC_NAME, listOfTreatments.get(x).get("generic_name"));
            val.put(TREATMENTS_DOSAGE, listOfTreatments.get(x).get("dosage"));

            if (request.equals("insert"))
                rowID = db.insert(TBL_PATIENT_TREATMENTS, null, val);
        }

        db.close();
        return rowID > 0;
    }

    public boolean saveClinic(Clinic clinic, String type) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SERVER_CLINICS_ID, clinic.getClinicsId());
        values.put(CLINIC_NAME, clinic.getName());
        values.put(CLINIC_CONTACT_NO, clinic.getContactNumber());
        values.put(CLINIC_UNIT_NO, clinic.getUnit_floor_room_no());
        values.put(CLINIC_BUILDING, clinic.getBuilding());
        values.put(CLINIC_LOT_NO, clinic.getLot_no());
        values.put(CLINIC_BLOCK_NO, clinic.getBlock_no());
        values.put(CLINIC_PHASE_NO, clinic.getPhase_no());
        values.put(CLINIC_HOUSE_NO, clinic.getAddress_house_no());
        values.put(CLINIC_STREET, clinic.getAddress_street());
        values.put(CLINIC_BARANGAY, clinic.getAddress_barangay());
        values.put(CLINIC_CITY, clinic.getAddress_city_municipality());
        values.put(CLINIC_PROVINCE, clinic.getAddress_province());
        values.put(CLINIC_REGION, clinic.getAddress_region());
        values.put(CLINIC_ZIP, clinic.getAddress_zip());
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

    public boolean savePrescription(JSONObject object) {
        long rowID = 0;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put(PRESCRIPTIONS_SERVER_ID, object.getInt("id"));
            values.put(PRESCRIPTIONS_APPROVED, object.getInt("is_approved"));
            values.put(PATIENT_ID, object.getInt("patient_id"));
            values.put(CREATED_AT, object.getString("created_at"));
            values.put(PRESCRIPTIONS_FILENAME, object.getString("filename"));

            rowID = db.insert(TBL_PATIENT_PRESCRIPTIONS, null, values);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.close();
        return rowID > 0;
    }

    public boolean saveBranches(JSONObject object) {
        long rowID = 0;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put(SERVER_BRANCHES_ID, object.getInt("id"));
            values.put(BRANCHES_NAME, object.getString(BRANCHES_NAME));
            values.put(BRANCHES_ADDITIONAL_ADDRESS, object.getString(BRANCHES_ADDITIONAL_ADDRESS));
            values.put(BRANCHES_BRGY_ID, object.getInt(BRANCHES_BRGY_ID));
            values.put(BRANCHES_BARANGAY, object.getString(BRANCHES_BARANGAY));
            values.put(BRANCHES_CITY, object.getString(BRANCHES_CITY));
            values.put(BRANCHES_PROVINCE, object.getString(BRANCHES_PROVINCE));
            values.put(BRANCHES_REGION, object.getString(BRANCHES_REGION));
            values.put(BRANCHES_STATUS, object.getString(BRANCHES_STATUS));
            values.put(CREATED_AT, object.getString("created_at"));

            rowID = db.insert(TBL_BRANCHES, null, values);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.close();
        return rowID > 0;
    }

    public boolean saveOrders(JSONObject object) {
        long rowID = 0;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put(SERVER_ORDERS_ID, object.getInt("id"));
            values.put(PATIENT_ID, object.getInt(PATIENT_ID));
            values.put(ORDERS_RECIPIENT_NAME, object.getString(ORDERS_RECIPIENT_NAME));
            values.put(ORDERS_RECIPIENT_ADDRESS, object.getString(ORDERS_RECIPIENT_ADDRESS));
            values.put(ORDERS_RECIPIENT_NUMBER, object.getString(ORDERS_RECIPIENT_NUMBER));
            values.put(ORDERS_DELIVERY_SCHED, object.getString(ORDERS_DELIVERY_SCHED));
            values.put(ORDERS_ECE_BRANCH, object.getInt(ORDERS_ECE_BRANCH));
            values.put(ORDERS_MODE_OF_DELIVERY, object.getString(ORDERS_MODE_OF_DELIVERY));
            values.put(ORDERS_STATUS, object.getString(ORDERS_STATUS));
            values.put(CREATED_AT, object.getString(CREATED_AT));
            values.put(UPDATED_AT, object.getString(UPDATED_AT));
            values.put(DELETED_AT, object.getString(DELETED_AT));

            rowID = db.insert(TBL_ORDERS, null, values);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.close();
        return rowID > 0;
    }

    public boolean saveOrderDetails(JSONObject object) {
        long rowID = 0;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(SERVER_ORDER_DETAILS_ID, object.getInt("id"));
            values.put(ORDER_DETAILS_ORDER_ID, object.getInt(ORDER_DETAILS_ORDER_ID));
            values.put(ORDER_DETAILS_PRODUCT_ID, object.getInt(ORDER_DETAILS_PRODUCT_ID));
            values.put(ORDER_DETAILS_PRESCRIPTION_ID, object.getInt(ORDER_DETAILS_PRESCRIPTION_ID));
            values.put(ORDER_DETAILS_QUANTITY, object.getInt(ORDER_DETAILS_QUANTITY));
            values.put(ORDER_DETAILS_TYPE, object.getString(ORDER_DETAILS_TYPE));
            values.put(ORDER_DETAILS_QTY_FULFILLED, object.getInt(ORDER_DETAILS_QTY_FULFILLED));
            values.put(CREATED_AT, object.getString(CREATED_AT));
            values.put(UPDATED_AT, object.getString(UPDATED_AT));
            values.put(DELETED_AT, object.getString(DELETED_AT));

            rowID = db.insert(TBL_ORDER_DETAILS, null, values);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.close();
        return rowID > 0;
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

    public boolean saveDoctor(Doctor doctor, String request) {
        long rowID = 0;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DOC_DOC_ID, doctor.getServer_doc_id());
        values.put(DOC_LNAME, doctor.getLname());
        values.put(DOC_MNAME, doctor.getMname());
        values.put(DOC_FNAME, doctor.getFname());
        values.put(DOC_PRC_NO, doctor.getPrc_no());
        values.put(DOC_SUB_SPECIALTY_ID, doctor.getSub_specialty_id());
        values.put(DOC_AFFILIATIONS, doctor.getAffiliation());
        values.put(DOC_EMAIL, doctor.getEmail());
        values.put(DOC_REFERRAL_ID, doctor.getReferral_id());
        values.put(CREATED_AT, doctor.getCreated_at());
        values.put(UPDATED_AT, doctor.getUpdated_at());
        values.put(DELETED_AT, doctor.getDeleted_at());

        if (request.equals("insert")) {
            rowID = db.insert(TBL_DOCTORS, null, values);

        } else if (request.equals("update")) {
            rowID = db.update(TBL_DOCTORS, values, DOC_DOC_ID + "=" + doctor.getServer_doc_id(), null);
        }

        db.close();
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
            rowID = db.update(TBL_SPECIALTIES, values, AI_ID + "=" + specialty.getSpecialty_id(), null);
        }

        db.close();
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
            rowID = db.update(TBL_SUB_SPECIALTIES, values, AI_ID + "=" + sub_specialty.getSpecialty_id(), null);
        }

        db.close();
        return rowID > 0;
    }

    public boolean savePatientConsultation(Consultation consult, String request) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        long rowID = 0;

        values.put(CONSULT_SERVER_ID, consult.getServerID());
        values.put(PATIENT_ID, consult.getPatientID());
        values.put(CONSULT_DOCTOR_ID, consult.getDoctorID());
        values.put(CONSULT_CLINIC_ID, consult.getClinicID());
        values.put(CONSULT_DATE, consult.getDate());
        values.put(CONSULT_TIME, consult.getTime());
        values.put(CONSULT_IS_ALARMED, consult.getIsAlarmed());
        values.put(CONSULT_ALARMED_TIME, consult.getAlarmedTime());
        values.put(CONSULT_IS_APPROVED, consult.getIs_approved());
        values.put(IS_READ, consult.getIs_read());
        values.put(CONSULT_COMMENT_DOCTOR, consult.getComment_doctor());
        values.put(CONSULT_PTNT_IS_APPROVED, consult.getPtnt_is_approved());
        values.put(CONSULT_COMMENT_PTNT, consult.getComment_patient());
        values.put(CREATED_AT, consult.getCreated_at());

        if (request.equals("add")) {
            rowID = db.insert(TBL_PATIENT_CONSULTATIONS, null, values);
        } else if (request.equals("update")) {
            rowID = db.update(TBL_PATIENT_CONSULTATIONS, values, CONSULT_SERVER_ID + "=" + consult.getServerID(), null);
        }
        db.close();

        return rowID > 0;
    }

    /* PROMO TABLE */
    public boolean savePromo(Promo promo, String action) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SERVER_PROMO_ID, promo.getServerPromoId());
        values.put(PROMO_NAME, promo.getName());
        values.put(PROMO_START_DATE, promo.getStartDate());
        values.put(PROMO_END_DATE, promo.getEndDate());
        values.put(PROMO_CREATED_AT, promo.getCreatedAt());
        values.put(PROMO_UPDATED_AT, promo.getUpdatedAt());
        values.put(PROMO_DELETED_AT, promo.getDeletedAt());

        long row;

        if (action.equals("insert")) {
            row = db.insert(TBL_PROMO, null, values);
        } else {
            row = db.update(TBL_PROMO, values, SERVER_PROMO_ID + "=" + promo.getServerPromoId(), null);
        }
        db.close();
        return row > 0;
    }

    /* DISCOUNTS & FREE PRODUCTS TABLE TABLE */
    public boolean saveDiscountsFreeProducts(DiscountsFreeProducts discountsFreeProducts, String action) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SERVER_DFP_ID, discountsFreeProducts.getDfpId());
        values.put(DFP_LESS, discountsFreeProducts.getLess());
        values.put(DFP_PROMO_ID, discountsFreeProducts.getPromoId());
        values.put(DFP_PRODUCT_ID, discountsFreeProducts.getProductId());
        values.put(DFP_QUANTITY_REQUIRED, discountsFreeProducts.getQuantityRequired());
        values.put(DFP_TYPE, discountsFreeProducts.getType());
        values.put(CREATED_AT, discountsFreeProducts.getCreatedAt());
        values.put(UPDATED_AT, discountsFreeProducts.getUpdatedAt());
        values.put(DELETED_AT, discountsFreeProducts.getDeletedAt());

        long row;

        if (action.equals("insert")) {
            row = db.insert(TBL_DISCOUNTS_FREE_PRODUCTS, null, values);
        } else {
            row = db.update(TBL_DISCOUNTS_FREE_PRODUCTS, values, SERVER_DFP_ID + "=" + discountsFreeProducts.getDfpId(), null);
        }
        db.close();
        return row > 0;
    }

    /* PROMO_FREE_PRODUCTS */
    public boolean saveFreeProducts(FreeProducts freeProducts, String action) {
        long row;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SERVER_FP_ID, freeProducts.getFreeProductsId());
        values.put(FP_DFP_ID, freeProducts.getDfpId());
        values.put(FP_PRODUCT_ID, freeProducts.getFreeProductsId());
        values.put(FP_QTY_FREE, freeProducts.getQuantityFree());
        values.put(FP_CREATED_AT, freeProducts.getCreatedAt());
        values.put(FP_UPDATED_AT, freeProducts.getUpdatedAt());
        values.put(FP_DELETED_AT, freeProducts.getDeletedAt());

        if (action.equals("insert")) {
            row = db.insert(TBL_FREE_PRODUCTS, null, values);
        } else {
            row = db.update(TBL_FREE_PRODUCTS, values, SERVER_FP_ID + "=" + freeProducts.getFreeProductsId(), null);
        }
        db.close();
        return row > 0;
    }

    //REFERRAL SETTINGS
    public boolean saveSettings(JSONObject json, String request) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues val = new ContentValues();
        long rowID = 0;

        try {
            val.put(SETTINGS_SERVER_ID, json.getInt("id"));
            val.put(SETTINGS_POINTS, json.getDouble(SETTINGS_POINTS));
            val.put(SETTINGS_POINTS_TO_PESO, json.getDouble(SETTINGS_POINTS_TO_PESO));
            val.put(SETTINGS_LVL_LIMIT, json.getInt(SETTINGS_LVL_LIMIT));
            val.put(SETTINGS_REFERRAL_COMM, json.getDouble(SETTINGS_REFERRAL_COMM));
            val.put(SETTINGS_COMM_VARIATION, json.getDouble(SETTINGS_COMM_VARIATION));
            val.put(SETTINGS_DELIVERY_CHARGE, json.getDouble(SETTINGS_DELIVERY_CHARGE));
            val.put(SETTINGS_DELIVERY_MINIMUM, json.getInt(SETTINGS_DELIVERY_MINIMUM));
            val.put(CREATED_AT, json.getString(CREATED_AT));
            val.put(UPDATED_AT, json.getString(UPDATED_AT));
            val.put(DELETED_AT, json.getString(DELETED_AT));

            if (request.equals("insert")) {
                rowID = db.insert(TBL_SETTINGS, null, val);
            } else if (request.equals("update")) {
                rowID = db.update(TBL_SETTINGS, val, SETTINGS_SERVER_ID + " = " + json.getInt("id"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        return rowID > 0;
    }

    public boolean saveMessages(JSONObject json, String request) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues val = new ContentValues();
        long rowID = 0;

        try {
            val.put(MSGS_SERVER_ID, json.getInt("id"));
            val.put(PATIENT_ID, json.getInt(PATIENT_ID));
            val.put(MSGS_SUBJECT, json.getString(MSGS_SUBJECT));
            val.put(MSGS_CONTENT, json.getString(MSGS_CONTENT));
            val.put(IS_READ, json.getInt(IS_READ));
            val.put(CREATED_AT, json.getString(CREATED_AT));
            val.put(UPDATED_AT, json.getString(UPDATED_AT));

            if (request.equals("insert")) {
                rowID = db.insert(TBl_MSGS, null, val);
            } else {
                rowID = db.update(TBl_MSGS, val, MSGS_SERVER_ID + " = " + json.getInt("id"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();

        return rowID > 0;
    }
    //////////////////////////////END OF SAVE METHODS//////////////////////////

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
        String sql1 = "SELECT * FROM " + TBL_PATIENTS + " WHERE " + PTNT_USERNAME + " = '" + uname + "' and " + PTNT_PASSWORD + " = '" + helper.md5(password) + "'";
        Cursor cur = db.rawQuery(sql1, null);
        cur.moveToFirst();

        if (cur.getCount() > 0) {
            login = 1;
        }

        cur.close();
        db.close();
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

        cur.close();
        db.close();
        return check;
    }

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
        values.put(PRODUCT_CRITICAL_STOCK, product.getPhoto());
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
            rowID = db.update(TBL_PRODUCTS, values, AI_ID + "=" + product.getProductId(), null);
        }

        db.close();
        return rowID > 0;
    }

    //////////////////////////////GET METHODS/////////////////////////////
    //for patients
    public ArrayList<HashMap<String, String>> getPatientRecord(int patientID) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select * FROM " + TBL_PATIENT_RECORDS + " WHERE " + PATIENT_ID + " = " + patientID + " ORDER BY " + RECORDS_DATE + " DESC";
        Cursor cur = db.rawQuery(sql, null);
        ArrayList<HashMap<String, String>> arrayOfRecords = new ArrayList();
        HashMap<String, String> map;

        while (cur.moveToNext()) {
            map = new HashMap();
            map.put(AI_ID, String.valueOf(cur.getInt(cur.getColumnIndex(AI_ID))));
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

    public Patient getCurrentLoggedInPatient() {
        Patient patient = this.getloginPatient(SidebarActivity.getUname());
        return patient;
    }

    public Patient getloginPatient(String username) {
        SQLiteDatabase db = getWritableDatabase();
        Patient patient = new Patient();

        String sql = "SELECT * FROM " + TBL_PATIENTS + " WHERE " + PTNT_USERNAME + " = '" + username + "'";
        Cursor cur = db.rawQuery(sql, null);
        cur.moveToFirst();

        if (cur.getCount() > 0) {
            patient.setId(cur.getInt(cur.getColumnIndex(AI_ID)));
            patient.setServerID(cur.getInt(cur.getColumnIndex(PATIENT_ID)));
            patient.setFname(cur.getString(cur.getColumnIndex(PTNT_FNAME)));
            patient.setMname(cur.getString(cur.getColumnIndex(PTNT_MNAME)));
            patient.setLname(cur.getString(cur.getColumnIndex(PTNT_LNAME)));
            patient.setUsername(cur.getString(cur.getColumnIndex(PTNT_USERNAME)));
            patient.setPassword(cur.getString(cur.getColumnIndex(PTNT_PASSWORD)));
            patient.setOccupation(cur.getString(cur.getColumnIndex(PTNT_OCCUPATION)));
            patient.setBirthdate(cur.getString(cur.getColumnIndex(PTNT_BIRTHDATE)));
            patient.setSex(cur.getString(cur.getColumnIndex(PTNT_SEX)));
            patient.setCivil_status(cur.getString(cur.getColumnIndex(PTNT_CIVIL_STATUS)));
            patient.setHeight(cur.getString(cur.getColumnIndex(PTNT_HEIGHT)));
            patient.setWeight(cur.getString(cur.getColumnIndex(PTNT_WEIGHT)));
            patient.setOptional_address(cur.getString(cur.getColumnIndex(PTNT_OPTIONAL_ADDRESS)));
            patient.setAddress_street(cur.getString(cur.getColumnIndex(PTNT_STREET)));
            patient.setBarangay(cur.getString(cur.getColumnIndex(PTNT_BARANGAY)));
            patient.setMunicipality(cur.getString(cur.getColumnIndex(PTNT_CITY)));
            patient.setProvince(cur.getString(cur.getColumnIndex(PTNT_PROVINCE)));
            patient.setRegion(cur.getString(cur.getColumnIndex(PTNT_REGION)));
            patient.setTel_no(cur.getString(cur.getColumnIndex(PTNT_TEL_NO)));
            patient.setMobile_no(cur.getString(cur.getColumnIndex(PTNT_MOBILE_NO)));
            patient.setEmail(cur.getString(cur.getColumnIndex(PTNT_EMAIL)));
            patient.setPhoto(cur.getString(cur.getColumnIndex(PTNT_PHOTO)));
            patient.setReferral_id(cur.getString(cur.getColumnIndex(PTNT_REFERRAL_ID)));
            patient.setReferred_byUser(cur.getString(cur.getColumnIndex(PTNT_REFERRED_BY_USER)));
            patient.setReferred_byDoctor(cur.getString(cur.getColumnIndex(PTNT_REFERRED_BY_DOCTOR)));
        }
        cur.close();
        db.close();

        return patient;
    }

    public PatientRecord getPatientRecordByRecordID(int recordID, int userID) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_PATIENT_RECORDS + " WHERE " + PATIENT_ID + " = " + userID + " AND " + AI_ID + " = " + recordID;
        Cursor cur = db.rawQuery(sql, null);
        cur.moveToFirst();
        PatientRecord record = new PatientRecord();

        if (cur.getCount() > 0) {
            record.setDoctorID(cur.getInt(cur.getColumnIndex(RECORDS_DOCTOR_ID)));
            record.setDoctorName(cur.getString(cur.getColumnIndex(RECORDS_DOCTOR_NAME)));
            record.setPatientID(cur.getInt(cur.getColumnIndex(PATIENT_ID)));
            record.setComplaints(cur.getString(cur.getColumnIndex(RECORDS_COMPLAINT)));
            record.setFindings(cur.getString(cur.getColumnIndex(RECORDS_FINDINGS)));
            record.setDate(cur.getString(cur.getColumnIndex(RECORDS_DATE)));
        }

        return record;
    }

    //for doctors
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
            map.put("doctor_id", String.valueOf(cur.getInt(cur.getColumnIndex(DOC_DOC_ID))));
            map.put("fullname", fullname);
            doctors.add(map);
        }
        cur.close();
        db.close();

        return doctors;
    }

    public ArrayList<HashMap<Integer, ArrayList<String>>> getSearchDoctors() {
        ArrayList<HashMap<Integer, ArrayList<String>>> doctors = new ArrayList();
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select d.*, s.name as specialty, ss.name as sub_specialty from " + TBL_SPECIALTIES + " as s" +
                " inner join " + TBL_SUB_SPECIALTIES + " as ss on s.specialty_id = ss.specialty_id inner join " + TBL_DOCTORS + " as d on ss.sub_specialty_id = d.sub_specialty_id";
        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            HashMap<Integer, ArrayList<String>> map = new HashMap();
            ArrayList<String> list = new ArrayList();

            list.add(cur.getString(cur.getColumnIndex(DOC_FNAME)) + " " + cur.getString(cur.getColumnIndex(DOC_LNAME)));
            list.add(cur.getString(cur.getColumnIndex(DOC_PRC_NO)));
            list.add(cur.getString(cur.getColumnIndex("specialty")));
            list.add(cur.getString(cur.getColumnIndex("sub_specialty")));

            map.put(cur.getInt(cur.getColumnIndex("doc_id")), list);
            doctors.add(map);
        }

        db.close();
        cur.close();

        return doctors;
    }

    public ArrayList<HashMap<String, String>> getAllDoctors() {
        ArrayList<HashMap<String, String>> doctors = new ArrayList();

        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT d.*, s.name FROM " + TBL_DOCTORS + " as d inner join " + TBL_SUB_SPECIALTIES + " as ss on d.sub_specialty_id = ss.sub_specialty_id inner join " + TBL_SPECIALTIES + " as s on ss.specialty_id = s.specialty_id";
        Cursor cur = db.rawQuery(sql, null);

        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            HashMap<String, String> map = new HashMap();
            map.put(DOC_DOC_ID, cur.getString(cur.getColumnIndex(DOC_DOC_ID)));
            map.put(DOC_FNAME, cur.getString(cur.getColumnIndex(DOC_FNAME)));
            map.put(DOC_LNAME, cur.getString(cur.getColumnIndex(DOC_LNAME)));
            map.put("fullname", "Dr. " + cur.getString(cur.getColumnIndex(DOC_FNAME)) + " " + cur.getString(cur.getColumnIndex(DOC_MNAME)).substring(0, 1) + ". " + cur.getString(cur.getColumnIndex(DOC_LNAME)));
            map.put(DOC_MNAME, cur.getString(cur.getColumnIndex(DOC_MNAME)));
            map.put(DOC_SUB_SPECIALTY_ID, cur.getString(cur.getColumnIndex(DOC_SUB_SPECIALTY_ID)));
            map.put("name", cur.getString(cur.getColumnIndex("name")));
            map.put(DOC_REFERRAL_ID, cur.getString(cur.getColumnIndex(DOC_REFERRAL_ID)));
            doctors.add(map);

            cur.moveToNext();
        }

        cur.close();
        db.close();

        return doctors;
    }

    public Doctor getDoctorByID(int doctorID) {
        SQLiteDatabase db = getWritableDatabase();
        String sqlgetDoctorByID = "SELECT d.*, s.name , ss.name as sub_name FROM " + TBL_DOCTORS + " as d inner join " +
                TBL_SUB_SPECIALTIES + " as ss on d.sub_specialty_id = ss.sub_specialty_id inner join " + TBL_SPECIALTIES +
                " as s on ss.specialty_id = s.specialty_id where d.doc_id = " + doctorID;
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
            doctor.setAffiliation(cur.getString(cur.getColumnIndex(DOC_AFFILIATIONS)));
            doctor.setEmail(cur.getString(cur.getColumnIndex(DOC_EMAIL)));
            doctor.setReferral_id(cur.getString(cur.getColumnIndex(DOC_REFERRAL_ID)));
            doctor.setCreated_at(cur.getString(cur.getColumnIndex(CREATED_AT)));
            doctor.setUpdated_at(cur.getString(cur.getColumnIndex(UPDATED_AT)));
            doctor.setDeleted_at(cur.getString(cur.getColumnIndex(DELETED_AT)));
        }
        cur.close();
        db.close();

        return doctor;
    }

    //for basket
    public Basket getBasket(int productId) {
        Basket basket = new Basket();

        String sql = "Select * from " + TBL_BASKETS + " where product_id=" + productId + " and patient_id=" + this.getCurrentLoggedInPatient().getServerID();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);

        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            basket.setId(cur.getInt(0));
            basket.setBasketId(cur.getInt(cur.getColumnIndex(SERVER_BASKET_ID)));
            basket.setPatienId(cur.getInt(cur.getColumnIndex(PATIENT_ID)));
            basket.setProductId(cur.getInt(cur.getColumnIndex(BASKET_PRODUCT_ID)));
            basket.setQuantity(cur.getInt(cur.getColumnIndex(BASKET_QUANTITY)));
            basket.setCreatedAt(cur.getString(cur.getColumnIndex(CREATED_AT)));
            basket.setUpdatedAt(cur.getString(cur.getColumnIndex(UPDATED_AT)));
            cur.moveToNext();
        }
        cur.close();
        db.close();
        return basket;
    }

    public ArrayList<HashMap<String, String>> getAllBasketItems(boolean onlyApprovedItems) {
        ArrayList<HashMap<String, String>> items = new ArrayList<>();

        String additionalWhere, sql;
        additionalWhere = "";
        if (onlyApprovedItems) {
            additionalWhere = " and b.is_approved=1";
        }

        sql = "Select b.id, b.basket_id, b.is_approved, b.prescription_id, p.product_id, p.name, p.price, p.packing, p.qty_per_packing," +
                " p.prescription_required, p.sku, b.quantity, p.unit from " + TBL_BASKETS + " as b " +
                "inner join " + TBL_PRODUCTS + " as p on p.product_id = b.product_id where " +
                "b.patient_id=" + this.getCurrentLoggedInPatient().getServerID() + additionalWhere;

        System.out.println("basket sql: " + sql);

        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);

        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            HashMap<String, String> map = new HashMap<>();
            map.put(AI_ID, cur.getString(cur.getColumnIndex(AI_ID)));
            map.put(SERVER_PRODUCT_ID, cur.getString(cur.getColumnIndex(SERVER_PRODUCT_ID)));
            map.put(SERVER_BASKET_ID, cur.getString(cur.getColumnIndex(SERVER_BASKET_ID)));
            map.put(PRODUCT_NAME, cur.getString(cur.getColumnIndex(PRODUCT_NAME)));
            map.put(PRODUCT_PRICE, String.valueOf(cur.getDouble(cur.getColumnIndex(PRODUCT_PRICE))));
            map.put(BASKET_QUANTITY, String.valueOf(cur.getInt(cur.getColumnIndex(BASKET_QUANTITY))));
            map.put(PRODUCT_UNIT, cur.getString(cur.getColumnIndex(PRODUCT_UNIT)));
            map.put(PRODUCT_SKU, cur.getString(cur.getColumnIndex(PRODUCT_SKU)));
            map.put(PRODUCT_PACKING, cur.getString(cur.getColumnIndex(PRODUCT_PACKING)));
            map.put(PRODUCT_QTY_PER_PACKING, cur.getString(cur.getColumnIndex(PRODUCT_QTY_PER_PACKING)));
            map.put(PRODUCT_PRESCRIPTION_REQUIRED, cur.getString(cur.getColumnIndex(PRODUCT_PRESCRIPTION_REQUIRED)));
            map.put(BASKET_PRESCRIPTION_ID, cur.getString(cur.getColumnIndex(BASKET_PRESCRIPTION_ID)));
            map.put(BASKET_IS_APPROVED, cur.getString(cur.getColumnIndex(BASKET_IS_APPROVED)));
            items.add(map);
            cur.moveToNext();
        }

        cur.close();
        db.close();
        return items;
    }

    public ArrayList<HashMap<String, String>> getAllOrderDetailItems() {
        ArrayList<HashMap<String, String>> stfu = new ArrayList<>();
        SQLiteDatabase asdas = getWritableDatabase();
        String sql = "SELECT od.order_details_id, p.name as product_name, p.price, od.quantity, o.created_at as ordered_on, o.status,  p.packing, p.qty_per_packing, p.unit from order_details as od inner join orders as o on od.order_id = o.orders_id inner join products as p on od.product_id = p.product_id inner join branches as br on o.branch_id = br.branches_id where o.patient_id = " + SidebarActivity.getUserID() + " order by od.created_at DESC";
        Cursor cur = asdas.rawQuery(sql, null);

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
//            map.put(ORDER_DETAILS_ID, cur.getString(cur.getColumnIndex(ORDER_DETAILS_ID)));
            map.put(SERVER_ORDER_DETAILS_ID, cur.getString(cur.getColumnIndex(SERVER_ORDER_DETAILS_ID)));
            map.put(PRODUCT_NAME, cur.getString(cur.getColumnIndex("product_name")));
            map.put(PRODUCT_PRICE, cur.getString(cur.getColumnIndex("price")));
            map.put(ORDER_DETAILS_QUANTITY, cur.getString(cur.getColumnIndex("quantity")));
            map.put(ORDERS_CREATED_AT, cur.getString(cur.getColumnIndex("ordered_on")));
            map.put(ORDERS_STATUS, cur.getString(cur.getColumnIndex("status")));
            map.put(PRODUCT_PACKING, cur.getString(cur.getColumnIndex(PRODUCT_PACKING)));
            map.put(PRODUCT_QTY_PER_PACKING, cur.getString(cur.getColumnIndex(PRODUCT_QTY_PER_PACKING)));
            map.put(PRODUCT_UNIT, cur.getString(cur.getColumnIndex(PRODUCT_UNIT)));
            stfu.add(map);
        }

        cur.close();
        asdas.close();
        return stfu;
    }

    public ArrayList<String> getAllOrderItems() {
        ArrayList<String> order_items = new ArrayList<>();
        SQLiteDatabase asdas = getWritableDatabase();
        String sql = "SELECT * from orders where patient_id = " + SidebarActivity.getUserID() + " order by created_at DESC";
        Cursor cur = asdas.rawQuery(sql, null);

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            order_items.add("Order #" + cur.getString(cur.getColumnIndex(SERVER_ORDERS_ID)) + " - " + cur.getString(cur.getColumnIndex(CREATED_AT)));
        }

        cur.close();
        asdas.close();
        return order_items;
    }

    //for prescription
    public ArrayList<HashMap<String, String>> getPrescriptionByUserID(int patientID) {
        ArrayList<HashMap<String, String>> listOfFilename = new ArrayList();
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_PATIENT_PRESCRIPTIONS + " WHERE " + PATIENT_ID + " = " + patientID;
        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap();
            map.put(PRESCRIPTIONS_SERVER_ID, String.valueOf(cur.getInt(cur.getColumnIndex(PRESCRIPTIONS_SERVER_ID))));
            map.put(PRESCRIPTIONS_FILENAME, cur.getString(cur.getColumnIndex(PRESCRIPTIONS_FILENAME)));
            listOfFilename.add(map);
        }

        cur.close();
        db.close();

        return listOfFilename;
    }

    public ArrayList<HashMap<String, String>> getAllClinics() {
        ArrayList<HashMap<String, String>> listOfClinics = new ArrayList();
        SQLiteDatabase db = getWritableDatabase();

        String sql = "SELECT * FROM " + TBL_CLINICS;
        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap();
            map.put("clinic_id", String.valueOf(cur.getInt(cur.getColumnIndex(SERVER_CLINICS_ID))));
            map.put("clinic_name", cur.getString(cur.getColumnIndex(CLINIC_NAME)));
            listOfClinics.add(map);
        }

        cur.close();
        db.close();
        return listOfClinics;
    }

    public ArrayList<HashMap<String, String>> getDoctorsInnerJoinClinics() {
        ArrayList<HashMap<String, String>> listOfDoctorClinic = new ArrayList();
        SQLiteDatabase db = getWritableDatabase();

        String sql = "select d.lname, d.mname, d.fname, c.name, c.clinics_id, cd.clinic_sched from " + TBL_DOCTORS + " as d INNER JOIN " +
                TBL_CLINIC_DOCTOR + " as cd on " + "d.doc_id = cd.doctor_id INNER JOIN " + TBL_CLINICS + " as c on cd.clinic_id = " +
                "c.clinics_id WHERE cd.is_active = 1";
        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap();
            String fullname = "Dr. " + cur.getString(cur.getColumnIndex(DOC_FNAME)) + " " + cur.getString(cur.getColumnIndex(DOC_MNAME)).substring(0, 1) + ". " + cur.getString(cur.getColumnIndex(DOC_LNAME));

            map.put("clinics_id", String.valueOf(cur.getInt(cur.getColumnIndex(SERVER_CLINICS_ID))));
            map.put("fullname", fullname);
            map.put("clinic_name", cur.getString(cur.getColumnIndex(CLINIC_NAME)));
            map.put("clinic_sched", cur.getString(cur.getColumnIndex(CD_CLINIC_SCHED)));
            listOfDoctorClinic.add(map);
        }
        cur.close();
        db.close();

        return listOfDoctorClinic;
    }

    public ArrayList<HashMap<String, String>> getClinicByDoctorID(int doctorID) {
        ArrayList<HashMap<String, String>> listOfClinics = new ArrayList();

        String sql = "SELECT c.*, cd.clinic_sched  FROM " + TBL_CLINICS + " as c INNER JOIN " + TBL_CLINIC_DOCTOR + " as cd ON c." + SERVER_CLINICS_ID +
                " = cd." + CD_CLINIC_ID + " WHERE cd." + CD_DOCTOR_ID + " = " + doctorID;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap();

            map.put(SERVER_CLINICS_ID, cur.getString(cur.getColumnIndex(SERVER_CLINICS_ID)));
            map.put(CLINIC_NAME, cur.getString(cur.getColumnIndex(CLINIC_NAME)));
            map.put(CLINIC_CONTACT_NO, cur.getString(cur.getColumnIndex(CLINIC_CONTACT_NO)));
            map.put(CLINIC_BUILDING, cur.getString(cur.getColumnIndex(CLINIC_BUILDING)));
            map.put(CLINIC_STREET, cur.getString(cur.getColumnIndex(CLINIC_STREET)));
            map.put(CLINIC_BARANGAY, cur.getString(cur.getColumnIndex(CLINIC_BARANGAY)));
            map.put(CLINIC_CITY, cur.getString(cur.getColumnIndex(CLINIC_CITY)));
            map.put(CLINIC_PROVINCE, cur.getString(cur.getColumnIndex(CLINIC_PROVINCE)));
            map.put(CLINIC_REGION, cur.getString(cur.getColumnIndex(CLINIC_REGION)));
            map.put(CLINIC_ZIP, cur.getString(cur.getColumnIndex(CLINIC_ZIP)));
            map.put(CD_CLINIC_SCHED, cur.getString(cur.getColumnIndex(CD_CLINIC_SCHED)));
            listOfClinics.add(map);
        }
        cur.close();
        db.close();

        return listOfClinics;
    }

    //for consultations
    public ArrayList<HashMap<String, String>> getAllConsultationsByUserId(int userID) {
        ArrayList<HashMap<String, String>> listOfAllConsultations = new ArrayList();
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT c.*, d.fname, d.lname, d.mname, cn.name FROM consultations as c inner join doctors as d " +
                "on c.doctor_id = d.id inner join clinics as cn on c.clinic_id = cn.id where c.patient_id = " + userID + " ORDER BY c.date ASC";
        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap();
            map.put(AI_ID, String.valueOf(cur.getInt(cur.getColumnIndex(AI_ID))));
            map.put(CONSULT_SERVER_ID, String.valueOf(cur.getInt(cur.getColumnIndex(CONSULT_SERVER_ID))));
            map.put(CONSULT_DOCTOR_ID, String.valueOf(cur.getInt(cur.getColumnIndex(CONSULT_DOCTOR_ID))));
            map.put("doctor_name", cur.getString(cur.getColumnIndex("fname")) + " " + cur.getString(cur.getColumnIndex("mname")).substring(0, 1) + ". " + cur.getString(cur.getColumnIndex("lname")));
            map.put(CONSULT_CLINIC_ID, String.valueOf(cur.getInt(cur.getColumnIndex(CONSULT_CLINIC_ID))));
            map.put("clinic_name", cur.getString(cur.getColumnIndex("name")));
            map.put(CONSULT_DATE, cur.getString(cur.getColumnIndex(CONSULT_DATE)));
            map.put(CONSULT_TIME, cur.getString(cur.getColumnIndex(CONSULT_TIME)));
            map.put(CONSULT_IS_ALARMED, String.valueOf(cur.getInt(cur.getColumnIndex(CONSULT_IS_ALARMED))));
            map.put(CONSULT_ALARMED_TIME, cur.getString(cur.getColumnIndex(CONSULT_ALARMED_TIME)));
            map.put(CONSULT_IS_APPROVED, String.valueOf(cur.getInt(cur.getColumnIndex(CONSULT_IS_APPROVED))));
            map.put(IS_READ, String.valueOf(cur.getInt(cur.getColumnIndex(IS_READ))));
            map.put(CONSULT_COMMENT_DOCTOR, cur.getString(cur.getColumnIndex(CONSULT_COMMENT_DOCTOR)));
            map.put(CONSULT_PTNT_IS_APPROVED, String.valueOf(cur.getInt(cur.getColumnIndex(CONSULT_PTNT_IS_APPROVED))));
            map.put(CONSULT_COMMENT_PTNT, cur.getString(cur.getColumnIndex(CONSULT_COMMENT_PTNT)));

            listOfAllConsultations.add(map);
        }
        db.close();
        cur.close();

        return listOfAllConsultations;
    }

    //for products
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
            map.put(AI_ID, cur.getString(cur.getColumnIndex(AI_ID)));
            map.put(PRODUCT_SUBCATEGORY_ID, cur.getString(cur.getColumnIndex(PRODUCT_SUBCATEGORY_ID)));
            map.put(PRODUCT_NAME, cur.getString(cur.getColumnIndex(PRODUCT_NAME)));
            map.put(PRODUCT_GENERIC_NAME, cur.getString(cur.getColumnIndex(PRODUCT_GENERIC_NAME)));
            map.put(PRODUCT_DESCRIPTION, cur.getString(cur.getColumnIndex(PRODUCT_DESCRIPTION)));
            map.put(PRODUCT_PRESCRIPTION_REQUIRED, cur.getString(cur.getColumnIndex(PRODUCT_PRESCRIPTION_REQUIRED)));
            map.put(PRODUCT_PRICE, cur.getString(cur.getColumnIndex(PRODUCT_PRICE)));
            map.put(PRODUCT_UNIT, cur.getString(cur.getColumnIndex(PRODUCT_UNIT)));
            map.put(PRODUCT_CRITICAL_STOCK, cur.getString(cur.getColumnIndex(PRODUCT_CRITICAL_STOCK)));
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

    public Product getProductById(int id) {
        Product prod = new Product();
        SQLiteDatabase db = getWritableDatabase();
        String sql = "Select * from " + TBL_PRODUCTS + " where product_id='" + id + "'";

        Cursor cur = db.rawQuery(sql, null);
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            prod.setId(cur.getInt(cur.getColumnIndex(AI_ID)));
            prod.setProductId(cur.getInt(cur.getColumnIndex(SERVER_PRODUCT_ID)));
            prod.setSubCategoryId(cur.getInt(cur.getColumnIndex(AI_ID)));
            prod.setName(cur.getString(cur.getColumnIndex(PRODUCT_NAME)));
            prod.setGenericName(cur.getString(cur.getColumnIndex(PRODUCT_GENERIC_NAME)));
            prod.setDescription(cur.getString(cur.getColumnIndex(PRODUCT_DESCRIPTION)));
            prod.setPrescriptionRequired(cur.getInt(cur.getColumnIndex(PRODUCT_PRESCRIPTION_REQUIRED)));
            prod.setPrice(cur.getDouble(cur.getColumnIndex(PRODUCT_PRICE)));
            prod.setUnit(cur.getString(cur.getColumnIndex(PRODUCT_UNIT)));
            prod.setPacking(cur.getString(cur.getColumnIndex(PRODUCT_PACKING)));
            prod.setQtyPerPacking(cur.getInt(cur.getColumnIndex(PRODUCT_QTY_PER_PACKING)));
            prod.setPhoto(cur.getString(cur.getColumnIndex(PRODUCT_CRITICAL_STOCK)));
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
            pID = cur.getInt(cur.getColumnIndex(AI_ID));
            cur.moveToNext();
        }
        cur.close();
        db.close();
        return pID;
    }

    //for promo
    public ArrayList<HashMap<String, String>> getPromo() {
        String sql = "Select pr.name as promo_name, pr.*, (Select min(dfp.less) from discounts_free_products as dfp " +
                "where dfp.promo_id = pr.promo_id and dfp.type=0) as min_discount, " +
                " (Select max(dfp.less) from discounts_free_products as dfp where dfp.promo_id = pr.promo_id and dfp.type=0) as max_discount from promo as pr " +
                "where  datetime('now') <= datetime(pr.end_date) and datetime('now') >= datetime(pr.start_date)";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);

        ArrayList<HashMap<String, String>> promo = new ArrayList<>();

        cur.moveToFirst();
        while (!cur.isAfterLast()) {

            HashMap<String, String> map = new HashMap<>();
            map.put("promo_name", cur.getString(cur.getColumnIndex("promo_name")));
            map.put("min_discount", cur.getString(cur.getColumnIndex("min_discount")));
            map.put("max_discount", cur.getString(cur.getColumnIndex("max_discount")));
            map.put("start_date", cur.getString(cur.getColumnIndex("start_date")));
            map.put("end_date", cur.getString(cur.getColumnIndex("end_date")));

            promo.add(map);
            cur.moveToNext();
        }
        cur.close();
        db.close();
        return promo;
    }

    public ArrayList<HashMap<String, String>> getPromoFreeProducts(int promoId) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "Select pfp.*, p.name as product_name, pd.name as promo_name, pd.quantity_required from promo_free_products " +
                "as pfp inner join promo_discounts as pd inner join products as p on p.product_id=pd.product_id where pfp.promo_id=" + promoId +
                " and datetime(pd.end_date) >= datetime('now')";
        Cursor cur = db.rawQuery(sql, null);
        ArrayList<HashMap<String, String>> products = new ArrayList<>();

        while (!cur.isAfterLast()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("promo_name", cur.getString(cur.getColumnIndex("promo_name")));
            map.put("product_name", cur.getString(cur.getColumnIndex("product_name")));
            map.put("no_of_units_free", cur.getString(cur.getColumnIndex("no_of_units_free")));
            map.put("quantity_required", cur.getString(cur.getColumnIndex("quantity_required")));

            products.add(map);
            cur.moveToNext();
        }
        cur.close();
        db.close();
        return products;
    }

    public ArrayList<HashMap<String, String>> getAllProducts() {
        ArrayList<HashMap<String, String>> products = new ArrayList();
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT DISTINCT p.*, f.product_id as fave_product FROM " + TBL_PRODUCTS + " as p LEFT JOIN " + TBL_FAVORITES + " as f on p." + SERVER_PRODUCT_ID + " = f.product_id";
        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap();
            map.put(AI_ID, cur.getString(cur.getColumnIndex(AI_ID)));
            map.put(SERVER_PRODUCT_ID, cur.getString(cur.getColumnIndex(SERVER_PRODUCT_ID)));
            map.put(PRODUCT_NAME, cur.getString(cur.getColumnIndex(PRODUCT_NAME)));
            map.put(PRODUCT_GENERIC_NAME, cur.getString(cur.getColumnIndex(PRODUCT_GENERIC_NAME)));
            map.put(PRODUCT_DESCRIPTION, cur.getString(cur.getColumnIndex(PRODUCT_DESCRIPTION)));
            map.put(PRODUCT_PRICE, cur.getString(cur.getColumnIndex(PRODUCT_PRICE)));
            map.put(PRODUCT_CRITICAL_STOCK, cur.getString(cur.getColumnIndex(PRODUCT_CRITICAL_STOCK)));
            map.put(PRODUCT_SKU, cur.getString(cur.getColumnIndex(PRODUCT_SKU)));
            map.put(PRODUCT_UNIT, cur.getString(cur.getColumnIndex(PRODUCT_UNIT)));
            map.put(PRODUCT_PACKING, cur.getString(cur.getColumnIndex(PRODUCT_PACKING)));
            map.put(PRODUCT_QTY_PER_PACKING, cur.getString(cur.getColumnIndex(PRODUCT_QTY_PER_PACKING)));
            map.put(PRODUCT_PRESCRIPTION_REQUIRED, cur.getString(cur.getColumnIndex(PRODUCT_PRESCRIPTION_REQUIRED)));
            map.put("is_favorite", cur.getString(cur.getColumnIndex("fave_product")));
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

    public Medicine getSpecificMedicine(String med_name) {
        Medicine medicine = new Medicine();
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_PRODUCTS + " WHERE " + PRODUCT_NAME + " = '" + med_name + "'";
        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            medicine.setId(cur.getInt(cur.getColumnIndex(AI_ID)));
            medicine.setServerID(cur.getInt(cur.getColumnIndex(SERVER_PRODUCT_ID)));
            medicine.setMedicine_name(cur.getString(cur.getColumnIndex(PRODUCT_NAME)));
            medicine.setGeneric_name(cur.getString(cur.getColumnIndex(PRODUCT_GENERIC_NAME)));
            medicine.setDescription(cur.getString(cur.getColumnIndex(PRODUCT_DESCRIPTION)));
            medicine.setPrice(cur.getDouble(cur.getColumnIndex(PRODUCT_PRICE)));
            medicine.setUnit(cur.getString(cur.getColumnIndex(PRODUCT_UNIT)));
            medicine.setPhoto(cur.getString(cur.getColumnIndex(PRODUCT_CRITICAL_STOCK)));
        }
        cur.close();
        db.close();

        return medicine;
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
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {
                        System.out.print("src: <DbHelper - getAllJSONArrayFrom>: " + e.toString());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return resultSet;
    }

    //for category
    public int getCategoryIdByName(String name) {
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
        cur.close();
        return id;
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

    //for subcategory
    public ProductSubCategory getSubCategoryByName(String name, int categoryId) {
        ProductSubCategory subCategory = new ProductSubCategory();
        SQLiteDatabase db = getWritableDatabase();
        name = name.replace("'", "''");
        String sql = "SELECT * FROM " + TBL_PRODUCT_SUBCATEGORIES + " where name='" + name + "' and category_id='" + categoryId + "'";
        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            subCategory.setId(cur.getInt(cur.getColumnIndex(AI_ID)));
            subCategory.setName(cur.getString(cur.getColumnIndex(PROD_SUBCAT_NAME)));
            subCategory.setCategoryId(Integer.parseInt(cur.getString(cur.getColumnIndex(PROD_SUBCAT_CATEGORY_ID))));
            subCategory.setCreatedAt(cur.getString(cur.getColumnIndex(CREATED_AT)));
            subCategory.setUpdatedAt(cur.getString(cur.getColumnIndex(UPDATED_AT)));
            subCategory.setDeletedAt(cur.getString(cur.getColumnIndex(DELETED_AT)));
        }

        db.close();
        cur.close();
        return subCategory;
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
        cur.close();
        String[] arr = new String[list.size()];
        return list.toArray(arr);
    }

    public Settings getAllSettings() {
        Settings settings = new Settings();

        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_SETTINGS;
        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            settings.setServerID(cur.getInt(cur.getColumnIndex(SETTINGS_SERVER_ID)));
            settings.setPoints(cur.getDouble(cur.getColumnIndex(SETTINGS_POINTS)));
            settings.setPoints_to_peso(cur.getDouble(cur.getColumnIndex(SETTINGS_POINTS_TO_PESO)));
            settings.setLvl_limit(cur.getInt(cur.getColumnIndex(SETTINGS_LVL_LIMIT)));
            settings.setReferral_comm(cur.getDouble(cur.getColumnIndex(SETTINGS_REFERRAL_COMM)));
            settings.setComm_variation(cur.getDouble(cur.getColumnIndex(SETTINGS_COMM_VARIATION)));
            settings.setDelivery_charge(cur.getDouble(cur.getColumnIndex(SETTINGS_DELIVERY_CHARGE)));
            settings.setDelivery_minimum(cur.getInt(cur.getColumnIndex(SETTINGS_DELIVERY_MINIMUM)));
            settings.setCreated_at(cur.getString(cur.getColumnIndex(CREATED_AT)));
            settings.setUpdated_at(cur.getString(cur.getColumnIndex(UPDATED_AT)));
            settings.setDeleted_at(cur.getString(cur.getColumnIndex(DELETED_AT)));
        }
        return settings;
    }

    public ArrayList<HashMap<String, String>> getAllMessages(int patientID) {
        ArrayList<HashMap<String, String>> array = new ArrayList();
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TBl_MSGS + " WHERE " + PATIENT_ID + " = " + patientID;
        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap();
            map.put("subject", cur.getString(cur.getColumnIndex(MSGS_SUBJECT)));
            map.put("content", cur.getString(cur.getColumnIndex(MSGS_CONTENT)));
            map.put("created_at", cur.getString(cur.getColumnIndex(CREATED_AT)));
            map.put("isRead", cur.getString(cur.getColumnIndex(IS_READ)));
            map.put("serverID", String.valueOf(cur.getInt(cur.getColumnIndex(MSGS_SERVER_ID))));
            array.add(map);
        }
        db.close();
        cur.close();

        return array;
    }

    public Messages getSpecificMessage(int serverID) {
        Messages messages = new Messages();
        String sql = "SELECT * FROM " + TBl_MSGS + " WHERE " + MSGS_SERVER_ID + " = " + serverID;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        cur.moveToFirst();

        if (cur.getCount() > 0) {
            messages.setServerID(serverID);
            messages.setSubject(cur.getString(cur.getColumnIndex(MSGS_SUBJECT)));
            messages.setContent(cur.getString(cur.getColumnIndex(MSGS_CONTENT)));
            messages.setDate(cur.getString(cur.getColumnIndex(CREATED_AT)));
            messages.setIsRead(cur.getInt(cur.getColumnIndex(IS_READ)));
        }
        db.close();
        cur.close();

        return messages;
    }
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

    public boolean updatePatientImage(String patient_image, int id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PTNT_PHOTO, patient_image);

        long row = db.update(TBL_PATIENTS, values, PATIENT_ID + "=" + id, null);

        db.close();
        return row > 0;
    }

    public boolean updateIsRead_table(int serverID, String table_name, String column_serverID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IS_READ, 1);

        long updated_id = db.update(table_name, values, column_serverID + " = " + serverID, null);

        db.close();
        return updated_id > 0;
    }

    public boolean updateSomeConsultation(JSONObject json) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        long updated_id = 0;

        try {
            values.put(CONSULT_TIME, json.getString("time"));
            values.put(IS_READ, 1);
            values.put(CONSULT_IS_APPROVED, json.getInt("is_approved"));
            values.put(CONSULT_COMMENT_DOCTOR, json.getString("comment_doctor"));

            updated_id = db.update(TBL_PATIENT_CONSULTATIONS, values, CONSULT_SERVER_ID + " = " + json.getInt("id"), null);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return updated_id > 0;
    }

    public boolean AcceptRejectConsultation(HashMap<String, String> map, String operation) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues val = new ContentValues();

        val.put(CONSULT_SERVER_ID, Integer.parseInt(map.get("id")));
        val.put(CONSULT_IS_APPROVED, map.get("is_approved"));
        val.put(CONSULT_PTNT_IS_APPROVED, map.get("patient_is_approved"));
        val.put(CONSULT_COMMENT_PTNT, map.get("comment_patient"));

        if (operation.equals("accept"))
            val.put(IS_READ, 0);
        else
            val.put(IS_READ, 1);

        long id = db.update(TBL_PATIENT_CONSULTATIONS, val, CONSULT_SERVER_ID + " = " + Integer.parseInt(map.get("id")), null);

        db.close();
        return id > 0;
    }
    ////////////////////////END OF UPDATE METHODS//////////////////////////////////

    /////////////////////////DELETE METHODS///////////////////////////////////////
    public boolean emptyBasket(int patient_id) {
        SQLiteDatabase db = getWritableDatabase();
        long row = db.delete(TBL_BASKETS, PATIENT_ID + "=" + patient_id, null);
        db.close();
        return row > 0;

    }

    public long deletePatientRecord(int recordID) {
        SQLiteDatabase db = getWritableDatabase();
        long deleted_record_ID = db.delete(TBL_PATIENT_RECORDS, AI_ID + " = " + recordID, null);
        db.close();

        return deleted_record_ID;
    }

    public boolean deletePrescriptionByServerID(int serverID) {
        SQLiteDatabase db = getWritableDatabase();
        long deletedPrescriptionID = db.delete(TBL_PATIENT_PRESCRIPTIONS, PRESCRIPTIONS_SERVER_ID + " = " + serverID, null);
        db.close();

        return deletedPrescriptionID > 0;
    }

    public boolean deleteFromTable(int serverID, String tableName, String column_serverID) {
        SQLiteDatabase db = getWritableDatabase();
        long deletedID = db.delete(tableName, column_serverID + " = " + serverID, null);

        db.close();
        return deletedID > 0;
    }

    public boolean removeFromSQLite(int AI_id) {
        SQLiteDatabase db = getWritableDatabase();
        long deletedID = db.delete(TBL_PATIENT_CONSULTATIONS, AI_ID + " = " + AI_id, null);

        db.close();
        return deletedID > 0;
    }

    public boolean removeFavorite(int user_id, int product_id) {
        SQLiteDatabase db = getWritableDatabase();
        long deleted_id = db.delete(TBL_FAVORITES, "product_id = " + product_id + " AND user_id = " + user_id, null);

        db.close();
        return deleted_id > 0;
    }
    ////////////////////////////END OF DELETE METHODS/////////////////////////////

    ////////////////////////////CHECK METHODS////////////////////////////////////
    public boolean checkOverlay(String title, String request) {
        SQLiteDatabase db = getWritableDatabase();
        long check = 0;

        if (request.equals("check")) {
            String sql = "SELECT * FROM " + TBL_OVERLAYS + " WHERE " + OVERLAY_TITLE + " = '" + title + "' AND " + IS_READ + " = 1";
            Cursor cur = db.rawQuery(sql, null);

            while (cur.moveToNext()) {
                check += 1;
            }

            cur.close();
        } else {
            ContentValues val = new ContentValues();

            val.put(OVERLAY_TITLE, title);
            val.put(IS_READ, 1);

            check = db.insert(TBL_OVERLAYS, null, val);
        }

        db.close();

        return check > 0;
    }

    public ArrayList<HashMap<String, String>> getECEBranches() {
        ArrayList<HashMap<String, String>> listOfBranches = new ArrayList();
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_BRANCHES;
        Cursor cur = db.rawQuery(sql, null);

        while (cur.moveToNext()) {
            HashMap<String, String> map = new HashMap();
            map.put(SERVER_BRANCHES_ID, String.valueOf(cur.getInt(cur.getColumnIndex(SERVER_BRANCHES_ID))));
            map.put(BRANCHES_NAME, cur.getString(cur.getColumnIndex(BRANCHES_NAME)));
            map.put(BRANCHES_ADDITIONAL_ADDRESS, cur.getString(cur.getColumnIndex(BRANCHES_ADDITIONAL_ADDRESS)));
            map.put(BRANCHES_BRGY_ID, cur.getString(cur.getColumnIndex(BRANCHES_BRGY_ID)));
            map.put(BRANCHES_BARANGAY, cur.getString(cur.getColumnIndex(BRANCHES_BARANGAY)));
            map.put(BRANCHES_CITY, cur.getString(cur.getColumnIndex(BRANCHES_CITY)));
            map.put(BRANCHES_PROVINCE, cur.getString(cur.getColumnIndex(BRANCHES_PROVINCE)));
            map.put(BRANCHES_REGION, cur.getString(cur.getColumnIndex(BRANCHES_REGION)));
            map.put("full_address", cur.getString(cur.getColumnIndex(BRANCHES_ADDITIONAL_ADDRESS)) + ", " + cur.getString(cur.getColumnIndex(BRANCHES_BARANGAY)) + ", " + cur.getString(cur.getColumnIndex(BRANCHES_CITY)));
            listOfBranches.add(map);
        }

        cur.close();
        db.close();
        return listOfBranches;
    }

    public ArrayList<HashMap<String, String>> getECEBranchesfromjson(JSONObject jobject, String tbl_name) {

        ArrayList<HashMap<String, String>> listOfBranches = new ArrayList();
        String table_name = tbl_name;

        try {
            JSONArray json_array_mysql = jobject.getJSONArray(tbl_name);
            for (int i = 0; i < json_array_mysql.length(); i++) {
                JSONObject row = json_array_mysql.getJSONObject(i);
                HashMap<String, String> map = new HashMap();
                map.put(SERVER_BRANCHES_ID, String.valueOf(row.getInt(AI_ID)));
                map.put(BRANCHES_NAME, row.getString(BRANCHES_NAME));
                map.put(BRANCHES_ADDITIONAL_ADDRESS, row.getString(BRANCHES_ADDITIONAL_ADDRESS));
                map.put(BRANCHES_BRGY_ID, row.getString(BRANCHES_BRGY_ID));
                map.put(BRANCHES_BARANGAY, row.getString(BRANCHES_BARANGAY));
                map.put(BRANCHES_CITY, row.getString(BRANCHES_CITY));
                map.put(BRANCHES_PROVINCE, row.getString(BRANCHES_PROVINCE));
                map.put(BRANCHES_REGION, row.getString(BRANCHES_REGION));
                map.put("latitude", String.valueOf(row.getDouble("latitude")));
                map.put("longitude", String.valueOf(row.getDouble("longitude")));
                map.put("full_address", row.getString(BRANCHES_ADDITIONAL_ADDRESS) + ", " + row.getString(BRANCHES_BARANGAY) + ", " + row.getString(BRANCHES_CITY));
                map.put("same_region", String.valueOf(row.getInt("same_region")));
                listOfBranches.add(map);
            }
        } catch (Exception e) {

        }

        return listOfBranches;
    }
    //////////////////////////END OF CHECK METHODS//////////////////////////////
}