package com.example.zem.patientcareapp.CheckoutModule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.DbHelper;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Model.OrderModel;
import com.example.zem.patientcareapp.Network.GetRequest;
import com.example.zem.patientcareapp.NonScrollListView;
import com.example.zem.patientcareapp.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zem on 11/20/2015.
 */
public class SummaryActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar myToolBar;
    Button change_id;
    OrderModel order_model;
    Intent get_intent;
    DbHelper dbHelper;
    ArrayList<HashMap<String, String>> items;
    Double totalAmount = 0.0;
    NonScrollListView order_summary;
    TextView amount_subtotal, amount_of_coupon_discount, amount_of_points_discount, total_amount;
    LinearLayout points_layout, coupon_layout, subtotal_layout, total_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summarylayout);

        get_intent = getIntent();
        Bundle bundle= get_intent.getExtras();

        order_model = (OrderModel) bundle.getSerializable("order_model");

        myToolBar = (Toolbar) findViewById(R.id.myToolBar);
        change_id = (Button) findViewById(R.id.change_id);
        order_summary = (NonScrollListView) findViewById(R.id.order_summary);

        amount_subtotal = (TextView) findViewById(R.id.amount_subtotal);
        amount_of_coupon_discount = (TextView) findViewById(R.id.amount_of_coupon_discount);
        amount_of_points_discount = (TextView) findViewById(R.id.amount_of_points_discount);
        total_amount = (TextView) findViewById(R.id.total_amount);

        points_layout = (LinearLayout) findViewById(R.id.points_layout);
        coupon_layout = (LinearLayout) findViewById(R.id.coupon_layout);
        subtotal_layout = (LinearLayout) findViewById(R.id.subtotal_layout);
        total_layout = (LinearLayout) findViewById(R.id.total_layout);

        dbHelper = new DbHelper(this);

        String url_raw = "get_basket_items&patient_id=" + dbHelper.getCurrentLoggedInPatient().getServerID() + "&table=baskets";

        GetRequest.getJSONobj(this, url_raw, "baskets", "basket_id", new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                items = dbHelper.getAllBasketItems(false); // returns all basket items for the currently loggedin patient

                for (HashMap<String, String> item : items) {
                    totalAmount = totalAmount + Double.parseDouble(item.get("item_subtotal"));
                }

                double coupon_discount = order_model.getCoupon_discount() * totalAmount;
                double points_discount = order_model.getPoints_discount() * totalAmount;
                double discounted_total = totalAmount - points_discount - coupon_discount;

                /* discounts and total block*/

                if(coupon_discount == 0.0)
                    coupon_layout.setVisibility(View.GONE);

                if(points_discount == 0.0)
                    points_layout.setVisibility(View.GONE);

                if(coupon_discount == 0.0 && points_discount == 0.0)
                    subtotal_layout.setVisibility(View.GONE);

                amount_subtotal.setText("\u20B1 " + String.valueOf(totalAmount));
                amount_of_coupon_discount.setText("\u20B1 " + String.valueOf(coupon_discount));
                amount_of_points_discount.setText("\u20B1 " + String.valueOf(points_discount));
                total_amount.setText("\u20B1 " + String.valueOf(discounted_total));
                /* discounts and total block*/

                SummaryAdapter adapter = new SummaryAdapter(SummaryActivity.this, items);
                order_summary.setAdapter(adapter);
            }
        }, new ErrorListener<VolleyError>() {
            public void getError(VolleyError error) {
                Log.d("shoppingcart0", error + "");
                Toast.makeText(getBaseContext(), "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Summary");
        myToolBar.setNavigationIcon(R.drawable.ic_back);

        change_id.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.change_id:
                Intent intent = new Intent(this, DeliverPickupOption.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("order_model", order_model);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
