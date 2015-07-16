package com.example.zem.patientcareapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * Created by Dexter B. on 5/4/2015.
 */
public class LazyAdapter extends BaseAdapter {
    public static int quantity = 0;
    public static double total_amount, price = 0;
    public static TextView qty;
    public static TextView total;

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    Helpers helpers;

    private String list_type;
    public ArrayList myItems = new ArrayList();

    TextView product_name, product_description, product_price, product_quantity;
    ImageButton btnAddQty;
    DbHelper dbHelper;


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


    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if (list_type.equals("list_of_doctors")) {
            vi = inflater.inflate(R.layout.list_row, null);

            TextView title = (TextView) vi.findViewById(R.id.title); // title
            TextView artist = (TextView) vi.findViewById(R.id.specialty); // artist name
            ImageView list_image = (ImageView) vi.findViewById(R.id.list_image); // thumb image

            HashMap<String, String> doctor;
            doctor = data.get(position);

            // Setting all values in listview
            title.setText(doctor.get(DbHelper.DOC_FULLNAME));
            artist.setText(doctor.get(DbHelper.DOC_SPECIALTY_NAME));
            imageLoader.DisplayImage(doctor.get(DbHelper.DOC_PHOTO), list_image);

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
            final int productId, productQtyPerPacking;


            HashMap<String, String> map;
            map = data.get(position);
            final Map<String, HashMap<String, String>> productQuantity = ProductsFragment.productQuantity;

            String fuckyou = map.get("id");

            productId = Integer.parseInt(fuckyou);
            productPacking = productQuantity.get(productId + "").get("packing");
            productUnit = map.get("unit");
            productQtyPerPacking = !productQuantity.get(productId + "").get("qty_per_packing").equals("null") ? Integer.parseInt(productQuantity.get(productId + "").get("qty_per_packing")) : 1;

            product_quantity.setText(productQtyPerPacking + "");

            int PID = dbHelper.getProductServerIdById(productId);

            product_quantity.setId(PID);
            product_description.setTag(PID);

            final TextView tv_new_product_quantity = (TextView) vi.findViewById(PID);
            final TextView tv_new_product_description = (TextView) vi.findViewWithTag(PID);

            btnAddQty.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NewApi")
                @Override
                public void onClick(View v) {

                    int qty = Integer.parseInt(productQuantity.get(productId + "").get("temp_basket_qty"));
                    int productQtyPerPacking = !productQuantity.get(productId + "").get("qty_per_packing").equals("null") ?
                            Integer.parseInt(productQuantity.get(productId + "").get("qty_per_packing")) : 1;

                    qty += productQtyPerPacking;
                    tv_new_product_quantity.setText(qty + "");

                    productQuantity.get(productId + "").put("temp_basket_qty", qty + "");
                    double totalPacking = (qty / productQtyPerPacking);
                    String fiProductPacking = totalPacking > 1 ? helpers.getPluralForm(productPacking) : productPacking;
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

                    qty -= productQtyPerPacking;
                    if (qty < 1) {
                        qty = productQtyPerPacking;
                    }
                    productQuantity.get(productId + "").put("temp_basket_qty", qty + "");
                    tv_new_product_quantity.setText(qty + "");
                    double totalPacking = (qty / productQtyPerPacking);
                    String fiProductPacking = totalPacking > 1 ? helpers.getPluralForm(productPacking) : productPacking;
                    tv_new_product_description.setText("1 " + productUnit + " x " + qty + "(" + totalPacking + " " + fiProductPacking + ")");

                    ProductsFragment.productQuantity = productQuantity;
                }
            });


            // Setting all values in listview
            vi.setTag(PID);
            product_name.setText(map.get(DbHelper.PRODUCT_NAME));
            product_description.setText("1 " + productUnit + " x " + productQtyPerPacking + "(1.0 " + productPacking + ")");
            product_price.setText("\u20B1 " + map.get(DbHelper.PRODUCT_PRICE));

            final View tempVi = vi;
            final HashMap<String, String> tempMap = map;
            final int tempPID = PID;
            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helpers helpers = new Helpers();
                    final DbHelper dbhelper = new DbHelper(activity);
                    final ServerRequest serverRequest = new ServerRequest();
