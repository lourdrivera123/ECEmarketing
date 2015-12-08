package com.example.zem.patientcareapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.ConfigurationModule.Helpers;
import com.example.zem.patientcareapp.Controllers.BasketController;
import com.example.zem.patientcareapp.Controllers.DbHelper;
import com.example.zem.patientcareapp.Controllers.PatientController;
import com.example.zem.patientcareapp.Controllers.ProductController;
import com.example.zem.patientcareapp.Model.Basket;
import com.example.zem.patientcareapp.Model.Product;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.PostRequest;
import com.example.zem.patientcareapp.Network.ServerRequest;
import com.example.zem.patientcareapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by User PC on 7/25/2015.
 */

public class SelectedProductActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String PRODUCT_ID = "productID";

    String productPacking = "";
    int get_productID = 0;
    int temp_qty = 1, qtyPerPacking = 1;

    Button add_cart_btn;
    ImageButton minus_qty, add_qty;
    Toolbar selected_product_toolbar;
    TextView prod_name, prod_generic, prod_unit, prod_price, qty_cart, prod_description;

    Handler handler;
    DbHelper dbhelper;
    ProductController pc;
    BasketController bc;
    PatientController ptc;
    Product prod;
    Helpers helpers;
    ServerRequest serverRequest;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_product_layout);

        handler = new Handler();
        dbhelper = new DbHelper(this);
        pc = new ProductController(this);
        bc = new BasketController(this);
        ptc = new PatientController(this);
        prod = new Product();
        helpers = new Helpers();
        serverRequest = new ServerRequest();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");

        Intent intent = getIntent();
        get_productID = intent.getIntExtra(PRODUCT_ID, 0);
        prod = pc.getProductById(get_productID);

        prod_name = (TextView) findViewById(R.id.prod_name);
        prod_generic = (TextView) findViewById(R.id.prod_generic);
        prod_unit = (TextView) findViewById(R.id.prod_unit);
        prod_price = (TextView) findViewById(R.id.prod_price);
        qty_cart = (TextView) findViewById(R.id.qty_cart);
        minus_qty = (ImageButton) findViewById(R.id.minus_qty);
        add_qty = (ImageButton) findViewById(R.id.add_qty);
        add_cart_btn = (Button) findViewById(R.id.add_cart_btn);
        prod_description = (TextView) findViewById(R.id.prod_description);

        minus_qty.setOnClickListener(this);
        add_qty.setOnClickListener(this);
        add_cart_btn.setOnClickListener(this);

        prod_name.setText(prod.getName());
        prod_generic.setText(prod.getGenericName());
        prod_unit.setText(prod.getUnit());
        prod_description.setText(prod.getDescription());

        prod_price.setText("\u20B1" + prod.getPrice());
        qtyPerPacking = prod.getQtyPerPacking();
        productPacking = prod.getPacking();
        temp_qty = qtyPerPacking;

        qty_cart.setText("" + temp_qty);
        prod_unit.setText("1 " + prod.getUnit() + " x " + qtyPerPacking + "(1 " + productPacking + ")");

        selected_product_toolbar = (Toolbar) findViewById(R.id.selected_product_toolbar);
        setSupportActionBar(selected_product_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(prod.getName());
        selected_product_toolbar.setNavigationIcon(R.drawable.ic_back);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.go_to_cart_menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.minus_qty:
                temp_qty -= qtyPerPacking;
                minus_qty.setImageResource(R.drawable.ic_minus_dead);

                if (temp_qty < 1) {
                    qty_cart.setText("" + qtyPerPacking);
                    temp_qty = qtyPerPacking;
                } else
                    qty_cart.setText("" + temp_qty);

                prod_unit.setText("1 " + prod.getUnit() + " x " + temp_qty + "(" + (temp_qty / qtyPerPacking) + " " + productPacking + ")");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        minus_qty.setImageResource(R.drawable.ic_minus);
                    }
                }, 100);
                break;

            case R.id.add_qty:
                temp_qty += qtyPerPacking;
                add_qty.setImageResource(R.drawable.ic_plus_dead);
                qty_cart.setText("" + temp_qty);
                prod_unit.setText("1 " + prod.getUnit() + " x " + temp_qty + "(" + (temp_qty / qtyPerPacking) + " " + productPacking + ")");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        add_qty.setImageResource(R.drawable.ic_plus);
                    }
                }, 100);
                break;

            case R.id.add_cart_btn:
                try {
                    int new_qty;
                    new_qty = Integer.parseInt(String.valueOf(temp_qty));

                    final Basket basket = bc.getBasket(get_productID);

                    if (basket.getBasketId() > 0) {
                        HashMap<String, String> hashMap = new HashMap();
                        hashMap.put("product_id", String.valueOf(get_productID));

                        hashMap.put("patient_id", String.valueOf(ptc.getCurrentLoggedInPatient().getServerID()));
                        hashMap.put("table", "baskets");
                        hashMap.put("request", "crud");

                        int old_qty = basket.getQuantity();
                        basket.setQuantity(new_qty + old_qty);
                        hashMap.put("quantity", String.valueOf(basket.getQuantity()));
                        hashMap.put("action", "update");
                        hashMap.put("id", String.valueOf(basket.getBasketId()));

                        final ProgressDialog pdialog = new ProgressDialog(this);
                        pdialog.setCancelable(false);
                        pdialog.setMessage("Loading...");
                        pdialog.show();

                        PostRequest.send(getBaseContext(), hashMap, serverRequest, new RespondListener<JSONObject>() {
                            @Override
                            public void getResult(JSONObject response) {
                                int success = 0;

                                try {
                                    success = response.getInt("success");

                                    if (success == 1) {
                                        if (bc.updateBasket(basket)) {
                                            Toast.makeText(getBaseContext(), "Your cart has been updated.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getBaseContext(), "Sorry, we can't update your cart this time.", Toast.LENGTH_SHORT).show();
                                            Log.d("SelectedProductAct", "error on dbhelper");
                                        }
                                    } else
                                        Toast.makeText(getBaseContext(), "Server error occurred.", Toast.LENGTH_SHORT).show();
                                    pdialog.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new ErrorListener<VolleyError>() {
                            public void getError(VolleyError error) {
                                Toast.makeText(getBaseContext(), "Couldn't delete item. Please check your Internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        final HashMap<String, String> hashMap = new HashMap();
                        hashMap.put("product_id", String.valueOf(get_productID));
                        hashMap.put("quantity", String.valueOf(new_qty));
                        hashMap.put("patient_id", String.valueOf(ptc.getCurrentLoggedInPatient().getServerID()));
                        hashMap.put("table", "baskets");
                        hashMap.put("request", "crud");
                        hashMap.put("action", "insert");

                        final ProgressDialog pdialog = new ProgressDialog(this);
                        pdialog.setCancelable(false);
                        pdialog.setMessage("Loading...");
                        pdialog.show();

                        serverRequest.setSuccessMessage("New item has been added to your cart!");
                        serverRequest.setErrorMessage("Sorry, we can't add to your cart this time.");

                        PostRequest.send(getBaseContext(), hashMap, serverRequest, new RespondListener<JSONObject>() {
                            @Override
                            public void getResult(JSONObject response) {
                            }
                        }, new ErrorListener<VolleyError>() {
                            public void getError(VolleyError error) {
                                Toast.makeText(getBaseContext(), "Couldn't delete item. Please check your Internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });
                        pdialog.dismiss();
                    }
                } catch (Exception e) {
                    Log.d("SelectedProductAct: ", e + "");
                }
                break;
        }
    }
}
