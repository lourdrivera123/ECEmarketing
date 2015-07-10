//package com.example.zem.patientcareapp;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//
//import com.nostra13.universalimageloader.core.*;
//import com.nostra13.universalimageloader.core.assist.FailReason;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
//import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
//
///**
// * Created by Zem on 7/10/2015.
// */
//public class TrialPrescriptionFragment extends Fragment {
//
//    GridView gridView;
//
//    public static final int INDEX = 1;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.prescriptions_layout, container, false);
//        gridView = (GridView) rootView.findViewById(R.id.gridView);
//        gridView.setAdapter(new ImageAdapter(getActivity()));
////        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                startImagePagerActivity(position);
////            }
////        });
//        return rootView;
//    }
//
//    private static class ImageAdapter extends BaseAdapter {
//
//        private static final String[] IMAGE_URLS = Constants.IMAGES;
//
//        private LayoutInflater inflater;
//
//        private DisplayImageOptions options;
//
//        ImageAdapter(Context context) {
//            inflater = LayoutInflater.from(context);
//
//            options = new DisplayImageOptions.Builder()
//                    .showImageOnLoading(R.mipmap.ic_stub)
//                    .showImageForEmptyUri(R.mipmap.ic_empty)
//                    .showImageOnFail(R.mipmap.ic_error)
//                    .cacheInMemory(true)
//                    .cacheOnDisk(true)
//                    .considerExifParams(true)
//                    .bitmapConfig(Bitmap.Config.RGB_565)
//                    .build();
//        }
//
//        @Override
//        public int getCount() {
//            return IMAGE_URLS.length;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            final ViewHolder holder;
//            View view = convertView;
//            if (view == null) {
//                view = inflater.inflate(R.layout.item_grid_image, parent, false);
//                holder = new ViewHolder();
//                assert view != null;
//                holder.imageView = (ImageView) view.findViewById(R.id.image);
//                holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
//                view.setTag(holder);
//            } else {
//                holder = (ViewHolder) view.getTag();
//            }
//
//            com.nostra13.universalimageloader.core.ImageLoader.getInstance()
//                    .displayImage(IMAGE_URLS[position], holder.imageView, options, new SimpleImageLoadingListener() {
//                        @Override
//                        public void onLoadingStarted(String imageUri, View view) {
//                            holder.progressBar.setProgress(0);
//                            holder.progressBar.setVisibility(View.VISIBLE);
//                        }
//
//                        @Override
//                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                            holder.progressBar.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                            holder.progressBar.setVisibility(View.GONE);
//                        }
//                    }, new ImageLoadingProgressListener() {
//                        @Override
//                        public void onProgressUpdate(String imageUri, View view, int current, int total) {
//                            holder.progressBar.setProgress(Math.round(100.0f * current / total));
//                        }
//                    });
//
//            return view;
//        }
//    }
//
//    static class ViewHolder {
//        ImageView imageView;
//        ProgressBar progressBar;
//    }
//}
