package com.example.zem.patientcareapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Dexter B. on 5/27/2015.
 */
public class ServerRequest {
    RequestQueue queue;
    Helpers helpers;
    HashMap<String, String> params;
//    String url = "http://vinzry.0fees.us/db/post.php";
    String url = "http://192.168.1.12/db/post.php";
    DbHelper dbHelper;
    boolean isSuccessful;
    Context activity;
    String request, updateSource;
    ProgressDialog pDialog;

    String  successMessage = "Success!.",
            errorMessage = "Sorry, something went wrong.";

    public ServerRequest() {

    }

    public boolean init(final Context getActivity, HashMap<String, String> parameters, String request) {
        activity = getActivity;
        queue = Volley.newRequestQueue(activity);
        params = parameters;
        dbHelper = new DbHelper(activity);
        helpers = new Helpers();
        isSuccessful = false;
        this.request = request;
        if (helpers.isNetworkAvailable(activity)) {


            System.out.print("FUCKING PARAMS: " + params.toString());

                /* This is where we get the results */
            if( pDialog != null ){
                pDialog.setMessage("Remember: Patience is a Virtue. So please wait while we save your information");
                pDialog.show();
            }
            CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("response is: " + response);

                            try {
                                int success = Integer.parseInt(response.getString("success"));
                                System.out.println("success is: " + success);
                                System.out.println("RESPONSE: "+response.toString());

                                    // saving to sqlite database
                                   if( params.get("table").equals("baskets") ){

                                       if( params.get("action").equals("insert") ){
                                           if(success == 1){
                                               int insertedId = Integer.parseInt(response.getString("last_inserted_id"));

                                               System.out.println("insertedId is :"+insertedId);

                                               Basket basket = new Basket();
                                               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                               Date now = new Date();

                                               basket.setBasketId(insertedId);
                                               basket.setQuantity(Double.parseDouble(params.get("quantity")));
                                               basket.setProductId(Integer.parseInt(params.get("product_id")));
                                               basket.setCreatedAt(sdf.format(now));



                                               if( dbHelper.insertBasket(basket) ){
                                                   Toast.makeText(activity, successMessage, Toast.LENGTH_SHORT).show();
                                                   isSuccessful = true;
                                               }else{
                                                   Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
                                               }
                                               if( pDialog != null ) pDialog.hide();
                                           }else{
                                               Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
                                           }
                                       }else if( params.get("action").equals("update") ){
                                           if( success == 1 ){
                                               isSuccessful = true;
                                               Toast.makeText(activity, successMessage, Toast.LENGTH_SHORT).show();
                                           }else if( success == 0 ){
                                               Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
                                           }
                                       }else if( params.get("action").equals("delete") ){
                                           if( success == 1 ){
                                               isSuccessful = true;
                                               Toast.makeText(activity, successMessage, Toast.LENGTH_SHORT).show();
                                           }else if( success == 0 ){
                                               Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                if( pDialog != null ) pDialog.hide();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("volley error ", "" + error.toString());
                    System.out.println("error is: " + error);
                    if( pDialog != null ) pDialog.hide();
                }
            });


            queue.add(jsObjRequest);
        }
        return isSuccessful;
    }

    public RequestQueue getQueue(){
        return this.queue;
    }

    public void setProgressDialog(ProgressDialog progressDialog){
        this.pDialog = progressDialog;
    }

    public void setSuccessMessage(String msg){
        this.successMessage = msg;
    }

    public void setErrorMessage(String msg){
        this.errorMessage = msg;
    }
}
