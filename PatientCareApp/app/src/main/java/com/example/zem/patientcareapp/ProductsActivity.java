package com.example.zem.patientcareapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.zem.patientcareapp.GetterSetter.Product;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.GetRequest;
import com.example.zem.patientcareapp.Network.ListOfPatientsRequest;
import com.example.zem.patientcareapp.Network.PostRequest;
import com.example.zem.patientcareapp.adapter.ProductsAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User PC on 11/20/2015.
 */

public class ProductsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    ListView listOfProducts;
    Toolbar myToolBar;
    LinearLayout results_layout;
    TextView noOfResults, number_of_notif;
    ImageButton go_to_cart;

    ProductsAdapter adapter;
    Helpers helpers;
    RequestQueue queue;
    ServerRequest serverRequest;
    static DbHelper dbHelper;

    public static Map<String, HashMap<String, String>> productQuantity;
    public static ArrayList<HashMap<String, String>> temp_products_items, products_items, basket_items;
    ArrayList<HashMap<Integer, HashMap<String, String>>> searchProducts;
    public static HashMap<String, String> map;

    public static int is_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview_products_layout);

        showOverLay(this);

        results_layout = (LinearLayout) findViewById(R.id.results_layout);
        noOfResults = (TextView) findViewById(R.id.noOfResults);
        listOfProducts = (ListView) findViewById(R.id.listOfProducts);

        myToolBar = (Toolbar) findViewById(R.id.myToolBar);
        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Products");
        myToolBar.setNavigationIcon(R.drawable.ic_back);

        dbHelper = new DbHelper(this);
        queue = Volley.newRequestQueue(this);
        helpers = new Helpers();
        serverRequest = new ServerRequest();

        searchProducts = new ArrayList();
        basket_items = new ArrayList();
        map = new HashMap();
        productQuantity = new HashMap();
        temp_products_items = new ArrayList();

        final ProgressDialog progress1 = new ProgressDialog(this);
        progress1.setMessage("Please wait...");
        progress1.show();

        GetRequest.getJSONobj(getBaseContext(), "get_products", "products", "product_id", new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
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
                    hash.put(Integer.parseInt(map.get("product_id")), temp);
                    searchProducts.add(hash);
                }
                adapter = new ProductsAdapter(ProductsActivity.this, R.layout.item_gridview_products, products_items);
                listOfProducts.setAdapter(adapter);
                progress1.dismiss();
            }
        }, new ErrorListener<VolleyError>() {
            public void getError(VolleyError error) {
                Log.d("Error", error + "");
                progress1.dismiss();
                Toast.makeText(getBaseContext(), "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        getAllBasketItems();

        listOfProducts.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SelectedProductActivity.is_resumed = 0;

        if (is_finish != 0) {
            final ProgressDialog pdialog = new ProgressDialog(this);
            pdialog.setCancelable(false);
            pdialog.setMessage("Please wait...");
            pdialog.show();

            int prescriptionId = is_finish;
            final HashMap<String, String> hashMap = map;
            hashMap.put("prescription_id", prescriptionId + "");
            hashMap.put("is_approved", "0");

            PostRequest.send(ProductsActivity.this, hashMap, serverRequest, new RespondListener<JSONObject>() {
                @Override
                public void getResult(JSONObject response) {
                    try {
                        int success = response.getInt("success");

                        if (success == 1) {
                            hashMap.put("server_id", String.valueOf(response.getInt("last_inserted_id")));
                            transferHashMap(hashMap);
                            if (dbHelper.saveBasket(hashMap))
                                Toast.makeText(ProductsActivity.this, "New item has been added to your cart", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(ProductsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(ProductsActivity.this, e + "", Toast.LENGTH_SHORT).show();
                    }
                    pdialog.dismiss();
                }
            }, new ErrorListener<VolleyError>() {
                public void getError(VolleyError error) {
                    pdialog.dismiss();
                    Toast.makeText(ProductsActivity.this, "Couldn't add item. Please check your Internet connection", Toast.LENGTH_LONG).show();
                }
            });
        }
        is_finish = 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem item = menu.findItem(R.id.shoppingcart);
        MenuItemCompat.setActionView(item, R.layout.count_badge_layout);
        RelativeLayout badgeLayout = (RelativeLayout) MenuItemCompat.getActionView(item);

        number_of_notif = (TextView) badgeLayout.findViewById(R.id.number_of_notif);
        go_to_cart = (ImageButton) badgeLayout.findViewById(R.id.go_to_cart);
        go_to_cart.setOnClickListener(this);

        if (number_of_notif.getVisibility() == View.VISIBLE)
            number_of_notif.setText("12");

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

                adapter = new ProductsAdapter(ProductsActivity.this, R.layout.item_gridview_products, products_items);
                listOfProducts.setAdapter(adapter);
                return true;
            }
        });

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int product_id = Integer.parseInt(products_items.get(position).get("product_id"));

        Product prod;
        prod = dbHelper.getProductById(product_id);

        Intent intent = new Intent(this, SelectedProductActivity.class);
        intent.putExtra(SelectedProductActivity.PRODUCT_ID, prod.getProductId());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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

    public void toggleOnClick(View v) {
        int pos = Integer.parseInt(String.valueOf(v.getTag()));
        int product_id = Integer.parseInt(products_items.get(pos).get("product_id"));
        String prod_name = products_items.get(pos).get("name");

        if (((ToggleButton) v).isChecked()) {
            if (dbHelper.insertFaveProduct(SidebarActivity.getUserID(), product_id))
                Toast.makeText(getBaseContext(), prod_name + " is added to your favorites", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getBaseContext(), "Error occurred", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.removeFavorite(SidebarActivity.getUserID(), product_id);
        }
    }

    void getAllBasketItems() {
        String url_raw = "get_basket_items&patient_id=" + SidebarActivity.getUserID() + "&table=baskets";

        ListOfPatientsRequest.getJSONobj(ProductsActivity.this, url_raw, new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                try {
                    int success = response.getInt("success");

                    if (success == 1) {
                        JSONArray json_mysql = response.getJSONArray("baskets");

                        for (int x = 0; x < json_mysql.length(); x++) {
                            JSONObject obj = json_mysql.getJSONObject(x);

                            HashMap<String, String> map = new HashMap();
                            map.put("server_id", String.valueOf(obj.getInt("id")));
                            map.put("product_id", String.valueOf(obj.getInt("product_id")));
                            map.put("quantity", String.valueOf(obj.getInt("quantity")));
                            map.put("prescription_id", String.valueOf(obj.getInt("prescription_id")));
                            basket_items.add(map);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(ProductsActivity.this, e + "", Toast.LENGTH_SHORT).show();
                }
            }
        }, new ErrorListener<VolleyError>() {
            @Override
            public void getError(VolleyError e) {
                Toast.makeText(ProductsActivity.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void transferHashMap(HashMap<String, String> map) {
        HashMap<String, String> hash = new HashMap();

        if (map.get("action").equals("update")) {
            for (int x = 0; x < basket_items.size(); x++) {
                if (map.get("id").equals(basket_items.get(x).get("server_id"))) {
                    hash.put("server_id", map.get("id"));
                    hash.put("product_id", basket_items.get(x).get("product_id"));
                    hash.put("quantity", map.get("quantity"));
                    hash.put("prescription_id", basket_items.get(x).get("prescription_id"));
                }
            }
        } else {
            hash.put("server_id", map.get("server_id"));
            hash.put("product_id", map.get("product_id"));
            hash.put("quantity", map.get("quantity"));
            hash.put("prescription_id", map.get("prescription_id"));
        }

        basket_items.add(hash);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_to_cart:
                startActivity(new Intent(this, ShoppingCartActivity.class));
                break;
        }
    }
}
