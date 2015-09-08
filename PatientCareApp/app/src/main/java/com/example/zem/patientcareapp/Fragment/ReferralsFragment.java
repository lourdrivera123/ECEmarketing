package com.example.zem.patientcareapp.Fragment;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User PC on 8/25/2015.
 */

public class ReferralsFragment extends Fragment implements TextWatcher, View.OnClickListener {
    ListView listOfReferrals;
    EditText searchReferral;
    ImageView refreshList;
    LinearLayout noReferralsLayout;
    TextView textNumber, referralName, referralDate;

    ArrayList<HashMap<String, String>> hashOfReferrals;
    ArrayList<HashMap<String, String>> tempHashOfReferrals;
    ArrayList<String> arrayOfReferrals;

    ReferralAdapter adapter;
    Patient patient;
    DbHelper db;
    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_referrals_fragment, container, false);

        listOfReferrals = (ListView) root.findViewById(R.id.listOfReferrals);
        searchReferral = (EditText) root.findViewById(R.id.searchReferral);
        noReferralsLayout = (LinearLayout) root.findViewById(R.id.noReferralsLayout);
        refreshList = (ImageView) root.findViewById(R.id.refreshList);

        db = new DbHelper(getActivity());
        patient = db.getCurrentLoggedInPatient();
        hashOfReferrals = new ArrayList();
        arrayOfReferrals = new ArrayList();
        tempHashOfReferrals = new ArrayList();

        ListOfPatientsRequest.getJSONobj(getActivity(), "get_referrals_by_user&referred_by=" + patient.getReferral_id(), new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                try {
                    int success = response.getInt("success");

                    if (success == 1) {
                        listOfReferrals.setVisibility(View.VISIBLE);
                        noReferralsLayout.setVisibility(View.GONE);

                        JSONArray json_array_mysql = response.getJSONArray("patients");
                        for (int x = 0; x < json_array_mysql.length(); x++) {
                            try {
                                JSONObject json_obj = json_array_mysql.getJSONObject(x);
                                HashMap<String, String> map = new HashMap();
                                map.put("fullname", json_obj.getString("fname") + " " + json_obj.getString("lname"));
                                map.put("created_at", json_obj.getString("created_at"));
                                hashOfReferrals.add(map);
                                arrayOfReferrals.add(hashOfReferrals.get(x).get("fullname"));
                            } catch (JSONException e) {
                                System.out.print("<ReferralsFragment>: " + e.toString());
                            }
                        }

                        adapter = new ReferralAdapter(getActivity(), R.layout.list_item_referrals_fragment, hashOfReferrals);
                        listOfReferrals.setAdapter(adapter);
                        tempHashOfReferrals.addAll(hashOfReferrals);
                    } else {
                        listOfReferrals.setVisibility(View.GONE);
                        noReferralsLayout.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Server error occurred", Toast.LENGTH_SHORT).show();
                    System.out.print("src: <ReferralsFragment>: " + e.toString());
                }
            }
        }, new ErrorListener<VolleyError>() {
            public void getError(VolleyError error) {
                Log.d("Error in ReferralsFragment", error + "");
                Toast.makeText(getActivity(), "Couldn't refresh list. Plealise check your Internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        listOfReferrals.setAdapter(adapter);
        searchReferral.addTextChangedListener(this);
        refreshList.setOnClickListener(this);

        return root;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String s_referral = searchReferral.getText().toString();
        hashOfReferrals.clear();

        for (String name : arrayOfReferrals) {
            if (name.toLowerCase().contains(s_referral.toLowerCase())) {
                int index = arrayOfReferrals.indexOf(name);

                HashMap<String, String> map = new HashMap();

                map.put("fullname", tempHashOfReferrals.get(index).get("fullname"));
                map.put("created_at", tempHashOfReferrals.get(index).get("created_at"));
                hashOfReferrals.add(map);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onClick(View v) {

    }

    private class ReferralAdapter extends ArrayAdapter {
        LayoutInflater inflater;

        public ReferralAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
            super(context, resource, objects);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = inflater.inflate(R.layout.list_item_referrals_fragment, parent, false);

            textNumber = (TextView) v.findViewById(R.id.textNumber);
            referralName = (TextView) v.findViewById(R.id.referralName);
            referralDate = (TextView) v.findViewById(R.id.referralDate);
            int y;
            int[] colors = getActivity().getResources().getIntArray(R.array.bg_colors);

            for (int x = 0; x < hashOfReferrals.size(); x++) {
                y = position + 1;
                textNumber.setText("" + y);
                referralName.setText(hashOfReferrals.get(position).get("fullname"));
                referralDate.setText(hashOfReferrals.get(position).get("created_at"));
                textNumber.setBackgroundColor(colors[position]);
            }

            return v;
        }
    }
}
