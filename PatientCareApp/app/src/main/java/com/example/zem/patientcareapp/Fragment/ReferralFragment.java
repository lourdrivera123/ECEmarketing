package com.example.zem.patientcareapp.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.Controllers.PatientController;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Model.Patient;
import com.example.zem.patientcareapp.Network.ListOfPatientsRequest;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.SidebarModule.SidebarActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by User PC on 1/4/2016.
 */

public class ReferralFragment extends Fragment {
    TextView referralsLvlLimit;
    TableLayout table_parent;
    LinearLayout parent;
    LayoutInflater inflater;

    Patient ptnt;
    PatientController pc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_referrals, container, false);
        this.inflater = inflater;

        referralsLvlLimit = (TextView) root.findViewById(R.id.referralsLvlLimit);
        parent = (LinearLayout) root.findViewById(R.id.parent);
        table_parent = (TableLayout) root.findViewById(R.id.table_parent);

        pc = new PatientController(getActivity());
        ptnt = pc.getCurrentLoggedInPatient();

        checkForSettingsUpdate();
        addTableRow();

        return root;
    }

    public void checkForSettingsUpdate() {
        ListOfPatientsRequest.getJSONobj(getActivity(), "get_settings", "settings", new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                try {
                    JSONArray json_mysql = response.getJSONArray("settings");
                    JSONObject object = json_mysql.getJSONObject(0);
                    int limit = object.getInt("level_limit");
                    referralsLvlLimit.setText("Referrals Level Limit: " + limit + " level/s");
                } catch (JSONException e) {
                    Log.d("exception1", e + "");
                    Snackbar.make(parent, "Error occurred", Snackbar.LENGTH_SHORT).show();
                }
            }
        }, new ErrorListener<VolleyError>() {
            public void getError(VolleyError error) {
                Snackbar.make(parent, "Network Error", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void addTableRow() {
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Please wait...");
        progress.show();

        Log.d("referral", ptnt.getReferral_id() + "");

        ListOfPatientsRequest.getJSONobj(getActivity(), "get_patients", "patients", new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                String referral_ID = null;

                try {
                    JSONArray json_mysql = response.getJSONArray("patients");

                    for (int x = 0; x < json_mysql.length(); x++) {
                        JSONObject obj = json_mysql.getJSONObject(x);

                    }
                } catch (Exception e) {
                    Log.d("exception2", e + "");
                    Snackbar.make(parent, "Error occurred", Snackbar.LENGTH_SHORT).show();
                }

                progress.dismiss();
            }
        }, new ErrorListener<VolleyError>() {
            public void getError(VolleyError error) {
                progress.dismiss();
                Snackbar.make(parent, "Network Error", Snackbar.LENGTH_SHORT).show();
            }
        });

        TableRow tr = new TableRow(getActivity());
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        for (int x = 0; x < 5; x++) {
            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER);
            txt.setBackgroundDrawable(getResources().getDrawable(R.drawable.cell_border));
            txt.setText(x + "");
            tr.addView(txt);
        }

        table_parent.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }
}
