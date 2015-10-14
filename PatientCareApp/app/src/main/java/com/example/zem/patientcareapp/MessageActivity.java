package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.GetterSetter.Messages;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by User PC on 10/13/2015.
 */
public class MessageActivity extends Activity {
    TextView date, subject, message;

    ServerRequest serverRequest;
    DbHelper db;
    Messages msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity_layout);

        date = (TextView) findViewById(R.id.date);
        subject = (TextView) findViewById(R.id.subject);
        message = (TextView) findViewById(R.id.message);

        ActionBar actionbar = getActionBar();
        MainActivity.setCustomActionBar(actionbar);
        actionbar.setDisplayHomeAsUpEnabled(true);

        db = new DbHelper(this);
        serverRequest = new ServerRequest();
        Intent intent = getIntent();

        msg = db.getSpecificMessage(intent.getIntExtra("serverID", 0));

        if (msg.getIsRead() == 0) {
            HashMap<String, String> hashMap = new HashMap();
            hashMap.put("request", "crud");
            hashMap.put("table", "messages");
            hashMap.put("action", "update");
            hashMap.put("id", String.valueOf(msg.getServerID()));
            hashMap.put("patient_id", String.valueOf(SidebarActivity.getUserID()));
            hashMap.put("isRead", String.valueOf(1));

            final ProgressDialog pdialog = new ProgressDialog(this);
            pdialog.setCancelable(false);
            pdialog.setMessage("Loading...");
            pdialog.show();

            PostRequest.send(this, hashMap, serverRequest, new RespondListener<JSONObject>() {
                @Override
                public void getResult(JSONObject response) {
                    try {
                        int success = response.getInt("success");

                        if (success == 1) {
                            if (db.updateMessage(msg.getServerID())) {

                            } else {
                                Toast.makeText(getBaseContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getBaseContext(), "Server error occurred", Toast.LENGTH_SHORT).show();
                        System.out.print("src: MessageActivity - " + e);
                    }
                    pdialog.dismiss();
                }
            }, new ErrorListener<VolleyError>() {
                public void getError(VolleyError error) {
                    pdialog.dismiss();
                    System.out.print("src: MessageActivity Network - " + error);
                    Toast.makeText(getBaseContext(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                }
            });
        }

        date.setText(msg.getDate());
        subject.setText(msg.getSubject());
        message.setText(msg.getContent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}
