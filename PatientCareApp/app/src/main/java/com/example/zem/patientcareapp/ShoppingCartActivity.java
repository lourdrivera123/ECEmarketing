package com.example.zem.patientcareapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.zem.patientcareapp.GetterSetter.Basket;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.adapter.LazyAdapter;
import com.example.zem.patientcareapp.Network.GetRequest;
import com.example.zem.patientcareapp.Network.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dexter B. on 5/6/2015.
 */
public class ShoppingCartActivity extends AppCompatActivity implements View.OnClickListener {
    LazyAdapter adapter;
    DbHelper dbHelper;
    Helpers helper;
    ServerRequest serverRequest;

    public ArrayList<HashMap<String, String>> items;
    HashMap<String, String> row;

    public Double TotalAmount = 0.00, oldTotal = 0.00, newTotal = 0.00;
    int gbl_pos = 0, old_qty = 0, newQty = 0, basktQty, billing_id = 0;

    TextView tv_amount, p_name, p_total, p_price, p_description, et_qty;
    public static TextView total_amount;
    ListView lv_items;
    Button btnCheckout;
    Toolbar myToolBar;

    Context context;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        btnCheckout = (Button) findViewById(R.id.btn_checkout_ready);
        lv_items = (ListView) findViewById(R.id.lv_items);
        total_amount = (TextView) findViewById(R.id.upper_cart_total);

        queue = Volley.newRequestQueue(this);
        dbHelper = new DbHelper(this);
        helper = new Helpers();
        serverRequest = new ServerRequest();

        myToolBar = (Toolbar) findViewById(R.id.myToolBar);
        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Shopping Cart");
        myToolBar.setNavigationIcon(R.drawable.ic_back);

        String url_raw = "get_basket_items&patient_id=" + dbHelper.getCurrentLoggedInPatient().getServerID() + "&table=baskets";

        GetRequest.getJSONobj(this, url_raw, "baskets", "basket_id", new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {

                items = dbHelper.getAllBasketItems(true); // returns all basket items for the currently loggedin patient

                for (HashMap<String, String> item : items) {
                    double price = Double.parseDouble(item.get(DbHelper.PRODUCT_PRICE));
                    double quantity = Double.parseDouble(item.get(DbHelper.BASKET_QUANTITY));
                    double total = price * quantity;
                    TotalAmount += total;
                }
                items = dbHelper.getAllBasketItems(false); // returns all basket items for the currently loggedin patient

                adapter = new LazyAdapter(ShoppingCartActivity.this, items, "basket_items");
                lv_items.setAdapter(adapter);
            }
        }, new ErrorListener<VolleyError>() {
            public void getError(VolleyError error) {
                Log.d("shoppingcart", error + "");
                Toast.makeText(context, "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        btnCheckout.setOnClickListener(this);
        lv_items.setOnCreateContextMenuListener(this);
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.cart_menus, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int pos = menuInfo.position;

        row = items.get(pos);
        gbl_pos = pos;
        old_qty = Integer.parseInt(row.get(DbHelper.BASKET_QUANTITY));

        switch (item.getItemId()) {
            case R.id.update_cart:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();

                @SuppressLint("InflateParams") final View layout = inflater.inflate(R.layout._partials_update_cart, null);
                alert.setView(layout);
                alert.setTitle("Update quantity");
                alert.setCancelable(true);
                alert.setNegativeButton("Cancel", null);

                et_qty = (TextView) layout.findViewById(R.id.update_qty);
                tv_amount = (TextView) layout.findViewById(R.id.new_total);
                et_qty.setText(row.get(DbHelper.BASKET_QUANTITY));
                p_name = (TextView) layout.findViewById(R.id.product_name);
                p_total = (TextView) layout.findViewById(R.id.new_total);
                p_price = (TextView) layout.findViewById(R.id.product_price);
                p_description = (TextView) layout.findViewById(R.id.product_description);
                ImageButton btnMinusQty = (ImageButton) layout.findViewById(R.id.minus_qty);
                ImageButton btnAddQty = (ImageButton) layout.findViewById(R.id.add_qty);

                final double price = Double.parseDouble(row.get(DbHelper.PRODUCT_PRICE));
                final String productPacking, productUnit, productName;
                final int productQtyPerPacking, productId, basketId;

                productPacking = row.get(DbHelper.PRODUCT_PACKING);
                productQtyPerPacking = Integer.parseInt(row.get(DbHelper.PRODUCT_QTY_PER_PACKING));
                productUnit = row.get(DbHelper.PRODUCT_UNIT);
                productName = row.get(DbHelper.PRODUCT_NAME);
                productId = Integer.parseInt(row.get(DbHelper.SERVER_PRODUCT_ID));
                basketId = Integer.parseInt(row.get(DbHelper.SERVER_BASKET_ID));
                et_qty.setText(productQtyPerPacking + "");
                basktQty = Integer.parseInt(row.get(DbHelper.BASKET_QUANTITY));

                btnAddQty.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(View v) {
                        int lastQty = Integer.parseInt(et_qty.getText().toString());
                        lastQty += productQtyPerPacking;
                        et_qty.setText(lastQty + "");

                        int totalPacking = (lastQty / productQtyPerPacking);
                        String fiProductPacking = helper.getPluralForm(productPacking, totalPacking);
                        p_description.setText("1 " + productUnit + " x " + lastQty + "(" + totalPacking + " " + fiProductPacking + ")");

                        basktQty = lastQty;
                        p_total.setText("\u20B1 " + (basktQty * price));
                    }
                });

                btnMinusQty.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(View v) {
                        int lastQty = Integer.parseInt(et_qty.getText().toString());
                        lastQty -= productQtyPerPacking;

                        if (lastQty < 1) {
                            lastQty = productQtyPerPacking;
                        }

                        et_qty.setText(lastQty + "");

                        int totalPacking = (lastQty / productQtyPerPacking);
                        String fiProductPacking = helper.getPluralForm(productPacking, totalPacking);

                        p_description.setText("1 " + productUnit + " x " + lastQty + "(" + totalPacking + " " + fiProductPacking + ")");

                        basktQty = lastQty;
                        p_total.setText("\u20B1 " + (basktQty * price));
                    }
                });

