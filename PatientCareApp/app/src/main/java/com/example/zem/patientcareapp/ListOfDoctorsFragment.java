package com.example.zem.patientcareapp;


import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

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
            songsList.add(map);
        }

        list_of_doctors = (ListView) rootView.findViewById(R.id.list_of_doctors);

        // Getting adapter by passing xml data ArrayList
        adapter = new LazyAdapter(getActivity(), songsList, "list_of_doctors");
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
