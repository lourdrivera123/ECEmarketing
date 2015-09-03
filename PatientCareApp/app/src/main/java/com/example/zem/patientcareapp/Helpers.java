package com.example.zem.patientcareapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.zem.patientcareapp.adapter.ImageAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User PC on 5/7/2015.
 */
public class Helpers implements View.OnCreateContextMenuListener {

    RequestQueue queue;
    boolean connection;
    Context contextc;
    Dialog presDialog;
    String imageFileUri;

    public Helpers(){

    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void hasActiveInternetConnection(Context context) {
        contextc = context;
        queue = Volley.newRequestQueue(contextc);

        if (isNetworkAvailable(context)) {

            StringRequest website_request = new StringRequest(Request.Method.GET, "http://vinzry.0fees.us", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    System.out.println("internet status: there is internet connection");
                    System.out.println("response internet status: "+response + "");

//                    connection = true;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("internet status", "there is no internet connection");
                    AlertDialog alertDialog = new AlertDialog.Builder(contextc).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Alert message to be shown");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
//                    Toast.makeText(context, "Error on request", Toast.LENGTH_SHORT).show();
//                    connection = false;
                }
            });
            queue.add(website_request);

        }
//        return connection;
    }


    /* Returns db row column value */
    public static String curGetStr(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public void showNotification(Context context, Intent resultIntent, int mNotificationId, String title, String body, boolean playRingTone) {
        NotificationCompat.Builder mBuilder;

        if( playRingTone ) {
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_app)
                            .setContentTitle(title)
                            .setSound(uri)
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setContentText(body);
        }else{
            mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_app)
                            .setContentTitle(title)
                            .setContentText(body);
        }

        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        // Sets an ID for the notification
        // int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public String get_url(String request){
        return Constants.GET_REQUEST_URL+request;
    }

    public String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getPluralForm(String noun, int qty){
        String lastChar = "";

        if(qty < 2) return  noun;

        lastChar = noun.substring(noun.length() -2);
        if( lastChar.equals("um") ) return noun.replace("um", "a");
        if( lastChar.equals("fe") ) return noun.replace("fe", "ves");


        lastChar = noun.substring(noun.length() - 1);

        if( lastChar.equals("f") ) return noun.replace("f", "ves");

        if( lastChar.equals("y") ){
            return noun.replace("y", "ies");
        }else if( lastChar.equals("s") || lastChar.equals("x") || lastChar.equals("ch") ){
            return noun+"es";
        }else{
            return noun+"s";
        }
    }


    public HashMap<GridView, Dialog> showPrescriptionDialog(Context context) {

        // Prepare grid view
        GridView gridView = new GridView(context);
        gridView.setNumColumns(2);
        gridView.setPadding(2, 4, 4, 2);

        ArrayList<HashMap<String, String>> arrayOfPrescriptions;
        int patientID;
        DbHelper dbHelper = new DbHelper(context);
        final Dialog builder = new Dialog(context);

        patientID = dbHelper.getCurrentLoggedInPatient().getServerID();
        arrayOfPrescriptions = refreshPrescriptionList(context, patientID);

        System.out.println("Array of Prescriptions: "+arrayOfPrescriptions.toString());

        ImageAdapter imgAdapter = new ImageAdapter(context, R.layout.item_grid_image, arrayOfPrescriptions);
        gridView.setAdapter(imgAdapter);
        gridView.setOnCreateContextMenuListener(this);


        // Set grid view to alertDialog

        builder.setContentView(gridView);
        builder.setTitle("Select Prescription");
        builder.setCancelable(true);
        builder.setCanceledOnTouchOutside(true);
        builder.show();

        /*gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("SELECTED: position: "+position+" id: "+id);
                builder.dismiss();
            }
        });*/
        HashMap<GridView, Dialog> map = new HashMap<>();
        map.put(gridView, builder);
        return map;
    }

    public ArrayList<HashMap<String, String>> refreshPrescriptionList(Context context, int patientID) {
        DbHelper dbHelper = new DbHelper(context);
        ArrayList<HashMap<String, String>> uploadsByUser = dbHelper.getPrescriptionByUserID(patientID);
        ArrayList<HashMap<String, String>> prescriptionArray = new ArrayList<>();

        for (int x = 0; x < uploadsByUser.size(); x++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", uploadsByUser.get(x).get(DbHelper.PRESCRIPTIONS_SERVER_ID));
            map.put("filename", uploadsByUser.get(x).get(DbHelper.PRESCRIPTIONS_FILENAME));
            /*prescriptionArray.add(uploadsByUser.get(x).get(DbHelper.PRESCRIPTIONS_FILENAME));*/
            prescriptionArray.add(map);
        }
        return prescriptionArray;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

    }
    public void setImage(String image_url, final ProgressBar progressBar, ImageView image_holder){
//        image_url = patient.getPhoto();
        //caching and displaying the image
        String image_url_local = "";
        if(!image_url.equals("")){
            image_url_local = Constants.UPLOAD_PATH_URL+"user_"+SidebarActivity.getUserID()+"/"+image_url;
        }

        DisplayImageOptions options;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_stub)
                .showImageForEmptyUri(R.drawable.img_holder)
                .showImageOnFail(R.mipmap.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

//        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);

        com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                .displayImage(image_url_local, image_holder, options, new SimpleImageLoadingListener() {
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
    }

    public void cacheImageOnly(String url, int user_id){
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImage(Constants.UPLOAD_PATH_URL + "user_" + user_id + "/"+url, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view,
                                          Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                Log.d("image cached", imageUri);
            }

        });
    }
}
