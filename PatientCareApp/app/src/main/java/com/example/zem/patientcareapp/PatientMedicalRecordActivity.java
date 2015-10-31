package com.example.zem.patientcareapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.example.zem.patientcareapp.GetterSetter.Medicine;
import com.example.zem.patientcareapp.GetterSetter.Patient;
import com.example.zem.patientcareapp.GetterSetter.PatientRecord;
import com.example.zem.patientcareapp.GetterSetter.Product;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class PatientMedicalRecordActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, CalendarDatePickerDialogFragment.OnDateSetListener {
    EditText date, complaint, diagnosis, generic_name, qty, dosage;
    TextView add_treatment;
    Toolbar myToolBar;
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

    public static final String TREATMENTS_ID = "id", MEDICINE_NAME = "medicine_name", GENERIC_NAME = "generic_name",
            QUANITY = "quantity", PRESCRIPTION = "prescription";

    private int doctorID = 0;
    private String request = "";
    long IDs;
    static int view_record_id = 0;
    static Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_new_medical_record);

        myToolBar = (Toolbar) findViewById(R.id.myToolBar);
        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("New Record");

        dbhelper = new DbHelper(this);
        product = new Product();
        items = new ArrayList();
        doctors = new ArrayList();
        treatments = new ArrayList();
        update_treatments = new ArrayList();
        medRecord = this;

        intent = getIntent();
        view_record_id = intent.getIntExtra("viewRecord", 0);

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

        if (view_record_id > 0) {
            getSupportActionBar().setTitle("View Record");
            add_treatment.setVisibility(View.INVISIBLE);

            record = dbhelper.getPatientRecordByRecordID(view_record_id, SidebarActivity.getUserID());
            update_treatments = dbhelper.getTreatmentByRecordID(view_record_id);

            for (int x = 0; x < update_treatments.size(); x++) {
                map = new HashMap();
                map.put(TREATMENTS_ID, update_treatments.get(x).get(dbhelper.AI_ID));
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

            date.setEnabled(false);
            complaint.setEnabled(false);
            diagnosis.setEnabled(false);
            search_doctor.setEnabled(false);
            list_of_treatments.setEnabled(false);
        }

        treatmentsAdapter = new ArrayAdapter(this, R.layout.treatments_list_layout, treatments);
        list_of_treatments.setAdapter(treatmentsAdapter);
        list_of_treatments.setOnCreateContextMenuListener(this);

        date.setOnClickListener(this);
        add_treatment.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        if (view_record_id > 0)
            getMenuInflater().inflate(R.menu.update_options_menu, menu);
        else
            getMenuInflater().inflate(R.menu.save_and_cancel, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (view_record_id > 0) {
            switch (item.getItemId()) {
                case R.id.edit:
                    MenuItem menuEdit = menu.findItem(R.id.edit);
                    menuEdit.setEnabled(false).setVisible(false);
                    getSupportActionBar().setTitle("Update Record");
                    getMenuInflater().inflate(R.menu.save_and_cancel, menu);

                    date.setEnabled(true);
                    search_doctor.setEnabled(true);
                    complaint.setEnabled(true);
                    diagnosis.setEnabled(true);
                    list_of_treatments.setEnabled(true);
                    add_treatment.setVisibility(View.VISIBLE);
                    break;
            }
        }
        switch (item.getItemId()) {
            case R.id.save:
                s_date = date.getText().toString();
                s_doctor = search_doctor.getText().toString();
                s_complaint = complaint.getText().toString();
                s_diagnosis = diagnosis.getText().toString();

                if (s_date.equals("") || s_doctor.equals("") || s_complaint.equals("") || s_diagnosis.equals("") || items.size() == 0) {
                    if (date.getText().toString().equals(""))
                        date.setError("Field required");
                    if (search_doctor.getText().toString().equals(""))
                        search_doctor.setError("Field required");
                    if (complaint.getText().toString().equals(""))
                        complaint.setError("Field required");
                    if (items.size() == 0)
                        add_treatment.setError("Field required");
                    if (diagnosis.getText().toString().equals(""))
                        diagnosis.setError("Field required");
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

                    if (view_record_id > 0) { //FOR UPDATING RECORD
                        request = "update";
                        record.setRecordID(view_record_id);
                    } else //FOR ADDING NEW RECORD
                        request = "insert";

                    IDs = dbhelper.savePatientRecord(record, request);
                    if (IDs > 0) {
                        if (view_record_id > 0)
                            IDs = view_record_id;

                        dbhelper.insertTreatment(items, IDs);
                        Intent intent = new Intent(this, MasterTabActivity.class);
                        intent.putExtra("selected", 1);
                        startActivity(intent);
                        this.finish();
                    } else
                        Toast.makeText(this, "error occurred", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.cancel:
                this.finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date:
                FragmentManager fm = getSupportFragmentManager();
                CalendarDatePickerDialogFragment datepicker;
                String birth = date.getText().toString();
                int month, year, day;

                if (birth.equals("")) {
                    Calendar calendar = Calendar.getInstance();
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    day = calendar.get(Calendar.DATE);
                } else {
                    int indexOfYear = birth.indexOf("-");
                    int indexOfMonthandDay = birth.lastIndexOf("-");
                    year = Integer.parseInt(birth.substring(0, indexOfYear));
                    month = Integer.parseInt(birth.substring(indexOfYear + 1, indexOfMonthandDay)) - 1;
                    day = Integer.parseInt(birth.substring(indexOfMonthandDay + 1, birth.length()));
                }

                datepicker = CalendarDatePickerDialogFragment.newInstance(this, year, month, day);

                datepicker.show(fm, "fragment_date_picker_name");
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
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        String dateStr = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        date.setText(dateStr);
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
        getMenuInflater().inflate(R.menu.multiple_delete_menu, menu);
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
