package com.example.zem.patientcareapp.Fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.zem.patientcareapp.DbHelper;
import com.example.zem.patientcareapp.DoctorActivity;
import com.example.zem.patientcareapp.Helpers;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.LazyAdapter;
import com.example.zem.patientcareapp.Network.GetRequest;
import com.example.zem.patientcareapp.Network.VolleySingleton;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.Sync;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListOfDoctorsFragment extends Fragment implements TextWatcher, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    ListView list_of_doctors;
    EditText search_doctor;
    SwipeRefreshLayout refresh_doctor;

    ArrayList<String> arrayOfSearchDoctors;
    ArrayList<HashMap<String, String>> temp_doctors;
    public static ArrayList<HashMap<String, String>> doctor_items;

    String s_doctor;

    DbHelper dbHelper;
    RequestQueue queue;

    LazyAdapter adapter;
    Helpers helpers;
    Sync sync;
    View root_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.patient_home_layout, container, false);
        root_view = rootView;

        helpers = new Helpers();
        dbHelper = new DbHelper(getActivity());
        queue = VolleySingleton.getInstance().getRequestQueue();

        arrayOfSearchDoctors = new ArrayList();
        temp_doctors = new ArrayList();

        search_doctor = (EditText) rootView.findViewById(R.id.search_doctor);
        refresh_doctor = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_doctor);

        doctor_items = dbHelper.getAllDoctors();
        populateDoctorListView(rootView, doctor_items);

        return rootView;
    }

    public void populateDoctorListView(View rootView, ArrayList<HashMap<String, String>> doctor_items) {
        for (int i = 0; i < doctor_items.size(); i++) {
            arrayOfSearchDoctors.add(doctor_items.get(i).get(dbHelper.DOC_FULLNAME));
        }
        temp_doctors.addAll(doctor_items);

        adapter = new LazyAdapter(getActivity(), doctor_items, "list_of_doctors");
        list_of_doctors = (ListView) rootView.findViewById(R.id.list_of_doctors);
        list_of_doctors.setAdapter(adapter);
        refresh_doctor.setOnRefreshListener(this);

        list_of_doctors.setOnItemClickListener(this);
        search_doctor.addTextChangedListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int ID = Integer.parseInt(doctor_items.get(position).get(dbHelper.DOC_DOC_ID));
        Intent intent = new Intent(getActivity(), DoctorActivity.class);
        intent.putExtra(dbHelper.DOC_DOC_ID, ID);
        startActivity(intent);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        s_doctor = search_doctor.getText().toString();
        doctor_items.clear();

        for (String doctor : arrayOfSearchDoctors) {
            if (doctor.toLowerCase().contains(s_doctor.toLowerCase())) {
                int doctorindex = arrayOfSearchDoctors.indexOf(doctor);

                HashMap<String, String> map = new HashMap();

                map.put(dbHelper.DOC_DOC_ID, temp_doctors.get(doctorindex).get(dbHelper.DOC_DOC_ID));
                map.put(dbHelper.DOC_FULLNAME, temp_doctors.get(doctorindex).get(dbHelper.DOC_FULLNAME));
                map.put(dbHelper.DOC_SPECIALTY_NAME, temp_doctors.get(doctorindex).get(dbHelper.DOC_SPECIALTY_NAME));
                doctor_items.add(map);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRefresh() {
        //request for doctors
        GetRequest.getJSONobj(getActivity(), "get_doctors", "doctors", "doc_id", new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                Log.d("response using interface <ListOfDoctorsFragment.java>", response + "");
                doctor_items = dbHelper.getAllDoctors();
                populateDoctorListView(root_view, doctor_items);
                refresh_doctor.setRefreshing(false);
            }
        }, new ErrorListener<VolleyError>() {
            public void getError(VolleyError error) {
                Toast.makeText(getActivity(), "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_LONG).show();
            }
        });

        //request for clinic
        GetRequest.getJSONobj(getActivity(), "get_clinics", "clinics", "clinics_id", new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                Log.d("response using interface <SplashActivity.java - clinic request >", response + "");
                doctor_items = dbHelper.getAllDoctors();
                populateDoctorListView(root_view, doctor_items);
                refresh_doctor.setRefreshing(false);
            }
        }, new ErrorListener<VolleyError>() {
            public void getError(VolleyError error) {
                Log.d("Error", error + "");
                Toast.makeText(getActivity(), "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_LONG).show();
            }
        });

        //request for clinic doctor request
        GetRequest.getJSONobj(getActivity(), "get_clinic_doctor", "clinic_doctor", "clinic_doctor_id", new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                Log.d("response using interface <SplashActivity.java - clinic_doctor request >", response + "");
                doctor_items = dbHelper.getAllDoctors();
                populateDoctorListView(root_view, doctor_items);
                refresh_doctor.setRefreshing(false);
            }
        }, new ErrorListener<VolleyError>() {
            public void getError(VolleyError error) {
                Log.d("Error", error + "");
                Toast.makeText(getActivity(), "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_LONG).show();
            }
        });
        
    }
}