                // Setting all values in listview
                p_name.setText(productName);
                int num = basktQty / productQtyPerPacking;
                p_description.setText("1 " + productUnit + " x " + basktQty + "(" + num + " " + helper.getPluralForm(productPacking, num) + ")");
                p_price.setText("\u20B1 " + price);
                p_total.setText("\u20B1 " + (basktQty * price));
                et_qty.setText(basktQty + "");

                alert.setCancelable(true);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        if (helper.isNetworkAvailable(getBaseContext())) {
                            try {
                                final int new_qty = Integer.parseInt(et_qty.getText().toString());
                                newQty = new_qty;

                                final double old_total = price * old_qty;
                                final double new_total = price * new_qty;
                                oldTotal = old_total;
                                newTotal = new_total;

                                final Basket basket = dbHelper.getBasket(Integer.parseInt(row.get("product_id")));
                                basket.setQuantity(new_qty);

                                HashMap<String, String> hashMap = new HashMap();
                                hashMap.put("product_id", String.valueOf(productId));
                                hashMap.put("request", "crud");
                                hashMap.put("quantity", String.valueOf(new_qty));
                                hashMap.put("patient_id", String.valueOf(dbHelper.getCurrentLoggedInPatient().getServerID()));
                                hashMap.put("table", "baskets");
                                hashMap.put("action", "update");
                                hashMap.put("is_direct_update", "true");
                                hashMap.put("id", String.valueOf(basketId));

                                final ProgressDialog pdialog = new ProgressDialog(getBaseContext());
                                pdialog.setCancelable(false);
                                pdialog.setMessage("Loading...");
                                pdialog.show();

                                PostRequest.send(getBaseContext(), hashMap, serverRequest, new RespondListener<JSONObject>() {
                                    @Override
                                    public void getResult(JSONObject response) {
                                        try {
                                            int success = response.getInt("success");

                                            if (success == 1) {
                                                if (dbHelper.updateBasket(basket)) {

                                                    tv_amount.setText(new_total + "");

                                                    row.put(DbHelper.BASKET_QUANTITY, new_qty + "");
                                                    items.set(gbl_pos, row);

                                                    TotalAmount -= old_total;
                                                    TotalAmount += new_total;
                                                    total_amount.setText("\u20B1 " + TotalAmount);

                                                    adapter.notifyDataSetChanged();
                                                } else
                                                    Toast.makeText(context, "Sorry, we can't update your item right now. Please try again later.", Toast.LENGTH_SHORT).show();
                                            } else
                                                Toast.makeText(getBaseContext(), "Server Error Occurred", Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new ErrorListener<VolleyError>() {
                                    public void getError(VolleyError error) {
                                        Log.d("shoppingCart2", error + "");
                                        Toast.makeText(context, "Couldn't update item. Please check your Internet connection", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                pdialog.dismiss();
                            } catch (Exception e) {
                                Log.d("shoppingcart1", e + "");
                                Toast.makeText(getBaseContext(), "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_SHORT).show();
                            }
                        } else
                            Toast.makeText(getBaseContext(), "Please connect to the internet", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.show();
                break;

            case R.id.delete_context:
                AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(getBaseContext());
                confirmationDialog.setTitle("Delete item?");
                confirmationDialog.setNegativeButton("No", null);
                confirmationDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final double amount = Double.parseDouble(row.get("price")) * Double.parseDouble(row.get("quantity"));

                        HashMap<String, String> hashMap = new HashMap();
                        hashMap.put("table", "baskets");
                        hashMap.put("request", "crud");
                        hashMap.put("action", "delete");
                        hashMap.put("id", String.valueOf(row.get("basket_id")));

                        final ProgressDialog pdialog = new ProgressDialog(getBaseContext());
                        pdialog.setCancelable(false);
                        pdialog.setMessage("Loading...");
                        pdialog.show();

                        PostRequest.send(getBaseContext(), hashMap, serverRequest, new RespondListener<JSONObject>() {
                            @Override
                            public void getResult(JSONObject response) {
                                try {
                                    int success = response.getInt("success");

                                    if (success == 1) {
                                        if (dbHelper.deleteFromTable(Integer.parseInt(row.get("basket_id")), "baskets", "basket_id")) {
                                            TotalAmount -= amount;
                                            total_amount.setText("\u20B1 " + TotalAmount);
                                            items.remove(pos);
                                            adapter.notifyDataSetChanged();
                                        } else
                                            Toast.makeText(getBaseContext(), "Sorry, we can't delete your item right now. Please try again later.", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(getBaseContext(), "Server error occurred", Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                pdialog.dismiss();
                            }
                        }, new ErrorListener<VolleyError>() {
                            public void getError(VolleyError error) {
                                Log.d("shoppingcart3", error + "");
                                pdialog.dismiss();
                                Toast.makeText(getBaseContext(), "Couldn't delete item. Please check your Internet connection", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                confirmationDialog.show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_checkout_ready:
                startActivity(new Intent(this, CheckoutActivity.class));
                break;
        }
    }
}