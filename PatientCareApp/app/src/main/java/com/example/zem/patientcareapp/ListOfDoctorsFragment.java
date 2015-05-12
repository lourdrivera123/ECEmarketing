package com.example.zem.patientcareapp;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Zem on 4/29/2015.
 */
public class ListOfDoctorsFragment extends Fragment {
    ListView list_of_doctors;
    ArrayAdapter doctors_adapter;
    DbHelper dbHelper;

    JSONArray doctors_json_array_mysql = null;
    JSONArray doctors_json_array_sqlite = null;
    JSONArray doctors_json_array_final = null;
    JSONArray doctors_json_array_final_update = null;

    RequestQueue queue;
    String url;
    ProgressDialog pDialog;

    ArrayList<Doctor> doctors_array_list;

    // XML node keys
    static final String KEY_FULL_NAME = "fullname"; // parent node
    static final String KEY_SPECIALTY = "specialty";
    static final String KEY_PHOTO = "photo";
    static final String KEY_ID = "id";
    static final String KEY_DOCTOR = "doctor";

    LazyAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.patient_home_layout, container, false);

        dbHelper = new DbHelper(getActivity());
        queue = Volley.newRequestQueue(getActivity());
        url = "http://192.168.10.1/db/get_all_doctors.php";

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        if (isNetworkAvailable()) {

            Log.d("Connected to internet", "yes");
            // Request a string response from the provided URL.
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int success = response.getInt("success");
                        if (success == 1) {
                            doctors_json_array_mysql = response.getJSONArray("doctors");
                            doctors_json_array_sqlite = dbHelper.getAllDoctorsJSONArray();

                            doctors_json_array_final = checkWhatToInsert(doctors_json_array_mysql, doctors_json_array_sqlite);
                            doctors_json_array_final_update = checkWhatToUpdate(doctors_json_array_mysql, doctors_json_array_sqlite);

//                        Log.d("let me know", ""+checkDateTime("2015-05-12 13:00:56", "2015-05-12 13:00:55") );
                            Log.d("doctor json array final", "" + doctors_json_array_final);
                            Log.d("doctor json array final update", "" + doctors_json_array_final_update);

                            if (!doctors_json_array_final.equals("null")) {
                                for (int i = 0; i < doctors_json_array_final.length(); i++) {
                                    JSONObject doctor_json_object = doctors_json_array_final.getJSONObject(i);

                                    if (!doctor_json_object.equals("null")) {
                                        if (dbHelper.insertDoctor(setDoctorObject(doctor_json_object))) {
                                            Toast.makeText(getActivity(), "successfully saved ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "failed to save ", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                doctors_json_array_final = null;
                            } else {
                                Toast.makeText(getActivity(), "the final list is empty", Toast.LENGTH_SHORT).show();
                            }

                            if (!doctors_json_array_final_update.equals("null")) {
                                for (int i = 0; i < doctors_json_array_final_update.length(); i++) {
                                    JSONObject doctor_json_object = doctors_json_array_final_update.getJSONObject(i);

                                    if (!doctor_json_object.equals("null")) {
                                        if (dbHelper.updateDoctor(setDoctorObject(doctor_json_object))) {
                                            Toast.makeText(getActivity(), "successfully updated ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "failed to update", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                doctors_json_array_final = null;
                                doctors_json_array_final_update = null;
                            } else {
                                Toast.makeText(getActivity(), "the final list is empty", Toast.LENGTH_SHORT).show();
                            }

                            doctors_array_list = dbHelper.getAllDoctors();
                            String xml = dbHelper.getDoctorsStringXml();

                            populateDoctorListView(rootView, xml);
                            pDialog.hide();
                        }

                    } catch (JSONException e) {
//                    Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Error on request", Toast.LENGTH_SHORT).show();
                    pDialog.hide();
                }
            });

            queue.add(stringRequest);
// end of request volley

        } else {
            Log.d("Connected to internet", "no");
            doctors_array_list = dbHelper.getAllDoctors();
            String xml = dbHelper.getDoctorsStringXml();

            populateDoctorListView(rootView, xml);
            pDialog.hide();
        }

        return rootView;
    }

    public void populateDoctorListView(View rootView, String xml) {
        ArrayList<HashMap<String, String>> doctorsList = new ArrayList<HashMap<String, String>>();

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

    public JSONArray checkWhatToInsert(JSONArray doctors_json_array_mysql, JSONArray doctors_json_array_sqlite) throws JSONException {
        JSONArray doctors_json_array_final_storage = new JSONArray();
        try {
            for (int i = 0; i < doctors_json_array_mysql.length(); i++) {
                JSONObject doctor_json_object_mysql = doctors_json_array_mysql.getJSONObject(i);
                Boolean flag = false;

                if (doctors_json_array_sqlite == null) {
                    doctors_json_array_final_storage.put(doctor_json_object_mysql);
                } else {

                    for (int x = 0; x < doctors_json_array_sqlite.length(); x++) {
                        JSONObject doctor_json_object_sqlite = doctors_json_array_sqlite.getJSONObject(x);

                        if (doctor_json_object_mysql.getInt("id") == doctor_json_object_sqlite.getInt("doc_id")) {
                            flag = true;
                        }
                    }

                    if (!flag) {
                        doctors_json_array_final_storage.put(doctor_json_object_mysql);
                    }

                }
            }

        } catch (JSONException e) {
            Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return doctors_json_array_final_storage;
    }

    public boolean checkDateTime(String str1, String str2) {
        Boolean something = false;
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//            String str1 = "12/10/2013";
            Date date1 = formatter.parse(str1);

//            String str2 = "13/10/2013";
            Date date2 = formatter.parse(str2);

            if (date1.compareTo(date2) < 0) {
                something = true;
//                System.out.println("date2 is Greater than my date1");
//            Log.d("date2 is Greater than my date1", "adsasd");
            }


        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        return something;
    }

    public JSONArray checkWhatToUpdate(JSONArray doctors_json_array_mysql, JSONArray doctors_json_array_sqlite) {
        JSONArray doctors_json_array_final_storage = new JSONArray();
        try {
            for (int i = 0; i < doctors_json_array_mysql.length(); i++) {

                JSONObject doctor_json_object_mysql = doctors_json_array_mysql.getJSONObject(i);

                if (checkDateTime(dbHelper.getLastUpdate("doctors"), doctor_json_object_mysql.getString("updated_at"))) { //to be repared
                    //the sqlite last update is lesser than from mysql
                    //put your json object into final array here.

                    doctors_json_array_final_storage.put(doctor_json_object_mysql);
                    Log.d("Updated at Compare", "the updated_at column in mysql is greater than in sqlite");

                } else {
                    Log.d("Updated at Compare", "the updated_at column in sqlite is greater than in mysql");

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return doctors_json_array_final_storage;
    }

    public Doctor setDoctorObject(JSONObject doctor_json_object) {

        Doctor doctor_object = new Doctor();
        try {

            doctor_object.setDoc_id(doctor_json_object.getInt("id"));
            doctor_object.setClinic_id(doctor_json_object.getInt("clinic_id"));
            doctor_object.setSecretary_id(doctor_json_object.getInt("secretary_id"));
            doctor_object.setFullname(doctor_json_object.getString("fname"), doctor_json_object.getString("mname"), doctor_json_object.getString("lname"));
            doctor_object.setFullAddress(doctor_json_object.getString("address_house_no"), doctor_json_object.getString("address_street"),
                    doctor_json_object.getString("address_barangay"), doctor_json_object.getString("address_city_municipality"),
                    doctor_json_object.getString("address_province"), doctor_json_object.getString("address_region"),
                    doctor_json_object.getString("address_country"), doctor_json_object.getString("address_zip"));
            doctor_object.setPrc_no(doctor_json_object.getInt("prc_no"));
            doctor_object.setSpecialty(doctor_json_object.getString("specialty"));
            doctor_object.setSub_specialty(doctor_json_object.getString("sub_specialty"));
            doctor_object.setCell_no(doctor_json_object.getString("cell_no"));
            doctor_object.setTel_no(doctor_json_object.getString("tel_no"));
            doctor_object.setPhoto(doctor_json_object.getString("photo"));
            doctor_object.setClinic_sched(doctor_json_object.getString("clinic_sched"));
            doctor_object.setEmail(doctor_json_object.getString("email"));
            doctor_object.setAffiliation(doctor_json_object.getString("affiliation"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return doctor_object;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
