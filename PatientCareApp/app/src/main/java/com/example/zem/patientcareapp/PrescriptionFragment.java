package com.example.zem.patientcareapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zem.patientcareapp.GetterSetter.ImageItem;
import com.example.zem.patientcareapp.adapter.GridViewAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    ImageButton add_pres;
    LinearLayout pick_camera_layout, pick_gallery_layout;
    ProgressBar progressBar;
    private TextView txtPercentage;
    GridView gridView;
    Dialog upload_dialog, dialog1;

    ArrayList<ImageItem> imageItems;
    public static ArrayList<Bitmap> allBitmap;
    ArrayList<String> uploadsByUser;

    private ImageItem item;
    private GridViewAdapter gridAdapter;
    Helpers helper;
    DbHelper dbhelper;

    String imageFileUri;
    String filePath = null;
    long totalSize = 0;
    int patientID;

//    ImageLoader imageLoader;
    DisplayImageOptions options;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.prescriptions_layout, container, false);

        add_pres = (ImageButton) rootView.findViewById(R.id.add_pres);
        gridView = (GridView) rootView.findViewById(R.id.gridView);

        upload_dialog = new Dialog(getActivity());
        upload_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        upload_dialog.setContentView(R.layout.activity_upload);

        txtPercentage = (TextView) upload_dialog.findViewById(R.id.txtPercentage);
        progressBar = (ProgressBar) upload_dialog.findViewById(R.id.progressBar);

        imageItems = new ArrayList();
        allBitmap = new ArrayList();

        helper = new Helpers();
        dbhelper = new DbHelper(getActivity());

        patientID = HomeTileActivity.getUserID();

        gridAdapter = new GridViewAdapter(getActivity(), imageItems);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(this);
        add_pres.setOnClickListener(this);

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

//        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(getActivity());
//        config.threadPriority(Thread.NORM_PRIORITY - 2);
//        config.denyCacheImageMultipleSizesInMemory();
//        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
//        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
//        config.tasksProcessingOrder(QueueProcessingType.LIFO);
//        config.writeDebugLogs(); // Remove for release app


//        imageLoader = ImageLoader.getInstance(); // Get singleton instance
//        imageLoader.init(config.build());

        uploadsByUser = dbhelper.getUploadedPrescriptionsByUserID(patientID);

        uploadsByUser.toArray(new String[uploadsByUser.size()]);

        if(uploadsByUser.size() > 0) {

                    for (int x= 0; x < uploadsByUser.size(); x++) {

//                        Log.d("index zero uploads by user", uploadsByUser.get(x) + "");
                        List<Bitmap> bm = MemoryCacheUtils.findCachedBitmapsForImageUri("https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", ImageLoader.getInstance().getMemoryCache());
                        Log.d("List of cache", bm + "");
                        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(bm.get(x), 960, 960);
                        item = new ImageItem(ThumbImage);
                        item.setImage(ThumbImage);

                        allBitmap.add(bm.get(x));
                        imageItems.add(new ImageItem(ThumbImage));
                        gridAdapter.notifyDataSetChanged();
                    }
        }

