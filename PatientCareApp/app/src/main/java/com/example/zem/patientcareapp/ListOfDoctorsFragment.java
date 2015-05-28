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
public class ListOfDoctorsFragment extends Fragment implements TextWatcher {
    ListView list_of_doctors;

    EditText search_doctor;
    String[] doctors = {
            "Esel", "Dexter", "Zemiel"
    };

    List<String> findArray = Arrays.asList(doctors);

    DbHelper dbHelper;

    RequestQueue queue;
    ProgressDialog pDialog;

    public ArrayList<Doctor> doctors_array_list;

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

        dbHelper = new DbHelper(getActivity());
        queue = Volley.newRequestQueue(getActivity());

        helpers = new Helpers();

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        if (helpers.isNetworkAvailable(getActivity())) {

//            queue = sync.getQueue();

            // Request a string response from the provided URL.
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, helpers.get_url("get_doctors"), null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response){
                    sync = new Sync();
                    sync.init(getActivity(), "get_doctors", "doctors", "doc_id", response);

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

//            rootView.postDelayed(new Runnable() {
//                public void run() {
//                    // Actions to do after 3 seconds
//
//                    doctors_array_list = dbHelper.getAllDoctors();
//                    String xml = dbHelper.getDoctorsStringXml();
//
//                    populateDoctorListView(rootView, xml);
//                    pDialog.hide();
//                }
//            }, 3000);

        } else {
            Log.d("Connected to internet", "no");
            doctors_array_list = dbHelper.getAllDoctors();
            String xml = dbHelper.getDoctorsStringXml();

            populateDoctorListView(rootView, xml);
            pDialog.hide();
        }

        return rootView;
    }

//    public void callresponse(){
//        doctors_array_list = dbHelper.getAllDoctors();
//        String xml = dbHelper.getDoctorsStringXml();
//
//        populateDoctorListView(root_view, xml);
//        pDialog.hide();
//    }

    public void populateDoctorListView(View rootView, String xml) {
        ArrayList<HashMap<String, String>> doctorsList = new ArrayList<>();
        search_doctor = (EditText) rootView.findViewById(R.id.search_doctor);
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
        }

        list_of_doctors = (ListView) rootView.findViewById(R.id.list_of_doctors);

        // Getting adapter by passing xml data ArrayList
        adapter = new LazyAdapter(getActivity(), doctorsList, "list_of_doctors");

        list_of_doctors.setAdapter(adapter);
        // Click event for single list row
        list_of_doctors.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), DoctorActivity.class));
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String key = search_doctor.getText().toString();

        if (findArray.contains(key)) {
            Toast.makeText(getActivity(), "" + key, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void doSomeShit(String xml) {
        populateDoctorListView(root_view, xml);
    }
}
