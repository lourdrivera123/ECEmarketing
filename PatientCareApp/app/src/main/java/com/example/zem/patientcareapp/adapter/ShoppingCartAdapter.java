package com.example.zem.patientcareapp.adapter;

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
    TextView productName, product_quantity, product_price, total;

    public static double cart_total = 0;

    public ShoppingCartAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.objects = objects;

        helpers = new Helpers();
        dbHelper = new DbHelper(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_shopping_cart, parent, false);

            prod_image = (ImageView) convertView.findViewById(R.id.prod_image);
            productName = (TextView) convertView.findViewById(R.id.productName);
            product_price = (TextView) convertView.findViewById(R.id.product_price);
            product_quantity = (TextView) convertView.findViewById(R.id.product_quantity);
            total = (TextView) convertView.findViewById(R.id.total);
            delete = (ImageButton) convertView.findViewById(R.id.delete);
            up_btn = (ImageButton) convertView.findViewById(R.id.up_btn);
            down_btn = (ImageButton) convertView.findViewById(R.id.down_btn);
            root = (LinearLayout) convertView.findViewById(R.id.root);

            delete.setTag(position);
            up_btn.setTag(product_quantity);
            down_btn.setTag(product_quantity);
        }

        Log.d("objects", objects.get(position) + "");
        Log.d("all_promos", ShoppingCartActivity.all_promos_map + "");

        final double price = Double.parseDouble(objects.get(position).get("price"));
        int qty_per_packing = Integer.parseInt(objects.get(position).get("qty_per_packing"));
        double total_per_item = price * Integer.parseInt(objects.get(position).get("quantity"));

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ambrolex_family);
        final int shadowSize = context.getResources().getDimensionPixelSize(R.dimen.shadow_size);
        final int shadowColor = context.getResources().getColor(R.color.shadow_color);
        prod_image.setImageDrawable(new RoundedAvatarDrawable(icon, shadowSize, shadowColor));
        prod_image.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        productName.setText(objects.get(position).get("name"));
        total.setText("Php " + total_per_item);
        product_quantity.setText(objects.get(position).get("quantity") + "");
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
                double total_per_item = price * lastQty;

                txt.setText(lastQty + "");
                p_total.setText("Php " + total_per_item);
                cart_total = cart_total + price;
                ShoppingCartActivity.total_amount.setText("Php " + cart_total);

                HashMap<String, String> temp = objects.get(position);
                temp.put("quantity", String.valueOf(lastQty));
                objects.set(position, temp);
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
    public void onClick(View v) {
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
                                        Snackbar.make(root, "Item has been deleted", Snackbar.LENGTH_SHORT).show();
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
                                Snackbar.make(root, "Please check your Internet connection", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                confirmationDialog.show();
                break;
        }
    }
}
