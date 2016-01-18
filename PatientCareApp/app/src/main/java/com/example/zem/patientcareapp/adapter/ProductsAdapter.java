package com.example.zem.patientcareapp.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.ConfigurationModule.Helpers;
import com.example.zem.patientcareapp.Controllers.DbHelper;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.PostRequest;
import com.example.zem.patientcareapp.Activities.ProductsActivity;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.ShowPrescriptionDialog;
import com.example.zem.patientcareapp.SidebarModule.SidebarActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User PC on 11/27/2015.
 */

public class ProductsAdapter extends ArrayAdapter implements View.OnClickListener {
    LayoutInflater inflater;
    TextView product_name, rs_price, cart_text, out_of_stock, is_promo;
    ImageView product_image;
    LinearLayout add_to_cart, root;
    ToggleButton add_to_favorite;
    ImageView cart_icon;

    Context context;

    DbHelper db;
    Helpers helpers;

    ArrayList<Map<String, String>> products_items, basket_items;
    ArrayList<Integer> list_favorites;

    HashMap<String, String> map_all_products_promos;
    ArrayList<HashMap<String, String>> specific_products_promos;

    public ProductsAdapter(Context context, int resource, ArrayList<Map<String, String>> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        this.context = context;
        products_items = objects;

        map_all_products_promos = new HashMap();
        specific_products_promos = new ArrayList();

        for (int x = 0; x < ProductsActivity.no_code_promos.size(); x++) {
            if (ProductsActivity.no_code_promos.get(x).get("product_applicability").equals("ALL_PRODUCTS"))
                map_all_products_promos.putAll(ProductsActivity.no_code_promos.get(x));
            else {
                HashMap<String, String> map = new HashMap();
                map.putAll(ProductsActivity.no_code_promos.get(x));
                specific_products_promos.add(map);
            }
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.product_item, parent, false);

        product_image = (ImageView) convertView.findViewById(R.id.product_image);
        product_name = (TextView) convertView.findViewById(R.id.product_name);
        rs_price = (TextView) convertView.findViewById(R.id.rs_price);
        add_to_cart = (LinearLayout) convertView.findViewById(R.id.add_to_cart);
        add_to_favorite = (ToggleButton) convertView.findViewById(R.id.add_to_favorite);
        cart_icon = (ImageView) convertView.findViewById(R.id.cart_icon);
        cart_text = (TextView) convertView.findViewById(R.id.cart_text);
        out_of_stock = (TextView) convertView.findViewById(R.id.out_of_stock);
        root = (LinearLayout) convertView.findViewById(R.id.root);
        is_promo = (TextView) convertView.findViewById(R.id.is_promo);

        add_to_cart.setTag(position);
        add_to_favorite.setTag(position);

        if (Integer.parseInt(products_items.get(position).get("available_quantity")) == 0) {
            out_of_stock.setVisibility(View.VISIBLE);
            add_to_cart.setVisibility(View.GONE);
        }
        add_to_cart.setOnClickListener(this);

        basket_items = ProductsActivity.basket_items;
        db = new DbHelper(context);
        helpers = new Helpers();
        list_favorites = db.getFavoritesByUserID(SidebarActivity.getUserID());

        ArrayList<String> all_promos = new ArrayList();
        double final_peso_discount = 0, final_min_purchase = 0;
        String final_free_gift = "", final_percentage_discount = "", final_free_delivery = "", final_qty_required = "0", final_is_every = "";
        int product_id = Integer.parseInt(products_items.get(position).get("product_id"));

        for (int x = 0; x < list_favorites.size(); x++) {
            if (list_favorites.get(x) == product_id)
                add_to_favorite.setChecked(true);
        }

        if (ProductsActivity.no_code_promos.size() > 0) {
            if (map_all_products_promos.size() > 0) {
                final_min_purchase = Double.parseDouble(map_all_products_promos.get("minimum_purchase"));
                final_qty_required = map_all_products_promos.get("pr_qty_required");
                final_peso_discount = Double.parseDouble(map_all_products_promos.get("pr_peso"));
                final_free_delivery = map_all_products_promos.get("pr_free_delivery");
                final_percentage_discount = map_all_products_promos.get("pr_percentage");
                final_free_gift = "";

            } else if (specific_products_promos.size() > 0) {
                for (int x = 0; x < specific_products_promos.size(); x++) {
                    if (specific_products_promos.get(x).get("product_id").equals(products_items.get(position).get("product_id"))) {
                        final_peso_discount = Double.parseDouble(specific_products_promos.get(x).get("peso_discount"));
                        final_qty_required = specific_products_promos.get(x).get("quantity_required");
                        final_free_delivery = specific_products_promos.get(x).get("is_free_delivery");
                        final_percentage_discount = specific_products_promos.get(x).get("percentage_discount");
                        final_free_gift = specific_products_promos.get(x).get("has_free_gifts");
                        final_is_every = specific_products_promos.get(x).get("is_every");
                    }
                }
            }

            if (final_peso_discount > 0)
                all_promos.add(final_peso_discount + " Php  off");

            if (final_free_delivery.equals("1"))
                all_promos.add("Free Delivery");

            if (final_free_gift.equals("1"))
                all_promos.add("A free item");

            if (!final_percentage_discount.equals("0"))
                all_promos.add(final_percentage_discount + "% off");
        }

        if (all_promos.size() > 0) {
            is_promo.setVisibility(View.VISIBLE);
            String final_list_of_promos = " ";

            for (int x = 0; x < all_promos.size(); x++) {
                final_list_of_promos = final_list_of_promos + all_promos.get(x);

                if (!((x + 1) == all_promos.size()))
                    final_list_of_promos = final_list_of_promos + ",";
                final_list_of_promos = final_list_of_promos + " ";
            }
            String purchases = helpers.getPluralForm(products_items.get(position).get("packing"), Integer.parseInt(final_qty_required));

            if (!final_qty_required.equals("0")) {
                if (final_is_every.equals("1"))
                    is_promo.setText("*" + final_list_of_promos + "for every " + final_qty_required + " " + purchases);
                else
                    is_promo.setText("*" + final_list_of_promos + "for " + final_qty_required + " " + purchases + " or more");
            } else if (final_min_purchase > 0) {
                is_promo.setText("*" + final_list_of_promos + "for a minimum purchase of " + final_min_purchase);
            }
        }

        product_name.setText(products_items.get(position).get("name"));
        rs_price.setText("Php " + products_items.get(position).get("price") + "/" + products_items.get(position).get("packing"));

        return convertView;
    }

