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
import java.util.Objects;

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

    public static double cart_total_amount;

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
        cart_total_amount = 0.0;

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
        final double total_per_item = price * cart_quantity;
        final DecimalFormat df = new DecimalFormat("#.##");
        int final_qty_required = 0, final_percentage = 0;
        String final_peso = "0", final_is_every = "0";

        cart_total_amount = cart_total_amount + total_per_item;

        if (map_all_products_promos.size() > 0 || specific_products_promos.size() > 0) {
            String final_free_delivery = "0", final_free_gifts = "0", final_min_purchase = "";
            ArrayList<String> all_promos = new ArrayList();

            if (map_all_products_promos.size() > 0) {
                final_qty_required = Integer.parseInt(map_all_products_promos.get("pr_qty_required"));
                final_free_delivery = map_all_products_promos.get("pr_free_delivery");
                final_percentage = Integer.parseInt(map_all_products_promos.get("pr_percentage"));
                final_peso = map_all_products_promos.get("pr_peso");
                final_min_purchase = map_all_products_promos.get("minimum_purchase");

            } else if (specific_products_promos.size() > 0) {
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
                        double percent_off = Double.parseDouble(String.valueOf(final_percentage / 100.0f));
                        double discounted_amount = 0;

                        if (final_is_every.equals("1")) {
                            int discount_times = cart_quantity / final_qty_required;
                            int left = cart_quantity - (discount_times * final_qty_required);
                            double total = 0;

                            for (int x = 0; x < discount_times; x++) {
                                discounted_amount = discounted_amount + ((price * final_qty_required) * percent_off);
                                double temp_total = (price * final_qty_required) - ((price * final_qty_required) * percent_off);
                                total = total + temp_total;
                            }
                            total = total + (price * left);
                            promo_total.setText("Php " + df.format(total));
                        } else {
                            discounted_amount = total_per_item * percent_off;
                            double discounted_total = total_per_item - discounted_amount;
                            promo_total.setText("Php " + df.format(discounted_total));
                        }
                        cart_total_amount = cart_total_amount - discounted_amount;
                    }
                }
            }

            if (!final_peso.equals("0")) {
                all_promos.add(final_peso + " Php off");

                if (final_qty_required > 0) {
                    if (cart_quantity >= final_qty_required) {
                        promo_total.setVisibility(View.VISIBLE);
                        total.setPaintFlags(total.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        double discounted_amount = 0;

                        if (final_is_every.equals("1")) {
                            int discount_times = cart_quantity / final_qty_required;
                            double discounted_total = 0;
                            int left = cart_quantity - (discount_times * final_qty_required);

                            for (int x = 0; x < discount_times; x++) {
                                double temp_total = (price * final_qty_required) - Double.parseDouble(final_peso);
                                discounted_total = discounted_total + temp_total;
                            }
                            discounted_total = discounted_total + (price * left);
                            promo_total.setText("Php " + discounted_total);
                            discounted_amount = total_per_item - discounted_total;
                        } else {
                            discounted_amount = Double.parseDouble(final_peso);
                            double discounted_total = total_per_item - Double.parseDouble(final_peso);
                            promo_total.setText("Php " + discounted_total);
                        }
                        cart_total_amount = cart_total_amount - discounted_amount;
                    }
                }
            }

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
        total.setText("Php " + total_per_item);
        product_quantity.setText(String.valueOf(cart_quantity));
        String plural = helpers.getPluralForm(objects.get(position).get("unit"), qty_per_packing);
        product_price.setText("Php " + price + "/" + objects.get(position).get("packing") + " (" + qty_per_packing + " " + plural + ")");
        cart_total = cart_total + total_per_item;

        delete.setOnClickListener(this);

        final View finalConvertView = convertView;
        final int final_qty_required1 = final_qty_required;
        final int final_percentage1 = final_percentage;
        final String final_peso1 = final_peso;
        final String final_is_every1 = final_is_every;
        final Object txt_promo_total = promo_total;
        final double percent_off = Double.parseDouble(String.valueOf(final_percentage1 / 100.0f));

        up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = (TextView) v.getTag();
                TextView txt_promo = (TextView) txt_promo_total;
                int lastQty = Integer.parseInt(txt.getText().toString());
                TextView p_total = (TextView) finalConvertView.findViewById(R.id.total);

                lastQty = lastQty + 1;

                if (lastQty <= available_qty) {
                    double total_per_item = price * lastQty;
                    txt.setText(lastQty + "");
                    cart_total = cart_total + price;


                    if (final_qty_required1 > 0) {
                        if (lastQty >= final_qty_required1) {
                            txt_promo.setVisibility(View.VISIBLE);
                            p_total.setPaintFlags(p_total.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                            if (final_is_every1.equals("1")) {

                            } else {
                                double temp_prod_discount = 0;

                                if (final_percentage1 > 0) {
                                    if (lastQty == final_qty_required1)
                                        cart_total = cart_total - (total_per_item * percent_off);

                                    temp_prod_discount = total_per_item - (total_per_item * percent_off);
                                } else if (!final_peso1.equals("0")) {
                                    if (lastQty == final_qty_required1)
                                        cart_total = cart_total - Double.parseDouble(final_peso1);

                                    temp_prod_discount = total_per_item - Double.parseDouble(final_peso1);
                                }
                                txt_promo.setText("Php " + df.format(temp_prod_discount));
                            }
                        }
                    }

                    p_total.setText("Php " + total_per_item);
                    ShoppingCartActivity.total_amount.setText("Php " + df.format(cart_total));

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
                TextView txt_promo = (TextView) txt_promo_total;

                int lastQty = Integer.parseInt(txt.getText().toString());
                lastQty = lastQty - 1;

                if (lastQty < 1) {
                    lastQty = 1;
                    ShoppingCartActivity.total_amount.setText("Php " + df.format(cart_total));
                } else {
                    cart_total = cart_total - price;

                    if (final_qty_required1 > 0) {
                        if (lastQty < final_qty_required1) {
                            txt_promo.setVisibility(View.GONE);
                            p_total.setPaintFlags(p_total.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

                            if (!final_peso1.equals("0")) {
                                if (lastQty == (final_qty_required1 - 1))
                                    cart_total = cart_total + Double.parseDouble(final_peso1);
                            } else if (final_percentage1 > 0) {
                                if (lastQty == (final_qty_required1 - 1)) {
                                    double prev_total = price * final_qty_required1;
                                    cart_total = cart_total + (prev_total * percent_off);
                                }
                            }
                        } else {
                            if (!final_peso1.equals("0")) {
                                double discounted_temp_total = (price * lastQty) - Double.parseDouble(final_peso1);
                                txt_promo.setText("Php " + discounted_temp_total);
                            }
                        }
                    }
                    ShoppingCartActivity.total_amount.setText("Php " + df.format(cart_total));
                }

                double total_per_item = price * lastQty;
                txt.setText(lastQty + "");
                p_total.setText("Php " + total_per_item);

                HashMap<String, String> temp = objects.get(position);
                temp.put("quantity", String.valueOf(lastQty));
                objects.set(position, temp);
            }
        });

        ShoppingCartActivity.total_amount.setText("Php " + df.format(cart_total_amount));

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
