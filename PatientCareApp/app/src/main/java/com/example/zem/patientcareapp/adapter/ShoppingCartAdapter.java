package com.example.zem.patientcareapp.adapter;

import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.Activities.ShoppingCartActivity;
import com.example.zem.patientcareapp.ConfigurationModule.Helpers;
import com.example.zem.patientcareapp.Controllers.DbHelper;
import com.example.zem.patientcareapp.Customizations.RoundedAvatarDrawable;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.PostRequest;
import com.example.zem.patientcareapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User PC on 12/19/2015.
 */

public class ShoppingCartAdapter extends ArrayAdapter implements View.OnClickListener {
    LayoutInflater inflater;
    Context context;

    ArrayList<HashMap<String, String>> objects;

    Helpers helpers;
    DbHelper dbHelper;

    ImageView prod_image;
    ImageButton delete, up_btn, down_btn;
    LinearLayout root;
    TextView productName, product_quantity, product_price, total, is_promo, promo_total;

    double cart_total = 0;
    HashMap<String, String> map_all_products_promos;
    ArrayList<HashMap<String, String>> specific_products_promos;

    public ShoppingCartAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.objects = objects;

        helpers = new Helpers();
        dbHelper = new DbHelper(context);
        map_all_products_promos = new HashMap();
        specific_products_promos = new ArrayList();
        cart_total = 0;

        for (int x = 0; x < ShoppingCartActivity.no_code_promos.size(); x++) {
            if (ShoppingCartActivity.no_code_promos.get(x).get("product_applicability").equals("ALL_PRODUCTS"))
                map_all_products_promos.putAll(ShoppingCartActivity.no_code_promos.get(x));
            else {
                HashMap<String, String> map = new HashMap();
                map.putAll(ShoppingCartActivity.no_code_promos.get(x));
                specific_products_promos.add(map);
            }
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_shopping_cart, parent, false);

        prod_image = (ImageView) convertView.findViewById(R.id.prod_image);
        productName = (TextView) convertView.findViewById(R.id.productName);
        product_price = (TextView) convertView.findViewById(R.id.product_price);
        product_quantity = (TextView) convertView.findViewById(R.id.product_quantity);
        is_promo = (TextView) convertView.findViewById(R.id.is_promo);
        total = (TextView) convertView.findViewById(R.id.total);
        delete = (ImageButton) convertView.findViewById(R.id.delete);
        up_btn = (ImageButton) convertView.findViewById(R.id.up_btn);
        down_btn = (ImageButton) convertView.findViewById(R.id.down_btn);
        root = (LinearLayout) convertView.findViewById(R.id.root);
        promo_total = (TextView) convertView.findViewById(R.id.promo_total);

        delete.setTag(position);
        up_btn.setTag(product_quantity);
        down_btn.setTag(product_quantity);

        final double price = Double.parseDouble(objects.get(position).get("price"));
        int qty_per_packing = Integer.parseInt(objects.get(position).get("qty_per_packing"));
        final int available_qty = Integer.parseInt(objects.get(position).get("available_quantity"));
        int cart_quantity = Integer.parseInt(objects.get(position).get("quantity"));
        double total_per_item = price * cart_quantity;

        if (map_all_products_promos.size() > 0 || specific_products_promos.size() > 0) {
            int final_qty_required = 0;
            String final_free_delivery = "0", final_peso = "0", final_free_gifts = "0", final_min_purchase = "", final_is_every = "0";
            int final_percentage = 0;
            ArrayList<String> all_promos = new ArrayList();

            if (map_all_products_promos.size() > 0) {
                Log.d("all_products", map_all_products_promos + "");

                final_qty_required = Integer.parseInt(map_all_products_promos.get("pr_qty_required"));
                final_free_delivery = map_all_products_promos.get("pr_free_delivery");
                final_percentage = Integer.parseInt(map_all_products_promos.get("pr_percentage"));
                final_peso = map_all_products_promos.get("pr_peso");
                final_min_purchase = map_all_products_promos.get("minimum_purchase");

            } else if (specific_products_promos.size() > 0) {
                Log.d("spec_products", specific_products_promos + "");

                for (int x = 0; x < specific_products_promos.size(); x++) {
                    if (specific_products_promos.get(x).get("product_id").equals(objects.get(position).get("product_id"))) {

                        final_qty_required = Integer.parseInt(specific_products_promos.get(x).get("quantity_required"));
                        final_free_delivery = specific_products_promos.get(x).get("is_free_delivery");
                        final_percentage = Integer.parseInt(specific_products_promos.get(x).get("percentage_discount"));
                        final_peso = specific_products_promos.get(x).get("peso_discount");
                        final_free_gifts = specific_products_promos.get(x).get("has_free_gifts");
                        final_is_every = specific_products_promos.get(x).get("is_every");
                    }
                }
            }

            if (final_free_delivery.equals("1"))
                all_promos.add("Free delivery");

            if (final_percentage > 0) {
                all_promos.add(final_percentage + "% off");

                if (final_qty_required > 0) {
                    if (cart_quantity >= final_qty_required) {
                        promo_total.setVisibility(View.VISIBLE);
                        total.setPaintFlags(total.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        DecimalFormat df = new DecimalFormat("#.##");
                        double percent_off = Double.parseDouble(df.format(final_percentage / 100.0f));
                        double discounted_total = (price * cart_quantity) - ((price * cart_quantity) * percent_off);
                        promo_total.setText("Php " + discounted_total);
                    }
                }
            }

            if (!final_peso.equals("0"))
                all_promos.add(final_peso + " Php off");

            if (!final_free_gifts.equals("0"))
                all_promos.add("A free item");

            if (all_promos.size() > 0) {
                is_promo.setVisibility(View.VISIBLE);
                String final_list_of_promos = " ";

                for (int x = 0; x < all_promos.size(); x++) {
                    final_list_of_promos = final_list_of_promos + all_promos.get(x);

                    if (!((x + 1) == all_promos.size()))
                        final_list_of_promos = final_list_of_promos + ",";
                    final_list_of_promos = final_list_of_promos + " ";
                }
                String purchases = helpers.getPluralForm(objects.get(position).get("packing"), final_qty_required);

                if (final_qty_required > 0) {
                    if (final_is_every.equals("1"))
                        is_promo.setText("*" + final_list_of_promos + " for every " + final_qty_required + " " + purchases);
                    else
                        is_promo.setText("*" + final_list_of_promos + " for " + final_qty_required + " " + purchases + " or more");
                } else if (!final_min_purchase.equals("0"))
                    is_promo.setText("*" + final_list_of_promos + "for a minimum purchase of " + final_min_purchase);
            }
        }

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ambrolex_family);
        final int shadowSize = context.getResources().getDimensionPixelSize(R.dimen.shadow_size);
        final int shadowColor = context.getResources().getColor(R.color.shadow_color);
        prod_image.setImageDrawable(new RoundedAvatarDrawable(icon, shadowSize, shadowColor));
        prod_image.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        productName.setText(objects.get(position).get("name"));
        total.setText("Php " + (price * cart_quantity));
        product_quantity.setText(String.valueOf(cart_quantity));
        String plural = helpers.getPluralForm(objects.get(position).get("unit"), qty_per_packing);
        product_price.setText("Php " + price + "/" + objects.get(position).get("packing") +
                " (" + qty_per_packing + " " + plural + ")");
        cart_total = cart_total + total_per_item;

