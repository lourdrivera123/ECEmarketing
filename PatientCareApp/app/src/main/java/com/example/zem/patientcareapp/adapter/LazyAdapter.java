package com.example.zem.patientcareapp.adapter;

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
import com.example.zem.patientcareapp.Activities.ProductsActivity;
import com.example.zem.patientcareapp.Controllers.BasketController;
import com.example.zem.patientcareapp.Controllers.DbHelper;

import com.example.zem.patientcareapp.Controllers.OrderController;
import com.example.zem.patientcareapp.Controllers.OrderDetailController;
import com.example.zem.patientcareapp.Controllers.PatientController;
import com.example.zem.patientcareapp.Controllers.ProductController;
import com.example.zem.patientcareapp.Model.Basket;
import com.example.zem.patientcareapp.ConfigurationModule.Helpers;
import com.example.zem.patientcareapp.ImageHandlingModule.ImageLoader;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.SwipeTabsModule.MasterTabActivity;
import com.example.zem.patientcareapp.Network.PostRequest;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.Network.ServerRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
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
    ProductController pc;
    BasketController bc;
    PatientController ptc;
    Helpers helpers;

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

        pc = new ProductController(activity);
        bc = new BasketController(activity);
        ptc = new PatientController(activity);

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
            TextView specialty = (TextView) vi.findViewById(R.id.specialty); // artist name

            HashMap<String, String> doctor;
            doctor = data.get(position);

            // Setting all values in listview
            title.setText("Dr. " + doctor.get("fullname"));
            specialty.setText(doctor.get("name"));
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
                price = Double.parseDouble(basket_items.get(ProductController.PRODUCT_PRICE));
                quantity = Integer.parseInt(basket_items.get(BasketController.BASKET_QUANTITY));

                unit = basket_items.get(ProductController.PRODUCT_UNIT);
                basketId = Integer.parseInt(basket_items.get(BasketController.SERVER_BASKET_ID));
                packing = basket_items.get(ProductController.PRODUCT_PACKING);
                qtyPerPacking = Integer.parseInt(basket_items.get(ProductController.PRODUCT_QTY_PER_PACKING));
                isApproved = Integer.parseInt(basket_items.get(BasketController.BASKET_IS_APPROVED));

                // Setting all values in listview
                basketIsApproved.setText("");
                if (isApproved == 0)
                    basketIsApproved.setText("Original Prescription must be presented upon delivery/pickup");

                productName.setText(basket_items.get(ProductController.PRODUCT_NAME));
                total_amount = price * quantity;

                total.setText("\u20B1 " + total_amount + "");
                qty.setText(quantity + "");
                int num = quantity / qtyPerPacking;
                productPrice.setText("Quantity: " + quantity + "  " + helpers.getPluralForm(unit, quantity) + " (" + num + " " + helpers.getPluralForm(packing, num) + ")" +
                        "\nPrice: \u20B1 " + String.format("%.2f", price) + " / " + (!unit.equals("0") ? unit : ""));

                qty.setId(basketId);
                total.setId(Integer.parseInt(basket_items.get(BasketController.SERVER_BASKET_ID)));
                total.setTag(Integer.parseInt(basket_items.get(BasketController.SERVER_BASKET_ID)));
            } catch (Exception e) {
                Log.d("lazyAdapter4", "error");
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
            price = Double.parseDouble(basket_items.get(ProductController.PRODUCT_PRICE));
            quantity = Integer.parseInt(basket_items.get(BasketController.BASKET_QUANTITY));
            unit = basket_items.get(ProductController.PRODUCT_UNIT);
            qtyPerPacking = Integer.parseInt(basket_items.get(ProductController.PRODUCT_QTY_PER_PACKING));
            packing = basket_items.get(ProductController.PRODUCT_PACKING);
            isApproved = Integer.parseInt(basket_items.get(BasketController.BASKET_IS_APPROVED));

            total_amount = price * quantity;
            unit = !unit.equals("0") ? unit : "";
            packingCount = quantity / qtyPerPacking;

            itemIsApproved.setText("");
            if (isApproved == 0)
                itemIsApproved.setText("Waiting for approval...");

            itemName.setText(basket_items.get(ProductController.PRODUCT_NAME));
            itemAmount.setText("\u20B1 " + total_amount + "");
            itemDetails.setText("Price: \u20B1 " + price + " / " + unit);
            itemQuantity.setText("Quantity: " + quantity + " " + helpers.getPluralForm(unit, quantity) +
                    " (" + packingCount + " " + helpers.getPluralForm(packing, packingCount) + ")");

            itemName.setTag(Integer.parseInt(basket_items.get(BasketController.SERVER_BASKET_ID)));
            itemAmount.setTag(Integer.parseInt(basket_items.get(BasketController.SERVER_BASKET_ID)));

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
                Log.d("lazyAdapter5", "error");
            }

        }
        return vi;
    }
}