    void AddToCart(HashMap<String, String> map) {
        ProductsActivity.map = map;
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.add_to_cart:
                final Helpers helpers = new Helpers();
                int pos = Integer.parseInt(String.valueOf(v.getTag()));
                int product_id = Integer.parseInt(products_items.get(pos).get("product_id"));
                int productQty = 1, check = 0, old_qty = 0;
                String server_id = null;

                int is_required = Integer.parseInt(products_items.get(pos).get("prescription_required"));

                final ProgressDialog pdialog = new ProgressDialog(context);
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
                    PostRequest.send(context, hashMap, new RespondListener<JSONObject>() {
                        @Override
                        public void getResult(JSONObject response) {
                            try {
                                int success = response.getInt("success");

                                if (success == 1) {
                                    ProductsActivity.transferHashMap(hashMap);
                                    Snackbar.make(v, "Your cart has been updated", Snackbar.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Snackbar.make(v, e + "", Snackbar.LENGTH_SHORT).show();
                            }
                            pdialog.dismiss();
                        }
                    }, new ErrorListener<VolleyError>() {
                        public void getError(VolleyError error) {
                            pdialog.dismiss();
                            Snackbar.make(v, "Network error", Snackbar.LENGTH_SHORT).show();
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
                        map = helpers.showPrescriptionDialog(context);

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

                                    PostRequest.send(context, hashMap, new RespondListener<JSONObject>() {
                                        @Override
                                        public void getResult(JSONObject response) {
                                            try {
                                                int success = response.getInt("success");

                                                if (success == 1) {
                                                    hashMap.put("server_id", String.valueOf(response.getInt("last_inserted_id")));
                                                    ProductsActivity.transferHashMap(hashMap);
                                                    Snackbar.make(v, "New item has been added to your cart", Snackbar.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                Snackbar.make(v, e + "", Snackbar.LENGTH_SHORT).show();
                                            }
                                            pdialog.dismiss();
                                        }
                                    }, new ErrorListener<VolleyError>() {
                                        public void getError(VolleyError error) {
                                            pdialog.dismiss();
                                            Snackbar.make(v, "Please check your Internet connection", Snackbar.LENGTH_SHORT).show();
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
                            AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(context);
                            confirmationDialog.setTitle("Attention!");
                            confirmationDialog.setMessage("This product requires you to upload a prescription, do you wish to continue ?");
                            confirmationDialog.setNegativeButton("No", null);
                            confirmationDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AddToCart(hashMap);
                                    context.startActivity(new Intent(context, ShowPrescriptionDialog.class));
                                }
                            });
                            confirmationDialog.show();
                        }
                    } else { //IF PRESCRIPTION IS NOT REQUIRED
                        pdialog.show();

                        hashMap.put("prescription_id", "0");
                        hashMap.put("is_approved", "1");

                        PostRequest.send(context, hashMap, new RespondListener<JSONObject>() {
                            @Override
                            public void getResult(JSONObject response) {
                                try {
                                    int success = response.getInt("success");

                                    if (success == 1) {
                                        hashMap.put("server_id", String.valueOf(response.getInt("last_inserted_id")));
                                        ProductsActivity.transferHashMap(hashMap);
                                        Snackbar.make(v, "New item has been added to your cart", Snackbar.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Snackbar.make(v, e + "", Snackbar.LENGTH_SHORT).show();
                                }
                                pdialog.dismiss();
                            }
                        }, new ErrorListener<VolleyError>() {
                            public void getError(VolleyError error) {
                                pdialog.dismiss();
                                Snackbar.make(v, "Please check your Internet connection", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                break;
        }
    }
}
