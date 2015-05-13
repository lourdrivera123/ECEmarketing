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
import com.example.zem.patientcareapp.adapter.MasterTabsAdapter;

import java.util.Calendar;

/**
 * Created by Zem on 4/28/2015.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    EditText birthdate;
    Spinner civil_status_spinner;

    ArrayAdapter<String> civil_status_adapter;
    String[] civil_status_array = {
            "Married", "Widowed", "Separated", "Divorced", "Single"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.patient_general_info_layout, container, false);

        birthdate = (EditText) rootView.findViewById(R.id.birthdate);
        civil_status_spinner = (Spinner) rootView.findViewById(R.id.civil_status);
        civil_status_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, civil_status_array);
        civil_status_spinner.setAdapter(civil_status_adapter);

        birthdate.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        Calendar cal = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.birthdate:
                //execute date picker dialog
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePicker.show();

                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String dateStr = String.format("%d/%d/%d", (month + 1), day, year);
        birthdate.setText(dateStr);
    }
}