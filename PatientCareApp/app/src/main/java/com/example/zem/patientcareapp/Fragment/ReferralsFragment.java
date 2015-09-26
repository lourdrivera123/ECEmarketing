package com.example.zem.patientcareapp.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.DbHelper;
import com.example.zem.patientcareapp.GetterSetter.Patient;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.ListOfPatientsRequest;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.adapter.ExpandableListViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User PC on 8/25/2015.
 */

public class ReferralsFragment extends Fragment {
    ExpandableListView listOfReferrals;
    ImageView refreshList;
    LinearLayout noReferralsLayout;
    TextView textNumber, referralName, referralDate;

    ArrayList<String> listDataHeader;
    ArrayList<String> tempDataHeader;
    HashMap<String, ArrayList<String>> listDataChild;

    ExpandableListViewAdapter adapter;
    Patient patient;
    DbHelper db;

    ProgressDialog dialog;
    View root;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_referrals_fragment, container, false);

        listOfReferrals = (ExpandableListView) root.findViewById(R.id.listOfReferrals);
        noReferralsLayout = (LinearLayout) root.findViewById(R.id.noReferralsLayout);
        refreshList = (ImageView) root.findViewById(R.id.refreshList);

        db = new DbHelper(getActivity());
        patient = db.getCurrentLoggedInPatient();
        context = getActivity();
        listDataHeader = new ArrayList();
        tempDataHeader = new ArrayList();
        listDataChild = new HashMap();

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait...");
        dialog.show();

        ListOfPatientsRequest.getJSONobj(getActivity(), "get_referrals_by_user&referred_by=" + patient.getReferral_id(), new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                try {
                    Log.d("response", response + "");
                    int success = response.getInt("success");

                    if (success == 1) {
                        listOfReferrals.setVisibility(View.VISIBLE);
                        noReferralsLayout.setVisibility(View.GONE);

                        JSONArray json_array_mysql = response.getJSONArray("patients");
                        for (int x = 0; x < json_array_mysql.length(); x++) {
                            JSONObject json_obj = json_array_mysql.getJSONObject(x);

                            String name = json_obj.getString("fname") + " " + json_obj.getString("lname");

                            listDataHeader.add(name);
                        }
                        Log.d("DataHeader1", listDataHeader + "");
                    } else {
                        listOfReferrals.setVisibility(View.GONE);
                        noReferralsLayout.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Server error occurred", Toast.LENGTH_SHORT).show();
                    System.out.print("src: <ReferralsFragment>: " + e.toString());
                }

                Log.d("DataHeader2", listDataHeader + "");
                dialog.dismiss();
            }
        }, new ErrorListener<VolleyError>() {
            public void getError(VolleyError error) {
                dialog.dismiss();
                Log.d("Error in ReferralsFragment", error + "");
                Toast.makeText(context, "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        Log.d("DataHeader", listDataHeader + "");

        adapter = new ExpandableListViewAdapter(getActivity(), listDataHeader, listDataChild);
        listOfReferrals.setAdapter(adapter);

        return root;
    }
}
