package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zem on 4/29/2015.
 */
public class PatientHomeActivity extends Activity{
    ListView list_of_doctors;
    String[] doctors = new String[] {
            "Dr. Zemiel M. Asma", "Dr. Rosell B. Barnes", "Dr. Dexter M. Bengil"
    };
    ArrayAdapter doctors_adapter;

    // XML node keys
    static final String KEY_FULL_NAME = "fullname"; // parent node
    static final String KEY_SPECIALTY = "specialty";
    static final String KEY_PHOTO = "photo";
    static final String KEY_ID = "id";
    static final String KEY_DOCTOR = "doctor";

    ListView list;
    LazyAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_home_layout);

//        list_of_doctors = (ListView) findViewById(R.id.list_of_doctors);
//        doctors_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, doctors);
//        list_of_doctors.setAdapter(doctors_adapter);
//        list_of_doctors.setOnItemClickListener(this);

        //        Doctor doctor = new Doctor();
//        doctor.setFullname("Dexter", "Mangubat", "Bengil");
//        doctor.setFullAddress("#86 Dexter Bldg.", "Dexter St.", "Cabantian", "Davao City",
//                "Davao del Sur", "Region XI", "Philippines", "8000");
//        doctor.setContactInfo("dexter@dexter.com", "+63 934-569-4345", "+856-7854");

        ArrayList<HashMap<String, String>> doctorsList = new ArrayList<HashMap<String, String>>();

        XMLParser parser = new XMLParser();
//        String xml = parser.getXmlFromUrl(URL); // getting XML from URL
        String xml = "<list><doctor>\n" +
                "<id>14</id>\n" +
                "<fullname>Dr. Zemiel Asma</fullname>\n" +
                "<specialty>Cardiologist</specialty>\n" +
                "<photo>\n" +
                "http://api.androidhive.info/music/images/rihanna.png\n" +
                "</photo>\n" +
                "</doctor><doctor>\n" +
                "<id>11</id>\n" +
                "<fullname>Dr. Esel Barnes</fullname>\n" +
                "<specialty>Doctorologist</specialty>\n" +
                "<photo>http://api.androidhive.info/music/images/adele.png</photo>\n" +
                "</doctor></list>";
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



        list_of_doctors = (ListView)findViewById(R.id.list_of_doctors);

        // Getting adapter by passing xml data ArrayList
        adapter=new LazyAdapter(this, doctorsList, "list_of_doctors");
        list_of_doctors.setAdapter(adapter);

        // Click event for single list row
        list_of_doctors.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Dialog dialog = new Dialog(PatientHomeActivity.this);
                dialog.setTitle("Medical Records");
                dialog.setContentView(R.layout.patient_diagnosis_layout);
                dialog.show();
            }
        });
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        String doctor = (String) list_of_doctors.getItemAtPosition(position);
//        Intent intent = new Intent(this, DoctorActivity.class);
//        startActivity(intent);
//    }
}
