package com.example.zem.patientcareapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.zem.patientcareapp.Activities.OrderDetailsActivity;
import com.example.zem.patientcareapp.Controllers.BillingController;
import com.example.zem.patientcareapp.Controllers.DbHelper;
import com.example.zem.patientcareapp.Controllers.OrderController;
import com.example.zem.patientcareapp.Controllers.OrderDetailController;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.adapter.OrdersAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zem on 10/2/2015.
 */
public class OrdersFragment extends Fragment implements AdapterView.OnItemClickListener {
    ListView lv_items;

    OrdersAdapter adapter;
    ArrayList<HashMap<String, String>> order_items;
    DbHelper dbHelper;
    OrderController oc;
    OrderDetailController odc;
    BillingController blc;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.orders_layout, container, false);

        lv_items = (ListView) rootView.findViewById(R.id.lv_items);

        dbHelper = new DbHelper(getActivity());
        oc = new OrderController(getActivity());
        odc = new OrderDetailController(getActivity());
        blc = new BillingController(getActivity());

        order_items = oc.getAllOrderItems();

        adapter = new OrdersAdapter(getActivity(), order_items);

        lv_items.setAdapter(adapter);
        lv_items.setOnItemClickListener(this);
//        Intent intent = getIntent();
//        String timestamp_ordered = intent.getStringExtra("timestamp_ordered");
//        String payment_from = intent.getStringExtra("payment_from");
//
//        if (payment_from != null) {
//            AlertDialog.Builder alert = new AlertDialog.Builder(OrdersActivity.this);
//
//
//            /*String str1 = "12/10/2013";*/
//            try {
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date1 = formatter.parse(timestamp_ordered);
//
//                //format to date only
//                SimpleDateFormat fd = new SimpleDateFormat("MMM d yyyy");
//                String formatted_date = fd.format(date1);
//
//                if (payment_from.equals("paypal")) {
//                    alert.setTitle("Purchase Completed!");
//                    alert.setMessage("Thank you for your purchase !\n You shall receive this order on or before " + formatted_date);
//                } else if (payment_from.equals("")) {
//                    alert.setTitle("Order Placed !");
//                    alert.setMessage("Thank you for your order !\n You shall receive a call from our pharmacist to confirm your order.");
//                }
//
//                alert.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(final DialogInterface dialog, int which) {
//
//                    }
//                });
//                alert.show();
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int order_id = Integer.parseInt(order_items.get(position).get("order_id"));

        Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
        intent.putExtra("order_id", order_id);
        startActivity(intent);
    }
}
