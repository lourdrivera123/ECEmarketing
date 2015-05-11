package com.example.zem.patientcareapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.security.spec.EllipticCurve;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Dexter B. on 5/6/2015.
 */
public class ShoppingCartFragment extends Fragment implements AdapterView.OnItemClickListener {
    ListView lv_items;
    LazyAdapter adapter;
    public static ArrayList<HashMap<String, String>> items;
    HashMap<String, String> map;
    public static TextView total_amount;
    public Double TotalAmount = 0.00;

    EditText et_qty;
    TextView tv_amount;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.cart_layout, container, false);

        lv_items = (ListView) rootView.findViewById(R.id.lv_items);
        total_amount = (TextView) rootView.findViewById(R.id.upper_cart_total);

        items = new ArrayList<HashMap<String, String>>();
        TableRow.LayoutParams lparams;
        map = new HashMap<String, String>();

        map.put("id", "1");
        map.put("name", "Biogesic");
        map.put("price", "2.75");
        map.put("quantity", "5");
        TotalAmount += 2.75 * 5;

        items.add(map);
        map = new HashMap<String, String>();

        map.put("id", "2");
        map.put("name", "Neozep1");
        map.put("price", "4.25");
        map.put("quantity", "10");
        TotalAmount += 4.25 * 10;

        items.add(map);
        map = new HashMap<String, String>();

        map.put("id", "3");
        map.put("name", "Neozep2");
        map.put("price", "4.25");
        map.put("quantity", "25");
        TotalAmount += 4.25 * 25;

        items.add(map);
        map = new HashMap<String, String>();

        map.put("id", "4");
        map.put("name", "Neozep3");
        map.put("price", "4.25");
        map.put("quantity", "35");
        TotalAmount += 4.25 * 35;

        items.add(map);
        map = new HashMap<String, String>();

        map.put("id", "5");
        map.put("name", "Neozep4");
        map.put("price", "4.25");
        map.put("quantity", "15");
        TotalAmount += 4.25 * 15;

        items.add(map); // the data of ArrayList 'items' here should be fetched from database ;)

        adapter = new LazyAdapter(getActivity(), items, "basket_items");

        lv_items.setAdapter(adapter);
        lv_items.setOnItemClickListener(this);

//        et_qty = (EditText) lv_items.findViewById(R.id.product_quantity);
//        et_qty.addTextChangedListener(this);

//        tv_amount = (TextView) lv_items.findViewById(R.id.total);

        lv_items.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, final View arg1,
                                           final int pos, long id) {

                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Delete this item?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "Item on Position:" + pos + " successfully removed.", Toast.LENGTH_SHORT).show();
                        double amount = Double.parseDouble(items.get(pos).get("price")) * Double.parseDouble(items.get(pos).get("quantity"));
                        TotalAmount -= amount;
                        total_amount.setText("Php " + TotalAmount);
                        items.remove(pos);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                builder.show();

                return true;
            }
        });

        // Do the fucking tae here
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //   Toast.makeText(getActivity(), "position: " + position, Toast.LENGTH_SHORT).show();
    }
}
