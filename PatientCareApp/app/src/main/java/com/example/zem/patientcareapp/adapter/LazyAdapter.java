package com.example.zem.patientcareapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.zem.patientcareapp.Controllers.PatientController;
import com.example.zem.patientcareapp.Controllers.ProductController;
import com.example.zem.patientcareapp.ConfigurationModule.Helpers;
import com.example.zem.patientcareapp.ImageHandlingModule.ImageLoader;
import com.example.zem.patientcareapp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dexter B. on 5/4/2015.
 */

public class LazyAdapter extends BaseAdapter {
    public static int quantity = 0;
    public static double price = 0;

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    ProductController pc;
    PatientController ptc;
    Helpers helpers;

    private String list_type;

    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d, String listType) {
        list_type = listType;
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
        helpers = new Helpers();

        pc = new ProductController(activity);
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
//            vi = inflater.inflate(R.layout.list_row, null);

            TextView title = (TextView) vi.findViewById(R.id.title); // title
            TextView specialty = (TextView) vi.findViewById(R.id.specialty); // artist name

            HashMap<String, String> doctor;
            doctor = data.get(position);

            // Setting all values in listview
            title.setText(doctor.get("fullname"));
            specialty.setText(doctor.get("name"));
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
            price = Double.parseDouble(basket_items.get("price"));
            quantity = Integer.parseInt(basket_items.get("quantity"));
            unit = basket_items.get(ProductController.PRODUCT_UNIT);
            qtyPerPacking = Integer.parseInt(basket_items.get(ProductController.PRODUCT_QTY_PER_PACKING));
            packing = basket_items.get(ProductController.PRODUCT_PACKING);
            isApproved = Integer.parseInt(basket_items.get("is_approved"));

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

            itemName.setTag(Integer.parseInt(basket_items.get("basket_id")));
            itemAmount.setTag(Integer.parseInt(basket_items.get("basket_id")));
        }

        return vi;
    }
}
