package com.example.zem.patientcareapp;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User PC on 5/5/2015. Updated 5/5/15
 */
public class ProductsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher {
    // XML node keys
    static final String KEY_PRODUCT_NAME = "name"; // parent node
    static final String KEY_PRODUCT_DESCRIPTION = "description";
    static final String KEY_PRODUCT_PHOTO = "photo";
    static final String KEY_PRODUCT_ID = "id";
    static final String KEY_PRODUCT_PRICE = "price";
    static final String KEY_PRODUCT = "entry";

    Button add_to_cart_btn;
    EditText qty;
    ListView list_of_products;
    LazyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.products_layout, container, false);

        ArrayList<HashMap<String, String>> products_list = new ArrayList<HashMap<String, String>>();

        XMLParser parser = new XMLParser();
//        String xml = parser.getXmlFromUrl(URL); // getting XML from URL
        String xml =
                "<list>" +
                        "<entry>\n" +
                        "<id>14</id>\n" +
                        "<name>Biogesic</name>\n" +
                        "<description>500mg Description ni diri</description>\n" +
                        "<price>Php 1.75</price>\n" +
                        "<photo>\n" +
                        "http://api.androidhive.info/music/images/rihanna.png\n" +
                        "</photo>\n" +
                        "</entry>" +
                        "<entry>\n" +
                        "<id>11</id>\n" +
                        "<name>Neozep</name>\n" +
                        "<description>500mg Description gihapon ni diri</description>\n" +
                        "<price>Php 3.75</price>\n" +
                        "<photo>http://api.androidhive.info/music/images/adele.png</photo>\n" +
                        "</entry>" +
                        "</list>";
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

        list_of_products = (ListView) rootView.findViewById(R.id.product_lists);

        // Getting adapter by passing xml data ArrayList
        adapter = new LazyAdapter(getActivity(), products_list, "product_lists");
        list_of_products.setAdapter(adapter);

        // Click event for single list row
        list_of_products.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Product Name");
        dialog.setContentView(R.layout.dialog_products_layout);
        dialog.show();

        add_to_cart_btn = (Button) dialog.findViewById(R.id.add_to_cart_btn);
        qty = (EditText) dialog.findViewById(R.id.qty);

        add_to_cart_btn.setOnClickListener(this);
        qty.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String qty_str = qty.getText().toString();
        Double price = 3.00;

        if(!qty_str.isEmpty()) {
            try {
                Double qty_int = Double.parseDouble(qty_str);
                Double product = qty_int * price;

                add_to_cart_btn.setText("Add to Cart | Php "+product);
            } catch(Exception e) {

            }
        }
        else {
            add_to_cart_btn.setText("Add to Cart | Php 3.00");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
