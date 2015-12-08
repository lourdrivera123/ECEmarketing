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
import com.example.zem.patientcareapp.Controllers.BasketController;
import com.example.zem.patientcareapp.Controllers.DbHelper;
import com.example.zem.patientcareapp.Controllers.PatientController;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Model.OrderModel;
import com.example.zem.patientcareapp.Network.GetRequest;
import com.example.zem.patientcareapp.Customizations.NonScrollListView;
import com.example.zem.patientcareapp.R;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zem on 11/20/2015.
 */
public class SummaryActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar myToolBar;
    Button change_id, order_now_btn;
    OrderModel order_model;
    Intent get_intent;
    DbHelper dbHelper;
    PatientController pc;
    BasketController bc;

    ArrayList<HashMap<String, String>> items;
    Double totalAmount = 0.0;
    NonScrollListView order_summary;
    TextView amount_subtotal, amount_of_coupon_discount, amount_of_points_discount, total_amount;
    LinearLayout points_layout, coupon_layout, subtotal_layout, total_layout;
    TextView order_receiving_option, address_option, recipient_option, payment_option, recipient_contact_number, address_or_branch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summarylayout);

        get_intent = getIntent();

        order_model = (OrderModel) get_intent.getSerializableExtra("order_model");

        myToolBar = (Toolbar) findViewById(R.id.myToolBar);
        change_id = (Button) findViewById(R.id.change_id);
        order_summary = (NonScrollListView) findViewById(R.id.order_summary);

        amount_subtotal = (TextView) findViewById(R.id.amount_subtotal);
        amount_of_coupon_discount = (TextView) findViewById(R.id.amount_of_coupon_discount);
        amount_of_points_discount = (TextView) findViewById(R.id.amount_of_points_discount);
        total_amount = (TextView) findViewById(R.id.total_amount);

        order_receiving_option = (TextView) findViewById(R.id.order_receiving_option);
        address_option = (TextView) findViewById(R.id.address_option);
        recipient_option = (TextView) findViewById(R.id.recipient_option);
        payment_option = (TextView) findViewById(R.id.payment_option);
        recipient_contact_number = (TextView) findViewById(R.id.recipient_contact_number);
        address_or_branch = (TextView) findViewById(R.id.address_or_branch);

        points_layout = (LinearLayout) findViewById(R.id.points_layout);
        coupon_layout = (LinearLayout) findViewById(R.id.coupon_layout);
        subtotal_layout = (LinearLayout) findViewById(R.id.subtotal_layout);
        total_layout = (LinearLayout) findViewById(R.id.total_layout);
        order_now_btn = (Button) findViewById(R.id.order_now_btn);

        dbHelper = new DbHelper(this);
        pc = new PatientController(this);
        bc = new BasketController(this);

        String url_raw = "get_basket_items&patient_id=" + pc.getCurrentLoggedInPatient().getServerID() + "&table=baskets";

        GetRequest.getJSONobj(this, url_raw, "baskets", "basket_id", new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                items = bc.getAllBasketItems(false); // returns all basket items for the currently loggedin patient

                for (HashMap<String, String> item : items) {
                    totalAmount = totalAmount + Double.parseDouble(item.get("item_subtotal"));
                }

                double coupon_discount = order_model.getCoupon_discount() * totalAmount;
                double points_discount = order_model.getPoints_discount() * totalAmount;
                double discounted_total = totalAmount - points_discount - coupon_discount;

                /* discounts and total block*/

                if (coupon_discount == 0.0)
                    coupon_layout.setVisibility(View.GONE);

                if (points_discount == 0.0)
                    points_layout.setVisibility(View.GONE);

                if (coupon_discount == 0.0 && points_discount == 0.0)
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

        if(order_model.getMode_of_delivery().equals("pickup")){
            address_or_branch.setText("Branch to pickup order");
            address_option.setText("ECE NAGA");
        } else {
            address_or_branch.setText("Address for delivery");
            address_option.setText(order_model.getRecipient_address());
        }
        order_receiving_option.setText(order_model.getMode_of_delivery());
        recipient_option.setText(order_model.getRecipient_name());
        payment_option.setText(order_model.getPayment_method());
        recipient_contact_number.setText(order_model.getRecipient_contactNumber());

        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Summary");
        myToolBar.setNavigationIcon(R.drawable.ic_back);

        change_id.setOnClickListener(this);
        order_now_btn.setOnClickListener(this);
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
                intent.putExtra("order_model", order_model);
                startActivity(intent);
                break;
            case R.id.order_now_btn:
                if(order_model.getPayment_method().equals("paypal")){
                    Intent paypal_intent = new Intent(this, PayPalCheckout.class);
                    paypal_intent.putExtra("order_model", order_model);
                    startActivity(paypal_intent);
                }
                break;
            default:
                break;
        }
    }
}
