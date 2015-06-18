package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
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

/**
 * Created by User PC on 6/16/2015.
 */
public class SelectedProductActivity extends Activity implements View.OnClickListener {
    public static final String PRODUCT_ID = "productID";
    public static final String UP_ACTIVITY = "parent_activity";

    String get_parent = "";
    int temp_qty = 1;

    TextView prod_name, prod_generic, prod_unit, prod_price, qty_cart;
    ImageButton minus_qty, add_qty;
    Button add_cart_btn;
    ImageView prod_unit_type;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_product_layout);

        ActionBar actionbar = getActionBar();
        MainActivity.setCustomActionBar(actionbar);
        actionbar.setDisplayHomeAsUpEnabled(true);

        handler = new Handler();


        Intent intent = getIntent();
        get_parent = intent.getStringExtra(UP_ACTIVITY);

        prod_name = (TextView) findViewById(R.id.prod_name);
        prod_generic = (TextView) findViewById(R.id.prod_generic);
        prod_unit = (TextView) findViewById(R.id.prod_unit);
        prod_price = (TextView) findViewById(R.id.prod_price);
        qty_cart = (TextView) findViewById(R.id.qty_cart);
        minus_qty = (ImageButton) findViewById(R.id.minus_qty);
        add_qty = (ImageButton) findViewById(R.id.add_qty);
        add_cart_btn = (Button) findViewById(R.id.add_cart_btn);
        prod_unit_type = (ImageView) findViewById(R.id.prod_unit_type);

        minus_qty.setOnClickListener(this);
        add_qty.setOnClickListener(this);
        add_cart_btn.setOnClickListener(this);

        qty_cart.setText("" + temp_qty);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (get_parent.equals("HomeTile")) {
                    startActivity(new Intent(this, HomeTileActivity.class));
                    this.finish();
                }
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
                temp_qty -= 1;
                minus_qty.setImageResource(R.drawable.ic_minus_dead);

                if (temp_qty <= 0) {
                    qty_cart.setText("" + 0);
                    temp_qty = 0;
                } else {
                    qty_cart.setText("" + temp_qty);
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        minus_qty.setImageResource(R.drawable.ic_minus);
                    }
                }, 100);
                break;

            case R.id.add_qty:
                temp_qty += 1;
                add_qty.setImageResource(R.drawable.ic_plus_dead);
                qty_cart.setText("" + temp_qty);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        add_qty.setImageResource(R.drawable.ic_plus);
                    }
                }, 100);
                break;

            case R.id.add_cart_btn:

                break;
        }
    }
}
