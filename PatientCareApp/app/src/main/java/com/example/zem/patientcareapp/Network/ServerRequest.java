package com.example.zem.patientcareapp.Network;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.ConfigurationModule.Helpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Dexter B. on 5/27/2015.
 */

public class ServerRequest {
    RequestQueue queue;
    Helpers helpers;
    HashMap<String, String> params;
    String url = "http://192.168.177.1/db/post.php";

    boolean isSuccessful;
    Context activity;
    ProgressDialog pDialog;

    public ServerRequest() {

    }

    public boolean init(final Context getActivity, HashMap<String, String> parameters, JSONObject response) {
        activity = getActivity;
        queue = VolleySingleton.getInstance().getRequestQueue();
        params = parameters;
        helpers = new Helpers();
        isSuccessful = false;

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = Integer.parseInt(response.getString("success"));

                    if (params.get("table").equals("patient_prescriptions")) {
                        if (params.get("action").equals("delete")) {
                            if (success == 1) {
                                isSuccessful = true;
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
        return isSuccessful;
    }

    public boolean getResponse() {
        return this.isSuccessful;
    }
}
