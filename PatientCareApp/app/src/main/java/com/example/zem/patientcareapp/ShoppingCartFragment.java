package com.example.zem.patientcareapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dexter B. on 5/6/2015.
 */
public class ShoppingCartFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.cart_layout, container, false);
        TableLayout tbl = (TableLayout) rootView.findViewById(R.id.tbl_items);

        ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
        TableRow.LayoutParams lparams;
        HashMap<String, String> map = new HashMap<String, String>();

        map.put("name", "Biogesic");
        map.put("price", "Php 2.75");
        map.put("quantity", "35");

        items.add(map);

        map.put("name", "Neozep");
        map.put("price", "4.25");
        map.put("quantity", "35");

        items.add(map); // the data of ArrayList 'items' here should be fetched from database ;)

        for (int x = 0; x < items.size(); x++){
            HashMap<String, String> row;
            row = items.get(x);

            TableRow tr = (TableRow) getLayoutInflater(savedInstanceState).inflate(R.layout._partials_table_row, null);

            TextView tv_item = (TextView) getLayoutInflater(savedInstanceState).inflate(R.layout._partials_table_row_item, null);
            TextView tv_qty = (TextView) getLayoutInflater(savedInstanceState).inflate(R.layout._partials_table_row_quantity, null);
            TextView tv_total = (TextView) getLayoutInflater(savedInstanceState).inflate(R.layout._partials_table_row_total, null);

            double price = Double.parseDouble(row.get("price"));
            int quantity = Integer.parseInt(row.get("quantity"));

            tv_item.setText(row.get("name")+" @Php "+price);
            tv_qty.setText(row.get("quantity"));
            tv_total.setText((quantity*price)+"");

            tr.addView(tv_item);
            tr.addView(tv_qty);
            tr.addView(tv_total);

            tbl.addView(tr);
        }

        return rootView;
    }

}
