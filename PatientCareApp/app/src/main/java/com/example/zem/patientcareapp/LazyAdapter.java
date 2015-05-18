package com.example.zem.patientcareapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Dexter B. on 5/4/2015.
 */
public class LazyAdapter extends BaseAdapter {
    public static int quantity = 0;
    public static double total_amount, price = 0;
    public static EditText qty;
    public static TextView total;

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;

    private String list_type;
    public ArrayList myItems = new ArrayList();


    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d, String lt) {
        list_type = lt;
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

        if (list_type == "list_of_doctors") {
            vi = inflater.inflate(R.layout.list_row, null);

            TextView title = (TextView) vi.findViewById(R.id.title); // title
            TextView artist = (TextView) vi.findViewById(R.id.specialty); // artist name
            ImageView list_image = (ImageView) vi.findViewById(R.id.list_image); // thumb image


            HashMap<String, String> doctor = new HashMap<String, String>();
            doctor = data.get(position);

            // Setting all values in listview
            title.setText(doctor.get(ListOfDoctorsFragment.KEY_FULL_NAME));
            artist.setText(doctor.get(ListOfDoctorsFragment.KEY_SPECIALTY));
            imageLoader.DisplayImage(doctor.get(ListOfDoctorsFragment.KEY_PHOTO), list_image);

        } else if (list_type == "product_lists") {
            vi = inflater.inflate(R.layout.list_row_products, null);
            TextView product_name = (TextView) vi.findViewById(R.id.product_name); // product name
            TextView product_description = (TextView) vi.findViewById(R.id.product_description); // product description
            TextView product_price = (TextView) vi.findViewById(R.id.product_price); // product price
            ImageView list_image = (ImageView) vi.findViewById(R.id.list_image); // thumb image


            HashMap<String, String> doctor = new HashMap<String, String>();
            doctor = data.get(position);

            // Setting all values in listview
            product_name.setText(doctor.get(ProductsFragment.KEY_PRODUCT_NAME));
            product_description.setText(doctor.get(ProductsFragment.KEY_PRODUCT_DESCRIPTION));
            product_price.setText(doctor.get(ProductsFragment.KEY_PRODUCT_PRICE));
            imageLoader.DisplayImage(doctor.get(ProductsFragment.KEY_PRODUCT_PHOTO), list_image);

        } else if (list_type == "consultation_lists") {
            vi = inflater.inflate(R.layout.list_row_consultations, null);

            TextView doctor = (TextView) vi.findViewById(R.id.doctor_name);
            TextView clinic_address = (TextView) vi.findViewById(R.id.clinic_address);
            TextView consultation_schedule = (TextView) vi.findViewById(R.id.consultation_schedule);

            HashMap<String, String> schedule = new HashMap<String, String>();
            schedule = data.get(position);

            // Setting all values in listview
            doctor.setText(schedule.get(PatientConsultationFragment.KEY_DOCTOR_NAME));
            clinic_address.setText(schedule.get(PatientConsultationFragment.KEY_CLINIC_ADDRESS));

            String sched = schedule.get(PatientConsultationFragment.KEY_DATE) + ", " +
                    (schedule.get(PatientConsultationFragment.KEY_SCHEDULE).equals("AM") ? "Morning" : "Afternoon");

            consultation_schedule.setText(sched);

        } else if (list_type == "basket_items") {
            vi = inflater.inflate(R.layout.list_row_basket_items, null);
            // qty.addTextChangedListener(this);

            HashMap<String, String> basket_items = new HashMap<String, String>();


            basket_items = data.get(position);

            TextView product_name = (TextView) vi.findViewById(R.id.product_name);
            qty = (EditText) vi.findViewById(R.id.product_quantity);
            total = (TextView) vi.findViewById(R.id.total);

            // Do the fucking stuffs here
            price = Double.parseDouble(basket_items.get("price"));
            quantity = Integer.parseInt(basket_items.get("quantity"));

            // Setting all values in listview
            product_name.setText(basket_items.get("name") + " @Php " + price);
            total_amount = price * quantity;

            total.setText(total_amount + "");
            qty.setText(quantity + "");

            qty.setId(position);
            total.setId(position);

        }
        return vi;
    }
}
