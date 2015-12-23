package com.example.zem.patientcareapp.CheckoutModule;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.zem.patientcareapp.Activities.ShoppingCart;
import com.example.zem.patientcareapp.Customizations.GlowingText;
import com.example.zem.patientcareapp.Model.OrderModel;
import com.example.zem.patientcareapp.R;

/**
 * Created by Zem on 11/19/2015.
 */
public class PromosDiscounts extends AppCompatActivity implements View.OnClickListener {
    Toolbar myToolBar;
    GlowingText glowButton;
    Button redeem_points, nxt_btn;
    SeekBar blood_seeker;
    TextView stepping_stone;
    float 	startGlowRadius = 25f,
            minGlowRadius   = 2f,
            maxGlowRadius   = 16f;
    OrderModel order_model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promos_and_discounts_layout);

        myToolBar = (Toolbar) findViewById(R.id.myToolBar);
        redeem_points = (Button) findViewById(R.id.redeem_points);
//        blood_seeker = (SeekBar) findViewById(R.id.blood_seeker);
        nxt_btn = (Button) findViewById(R.id.next_btn);
//        stepping_stone = (TextView) findViewById(R.id.stepping_stone);

//        stepping_stone.setText("Step 4/5");
//        blood_seeker.setProgress(80);

        Intent get_intent = getIntent();
        Bundle bundle= get_intent.getExtras();

        order_model = (OrderModel) get_intent.getSerializableExtra("order_model");

//        if(order_model.getMode_of_delivery().equals("delivery")){
//            stepping_stone.setText("Step 4/5");
//            blood_seeker.setProgress(80);
//        } else {
//            stepping_stone.setText("Step 2/3");
//            blood_seeker.setProgress(67);
//        }

        order_model.setCoupon_discount(0.1);
        order_model.setPoints_discount(0.1);

//        blood_seeker.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });

        Log.d("putangina", order_model.getMode_of_delivery() + " " + order_model.getRecipient_address() + " " + order_model.getRecipient_name() + " " + order_model.getRecipient_contactNumber());


        glowButton = new GlowingText(
                PromosDiscounts.this,               // Pass activity Object
                getBaseContext(),       // Context
                redeem_points,                 // Button View
                minGlowRadius,          // Minimum Glow Radius
                maxGlowRadius,          // Maximum Glow Radius
                startGlowRadius,        // Start Glow Radius - Increases to MaxGlowRadius then decreases to MinGlowRadius.
                Color.WHITE,              // Glow Color (int)
                2);                     // Glowing Transition Speed (Range of 1 to 10)

        nxt_btn.setOnClickListener(this);

        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Promos & Discounts");
        myToolBar.setNavigationIcon(R.drawable.ic_back);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, ShoppingCart.class));
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, SummaryActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("order_model", order_model);
        intent.putExtra("order_model", order_model);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Don't forget to use this.
        glowButton.stopGlowing();
    }
}
