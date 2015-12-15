package com.example.zem.patientcareapp.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
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
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.ConfigurationModule.Helpers;
import com.example.zem.patientcareapp.Controllers.BasketController;
import com.example.zem.patientcareapp.Controllers.DbHelper;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.PostRequest;
import com.example.zem.patientcareapp.Activities.ProductsActivity;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.Network.ServerRequest;
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
    TextView original_price, product_name, promo, rs_price;
    ImageView product_image;
    LinearLayout if_promo_layout, add_to_cart;
    ToggleButton add_to_favorite;

    Context context;

    BasketController bc;
    ServerRequest serverRequest;
    DbHelper db;

    ArrayList<Map<String, String>> products_items, basket_items;
    ArrayList<Integer> list_favorites;

    public ProductsAdapter(Context context, int resource, ArrayList<Map<String, String>> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        this.context = context;
        products_items = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_gridview_products, parent, false);

        original_price = (TextView) view.findViewById(R.id.original_price);
        product_image = (ImageView) view.findViewById(R.id.product_image);
        product_name = (TextView) view.findViewById(R.id.product_name);
        if_promo_layout = (LinearLayout) view.findViewById(R.id.if_promo_layout);
        promo = (TextView) view.findViewById(R.id.promo);
        rs_price = (TextView) view.findViewById(R.id.rs_price);
        add_to_cart = (LinearLayout) view.findViewById(R.id.add_to_cart);
        add_to_favorite = (ToggleButton) view.findViewById(R.id.add_to_favorite);

        add_to_cart.setTag(position);
        add_to_favorite.setTag(position);

        add_to_cart.setOnClickListener(this);

        serverRequest = new ServerRequest();
        bc = new BasketController(context);
        basket_items = ProductsActivity.basket_items;
        db = new DbHelper(context);
        list_favorites = db.getFavoritesByUserID(SidebarActivity.getUserID());

        try {
            int product_id = Integer.parseInt(products_items.get(position).get("product_id"));

            for (int x = 0; x < list_favorites.size(); x++) {
                if (list_favorites.get(x) == product_id)
                    add_to_favorite.setChecked(true);
            }
        } catch (Exception e) {
            Toast.makeText(context, e + "", Toast.LENGTH_SHORT).show();
        }

        original_price.setPaintFlags(original_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        product_name.setText(products_items.get(position).get("name"));
        rs_price.setText("Php " + products_items.get(position).get("price") + "/" + products_items.get(position).get("packing"));

        return view;
    }

    void AddToCart(HashMap<String, String> map) {
        ProductsActivity.map = map;
    }

    @Override
    public void onClick(View v) {
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
                    PostRequest.send(context, hashMap, serverRequest, new RespondListener<JSONObject>() {
                        @Override
                        public void getResult(JSONObject response) {
                            try {
                                int success = response.getInt("success");

                                if (success == 1) {
                                    if (!bc.saveBasket(hashMap))
                                        Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show();
                                    ProductsActivity.transferHashMap(hashMap);
                                    Toast.makeText(context, "Your cart has been updated", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(context, e + "", Toast.LENGTH_SHORT).show();
                            }
                            pdialog.dismiss();
                        }
                    }, new ErrorListener<VolleyError>() {
                        public void getError(VolleyError error) {
                            pdialog.dismiss();
                            Toast.makeText(context, "Please check your Internet connection", Toast.LENGTH_SHORT).show();
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

                                    PostRequest.send(context, hashMap, serverRequest, new RespondListener<JSONObject>() {
                                        @Override
                                        public void getResult(JSONObject response) {
                                            try {
                                                int success = response.getInt("success");

                                                if (success == 1) {
                                                    hashMap.put("server_id", String.valueOf(response.getInt("last_inserted_id")));
                                                    if (bc.saveBasket(hashMap))
                                                        Toast.makeText(context, "New item has been added to your cart", Toast.LENGTH_SHORT).show();
                                                    else
                                                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                    ProductsActivity.transferHashMap(hashMap);
                                                }
                                            } catch (JSONException e) {
                                                Toast.makeText(context, e + "", Toast.LENGTH_SHORT).show();
                                            }
                                            pdialog.dismiss();
                                        }
                                    }, new ErrorListener<VolleyError>() {
                                        public void getError(VolleyError error) {
                                            pdialog.dismiss();
                                            Toast.makeText(context, "Couldn't add item. Please check your Internet connection", Toast.LENGTH_LONG).show();
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

                        PostRequest.send(context, hashMap, serverRequest, new RespondListener<JSONObject>() {
                            @Override
                            public void getResult(JSONObject response) {
                                try {
                                    int success = response.getInt("success");

                                    if (success == 1) {
                                        hashMap.put("server_id", String.valueOf(response.getInt("last_inserted_id")));

                                        if (bc.saveBasket(hashMap))
                                            Toast.makeText(context, "New item has been added to your cart", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        ProductsActivity.transferHashMap(hashMap);
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(context, e + "", Toast.LENGTH_SHORT).show();
                                }
                                pdialog.dismiss();
                            }
                        }, new ErrorListener<VolleyError>() {
                            public void getError(VolleyError error) {
                                pdialog.dismiss();
                                Toast.makeText(context, "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                break;
        }
    }
}
