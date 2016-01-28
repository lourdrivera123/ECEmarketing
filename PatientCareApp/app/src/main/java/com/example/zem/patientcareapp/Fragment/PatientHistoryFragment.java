package com.example.zem.patientcareapp.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.Controllers.DbHelper;
import com.example.zem.patientcareapp.ConfigurationModule.Helpers;
import com.example.zem.patientcareapp.Controllers.PatientRecordController;
import com.example.zem.patientcareapp.Controllers.PatientTreatmentsController;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Model.PatientRecord;
import com.example.zem.patientcareapp.Network.ListRequestFromCustomURI;
import com.example.zem.patientcareapp.Network.PostRequest;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.Activities.SaveMedicalRecordActivity;
import com.example.zem.patientcareapp.SidebarModule.SidebarActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PatientHistoryFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    ListView list_of_history;
    ImageButton add_record;
    TextView noResults;
    LinearLayout root;

    ArrayList<HashMap<String, String>> hashHistory;
    ArrayList<String> arrayOfRecords;
    ArrayList<Integer> selectedList;

    private SelectionAdapter mAdapter;

    DbHelper dbHelper;
    PatientRecordController prc;
    PatientTreatmentsController ptc;
    Helpers helpers;
    Dialog dialog;
    Dialog dialog2;
    ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patient_records, container, false);

        dbHelper = new DbHelper(getActivity());
        prc = new PatientRecordController(getActivity());
        ptc = new PatientTreatmentsController(getActivity());
        helpers = new Helpers();
        hashHistory = prc.getPatientRecord();
        arrayOfRecords = new ArrayList();
        selectedList = new ArrayList();

        add_record = (ImageButton) rootView.findViewById(R.id.add_record);
        noResults = (TextView) rootView.findViewById(R.id.noResults);
        list_of_history = (ListView) rootView.findViewById(R.id.list_of_history);
        root = (LinearLayout) rootView.findViewById(R.id.root);

        if (hashHistory.size() == 0) {
            noResults.setVisibility(View.VISIBLE);
            list_of_history.setVisibility(View.GONE);
        }

        mAdapter = new SelectionAdapter(getActivity(), R.layout.listview_history_views, hashHistory);
        list_of_history.setAdapter(mAdapter);

        list_of_history.setOnItemClickListener(this);
        add_record.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        view_record_id = Integer.parseInt(hashHistory.get(position).get(dbHelper.AI_ID));
