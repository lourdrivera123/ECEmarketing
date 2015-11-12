package com.example.zem.patientcareapp.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.DbHelper;
import com.example.zem.patientcareapp.GoogleMapsActivity;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.MasterTabActivity;
import com.example.zem.patientcareapp.Network.ListOfPatientsRequest;
import com.example.zem.patientcareapp.Network.PostRequest;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.ServerRequest;
import com.example.zem.patientcareapp.SidebarActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class HomeTileFragment extends Fragment implements View.OnClickListener {
    LinearLayout orderLayout, refillLayout, pointsLayout, prescriptionLayout, consultationLayout;
    ImageView sideBar_overlay;
    TextView notifConsultation;

    ServerRequest serverRequest;
    DbHelper db;

    static int patientID;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_home_layout, container, false);

        context = getActivity();
        db = new DbHelper(context);
        showOverLay();

        orderLayout = (LinearLayout) rootView.findViewById(R.id.orderLayout);
        refillLayout = (LinearLayout) rootView.findViewById(R.id.refillLayout);
        pointsLayout = (LinearLayout) rootView.findViewById(R.id.pointsLayout);
        prescriptionLayout = (LinearLayout) rootView.findViewById(R.id.prescriptionLayout);
        consultationLayout = (LinearLayout) rootView.findViewById(R.id.consultationLayout);
        notifConsultation = (TextView) rootView.findViewById(R.id.notifConsultation);

        orderLayout.setOnClickListener(this);
        refillLayout.setOnClickListener(this);
        pointsLayout.setOnClickListener(this);
        prescriptionLayout.setOnClickListener(this);
        consultationLayout.setOnClickListener(this);

        patientID = SidebarActivity.getUserID();
        serverRequest = new ServerRequest();

        return rootView;
    }

    @Override
    public void onResume() {
        ListOfPatientsRequest.getJSONobj(getActivity(), "get_consultations_notif&patient_ID=" + patientID, new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                try {
                    int success = response.getInt("success");

                    if (success == 1) {
                        JSONArray json_mysql = response.getJSONArray("consultations");
                        notifConsultation.setVisibility(View.VISIBLE);

                        for (int x = 0; x < json_mysql.length(); x++) {
                            JSONObject obj = json_mysql.getJSONObject(x);

                            if (!db.updateSomeConsultation(obj))
                                Toast.makeText(getActivity(), "Error occurred", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        notifConsultation.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Server error occurred", Toast.LENGTH_SHORT).show();
                    Log.d("hometilefrag 2", e + "");
                }
            }
        }, new ErrorListener<VolleyError>() {
            @Override
            public void getError(VolleyError e) {
                Log.d("hometilefrag3", e + "");
                Toast.makeText(context, "Please check your Internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        final Intent intent = new Intent(getActivity(), MasterTabActivity.class);
        switch (v.getId()) {

            case R.id.orderLayout:
                Intent intent1 = new Intent(getActivity(), GoogleMapsActivity.class);
                startActivity(intent1);
                break;

            case R.id.refillLayout:
                intent.putExtra("selected", 0);
                startActivity(intent);
                break;

            case R.id.pointsLayout:
                intent.putExtra("selected", 1);
                startActivity(intent);
                break;

            case R.id.prescriptionLayout:
                intent.putExtra("selected", 2);
                startActivity(intent);
                break;

            case R.id.consultationLayout:
                HashMap<String, String> hashMap = new HashMap();
                hashMap.put("request", "crud");
                hashMap.put("table", "consultations");
                hashMap.put("action", "update_with_custom_where_clause");
                hashMap.put("isRead", String.valueOf(1));
                hashMap.put("custom_where_clause", "patient_id = " + String.valueOf(SidebarActivity.getUserID()) + " and isRead=0 and is_approved!=0");

                final ProgressDialog pdialog = new ProgressDialog(getActivity());
                pdialog.setCancelable(false);
                pdialog.setMessage("Loading...");
                pdialog.show();

                PostRequest.send(getActivity(), hashMap, serverRequest, new RespondListener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject response) {
                        try {
                            int success = response.getInt("success");
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Server error occurred", Toast.LENGTH_SHORT).show();
                            Log.d("hometilefrag", e + "");
                        }
                        pdialog.dismiss();
                    }
                }, new ErrorListener<VolleyError>() {
                    public void getError(VolleyError error) {
                        pdialog.dismiss();
                        Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                    }
                });

                intent.putExtra("selected", 3);
                startActivity(intent);
                break;
        }
    }

    private void showOverLay() {
        if (db.checkOverlay("HomeTile", "check")) {

        } else {
            final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
            dialog.setContentView(R.layout.overlay_hometile);

            sideBar_overlay = (ImageView) dialog.findViewById(R.id.sideBar_overlay);
            LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.overLayHometile);
            layout.setAlpha((float) 0.8);

            sideBar_overlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (db.checkOverlay("HomeTile", "insert"))
                        dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}
