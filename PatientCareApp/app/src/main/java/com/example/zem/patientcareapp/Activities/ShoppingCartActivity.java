package com.example.zem.patientcareapp.Activities;

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
import com.example.zem.patientcareapp.CheckoutModule.DeliverPickupOption;
import com.example.zem.patientcareapp.CheckoutModule.PromosDiscounts;
import com.example.zem.patientcareapp.ConfigurationModule.Helpers;
import com.example.zem.patientcareapp.Controllers.BasketController;
import com.example.zem.patientcareapp.Controllers.DbHelper;
import com.example.zem.patientcareapp.Controllers.OrderController;
import com.example.zem.patientcareapp.Controllers.OrderPreferenceController;
import com.example.zem.patientcareapp.Controllers.ProductController;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Model.OrderModel;
import com.example.zem.patientcareapp.Network.ListOfPatientsRequest;
import com.example.zem.patientcareapp.Network.ServerRequest;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.SidebarModule.SidebarActivity;
import com.example.zem.patientcareapp.adapter.LazyAdapter;
import com.example.zem.patientcareapp.Network.PostRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dexter B. on 5/6/2015.
 */
public class ShoppingCartActivity extends AppCompatActivity implements View.OnClickListener {
    LazyAdapter adapter;
    DbHelper dbHelper;
    OrderController oc;
    OrderPreferenceController opc;

    Helpers helper;
    ServerRequest serverRequest;

    public ArrayList<HashMap<String, String>> items = new ArrayList();
    HashMap<String, String> row;

    public Double TotalAmount = 0.00, oldTotal = 0.00, newTotal = 0.00;
    int gbl_pos = 0, old_qty = 0, newQty = 0, basktQty;

    TextView tv_amount, p_name, p_total, p_price, p_description, et_qty;
    public static TextView total_amount;
    ListView lv_items;
    Button btnCheckout;
    Toolbar myToolBar;

    Context context;
    RequestQueue queue;

    BasketController bc;
    ProductController pc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart);

        btnCheckout = (Button) findViewById(R.id.btn_checkout_ready);
        lv_items = (ListView) findViewById(R.id.lv_items);
        total_amount = (TextView) findViewById(R.id.upper_cart_total);

        queue = Volley.newRequestQueue(this);
        dbHelper = new DbHelper(this);
        pc = new ProductController(this);
        bc = new BasketController();
        oc = new OrderController(this);
        opc = new OrderPreferenceController(this);

        helper = new Helpers();
        serverRequest = new ServerRequest();

        myToolBar = (Toolbar) findViewById(R.id.myToolBar);
        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Shopping Cart");
        myToolBar.setNavigationIcon(R.drawable.ic_back);

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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo
            menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.cart_menus, menu);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int pos = menuInfo.position;

        row = items.get(pos);
        gbl_pos = pos;
        old_qty = Integer.parseInt(row.get("quantity"));

        switch (item.getItemId()) {
//            case R.id.update_cart:
//                final double price = Double.parseDouble(row.get(ProductController.PRODUCT_PRICE));
//                final String productPacking, productUnit, productName;
//                final int productQtyPerPacking, productId, basketId;
//
//                productPacking = row.get(ProductController.PRODUCT_PACKING);
//                productQtyPerPacking = Integer.parseInt(row.get(ProductController.PRODUCT_QTY_PER_PACKING));
//                productUnit = row.get(ProductController.PRODUCT_UNIT);
//                productName = row.get(ProductController.PRODUCT_NAME);
//                productId = Integer.parseInt(row.get(ProductController.SERVER_PRODUCT_ID));
//                basketId = Integer.parseInt(row.get("basket_id"));
//                et_qty.setText(productQtyPerPacking + "");
//                basktQty = Integer.parseInt(row.get("quantity"));
//
//                // Setting all values in listview
//                p_name.setText(productName);
//                int num = basktQty / productQtyPerPacking;
//                p_description.setText("1 " + productUnit + " x " + basktQty + "(" + num + " " + helper.getPluralForm(productPacking, num) + ")");
//                p_price.setText("\u20B1 " + price);
//                p_total.setText("\u20B1 " + (basktQty * price));
//                et_qty.setText(basktQty + "");
//
//                alert.setCancelable(true);
//                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(final DialogInterface dialog, int which) {
//                        try {
//                            final int new_qty = Integer.parseInt(et_qty.getText().toString());
//                            newQty = new_qty;
//
//                            final double old_total = price * old_qty;
//                            final double new_total = price * new_qty;
//                            oldTotal = old_total;
//                            newTotal = new_total;
//
//                            final HashMap<String, String> hashMap = new HashMap();
//                            hashMap.put("product_id", String.valueOf(productId));
//                            hashMap.put("request", "crud");
//                            hashMap.put("quantity", String.valueOf(new_qty));
//                            hashMap.put("patient_id", String.valueOf(SidebarActivity.getUserID()));
//                            hashMap.put("table", "baskets");
//                            hashMap.put("action", "update");
//                            hashMap.put("is_direct_update", "true");
//                            hashMap.put("id", String.valueOf(basketId));
//
//                            final ProgressDialog pdialog = new ProgressDialog(getBaseContext());
//                            pdialog.setCancelable(false);
//                            pdialog.setMessage("Loading...");
//                            pdialog.show();
//
//                            PostRequest.send(getBaseContext(), hashMap, serverRequest, new RespondListener<JSONObject>() {
//                                @Override
//                                public void getResult(JSONObject response) {
//                                    try {
//                                        int success = response.getInt("success");
//
//                                        if (success == 1) {
//                                            tv_amount.setText(new_total + "");
//
//                                            row.put("quantity", new_qty + "");
//                                            items.set(gbl_pos, row);
//
//                                            TotalAmount -= old_total;
//                                            TotalAmount += new_total;
//                                            total_amount.setText("\u20B1 " + TotalAmount);
//
//                                            adapter.notifyDataSetChanged();
//                                        }
//                                    } catch (Exception e) {
//
//                                    }
//                                }
//                            }, new ErrorListener<VolleyError>() {
//                                public void getError(VolleyError error) {
//                                    Log.d("shoppingCart2", error + "");
//                                    Toast.makeText(context, "Couldn't update item. Please check your Internet connection", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        } catch (Exception e) {
//
//                        }
//                    }
//                });
//
//                alert.show();
//                break;
        }

        return super.onContextItemSelected(item);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_checkout_ready:
                OrderModel order_model = opc.getOrderPreference();
                if (order_model.isValid()) {
                    Intent summary_intent = new Intent(this, PromosDiscounts.class);
                    summary_intent.putExtra("order_model", order_model);
                    startActivity(summary_intent);
                    this.finish();
                } else {
                    startActivity(new Intent(this, DeliverPickupOption.class));
                    this.finish();
                }
                break;
        }
    }
}