//        Log.d("index zero uploads by user", uploadsByUser.get(0)+"");
//        List<Bitmap> bm = MemoryCacheUtils.findCachedBitmapsForImageUri("https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg", ImageLoader.getInstance().getMemoryCache());
//        Log.d("List of cache", bm + "");
//        for (int x= 0; x < uploadsByUser.size(); x++) {
//
//            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(bm.get(0), 960, 960);
//            item = new ImageItem(ThumbImage);
//            item.setImage(ThumbImage);
//
//            allBitmap.add(bm.get(0));
//            imageItems.add(new ImageItem(ThumbImage));
//            gridAdapter.notifyDataSetChanged();
//        }
        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_pres:
                if (helper.isNetworkAvailable(getActivity())) {
                    dialog1 = new Dialog(getActivity());
                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog1.setContentView(R.layout.dialog_gallery_camera);
                    dialog1.show();

                    pick_camera_layout = (LinearLayout) dialog1.findViewById(R.id.pick_camera_layout);
                    pick_gallery_layout = (LinearLayout) dialog1.findViewById(R.id.pick_gallery_layout);

                    pick_camera_layout.setOnClickListener(this);
                    pick_gallery_layout.setOnClickListener(this);
                } else {
                    Toast.makeText(getActivity(), "Network unavailable", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.pick_camera_layout:
                Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent_camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(getActivity())));
                startActivityForResult(intent_camera, 1337);
                dialog1.dismiss();
                break;

            case R.id.pick_gallery_layout:
                Intent intent_gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent_gallery.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
                startActivityForResult(intent_gallery, 111);
                dialog1.dismiss();
                break;
        }
    }

    private File getTempFile(Context context) {
        final File path = new File(Environment.getExternalStorageDirectory(), context.getPackageName());
        if (!path.exists()) {
            path.mkdir();
        }
        return new File(path, "image.tmp");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == getActivity().RESULT_OK) { //GALLERY
            if (data.getData() != null && !data.getData().equals(Uri.EMPTY)) {
                Uri uri = data.getData();
                String[] projection = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(projection[0]);
                String path = cursor.getString(columnIndex);

                filePath = path;
                showProgressbar();
                new UploadFileToServer().execute();

                cursor.close();
            }
        } else if (requestCode == 1337 && resultCode == getActivity().RESULT_OK) { //CAMERA
            final File file = getTempFile(getActivity());
            try {
                Bitmap captureBmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.fromFile(file));

                Uri tempUri = getImageUri(getActivity(), captureBmp);
                File finalFile = new File(getRealPathFromURI(tempUri));
                String path = String.valueOf(finalFile);

                filePath = path;
                showProgressbar();

                new UploadFileToServer().execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap rotateIMG(String file) {
        Bitmap rotatedBitmap = null;
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bm = BitmapFactory.decodeFile(file, opts);

            ExifInterface exif = new ExifInterface(file);
            String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;

            int rotationAngle = 0;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

            Matrix matrix = new Matrix();
            matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
            rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, opts.outWidth, opts.outHeight, matrix, true);
        } catch (Exception e) {

        }
        return rotatedBitmap;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(), FullScreenViewActivity.class);
        i.putExtra("position", position);
        startActivity(i);
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(progress[0]);
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString;

            DbHelper dbHelper = new DbHelper(getActivity());
            int patientID = dbHelper.getCurrentLoggedInPatient().getServerID();

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL+"?patient_id="+patientID);

            try {
                AndroidMultipartEntity entity = new AndroidMultipartEntity(
                        new AndroidMultipartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filePath);

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: " + statusCode;
                }
            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.d("response from server", result);
            JSONObject jObject;
            String image_url = "";
            try {
                jObject = new JSONObject(result);
                image_url = jObject.getString("file_path");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Load image, decode it to Bitmap and return Bitmap to callback
            ImageSize targetSize = new ImageSize(80, 50); // result Bitmap will be fit to this size
            ImageLoader.getInstance().loadImage(image_url, targetSize, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (dbhelper.insertUploadOnPrescription(patientID, imageUri)) {
//                        Log.d();
                        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(loadedImage, 960, 960);
                        item = new ImageItem(ThumbImage);
                        item.setImage(ThumbImage);

                        allBitmap.add(loadedImage);
                        imageItems.add(new ImageItem(ThumbImage));
                        gridAdapter.notifyDataSetChanged();
                        List<Bitmap> bm = MemoryCacheUtils.findCachedBitmapsForImageUri(imageUri, ImageLoader.getInstance().getMemoryCache());
                        Log.d("image_uri from server", imageUri + "");
                        Log.d("List of cache", bm + "");
                    } else {
                        Toast.makeText(getActivity(), "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            upload_dialog.dismiss();
            super.onPostExecute(result);
        }
    }

    public void showProgressbar() {
        upload_dialog.show();
    }
}
