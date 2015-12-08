package com.example.zem.patientcareapp.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.zem.patientcareapp.Controllers.DbHelper;
import com.example.zem.patientcareapp.ConfigurationModule.Helpers;
import com.example.zem.patientcareapp.Controllers.PromoController;
import com.example.zem.patientcareapp.adapter.LazyAdapter;
import com.example.zem.patientcareapp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dexter B. on 7/6/2015.
 */
public class PromoFragment extends Fragment {
    View root_view;
    ListView promoItems;
    Helpers helpers;
    DbHelper dbHelper;
    PromoController pc;
    LazyAdapter adapter;
    ArrayList<HashMap<String, String>> promoDiscounts;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_list_of_doctors_fragment, container, false);
        root_view = rootView;
        promoItems = (ListView) root_view.findViewById(R.id.list_of_doctors);

        helpers = new Helpers();
        dbHelper = new DbHelper(getActivity());
        pc = new PromoController(getActivity());

        promoDiscounts = pc.getPromo();

        adapter = new LazyAdapter(getActivity(), promoDiscounts, "promo_items");
        promoItems.setAdapter(adapter);

        promoItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        return rootView;
    }
}
