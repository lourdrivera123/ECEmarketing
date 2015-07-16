package com.example.zem.patientcareapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class PatientConsultationFragment extends Fragment implements View.OnClickListener {
    ListView consultation_schedules;
    ImageButton add_consultation;
    TextView doctor_name, clinic_address, consultation_schedule;

    private ConsultationAdapter consultAdapter;
    DbHelper dbhelper;

    ArrayList<HashMap<String, String>> listOfAllConsultations;
    ArrayList<String> consultationDoctors;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.patient_consultation_layout, container, false);

        consultation_schedules = (ListView) rootView.findViewById(R.id.consultation_schedules);
        add_consultation = (ImageButton) rootView.findViewById(R.id.add_consultation);

        dbhelper = new DbHelper(getActivity());
        listOfAllConsultations = dbhelper.getAllConsultationsByUserId(HomeTileActivity.getUserID());
        consultationDoctors = new ArrayList();

        for (int x = 0; x < listOfAllConsultations.size(); x++) {
            consultationDoctors.add(listOfAllConsultations.get(x).get(dbhelper.CONSULT_DOCTOR));
        }
        consultAdapter = new ConsultationAdapter(getActivity(), R.layout.list_row_consultations, R.id.doctor_name, consultationDoctors);
        consultation_schedules.setAdapter(consultAdapter);

        add_consultation.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), PatientConsultationActivity.class);
        startActivity(intent);
    }

    private class ConsultationAdapter extends ArrayAdapter {

        public ConsultationAdapter(Context context, int resource, int textViewResourceId, ArrayList<String> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);//let the adapter handle setting up the row views
            Drawable d = getResources().getDrawable(R.drawable.list_selector);
            v.setBackgroundDrawable(d);

            doctor_name = (TextView) v.findViewById(R.id.doctor_name);
            clinic_address = (TextView) v.findViewById(R.id.clinic_address);
            consultation_schedule = (TextView) v.findViewById(R.id.consultation_schedule);

            doctor_name.setText(listOfAllConsultations.get(position).get(dbhelper.CONSULT_DOCTOR));
            clinic_address.setText(listOfAllConsultations.get(position).get(dbhelper.CONSULT_CLINIC));
            consultation_schedule.setText(listOfAllConsultations.get(position).get(dbhelper.CONSULT_DATE) + ", " + listOfAllConsultations.get(position).get(dbhelper.CONSULT_PART_OF_DAY));

            return v;
        }
    }
}
