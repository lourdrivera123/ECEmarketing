package com.example.zem.patientcareapp.Fragment;

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

import com.example.zem.patientcareapp.DbHelper;
import com.example.zem.patientcareapp.GetterSetter.Consultation;
import com.example.zem.patientcareapp.PatientConsultationActivity;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.SidebarActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class PatientConsultationFragment extends Fragment implements View.OnClickListener {
    ListView listOfConsultations;
    ImageButton add_consultation;
    TextView doctor_name, clinic_address, consultation_schedule;

    private ConsultationAdapter consultAdapter;
    Consultation consult;
    DbHelper dbhelper;

    ArrayList<HashMap<String, String>> listOfAllConsultations;
    ArrayList<String> consultationDoctors;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patient_consultation_fragment, container, false);

        listOfConsultations = (ListView) rootView.findViewById(R.id.consultation_schedules);
        add_consultation = (ImageButton) rootView.findViewById(R.id.add_consultation);

        add_consultation.setOnClickListener(this);
        listOfConsultations.setOnCreateContextMenuListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        dbhelper = new DbHelper(getActivity());
        listOfAllConsultations = dbhelper.getAllConsultationsByUserId(SidebarActivity.getUserID());
        consultationDoctors = new ArrayList();
        consult = new Consultation();

        consultAdapter = new ConsultationAdapter(getActivity(), R.layout.list_row_consultations, listOfAllConsultations);
        listOfConsultations.setAdapter(consultAdapter);

        super.onResume();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), PatientConsultationActivity.class);
        intent.putExtra("request", "add");
        intent.putExtra("doctorID", 0);
        startActivity(intent);
    }

    private class ConsultationAdapter extends ArrayAdapter {
        LayoutInflater inflater;

        public ConsultationAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
            super(context, resource, objects);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = inflater.inflate(R.layout.list_row_consultations, parent, false);

            Drawable d = getResources().getDrawable(R.drawable.list_selector);
            v.setBackgroundDrawable(d);

            doctor_name = (TextView) v.findViewById(R.id.doctor_name);
            clinic_address = (TextView) v.findViewById(R.id.clinic_address);
            consultation_schedule = (TextView) v.findViewById(R.id.consultation_schedule);

//            doctor_name.setText("Dr. " + listOfAllConsultations.get(position).get(DbHelper.CONSULT_DOCTOR));
//            clinic_address.setText(listOfAllConsultations.get(position).get(DbHelper.CONSULT_CLINIC));
            consultation_schedule.setText(listOfAllConsultations.get(position).get(DbHelper.CONSULT_DATE) + ", " + listOfAllConsultations.get(position).get(dbhelper.CONSULT_TIME));

            return v;
        }
    }
}
