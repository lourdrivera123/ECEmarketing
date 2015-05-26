package com.example.zem.patientcareapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
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

/**
 * Created by User PC on 5/18/2015.
 */
public class PatientMedicalRecordActivity extends ActionBarActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemClickListener {
    EditText date, complaint, diagnosis, generic_name, qty, dosage;
    TextView add_treatment;
    ListView list_of_treatments;
    AutoCompleteTextView search_doctor, search_medicine;
    Button btn_save, btn_cancel, save_treatment, cancel_treatment;
    String s_date, s_doctor, s_complaint, s_diagnosis, s_generic_name, s_qty, s_dosage, s_medicine;

    ArrayList<HashMap<String, String>> arrayOfDoctors;
    ArrayList<String> doctors;
    ArrayList<String> treatments;
    ArrayList<String> medicine;
    ArrayAdapter treatmentsAdapter;
    ArrayAdapter medicineAdapter;

    ArrayList<HashMap<String, String>> items;
    HashMap<String, String> map;

    DbHelper dbhelper;
    Dialog dialog;
    Patient patient;
    Product product;
    Medicine med;
    PatientRecord record;

    static int edit_request = -1;
    static int add_request = -1;

    int doctorID = 0;
    long IDs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_new_medical_record);

        dbhelper = new DbHelper(this);
        product = new Product();
        record = new PatientRecord();
        items = new ArrayList<HashMap<String, String>>();
        doctors = new ArrayList<String>();

        date = (EditText) findViewById(R.id.date);
        list_of_treatments = (ListView) findViewById(R.id.list_of_treatments);
        search_doctor = (AutoCompleteTextView) findViewById(R.id.search_doctor);
        complaint = (EditText) findViewById(R.id.complaint);
        diagnosis = (EditText) findViewById(R.id.diagnosis);
        add_treatment = (TextView) findViewById(R.id.add_treatment);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

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

        treatments = new ArrayList<String>();
        treatmentsAdapter = new ArrayAdapter(this, R.layout.treatments_list_layout, treatments);
        list_of_treatments.setAdapter(treatmentsAdapter);
        list_of_treatments.setOnCreateContextMenuListener(this);

        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        date.setOnClickListener(this);
        add_treatment.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
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
                    if (!doctors.contains(s_doctor)) {
                        doctorID = 0;
                    }

                    String uname = HomeTileActivity.getUname();
                    patient = dbhelper.getloginPatient(uname);

                    record.setPatientID(patient.getServerID());
                    record.setComplaints(s_complaint);
                    record.setFindings(s_diagnosis);
                    record.setDate(s_date);
                    record.setDoctorID(doctorID);
                    record.setDoctorName(s_doctor);

                    IDs = dbhelper.insertPatientRecord(record);
                    if (IDs > 0) {
                        if (dbhelper.insertTreatment(items, IDs).size() > 0) {
                            Toast.makeText(this, "successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, MasterTabActivity.class);
                            intent.putExtra("selected", 1);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(this, "error occurred", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.btn_cancel:
                this.finish();
                break;

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
                add_request = 20;
                readDialog("", -1);
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
        getMenuInflater().inflate(R.menu.cart_menus, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuinfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String treatment_name = (String) list_of_treatments.getItemAtPosition(menuinfo.position);

        switch (item.getItemId()) {
            case R.id.update_cart:
                int indexOfUnit = treatment_name.indexOf("-");
                treatment_name = treatment_name.substring(0, indexOfUnit).trim();
                edit_request = menuinfo.position;

                readDialog(treatment_name, menuinfo.position);
                break;
            case R.id.delete_cart:
                treatmentsAdapter.remove(treatment_name);
                treatmentsAdapter.notifyDataSetChanged();
                items.remove(menuinfo.position);
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void readDialog(String name, int position) {

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

        if (!name.equals("")) {
            search_medicine.setText(name);
            generic_name.setText(String.valueOf(items.get(position).get("generic_name")));
            qty.setText(String.valueOf(items.get(position).get("qty")));
            dosage.setText(String.valueOf(items.get(position).get("dosage")));
        }

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
                    map = new HashMap<String, String>();
                    map.put("treatment_name", s_medicine);
                    map.put("generic_name", s_generic_name);
                    map.put("qty", s_qty);
                    map.put("dosage", s_dosage);

                    if (edit_request > -1) {
                        items.set(edit_request, map);
                        treatments.set(edit_request, s_medicine + " - " + s_dosage);
                        edit_request = -1;
                    } else if (add_request > -1) {
                        items.add(map);
                        treatments.add(s_medicine + " - " + s_dosage);
                        add_request = -1;
                    }

                    for (int x = 0; x > items.size(); x++) {
                        Log.i("update array", String.valueOf(items.get(x)));
                    }
                    treatmentsAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
    }
}
