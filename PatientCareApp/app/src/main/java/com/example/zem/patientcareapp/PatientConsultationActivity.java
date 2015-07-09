package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;

public class PatientConsultationActivity extends Activity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    LazyAdapter adapter;

    LinearLayout setDate, setTime;
    TextView txtDate, txtTime;
    CheckBox checkAlarm;

    Calendar cal;

    int hour, minute, new_hour, new_minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_consultation_layout);

        ActionBar actionBar = getActionBar();
        MainActivity.setCustomActionBar(actionBar);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setDate = (LinearLayout) findViewById(R.id.setDate);
        setTime = (LinearLayout) findViewById(R.id.setTime);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtTime = (TextView) findViewById(R.id.txtTime);
        checkAlarm = (CheckBox) findViewById(R.id.checkAlarm);

        cal = Calendar.getInstance();
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        txtTime.setText(hour + " : " + minute);

        setDate.setOnClickListener(this);
        setTime.setOnClickListener(this);
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

        } else {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setDate:
                DatePickerDialog datePicker = new DatePickerDialog(this, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePicker.show();
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
            if (hourOfDay > 12) {
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
}
