package com.example.zem.patientcareapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.CheckoutModule.DeliverPickupOption;
import com.example.zem.patientcareapp.CheckoutModule.PromosDiscounts;
import com.example.zem.patientcareapp.Controllers.BasketController;
import com.example.zem.patientcareapp.Controllers.OrderPreferenceController;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Model.OrderModel;
import com.example.zem.patientcareapp.Network.ListOfPatientsRequest;
import com.example.zem.patientcareapp.Network.PostRequest;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.SidebarModule.SidebarActivity;
import com.example.zem.patientcareapp.adapter.ShoppingCartAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User PC on 12/19/2015.
 */
public class ShoppingCartActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar myToolBar;
    ListView lisOfItems;
    LinearLayout root;
    TextView proceed_to_checkout;
    public static TextView total_amount;
    OrderPreferenceController opc;

    ShoppingCartAdapter adapter;
    BasketController bc;
    OrderModel intent_ordermodel;

    public ArrayList<HashMap<String, String>> items = new ArrayList();
    public static ArrayList<HashMap<String, String>> no_code_promos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        myToolBar = (Toolbar) findViewById(R.id.myToolBar);
        lisOfItems = (ListView) findViewById(R.id.lisOfItems);
        root = (LinearLayout) findViewById(R.id.root);
        total_amount = (TextView) findViewById(R.id.total_amount);
        proceed_to_checkout = (TextView) findViewById(R.id.proceed_to_checkout);

        bc = new BasketController();
        opc = new OrderPreferenceController(this);
        no_code_promos = new ArrayList();

        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Shopping Cart");
        myToolBar.setNavigationIcon(R.drawable.ic_back);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        proceed_to_checkout.setOnClickListener(this);

        if (getIntent().getSerializableExtra("order_model") != null) {
            intent_ordermodel = (OrderModel) getIntent().getSerializableExtra("order_model");
        }

        ListOfPatientsRequest.getJSONobj(ShoppingCartActivity.this, "get_nocode_promos", "promos", new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                try {
                    int success = response.getInt("success");

                    if (success == 1) {
                        JSONArray json_mysql = response.getJSONArray("promos");

                        for (int x = 0; x < json_mysql.length(); x++) {
                            JSONObject obj = json_mysql.getJSONObject(x);
                            HashMap<String, String> map = new HashMap();

                            map.put("promo_id", obj.getString("pr_promo_id"));
                            map.put("product_applicability", obj.getString("product_applicability"));
                            map.put("minimum_purchase", obj.getString("minimum_purchase_amount"));
                            map.put("pr_qty_required", obj.getString("pr_qty_required"));
                            map.put("is_every", obj.getString("is_every"));
                            map.put("pr_free_delivery", obj.getString("pr_free_delivery"));
                            map.put("pr_percentage", obj.getString("pr_percentage"));
                            map.put("pr_peso", obj.getString("pr_peso"));
                            map.put("product_id", obj.getString("product_id"));
                            map.put("quantity_required", obj.getString("quantity_required"));
                            map.put("has_free_gifts", obj.getString("has_free_gifts"));
                            map.put("is_free_delivery", obj.getString("is_free_delivery"));
                            map.put("percentage_discount", obj.getString("percentage_discount"));
                            map.put("peso_discount", obj.getString("peso_discount"));
                            no_code_promos.add(map);
                        }
                    }
                } catch (Exception e) {
                    Snackbar.make(root, e + "", Snackbar.LENGTH_SHORT).show();
                }

                String url_raw = "get_basket_details&patient_id=" + SidebarActivity.getUserID();
                ListOfPatientsRequest.getJSONobj(ShoppingCartActivity.this, url_raw, "baskets", new RespondListener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject response) {
                        try {
                            int success = response.getInt("success");

                            if (success == 1) {
                                JSONArray json_mysql = response.getJSONArray("baskets");
                                items = bc.convertFromJson(ShoppingCartActivity.this, json_mysql);

                                adapter = new ShoppingCartAdapter(ShoppingCartActivity.this, R.layout.item_shopping_cart, items);
                                lisOfItems.setAdapter(adapter);
                            }
                        } catch (Exception e) {
                            Toast.makeText(ShoppingCartActivity.this, e + "", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }
                }, new ErrorListener<VolleyError>() {
                    @Override
                    public void getError(VolleyError e) {
                        dialog.dismiss();
                        Snackbar.make(root, "Network Error", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        }, new ErrorListener<VolleyError>() {
            @Override
            public void getError(VolleyError e) {
                dialog.dismiss();
                Snackbar.make(root, "Network error", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.proceed_to_checkout:
                updateBasket(items, 1);
                break;
        }
    }

    protected void onPause() {
        updateBasket(items, 0);
        super.onPause();
    }

    void updateBasket(ArrayList<HashMap<String, String>> objects, final int check) {
        try {
            JSONArray master_arr = new JSONArray();
            HashMap<String, String> hash = new HashMap();
            JSONObject obj_for_server;

            for (int x = 0; x < objects.size(); x++) {
                hash.put("quantity", String.valueOf(objects.get(x).get("quantity")));
                hash.put("id", String.valueOf(objects.get(x).get("basket_id")));
                obj_for_server = new JSONObject(hash);
                master_arr.put(obj_for_server);
            }

            final JSONObject json_to_be_passed = new JSONObject();
            json_to_be_passed.put("jsobj", master_arr);

            HashMap<String, String> params = new HashMap();
            params.put("table", "baskets");
            params.put("request", "crud");
            params.put("action", "multiple_update_for_basket");
            params.put("jsobj", json_to_be_passed.toString());

            PostRequest.send(ShoppingCartActivity.this, params, new RespondListener<JSONObject>() {
                @Override
                public void getResult(JSONObject response) {
                    try {
                        int success = response.getInt("success");
                        if (success == 1) {

                            if (check == 1) {
                                OrderModel order_model = opc.getOrderPreference();
                                if (order_model.isValid()) {
                                    Intent summary_intent = new Intent(ShoppingCartActivity.this, PromosDiscounts.class);
                                    summary_intent.putExtra("order_model", order_model);
                                    startActivity(summary_intent);
                                } else{
                                    Intent deliver_p_intent = new Intent(ShoppingCartActivity.this, DeliverPickupOption.class);
                                    deliver_p_intent.putExtra("order_model", intent_ordermodel);
                                    startActivity(new Intent(ShoppingCartActivity.this, DeliverPickupOption.class));
                                }

                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(ShoppingCartActivity.this, e + "", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new ErrorListener<VolleyError>() {
                public void getError(VolleyError error) {
                    Snackbar.make(root, "Network Error", Snackbar.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            Toast.makeText(ShoppingCartActivity.this, e + "", Toast.LENGTH_SHORT).show();
        }
    }
}