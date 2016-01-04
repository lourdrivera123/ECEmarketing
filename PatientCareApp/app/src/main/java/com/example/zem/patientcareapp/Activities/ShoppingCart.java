package com.example.zem.patientcareapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
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
import com.example.zem.patientcareapp.Controllers.ProductController;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Model.OrderModel;
import com.example.zem.patientcareapp.Network.ListOfPatientsRequest;
import com.example.zem.patientcareapp.Network.PostRequest;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.SidebarModule.SidebarActivity;
import com.example.zem.patientcareapp.adapter.LazyAdapter;
import com.example.zem.patientcareapp.adapter.ShoppingCartAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User PC on 12/19/2015.
 */
public class ShoppingCart extends AppCompatActivity implements View.OnClickListener {
    Toolbar myToolBar;
    ListView lisOfItems;
    LinearLayout root;
    TextView proceed_to_checkout;
    public static TextView total_amount;
    OrderPreferenceController opc;

    ShoppingCartAdapter adapter;
    BasketController bc;

    public ArrayList<HashMap<String, String>> items = new ArrayList();

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

        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Shopping Cart");
        myToolBar.setNavigationIcon(R.drawable.ic_back);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        proceed_to_checkout.setOnClickListener(this);

        String url_raw = "get_basket_details&patient_id=" + SidebarActivity.getUserID();
        ListOfPatientsRequest.getJSONobj(this, url_raw, "baskets", new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                double TotalAmount = 0.00;

                try {
                    int success = response.getInt("success");

                    if (success == 1) {
                        JSONArray json_mysql = response.getJSONArray("baskets");
                        items = bc.convertFromJson(ShoppingCart.this, json_mysql);

                        adapter = new ShoppingCartAdapter(ShoppingCart.this, R.layout.item_shopping_cart, items);
                        lisOfItems.setAdapter(adapter);

                        for (HashMap<String, String> item : items) {
                            double price = Double.parseDouble(item.get("price"));
                            double quantity = Double.parseDouble(item.get("quantity"));
                            double total = price * quantity;
                            TotalAmount += total;
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(ShoppingCart.this, e + "", Toast.LENGTH_SHORT).show();
                }
                total_amount.setText("Php " + TotalAmount);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.proceed_to_checkout:
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