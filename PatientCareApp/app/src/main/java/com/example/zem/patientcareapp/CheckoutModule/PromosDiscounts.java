package com.example.zem.patientcareapp.CheckoutModule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.Activities.MainActivity;
import com.example.zem.patientcareapp.Activities.ReferralActivity;
import com.example.zem.patientcareapp.Activities.ShoppingCart;
import com.example.zem.patientcareapp.Controllers.PatientController;
import com.example.zem.patientcareapp.Customizations.GlowingText;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Interface.StringRespondListener;
import com.example.zem.patientcareapp.Model.OrderModel;
import com.example.zem.patientcareapp.Model.Patient;
import com.example.zem.patientcareapp.Network.PostRequest;
import com.example.zem.patientcareapp.Network.StringRequests;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.SidebarModule.SidebarActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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
    TextView points_text;
    Patient patient;
    PatientController pc;
    LinearLayout points_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promos_and_discounts_layout);

        myToolBar = (Toolbar) findViewById(R.id.myToolBar);
        redeem_points = (Button) findViewById(R.id.redeem_points);
        points_text = (TextView) findViewById(R.id.points_text);
        nxt_btn = (Button) findViewById(R.id.next_btn);
        points_layout = (LinearLayout) findViewById(R.id.points_layout);

        pc = new PatientController(this);
        patient = pc.getloginPatient(SidebarActivity.getUname());

        StringRequests.getString(PromosDiscounts.this, "db/get.php?q=get_patient_points&patient_id="+patient.getServerID(), new StringRespondListener<String>() {
            @Override
            public void getResult(String response) {
                patient.setPoints(Double.parseDouble(response));
                pc.updatePoints(Double.parseDouble(response));

                if(patient.getPoints() > 0){
                    points_layout.setVisibility(View.VISIBLE);
                    points_text.setText("Your current referral points is "+patient.getPoints()+" \n(1 point = 1 peso)\nClick 'Redeem Points' to use your points");
                    glowButton = new GlowingText(
                            PromosDiscounts.this,               // Pass activity Object
                            getBaseContext(),       // Context
                            redeem_points,                 // Button View
                            minGlowRadius,          // Minimum Glow Radius
                            maxGlowRadius,          // Maximum Glow Radius
                            startGlowRadius,        // Start Glow Radius - Increases to MaxGlowRadius then decreases to MinGlowRadius.
                            Color.WHITE,              // Glow Color (int)
                            2);                     // Glowing Transition Speed (Range of 1 to 10)
                }
            }
        }, new ErrorListener<VolleyError>() {
            public void getError(VolleyError error) {
                Log.d("error for sumrhing", error + "");
            }
        });

        Intent get_intent = getIntent();

        order_model = (OrderModel) get_intent.getSerializableExtra("order_model");

        order_model.setCoupon_discount(0.1);
        order_model.setPoints_discount(0.1);

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
