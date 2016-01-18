package com.example.zem.patientcareapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zem.patientcareapp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lourdrivera on 1/18/2016.
 */
public class PointsLogAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, String>> hashOfPointsLog;

    public PointsLogAdapter(Context context, ArrayList<HashMap<String, String>> hashOfPointsLog){
        this.context = context;
        this.hashOfPointsLog = hashOfPointsLog;
    }

    @Override
    public int getCount() {
        return hashOfPointsLog.size();
    }

    @Override
    public Object getItem(int position) {
        return hashOfPointsLog.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.points_log_item, null);
        }

        TextView date_acquired = (TextView) convertView.findViewById(R.id.date_acquired);
        TextView note = (TextView) convertView.findViewById(R.id.note);

        date_acquired.setText(hashOfPointsLog.get(position).get("created_at"));
        note.setText(hashOfPointsLog.get(position).get("notes"));

        return convertView;
    }
}
