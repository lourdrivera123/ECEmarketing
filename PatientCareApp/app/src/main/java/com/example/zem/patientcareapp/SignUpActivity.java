package com.example.zem.patientcareapp;

import com.example.zem.patientcareapp.R;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by Zem on 4/28/2015.
 */
public class SignUpActivity extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.patient_general_info_layout, container, false);

//        birthdate = (EditText) getView().findViewById(R.id.birthdate);
//        birthdate.setOnClickListener(this);

        return rootView;
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.save) {
//            //validate diri then adtu sa account info
//            Intent intent = new Intent(this, AccountActivity.class);
//            startActivity(intent);
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onClick(View v) {
//        Calendar cal = Calendar.getInstance();

//            case R.id.birthdate:
//
//                DialogFragment newFragment = new SelectDateFragment();
//                newFragment.show(getFragmentManager(), "DatePicker");
//
//                //execute date picker dialog
//                DatePickerDialog datePicker = new DatePickerDialog(this, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
//                datePicker.show();
//
//                break;
    }

//    @Override
//    public void onDateSet(DatePicker view, int year, int month, int day) {
//        String dateStr = String.format("%d/%d/%d", (month + 1), day, year);
//        birthdate.setText(dateStr);
//    }
}

//class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        final Calendar calendar = Calendar.getInstance();
//        int yy = calendar.get(Calendar.YEAR);
//        int mm = calendar.get(Calendar.MONTH);
//        int dd = calendar.get(Calendar.DAY_OF_MONTH);
//        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
//    }
//
//    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
//        populateSetDate(yy, mm + 1, dd);
//    }
//
//    public void populateSetDate(int year, int month, int day) {
//        //  birthdate.setText(month+"/"+day+"/"+year);
//    }
//
//}