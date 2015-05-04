package com.example.zem.patientcareapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by Zem on 4/28/2015.
 */
public class SignUpActivity extends ActionBarActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    EditText birthdate;
    Button next_to_contact_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_general_info_layout);

        birthdate = (EditText) findViewById(R.id.birthdate);
        birthdate.setOnClickListener(this);

        next_to_contact_btn = (Button) findViewById(R.id.next_to_contact_btn);
        next_to_contact_btn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save) {
            //validate diri then adtu sa account info
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Calendar cal = Calendar.getInstance();

        switch(v.getId()){

            case R.id.birthdate:

                //execute date picker dialog
                DatePickerDialog datePicker = new DatePickerDialog(this, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePicker.show();

                break;

            case R.id.next_to_contact_btn:
               startActivity(new Intent(this, ContactActivity.class));
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String dateStr = String.format("%d/%d/%d",(month+1),day,year);
        birthdate.setText(dateStr);
    }
}
