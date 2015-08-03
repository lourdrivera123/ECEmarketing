package com.example.zem.patientcareapp.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.zem.patientcareapp.DbHelper;
import com.example.zem.patientcareapp.GetterSetter.Product;
import com.example.zem.patientcareapp.GetterSetter.ProductSubCategory;
import com.example.zem.patientcareapp.Helpers;
import com.example.zem.patientcareapp.LazyAdapter;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.SelectedProductActivity;
import com.example.zem.patientcareapp.ServerRequest;
import com.example.zem.patientcareapp.Sync;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    ListView list_of_products, lv_subcategories;
    Spinner lv_categories;
    LazyAdapter adapter;
    ArrayAdapter category_list_adapter;
    DbHelper dbHelper;
    Helpers helpers;
    Sync sync;
    ServerRequest serverRequest;
    List<String> category_list;
    SwipeRefreshLayout refresh_products_list;

    RequestQueue queue;
    ProgressDialog pDialog;
    View root_view;
    public static Map<String, HashMap<String, String>> productQuantity;
    public static ArrayList<HashMap<String, String>> products_items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.products_layout, container, false);
        root_view = rootView;

        lv_categories = (Spinner) rootView.findViewById(R.id.categories);
        refresh_products_list = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_products_list);
        refresh_products_list.setOnRefreshListener(this);

        productQuantity = new HashMap();

        dbHelper = new DbHelper(getActivity());
        serverRequest = new ServerRequest();
        queue = Volley.newRequestQueue(getActivity());
        helpers = new Helpers();

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");

//        if (helpers.isNetworkAvailable(getActivity())) {
            products_items = dbHelper.getAllProducts();

            for (HashMap<String, String> map : products_items) {

                HashMap<String, String> tempMap = new HashMap();
                tempMap.put("id", map.get("id"));
                tempMap.put("product_id", map.get("product_id"));
                tempMap.put("qty_per_packing", map.get("qty_per_packing"));
                tempMap.put("packing", map.get("packing"));
                tempMap.put("temp_basket_qty", "0");
                productQuantity.put(map.get("product_id"), tempMap);
            }

            populateProductsListView(rootView, products_items);
            category_list = dbHelper.getAllProductCategoriesArray();
            populateListView(rootView, category_list);
            pDialog.hide();
//        }
        return rootView;
    }

    public void populateProductsListView(View rootView, ArrayList<HashMap<String, String>> products_items) {
        list_of_products = (ListView) rootView.findViewById(R.id.product_lists);

        // Getting adapter by passing xml data ArrayList
        adapter = new LazyAdapter(getActivity(), products_items, "product_lists");
        list_of_products.setAdapter(adapter);
        list_of_products.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Product prod;
        prod = dbHelper.getProductById(Integer.parseInt(view.getTag().toString()));

        Intent intent = new Intent(getActivity(), SelectedProductActivity.class);
        intent.putExtra(SelectedProductActivity.PRODUCT_ID, prod.getProductId());
        intent.putExtra(SelectedProductActivity.UP_ACTIVITY, "ProductsFragment");
        startActivity(intent);
        getActivity().finish();
    }

    public void populateListView(View rootView, List<String> categories) {
        lv_categories = (Spinner) rootView.findViewById(R.id.categories);
        lv_subcategories = (ListView) rootView.findViewById(R.id.subcategories);
        categories.add(0, "Select Category");

        category_list_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, categories);
        lv_categories.setAdapter(category_list_adapter);
        lv_subcategories.setVisibility(View.GONE);
        lv_categories.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            String item = ((TextView) view).getText().toString();

            final int categoryId = dbHelper.getCategoryIdByName(item);
            String[] arr = dbHelper.getAllProductSubCategoriesArray(categoryId);

            final Dialog dialog = new Dialog(getActivity());
            dialog.setTitle("subcategories");
            dialog.setContentView(R.layout.categories_layout);

            dialog.show();

            TextView browseBy = (TextView) dialog.findViewById(R.id.browse_by);
            browseBy.setText("");
            lv_subcategories = (ListView) dialog.findViewById(R.id.subcategories);
            lv_categories = (Spinner) dialog.findViewById(R.id.categories);

            category_list_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arr);
            lv_subcategories.setAdapter(category_list_adapter);
            lv_categories.setVisibility(View.GONE);
            lv_subcategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String subCategoryName = ((TextView) view).getText().toString();
                    ProductSubCategory subCategory = dbHelper.getSubCategoryByName(subCategoryName, categoryId);

                    ArrayList<HashMap<String, String>> list = dbHelper.getProductsBySubCategory(subCategory.getId());

                    Toast.makeText(getActivity(), subCategoryName + " : " + subCategory.getName() + " : " + list.size()
                            , Toast.LENGTH_SHORT).show();
                    // Getting adapter by passing xml data ArrayList
                    if (list.size() > 0) {
                        products_items.clear();
                        products_items.addAll(list);
                        adapter.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                }
            });
        } else {
            ArrayList<HashMap<String, String>> prods = dbHelper.getProductsBySubCategory(0);
            products_items.clear();
            products_items.addAll(prods);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onRefresh() {
//        if (helpers.isNetworkAvailable(getActivity())) {
            JsonObjectRequest product_request = new JsonObjectRequest(Request.Method.GET, helpers.get_url("get_products"), null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d("response from update", response.toString());
                    sync = new Sync();
                    sync.init(getActivity(), "get_products", "products", "product_id", response);
                    try {
                        System.out.println("timestamp from server: " + response.getString("server_timestamp"));
                        dbHelper.updateLastUpdatedTable("products", response.getString("server_timestamp"));
                        refresh_products_list.setRefreshing(false);
                    } catch (Exception e) {
                        System.out.println("error fetching server timestamp: " + e);
                    }

                    products_items = dbHelper.getAllProducts();
                    populateProductsListView(root_view, products_items);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_LONG).show();

//                    Toast.makeText(getActivity(), "Error on request", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(product_request);
//        } else {
//            Toast.makeText(getActivity(), "Couldn't refresh feed. Please check your Internet connection", Toast.LENGTH_LONG).show();
//            refresh_products_list.setRefreshing(false);
//        }
    }
}