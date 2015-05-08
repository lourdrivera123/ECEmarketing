package com.example.zem.patientcareapp;


import android.app.Dialog;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zem on 4/29/2015.
 */
public class ListOfDoctorsFragment extends Fragment {
    ListView list_of_doctors;
    ArrayAdapter doctors_adapter;
    DbHelper dbHelper;

    JSONArray doctors_json_array = null;
    String fname = "", lname = "", mname = "", address_house_no = "", address_street = "",
            address_barangay = "", address_city_municipality = "", address_province = "", address_region = "",
            address_country = "", address_zip = "", specialty = "", sub_specialty = "", cell_no = "",
            tel_no = "", photo = "", clinic_sched = "", affiliation = "", email = "";
    int doctor_id = 0, prc_no = 0, clinic_id = 0, secretary_id = 0, doc_id = 0;

    ArrayList<Doctor> doctors_array_list;

    // XML node keys
    static final String KEY_FULL_NAME = "fullname"; // parent node
    static final String KEY_SPECIALTY = "specialty";
    static final String KEY_PHOTO = "photo";
    static final String KEY_ID = "id";
    static final String KEY_DOCTOR = "doctor";

    ListView list;
    LazyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.patient_home_layout, container, false);

        dbHelper = new DbHelper(getActivity());
        doctors_array_list = dbHelper.getAllDoctors();
        String xml = dbHelper.getDoctorsStringXml();

//        Toast.makeText(getActivity(), ""+doctors_array_list, Toast.LENGTH_LONG).show();
    Log.d("Log Doctor Array: ", ""+doctors_array_list);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "http://192.168.10.1/db/get_all_doctors.php";

        //From StandStrong

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    if (success == 1) {
                        doctors_json_array = response.getJSONArray("doctors");

                        // looping through All Doctors
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject doctor_json_object = doctors_json_array.getJSONObject(i);

                            // Storing each json item in variable
                            String id = doctor_json_object.getString("id");
                            String lname = doctor_json_object.getString("lname");


                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

//              Check if there are null values and put it into variable

                            doc_id = doctor_json_object.getInt("id");
                            fname = doctor_json_object.getString("fname");
                            lname = doctor_json_object.getString("lname");
                            mname = doctor_json_object.getString("mname");
                            clinic_id = doctor_json_object.getInt("clinic_id");
                            secretary_id = doctor_json_object.getInt("secretary_id");
                            prc_no = doctor_json_object.getInt("prc_no");
                            address_house_no = doctor_json_object.getString("address_house_no");
                            address_street = doctor_json_object.getString("address_street");
                            address_barangay = doctor_json_object.getString("address_barangay");
                            address_city_municipality = doctor_json_object.getString("address_city_municipality");
                            address_province = doctor_json_object.getString("address_province");
                            address_region = doctor_json_object.getString("address_region");
                            address_country = doctor_json_object.getString("address_country");
                            address_zip = doctor_json_object.getString("address_zip");
                            specialty = doctor_json_object.getString("specialty");
                            sub_specialty = doctor_json_object.getString("sub_specialty");
                            cell_no = doctor_json_object.getString("cell_no");
                            tel_no = doctor_json_object.getString("tel_no");
                            photo = doctor_json_object.getString("photo");
                            clinic_sched = doctor_json_object.getString("clinic_sched");
                            email = doctor_json_object.getString("email");
                            affiliation = doctor_json_object.getString("affiliation");

//          Initiate Doctor Object and set the attributes

                            Doctor doctor_object = new Doctor();
                            doctor_object.setDoc_id(doc_id);
                            doctor_object.setClinic_id(clinic_id);
                            doctor_object.setSecretary_id(secretary_id);
                            doctor_object.setFullname(fname, mname, lname);
                            doctor_object.setFullAddress(address_house_no, address_street, address_barangay, address_city_municipality,
                                    address_province, address_region, address_country, address_zip);
                            doctor_object.setPrc_no(prc_no);
                            doctor_object.setSpecialty(specialty);
                            doctor_object.setSub_specialty(sub_specialty);
                            doctor_object.setCell_no(cell_no);
                            doctor_object.setTel_no(tel_no);
                            doctor_object.setPhoto(photo);
                            doctor_object.setClinic_sched(clinic_sched);
                            doctor_object.setEmail(email);
                            doctor_object.setAffiliation(affiliation);
//
//                            if (dbHelper.insertDoctor(doctor_object)) {
//                                Toast.makeText(getActivity(), "successfully saved " + fname, Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(getActivity(), "failed to save "+ fname, Toast.LENGTH_SHORT).show();
//                            }

                            // adding HashList to ArrayList
//                            doctors_list.add(map);
                        }
                    }

                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

        //End of StandStrong

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
        adapter=new LazyAdapter(getActivity(), doctorsList, "list_of_doctors");

        list_of_doctors.setAdapter(adapter);

        // Click event for single list row
        list_of_doctors.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), DoctorActivity.class));
            }
        });

        return rootView;
    }

}
