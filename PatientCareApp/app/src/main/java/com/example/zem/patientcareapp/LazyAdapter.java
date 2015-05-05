package com.example.zem.patientcareapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


/**
 * Created by Dexter B. on 5/4/2015.
 */
public class LazyAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;

    private String list_type;

    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d, String lt){
        list_type = lt;
        activity = a;
        data = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
    }

    public int getCount(){
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;

        System.out.println("id: "+list_type);

        if( list_type == "list_of_doctors" ) {
            vi = inflater.inflate(R.layout.list_row, null);

            TextView title = (TextView) vi.findViewById(R.id.title); // title
            TextView artist = (TextView) vi.findViewById(R.id.specialty); // artist name
            ImageView list_image = (ImageView) vi.findViewById(R.id.list_image); // thumb image


            HashMap<String, String> doctor = new HashMap<String, String>();
            doctor = data.get(position);

            // Setting all values in listview
            title.setText(doctor.get(PatientHomeActivity.KEY_FULL_NAME));
            artist.setText(doctor.get(PatientHomeActivity.KEY_SPECIALTY));
            imageLoader.DisplayImage(doctor.get(PatientHomeActivity.KEY_PHOTO), list_image);

        }else if( list_type == "product_lists" ){
            vi = inflater.inflate(R.layout.list_row_products, null);
            TextView product_name = (TextView) vi.findViewById(R.id.product_name); // product name
            TextView product_description = (TextView) vi.findViewById(R.id.product_description); // product description
            TextView product_price = (TextView) vi.findViewById(R.id.product_price); // product price
            ImageView list_image = (ImageView) vi.findViewById(R.id.list_image); // thumb image



            HashMap<String, String> doctor = new HashMap<String, String>();
            doctor = data.get(position);

            // Setting all values in listview
            product_name.setText(doctor.get(ProductsActivity.KEY_PRODUCT_NAME));
            product_description.setText(doctor.get(ProductsActivity.KEY_PRODUCT_DESCRIPTION));
            product_price.setText(doctor.get(ProductsActivity.KEY_PRODUCT_PRICE));
            imageLoader.DisplayImage(doctor.get(ProductsActivity.KEY_PRODUCT_PHOTO), list_image);

        }else if( list_type == "consultation_lists" ){
            vi = inflater.inflate(R.layout.list_row_consultations, null);

            TextView doctor = (TextView) vi.findViewById(R.id.doctor_name);
            TextView clinic_address = (TextView) vi.findViewById(R.id.clinic_address);
            TextView consultation_schedule = (TextView) vi.findViewById(R.id.consultation_schedule);

            HashMap<String, String> schedule = new HashMap<String, String>();
            schedule = data.get(position);

            // Setting all values in listview
            doctor.setText(schedule.get(PatientConsultationActivity.KEY_DOCTOR_NAME));
            clinic_address.setText(schedule.get(PatientConsultationActivity.KEY_CLINIC_ADDRESS));

            String sched = schedule.get(PatientConsultationActivity.KEY_DATE)+", "+
                    ( schedule.get(PatientConsultationActivity.KEY_SCHEDULE).equals("AM")  ?  "Morning" : "Afternoon");

            consultation_schedule.setText(sched);

        }
        return vi;
    }
}
