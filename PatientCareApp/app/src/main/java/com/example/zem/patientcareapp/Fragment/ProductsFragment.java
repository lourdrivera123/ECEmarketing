package com.example.zem.patientcareapp.Fragment;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.zem.patientcareapp.DbHelper;
import com.example.zem.patientcareapp.GetterSetter.Product;
import com.example.zem.patientcareapp.GetterSetter.ProductSubCategory;
import com.example.zem.patientcareapp.Helpers;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.adapter.LazyAdapter;
import com.example.zem.patientcareapp.Network.GetRequest;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.SelectedProductActivity;
import com.example.zem.patientcareapp.SidebarActivity;

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
    static DbHelper dbHelper;
    Helpers helpers;
    List<String> category_list;
    SwipeRefreshLayout refresh_products_list;

    RequestQueue queue;
    View root_view;
    public static Map<String, HashMap<String, String>> productQuantity;
    public static ArrayList<HashMap<String, String>> products_items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_products_fragment, container, false);
        root_view = rootView;

        lv_categories = (Spinner) rootView.findViewById(R.id.categories);

        productQuantity = new HashMap();

        dbHelper = new DbHelper(getActivity());
        queue = Volley.newRequestQueue(getActivity());
        helpers = new Helpers();

        products_items = dbHelper.getAllProducts();

        for (HashMap<String, String> map : products_items) {
            HashMap<String, String> tempMap = new HashMap();
            tempMap.put("id", map.get("id"));
            tempMap.put("product_id", map.get("product_id"));
            tempMap.put("qty_per_packing", map.get("qty_per_packing"));
            tempMap.put("packing", map.get("packing"));
            tempMap.put("temp_basket_qty", "0");
            tempMap.put("prescription_required", map.get("prescription_required"));
            productQuantity.put(map.get("product_id"), tempMap);
        }

        populateProductsListView(rootView, products_items);
        category_list = dbHelper.getAllProductCategoriesArray();
        populateListView(rootView, category_list);

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

                    Toast.makeText(getActivity(), subCategoryName + " : " + list.size() + " item/s found", Toast.LENGTH_SHORT).show();

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
        GetRequest.getJSONobj(getActivity(), "get_products", "products", "product_id", new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
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
                populateProductsListView(root_view, products_items);
                refresh_products_list.setRefreshing(false);
            }
        }, new ErrorListener<VolleyError>() {
            public void getError(VolleyError error) {
                Log.d("<ProductsFragment>", error.toString());
                Toast.makeText(getActivity(), "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_LONG).show();
            }
        });
    }

//    public static void showOverLay(Context context) {
//        dbHelper = new DbHelper(context);
//
//        if (dbHelper.checkOverlay("Products", "check")) {
//
//        } else {
//            final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
//            dialog.setContentView(R.layout.products_overlay);
//
//            LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.productsLayout);
//            layout.setAlpha((float) 0.8);
//
//            layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (dbHelper.checkOverlay("Products", "insert"))
//                        dialog.dismiss();
//                }
//            });
//            dialog.show();
//        }
//    }
}
