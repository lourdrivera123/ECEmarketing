package com.example.zem.patientcareapp;

import android.app.DatePickerDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Zem on 4/28/2015.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public static EditText birthdate, fname, lname, mname, height, weight, occupation;
    public static Spinner civil_status_spinner;
    public static RadioGroup sex;
    public static RadioButton male_rb, female_rb;
    public static View rootView;

    public static ArrayAdapter<String> civil_status_adapter;
    public static String[] civil_status_array = {
            "Single", "Married", "Widowed", "Separated", "Divorced"
    };
    public static int int_year, int_month, int_day;

    DbHelper dbhelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.patient_general_info_layout, container, false);

        dbhelper = new DbHelper(getActivity());
        int edit = EditTabsActivity.edit_int;

        fname = (EditText) rootView.findViewById(R.id.fname);
        lname = (EditText) rootView.findViewById(R.id.lname);
        mname = (EditText) rootView.findViewById(R.id.mname);
        height = (EditText) rootView.findViewById(R.id.height);
        weight = (EditText) rootView.findViewById(R.id.weight);
        occupation = (EditText) rootView.findViewById(R.id.occupation);
        male_rb = (RadioButton) rootView.findViewById(R.id.male_rb);
        female_rb = (RadioButton) rootView.findViewById(R.id.female_rb);

        civil_status_spinner = (Spinner) rootView.findViewById(R.id.civil_status);
        civil_status_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, civil_status_array);
        civil_status_spinner.setAdapter(civil_status_adapter);

        birthdate = (EditText) rootView.findViewById(R.id.birthdate);
        birthdate.setOnClickListener(this);

        if (edit > 0) {
            String edit_uname = HomeTileActivity.getUname();
            Patient patient = dbhelper.getloginPatient(edit_uname);

            fname.setText(patient.getFname());
            lname.setText(patient.getLname());
            mname.setText(patient.getMname());
            birthdate.setText(patient.getBirthdate());
            occupation.setText(patient.getOccupation());
            height.setText(patient.getHeight());
            weight.setText(patient.getWeight());
            civil_status_spinner.setSelection(civil_status_adapter.getPosition(patient.getCivil_status()));

            if (male_rb.getText().equals(patient.getSex())) {
                male_rb.setChecked(true);
                female_rb.setChecked(false);
            } else if (female_rb.getText().equals(patient.getSex())) {
                female_rb.setChecked(true);
                male_rb.setChecked(false);
            }
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        Calendar cal = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.birthdate:
                if (birthdate.getText().toString().equals("")) {
                    DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                    datePicker.show();
                } else {
                    int month, year, day;
                    String birth = birthdate.getText().toString();
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
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String dateStr = String.format("%d/%d/%d", (month + 1), day, year);
        birthdate.setText(dateStr);
        int_year = year;
        int_month = month;
        int_day = day;
    }

    public void updateDate(int year, int monthOfYear, int dayOfMonth) {
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this, year, monthOfYear, dayOfMonth);
        datePicker.show();
    }
}