package com.example.zem.patientcareapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dexter B. on 5/6/2015.
 */
public class ShoppingCartFragment extends Fragment implements View.OnClickListener {
    ListView lv_items;
    LazyAdapter adapter;
    public static ArrayList<HashMap<String, String>> items;
    HashMap<String, String> map;
    public static TextView total_amount;
    public Double TotalAmount = 0.00, oldTotal = 0.00, newTotal = 0.00;

    EditText et_qty;
    TextView tv_amount, p_name, p_total, p_price ;
    int gbl_pos = 0, old_qty = 0, newQty = 0;

    HashMap<String, String> row;
    DbHelper dbHelper;
    Helpers helper;
    ServerRequest serverRequest;
    View root_view;
    Button btnCheckout;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.cart_layout, container, false);
        root_view = rootView;

        btnCheckout = (Button) root_view.findViewById(R.id.btn_checkout_ready);
        btnCheckout.setOnClickListener(this);

        lv_items = (ListView) rootView.findViewById(R.id.lv_items);
        total_amount = (TextView) rootView.findViewById(R.id.upper_cart_total);

        dbHelper = new DbHelper(getActivity());
        helper = new Helpers();
        items = dbHelper.getAllBasketItems(); // returns all basket items for the currently loggedin patient

        for(HashMap<String, String> item : items){
            double price = Double.parseDouble(item.get(DbHelper.PRODUCT_PRICE));
            double quantity = Double.parseDouble(item.get(DbHelper.BASKET_QUANTITY));
            double total = price*quantity;
            TotalAmount+= total;
        }

        total_amount.setText("Php " + TotalAmount);

        adapter = new LazyAdapter(getActivity(), items, "basket_items");

        lv_items.setAdapter(adapter);

        lv_items.setOnCreateContextMenuListener(this);

        return rootView;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.cart_menus, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (getUserVisibleHint()) {
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            final int pos = menuInfo.position;

            row = items.get(pos);
            gbl_pos = pos;
            old_qty = Integer.parseInt(row.get(DbHelper.BASKET_QUANTITY));

            switch (item.getItemId()){
                case R.id.update_cart:
                    // TODO Auto-generated method stub

                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater= getActivity().getLayoutInflater();
                    //this is what I did to add the layout to the alert dialog
                    @SuppressLint("InflateParams") View layout=inflater.inflate(R.layout._partials_update_cart, null);
                    alert.setView(layout);
                    alert.setTitle("Update quantity");
                    alert.setCancelable(true);
                    alert.setNegativeButton("Cancel", null);

                    et_qty = (EditText) layout.findViewById(R.id.update_qty);
                    tv_amount = (TextView) layout.findViewById(R.id.new_total);
                    et_qty.setText(row.get(DbHelper.BASKET_QUANTITY));
                    p_name = (TextView) layout.findViewById(R.id.product_name);
                    p_total = (TextView) layout.findViewById(R.id.new_total);
                    p_price = (TextView) layout.findViewById(R.id.product_price);

                    final double price = Double.parseDouble(row.get(DbHelper.PRODUCT_PRICE));

                    p_name.setText(row.get(DbHelper.PRODUCT_NAME));
                    p_total.setText("Php "+ (old_qty* price));
                    p_price.setText("Php "+price+"/"+row.get(DbHelper.PRODUCT_UNIT));

                    et_qty.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            try {
                                String cs = s.toString();
                                if (cs.equals("") || cs.equals("0")) {
                                    cs = "1";
                                }
                                int new_qty = Integer.parseInt(cs);
                                p_total.setText("Php " + (price * new_qty));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    alert.setCancelable(true);
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                          if( helper.isNetworkAvailable(getActivity()) ){
                              try {
                                  final int new_qty = Integer.parseInt(et_qty.getText().toString());
                                  newQty = new_qty;

                                  final double old_total = price * old_qty;
                                  final double new_total = price * new_qty;
                                  oldTotal = old_total;
                                  newTotal = new_total;

                                  System.out.println("PAKING BASKET ROW: "+row.toString() );

                                  serverRequest = new ServerRequest();
                                  final Basket basket = dbHelper.getBasket(Integer.parseInt(row.get("product_id")));
                                  System.out.println("PAKING BASKET: "+basket.getId()+" "+basket.getQuantity() + " "+basket.getProductId());
                                  basket.setQuantity(new_qty);

                                  HashMap<String, String> hashMap = new HashMap<>();
                                  hashMap.put("product_id", String.valueOf(basket.getProductId()));
                                  hashMap.put("quantity", String.valueOf(new_qty));
                                  hashMap.put("patient_id", String.valueOf(dbHelper.getCurrentLoggedInPatient().getServerID()));
                                  hashMap.put("table", "baskets");
                                  hashMap.put("request", "crud");
                                  hashMap.put("action", "update");
                                  hashMap.put("id", String.valueOf(basket.getBasketId()));

                                  /*if( dbHelper.updateBasket(basket) ){*/
                                  serverRequest.init(getActivity(), hashMap, "insert_basket");

                                  root_view.postDelayed(new Runnable() {
                                      public void run() {
                                          // Actions to do after 3 seconds
                                          boolean responseFromServer = serverRequest.getResponse();
                                          System.out.println("FUCKING RESPONSE FROM SERVER: " + responseFromServer);
                                          if (responseFromServer) {
                                              if ( dbHelper.updateBasket(basket) ) {
                                                  tv_amount.setText(new_total + "");

                                                  row.put(DbHelper.BASKET_QUANTITY, new_qty + "");
                                                  items.set(gbl_pos, row);

                                                  TotalAmount -= old_total;
                                                  TotalAmount += new_total;
                                                  total_amount.setText("Php " + TotalAmount);

                                                  adapter.notifyDataSetChanged();
                                                  Toast.makeText(getActivity(), "Your cart has been updated.", Toast.LENGTH_SHORT).show();
                                              } else {
                                                  Toast.makeText(getActivity(), "Sorry, we can't update your item right now. Please try again later.", Toast.LENGTH_SHORT).show();
                                              }

                                          }
                                      }
                                  }, 3000);


                                     /* tv_amount.setText(new_total+"");

                                      row.put(DbHelper.BASKET_QUANTITY, new_qty + "");
                                      items.set(gbl_pos, row);

                                      TotalAmount -= old_total;
                                      TotalAmount += new_total;
                                      total_amount.setText("Php " + TotalAmount);

                                      adapter.notifyDataSetChanged();*/

                                  /*}else{
                                      Toast.makeText(getActivity(), "Sorry, Something went wrong.", Toast.LENGTH_SHORT).show();
                                  }*/
                              }catch (Exception e){
                                  System.out.println("PAKING BASKET ERROR: "+e.getMessage());
                                  e.printStackTrace();
                              }
                          }else{
                              Toast.makeText(getActivity(), "Please connect to the internet to continue..", Toast.LENGTH_SHORT).show();
                          }

                        }
                    });

                    alert.show();

                    return true;
                case R.id.delete_cart:
                    AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(getActivity());
                    confirmationDialog.setTitle("Are you sure?");
                    confirmationDialog.setNegativeButton("No", null);
                    confirmationDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if( helper.isNetworkAvailable(getActivity()) ){
                                final double amount = Double.parseDouble(row.get("price")) * Double.parseDouble(row.get("quantity"));
                                serverRequest = new ServerRequest();
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("table", "baskets");
                                hashMap.put("request", "crud");
                                hashMap.put("action", "delete");
                                hashMap.put("id", String.valueOf(row.get("basket_id")));
                                serverRequest.init(getActivity(), hashMap, "insert");



                                root_view.postDelayed(new Runnable(){
                                    public void run(){
                                        // Actions to do after 3 seconds
                                        boolean responseFromServer = serverRequest.getResponse();
                                        System.out.println("FUCKING RESPONSE FROM SERVER: " + responseFromServer);
                                        if(responseFromServer){
                                            if( dbHelper.deleteBasketItem(Integer.parseInt(row.get("basket_id"))) ) {
                                                TotalAmount -= amount;
                                                total_amount.setText("Php " + TotalAmount);
                                                items.remove(pos);
                                                adapter.notifyDataSetChanged();
                                                Toast.makeText(getActivity(), "An item has been successfully removed.", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(getActivity(),"Sorry, we can't delete your item right now. Please try again later.", Toast.LENGTH_SHORT ).show();
                                            }

                                        }
                                    }
                                }, 3000);

                            }else{
                                Toast.makeText(getActivity(), "Please connect to the internet to continue..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    confirmationDialog.show();

                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
        return false;
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_checkout_ready:
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                Dialog builder = new Dialog(getActivity());
                builder.setTitle("Checkout");
                builder.setContentView(R.layout.checkout_layout);
//                builder.setView(R.layout.checkout_layout);
                builder.show();

                break;
        }
    }
}
