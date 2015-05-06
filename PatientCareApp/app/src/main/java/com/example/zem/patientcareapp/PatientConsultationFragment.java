package com.example.zem.patientcareapp;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dexter B. on 5/5/2015.
 */
public class PatientConsultationFragment extends Fragment {
    // XML node keys
    static final String KEY_DOCTOR_NAME = "doctor"; // parent node
    static final String KEY_CLINIC_ADDRESS = "clinic_address";
    static final String KEY_SCHEDULE = "schedule";
    static final String KEY_DATE = "date";
    static final String KEY_SCHED = "entry";
    static final String KEY_ID = "id";

    ListView  consultation_schedules;
    LazyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.patient_consultation_layout, container, false);

        ArrayList<HashMap<String, String>> consultationScheds = new ArrayList<HashMap<String, String>>();
        XMLParser parser = new XMLParser();

        String xml= "<list>" +
                "<entry>\n" +
                "<id>14</id>\n" +
                "<doctor>Dr. Zemiel Asma</doctor>\n" +
                "<clinic_address>#67 Acacia Rd., Dexter Ave., Davao City</clinic_address>\n" +
                "<date>24th May 2015</date>\n" +
                "<schedule>AM</schedule>\n" +
                "</entry>" +
                "<entry>\n" +
                "<id>11</id>\n" +
                "<doctor>Dr. Esel Barnes</doctor>\n" +
                "<clinic_address>#67 Acacia Rd., Dexter Ave., Davao City</clinic_address>\n" +
                "<date>19th May 2015</date>\n" +
                "<schedule>PM</schedule>\n" +
                "</entry>" +
                "<entry>\n" +
                "<id>14</id>\n" +
                "<doctor>Dr. Zemiel Asma</doctor>\n" +
                "<clinic_address>#67 Acacia Rd., Dexter Ave., Davao City</clinic_address>\n" +
                "<date>24th May 2015</date>\n" +
                "<schedule>AM</schedule>\n" +
                "</entry>" +
                "<entry>\n" +
                "<id>11</id>\n" +
                "<doctor>Dr. Esel Barnes</doctor>\n" +
                "<clinic_address>#67 Acacia Rd., Dexter Ave., Davao City</clinic_address>\n" +
                "<date>19th May 2015</date>\n" +
                "<schedule>PM</schedule>\n" +
                "</entry>" +
                "<entry>\n" +
                "<id>14</id>\n" +
                "<doctor>Dr. Zemiel Asma</doctor>\n" +
                "<clinic_address>#67 Acacia Rd., Dexter Ave., Davao City</clinic_address>\n" +
                "<date>24th May 2015</date>\n" +
                "<schedule>AM</schedule>\n" +
                "</entry>" +
                "<entry>\n" +
                "<id>11</id>\n" +
                "<doctor>Dr. Esel Barnes</doctor>\n" +
                "<clinic_address>#67 Acacia Rd., Dexter Ave., Davao City</clinic_address>\n" +
                "<date>19th May 2015</date>\n" +
                "<schedule>PM</schedule>\n" +
                "</entry>" +
                "</list>";
        Document doc = parser.getDomElement(xml);

        NodeList nl = doc.getElementsByTagName(KEY_SCHED);

        for (int x = 0; x < nl.getLength(); x++){
            // creating new HashMaps
            HashMap<String, String> map = new HashMap<String, String>();

            Element e = (Element) nl.item(x);

            map.put(KEY_ID, parser.getValue(e, KEY_ID));
            map.put(KEY_DOCTOR_NAME, parser.getValue(e, KEY_DOCTOR_NAME));
            map.put(KEY_CLINIC_ADDRESS, parser.getValue(e, KEY_CLINIC_ADDRESS));
            map.put(KEY_DATE, parser.getValue(e, KEY_DATE));
            map.put(KEY_SCHEDULE, parser.getValue(e, KEY_SCHEDULE));

            // let's add the map to the arraylist
            consultationScheds.add(map);
        }

        consultation_schedules = (ListView) rootView.findViewById(R.id.consultation_schedules);
        adapter = new LazyAdapter(getActivity(), consultationScheds, "consultation_lists");

        consultation_schedules.setAdapter(adapter);
        return rootView;
    }
}
