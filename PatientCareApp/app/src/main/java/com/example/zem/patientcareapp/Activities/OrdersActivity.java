package com.example.zem.patientcareapp.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.zem.patientcareapp.Controllers.DbHelper;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.SidebarModule.SidebarActivity;
import com.example.zem.patientcareapp.adapter.LazyAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Zem on 10/2/2015.
 */
public class OrdersActivity extends AppCompatActivity {

    ListView lv_items;
    Toolbar ordersToolbar;

    LazyAdapter adapter;
    ArrayList<String> order_items;
    ArrayList<HashMap<String, String>> items;
    DbHelper dbHelper;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_layout);

        lv_items = (ListView) findViewById(R.id.lv_items);
//        ordersToolbar = (Toolbar) findViewById(R.id.ordersToolbar);

        setSupportActionBar(ordersToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Recent Orders");
        ordersToolbar.setNavigationIcon(R.drawable.ic_back);

        context = getBaseContext();
        dbHelper = new DbHelper(OrdersActivity.this);

//        order_items = dbHelper.getAllOrderItems();
//        items = dbHelper.getAllOrderDetailItems(); // returns all basket items for the currently loggedin patient

        //experiment on expandable listview

//       ExpandableListView lv_items = (ExpandableListView) findViewById(R.id.lv_items);
//
//
//        ArrayList<String> listDataHeader = new ArrayList<String>();
//
//        HashMap<String, ArrayList<String>> listDataChild = new HashMap<String, ArrayList<String>>();
//
//        listDataHeader.add("Coming Soon");
//
//        ArrayList<String> comingSoon = new ArrayList<String>();
//        comingSoon.add("2 Guns");
//        comingSoon.add("The Smurfs 2");
//        comingSoon.add("The Spectacular Now");
//        comingSoon.add("The Canyons");
//        comingSoon.add("Europa Report");
//
//        listDataChild.put(order_items.get(0), comingSoon);
//
//        ExpandableListViewAdapter listAdapte
//       //end of experimentr = new ExpandableListViewAdapter(this, order_items, listDataChild);
//        lv_items.setAdapter(listAdapter);

        adapter = new LazyAdapter(OrdersActivity.this, items, "order_items");
        lv_items.setAdapter(adapter);


        Intent intent = getIntent();
        String timestamp_ordered = intent.getStringExtra("timestamp_ordered");
        String payment_from = intent.getStringExtra("payment_from");


        if (payment_from != null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(OrdersActivity.this);


            /*String str1 = "12/10/2013";*/
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date1 = formatter.parse(timestamp_ordered);

                //format to date only
                SimpleDateFormat fd = new SimpleDateFormat("MMM d yyyy");
                String formatted_date = fd.format(date1);

                if (payment_from.equals("paypal")) {
                    alert.setTitle("Purchase Completed!");
                    alert.setMessage("Thank you for your purchase !\n You shall receive this order on or before " + formatted_date);
                } else if (payment_from.equals("")) {
                    alert.setTitle("Order Placed !");
                    alert.setMessage("Thank you for your order !\n You shall receive a call from our pharmacist to confirm your order.");
                }

                alert.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                    }
                });
                alert.show();

            } catch (ParseException e) {
                e.printStackTrace();
            }


        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, SidebarActivity.class));
        this.finish();

        return super.onOptionsItemSelected(item);
    }
}