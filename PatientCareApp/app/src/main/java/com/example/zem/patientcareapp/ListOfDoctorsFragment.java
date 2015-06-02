package com.example.zem.patientcareapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Zem on 4/29/2015.
 */
public class ListOfDoctorsFragment extends Fragment implements AdapterView.OnItemClickListener, TextWatcher, View.OnClickListener {
    ListView list_of_doctors;
    AutoCompleteTextView search_doctor;
    ImageButton refresh_doctors_list;

    ArrayAdapter doctoradapter;
    ArrayList<String> arrayOfSearchDoctors;
    ArrayList<HashMap<String, String>> doctorsList;
    public ArrayList<Doctor> doctors_array_list;

    String s_doctor;

    DbHelper dbHelper;
    RequestQueue queue;
    ProgressDialog pDialog;

    // XML node keys
    static final String KEY_FULL_NAME = "fullname"; // parent node
    static final String KEY_SPECIALTY = "specialty";
    static final String KEY_PHOTO = "photo";
    static final String KEY_ID = "id";
    static final String KEY_DOCTOR = "doctor";

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
        arrayOfSearchDoctors = new ArrayList<String>();
        queue = Volley.newRequestQueue(getActivity());

        search_doctor = (AutoCompleteTextView) rootView.findViewById(R.id.search_doctor);
        refresh_doctors_list = (ImageButton) rootView.findViewById(R.id.refresh_doctors_list);

        refresh_doctors_list.setOnClickListener(this);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

            if ( helpers.isNetworkAvailable(getActivity()) ) {

            // Request a string response from the provided URL.
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, helpers.get_url("get_doctors"), null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response){
                    sync = new Sync();
                    sync.init(getActivity(), "get_doctors", "doctors", "doc_id", response);
                    try {
                        dbHelper.updateLastUpdatedTable("doctors", response.getString("server_timestamp"));
                    } catch (Exception e) {
                        System.out.println("error fetching server timestamp: "+ e);
                    }
                    doctors_array_list = dbHelper.getAllDoctors();
                    String xml = dbHelper.getDoctorsStringXml();

                    populateDoctorListView(rootView, xml);
                    pDialog.hide();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Error on request", Toast.LENGTH_SHORT).show();
                    System.out.println("GWAPO DAW KO: " + error);
                }
            });

            queue.add(stringRequest);

        } else {
            doctors_array_list = dbHelper.getAllDoctors();
            String xml = dbHelper.getDoctorsStringXml();

            populateDoctorListView(rootView, xml);
            pDialog.hide();
        }
        return rootView;
    }

    public void populateDoctorListView(View rootView, String xml) {
        final ArrayList<HashMap<String, String>> doctorsList = new ArrayList<HashMap<String, String>>();
        search_doctor = (AutoCompleteTextView) rootView.findViewById(R.id.search_doctor);
        search_doctor.addTextChangedListener(this);

        XMLParser parser = new XMLParser();
        Document doc = parser.getDomElement(xml); // getting DOM element

        NodeList nl = doc.getElementsByTagName(KEY_DOCTOR);
        // looping through all song nodes &lt;song&gt;
        for (int i = 0; i < nl.getLength(); i++) {
            // creating new HashMap
            HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(i);
            // adding each child node to HashMap key =&gt; value
            map.put(KEY_ID, parser.getValue(e, KEY_ID));
            map.put(KEY_FULL_NAME, parser.getValue(e, KEY_FULL_NAME));
            map.put(KEY_SPECIALTY, parser.getValue(e, KEY_SPECIALTY));
            map.put(KEY_PHOTO, parser.getValue(e, KEY_PHOTO));

            // adding HashList to ArrayList
            doctorsList.add(map);
            arrayOfSearchDoctors.add(doctorsList.get(i).get(KEY_FULL_NAME));
        }

        adapter = new LazyAdapter(getActivity(), doctorsList, "list_of_doctors");
        list_of_doctors = (ListView) rootView.findViewById(R.id.list_of_doctors);
        list_of_doctors.setAdapter(adapter);

        // Click event for single list row
        list_of_doctors.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                System.out.println("FUCKING DOCTOR: " + doctorsList.toString());
                Intent intent = new Intent(getActivity(), DoctorActivity.class);
                intent.putExtra(KEY_ID, doctorsList.get(position).get("id"));
                startActivity(intent);
            }
        });

        doctoradapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayOfSearchDoctors);
        search_doctor.setAdapter(doctoradapter);
        search_doctor.setOnItemClickListener(this);
        search_doctor.addTextChangedListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item_clicked = parent.getItemAtPosition(position).toString();
        int doctorindex = arrayOfSearchDoctors.indexOf(item_clicked);
        HashMap<String, String> map = new HashMap<String, String>();

        map.put(KEY_ID, doctorsList.get(doctorindex).get(KEY_ID));
        map.put(KEY_FULL_NAME, doctorsList.get(doctorindex).get(KEY_FULL_NAME));
        map.put(KEY_SPECIALTY, doctorsList.get(doctorindex).get(KEY_SPECIALTY));
        map.put(KEY_PHOTO, doctorsList.get(doctorindex).get(KEY_PHOTO));

        doctorsList.clear();
        doctorsList.add(map);
        adapter.notifyDataSetChanged();
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

        if (arrayOfSearchDoctors.contains(s_doctor)) {
            Toast.makeText(getActivity(), "search for: " + s_doctor, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "no results found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent =  new Intent(getActivity(), MasterTabActivity.class);
        intent.putExtra("selected", 3);
        startActivity(intent);
    }
}
