package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.Fragment.TrialPrescriptionFragment;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.PostRequest;
import com.nostra13.universalimageloader.core.*;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewPagerActivity extends Activity implements ViewPager.OnPageChangeListener {
    ViewPager viewPager;
    View v;

    ArrayList<String> uploadsByUser;
    ArrayList<HashMap<String, String>> hashPrescriptions;

    MyPagerAdapter pagerAdapter;
    ServerRequest serverRequest;
    DbHelper dbhelper;
    Intent intent;

    int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_full_screen);

        dbhelper = new DbHelper(this);
        uploadsByUser = new ArrayList();
        intent = getIntent();

        ActionBar actionbar = getActionBar();
        MainActivity.setCustomActionBar(actionbar);
        actionbar.setDisplayHomeAsUpEnabled(true);

        int patientID = dbhelper.getCurrentLoggedInPatient().getServerID();
        hashPrescriptions = dbhelper.getPrescriptionByUserID(patientID);
        selectedPosition = intent.getIntExtra(Config.IMAGE_POSITION, 0);

        for (int x = 0; x < hashPrescriptions.size(); x++) {
            uploadsByUser.add(hashPrescriptions.get(x).get(dbhelper.PRESCRIPTIONS_FILENAME));
        }

        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new MyPagerAdapter(this, uploadsByUser);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(selectedPosition);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            case R.id.item_delete:
                AlertDialog.Builder delete = new AlertDialog.Builder(this);
                delete.setTitle("Delete?");
                delete.setNegativeButton("No", null);
                delete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final int serverID = Integer.parseInt(hashPrescriptions.get(selectedPosition).get(dbhelper.PRESCRIPTIONS_SERVER_ID));
                        serverRequest = new ServerRequest();
                        HashMap<String, String> hashMap = new HashMap();
                        hashMap.put("table", "patient_prescriptions");
                        hashMap.put("request", "crud");
                        hashMap.put("action", "delete");
                        hashMap.put("id", String.valueOf(serverID));

                        final ProgressDialog pdialog = new ProgressDialog(ViewPagerActivity.this);
                        pdialog.setCancelable(false);
                        pdialog.setMessage("Deleting...");
                        pdialog.show();

                        PostRequest.send(getBaseContext(), hashMap, serverRequest, new RespondListener<JSONObject>() {
                            @Override
                            public void getResult(JSONObject response) {
                                Log.d("response using interface <ViewPagerActivity.java>", response + "");
                                boolean responseFromServer = serverRequest.getResponse();

                                if (responseFromServer) {
                                    if (dbhelper.deletePrescriptionByServerID(serverID)) {
                                        pagerAdapter.removeView(viewPager, selectedPosition);
                                        pdialog.dismiss();
                                    } else {
                                        Toast.makeText(getBaseContext(), "Sorry, we can't delete your item right now. Please try again later.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                        }, new ErrorListener<VolleyError>() {
                            public void getError(VolleyError error) {
                                Log.d("Error", "asdasda ");
                                Toast.makeText(getBaseContext(), "Couldn't delete item. Please check your Internet connection", Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                });
                delete.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        selectedPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    ///////////////////////ADAPTER FOR VIEWPAGER//////////////////////

    private class MyPagerAdapter extends PagerAdapter {
        private DisplayImageOptions options;
        private LayoutInflater inflater;
        String[] image_urls;

        MyPagerAdapter(Context context, ArrayList<String> uploadsByUser) {
            inflater = LayoutInflater.from(context);
            image_urls = uploadsByUser.toArray(new String[uploadsByUser.size()]);

            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.mipmap.ic_empty)
                    .showImageOnFail(R.mipmap.ic_error)
                    .resetViewBeforeLoading(true)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .considerExifParams(true)
                    .displayer(new FadeInBitmapDisplayer(300))
                    .build();
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            v = inflater.inflate(R.layout.layout_fullscreen_image, view, false);
            assert v != null;

            ImageView imageView = (ImageView) v.findViewById(R.id.imgDisplay);
            final ProgressBar spinner = (ProgressBar) v.findViewById(R.id.loading);

            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(image_urls[position], imageView, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    spinner.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    String message = null;
                    switch (failReason.getType()) {
                        case IO_ERROR:
                            message = "Input/Output error";
                            break;
                        case DECODING_ERROR:
                            message = "Image can't be decoded";
                            break;
                        case NETWORK_DENIED:
                            message = "Downloads are denied";
                            break;
                        case OUT_OF_MEMORY:
                            message = "Out Of Memory error";
                            break;
                        case UNKNOWN:
                            message = "Unknown error";
                            break;
                    }
                    Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

                    spinner.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    spinner.setVisibility(View.GONE);
                }
            });

            view.addView(v, 0);
            return v;
        }

        @Override
        public int getCount() {
            return image_urls.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public int removeView(ViewPager pager, int position) {
            uploadsByUser.remove(position);
            hashPrescriptions.remove(position);

            if (hashPrescriptions.size() == 0 && uploadsByUser.size() == 0)
                ViewPagerActivity.this.finish();

            pagerAdapter = new MyPagerAdapter(ViewPagerActivity.this, uploadsByUser);
            pager.setAdapter(pagerAdapter);
            pager.setCurrentItem(position);
            v.invalidate();

            return position;
        }
    }
}
