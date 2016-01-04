package com.example.zem.patientcareapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.example.zem.patientcareapp.AlarmModule.AlarmService;
import com.example.zem.patientcareapp.Controllers.DoctorController;
import com.example.zem.patientcareapp.Controllers.DbHelper;
import com.example.zem.patientcareapp.Controllers.PatientConsultationController;
import com.example.zem.patientcareapp.Model.Consultation;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.PostRequest;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.SidebarModule.SidebarActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class PatientConsultationActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener, CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener {
    DbHelper dbhelper;
    PatientConsultationController pcc;
    Consultation consult;
    AlarmService alarmService;

    LinearLayout setDate, setAlarmedTime;
    TextView txtDate, txtAlarmedTime;
    CheckBox checkAlarm;
    AutoCompleteTextView search_doctor;
    Spinner spinner_clinic;
    Toolbar myToolBar;

    Calendar cal;
    ArrayAdapter<String> doctorAdapter, clinicAdapter;

    ArrayList<HashMap<String, String>> doctorsHashmap, doctorClinicHashmap;
    ArrayList<String> listOfClinic, listOfDoctors;

    String request;
    int hour, minute, new_hour, new_min, isAlarm, doctorID = 0;
    static int doctor_id = 0;
    static String time_alarm = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_consultation_layout);

        myToolBar = (Toolbar) findViewById(R.id.myToolBar);
        setSupportActionBar(myToolBar);
        getSupportActionBar().setTitle("Set Consultation");

        dbhelper = new DbHelper(this);
        pcc = new PatientConsultationController(this);
        alarmService = new AlarmService(this);
        Intent getIntent = getIntent();
        listOfDoctors = new ArrayList();
        listOfClinic = new ArrayList();

        DoctorController doctor_controller = new DoctorController(this);

        doctorClinicHashmap = doctor_controller.getDoctorsInnerJoinClinics();
        doctorsHashmap = doctor_controller.getAllDoctors();

        setDate = (LinearLayout) findViewById(R.id.setDate);
        setAlarmedTime = (LinearLayout) findViewById(R.id.setAlarmedTime);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtAlarmedTime = (TextView) findViewById(R.id.txtAlarmedTime);
        checkAlarm = (CheckBox) findViewById(R.id.checkAlarm);
        search_doctor = (AutoCompleteTextView) findViewById(R.id.search_doctor);
        spinner_clinic = (Spinner) findViewById(R.id.spinner_clinic);

        cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);

        for (int i = 0; i < doctorsHashmap.size(); i++)
            listOfDoctors.add(doctorsHashmap.get(i).get("fullname"));

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

            if (hour > 12)
                txtAlarmedTime.setText((hour - 12) + " : " + minute + " PM");
            else
                txtAlarmedTime.setText(hour + " : " + minute + " AM");

            new_hour = hour;
            new_min = minute;

            String check_date;

            if (cal.get(Calendar.DATE) < 10)
                check_date = "0" + (cal.get(Calendar.DATE));
            else
                check_date = String.valueOf(cal.get(Calendar.DATE));

            txtDate.setText(cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + check_date);
        }

        listOfClinic.add("Choose a Doctor");
        clinicAdapter = new ArrayAdapter(this, R.layout.spinner_clinics_by_doctors, listOfClinic);
        spinner_clinic.setAdapter(clinicAdapter);

        search_doctor.addTextChangedListener(this);
        search_doctor.setOnItemClickListener(this);
        setDate.setOnClickListener(this);
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
        int check = 0;

        if (item.getItemId() == R.id.save) {
            if (search_doctor.getText().toString().equals("")) {
                search_doctor.setError("Field required");
                check += 1;
            } else if (listOfClinic.get(0).equals("Choose a Doctor")) {
                search_doctor.setError("Name not found");
                check += 1;
            }

            if (check == 0) {
                for (int i = 0; i < doctorClinicHashmap.size(); i++) {
                    if (doctorClinicHashmap.get(i).get("clinic_name").equals(spinner_clinic.getSelectedItem().toString()))
                        consult.setClinicID(Integer.parseInt(doctorClinicHashmap.get(i).get("clinics_id")));
                }

                consult.setPatientID(SidebarActivity.getUserID());
                consult.setDoctorID(doctor_id);
                consult.setDate(txtDate.getText().toString());
                consult.setIsAlarmed(isAlarm);
                consult.setAlarmedTime(time_alarm);
                consult.setIs_approved(0);
                consult.setIs_read(0);
                consult.setPtnt_is_approved(0);

                final HashMap<String, String> hashMap = new HashMap();
                hashMap.put("table", "consultations");
                hashMap.put("request", "crud");
                hashMap.put("action", "insert");
                hashMap.put("patient_id", String.valueOf(consult.getPatientID()));
                hashMap.put("doctor_id", String.valueOf(consult.getDoctorID()));
                hashMap.put("clinic_id", String.valueOf(consult.getClinicID()));
                hashMap.put("date", consult.getDate());
                hashMap.put("time", "");
                hashMap.put("is_alarm", String.valueOf(consult.getIsAlarmed()));
                hashMap.put("alarm_time", consult.getAlarmedTime());

                final ProgressDialog pdialog = new ProgressDialog(this);
                pdialog.setMessage("Please wait...");
                pdialog.show();

                PostRequest.send(this, hashMap, new RespondListener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject response) {
                        try {
                            int success = response.getInt("success");

                            if (success == 1) {
                                consult.setServerID(response.getInt("last_inserted_id"));
                                consult.setCreated_at(response.getString("created_at"));

                                if (pcc.savePatientConsultation(consult, request)) {
                                    alarmService.patientConsultationReminder();
                                    PatientConsultationActivity.this.finish();
                                    Toast.makeText(getBaseContext(), "Your request has been submitted. Please wait for your confirmation.", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(getBaseContext(), "Error while saving", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getBaseContext(), "Server error occurred", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(), e + "", Toast.LENGTH_SHORT).show();
                        }
                        pdialog.dismiss();
                    }
                }, new ErrorListener<VolleyError>() {
                    public void getError(VolleyError error) {
                        pdialog.dismiss();
                        Log.d("PatientConsultAct", error + "");
                        Toast.makeText(getBaseContext(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else
            this.finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setDate:
                FragmentManager fm = getSupportFragmentManager();
                CalendarDatePickerDialogFragment datepicker;
                String dateInView = txtDate.getText().toString();
                int month, year, day;

                int indexOfYear = dateInView.indexOf("-");
                int indexOfMonthandDay = dateInView.lastIndexOf("-");
                year = Integer.parseInt(dateInView.substring(0, indexOfYear));
                month = Integer.parseInt(dateInView.substring(indexOfYear + 1, indexOfMonthandDay)) - 1;
                day = Integer.parseInt(dateInView.substring(indexOfMonthandDay + 1, dateInView.length()));

                datepicker = CalendarDatePickerDialogFragment.newInstance(this, year, month, day);
                datepicker.setDateRange(new MonthAdapter.CalendarDay(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)), null);
                datepicker.show(fm, "fragment_date_picker_name");

                break;

            case R.id.setAlarmedTime:
                RadialTimePickerDialogFragment timePickerDialog = RadialTimePickerDialogFragment.newInstance(this, new_hour, minute, DateFormat.is24HourFormat(PatientConsultationActivity.this));

                timePickerDialog.show(getSupportFragmentManager(), "timePickerDialogFragment");
                break;
        }
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        String meridiem = "AM";
        new_hour = hourOfDay;
        new_min = minute;

        if (hourOfDay == 0)
            hourOfDay = 12;
        else if (hourOfDay == 12)
            meridiem = "PM";
        else if (hourOfDay > 12) {
            hourOfDay -= 12;
            meridiem = "PM";
        }
        if (minute < 10)
            txtAlarmedTime.setText(hourOfDay + " : 0" + minute + " " + meridiem);
        else
            txtAlarmedTime.setText(hourOfDay + " : " + minute + " " + meridiem);
    }


    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        String day;

        if (dayOfMonth < 10)
            day = "0" + dayOfMonth;
        else
            day = String.valueOf(dayOfMonth);

        String dateStr = year + "-" + (monthOfYear + 1) + "-" + day;
        txtDate.setText(dateStr);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item_clicked = parent.getItemAtPosition(position).toString();
        prepareSpinner(item_clicked);

        for (int i = 0; i < doctorsHashmap.size(); i++) {
            if (doctorsHashmap.get(i).get("fullname").equals(item_clicked))
                doctor_id = Integer.parseInt(doctorsHashmap.get(i).get("doc_id"));
        }
    }

    public void prepareSpinner(String doctor_name) {
        spinner_clinic.setVisibility(View.VISIBLE);
        listOfClinic.clear();

        for (int x = 0; x < doctorClinicHashmap.size(); x++) {
            if (doctor_name.equals(doctorClinicHashmap.get(x).get("fullname"))) {
                listOfClinic.add(doctorClinicHashmap.get(x).get("clinic_name"));
                clinicAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (checkAlarm.isChecked()) {
            setAlarmedTime.setVisibility(View.VISIBLE);
            isAlarm = 1;
            time_alarm = txtAlarmedTime.getText().toString();
        } else {
            setAlarmedTime.setVisibility(View.GONE);
            isAlarm = 0;
            time_alarm = "";
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String s_doctor = search_doctor.getText().toString();

        for (String doctor : listOfDoctors) {
            if (!doctor.toLowerCase().contains(s_doctor.toLowerCase())) {
                listOfClinic.clear();
                listOfClinic.add("Choose a Doctor");
                clinicAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
}
