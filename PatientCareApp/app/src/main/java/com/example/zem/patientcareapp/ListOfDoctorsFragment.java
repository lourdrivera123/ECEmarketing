package com.example.zem.patientcareapp;

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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zem on 4/29/2015.
 */

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

    // XML node keys
    static final String KEY_FULL_NAME = "fullname"; // parent node
    static final String KEY_SPECIALTY = "specialty";
    static final String KEY_PHOTO = "photo";
    static final String KEY_ID = "id";

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
        queue = Volley.newRequestQueue(getActivity());

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
            arrayOfSearchDoctors.add(doctor_items.get(i).get(KEY_FULL_NAME));
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
        int ID = Integer.parseInt(doctor_items.get(position).get(KEY_ID));
        Intent intent = new Intent(getActivity(), DoctorActivity.class);
        intent.putExtra(dbHelper.RECORDS_DOCTOR_ID, ID);
        intent.putExtra(DoctorActivity.PARENT_ACTIVITY, "ListOfDoctorsFragment");
        startActivity(intent);
        getActivity().finish();
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

                map.put(KEY_ID, temp_doctors.get(doctorindex).get(KEY_ID));
                map.put(KEY_FULL_NAME, temp_doctors.get(doctorindex).get(KEY_FULL_NAME));
                map.put(KEY_SPECIALTY, temp_doctors.get(doctorindex).get(KEY_SPECIALTY));
                map.put(KEY_PHOTO, temp_doctors.get(doctorindex).get(KEY_PHOTO));
                doctor_items.add(map);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRefresh() {
        if (helpers.isNetworkAvailable(getActivity())) {
            // Request a string response from the provided URL.
            JsonObjectRequest doctor_request = new JsonObjectRequest(Request.Method.GET, helpers.get_url("get_doctors"), null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    sync = new Sync();
                    sync.init(getActivity(), "get_doctors", "doctors", "doc_id", response);
                    try {
                        dbHelper.updateLastUpdatedTable("doctors", response.getString("server_timestamp"));
                        adapter.notifyDataSetChanged();
                        refresh_doctor.setRefreshing(false);
                    } catch (Exception e) {

                    }
                    doctor_items = dbHelper.getAllDoctors();
                    populateDoctorListView(root_view, doctor_items);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Error on request", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(doctor_request);
        } else {
            Toast.makeText(getActivity(), "Couldn't refresh feed. Please check your Internet connection", Toast.LENGTH_LONG).show();
            refresh_doctor.setRefreshing(false);
        }
    }
}

