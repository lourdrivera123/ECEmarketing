package com.example.zem.patientcareapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Zem on 4/29/2015. Update by Esel 5/5/2015
 */
public class PatientProfileFragment extends Fragment implements View.OnClickListener {
    Button edit_patient_profile_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.patient_profile_layout, container, false);

        edit_patient_profile_btn = (Button) rootView.findViewById(R.id.edit_patient_profile_btn);
        edit_patient_profile_btn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), EditTabsActivity.class));
    }
}
