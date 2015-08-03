package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.zem.patientcareapp.GetterSetter.Consultation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class PatientConsultationActivity extends Activity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener {
    DbHelper dbhelper;
    Consultation consult;
    AlarmService alarmService;

    LinearLayout setDate, setTime;
    TextView txtDate, txtTime;
    CheckBox checkAlarm;
    AutoCompleteTextView search_doctor, search_clinic;
    Spinner spinner_clinic, spin_dayTime;

    Calendar cal;
    ArrayAdapter<String> doctorAdapter;
    ArrayAdapter<String> clinicAdapter;
    ArrayAdapter<String> partOfDayAdapter;

    ArrayList<HashMap<String, String>> doctorsHashmap;
    ArrayList<HashMap<String, String>> doctorClinicHashmap;
    ArrayList<String> listOfClinic;
    ArrayList<String> listOfDoctors;
    String request;
    String[] partOfDay = new String[]{
            "Morning", "Afternoon"
    };

    int hour, minute, new_hour, new_minute;
    int isAlarm, updateID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_consultation_layout);

        ActionBar actionBar = getActionBar();
        MainActivity.setCustomActionBar(actionBar);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbhelper = new DbHelper(this);
        alarmService = new AlarmService(this);
        Intent getIntent = getIntent();

        setDate = (LinearLayout) findViewById(R.id.setDate);
        setTime = (LinearLayout) findViewById(R.id.setTime);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtTime = (TextView) findViewById(R.id.txtTime);
        checkAlarm = (CheckBox) findViewById(R.id.checkAlarm);
        search_doctor = (AutoCompleteTextView) findViewById(R.id.search_doctor);
        search_clinic = (AutoCompleteTextView) findViewById(R.id.search_clinic);
        spinner_clinic = (Spinner) findViewById(R.id.spinner_clinic);
        spin_dayTime = (Spinner) findViewById(R.id.spin_dayTime);

        cal = Calendar.getInstance();
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);

        if (getIntent.getStringExtra("request").equals("add")) {
            request = "add";
            consult = new Consultation();

            txtTime.setText(hour + " : " + minute);
            txtDate.setText((cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR));
        } else {
            request = "update";
            updateID = Integer.parseInt(getIntent.getStringExtra("updateID"));
            consult = dbhelper.getConsultationById(updateID, SidebarActivity.getUserID());

            txtDate.setText(consult.getDate());
            search_doctor.setText(consult.getDoctor());
            search_clinic.setText(consult.getClinic());

            if (consult.getIsAlarmed() == 1) {
                checkAlarm.setChecked(true);
                setTime.setVisibility(View.VISIBLE);
            }
        }


        doctorClinicHashmap = dbhelper.getAllDoctorClinic();
        doctorsHashmap = dbhelper.getAllDoctors();
        listOfDoctors = new ArrayList();

        for (int i = 0; i < doctorsHashmap.size(); i++) {
            listOfDoctors.add(doctorsHashmap.get(i).get("fullname"));
        }

        partOfDayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, partOfDay);
        doctorAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listOfDoctors);

        search_doctor.setAdapter(doctorAdapter);
        spin_dayTime.setAdapter(partOfDayAdapter);
        search_doctor.addTextChangedListener(this);
        search_doctor.setOnItemClickListener(this);
        setDate.setOnClickListener(this);
        setTime.setOnClickListener(this);
        checkAlarm.setOnCheckedChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            if (search_doctor.getText().toString().equals("")) {
                search_doctor.setError("Field required");
            } else if (search_clinic.getText().toString().equals("") && search_clinic.getVisibility() == View.VISIBLE) {
                search_clinic.setError("Field Required");
            } else {
                if (search_clinic.getVisibility() == View.VISIBLE && !search_clinic.getText().toString().equals("")) {
                    consult.setClinic(search_clinic.getText().toString());
                } else {
                    consult.setClinic(spinner_clinic.getSelectedItem().toString());
                }

                if (request.equals("update"))
                    consult.setId(updateID);

                consult.setPatientID(SidebarActivity.getUserID());
                consult.setDoctor(search_doctor.getText().toString());
                consult.setDate(txtDate.getText().toString());
                consult.setPartOfDay(spin_dayTime.getSelectedItem().toString());
                consult.setIsAlarmed(isAlarm);
                consult.setTime(txtTime.getText().toString());
                consult.setIsFinished(0);

                if (dbhelper.savePatientConsultation(consult, request)) {
                    alarmService.patientConsultationReminder();
                    Intent intent = new Intent(this, MasterTabActivity.class);
                    intent.putExtra("selected", 4);
                    startActivity(intent);
                    this.finish();
                } else {
                    Toast.makeText(this, "Error while saving", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setDate:
                String dateInView = txtDate.getText().toString();

                int month, year, day;
                int indexOfMonthandDay = dateInView.indexOf("/");
                int indexOfYear = dateInView.lastIndexOf("/");
                year = Integer.parseInt(dateInView.substring(indexOfYear + 1, dateInView.length()));
                month = Integer.parseInt(dateInView.substring(0, indexOfMonthandDay));
                day = Integer.parseInt(dateInView.substring(indexOfMonthandDay + 1, indexOfYear));

                updateDate(year, month - 1, day);
                break;

            case R.id.setTime:
                if (txtTime.getText().toString().equals(hour + " : " + minute)) {
                    TimePickerDialog mTimePicker = new TimePickerDialog(this, onStartTimeListener, hour, minute, false);
                    mTimePicker.show();
                } else {
                    TimePickerDialog mTimePicker = new TimePickerDialog(this, onStartTimeListener, new_hour, new_minute, false);
                    mTimePicker.show();
                }
                break;
        }
    }

    TimePickerDialog.OnTimeSetListener onStartTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String meridiem = "AM";

            if (hourOfDay == 12) {
                meridiem = "PM";
            } else if (hourOfDay > 11) {
                hourOfDay -= 12;
                meridiem = "PM";
            }
            if (minute < 10)
                txtTime.setText(hourOfDay + " : 0" + minute + " " + meridiem);
            else
                txtTime.setText(hourOfDay + " : " + minute + " " + meridiem);

            new_hour = hourOfDay;
            new_minute = minute;
        }
    };

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String dateStr = String.format("%d/%d/%d", (monthOfYear + 1), dayOfMonth, year);
        txtDate.setText(dateStr);
    }

    public void updateDate(int year, int monthOfYear, int dayOfMonth) {
        DatePickerDialog datePicker = new DatePickerDialog(this, this, year, monthOfYear, dayOfMonth);
        datePicker.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        spinner_clinic.setVisibility(View.VISIBLE);
        search_clinic.setVisibility(View.GONE);

        String item_clicked = parent.getItemAtPosition(position).toString();
        listOfClinic = new ArrayList();
        clinicAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listOfClinic);
        spinner_clinic.setAdapter(clinicAdapter);

        for (int x = 0; x < doctorClinicHashmap.size(); x++) {
            if (item_clicked.equals(doctorClinicHashmap.get(x).get("fullname"))) {
                listOfClinic.add(doctorClinicHashmap.get(x).get("clinic_name"));
                clinicAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String s_doctor = search_doctor.getText().toString();

        for (String doctor : listOfDoctors) {
            if (!doctor.toLowerCase().contains(s_doctor.toLowerCase())) {
                spinner_clinic.setVisibility(View.GONE);
                search_clinic.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (checkAlarm.isChecked()) {
            setTime.setVisibility(View.VISIBLE);
            isAlarm = 1;
        } else {
            setTime.setVisibility(View.GONE);
            isAlarm = 0;
        }
    }
}
