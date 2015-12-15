package com.example.zem.patientcareapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.zem.patientcareapp.CheckoutModule.SummaryAdapter;
import com.example.zem.patientcareapp.Controllers.OrderDetailController;
import com.example.zem.patientcareapp.Customizations.NonScrollListView;
import com.example.zem.patientcareapp.Fragment.OrdersFragment;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.SidebarModule.SidebarActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zemskie on 12/11/2015.
 */
public class OrderDetailsActivity extends AppCompatActivity {

    Toolbar myToolBar;
    NonScrollListView order_summary;
    ArrayList<HashMap<String, String>> items;
    OrderDetailController odc;
    int order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        odc = new OrderDetailController(this);
        order_id = getIntent().getIntExtra("order_id", 0);

        if(order_id == 0)
            exitActivity();

        setContentView(R.layout.order_details_layout);
        myToolBar = (Toolbar) findViewById(R.id.myToolBar);
        order_summary = (NonScrollListView) findViewById(R.id.order_summary);

        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Details");
        myToolBar.setNavigationIcon(R.drawable.ic_back);
        items = odc.getOrderDetailsFromOrder(order_id);

        Log.d("items", items.toString());

        SummaryAdapter adapter = new SummaryAdapter(OrderDetailsActivity.this, items);
        order_summary.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                exitActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void exitActivity(){
        Intent order_intent = new Intent(getBaseContext(), SidebarActivity.class);
        order_intent.putExtra("select", 5);
        startActivity(order_intent);
        this.finish();
    }
}
