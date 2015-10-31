package com.example.zem.patientcareapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import java.util.TimeZone;

public class PatientConsultationActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener {
    DbHelper dbhelper;
    Consultation consult;
    AlarmService alarmService;

    LinearLayout setDate, setTime, setAlarmedTime;
    TextView txtDate, txtTime, txtAlarmedTime;
    CheckBox checkAlarm;
    AutoCompleteTextView search_doctor, search_clinic;
    Spinner spinner_clinic;
    Toolbar myToolBar;

    Calendar cal;
    ArrayAdapter<String> doctorAdapter, clinicAdapter;

    ArrayList<HashMap<String, String>> doctorsHashmap, doctorClinicHashmap;
    ArrayList<String> listOfClinic, listOfDoctors;

    String request, current_hour, current_meridiem;
    int hour, minute, new_hour, hourTime_new, minuteTime_new, new_minute, isAlarm, updateID, doctorID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_consultation_layout);

        myToolBar = (Toolbar) findViewById(R.id.myToolBar);
        setSupportActionBar(myToolBar);
        getSupportActionBar().setTitle("Set Consultation");

        dbhelper = new DbHelper(this);
        alarmService = new AlarmService(this);
        Intent getIntent = getIntent();

        setDate = (LinearLayout) findViewById(R.id.setDate);
        setTime = (LinearLayout) findViewById(R.id.setTime);
        setAlarmedTime = (LinearLayout) findViewById(R.id.setAlarmedTime);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtAlarmedTime = (TextView) findViewById(R.id.txtAlarmedTime);
        checkAlarm = (CheckBox) findViewById(R.id.checkAlarm);
        search_doctor = (AutoCompleteTextView) findViewById(R.id.search_doctor);
        search_clinic = (AutoCompleteTextView) findViewById(R.id.search_clinic);
        spinner_clinic = (Spinner) findViewById(R.id.spinner_clinic);

        cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);

        doctorClinicHashmap = dbhelper.getAllActiveClinics();
        doctorsHashmap = dbhelper.getAllDoctors();
        listOfDoctors = new ArrayList();

        for (int i = 0; i < doctorsHashmap.size(); i++) {
            listOfDoctors.add(doctorsHashmap.get(i).get("fullname"));
        }

        doctorAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listOfDoctors);

        search_doctor.setAdapter(doctorAdapter);

        if (getIntent.getStringExtra("request").equals("add")) {
            String doctor = "";
            if (getIntent.getIntExtra("doctorID", 0) > 0) {
                doctorID = getIntent.getIntExtra("doctorID", 0);

                for (int i = 0; i < doctorsHashmap.size(); i++) {
                    if (Integer.parseInt(doctorsHashmap.get(i).get("doc_id")) == doctorID) {
                        doctor = doctorsHashmap.get(i).get("fullname");
                        search_doctor.setText(doctor);
                    }
                }
                prepareSpinner(doctor);
            }

            request = "add";
            consult = new Consultation();

            if (hour > 12) {
                current_meridiem = " PM";
                int n_hour = hour - 12;
                Log.d("time nisulod", n_hour + "");
                current_hour = n_hour + " : " + minute + " PM";
                txtTime.setText((hour - 12) + " : " + minute + " PM");
                txtAlarmedTime.setText((hour - 12) + " : " + minute + " PM");
            } else {
                current_meridiem = " AM";
                current_hour = hour + " : " + minute + " AM";
                txtTime.setText(hour + " : " + minute + " AM");
                txtAlarmedTime.setText(hour + " : " + minute + " AM");
            }

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
                setAlarmedTime.setVisibility(View.VISIBLE);
            }
        }

        search_doctor.addTextChangedListener(this);
        search_doctor.setOnItemClickListener(this);
        setDate.setOnClickListener(this);
        setTime.setOnClickListener(this);
        setAlarmedTime.setOnClickListener(this);
        checkAlarm.setOnCheckedChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_and_cancel, menu);
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
                consult.setTime(txtTime.getText().toString());
                consult.setIsAlarmed(isAlarm);
                consult.setAlarmedTime(txtAlarmedTime.getText().toString());
                consult.setIsFinished(0);

                if (dbhelper.savePatientConsultation(consult, request)) {
                    alarmService.patientConsultationReminder();
                    Intent intent = new Intent(this, MasterTabActivity.class);
                    intent.putExtra("selected", 4);
                    startActivity(intent);
                    this.finish();
                } else
                    Toast.makeText(this, "Error while saving", Toast.LENGTH_SHORT).show();
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

            case R.id.setAlarmedTime:

                Log.d("time sa txtview", current_hour);
                Log.d("time na current time", hour + " : " + minute + current_meridiem);
                if (current_hour.equals(hour + " : " + minute + current_meridiem)) {
                    Toast.makeText(PatientConsultationActivity.this, "true", Toast.LENGTH_SHORT).show();

                    TimePickerDialog alarmedPicker = new TimePickerDialog(this, onStartTimeListener, hour, minute, false);
                    alarmedPicker.show();
                } else {
                    Toast.makeText(PatientConsultationActivity.this, "false", Toast.LENGTH_SHORT).show();

                    TimePickerDialog alarmedPicker = new TimePickerDialog(this, onStartTimeListener, new_hour, new_minute, false);
                    alarmedPicker.show();
                }
                break;

            case R.id.setTime:
                TimePickerDialog mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String meridiem = "AM";

                        if (hourOfDay == 12) {
                            meridiem = "PM";
                        } else if (hourOfDay > 12) {
                            hourOfDay -= 12;
                            meridiem = "PM";
                        }
                        if (minute < 10)
                            txtTime.setText(hourOfDay + " : 0" + minute + " " + meridiem);
                        else
                            txtTime.setText(hourOfDay + " : " + minute + " " + meridiem);

                        hourTime_new = hourOfDay;
                        minuteTime_new = minute;
                    }
                }, hour, minute, false);
                mTimePicker.show();
                break;
        }
    }

    TimePickerDialog.OnTimeSetListener onStartTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String meridiem = "AM";

            if (hourOfDay == 12) {
                meridiem = "PM";
            } else if (hourOfDay > 12) {
                hourOfDay -= 12;
                meridiem = "PM";
            }
            if (minute < 10)
                txtAlarmedTime.setText(hourOfDay + " : 0" + minute + " " + meridiem);
            else
                txtAlarmedTime.setText(hourOfDay + " : " + minute + " " + meridiem);

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
        String item_clicked = parent.getItemAtPosition(position).toString();
        prepareSpinner(item_clicked);
    }

    public void prepareSpinner(String doctor_name) {
        spinner_clinic.setVisibility(View.VISIBLE);
        search_clinic.setVisibility(View.GONE);

        listOfClinic = new ArrayList();
        clinicAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listOfClinic);
        spinner_clinic.setAdapter(clinicAdapter);

        for (int x = 0; x < doctorClinicHashmap.size(); x++) {
            if (doctor_name.equals(doctorClinicHashmap.get(x).get("fullname"))) {
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
            setAlarmedTime.setVisibility(View.VISIBLE);
            isAlarm = 1;
        } else {
            setAlarmedTime.setVisibility(View.GONE);
            isAlarm = 0;
        }
    }
}
