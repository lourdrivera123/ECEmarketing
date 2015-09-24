package com.example.zem.patientcareapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.Fragment.ProductsFragment;
import com.example.zem.patientcareapp.GetterSetter.Basket;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.PostRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dexter B. on 5/4/2015.
 */
public class LazyAdapter extends BaseAdapter {
    public static int quantity = 0;
    public static double total_amount, price = 0;
    public static TextView qty;
    public static TextView total;
    TextView product_name, product_description, product_price, product_quantity;
    ImageButton btnAddQty;

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    DbHelper dbHelper;
    Helpers helpers;
    int basket_id = 0;

    int productQty, prescriptionId = 0;

    private String list_type;

    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d, String listType) {

        list_type = listType;
        activity = a;
        dbHelper = new DbHelper(activity);
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
        helpers = new Helpers();

        notifyDataSetChanged();
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, final View convertView, ViewGroup parent) {
        View vi = convertView;

        if (list_type.equals("list_of_doctors")) {
            vi = inflater.inflate(R.layout.list_row, null);

            TextView title = (TextView) vi.findViewById(R.id.title); // title
            TextView artist = (TextView) vi.findViewById(R.id.specialty); // artist name

            HashMap<String, String> doctor;
            doctor = data.get(position);

            // Setting all values in listview
            title.setText("Dr. " + doctor.get(DbHelper.DOC_FULLNAME));
            artist.setText(doctor.get(DbHelper.DOC_SPECIALTY_NAME));

        } else if (list_type.equals("product_lists")) {
            vi = inflater.inflate(R.layout.list_row_products, null);

            product_name = (TextView) vi.findViewById(R.id.product_name); // product name
            product_description = (TextView) vi.findViewById(R.id.product_description); // product description
            product_price = (TextView) vi.findViewById(R.id.product_price); // product price
            product_quantity = (TextView) vi.findViewById(R.id.basket_quantity);
            btnAddQty = (ImageButton) vi.findViewById(R.id.add_qty);

            ImageButton btnMinusQty = (ImageButton) vi.findViewById(R.id.minus_qty);
            Button addToCart = (Button) vi.findViewById(R.id.add_to_cart_btn);

            final String productPacking;
            final String productUnit;
            final int productId, productQtyPerPacking, isPrescriptionRequired;

            HashMap<String, String> map;
            map = data.get(position);
            final Map<String, HashMap<String, String>> productQuantity = ProductsFragment.productQuantity;

            productId = Integer.parseInt(map.get("id"));
            productPacking = productQuantity.get(productId + "").get("packing");
            productUnit = map.get("unit");
            productQtyPerPacking = !productQuantity.get(productId + "").get("qty_per_packing").equals("null") ? Integer.parseInt(productQuantity.get(productId + "").get("qty_per_packing")) : 1;
            isPrescriptionRequired = Integer.parseInt(map.get("prescription_required"));

            product_quantity.setText(productQtyPerPacking + "");

            int PID = dbHelper.getProductServerIdById(productId);

            product_quantity.setId(PID);
            product_description.setTag(PID);

            productQty = productQty > 0 ? productQty : productQtyPerPacking;

            final TextView tv_new_product_quantity = (TextView) vi.findViewById(PID);
            final TextView tv_new_product_description = (TextView) vi.findViewWithTag(PID);

            btnAddQty.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NewApi")
                @Override
                public void onClick(View v) {
                    int qty = Integer.parseInt(productQuantity.get(productId + "").get("temp_basket_qty"));
                    int productQtyPerPacking = !productQuantity.get(productId + "").get("qty_per_packing").equals("null") ?
                            Integer.parseInt(productQuantity.get(productId + "").get("qty_per_packing")) : 1;

                    if (qty == 0) {
                        qty = productQtyPerPacking;
                    }

                    qty += productQtyPerPacking;
                    tv_new_product_quantity.setText(qty + "");

                    productQty = qty;

                    productQuantity.get(productId + "").put("temp_basket_qty", qty + "");
                    int totalPacking = (qty / productQtyPerPacking);
                    String fiProductPacking = helpers.getPluralForm(productPacking, totalPacking);
                    tv_new_product_description.setText("1 " + productUnit + " x " + qty + "(" + totalPacking + " " + fiProductPacking + ")");

                    ProductsFragment.productQuantity = productQuantity;
                }
            });

            btnMinusQty.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NewApi")
                @Override
                public void onClick(View v) {
                    int qty = Integer.parseInt(productQuantity.get(productId + "").get("temp_basket_qty"));
                    int productQtyPerPacking = !productQuantity.get(productId + "").get("qty_per_packing").equals("null") ?
                            Integer.parseInt(productQuantity.get(productId + "").get("qty_per_packing")) : 1;

                    if (qty == 0) {  // check if temp qty is empty
                        qty = productQtyPerPacking;
                    }

                    qty -= productQtyPerPacking;
                    if (qty < 1) {
                        qty = productQtyPerPacking;
                    }

                    productQty = qty;

                    productQuantity.get(productId + "").put("temp_basket_qty", qty + "");
                    tv_new_product_quantity.setText(qty + "");
                    int totalPacking = (qty / productQtyPerPacking);
                    String fiProductPacking = helpers.getPluralForm(productPacking, totalPacking);
                    tv_new_product_description.setText("1 " + productUnit + " x " + qty + "(" + totalPacking + " " + fiProductPacking + ")");

                    ProductsFragment.productQuantity = productQuantity;
                }
            });

            // Setting all values in listview
            vi.setTag(PID);
            product_name.setText(map.get(DbHelper.PRODUCT_NAME));
            product_description.setText("1 " + productUnit + " x " + productQtyPerPacking + "(1 " + productPacking + ")");
            product_price.setText("\u20B1 " + map.get(DbHelper.PRODUCT_PRICE));

            final int tempPID = PID;

            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Helpers helpers = new Helpers();
                    final DbHelper dbhelper = new DbHelper(activity);
                    final ServerRequest serverRequest = new ServerRequest();

                    try {
                        int q = Integer.parseInt(productQuantity.get(productId + "").get("temp_basket_qty"));
                        int p = Integer.parseInt(productQuantity.get(productId + "").get("qty_per_packing"));
                        productQty = q != 0 ? q : p;
                        final Basket basket = dbhelper.getBasket(tempPID);

                        final ProgressDialog pdialog = new ProgressDialog(activity);
                        pdialog.setCancelable(false);
                        pdialog.setMessage("Please wait...");
                        pdialog.show();

                        if (basket.getBasketId() > 0) { //EXISTING ITEM IN YOUR BASKET (UPDATE ONLY)
                            HashMap<String, String> hashMap = new HashMap();
                            hashMap.put("id", String.valueOf(tempPID));

                            hashMap.put("patient_id", String.valueOf(dbhelper.getCurrentLoggedInPatient().getServerID()));
                            hashMap.put("table", "baskets");
                            hashMap.put("request", "crud");

                            int old_qty = basket.getQuantity();
                            basket.setQuantity(productQty + old_qty);
                            hashMap.put("quantity", String.valueOf(basket.getQuantity()));
                            hashMap.put("action", "update");
                            hashMap.put("id", String.valueOf(basket.getBasketId()));

                            PostRequest.send(activity, hashMap, serverRequest, new RespondListener<JSONObject>() {
                                @Override
                                public void getResult(JSONObject response) {
                                    System.out.print("response using interface <LazyAdapter.java>" + response);
                                    int success = 0;

                                    try {
                                        success = response.getInt("success");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    if (success == 1) {
                                        if (dbhelper.updateBasket(basket)) {
                                            Toast.makeText(activity, "Your cart has been updated.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(activity, "Sorry, we can't update your cart this time.", Toast.LENGTH_SHORT).show();
                                            Log.d("error on dbhelper", "error");
                                        }
                                    } else {
                                        Toast.makeText(activity, "Server error occurred", Toast.LENGTH_SHORT).show();
                                        System.out.print("src: <LazyAdapter - product_lists>");
                                    }
                                    pdialog.dismiss();
                                }
                            }, new ErrorListener<VolleyError>() {
                                public void getError(VolleyError error) {
                                    pdialog.dismiss();
                                    System.out.print("src: <LazyAdapter - product_lists>: " + error.toString());
                                    Toast.makeText(activity, "Couldn't delete item. Please check your Internet connection", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else { //INSERT NEW PRODUCT IN BASKETS TABLE//
                            final HashMap<String, String> hashMap = new HashMap();
                            hashMap.put("product_id", String.valueOf(tempPID));
                            hashMap.put("quantity", String.valueOf(productQty));
                            hashMap.put("patient_id", String.valueOf(dbhelper.getCurrentLoggedInPatient().getServerID()));
                            hashMap.put("prescription_id", "0");
                            hashMap.put("is_approved", "1");
                            hashMap.put("table", "baskets");
                            hashMap.put("request", "crud");
                            hashMap.put("action", "insert");

                            // if prescription required, ask for the prescription
                            if (isPrescriptionRequired == 1) {
                                GridView gridView;
                                final Dialog builder;
                                HashMap<GridView, Dialog> map;
                                map = helpers.showPrescriptionDialog(activity);

                                if (map.size() > 0) {
                                    Map.Entry<GridView, Dialog> entry = map.entrySet().iterator().next();
                                    gridView = entry.getKey();
                                    builder = entry.getValue();

                                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            prescriptionId = (int) id;
                                            hashMap.put("prescription_id", prescriptionId + "");
                                            hashMap.put("is_approved", "0");

                                            serverRequest.setErrorMessage("Sorry, we can't update your cart this time.");

                                            PostRequest.send(activity, hashMap, serverRequest, new RespondListener<JSONObject>() {
                                                @Override
                                                public void getResult(JSONObject response) {
                                                    pdialog.dismiss();
                                                    Toast.makeText(activity, "New item has been added to your cart", Toast.LENGTH_SHORT).show();
                                                }
                                            }, new ErrorListener<VolleyError>() {
                                                public void getError(VolleyError error) {
                                                    pdialog.dismiss();
                                                    Toast.makeText(activity, "Couldn't delete item. Please check your Internet connection", Toast.LENGTH_LONG).show();
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
                                } else {
                                    pdialog.dismiss();
//                                    Toast.makeText(activity, "Please upload prescription", Toast.LENGTH_SHORT).show();
                                    AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(activity);
                                    confirmationDialog.setTitle("Attention!");
                                    confirmationDialog.setMessage("This product requires you to upload a prescription, do you wish to continue ?");
                                    confirmationDialog.setNegativeButton("No", null);
                                    confirmationDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent mastertab_activity_intent = new Intent(activity, MasterTabActivity.class);
                                            mastertab_activity_intent.putExtra("selected", 2);
                                            activity.startActivity(mastertab_activity_intent);
                                        }
                                    });
                                    confirmationDialog.show();
                                }
                            } else {
                                serverRequest.setErrorMessage("Sorry, we can't update your cart this time.");

                                PostRequest.send(activity, hashMap, serverRequest, new RespondListener<JSONObject>() {
                                    @Override
                                    public void getResult(JSONObject response) {
                                        pdialog.dismiss();
                                        Toast.makeText(activity, "New item has been added to your cart", Toast.LENGTH_SHORT).show();
                                    }
                                }, new ErrorListener<VolleyError>() {
                                    public void getError(VolleyError error) {
                                        pdialog.dismiss();
                                        System.out.print("<LazyAdapter, product_lists>" + error.toString());
                                        Toast.makeText(activity, "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } else if (list_type.equals("basket_items")) {
            try {
                vi = inflater.inflate(R.layout.list_row_basket_items, null);

                HashMap<String, String> basket_items;
                basket_items = data.get(position);

                TextView productName, productPrice, basketIsApproved;
                String unit, packing;
                int basketId, qtyPerPacking, isApproved;

                productName = (TextView) vi.findViewById(R.id.product_name);
                productPrice = (TextView) vi.findViewById(R.id.product_price);
                basketIsApproved = (TextView) vi.findViewById(R.id.is_approved);
                qty = (TextView) vi.findViewById(R.id.product_quantity);
                total = (TextView) vi.findViewById(R.id.total);

                // Do stuffs here
                price = Double.parseDouble(basket_items.get(DbHelper.PRODUCT_PRICE));
                quantity = Integer.parseInt(basket_items.get(DbHelper.BASKET_QUANTITY));

                unit = basket_items.get(DbHelper.PRODUCT_UNIT);
                basketId = Integer.parseInt(basket_items.get(DbHelper.SERVER_BASKET_ID));
                packing = basket_items.get(DbHelper.PRODUCT_PACKING);
                qtyPerPacking = Integer.parseInt(basket_items.get(DbHelper.PRODUCT_QTY_PER_PACKING));
                isApproved = Integer.parseInt(basket_items.get(DbHelper.BASKET_IS_APPROVED));

                // Setting all values in listview
                basketIsApproved.setText("");
                if (isApproved == 0)
                    basketIsApproved.setText("Waiting for approval...");

                productName.setText(basket_items.get(DbHelper.PRODUCT_NAME));
                total_amount = price * quantity;

                total.setText("\u20B1 " + total_amount + "");
                qty.setText(quantity + "");
                int num = quantity / qtyPerPacking;
                productPrice.setText("Quantity: " + quantity + "  " + helpers.getPluralForm(unit, quantity) + " (" + num + " " + helpers.getPluralForm(packing, num) + ")" +
                        "\nPrice: \u20B1 " + String.format("%.2f", price) + " / " + (!unit.equals("0") ? unit : ""));

                qty.setId(basketId);
                total.setId(Integer.parseInt(basket_items.get(DbHelper.SERVER_BASKET_ID)));
                total.setTag(Integer.parseInt(basket_items.get(DbHelper.SERVER_BASKET_ID)));
            } catch (Exception e) {
                System.out.println("error on LazyAdapter@basket_items" + e.getMessage());
                e.printStackTrace();
            }

        } else if (list_type.equals("ready_for_checkout_items")) {
            vi = inflater.inflate(R.layout.checkout_list_item, null);

            HashMap<String, String> basket_items;
            basket_items = data.get(position);

            TextView itemName, itemDetails, itemAmount, itemQuantity, itemIsApproved;

            itemName = (TextView) vi.findViewById(R.id.item_name);
            itemDetails = (TextView) vi.findViewById(R.id.item_details);
            itemAmount = (TextView) vi.findViewById(R.id.item_amount);
            itemQuantity = (TextView) vi.findViewById(R.id.item_quantity);
            itemIsApproved = (TextView) vi.findViewById(R.id.item_is_approved);

            // Do the fucking stuffs here
            double price, total_amount;
            int quantity, qtyPerPacking, packingCount, isApproved;
            String unit, packing;

            // Setting all values in listview
            price = Double.parseDouble(basket_items.get(DbHelper.PRODUCT_PRICE));
            quantity = Integer.parseInt(basket_items.get(DbHelper.BASKET_QUANTITY));
            unit = basket_items.get(DbHelper.PRODUCT_UNIT);
            qtyPerPacking = Integer.parseInt(basket_items.get(DbHelper.PRODUCT_QTY_PER_PACKING));
            packing = basket_items.get(DbHelper.PRODUCT_PACKING);
            isApproved = Integer.parseInt(basket_items.get(DbHelper.BASKET_IS_APPROVED));

            total_amount = price * quantity;
            unit = !unit.equals("0") ? unit : "";
            packingCount = quantity / qtyPerPacking;

            itemIsApproved.setText("");
            if (isApproved == 0)
                itemIsApproved.setText("Waiting for approval...");

            itemName.setText(basket_items.get(DbHelper.PRODUCT_NAME));
            itemAmount.setText("\u20B1 " + total_amount + "");
            itemDetails.setText("Price: \u20B1 " + price + " / " + unit);
            itemQuantity.setText("Quantity: " + quantity + " " + helpers.getPluralForm(unit, quantity) +
                    " (" + packingCount + " " + helpers.getPluralForm(packing, packingCount) + ")");

            itemName.setTag(Integer.parseInt(basket_items.get(DbHelper.SERVER_BASKET_ID)));
            itemAmount.setTag(Integer.parseInt(basket_items.get(DbHelper.SERVER_BASKET_ID)));

        } else if (list_type.equals("promo_items")) {
            vi = inflater.inflate(R.layout.list_row_promo_item, null);
            TextView promoName, less, lessAmount, duration;
            promoName = (TextView) vi.findViewById(R.id.promo_name);
            less = (TextView) vi.findViewById(R.id.less);
            lessAmount = (TextView) vi.findViewById(R.id.less_amount);
            duration = (TextView) vi.findViewById(R.id.duration);

            HashMap<String, String> promo_item;

            promo_item = data.get(position);
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

            SimpleDateFormat df = new SimpleDateFormat("MMMMM d, yyyy");

            promoName.setText(promo_item.get("promo_name"));
            less.setText("LESS:");
            if (promo_item.get("max_discount") != null)
                lessAmount.setText("Upto " + promo_item.get("min_discount") + " - " + promo_item.get("max_discount") + "% DISCOUNT");
            else
                lessAmount.setText("Free items available on selected products.");

            try {
                Date dateStart = fmt.parse(promo_item.get("start_date"));
                Date dateEnd = fmt.parse(promo_item.get("end_date"));
                duration.setText("Starting " + df.format(dateStart) + " to " + df.format(dateEnd));
            } catch (Exception e) {
                System.out.println("<source: LazyAdapter@promo_items." + e.getMessage());
            }
        }
        return vi;
    }
}
