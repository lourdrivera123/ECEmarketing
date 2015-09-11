package com.example.zem.patientcareapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.ListOfPatientsRequest;
import com.example.zem.patientcareapp.Network.PostRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeTileActivityClone extends Fragment implements View.OnClickListener {
    LinearLayout profileLayout, patientHistoryLayout, doctorsLayout, consultationLayout, productsLayout, cartLayout, promosLayout, newsLayout;
    RelativeLayout prescriptionLayout;
    TextView newNotif;

    ArrayList<Integer> arrayOfIDs;

    static DbHelper dbHelper;
    ServerRequest serverRequest;

    static int patientID;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_tile_layout, container, false);

        context = getActivity();

        profileLayout = (LinearLayout) rootView.findViewById(R.id.profileLayout);
        patientHistoryLayout = (LinearLayout) rootView.findViewById(R.id.patientHistoryLayout);
        prescriptionLayout = (RelativeLayout) rootView.findViewById(R.id.prescriptionLayout);
        doctorsLayout = (LinearLayout) rootView.findViewById(R.id.doctorsLayout);
        consultationLayout = (LinearLayout) rootView.findViewById(R.id.consultationLayout);
        productsLayout = (LinearLayout) rootView.findViewById(R.id.productsLayout);
        cartLayout = (LinearLayout) rootView.findViewById(R.id.cartLayout);
        promosLayout = (LinearLayout) rootView.findViewById(R.id.promosLayout);
        newsLayout = (LinearLayout) rootView.findViewById(R.id.newsLayout);
        newNotif = (TextView) rootView.findViewById(R.id.newNotif);

        profileLayout.setOnClickListener(this);
        patientHistoryLayout.setOnClickListener(this);
        prescriptionLayout.setOnClickListener(this);
        doctorsLayout.setOnClickListener(this);
        consultationLayout.setOnClickListener(this);
        productsLayout.setOnClickListener(this);
        cartLayout.setOnClickListener(this);
        promosLayout.setOnClickListener(this);
        newsLayout.setOnClickListener(this);

        patientID = SidebarActivity.getUserID();
        serverRequest = new ServerRequest();

        return rootView;
    }

    @Override
    public void onResume() {
        arrayOfIDs = new ArrayList();

        ListOfPatientsRequest.getJSONobj(getActivity(), "get_notifications&patient_ID=" + patientID + "&table_name=patient_prescriptions", new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                try {
                    int success = response.getInt("success");

                    if (success == 1) {
                        newNotif.setVisibility(View.VISIBLE);

                        JSONArray json_mysql = response.getJSONArray("notifications");

                        for (int x = 0; x < json_mysql.length(); x++) {
                            JSONObject json_obj = json_mysql.getJSONObject(x);
                            arrayOfIDs.add(json_obj.getInt("id"));
                        }
                        newNotif.setText(json_mysql.length() + "");
                    } else
                        newNotif.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Server error occurred", Toast.LENGTH_SHORT).show();
                    System.out.print("src: <HomeTileActivityClone>: " + e.toString());
                }
            }
        }, new ErrorListener<VolleyError>() {
            @Override
            public void getError(VolleyError error) {
                Log.d("Error in HomeTileActivityClone", error + "");
                Toast.makeText(context, "Please check your Internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        final Intent intent = new Intent(getActivity(), MasterTabActivity.class);
        switch (v.getId()) {

            case R.id.profileLayout:
                intent.putExtra("selected", 0);
                startActivity(intent);
                break;

            case R.id.patientHistoryLayout:
                intent.putExtra("selected", 1);
                startActivity(intent);
                break;

            case R.id.prescriptionLayout:
                for (int x = 0; x < arrayOfIDs.size(); x++) {
                    HashMap<String, String> map = new HashMap();
                    map.put("id", String.valueOf(arrayOfIDs.get(x)));
                    map.put("table", "notifications");
                    map.put("request", "crud");
                    map.put("action", "update");
                    map.put("isRead", String.valueOf(1));

                    PostRequest.send(getActivity(), map, serverRequest, new RespondListener<JSONObject>() {
                        @Override
                        public void getResult(JSONObject response) {
                            try {
                                Toast.makeText(getActivity(), "Prescription has been approved", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                System.out.print("src: <HomeTileActivityClone> " + e.toString());
                            }
                        }
                    }, new ErrorListener<VolleyError>() {
                        @Override
                        public void getError(VolleyError error) {
                            System.out.print("src: <HomeTileActivityClone>: " + error.toString());
                            Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                intent.putExtra("selected", 2);
                startActivity(intent);
                break;

            case R.id.doctorsLayout:
                intent.putExtra("selected", 3);
                startActivity(intent);
                break;

            case R.id.consultationLayout:
                intent.putExtra("selected", 4);
                startActivity(intent);
                break;

            case R.id.productsLayout:
                intent.putExtra("selected", 5);
                startActivity(intent);
                break;

            case R.id.promosLayout:
                intent.putExtra("selected", 6);
                startActivity(intent);
                break;

            case R.id.cartLayout:
                intent.putExtra("selected", 7);
                startActivity(intent);
                break;

            case R.id.newsLayout:
                intent.putExtra("selected", 8);
                break;
        }
    }
}
