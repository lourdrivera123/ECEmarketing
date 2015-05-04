package com.example.zem.patientcareapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Dexter B. on 5/4/2015.
 */
public class LazyAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;

    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d){
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
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView artist = (TextView)vi.findViewById(R.id.specialty); // artist name
        ImageView list_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image


        HashMap <String, String> doctor = new HashMap <String, String>();
        doctor = data.get(position);

        // Setting all values in listview
        title.setText(doctor.get(PatientHomeActivity.KEY_FULL_NAME));
        artist.setText(doctor.get(PatientHomeActivity.KEY_SPECIALTY));
        imageLoader.DisplayImage(doctor.get(PatientHomeActivity.KEY_PHOTO), list_image);
        return vi;
    }
}
