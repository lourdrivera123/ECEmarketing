package com.example.zem.patientcareapp;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.zem.patientcareapp.Model.Product;
import com.example.zem.patientcareapp.Model.ProductSubCategory;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.GetRequest;
import com.example.zem.patientcareapp.adapter.LazyAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zem on 11/5/2015.
 */

public class ProductsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView list_of_products;
    Toolbar products_toolbar;
    TextView noOfResults;
    LinearLayout results_layout;


    LazyAdapter adapter;
    Helpers helpers;
    RequestQueue queue;
    static DbHelper dbHelper;

    public static Map<String, HashMap<String, String>> productQuantity;
    public static ArrayList<HashMap<String, String>> products_items;
    public static ArrayList<HashMap<String, String>> temp_products_items;
    ArrayList<HashMap<Integer, HashMap<String, String>>> searchProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_activity);

        showOverLay(this);

        results_layout = (LinearLayout) findViewById(R.id.results_layout);
        noOfResults = (TextView) findViewById(R.id.noOfResults);
        list_of_products = (ListView) findViewById(R.id.product_lists);

        products_toolbar = (Toolbar) findViewById(R.id.products_toolbar);
        setSupportActionBar(products_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Products");
        products_toolbar.setNavigationIcon(R.drawable.ic_back);

        dbHelper = new DbHelper(getBaseContext());
        queue = Volley.newRequestQueue(getBaseContext());
        helpers = new Helpers();

        searchProducts = new ArrayList();
        productQuantity = new HashMap();
        temp_products_items = new ArrayList();
        products_items = dbHelper.getAllProducts();
        temp_products_items.addAll(products_items);

        for (HashMap<String, String> map : products_items) {
            HashMap<String, String> tempMap = new HashMap();
            tempMap.put("id", map.get("id"));
            tempMap.put("product_id", map.get("product_id"));
            tempMap.put("qty_per_packing", map.get("qty_per_packing"));
            tempMap.put("packing", map.get("packing"));
            tempMap.put("temp_basket_qty", "0");
            tempMap.put("prescription_required", map.get("prescription_required"));
            productQuantity.put(map.get("product_id"), tempMap);

            HashMap<Integer, HashMap<String, String>> hash = new HashMap();
            HashMap<String, String> temp = new HashMap();
            temp.put("product_name", map.get("name"));
            temp.put("generic_name", map.get("generic_name"));
            hash.put(Integer.parseInt(tempMap.get("product_id")), temp);
            searchProducts.add(hash);
        }

        adapter = new LazyAdapter(this, products_items, "product_lists");
        list_of_products.setAdapter(adapter);
        list_of_products.setOnItemClickListener(this);

        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                results_layout.setVisibility(View.GONE);

                products_items = new ArrayList();
                products_items = dbHelper.getAllProducts();

                adapter = new LazyAdapter(ProductsActivity.this, products_items, "product_lists");
                list_of_products.setAdapter(adapter);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            case R.id.shoppingcart:
                startActivity(new Intent(this, ShoppingCartActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Product prod;
        prod = dbHelper.getProductById(Integer.parseInt(view.getTag().toString()));

        Intent intent = new Intent(getBaseContext(), SelectedProductActivity.class);
        intent.putExtra(SelectedProductActivity.PRODUCT_ID, prod.getProductId());
        startActivity(intent);
    }

    public static void showOverLay(Context context) {
        dbHelper = new DbHelper(context);

        if (!dbHelper.checkOverlay("Products", "check")) {
            final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
            dialog.setContentView(R.layout.products_overlay);

            LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.productsLayout);
            layout.setAlpha((float) 0.8);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dbHelper.checkOverlay("Products", "insert"))
                        dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            results_layout.setVisibility(View.VISIBLE);
            products_items.clear();
            int ctr = 0;
            String query = intent.getStringExtra(SearchManager.QUERY);

            for (int x = 0; x < searchProducts.size(); x++) {
                for (Map.Entry<Integer, HashMap<String, String>> ee : searchProducts.get(x).entrySet()) {
                    HashMap<String, String> values = ee.getValue();

                    if (values.get("product_name").toLowerCase().contains(query.toLowerCase()) || values.get("generic_name").toLowerCase().contains(query.toLowerCase())) {
                        HashMap<String, String> details = temp_products_items.get(x);
                        products_items.add(details);
                        ctr += 1;
                    }
                }
            }
            adapter.notifyDataSetChanged();
            noOfResults.setText(ctr + "");

            if (ctr == 0)
                Toast.makeText(this, "No Results", Toast.LENGTH_LONG).show();
        }
    }
}
