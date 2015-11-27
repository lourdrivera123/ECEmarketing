package com.example.zem.patientcareapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.zem.patientcareapp.GetterSetter.Product;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.ListOfPatientsRequest;
import com.example.zem.patientcareapp.Network.PostRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User PC on 11/20/2015.
 */

public class ProductsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listOfProducts;
    Toolbar products_toolbar;
    LinearLayout results_layout, add_to_cart, if_promo_layout;
    ImageView product_image;
    ToggleButton add_to_favorite;
    TextView noOfResults, original_price, product_name, promo, rs_price;

    GridViewAdapter adapter;
    Helpers helpers;
    RequestQueue queue;
    ServerRequest serverRequest;
    static DbHelper dbHelper;

    public static Map<String, HashMap<String, String>> productQuantity;
    public static ArrayList<HashMap<String, String>> temp_products_items, products_items, basket_items;
    ArrayList<HashMap<Integer, HashMap<String, String>>> searchProducts;
    public HashMap<String, String> map;

    public static int is_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview_products_layout);

        showOverLay(this);

        results_layout = (LinearLayout) findViewById(R.id.results_layout);
        noOfResults = (TextView) findViewById(R.id.noOfResults);

        products_toolbar = (Toolbar) findViewById(R.id.products_toolbar);
        setSupportActionBar(products_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Products");
        products_toolbar.setNavigationIcon(R.drawable.ic_back);

        dbHelper = new DbHelper(this);
        queue = Volley.newRequestQueue(this);
        helpers = new Helpers();
        serverRequest = new ServerRequest();

        searchProducts = new ArrayList();
        basket_items = new ArrayList();
        map = new HashMap();
        productQuantity = new HashMap();
        temp_products_items = new ArrayList();
        products_items = dbHelper.getAllProducts();
        temp_products_items.addAll(products_items);

        getAllBasketItems();

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

        listOfProducts = (ListView) findViewById(R.id.listOfProducts);
        adapter = new GridViewAdapter(this, R.layout.item_gridview_products, products_items);
        listOfProducts.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (is_finish != 0) {
            final ProgressDialog pdialog = new ProgressDialog(ProductsActivity.this);
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
                            Toast.makeText(ProductsActivity.this, "New item has been added to your cart", Toast.LENGTH_SHORT).show();
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

                adapter = new GridViewAdapter(ProductsActivity.this, R.layout.item_gridview_products, products_items);
                listOfProducts.setAdapter(adapter);
                return true;
            }
        });

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Product prod;
        prod = dbHelper.getProductById(Integer.parseInt(view.getTag().toString()));

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

            case R.id.shoppingcart:
                startActivity(new Intent(this, ShoppingCartActivity.class));
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

    void transferHashMap(HashMap<String, String> map) {
        HashMap<String, String> hash = new HashMap();
        hash.put("server_id", map.get("server_id"));
        hash.put("product_id", map.get("product_id"));
        hash.put("quantity", map.get("quantity"));
        hash.put("prescription_id", map.get("prescription_id"));
        basket_items.add(hash);
    }

    //customAdapter
    private class GridViewAdapter extends ArrayAdapter implements View.OnClickListener {
        LayoutInflater inflater;

        public GridViewAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
            super(context, resource, objects);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.item_gridview_products, parent, false);

            original_price = (TextView) view.findViewById(R.id.original_price);
            product_image = (ImageView) view.findViewById(R.id.product_image);
            product_name = (TextView) view.findViewById(R.id.product_name);
            if_promo_layout = (LinearLayout) view.findViewById(R.id.if_promo_layout);
            promo = (TextView) view.findViewById(R.id.promo);
            rs_price = (TextView) view.findViewById(R.id.rs_price);
            add_to_cart = (LinearLayout) view.findViewById(R.id.add_to_cart);
            add_to_favorite = (ToggleButton) view.findViewById(R.id.add_to_favorite);

            add_to_cart.setTag(position);
            add_to_favorite.setTag(position);

            add_to_cart.setOnClickListener(this);

            try {
                if (!products_items.get(position).get("is_favorite").equals(null)) {
                    add_to_favorite.setChecked(true);
                }
            } catch (Exception e) {

            }

            original_price.setPaintFlags(original_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            product_name.setText(products_items.get(position).get("name"));
            rs_price.setText("Php " + products_items.get(position).get("price"));

            return view;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_to_cart:
                    final Helpers helpers = new Helpers();
                    int pos = Integer.parseInt(String.valueOf(v.getTag()));
                    int product_id = Integer.parseInt(products_items.get(pos).get("product_id"));
                    int productQty, check = 0, old_qty = 0;
                    String server_id = null;

                    int is_required = Integer.parseInt(products_items.get(pos).get("prescription_required"));
                    int q = Integer.parseInt(productQuantity.get(product_id + "").get("temp_basket_qty"));
                    int p = Integer.parseInt(productQuantity.get(product_id + "").get("qty_per_packing"));
                    productQty = q != 0 ? q : p;

                    final ProgressDialog pdialog = new ProgressDialog(ProductsActivity.this);
                    pdialog.setCancelable(false);
                    pdialog.setMessage("Please wait...");

                    for (int x = 0; x < basket_items.size(); x++) {
                        if (basket_items.get(x).get("product_id").equals(String.valueOf(product_id))) {
                            check += 1;
                            old_qty = Integer.parseInt(basket_items.get(x).get("quantity"));
                            server_id = basket_items.get(x).get("server_id");
                        }
                    }

                    if (check > 0) { //EXISTING ITEM IN YOUR BASKET (UPDATE ONLY)
                        final HashMap<String, String> hashMap = new HashMap();
                        hashMap.put("patient_id", String.valueOf(SidebarActivity.getUserID()));
                        hashMap.put("table", "baskets");
                        hashMap.put("request", "crud");
                        hashMap.put("action", "update");
                        hashMap.put("id", server_id);
                        int new_qty = old_qty + productQty;
                        hashMap.put("quantity", String.valueOf(new_qty));

                        pdialog.show();
                        PostRequest.send(ProductsActivity.this, hashMap, serverRequest, new RespondListener<JSONObject>() {
                            @Override
                            public void getResult(JSONObject response) {
                                try {
                                    int success = response.getInt("success");

                                    if (success == 1) {
                                        Toast.makeText(ProductsActivity.this, "Your cart has been updated", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(ProductsActivity.this, e + "", Toast.LENGTH_SHORT).show();
                                }
                                pdialog.dismiss();
                            }
                        }, new ErrorListener<VolleyError>() {
                            public void getError(VolleyError error) {
                                pdialog.dismiss();
                                Toast.makeText(ProductsActivity.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else { //ADD NEW SA BASKET
                        final HashMap<String, String> hashMap = new HashMap();
                        hashMap.put("product_id", String.valueOf(product_id));
                        hashMap.put("quantity", String.valueOf(productQty));
                        hashMap.put("patient_id", String.valueOf(SidebarActivity.getUserID()));
                        hashMap.put("table", "baskets");
                        hashMap.put("request", "crud");
                        hashMap.put("action", "insert");

                        if (is_required == 1) { //IF PRESCRIPTION IS REQUIRED
                            GridView gridView;
                            final Dialog builder;
                            HashMap<GridView, Dialog> map;
                            map = helpers.showPrescriptionDialog(ProductsActivity.this);

                            if (map.size() > 0) { //IF NAA NAY UPLOADED NGA PRESCRIPTION
                                Map.Entry<GridView, Dialog> entry = map.entrySet().iterator().next();
                                gridView = entry.getKey();
                                builder = entry.getValue();

                                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        pdialog.show();
                                        int prescriptionId = (int) id;
                                        hashMap.put("prescription_id", prescriptionId + "");
                                        hashMap.put("is_approved", "0");

                                        PostRequest.send(ProductsActivity.this, hashMap, serverRequest, new RespondListener<JSONObject>() {
                                            @Override
                                            public void getResult(JSONObject response) {
                                                try {
                                                    int success = response.getInt("success");

                                                    if (success == 1) {
                                                        hashMap.put("server_id", String.valueOf(response.getInt("last_inserted_id")));
                                                        ProductsActivity.this.transferHashMap(hashMap);
                                                        Toast.makeText(ProductsActivity.this, "New item has been added to your cart", Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
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
                                        builder.dismiss();
                                    }
                                });
                                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        pdialog.dismiss();
                                    }
                                });
                            } else { //IF EMPTY ANG PRESCRIPTIONS NGA TAB
                                AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(ProductsActivity.this);
                                confirmationDialog.setTitle("Attention!");
                                confirmationDialog.setMessage("This product requires you to upload a prescription, do you wish to continue ?");
                                confirmationDialog.setNegativeButton("No", null);
                                confirmationDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AddToCart(hashMap);
                                        startActivity(new Intent(ProductsActivity.this, ShowPrescriptionDialog.class));
                                    }
                                });
                                confirmationDialog.show();
                            }
                        } else { //IF PRESCRIPTION IS NOT REQUIRED
                            pdialog.show();

                            hashMap.put("prescription_id", "0");
                            hashMap.put("is_approved", "1");

                            PostRequest.send(ProductsActivity.this, hashMap, serverRequest, new RespondListener<JSONObject>() {
                                @Override
                                public void getResult(JSONObject response) {
                                    try {
                                        int success = response.getInt("success");

                                        if (success == 1) {
                                            hashMap.put("server_id", String.valueOf(response.getInt("last_inserted_id")));
                                            Toast.makeText(ProductsActivity.this, "New item has been added to your cart", Toast.LENGTH_SHORT).show();
                                            ProductsActivity.this.transferHashMap(hashMap);
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(ProductsActivity.this, e + "", Toast.LENGTH_SHORT).show();
                                    }
                                    pdialog.dismiss();
                                }
                            }, new ErrorListener<VolleyError>() {
                                public void getError(VolleyError error) {
                                    pdialog.dismiss();
                                    Toast.makeText(ProductsActivity.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    break;
            }
        }

        void AddToCart(HashMap<String, String> map) {
            ProductsActivity.this.map = map;
        }
    }
}
