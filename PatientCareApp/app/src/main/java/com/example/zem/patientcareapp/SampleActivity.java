package com.example.zem.patientcareapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.GetterSetter.Patient;
import com.example.zem.patientcareapp.GetterSetter.Settings;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.ListOfPatientsRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User PC on 10/1/2015.
 */
public class SampleActivity extends Activity {
//    Patient patient;
//    Settings settings;
//    DbHelper db;
//
//    ProgressDialog dialog;
//    Context context;
//
//    int lvl = 0;
//    String referral_ID = "";
//
//    LinearLayout linear;
//    LinearLayout linearLayout;
//    TextView[] tv;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.view_referrals_layout);
//
//        db = new DbHelper(this);
//        settings = db.getAllSettings();
//        patient = db.getCurrentLoggedInPatient();
//
//        lvl = settings.getLvl_limit();
//        referral_ID = patient.getReferral_id();
//
//        linear = (LinearLayout) findViewById(R.id.linear);
//
//        dialog = new ProgressDialog(SampleActivity.this);
//        dialog.setMessage("Please wait...");
//        dialog.show();
//
//        ListOfPatientsRequest.getJSONobj(SampleActivity.this, "get_referrals_by_user", new RespondListener<JSONObject>() {
//            @Override
//            public void getResult(JSONObject response) {
//                try {
//                    int success = response.getInt("success");
//
//                    if (success == 1) {
//                        JSONArray json_array_mysql = response.getJSONArray("patients");
//                        tv = new TextView[json_array_mysql.length()];
//                        int count = 0;
//
//                        for (int x = 0; x < json_array_mysql.length(); x++) {
//                            JSONObject json_obj = json_array_mysql.getJSONObject(x);
//
//                            if (json_obj.get("referred_by").equals(referral_ID)) {
//                                tv[x] = new TextView(SampleActivity.this);
//                                count += 1;
//
//                                String name = json_obj.getString("fname") + " " + json_obj.getString("lname");
//
//                                tv[x].setText(name);
//                                tv[x].setTag(json_obj.getString("referral_id"));
//                                tv[x].setId(count);
//
//                                linear.addView(tv[x]);
//
//                                final int finalX = x;
//                                final int[] clicked = {0};
//
//                                tv[x].setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        linearLayout = new LinearLayout(SampleActivity.this);
//                                        linearLayout.setOrientation(LinearLayout.VERTICAL);
//
//                                        for (int x = 0; x <= 4; x++) {
//                                            TextView textView = new TextView(SampleActivity.this);
//                                            textView.setText(x + "");
//                                            linearLayout.addView(textView);
//                                        }
//
//                                        Toast.makeText(SampleActivity.this, tv[finalX].getId() + "", Toast.LENGTH_SHORT).show();
//
//                                        if (clicked[0] == 0) {
//                                            linear.addView(linearLayout, tv[finalX].getId());
//                                            clicked[0] += 1;
//                                        } else {
//                                            linear.removeView(linearLayout);
//                                            linear.removeViewAt(tv[finalX].getId());
//                                            clicked[0] = 0;
//                                        }
//                                    }
//                                });
//
//                            }
//                        }
//                    }
//                } catch (JSONException e) {
//                    Log.d("error", e + "");
//                    Toast.makeText(SampleActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
//                }
//                dialog.dismiss();
//            }
//        }, new ErrorListener<VolleyError>() {
//            public void getError(VolleyError error) {
//                dialog.dismiss();
//                Toast.makeText(context, "Couldn't refresh list. Plealise check your Internet connection", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
