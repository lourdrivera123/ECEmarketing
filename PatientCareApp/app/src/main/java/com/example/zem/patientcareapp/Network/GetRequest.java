package com.example.zem.patientcareapp.Network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.zem.patientcareapp.DbHelper;
import com.example.zem.patientcareapp.Helpers;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Sync;

import org.json.JSONObject;

/**
 * Created by Zem on 8/3/2015.
 */
public class GetRequest {

    public static void getJSONobj(final Context c, final String q, final String table_name, final String table_id, final RespondListener<JSONObject> listener, final ErrorListener<VolleyError> errorlistener) {
        RequestQueue queue;
        Helpers helpers;
        final DbHelper dbHelper;

        queue = VolleySingleton.getInstance().getRequestQueue();
        helpers = new Helpers();
        dbHelper = new DbHelper(c);

        JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.GET, helpers.get_url(q), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Sync sync = new Sync();
                sync.init(c, q, table_name, table_id, response);
                try {
                    dbHelper.updateLastUpdatedTable(table_name, response.getString("latest_updated_at"));
                } catch (Exception e) {
                    System.out.print("<GetRequest> something wrong with json: " + e);
                }
                listener.getResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorlistener.getError(error);
                Log.d("error <GetRequest> ", error + "");
            }
        });
        queue.add(jsonrequest);
    }
}
