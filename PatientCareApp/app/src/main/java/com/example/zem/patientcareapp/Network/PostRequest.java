package com.example.zem.patientcareapp.Network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.CustomRequest;
import com.example.zem.patientcareapp.DbHelper;
import com.example.zem.patientcareapp.GetterSetter.Basket;
import com.example.zem.patientcareapp.Helpers;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.ServerRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Zem on 8/4/2015.
 */
public class PostRequest {

    public static void send(final Context c, final HashMap<String, String> parameters, final String request, final ServerRequest serverRequest, final RespondListener<JSONObject> listener, final ErrorListener<VolleyError> errorlistener){
        RequestQueue queue;

        queue = VolleySingleton.getInstance().getRequestQueue();
        String url = "http://vinzry.0fees.us/db/post.php";

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("response is <PostRequest.java>: " + response);

                        serverRequest.init(c, parameters, request, response);

                        listener.getResult(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorlistener.getError(error);
                Log.d("error on interface <PostRequest.java>", error + "");
            }
        });
        queue.add(jsObjRequest);
    }
}
