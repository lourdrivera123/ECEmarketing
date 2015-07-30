package com.example.zem.patientcareapp.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.zem.patientcareapp.Config;
import com.example.zem.patientcareapp.DbHelper;
import com.example.zem.patientcareapp.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ImagePagerFragment extends Fragment {

    ArrayList<String> uploadsByUser;
    ArrayList<HashMap<String, String>> hashPrescriptions;

    DbHelper dbhelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.img_full_screen, container, false);

        dbhelper = new DbHelper(getActivity());

        int patientID = dbhelper.getCurrentLoggedInPatient().getServerID();

        uploadsByUser = new ArrayList();
        hashPrescriptions = dbhelper.getPrescriptionByUserID(patientID);

        for (int x = 0; x < hashPrescriptions.size(); x++) {
            uploadsByUser.add(hashPrescriptions.get(x).get(dbhelper.PRESCRIPTIONS_FILENAME));
        }

        ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);

        pager.setAdapter(new ImageAdapter(getActivity(), uploadsByUser));
        pager.setCurrentItem(getArguments().getInt(Config.IMAGE_POSITION, 0));

        return rootView;
    }

    private static class ImageAdapter extends PagerAdapter {
        String[] image_urls;

        private LayoutInflater inflater;
        private DisplayImageOptions options;

        ImageAdapter(Context context, ArrayList<String> uploadsByUser) {
            inflater = LayoutInflater.from(context);

            image_urls = (String[]) uploadsByUser.toArray(new String[uploadsByUser.size()]);


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
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return image_urls.length;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.layout_fullscreen_image, view, false);
            Log.d("position on imagepager", position + "");

            assert imageLayout != null;
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.imgDisplay);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

            ImageLoader.getInstance().displayImage(image_urls[position], imageView, options, new SimpleImageLoadingListener() {
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

            view.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }
}