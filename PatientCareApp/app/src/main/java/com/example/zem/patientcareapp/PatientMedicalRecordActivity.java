package com.example.zem.patientcareapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
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

/**
 * Created by User PC on 5/18/2015.
 */
public class PatientMedicalRecordActivity extends ActionBarActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    EditText date, complaint, diagnosis, generic_name, qty, dosage;
    TextView add_treatment;
    ListView list_of_treatments;
    AutoCompleteTextView search_doctor, search_medicine;
    Button btn_save, btn_cancel, save_treatment, cancel_treatment;
    String s_date, s_doctor, s_complaint, s_diagnosis, s_generic_name, s_qty, s_dosage, s_medicine;

    ArrayList<String> doctors;
    ArrayList<String> treatments;
    ArrayAdapter treatmentsAdapter;

    DbHelper dbhelper;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_new_medical_record);

        dbhelper = new DbHelper(this);

        date = (EditText) findViewById(R.id.date);
        list_of_treatments = (ListView) findViewById(R.id.list_of_treatments);
        search_doctor = (AutoCompleteTextView) findViewById(R.id.search_doctor);
        complaint = (EditText) findViewById(R.id.complaint);
        diagnosis = (EditText) findViewById(R.id.diagnosis);
        add_treatment = (TextView) findViewById(R.id.add_treatment);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        doctors = dbhelper.getDoctorName();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, doctors);
        search_doctor.setAdapter(adapter);

        treatments = new ArrayList<String>();
        treatmentsAdapter = new ArrayAdapter(this, R.layout.treatments_list_layout, treatments);
        list_of_treatments.setAdapter(treatmentsAdapter);

        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        date.setOnClickListener(this);
        add_treatment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                if (date.getText().toString().equals("")) {
                    date.setError("Field required");
                }
                if (search_doctor.getText().toString().equals("")) {
                    search_doctor.setError("Field required");
                }
                if (complaint.getText().toString().equals("")) {
                    complaint.setError("Field required");
                }
                if (diagnosis.getText().toString().equals("")) {
                    diagnosis.setError("Field required");
                } else {
                    s_date = date.getText().toString();
                    s_date = date.getText().toString();
                    s_doctor = search_doctor.getText().toString();
                    s_complaint = complaint.getText().toString();
                    s_diagnosis = diagnosis.getText().toString();

                    if (doctors.contains(s_doctor)) {
                        Toast.makeText(this, "found doctor: " + s_doctor, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "no doctor found: " + s_doctor, Toast.LENGTH_SHORT).show();
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

                cancel_treatment.setOnClickListener(this);
                save_treatment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (generic_name.getText().toString().equals("")) {
                            generic_name.setError("Field required");
                        }
                        if (qty.getText().toString().equals("")) {
                            qty.setError("Field required");
                        }
                        if (dosage.getText().toString().equals("")) {
                            dosage.equals("Field required");
                        }
                        if (search_medicine.getText().toString().equals("")) {
                            search_medicine.setError("Field required");
                        } else {
                            s_generic_name = generic_name.getText().toString();
                            s_qty = qty.getText().toString();
                            s_dosage = dosage.getText().toString();
                            s_medicine = search_medicine.getText().toString();

                            treatments.add(s_medicine + " - " + s_dosage);
                            treatmentsAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    }
                });

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
}
