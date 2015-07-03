package com.example.zem.patientcareapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
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
import com.example.zem.patientcareapp.ImageGallery.AppConstant;
import com.example.zem.patientcareapp.ImageGallery.ImageUtils;
import com.example.zem.patientcareapp.adapter.GridViewAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Zemie, Esel, Dexie
 */
public class PrescriptionFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    ImageButton add_pres;
    LinearLayout pick_camera_layout, pick_gallery_layout;
    ArrayList<ImageItem> imageItems;
    public static ArrayList<String> uriItems;

    private GridView gridView;
    private ImageItem item;

    Dialog dialog1;
    Dialog upload_dialog;
    private GridViewAdapter gridAdapter;
    String imageFileUri;
    Uri imageUri;

    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private String filePath = null;
    private TextView txtPercentage;
    long totalSize = 0;

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
        uriItems = new ArrayList();

        gridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, imageItems);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(this);
        add_pres.setOnClickListener(this);

//        PrescriptionFragment.imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_pres:
                dialog1 = new Dialog(getActivity());
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.dialog_gallery_camera);
                dialog1.show();

                pick_camera_layout = (LinearLayout) dialog1.findViewById(R.id.pick_camera_layout);
                pick_gallery_layout = (LinearLayout) dialog1.findViewById(R.id.pick_gallery_layout);

                pick_camera_layout.setOnClickListener(this);
                pick_gallery_layout.setOnClickListener(this);

                break;

            case R.id.pick_camera_layout:
                Intent intent_camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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
        if (requestCode == 111 && resultCode == getActivity().RESULT_OK) {
            if (data.getData() != null && !data.getData().equals(Uri.EMPTY)) {
                Uri uri = data.getData();
                String[] projection = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(projection[0]);
                String path = cursor.getString(columnIndex);
                uriItems.add(path);

                filePath = path;
                showProgressbar();

                new UploadFileToServer().execute();

                cursor.close();

//                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
//                item = new ImageItem(yourSelectedImage, "selected");
//                item.setImage(yourSelectedImage);
//
//                imageItems.add(new ImageItem(yourSelectedImage, "selected"));
//                gridAdapter.notifyDataSetChanged();


            }
        } else if (requestCode == 1337 && resultCode == getActivity().RESULT_OK) {
            final File file = getTempFile(getActivity());
            try {
                Bitmap captureBmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.fromFile(file));
//                item = new ImageItem(captureBmp, "selected");
//                item.setImage(captureBmp);
//
//                imageItems.add(new ImageItem(captureBmp, "selected"));
//                gridAdapter.notifyDataSetChanged();

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getActivity(), captureBmp);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));
                String path = String.valueOf(finalFile);
                uriItems.add(path);

                // Receiving the data from previous activity
//                Intent i = getIntent();
//
//                // image or video path that is captured in previous activity

                //                if (path != null) {
//                } else {
//                    Toast.makeText(getActivity(),
//                            "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
//                }
                filePath = path;
                showProgressbar();

                new UploadFileToServer().execute();

//                Intent i = new Intent(getActivity(), UploadActivity.class);
//                i.putExtra("filePath", path);
//                startActivity(i);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

//    public String getRealPathFromURI(Uri uri) {
//        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//        return cursor.getString(idx);
//    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(), FullScreenViewActivity.class);
        i.putExtra("position", position);
        startActivity(i);
    }

    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);

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

                // Extra parameters if you want to pass to server
                entity.addPart("website",
                        new StringBody("www.androidhive.info"));
                entity.addPart("email", new StringBody("abc@gmail.com"));

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
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
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
            Log.e(TAG, "Response from server: " + result);

            JSONObject jObject = null;
            String image_url = "";
            try {
                jObject = new JSONObject(result);
                image_url = jObject.getString("file_path");
                showAlert(image_url);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//
//            JSONObject file_path_json = null;
//
//


            //      // showing the server response in an alert dialog
//            showAlert(result);

            ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
//
            imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
// Load image, decode it to Bitmap and display Bitmap in ImageView (or any other view
//  which implements ImageAware interface)
//            imageLoader.displayImage(imageUri, imageItems);
            // Load image, decode it to Bitmap and return Bitmap to callback
            imageLoader.loadImage(image_url, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    // Do whatever you want with Bitmap
//                    Bitmap captureBmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.fromFile(file));
                    item = new ImageItem(loadedImage, "selected");
                    item.setImage(loadedImage);

                    imageItems.add(new ImageItem(loadedImage, "selected"));
                    gridAdapter.notifyDataSetChanged();
                }
            });

            upload_dialog.dismiss();



            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showProgressbar() {
//        upload_dialog = new Dialog(getActivity());
//        upload_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        upload_dialog.setContentView(R.layout.activity_upload);
        upload_dialog.show();
    }
}
