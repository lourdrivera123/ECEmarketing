package com.example.zem.patientcareapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

/**
 * Created by Dexter B. on 5/18/2015.
 */
public class ProductCategoriesFragment extends Fragment {
    ListView lv_categories;
    Helpers helpers;
    View root_view;
    Sync sync;
    RequestQueue queue;
    ProgressDialog pDialog;
    DbHelper dbHelper;
    ArrayList<ProductCategory> category_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.categories_layout, container, false);
        root_view = rootView;

        dbHelper = new DbHelper(getActivity());
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();


        if (helpers.isNetworkAvailable(getActivity())) {
            sync = new Sync();
            sync.init(getActivity(), "get_product_categories", "product_categories", "product_categories_id");
            queue = sync.getQueue();


            rootView.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after 1 second
                    category_list = dbHelper.getAllProductCategories();

                    populateListView(rootView, category_list);
                    pDialog.hide();
                }
            }, 1000);

        } else {
            Log.d("Connected to internet", "no");
            category_list = dbHelper.getAllProductCategories();

            populateListView(rootView, category_list);
            pDialog.hide();
        }

        return rootView;
    }

    public void populateListView(View rootView, ArrayList<ProductCategory> categories){
        lv_categories = (ListView) rootView.findViewById(R.id.categories);

    }
}
