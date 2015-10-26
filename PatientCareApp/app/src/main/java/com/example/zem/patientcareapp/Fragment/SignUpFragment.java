package com.example.zem.patientcareapp.Fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.example.zem.patientcareapp.DbHelper;
import com.example.zem.patientcareapp.EditTabsActivity;
import com.example.zem.patientcareapp.GetterSetter.Patient;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.SidebarActivity;

import java.util.Calendar;

/**
 * Created by Zem on 4/28/2015.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener, CalendarDatePickerDialogFragment.OnDateSetListener {

    public static EditText birthdate, fname, lname, mname, height, weight, occupation;
    public static Spinner civil_status_spinner;
    public static RadioButton male_rb, female_rb;
    public static View rootView;

    public static ArrayAdapter<String> civil_status_adapter;
    public static String[] civil_status_array = {
            "Single", "Married", "Widowed", "Separated", "Divorced"
    };
    String get_birthdate;
    public static int int_year, int_month, int_day;

    DbHelper dbhelper;

    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_signup_fragment, container, false);

        dbhelper = new DbHelper(getActivity());
        int edit = EditTabsActivity.edit_int;
        int signup = EditTabsActivity.signup_int;
        intent = EditTabsActivity.intent;
        get_birthdate = "";

        fname = (EditText) rootView.findViewById(R.id.fname);
        lname = (EditText) rootView.findViewById(R.id.lname);
        mname = (EditText) rootView.findViewById(R.id.mname);
        height = (EditText) rootView.findViewById(R.id.height);
        weight = (EditText) rootView.findViewById(R.id.weight);
        occupation = (EditText) rootView.findViewById(R.id.occupation);
        male_rb = (RadioButton) rootView.findViewById(R.id.male_rb);
        female_rb = (RadioButton) rootView.findViewById(R.id.female_rb);

        civil_status_spinner = (Spinner) rootView.findViewById(R.id.civil_status);
        civil_status_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, civil_status_array);
        civil_status_spinner.setAdapter(civil_status_adapter);

        birthdate = (EditText) rootView.findViewById(R.id.birthdate);
        birthdate.setOnClickListener(this);

        if (edit > 0) {
            String edit_uname = SidebarActivity.getUname();
            Patient patient = dbhelper.getloginPatient(edit_uname);
            get_birthdate = patient.getBirthdate();

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
        } else if (signup > 0) {
            if (intent.getExtras().getString("fname") != null) {
                get_birthdate = intent.getExtras().getString("birthdate");

                fname.setText(intent.getExtras().getString("fname"));
                mname.setText(intent.getExtras().getString("mname"));
                lname.setText(intent.getExtras().getString("lname"));
                birthdate.setText(intent.getExtras().getString("birthdate"));
                occupation.setText(intent.getExtras().getString("occupation"));
                height.setText(intent.getExtras().getString("height"));
                weight.setText(intent.getExtras().getString("weight"));
                civil_status_spinner.setSelection(civil_status_adapter.getPosition(intent.getExtras().getString("civil_status")));

                if (male_rb.getText().equals(intent.getExtras().getString("sex")))
                    male_rb.setChecked(true);
                else if (female_rb.getText().equals(intent.getExtras().getString("sex")))
                    female_rb.setChecked(true);
            }
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.birthdate:
                FragmentManager fm = getChildFragmentManager();
                Calendar now = Calendar.getInstance();
                CalendarDatePickerDialogFragment datepicker;

                if (get_birthdate.equals("")) {
                    datepicker = CalendarDatePickerDialogFragment
                            .newInstance(this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                } else {
                    int month, year, day;
                    String birth = birthdate.getText().toString();
                    int indexOfYear = birth.indexOf("-");
                    int indexOfMonthandDay = birth.lastIndexOf("-");
                    year = Integer.parseInt(birth.substring(0, indexOfYear));
                    month = Integer.parseInt(birth.substring(indexOfYear + 1, indexOfMonthandDay)) - 1;
                    day = Integer.parseInt(birth.substring(indexOfMonthandDay + 1, birth.length()));

                    datepicker = CalendarDatePickerDialogFragment
                            .newInstance(this, year, month, day);
                }
                datepicker.show(fm, "fragment_date_picker_name");
                break;
        }
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        String dateStr = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        birthdate.setText(dateStr);
        get_birthdate = dateStr;

        int_year = year;
        int_month = monthOfYear;
        int_day = dayOfMonth;

        Calendar calendar = Calendar.getInstance();
        int current_year = calendar.get(Calendar.YEAR);

        if ((current_year - year) < 18)
            birthdate.setError("Must be 18 years old and above");
        else
            birthdate.setError(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        CalendarDatePickerDialogFragment calendarDatePickerDialogFragment = (CalendarDatePickerDialogFragment) getChildFragmentManager()
                .findFragmentByTag("fragment_date_picker_name");
        if (calendarDatePickerDialogFragment != null) {
            calendarDatePickerDialogFragment.setOnDateSetListener(this);
        }
    }
}