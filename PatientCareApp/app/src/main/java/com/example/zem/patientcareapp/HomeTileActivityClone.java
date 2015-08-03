package com.example.zem.patientcareapp;

import android.app.ActionBar;
//import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.example.zem.patientcareapp.GetterSetter.Patient;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zem on 7/21/2015.
 */
public class HomeTileActivityClone extends Fragment implements View.OnClickListener {
    ImageButton profile_btn, news_btn, promos_btn, cart_btn, history_btn, products_btn, test_results_btn, doctors_btn,
            consultation_btn;
    FragmentTransaction fragmentTransaction;
    AutoCompleteTextView search_product;

    public static SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static String uname;
    public static int userID;

    static Patient patient;
    static DbHelper dbHelper;

    ArrayAdapter search_adapter;
    ArrayList<HashMap<String, String>> hash_allProducts;
    ArrayList<String> products;

    int productID = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_tile_layout, container, false);

//        hometile = getActivity();
//        ActionBar actionbar = getActionBar();
//        MainActivity.setCustomActionBar(actionbar);
//
//        sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
//        editor = sharedpreferences.edit();

//        dbHelper = new DbHelper(getActivity());
//        hash_allProducts = dbHelper.getAllProducts();
//        products = new ArrayList();
//
//        for (int x = 0; x < hash_allProducts.size(); x++) {
//            products.add(hash_allProducts.get(x).get(dbHelper.PRODUCT_NAME));
//        }
//        createCustomSearchBar(actionbar);
//
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();

        profile_btn = (ImageButton) rootView.findViewById(R.id.profile_btn);
        news_btn = (ImageButton) rootView.findViewById(R.id.news_btn);
        promos_btn = (ImageButton) rootView.findViewById(R.id.promos_btn);
        doctors_btn = (ImageButton) rootView.findViewById(R.id.doctors_btn);
        history_btn = (ImageButton) rootView.findViewById(R.id.history_btn);
        test_results_btn = (ImageButton) rootView.findViewById(R.id.test_results_btn);
        cart_btn = (ImageButton) rootView.findViewById(R.id.cart_btn);
        products_btn = (ImageButton) rootView.findViewById(R.id.products_btn);
        consultation_btn = (ImageButton) rootView.findViewById(R.id.consultation_btn);

        consultation_btn.setOnClickListener(this);
        profile_btn.setOnClickListener(this);
        news_btn.setOnClickListener(this);
        promos_btn.setOnClickListener(this);
        doctors_btn.setOnClickListener(this);
        history_btn.setOnClickListener(this);
        test_results_btn.setOnClickListener(this);
        cart_btn.setOnClickListener(this);
        products_btn.setOnClickListener(this);

//        if (dbHelper.checkUserIfRegistered(getUname()) > 0) {
//
//        } else {
//            editor.clear();
//            editor.commit();
//            moveTaskToBack(true);
//            HomeTileActivity.this.finish();
//            startActivity(new Intent(this, MainActivity.class));
//        }
        return rootView;

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), MasterTabActivity.class);
        switch (v.getId()) {

            case R.id.profile_btn:
                intent.putExtra("selected", 0);
                startActivity(intent);
                break;

            case R.id.history_btn:
                intent.putExtra("selected", 1);
                startActivity(intent);
                break;

            case R.id.test_results_btn:
                intent.putExtra("selected", 2);
                startActivity(intent);
                break;

            case R.id.doctors_btn:
                intent.putExtra("selected", 3);
                startActivity(intent);
                break;

            case R.id.consultation_btn:
                intent.putExtra("selected", 4);
                startActivity(intent);
                break;

            case R.id.products_btn:
                intent.putExtra("selected", 5);
                startActivity(intent);
                break;

            case R.id.cart_btn:
                intent.putExtra("selected", 6);
                startActivity(intent);
                break;

            case R.id.promos_btn:
                intent.putExtra("selected", 7);
                startActivity(intent);
                break;

            case R.id.news_btn:
                intent.putExtra("selected", 8);
                break;
        }
    }

//    @Override
//    public void onBackPressed() {
//        MainActivity.main.finish();
//        this.finish();
//        super.onBackPressed();
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.logout_menu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.logout) {
//            editor.clear();
//            editor.commit();
//            startActivity(new Intent(this, MainActivity.class));
//            this.finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    public static String getUname() {
//        uname = sharedpreferences.getString("nameKey", "DEFAULT");
//        return uname;
//    }
//
//    public static int getUserID() {
//        patient = dbHelper.getloginPatient(getUname());
//        userID = patient.getServerID();
//
//        return userID;
//    }

//    public void createCustomSearchBar(ActionBar actionbar) {
//        actionbar.setDisplayShowHomeEnabled(true);
//        actionbar.setDisplayShowTitleEnabled(false);
//        LayoutInflater mInflater = LayoutInflater.from(this);
//
//        View mCustomView = mInflater.inflate(R.layout.searchview_layout, null);
//
//        search_product = (AutoCompleteTextView) mCustomView.findViewById(R.id.search_product);
//        search_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, products);
//        search_product.setAdapter(search_adapter);
//
//        search_product.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                search_product.setCursorVisible(true);
//                search_product.setFocusableInTouchMode(true);
//                search_product.setFocusable(true);
//            }
//        });
//
//        search_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String item_clicked = parent.getItemAtPosition(position).toString();
//                int itemID = products.indexOf(item_clicked);
//                productID = Integer.parseInt(hash_allProducts.get(itemID).get(dbHelper.SERVER_PRODUCT_ID));
//
//                Intent intent = new Intent(getBaseContext(), SelectedProductActivity.class);
//                intent.putExtra(SelectedProductActivity.PRODUCT_ID, productID);
//                intent.putExtra(SelectedProductActivity.UP_ACTIVITY, "HomeTile");
//                startActivity(intent);
//                HomeTileActivity.this.finish();
//            }
//        });
//
//        actionbar.setCustomView(mCustomView);
//        actionbar.setDisplayShowCustomEnabled(true);
//    }
}