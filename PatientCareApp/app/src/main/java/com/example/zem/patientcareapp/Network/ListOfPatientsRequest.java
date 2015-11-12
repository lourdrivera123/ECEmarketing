package com.example.zem.patientcareapp.Network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.zem.patientcareapp.Helpers;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;

import org.json.JSONObject;

/**
 * Created by User PC on 8/15/2015.
 */
public class ListOfPatientsRequest {

    public static void getJSONobj(final Context c, final String q, final RespondListener<JSONObject> listener, final ErrorListener<VolleyError> errorlistener) {
        RequestQueue queue;
        Helpers helpers;

        queue = VolleySingleton.getInstance().getRequestQueue();
        helpers = new Helpers();

        JsonObjectRequest doctor_request = new JsonObjectRequest(Request.Method.GET, helpers.get_url(q), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                listener.getResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorlistener.getError(error);
                System.out.print("error on interface: " + error);
            }
        });
        queue.add(doctor_request);
    }

}
