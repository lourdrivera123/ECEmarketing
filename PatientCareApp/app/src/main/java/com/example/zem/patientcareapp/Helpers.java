package com.example.zem.patientcareapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by User PC on 5/7/2015.
 */
public class Helpers {

    RequestQueue queue;
    boolean connection;
    Context contextc;

    public void Helpers(){

    }

    public static float dpFromPx(final int px, final Context context) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final int dp, final Context context) {
        return dp * context.getResources().getDisplayMetrics().density;
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
        return "http://192.168.10.1/db/get.php?q="+request;
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

    public String getPluralForm(String noun){
        String lastChar = "";

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

    
}