//                    int get_productID = Integer.parseInt(tempMap.get(DbHelper.PRODUCT_ID));
                    int get_productID = tempPID;
                    ProgressDialog pDialog = new ProgressDialog(activity);
                    pDialog.setMessage("Please wait...");

                    if (helpers.isNetworkAvailable(activity)) {
                        try {
                            double new_qty;
                            new_qty = Double.parseDouble(product_quantity.getText().toString());

                        /* let's check if the product already exists in our basket */
                            final Basket basket = dbhelper.getBasket(get_productID);

                            if (basket.getBasketId() > 0) {
                                HashMap<String, String> hashMap = new HashMap();
                                hashMap.put("id", String.valueOf(get_productID));

                                hashMap.put("patient_id", String.valueOf(dbhelper.getCurrentLoggedInPatient().getServerID()));
                                hashMap.put("table", "baskets");
                                hashMap.put("request", "crud");

                                double old_qty = basket.getQuantity();
                                basket.setQuantity(new_qty + old_qty);
                                hashMap.put("quantity", String.valueOf(basket.getQuantity()));
                                hashMap.put("action", "update");
                                hashMap.put("id", String.valueOf(basket.getBasketId()));

                                serverRequest.init(activity, hashMap, "insert_basket");
                                serverRequest.setProgressDialog(pDialog);

                                tempVi.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        boolean responseFromServer = serverRequest.getResponse();
                                        if (responseFromServer) {
                                            if (dbhelper.updateBasket(basket)) {
                                                Toast.makeText(activity, "Your cart has been updated.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(activity, "Sorry, we can't update your cart this time.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(activity, "Sorry, we can't update your cart this time.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, 2500);
                            } else {
                            /* since, we can't find the product in baskets table, let's insert a new one */
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                hashMap.put("product_id", String.valueOf(get_productID));
                                hashMap.put("quantity", String.valueOf(new_qty));
                                hashMap.put("patient_id", String.valueOf(dbhelper.getCurrentLoggedInPatient().getServerID()));
                                hashMap.put("table", "baskets");
                                hashMap.put("request", "crud");
                                hashMap.put("action", "insert");

                                serverRequest.setProgressDialog(pDialog);

                                serverRequest.setSuccessMessage("New item has been added to your cart!");
                                serverRequest.setErrorMessage("Sorry, we can't add to your cart this time.");
                                serverRequest.init(activity, hashMap, "insert_basket");
                            }
                        } catch (Exception e) {
                        }
                    } else {
                        Toast.makeText(activity, "Sorry, please connect to the internet.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else if (list_type.equals("basket_items")) {
            try {
                vi = inflater.inflate(R.layout.list_row_basket_items, null);

                HashMap<String, String> basket_items;

                basket_items = data.get(position);

                TextView productName = (TextView) vi.findViewById(R.id.product_name);
                TextView productPrice = (TextView) vi.findViewById(R.id.product_price);
                qty = (TextView) vi.findViewById(R.id.product_quantity);
                total = (TextView) vi.findViewById(R.id.total);

                // Do the fucking stuffs here
                price = Double.parseDouble(basket_items.get(DbHelper.PRODUCT_PRICE));
                quantity = Integer.parseInt(basket_items.get(DbHelper.BASKET_QUANTITY));
                String unit = basket_items.get(DbHelper.PRODUCT_UNIT);

                // Setting all values in listview
                productName.setText(basket_items.get(DbHelper.PRODUCT_NAME));
                total_amount = price * quantity;

                total.setText(total_amount + "");
                qty.setText(quantity + "");
                productPrice.setText("Quantity: " + quantity + "\nPrice: \u20B1 " + price + " / " + (!unit.equals("0") ? unit : ""));

                qty.setId(Integer.parseInt(basket_items.get(DbHelper.SERVER_BASKET_ID)));
                total.setId(Integer.parseInt(basket_items.get(DbHelper.SERVER_BASKET_ID)));
            } catch (Exception e) {
                System.out.println("FUCKING ERROR on LazyAdapter@basket_items" + e.getMessage());
                e.printStackTrace();
            }

        } else if (list_type.equals("ready_for_checkout_items")) {
            vi = inflater.inflate(R.layout.checkout_list_item, null);

            HashMap<String, String> basket_items;

            basket_items = data.get(position);

            TextView itemName = (TextView) vi.findViewById(R.id.item_name);
            TextView itemDetails = (TextView) vi.findViewById(R.id.item_details);
            TextView itemAmount = (TextView) vi.findViewById(R.id.item_amount);
            TextView itemQuantity = (TextView) vi.findViewById(R.id.item_quantity);

            // Do the fucking stuffs here
            double price = Double.parseDouble(basket_items.get(DbHelper.PRODUCT_PRICE));
            int quantity = Integer.parseInt(basket_items.get(DbHelper.BASKET_QUANTITY));

            // Setting all values in listview
            itemName.setText(basket_items.get(DbHelper.PRODUCT_NAME));
            double total_amount = price * quantity;
            String unit = basket_items.get(DbHelper.PRODUCT_UNIT);


            itemAmount.setText("\u20B1 " + total_amount + "");
            itemDetails.setText("Price: \u20B1 " + price + " / " + (!unit.equals("0") ? unit : ""));
            itemQuantity.setText("Quantity: " + quantity);

            itemName.setTag(Integer.parseInt(basket_items.get(DbHelper.SERVER_BASKET_ID)));
            itemAmount.setTag(Integer.parseInt(basket_items.get(DbHelper.SERVER_BASKET_ID)));

        } else if(list_type.equals("promo_items")){
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
            less.setText("HURRY!");
            if(promo_item.get("max_discount") != null){
                lessAmount.setText("Up to "+promo_item.get("min_discount")+" - "+promo_item.get("max_discount")+"% DISCOUNT");
            } else {
                lessAmount.setText("Free items available on selected products.");
            }

            try{
                Date dateStart = fmt.parse(promo_item.get("start_date"));
                Date dateEnd = fmt.parse(promo_item.get("end_date"));
                duration.setText("Starting "+df.format(dateStart) +" to "+df.format(dateEnd) );
            }catch(Exception e){
                System.out.println("Oh snap! We got some error <source: LazyAdapter@promo_items."+e.getMessage());
            }
        }

        return vi;
    }
}
