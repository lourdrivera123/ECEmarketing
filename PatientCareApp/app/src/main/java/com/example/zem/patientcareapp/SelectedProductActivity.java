package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by User PC on 6/16/2015.
 */
public class SelectedProductActivity extends Activity implements View.OnClickListener {
    public static final String PRODUCT_ID = "productID";
    public static final String UP_ACTIVITY = "parent_activity";

    String get_parent = "", productPacking = "";
    int get_productID = 0;
    int temp_qty = 1, qtyPerPacking = 1;

    TextView prod_name, prod_generic, prod_unit, prod_price, qty_cart, prod_description;
    ImageButton minus_qty, add_qty;
    Button add_cart_btn;
    ImageView prod_unit_type;

    Handler handler;
    DbHelper dbhelper;
    Product prod;
    Helpers helpers;
    ServerRequest serverRequest;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_product_layout);

        ActionBar actionbar = getActionBar();
        MainActivity.setCustomActionBar(actionbar);
        actionbar.setDisplayHomeAsUpEnabled(true);

        handler = new Handler();
        dbhelper = new DbHelper(this);
        prod = new Product();
        helpers = new Helpers();
        serverRequest = new ServerRequest();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");

        Intent intent = getIntent();
        get_parent = intent.getStringExtra(UP_ACTIVITY);
        get_productID = intent.getIntExtra(PRODUCT_ID, 0);
        prod = dbhelper.getProductById(get_productID);

        prod_name = (TextView) findViewById(R.id.prod_name);
        prod_generic = (TextView) findViewById(R.id.prod_generic);
        prod_unit = (TextView) findViewById(R.id.prod_unit);
        prod_price = (TextView) findViewById(R.id.prod_price);
        qty_cart = (TextView) findViewById(R.id.qty_cart);
        minus_qty = (ImageButton) findViewById(R.id.minus_qty);
        add_qty = (ImageButton) findViewById(R.id.add_qty);
        add_cart_btn = (Button) findViewById(R.id.add_cart_btn);
        prod_unit_type = (ImageView) findViewById(R.id.prod_unit_type);
        prod_description = (TextView) findViewById(R.id.prod_description);

        minus_qty.setOnClickListener(this);
        add_qty.setOnClickListener(this);
        add_cart_btn.setOnClickListener(this);

        qty_cart.setText("" + temp_qty);
        prod_name.setText(prod.getName());
        prod_generic.setText(prod.getGenericName());
        prod_unit.setText(prod.getUnit());
        prod_description.setText(prod.getDescription());

        prod_price.setText("\u20B1" + prod.getPrice());
        qtyPerPacking = prod.getQtyPerPacking();
        productPacking = prod.getPacking();
        temp_qty = qtyPerPacking;
        prod_unit.setText("1 " + prod.getUnit() + " x " + qtyPerPacking + "(1 " + productPacking + ")");

    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (get_parent.equals("HomeTile")) {
                    startActivity(new Intent(this, HomeTileActivity.class));
                } else if (get_parent.equals("ProductsFragment")) {
                    Intent intent = new Intent(this, MasterTabActivity.class);
                    intent.putExtra("selected", 5);
                    startActivity(intent);
                }
                this.finish();
                return true;

            case R.id.go_to_cart:
                Intent intent = new Intent(this, MasterTabActivity.class);
                intent.putExtra("selected", 6);
                startActivity(intent);
                break;
        }
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

                } else {
                    qty_cart.setText("" + temp_qty);
                }

                prod_unit.setText("1 " + prod.getUnit() + " x " + temp_qty + "("+(temp_qty/qtyPerPacking)+" " + productPacking + ")");

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

                prod_unit.setText("1 " + prod.getUnit() + " x " + temp_qty + "("+(temp_qty/qtyPerPacking)+" " + productPacking + ")");


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        add_qty.setImageResource(R.drawable.ic_plus);
                    }
                }, 100);
                break;

            case R.id.add_cart_btn:
                if (helpers.isNetworkAvailable(this)) {
                    try {
                        double new_qty;
                        new_qty = Double.parseDouble(String.valueOf(temp_qty));

                    /* let's check if the product already exists in our basket */
                        final Basket basket = dbhelper.getBasket(get_productID);

                        if (basket.getBasketId() > 0) {
                            HashMap<String, String> hashMap = new HashMap();
                            hashMap.put("product_id", String.valueOf(get_productID));

                            hashMap.put("patient_id", String.valueOf(dbhelper.getCurrentLoggedInPatient().getServerID()));
                            hashMap.put("table", "baskets");
                            hashMap.put("request", "crud");

                            double old_qty = basket.getQuantity();
                            basket.setQuantity(new_qty + old_qty);
                            hashMap.put("quantity", String.valueOf(basket.getQuantity()));
                            hashMap.put("action", "update");
                            hashMap.put("id", String.valueOf(basket.getBasketId()));

                            serverRequest.init(this, hashMap, "insert_basket");
                            serverRequest.setProgressDialog(pDialog);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    boolean responseFromServer = serverRequest.getResponse();
                                    if (responseFromServer) {
                                        if (dbhelper.updateBasket(basket)) {
                                            Toast.makeText(getBaseContext(), "Your cart has been updated.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getBaseContext(), "Sorry, we can't update your cart this time.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getBaseContext(), "Sorry, we can't update your cart this time.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, 2500);
                        } else {
                        /* since, we can't find the product in baskets table, let's insert a new one */
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("product_id", String.valueOf(get_productID));
                            hashMap.put("quantity", String.valueOf(new_qty));
                            hashMap.put("patient_id", String.valueOf(dbhelper.getCurrentLoggedInPatient().getServerID()));
                            hashMap.put("table", "baskets");
                            hashMap.put("request", "crud");
                            hashMap.put("action", "insert");

                            serverRequest.setProgressDialog(pDialog);

                            serverRequest.setSuccessMessage("New item has been added to your cart!");
                            serverRequest.setErrorMessage("Sorry, we can't add to your cart this time.");
                            serverRequest.init(this, hashMap, "insert_basket");
                        }
                    } catch (Exception e) {
                    }
                } else {
                    Toast.makeText(this, "Sorry, please connect to the internet.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
