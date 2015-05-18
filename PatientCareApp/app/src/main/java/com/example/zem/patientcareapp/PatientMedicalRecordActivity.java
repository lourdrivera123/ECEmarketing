package com.example.zem.patientcareapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by User PC on 5/18/2015.
 */
public class PatientMedicalRecordActivity extends ActionBarActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    EditText date, complaint, diagnosis, treatment;
    AutoCompleteTextView search_doctor;
    Button btn_save, btn_cancel;
    String s_date, s_doctor, s_complaint, s_diagnosis, s_treatment;
    String[] doctors = {
            "Zemiel Asma", "Esel Barnes", "Dexter Bengil"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_new_medical_record);

        date = (EditText) findViewById(R.id.date);
        search_doctor = (AutoCompleteTextView) findViewById(R.id.search_doctor);
        complaint = (EditText) findViewById(R.id.complaint);
        diagnosis = (EditText) findViewById(R.id.diagnosis);
        treatment = (EditText) findViewById(R.id.treatment);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, doctors);
        search_doctor.setAdapter(adapter);

        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        date.setOnClickListener(this);
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
                }
                if (treatment.getText().toString().equals("")) {
                    treatment.setError("Field required");
                } else {
                    s_date = date.getText().toString();
                    s_date = date.getText().toString();
                    s_doctor = search_doctor.getText().toString();
                    s_complaint = complaint.getText().toString();
                    s_diagnosis = diagnosis.getText().toString();
                    s_treatment = treatment.getText().toString();
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
