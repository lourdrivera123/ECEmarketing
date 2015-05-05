package com.example.zem.patientcareapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User PC on 5/5/2015.
 */
public class ProductsActivity extends ActionBarActivity{
    ListView list_of_doctors;
    String[] doctors = new String[] {
            "Dr. Zemiel M. Asma", "Dr. Rosell B. Barnes", "Dr. Dexter M. Bengil"
    };
    ArrayAdapter doctors_adapter;

    // XML node keys
    static final String KEY_PRODUCT_NAME = "name"; // parent node
    static final String KEY_PRODUCT_DESCRIPTION = "description";
    static final String KEY_PRODUCT_PHOTO = "photo";
    static final String KEY_PRODUCT_ID = "id";
    static final String KEY_PRODUCT_PRICE = "price";
    static final String KEY_PRODUCT = "entry";

    ListView list_of_products;
    LazyAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_layout);

//        list_of_doctors = (ListView) findViewById(R.id.list_of_doctors);
//        doctors_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, doctors);
//        list_of_doctors.setAdapter(doctors_adapter);
//        list_of_doctors.setOnItemClickListener(this);

        //        Doctor doctor = new Doctor();
//        doctor.setFullname("Dexter", "Mangubat", "Bengil");
//        doctor.setFullAddress("#86 Dexter Bldg.", "Dexter St.", "Cabantian", "Davao City",
//                "Davao del Sur", "Region XI", "Philippines", "8000");
//        doctor.setContactInfo("dexter@dexter.com", "+63 934-569-4345", "+856-7854");

        ArrayList<HashMap<String, String>> products_list = new ArrayList<HashMap<String, String>>();

        XMLParser parser = new XMLParser();
//        String xml = parser.getXmlFromUrl(URL); // getting XML from URL
        String xml = "<list><entry>\n" +
                "<id>14</id>\n" +
                "<name>Biogesic</name>\n" +
                "<description>500mg Description ni diri</description>\n" +
                "<price>Php 1.75</price>\n" +
                "<photo>\n" +
                "http://api.androidhive.info/music/images/rihanna.png\n" +
                "</photo>\n" +
                "</entry><entry>\n" +
                "<id>11</id>\n" +
                "<name>Neozep</name>\n" +
                "<description>500mg Description gihapon ni diri</description>\n" +
                "<price>Php 3.75</price>\n" +
                "<photo>http://api.androidhive.info/music/images/adele.png</photo>\n" +
                "</entry></list>";
        Document doc = parser.getDomElement(xml); // getting DOM element



        NodeList nl = doc.getElementsByTagName(KEY_PRODUCT);
        // looping through all song nodes &lt;song&gt;
        for (int i = 0; i < nl.getLength(); i++) {
            // creating new HashMap
            HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(i);
            // adding each child node to HashMap key =&gt; value
            map.put(KEY_PRODUCT_ID, parser.getValue(e, KEY_PRODUCT_ID));
            map.put(KEY_PRODUCT_NAME, parser.getValue(e, KEY_PRODUCT_NAME));
            map.put(KEY_PRODUCT_DESCRIPTION, parser.getValue(e, KEY_PRODUCT_DESCRIPTION));
            map.put(KEY_PRODUCT_PHOTO, parser.getValue(e, KEY_PRODUCT_PHOTO));
            map.put(KEY_PRODUCT_PRICE, parser.getValue(e, KEY_PRODUCT_PRICE));

            // adding HashList to ArrayList
            products_list.add(map);
        }



        list_of_products = (ListView)findViewById(R.id.product_lists);

        // Getting adapter by passing xml data ArrayList
        adapter=new LazyAdapter(this, products_list);
        list_of_products.setAdapter(adapter);

        // Click event for single list row
        list_of_products.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Dialog dialog = new Dialog(ProductsActivity.this);
                dialog.setTitle("Medical Records");
                dialog.setContentView(R.layout.patient_diagnosis_layout);
                dialog.show();
            }
        });
    }
}