        delete.setOnClickListener(this);

        final View finalConvertView = convertView;
        up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = (TextView) v.getTag();
                int lastQty = Integer.parseInt(txt.getText().toString());
                TextView p_total = (TextView) finalConvertView.findViewById(R.id.total);

                lastQty = lastQty + 1;

                if (lastQty <= available_qty) {
                    double total_per_item = price * lastQty;

                    txt.setText(lastQty + "");
                    p_total.setText("Php " + total_per_item);
                    cart_total = cart_total + price;
                    ShoppingCartActivity.total_amount.setText("Php " + cart_total);

                    HashMap<String, String> temp = objects.get(position);
                    temp.put("quantity", String.valueOf(lastQty));
                    objects.set(position, temp);
                } else
                    Snackbar.make(v, "Out of stock", Snackbar.LENGTH_SHORT).show();
            }
        });

        down_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = (TextView) v.getTag();
                TextView p_total = (TextView) finalConvertView.findViewById(R.id.total);

                int lastQty = Integer.parseInt(txt.getText().toString());
                lastQty = lastQty - 1;

                if (lastQty < 1) {
                    lastQty = 1;
                    ShoppingCartActivity.total_amount.setText("Php " + cart_total);
                } else {
                    cart_total = cart_total - price;
                    ShoppingCartActivity.total_amount.setText("Php " + cart_total);
                }

                double total_per_item = price * lastQty;
                txt.setText(lastQty + "");
                p_total.setText("Php " + total_per_item);

                HashMap<String, String> temp = objects.get(position);
                temp.put("quantity", String.valueOf(lastQty));
                objects.set(position, temp);
            }
        });

        return convertView;
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.delete:
                final int pos = Integer.parseInt(String.valueOf(v.getTag()));
                final int server_id = Integer.parseInt(objects.get(pos).get("basket_id"));

                AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(context);
                confirmationDialog.setTitle("Delete item?");
                confirmationDialog.setNegativeButton("No", null);
                confirmationDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        HashMap<String, String> hashMap = new HashMap();
                        hashMap.put("table", "baskets");
                        hashMap.put("request", "crud");
                        hashMap.put("action", "delete");
                        hashMap.put("id", String.valueOf(server_id));

                        final ProgressDialog pdialog = new ProgressDialog(context);
                        pdialog.setCancelable(false);
                        pdialog.setMessage("Loading...");
                        pdialog.show();

                        PostRequest.send(context, hashMap, new RespondListener<JSONObject>() {
                            @Override
                            public void getResult(JSONObject response) {
                                try {
                                    int success = response.getInt("success");

                                    if (success == 1) {
                                        objects.remove(pos);
                                        ShoppingCartAdapter.this.notifyDataSetChanged();
                                        Snackbar.make(v, "Item has been deleted", Snackbar.LENGTH_SHORT).show();
                                    } else
                                        Snackbar.make(root, "Server error occurred", Snackbar.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    Snackbar.make(root, e + "", Snackbar.LENGTH_SHORT).show();
                                }
                                pdialog.dismiss();
                            }
                        }, new ErrorListener<VolleyError>() {
                            public void getError(VolleyError error) {
                                pdialog.dismiss();
                                Snackbar.make(root, "Network error", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                confirmationDialog.show();
                break;
        }
    }
}
