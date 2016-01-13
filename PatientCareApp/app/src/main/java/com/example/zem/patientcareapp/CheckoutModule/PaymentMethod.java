package com.example.zem.patientcareapp.CheckoutModule;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.zem.patientcareapp.Controllers.OrderPreferenceController;
import com.example.zem.patientcareapp.Model.OrderModel;
import com.example.zem.patientcareapp.R;

/**
 * Created by Zem on 11/18/2015.
 */
public class PaymentMethod extends AppCompatActivity implements View.OnClickListener {

    Toolbar myToolBar;
    SeekBar blood_seeker;
    TextView stepping_stone;
    Intent get_intent;
    OrderModel order_model;
    LinearLayout cash, visa_or_mastercard, paypal;
    Intent intent;
    String payment_method;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_method_layout);

        get_intent = getIntent();
//        Bundle bundle= get_intent.getExtras();

        order_model = (OrderModel) get_intent.getSerializableExtra("order_model");

        myToolBar = (Toolbar) findViewById(R.id.myToolBar);
        blood_seeker = (SeekBar) findViewById(R.id.blood_seeker);
        stepping_stone = (TextView) findViewById(R.id.stepping_stone);
        cash = (LinearLayout) findViewById(R.id.cash);
        visa_or_mastercard = (LinearLayout) findViewById(R.id.visa_or_mastercard);
        paypal = (LinearLayout) findViewById(R.id.paypal);

        if (order_model.getMode_of_delivery().equals("delivery")) {
            stepping_stone.setText("Step 4/4");
            blood_seeker.setProgress(100);
        } else {
            stepping_stone.setText("Step 3/3");
            blood_seeker.setProgress(100);
        }

        blood_seeker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        cash.setOnClickListener(this);
        visa_or_mastercard.setOnClickListener(this);
        paypal.setOnClickListener(this);

        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Payment Method");
        myToolBar.setNavigationIcon(R.drawable.ic_back);
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
        switch (v.getId()) {
            case R.id.cash:
//                order_model.setPayment_method("cash");
                payment_method = "cash_on_delivery";
                ok_lets_go();
                break;
            case R.id.visa_or_mastercard:
                payment_method = "visa_or_mastercard";
//                order_model.setPayment_method("visa_or_mastercard");
                ok_lets_go();
                break;
            case R.id.paypal:
                payment_method = "paypal";
//                order_model.setPayment_method("order_model");
                ok_lets_go();
                break;
            default:
                break;
        }
    }

    public void ok_lets_go() {
        order_model.setPayment_method(payment_method);
        OrderPreferenceController opc = new OrderPreferenceController(this);
        if (opc.savePreference(order_model)) {
            intent = new Intent(this, PromosDiscounts.class);
            intent.putExtra("order_model", order_model);
            startActivity(intent);
            this.finish();
        } else {
            Log.d("ot", "what  the fuck is wrong ?");
        }
    }
}
