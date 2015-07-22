package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class PatientMedicalRecordActivity extends Activity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemClickListener {
    EditText date, complaint, diagnosis, generic_name, qty, dosage;
    TextView add_treatment;
    ListView list_of_treatments;
    AutoCompleteTextView search_doctor, search_medicine;
    Button save_treatment, cancel_treatment;
    String s_date, s_doctor, s_complaint, s_diagnosis, s_generic_name, s_qty, s_dosage, s_medicine;

    ArrayList<HashMap<String, String>> arrayOfDoctors, items, update_treatments;
    ArrayList<String> doctors, medicine, treatments;
    HashMap<String, String> map;

    ArrayAdapter treatmentsAdapter, medicineAdapter;

    DbHelper dbhelper;
    Dialog dialog;
    Patient patient;
    Product product;
    Medicine med;
    PatientRecord record;
    Intent intent;

    public static Activity medRecord;
    public static String UPDATE_RECORD_ID = "recordID";

    public static final String TREATMENTS_ID = "id", MEDICINE_NAME = "medicine_name", GENERIC_NAME = "generic_name",
            QUANITY = "quantity", PRESCRIPTION = "prescription";

    private int doctorID = 0;
    private String request = "";
    long IDs;
    static int update_recordID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_new_medical_record);

        ActionBar actionBar = getActionBar();
        MainActivity.setCustomActionBar(actionBar);

        dbhelper = new DbHelper(this);
        product = new Product();
        items = new ArrayList();
        doctors = new ArrayList();
        treatments = new ArrayList();
        update_treatments = new ArrayList();
        medRecord = this;

        intent = getIntent();
        update_recordID = intent.getIntExtra(UPDATE_RECORD_ID, 0);

        date = (EditText) findViewById(R.id.date);
        list_of_treatments = (ListView) findViewById(R.id.list_of_treatments);
        search_doctor = (AutoCompleteTextView) findViewById(R.id.search_doctor);
        complaint = (EditText) findViewById(R.id.complaint);
        diagnosis = (EditText) findViewById(R.id.diagnosis);
        add_treatment = (TextView) findViewById(R.id.add_treatment);

        arrayOfDoctors = dbhelper.getDoctorName();

        for (int x = 0; x < arrayOfDoctors.size(); x++) {
            doctors.add(arrayOfDoctors.get(x).get("fullname"));
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, doctors);
        search_doctor.setAdapter(adapter);
        search_doctor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item_clicked = parent.getItemAtPosition(position).toString();
                int itemID = doctors.indexOf(item_clicked);

                doctorID = Integer.parseInt(arrayOfDoctors.get(itemID).get("ID"));
            }
        });

        if (update_recordID > 0) {
            record = dbhelper.getPatientRecordByRecordID(update_recordID, SidebarActivity.getUserID());
            update_treatments = dbhelper.getTreatmentByRecordID(update_recordID);

            for (int x = 0; x < update_treatments.size(); x++) {
                map = new HashMap();
                map.put(TREATMENTS_ID, update_treatments.get(x).get(dbhelper.TREATMENTS_ID));
                map.put(MEDICINE_NAME, update_treatments.get(x).get(dbhelper.TREATMENTS_MEDICINE_NAME));
                map.put(GENERIC_NAME, update_treatments.get(x).get(dbhelper.TREATMENTS_GENERIC_NAME));
                map.put(QUANITY, update_treatments.get(x).get(dbhelper.TREATMENTS_QUANITY));
                map.put(PRESCRIPTION, update_treatments.get(x).get(dbhelper.TREATMENTS_PRESCRIPTION));
                items.add(map);
                treatments.add(items.get(x).get(MEDICINE_NAME) + " - " + items.get(x).get(PRESCRIPTION));
            }
            date.setText(record.getDate());
            complaint.setText(record.getComplaints());
            diagnosis.setText(record.getFindings());
            search_doctor.setText(record.getDoctorName());
            doctorID = record.getDoctorID();
        }

        treatmentsAdapter = new ArrayAdapter(this, R.layout.treatments_list_layout, treatments);
        list_of_treatments.setAdapter(treatmentsAdapter);
        list_of_treatments.setOnCreateContextMenuListener(this);

        date.setOnClickListener(this);
        add_treatment.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_and_cancel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save) {
            s_date = date.getText().toString();
            s_doctor = search_doctor.getText().toString();
            s_complaint = complaint.getText().toString();
            s_diagnosis = diagnosis.getText().toString();

            if (s_date.equals("") || s_doctor.equals("") || s_complaint.equals("") || s_diagnosis.equals("") || items.size() == 0) {
                if (date.getText().toString().equals("")) {
                    date.setError("Field required");
                }
                if (search_doctor.getText().toString().equals("")) {
                    search_doctor.setError("Field required");
                }
                if (complaint.getText().toString().equals("")) {
                    complaint.setError("Field required");
                }
                if (items.size() == 0) {
                    add_treatment.setError("Field required");
                }
                if (diagnosis.getText().toString().equals("")) {
                    diagnosis.setError("Field required");
                }
            } else {
                String uname = SidebarActivity.getUname();
                patient = dbhelper.getloginPatient(uname);

                record = new PatientRecord();
                record.setPatientID(patient.getServerID());
                record.setComplaints(s_complaint);
                record.setFindings(s_diagnosis);
                record.setDate(s_date);
                record.setDoctorID(doctorID);
                record.setDoctorName(s_doctor);

                if (update_recordID > 0) { //FOR UPDATING RECORD
                    request = "update";
                    record.setRecordID(update_recordID);
                } else { //FOR ADDING NEW RECORD
                    request = "insert";
                }

                IDs = dbhelper.savePatientRecord(record, request);
                if (IDs > 0) {
                    if (update_recordID > 0) {
                        IDs = update_recordID;
                    }
                    dbhelper.insertTreatment(items, IDs);
                    Intent intent = new Intent(this, MasterTabActivity.class);
                    intent.putExtra("selected", 1);
                    startActivity(intent);
                    this.finish();

                } else {
                    Toast.makeText(this, "error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (id == R.id.cancel) {
            Intent intent = new Intent(this, MasterTabActivity.class);
            intent.putExtra("selected", 1);
            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date:
                Calendar cal = Calendar.getInstance();
                if (date.getText().toString().equals("")) {
                    DatePickerDialog datePicker = new DatePickerDialog(this, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                    datePicker.show();
                } else {
                    int month, year, day;
                    String birth = date.getText().toString();
                    int indexOfMonthandDay = birth.indexOf("/");
                    int indexOfYear = birth.lastIndexOf("/");
                    year = Integer.parseInt(birth.substring(indexOfYear + 1, birth.length()));
                    month = Integer.parseInt(birth.substring(0, indexOfMonthandDay));
                    day = Integer.parseInt(birth.substring(indexOfMonthandDay + 1, indexOfYear));

                    updateDate(year, month - 1, day);
                }
                break;

            case R.id.add_treatment:
                readDialog();
                break;

            case R.id.cancel_treatment:
                dialog.dismiss();
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String dateStr = String.format("%d/%d/%d", (monthOfYear + 1), dayOfMonth, year);
        date.setText(dateStr);
        date.setError(null);
    }

    public void updateDate(int year, int monthOfYear, int dayOfMonth) {
        DatePickerDialog datePicker = new DatePickerDialog(this, this, year, monthOfYear, dayOfMonth);
        datePicker.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item_clicked = parent.getItemAtPosition(position).toString();
        String medicine = "";
        med = new Medicine();

        if (item_clicked.contains("(")) {
            int indexOfUnit = item_clicked.indexOf("(");
            medicine = item_clicked.substring(0, indexOfUnit).trim();
        } else {
            medicine = parent.getItemAtPosition(position).toString();
        }

        dbhelper.getSpecificMedicine(medicine, med);
        generic_name.setText(med.getGeneric_name());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.delete_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuinfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.item_delete:
                treatments.remove(menuinfo.position);
                items.remove(menuinfo.position);
                treatmentsAdapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void readDialog() {
        dialog = new Dialog(this);
        dialog.setTitle("Add Treatment");
        dialog.setContentView(R.layout.dialog_new_treatment);
        dialog.show();

        generic_name = (EditText) dialog.findViewById(R.id.generic_name);
        qty = (EditText) dialog.findViewById(R.id.qty);
        dosage = (EditText) dialog.findViewById(R.id.dosage);
        search_medicine = (AutoCompleteTextView) dialog.findViewById(R.id.search_medicine);
        save_treatment = (Button) dialog.findViewById(R.id.save_treatment);
        cancel_treatment = (Button) dialog.findViewById(R.id.cancel_treatment);

        medicine = dbhelper.getMedicine();

        medicineAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, medicine);
        search_medicine.setAdapter(medicineAdapter);
        search_medicine.setOnItemClickListener(this);

        add_treatment.setError(null);

        cancel_treatment.setOnClickListener(this);
        save_treatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_generic_name = generic_name.getText().toString();
                s_qty = qty.getText().toString();
                s_dosage = dosage.getText().toString();
                s_medicine = search_medicine.getText().toString();

                if (s_generic_name.equals("") || s_dosage.equals("") || s_medicine.equals("")) {
                    if (s_generic_name.equals("")) {
                        generic_name.setError("Field required");
                    }
                    if (s_medicine.equals("")) {
                        search_medicine.setError("Field required");
                    }
                    if (s_dosage.equals("")) {
                        dosage.setError("Field required");
                    }
                } else {
                    map = new HashMap();
                    map.put(MEDICINE_NAME, s_medicine);
                    map.put(GENERIC_NAME, s_generic_name);
                    map.put(QUANITY, s_qty);
                    map.put(PRESCRIPTION, s_dosage);
                    items.add(map);
                    treatments.add(s_medicine + " - " + s_dosage);

                    treatmentsAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
    }
}
