package com.example.zem.patientcareapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.security.spec.EllipticCurve;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Dexter B. on 5/6/2015.
 */
public class ShoppingCartFragment extends Fragment {
    ListView lv_items;
    LazyAdapter adapter;
    public static ArrayList<HashMap<String, String>> items;
    HashMap<String, String> map;
    public static TextView total_amount;
    public Double TotalAmount = 0.00;

    EditText et_qty;
    TextView tv_amount, p_name, p_total, p_price ;
    int gbl_pos = 0, old_qty = 0;

    HashMap<String, String> row;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.cart_layout, container, false);

        lv_items = (ListView) rootView.findViewById(R.id.lv_items);
        total_amount = (TextView) rootView.findViewById(R.id.upper_cart_total);

        items = new ArrayList<HashMap<String, String>>();
        TableRow.LayoutParams lparams;
        map = new HashMap<String, String>();

        map.put("id", "100");
        map.put("name", "Biogesic");
        map.put("price", "2.75");
        map.put("quantity", "5");
        map.put("unit", "tablet");
        TotalAmount += 2.75 * 5;

        items.add(map);
        map = new HashMap<String, String>();

        map.put("id", "200");
        map.put("name", "Neozep1");
        map.put("price", "4.25");
        map.put("quantity", "10");
        map.put("unit", "tablet");
        TotalAmount += 4.25 * 10;

        items.add(map);
        map = new HashMap<String, String>();

        map.put("id", "300");
        map.put("name", "Neozep2");
        map.put("unit", "tablet");
        map.put("price", "4.25");
        map.put("quantity", "25");
        TotalAmount += 4.25 * 25;

        items.add(map);
        map = new HashMap<String, String>();

        map.put("id", "400");
        map.put("name", "Neozep3");
        map.put("price", "4.25");
        map.put("quantity", "35");
        map.put("unit", "bottle");
        TotalAmount += 4.25 * 35;

        items.add(map);
        map = new HashMap<String, String>();

        map.put("id", "500");
        map.put("name", "Neozep4");
        map.put("price", "4.25");
        map.put("quantity", "15");
        map.put("unit", "box");
        TotalAmount += 4.25 * 15;

        total_amount.setText("Php "+TotalAmount);
        items.add(map); // the data of ArrayList 'items' here should be fetched from database ;)

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
            old_qty = Integer.parseInt(row.get("quantity"));

            switch (item.getItemId()){
                case R.id.update_cart:
                    // TODO Auto-generated method stub

                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater= getActivity().getLayoutInflater();
                    //this is what I did to add the layout to the alert dialog
                    View layout=inflater.inflate(R.layout._partials_update_cart,null);
                    alert.setView(layout);
                    alert.setTitle("Update quantity");
                    alert.setCancelable(true);
                    alert.setNegativeButton("Cancel", null);

                    et_qty = (EditText) layout.findViewById(R.id.update_qty);
                    tv_amount = (TextView) layout.findViewById(R.id.new_total);
                    et_qty.setText(row.get("quantity"));
                    p_name = (TextView) layout.findViewById(R.id.product_name);
                    p_total = (TextView) layout.findViewById(R.id.new_total);
                    p_price = (TextView) layout.findViewById(R.id.product_price);

                    final double price = Double.parseDouble(row.get("price"));

                    p_name.setText(row.get("name"));
                    p_total.setText("Php "+ (old_qty* price));
                    p_price.setText("Php "+price+"/"+row.get("unit"));

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
                                    et_qty.setText("" + cs);
                                }
                                int new_qty = Integer.parseInt(cs);
                                p_total.setText("Php " + (price * new_qty));
                            } catch (Exception e) {

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
                           try {
                               int new_qty = Integer.parseInt(et_qty.getText().toString());

                               double old_total = price * old_qty;
                               double new_total = price * new_qty;

                               tv_amount.setText(new_total+"");

                               row.put("quantity", new_qty + "");
                               items.set(gbl_pos, row);

                               TotalAmount -= old_total;
                               TotalAmount += new_total;
                               total_amount.setText("Php " + TotalAmount);

                               adapter.notifyDataSetChanged();
                           }catch (Exception e){

                           }
                        }
                    });

                    alert.show();

                    return true;
                case R.id.delete_cart:
                    Toast.makeText(getActivity(), "Item with ID:" + row.get("id") + " successfully removed.", Toast.LENGTH_SHORT).show();
                    double amount = Double.parseDouble(row.get("price")) * Double.parseDouble(row.get("quantity"));
                    this.TotalAmount -= amount;
                    total_amount.setText("Php " + TotalAmount);
                    items.remove(pos);
                    this.adapter.notifyDataSetChanged();

                    System.out.println(row);
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
        return false;
    }
}
