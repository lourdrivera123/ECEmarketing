package com.example.zem.patientcareapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.GetterSetter.Basket;
import com.example.zem.patientcareapp.Network.VolleySingleton;

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
    String url = "http://192.168.177.1/db/post.php";

    DbHelper dbHelper;
    boolean isSuccessful;
    Context activity;
    ProgressDialog pDialog;

    String successMessage = "Success!.",
            errorMessage = "Sorry, something went wrong.";

    public ServerRequest() {

    }

    public boolean init(final Context getActivity, HashMap<String, String> parameters, JSONObject response) {
        activity = getActivity;
        queue = VolleySingleton.getInstance().getRequestQueue();
        params = parameters;
        dbHelper = new DbHelper(activity);
        helpers = new Helpers();
        isSuccessful = false;

        if (helpers.isNetworkAvailable(activity)) {
            CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("response is: " + response + "\n" + " url: " + url + "\n" + " params: " + params.toString());

                    try {
                        int success = Integer.parseInt(response.getString("success"));
                        System.out.println("success is: " + success);
                        System.out.println("RESPONSE: " + response.toString());

                        // if table requested is "BASKETS"
                        if (params.get("table").equals("baskets")) {
                            if (params.get("action").equals("insert")) {
                                if (success == 1) {
                                    int insertedId = Integer.parseInt(response.getString("last_inserted_id"));

                                    Basket basket = new Basket();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date now = new Date();

                                    basket.setBasketId(insertedId);
                                    basket.setQuantity(Integer.parseInt(params.get("quantity")));
                                    basket.setProductId(Integer.parseInt(params.get("product_id")));
                                    basket.setPrescriptionId(Integer.parseInt(params.get("prescription_id")));
                                    basket.setIsApproved(Integer.parseInt(params.get("is_approved")));
                                    basket.setCreatedAt(sdf.format(now));

                                    if (dbHelper.insertBasket(basket)) {
                                        Toast.makeText(activity, successMessage, Toast.LENGTH_SHORT).show();
                                        isSuccessful = true;
                                    }

                                    if (pDialog != null) pDialog.hide();
                                }
                            } else if (params.get("action").equals("update")) {
                                if (success == 1) {
                                    isSuccessful = true;
                                }
                            } else if (params.get("action").equals("delete")) {
                                if (success == 1) {
                                    isSuccessful = true;
                                }
                            }
                        } else if (params.get("table").equals("patient_prescriptions")) {
                            if (params.get("action").equals("delete")) {
                                if (success == 1) {
                                    isSuccessful = true;
                                }
                            }
                        } else if (params.get("table").equals("referrals")) {
                            if (params.get("action").equals("insert")) {
                                if (success == 1)
                                    isSuccessful = true;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (pDialog != null) pDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("volley error ", "" + error.toString());
                    if (pDialog != null) pDialog.dismiss();
                }

            });
        }
        return isSuccessful;
    }

    public boolean getResponse() {
        return this.isSuccessful;
    }

    public void setSuccessMessage(String msg) {
        this.successMessage = msg;
    }

    public void setErrorMessage(String msg) {
        this.errorMessage = msg;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.pDialog = progressDialog;
    }
}
