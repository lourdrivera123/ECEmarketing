package com.example.zem.patientcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class HomeTileActivityClone extends Fragment implements View.OnClickListener {
    LinearLayout profileLayout, patientHistoryLayout, prescriptionLayout, doctorsLayout, consultationLayout, productsLayout, cartLayout, promosLayout, newsLayout;
    static DbHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_tile_layout, container, false);

        profileLayout = (LinearLayout) rootView.findViewById(R.id.profileLayout);
        patientHistoryLayout = (LinearLayout) rootView.findViewById(R.id.patientHistoryLayout);
        prescriptionLayout = (LinearLayout) rootView.findViewById(R.id.prescriptionLayout);
        doctorsLayout = (LinearLayout) rootView.findViewById(R.id.doctorsLayout);
        consultationLayout = (LinearLayout) rootView.findViewById(R.id.consultationLayout);
        productsLayout = (LinearLayout) rootView.findViewById(R.id.productsLayout);
        cartLayout = (LinearLayout) rootView.findViewById(R.id.cartLayout);
        promosLayout = (LinearLayout) rootView.findViewById(R.id.promosLayout);
        newsLayout = (LinearLayout) rootView.findViewById(R.id.newsLayout);

        profileLayout.setOnClickListener(this);
        patientHistoryLayout.setOnClickListener(this);
        prescriptionLayout.setOnClickListener(this);
        doctorsLayout.setOnClickListener(this);
        consultationLayout.setOnClickListener(this);
        productsLayout.setOnClickListener(this);
        cartLayout.setOnClickListener(this);
        promosLayout.setOnClickListener(this);
        newsLayout.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), MasterTabActivity.class);
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
