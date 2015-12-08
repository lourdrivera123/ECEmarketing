package com.example.zem.patientcareapp.CheckoutModule;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zem.patientcareapp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zem on 11/19/2015.
 */
public class SummaryAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<HashMap<String, String>> hashOfOrders;

    public SummaryAdapter(Context context, ArrayList<HashMap<String, String>> hashOfOrders){
        this.context = context;
        this.hashOfOrders = hashOfOrders;
    }

    @Override
    public int getCount() {
        return hashOfOrders.size();
    }

    @Override
    public Object getItem(int position) {
        return hashOfOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.basket_summary_item, null);
        }

        TextView medicine_name = (TextView) convertView.findViewById(R.id.medicine_name);
        TextView qty_price = (TextView) convertView.findViewById(R.id.qty_price);
        TextView item_subtotal = (TextView) convertView.findViewById(R.id.item_subtotal);

        medicine_name.setText(hashOfOrders.get(position).get("name"));
        qty_price.setText("\u20B1 "+hashOfOrders.get(position).get("price") + "x" +  hashOfOrders.get(position).get("quantity"));
        item_subtotal.setText("\u20B1 "+hashOfOrders.get(position).get("item_subtotal"));

        return convertView;
    }
}
