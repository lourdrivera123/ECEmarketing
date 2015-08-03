package com.example.zem.patientcareapp.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zem.patientcareapp.AndroidMultipartEntity;
import com.example.zem.patientcareapp.Config;
import com.example.zem.patientcareapp.DbHelper;
import com.example.zem.patientcareapp.Helpers;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.ServerRequest;
import com.example.zem.patientcareapp.SimpleImageActivity;
import com.nostra13.universalimageloader.core.*;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TrialPrescriptionFragment extends Fragment implements View.OnClickListener {
    GridView gridView;
    ImageButton add_pres;
    LinearLayout pick_camera_layout, pick_gallery_layout;
    ProgressBar progressBar;
    private TextView txtPercentage;
    Dialog upload_dialog, dialog1;

    ArrayList<HashMap<String, String>> uploadsByUser;
    ArrayList<String> arrayOfPrescriptions;

    Helpers helper;
    ImageAdapter imgAdapter;
    DbHelper dbhelper;
    ServerRequest serverRequest;
    View rootView;

    String imageFileUri;
    String filePath = null;
    long totalSize = 0;
    int patientID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.prescriptions_layout, container, false);

        dbhelper = new DbHelper(getActivity());
        helper = new Helpers();

        gridView = (GridView) rootView.findViewById(R.id.gridView);
        add_pres = (ImageButton) rootView.findViewById(R.id.add_pres);

        patientID = dbhelper.getCurrentLoggedInPatient().getServerID();
        arrayOfPrescriptions = refreshPrescriptionList();

        add_pres.setOnClickListener(this);

        imgAdapter = new ImageAdapter(getActivity(), R.layout.item_grid_image, arrayOfPrescriptions);
        gridView.setAdapter(imgAdapter);
        gridView.setOnCreateContextMenuListener(this);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startImagePagerActivity(position);
            }
        });

        upload_dialog = new Dialog(getActivity());
        upload_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        upload_dialog.setContentView(R.layout.activity_upload);

        txtPercentage = (TextView) upload_dialog.findViewById(R.id.txtPercentage);
        progressBar = (ProgressBar) upload_dialog.findViewById(R.id.progressBar);

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.delete_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (item.getItemId() == R.id.delete_context) {
            AlertDialog.Builder delete = new AlertDialog.Builder(getActivity());
            delete.setTitle("Delete?");
            delete.setNegativeButton("No", null);
            delete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (helper.isNetworkAvailable(getActivity())) {
                        final int serverID = Integer.parseInt(uploadsByUser.get(menuInfo.position).get(dbhelper.PRESCRIPTIONS_SERVER_ID));
                        serverRequest = new ServerRequest();
                        HashMap<String, String> hashMap = new HashMap();
                        hashMap.put("table", "patient_prescriptions");
                        hashMap.put("request", "crud");
                        hashMap.put("action", "delete");
                        hashMap.put("id", String.valueOf(serverID));
                        serverRequest.init(getActivity(), hashMap, "insert");

                        final ProgressDialog pdialog = new ProgressDialog(getActivity());
                        pdialog.setCancelable(false);
                        pdialog.setMessage("Deleting...");
                        pdialog.show();

                        rootView.postDelayed(new Runnable() {
                            public void run() {
                                boolean responseFromServer = serverRequest.getResponse();

                                if (responseFromServer) {
                                    if (dbhelper.deletePrescriptionByServerID(serverID)) {
                                        arrayOfPrescriptions = refreshPrescriptionList();
                                        gridView.setAdapter(new ImageAdapter(getActivity(), 0, arrayOfPrescriptions));
                                        pdialog.dismiss();
                                    } else {
                                        Toast.makeText(getActivity(), "Sorry, we can't delete your item right now. Please try again later.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }, 3000);
                    } else {
                        Toast.makeText(getActivity(), "Please connect to the internet.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            delete.show();
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_pres:
//                if (helper.isNetworkAvailable(getActivity())) {
                    dialog1 = new Dialog(getActivity());
                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog1.setContentView(R.layout.dialog_gallery_camera);
                    dialog1.show();

                    pick_camera_layout = (LinearLayout) dialog1.findViewById(R.id.pick_camera_layout);
                    pick_gallery_layout = (LinearLayout) dialog1.findViewById(R.id.pick_gallery_layout);

                    pick_camera_layout.setOnClickListener(this);
                    pick_gallery_layout.setOnClickListener(this);
//                } else {
//                    Toast.makeText(getActivity(), "Network unavailable", Toast.LENGTH_SHORT).show();
//                }
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

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
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
            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL + "?patient_id=" + patientID);

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
            JSONObject jObject;
            String image_url = "";
            int serverID = 0;
            try {
                jObject = new JSONObject(result);
                image_url = jObject.getString("file_path");
                serverID = jObject.getInt("server_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //put the refresh grid here or the display newly added image here
            if (dbhelper.insertUploadOnPrescription(patientID, image_url, serverID)) {
                arrayOfPrescriptions = refreshPrescriptionList();
                gridView.setAdapter(new ImageAdapter(getActivity(), 0, arrayOfPrescriptions));
            } else {
                Toast.makeText(getActivity(), "Error occurred", Toast.LENGTH_SHORT).show();
            }

            upload_dialog.dismiss();
            super.onPostExecute(result);
        }
    }

    public void showProgressbar() {
        upload_dialog.show();
    }

    private class ImageAdapter extends ArrayAdapter<String> {
        String[] image_urls;
        private LayoutInflater inflater;
        private DisplayImageOptions options;

        public ImageAdapter(Context context, int resource, ArrayList<String> uploadsByUser) {
            super(context, R.layout.item_grid_image, uploadsByUser);

            inflater = LayoutInflater.from(context);
            image_urls = uploadsByUser.toArray(new String[uploadsByUser.size()]);

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.ic_stub)
                    .showImageForEmptyUri(R.mipmap.ic_empty)
                    .showImageOnFail(R.mipmap.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

        @Override
        public int getCount() {
            return image_urls.length;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.item_grid_image, parent, false);
            assert view != null;

            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            imageView.setTag(position);
            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
            progressBar.setTag(position);

            com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                    .displayImage(image_urls[position], imageView, options, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            progressBar.setProgress(0);
                            progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }, new ImageLoadingProgressListener() {
                        @Override
                        public void onProgressUpdate(String imageUri, View view, int current, int total) {
                            progressBar.setProgress(Math.round(100.0f * current / total));
                        }
                    });

            return view;
        }
    }

    protected void startImagePagerActivity(int position) {
        Intent intent = new Intent(getActivity(), SimpleImageActivity.class);
        intent.putExtra(Config.IMAGE_POSITION, position);
        startActivity(intent);
    }

    public ArrayList<String> refreshPrescriptionList() {
        uploadsByUser = dbhelper.getPrescriptionByUserID(patientID);
        ArrayList<String> prescriptionArray = new ArrayList();

        for (int x = 0; x < uploadsByUser.size(); x++) {
            prescriptionArray.add(uploadsByUser.get(x).get(dbhelper.PRESCRIPTIONS_FILENAME));
        }
        return prescriptionArray;
    }
}
