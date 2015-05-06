package com.example.zem.patientcareapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by Zem on 4/28/2015.
 */
public class ContactsFragment extends Fragment implements View.OnClickListener {

    Spinner address_region;
    Resources res;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.patient_contact_info, container, false);

        address_region = (Spinner) rootView.findViewById(R.id.address_region);
        ArrayAdapter<String> regions_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.regions));
        address_region.setAdapter(regions_adapter);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), AccountFragment.class));
    }
}
