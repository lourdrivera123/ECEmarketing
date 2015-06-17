package com.example.zem.patientcareapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;


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

    private String list_type;
    public ArrayList myItems = new ArrayList();


    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d, String listType) {
        list_type = listType;
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());

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

            HashMap<String, String> doctor = new HashMap<String, String>();
            doctor = data.get(position);

            // Setting all values in listview
            title.setText(doctor.get(DbHelper.DOC_FULLNAME));
            artist.setText(doctor.get(DbHelper.DOC_SPECIALTY_NAME));
            imageLoader.DisplayImage(doctor.get(DbHelper.DOC_PHOTO), list_image);

        } else if (list_type.equals("product_lists")) {
            vi = inflater.inflate(R.layout.list_row_products, null);
            TextView product_name = (TextView) vi.findViewById(R.id.product_name); // product name
            TextView product_description = (TextView) vi.findViewById(R.id.product_description); // product description
            TextView product_price = (TextView) vi.findViewById(R.id.product_price); // product price
            final TextView btnAddQty = (TextView) vi.findViewById(R.id.add_qty);
            TextView btnMinusQty = (TextView) vi.findViewById(R.id.minus_qty);

            btnAddQty.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NewApi")
                @Override
                public void onClick(View v) {
                    btnAddQty.setBackground(activity.getResources().getDrawable( R.mipmap.ic_plus_dead ));
                    btnAddQty.setBackground(activity.getResources().getDrawable(R.mipmap.ic_plus));
                }
            });


            HashMap<String, String> map;
            map = data.get(position);

            // Setting all values in listview
            vi.setTag(map.get(DbHelper.PRODUCT_ID));
            product_name.setText(map.get(DbHelper.PRODUCT_NAME));
            product_description.setText(map.get(DbHelper.PRODUCT_DESCRIPTION));
            product_price.setText("\u20B1 "+map.get(DbHelper.PRODUCT_PRICE));

        } else if (list_type.equals("consultation_lists")) {
            vi = inflater.inflate(R.layout.list_row_consultations, null);

            TextView doctor = (TextView) vi.findViewById(R.id.doctor_name);
            TextView clinic_address = (TextView) vi.findViewById(R.id.clinic_address);
            TextView consultation_schedule = (TextView) vi.findViewById(R.id.consultation_schedule);

            HashMap<String, String> schedule;
            schedule = data.get(position);

            // Setting all values in listview
            doctor.setText(schedule.get(PatientConsultationFragment.KEY_DOCTOR_NAME));
            clinic_address.setText(schedule.get(PatientConsultationFragment.KEY_CLINIC_ADDRESS));

            String sched = schedule.get(PatientConsultationFragment.KEY_DATE) + ", " +
                    (schedule.get(PatientConsultationFragment.KEY_SCHEDULE).equals("AM") ? "Morning" : "Afternoon");

            consultation_schedule.setText(sched);

        } else if (list_type.equals("basket_items")) {
            vi = inflater.inflate(R.layout.list_row_basket_items, null);

            HashMap<String, String> basket_items;

            basket_items = data.get(position);

            TextView product_name = (TextView) vi.findViewById(R.id.product_name);
            TextView productPrice = (TextView) vi.findViewById(R.id.product_price);
            qty = (TextView) vi.findViewById(R.id.product_quantity);
            total = (TextView) vi.findViewById(R.id.total);

            // Do the fucking stuffs here
            price = Double.parseDouble(basket_items.get(DbHelper.PRODUCT_PRICE));
            quantity = Integer.parseInt(basket_items.get(DbHelper.BASKET_QUANTITY));
            String unit = basket_items.get(DbHelper.PRODUCT_UNIT);
            // Setting all values in listview
            product_name.setText(basket_items.get(DbHelper.PRODUCT_NAME));
            total_amount = price * quantity;

            total.setText(total_amount + "");
            qty.setText(quantity + "");
            productPrice.setText("Quantity: "+quantity+"\nPrice: \u20B1 " + price+" / "+(!unit.equals("0") ? unit : ""));

            qty.setId(Integer.parseInt(basket_items.get(DbHelper.SERVER_BASKET_ID)));
            total.setId(Integer.parseInt(basket_items.get(DbHelper.SERVER_BASKET_ID)));
        } else if (list_type.equals("ready_for_checkout_items")){
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



            itemAmount.setText("\u20B1 "+total_amount + "");
            itemDetails.setText("Price: \u20B1 "+price+" / "+(!unit.equals("0") ? unit : ""));
            itemQuantity.setText("Quantity: "+quantity);

            itemName.setTag(Integer.parseInt(basket_items.get(DbHelper.SERVER_BASKET_ID)));
            itemAmount.setTag(Integer.parseInt(basket_items.get(DbHelper.SERVER_BASKET_ID)));
        }
        return vi;
    }
}
