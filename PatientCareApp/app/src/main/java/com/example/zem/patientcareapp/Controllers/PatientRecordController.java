package com.example.zem.patientcareapp.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.zem.patientcareapp.Model.PatientRecord;

import java.util.ArrayList;
import java.util.HashMap;

public class PatientRecordController extends DbHelper {

    DbHelper dbhelper;
    SQLiteDatabase sql_db;

    //PATIENT_RECORDS TABLE
    public static final String TBL_PATIENT_RECORDS = "patient_records",
            SERVER_RECORDS_ID = "record_id",
            SERVER_CPR_ID = "clinic_patient_record_id",
            RECORDS_DOCTOR_ID = "doctor_id",
            RECORDS_CLINIC_ID = "clinic_id",
            RECORDS_DOCTOR_NAME = "doctor_name",
            RECORDS_CLINIC_NAME = "clinic_name",
            RECORDS_COMPLAINT = "complaints",
            RECORDS_FINDINGS = "findings",
            RECORDS_DATE = "record_date";

    // SQL to create "patient_records"
    public static final String CREATE_TABLE = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT )",
            TBL_PATIENT_RECORDS, AI_ID, SERVER_RECORDS_ID, SERVER_CPR_ID, RECORDS_DOCTOR_ID, RECORDS_CLINIC_ID, RECORDS_DOCTOR_NAME, RECORDS_CLINIC_NAME, RECORDS_COMPLAINT, RECORDS_FINDINGS, RECORDS_DATE, CREATED_AT, UPDATED_AT, DELETED_AT);

    public PatientRecordController(Context context) {
        super(context);
        dbhelper = new DbHelper(context);
        sql_db = dbhelper.getWritableDatabase();
    }

    public boolean savePatientRecord(PatientRecord record, String request) {
        SQLiteDatabase sql_db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        long rowID = 0;

        if (record.getDoctorID() > 0 && record.getClinicID() > 0) {
            String sql = "SELECT d.fname, d.lname, c.name FROM doctors as d inner join clinic_doctor as cd on cd.doctor_id = d.doc_id " +
                    "inner join clinics as c on c.clinics_id = cd.clinic_id where d.doc_id = " + record.getDoctorID() + " and c.clinics_id = " + record.getClinicID();
            Cursor cur = sql_db.rawQuery(sql, null);

            if (cur.moveToNext()) {
                String fullname = cur.getString(cur.getColumnIndex("lname")) + ", " + cur.getString(cur.getColumnIndex("fname"));
                String clinic = cur.getString(cur.getColumnIndex("name"));

                values.put(RECORDS_DOCTOR_NAME, fullname);
                values.put(RECORDS_CLINIC_NAME, clinic);
            }
        } else {
            values.put(RECORDS_DOCTOR_NAME, record.getDoctorName());
            values.put(RECORDS_CLINIC_NAME, record.getClinicName());
        }


        values.put(SERVER_RECORDS_ID, record.getRecordID());
        values.put(RECORDS_DOCTOR_ID, record.getDoctorID());
        values.put(RECORDS_CLINIC_ID, record.getClinicID());
        values.put(RECORDS_COMPLAINT, record.getComplaints());
        values.put(RECORDS_FINDINGS, record.getFindings());
        values.put(RECORDS_DATE, record.getDate());
        values.put(CREATED_AT, record.getCreated_at());
        values.put(UPDATED_AT, record.getUpdated_at());
        values.put(DELETED_AT, record.getDeleted_at());

        if (request.equals("insert"))
            rowID = sql_db.insert(TBL_PATIENT_RECORDS, null, values);

        sql_db.close();
        return rowID > 0;
    }

    public ArrayList<HashMap<String, String>> getPatientRecord() {
        SQLiteDatabase sql_db = dbhelper.getWritableDatabase();
        String sql = "SELECT * FROM " + TBL_PATIENT_RECORDS + " ORDER BY " + RECORDS_DATE + " DESC";
        Cursor cur = sql_db.rawQuery(sql, null);
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
        sql_db.close();

        return arrayOfRecords;
    }
}
