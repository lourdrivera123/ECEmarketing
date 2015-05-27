package com.example.zem.patientcareapp;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class PatientHistoryFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    ListView list_of_history;
    TextView add_record, noResults;
    Button view_doctor_btn, call_doctor_btn;

    TextView date, doctor_name;
    EditText complaints, findings, treatments;

    ArrayAdapter<String> history_adapter;
    ArrayList<HashMap<String, String>> hashHistory;
    ArrayList<HashMap<String, String>> hashTreatments;
    ArrayList<String> medRecords;
    ArrayList<String> arrayOfRecords;
    public static final int new_treatment_request = 10;

    DbHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.patient_records_layout, container, false);
        dbHelper = new DbHelper(getActivity());

        hashHistory = dbHelper.getPatientRecord(HomeTileActivity.getUserID());
        medRecords = new ArrayList<String>();
        arrayOfRecords = new ArrayList<String>();

        for (int x = 0; x < hashHistory.size(); x++) {
            medRecords.add(hashHistory.get(x).get("doctor_name") + " - " + hashHistory.get(x).get("record_date"));
        }

        add_record = (TextView) rootView.findViewById(R.id.add_record);
        noResults = (TextView) rootView.findViewById(R.id.noResults);
        list_of_history = (ListView) rootView.findViewById(R.id.list_of_history);

        if (hashHistory.size() == 0) {
            noResults.setVisibility(View.VISIBLE);
        }

        history_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, medRecords);
        list_of_history.setAdapter(history_adapter);
        list_of_history.setOnItemClickListener(this);

        add_record.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Medical Records");
        dialog.setContentView(R.layout.patient_diagnosis_layout);
        dialog.show();

        view_doctor_btn = (Button) dialog.findViewById(R.id.view_doctor_btn);
        call_doctor_btn = (Button) dialog.findViewById(R.id.call_doctor_btn);
        date = (TextView) dialog.findViewById(R.id.date);
        doctor_name = (TextView) dialog.findViewById(R.id.doctor_name);
        complaints = (EditText) dialog.findViewById(R.id.complaints);
        findings = (EditText) dialog.findViewById(R.id.findings);
        treatments = (EditText) dialog.findViewById(R.id.treatments);

        date.setText(hashHistory.get(position).get("record_date"));
        complaints.setText(hashHistory.get(position).get("complaints"));
        findings.setText(hashHistory.get(position).get("findings"));
        doctor_name.setText(hashHistory.get(position).get("doctor_name"));

        hashTreatments = dbHelper.getTreatmentRecord(Integer.parseInt(hashHistory.get(position).get("recordID")));
        if (hashTreatments.size() > 0) {
            for (int x = 0; x < hashTreatments.size(); x++) {
                arrayOfRecords.add(hashTreatments.get(x).get("medicine_name") + " - " + hashTreatments.get(x).get("prescription"));
            }
            treatments.setText("" + arrayOfRecords);
        }

        call_doctor_btn.setOnClickListener(this);
        view_doctor_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record:
                Intent intent = new Intent(getActivity(), PatientMedicalRecordActivity.class);
                startActivity(intent);

                break;
            case R.id.view_doctor_btn:
                startActivity(new Intent(getActivity(), DoctorActivity.class));
                break;

            case R.id.call_doctor_btn:
                String phoneNumber = "09095331440";
                String uriTel = "tel:" + phoneNumber;

                //Present you with the dialler
                Uri telUri = Uri.parse(uriTel);
                Intent returnIt = new Intent(Intent.ACTION_DIAL, telUri);
                startActivity(returnIt);

                break;
        }
    }

}