//
//        Intent view_record = new Intent(getActivity(), SaveMedicalRecordActivity.class);
//        view_record.putExtra("viewRecord", view_record_id);
//        startActivity(view_record);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record:
                dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
                dialog.setContentView(R.layout.add_new_med_records);

                ImageButton clinicRecord = (ImageButton) dialog.findViewById(R.id.clinicRecord);
                clinicRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        dialog2 = new Dialog(getActivity());
                        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog2.setContentView(R.layout.get_clinic_record);
                        dialog2.show();

                        final EditText username = (EditText) dialog2.findViewById(R.id.username);
                        final EditText password = (EditText) dialog2.findViewById(R.id.password);
                        Button submitBtn = (Button) dialog2.findViewById(R.id.submitBtn);

                        password.setTransformationMethod(new PasswordTransformationMethod());

                        submitBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (username.getText().toString().equals(""))
                                    username.setError("Field required");
                                else if (password.getText().toString().equals(""))
                                    password.setError("Field required");
                                else {
                                    progress = new ProgressDialog(getActivity());
                                    progress.setMessage("Please wait...");
                                    progress.show();

                                    String uname = username.getText().toString();
                                    String pword = password.getText().toString();
                                    String url = "db/get.php?q=get_clinic_records&username=" + uname + "&password=" + pword + "&patient_id=" + SidebarActivity.getUserID();

                                    ListRequestFromCustomURI.getJSONobj(getActivity(), url, "records", new RespondListener<JSONObject>() {
                                        @Override
                                        public void getResult(JSONObject response) {
                                            try {
                                                int success = response.getInt("success");

                                                if (success == 1) {
                                                    int hasRecord = response.getInt("has_record");
                                                    JSONArray json_mysql = response.getJSONArray("records");

                                                    if (hasRecord == 1)
                                                        Snackbar.make(root, "NAA NAKA ANI NGA RECORD", Snackbar.LENGTH_SHORT).show();
                                                    else
                                                        insertHistory(json_mysql);
                                                } else
                                                    Snackbar.make(root, "Invalid credentials", Snackbar.LENGTH_SHORT).show();
                                            } catch (Exception e) {
                                                Log.e("patientHistoryFrag0", e + "");
                                                Snackbar.make(root, "Server error occurred", Snackbar.LENGTH_SHORT).show();
                                            }
                                            progress.dismiss();
                                        }
                                    }, new ErrorListener<VolleyError>() {
                                        @Override
                                        public void getError(VolleyError e) {
                                            progress.dismiss();
                                            Log.d("patientHistoryFrag1", e + "");
                                            Snackbar.make(root, "Network error", Snackbar.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                });

                final ImageButton personalRecord = (ImageButton) dialog.findViewById(R.id.personalRecord);
                personalRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), SaveMedicalRecordActivity.class));
                        dialog.dismiss();
                    }
                });
                dialog.show();

                break;
        }
    }

    public void insertHistory(final JSONArray array) {
        try {
            JSONObject object = array.getJSONObject(0);

            final HashMap<String, String> map = new HashMap();
            map.put("table", "patient_records");
            map.put("request", "crud");
            map.put("action", "insert");
            map.put("clinic_patient_record_id", object.getString("cpr_id"));
            map.put("patient_id", String.valueOf(SidebarActivity.getUserID()));
            map.put("doctor_id", String.valueOf(object.getInt("doctor_id")));
            map.put("clinic_id", String.valueOf(object.getInt("clinic_id")));
            map.put("complaints", object.getString("complaints"));
            map.put("findings", object.getString("findings"));
            map.put("record_date", object.getString("record_date"));

            PostRequest.send(getActivity(), map, new RespondListener<JSONObject>() {
                @Override
                public void getResult(JSONObject response) {
                    try {
                        int success = response.getInt("success");
                        if (success == 1) {
                            int last_inserted_id = response.getInt("last_inserted_id");

                            final PatientRecord pr = new PatientRecord();
                            pr.setRecordID(last_inserted_id);
                            pr.setCpr_id(Integer.parseInt(map.get("clinic_patient_record_id")));
                            pr.setDoctorID(Integer.parseInt(map.get("doctor_id")));
                            pr.setClinicID(Integer.parseInt(map.get("clinic_id")));
                            pr.setComplaints(map.get("complaints"));
                            pr.setFindings(map.get("findings"));
                            pr.setDate(map.get("record_date"));

                            final JSONArray master_arr = new JSONArray();
                            final ArrayList<HashMap<String, String>> array_treatments = new ArrayList();

                            for (int x = 0; x < array.length(); x++) {
                                JSONObject obj = array.getJSONObject(x);
                                HashMap<String, String> hash = new HashMap();

                                hash.put("patient_records_id", String.valueOf(last_inserted_id));
                                hash.put("medicine_id", obj.getString("medicine_id"));
                                hash.put("medicine_name", obj.getString("med_name"));
                                hash.put("frequency", obj.getString("frequency"));
                                hash.put("duration", obj.getString("duration"));
                                hash.put("duration_type", obj.getString("duration_type"));

                                JSONObject obj_for_server = new JSONObject(hash);
                                master_arr.put(obj_for_server);
                                array_treatments.add(hash);
                            }

                            JSONObject json_to_be_passed = new JSONObject();
                            json_to_be_passed.put("json_treatments", master_arr);

                            HashMap<String, String> hash = new HashMap();
                            hash.put("table", "patient_treatments");
                            hash.put("request", "crud");
                            hash.put("action", "multiple_insert");
                            hash.put("jsobj", json_to_be_passed.toString());

                            PostRequest.send(getActivity(), hash, new RespondListener<JSONObject>() {
                                @Override
                                public void getResult(JSONObject response) {
                                    try {
                                        int start_server_id = response.getInt("last_inserted_id");

                                        for (int x = 0; x < array_treatments.size(); x++) {
                                            HashMap<String, String> map = array_treatments.get(x);
                                            map.put("treatments_id", String.valueOf(start_server_id));

                                            array_treatments.set(x, map);
                                            start_server_id += 1;
                                        }

                                        if (prc.savePatientRecord(pr, "insert")) {
                                            if (ptc.savePatientTreatments(array_treatments, "insert")) {
                                                updateReceiptsList();
//                                                hashHistory = prc.getPatientRecord();
//                                                mAdapter = new SelectionAdapter(getActivity(), R.layout.listview_history_views, hashHistory);
//                                                list_of_history.setAdapter(mAdapter);
                                                progress.dismiss();
                                            }
                                        }
                                    } catch (Exception e) {
                                        Log.d("patientHistoryFrag4", e + "");
                                        Snackbar.make(root, "Server error occurred", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            }, new ErrorListener<VolleyError>() {
                                public void getError(VolleyError error) {
                                    progress.dismiss();
                                    Snackbar.make(root, "Network error", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        Log.d("patientHistoryFrag3", e + "");
                        Snackbar.make(root, "Server error occurred", Snackbar.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                }
            }, new ErrorListener<VolleyError>() {
                public void getError(VolleyError error) {
                    progress.dismiss();
                    Log.d("patientHistoryFrag2", error + "");
                    Snackbar.make(root, "Network error", Snackbar.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog2.dismiss();
    }

    void updateReceiptsList() {
        noResults.setVisibility(View.GONE);
        list_of_history.setVisibility(View.VISIBLE);

        hashHistory.clear();
        hashHistory = prc.getPatientRecord();
        Log.d("hashHistory", hashHistory + "");
        mAdapter = new SelectionAdapter(getActivity(), R.layout.listview_history_views, hashHistory);
        list_of_history.setAdapter(mAdapter);
    }

    private class SelectionAdapter extends ArrayAdapter {
        TextView dosage, medicine, findings, record_date, doctor;
        LayoutInflater inflater;
        ArrayList<HashMap<String, String>> objects;

        public SelectionAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
            super(context, resource, objects);
            inflater = LayoutInflater.from(context);
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = inflater.inflate(R.layout.listview_history_views, parent, false);

            record_date = (TextView) v.findViewById(R.id.record_date);
            findings = (TextView) v.findViewById(R.id.findings);
            doctor = (TextView) v.findViewById(R.id.doctor_name);
            medicine = (TextView) v.findViewById(R.id.medicine);
            dosage = (TextView) v.findViewById(R.id.dosage);

            doctor.setText(objects.get(position).get(PatientRecordController.RECORDS_DOCTOR_NAME));
            record_date.setText(objects.get(position).get(PatientRecordController.RECORDS_DATE));
            findings.setText(objects.get(position).get(PatientRecordController.RECORDS_FINDINGS));

            return v;
        }
    }
}
