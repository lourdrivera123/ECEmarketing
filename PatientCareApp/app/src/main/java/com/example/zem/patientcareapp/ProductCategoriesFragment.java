package com.example.zem.patientcareapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

/**
 * Created by Dexter B. on 5/18/2015.
 */
public class ProductCategoriesFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    Spinner lv_categories;
    ListView lv_subcategories;
    Helpers helpers;
    View root_view;
    Sync sync;
    RequestQueue queue;
    ProgressDialog pDialog;
    DbHelper dbHelper;
    String[] category_list;
    public static ArrayAdapter category_list_adapter;
    Queue fq ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.categories_layout, container, false);
        root_view = rootView;

        dbHelper = new DbHelper(getActivity());
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        helpers = new Helpers();

        lv_categories = (Spinner) rootView.findViewById(R.id.categories);

        final String list[] = {};
        if (helpers.isNetworkAvailable(getActivity())) {
            sync = new Sync();
            sync.init(getActivity(), "get_product_categories", "product_categories", "id");
            queue = sync.getQueue();

            sync.init(getActivity(), "get_product_subcategories&cat=all", "product_subcategories", "id");
            queue = sync.getQueue();

            rootView.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after 3 seconds
                    category_list = dbHelper.getAllProductcategoriesArray();
                    populateListView(rootView, category_list);
                    pDialog.hide();
                }
            }, 3000);

        } else {
            Log.d("Connected to internet", "no");
            category_list = dbHelper.getAllProductcategoriesArray();

            populateListView(rootView, category_list);
            pDialog.hide();
        }

        return rootView;
    }

    public void populateListView(View rootView, String[] categories){
        lv_categories = (Spinner) rootView.findViewById(R.id.categories);
        lv_subcategories = (ListView) rootView.findViewById(R.id.subcategories);

        category_list_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, categories);
        lv_categories.setAdapter(category_list_adapter);
        lv_subcategories.setVisibility(View.GONE);
//        lv_categories.setOnItemClickListener(this);
        lv_categories.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ProductsFragment.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = ((TextView)view).getText().toString();
        Toast.makeText(getActivity(), item, Toast.LENGTH_LONG).show();

        int categoryId = dbHelper.categoryGetIdByName(item);
        String []arr = dbHelper.getAllProductSubCategoriesArray(categoryId);

        Dialog dialog = new Dialog(getActivity());
        dialog.setTitle(item + " subcategories");
        dialog.setContentView(R.layout.categories_layout);

        if(position != 0) dialog.show();

        TextView browseBy = (TextView) dialog.findViewById(R.id.browse_by);
        browseBy.setText("");
        lv_subcategories = (ListView) dialog.findViewById(R.id.subcategories);
        lv_categories = (Spinner) dialog.findViewById(R.id.categories);

        category_list_adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arr);
        lv_subcategories.setAdapter(category_list_adapter);
        lv_categories.setVisibility(View.GONE);
        lv_subcategories.setOnItemClickListener(this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
