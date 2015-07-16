package com.example.zem.patientcareapp.adapter;

import com.example.zem.patientcareapp.ImageGallery.TouchImageView;
import com.example.zem.patientcareapp.PrescriptionFragment;
import com.example.zem.patientcareapp.R;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class FullScreenImageAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<Bitmap> imgBitmap;
    private LayoutInflater inflater;

    // constructor
    public FullScreenImageAdapter(Activity activity, ArrayList<Bitmap> imgBitmap) {
        this._activity = activity;
        this.imgBitmap = imgBitmap;
    }

    @Override
    public int getCount() {
        return this.imgBitmap.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView imgDisplay;

        inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container, false);

        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);

        Bitmap bitmap = imgBitmap.get(position);
        imgDisplay.setImageBitmap(bitmap);

        container.addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}