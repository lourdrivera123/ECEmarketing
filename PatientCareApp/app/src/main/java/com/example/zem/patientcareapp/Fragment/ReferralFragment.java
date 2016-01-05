package com.example.zem.patientcareapp.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.GetRequest;
import com.example.zem.patientcareapp.Network.ListOfPatientsRequest;
import com.example.zem.patientcareapp.R;

import org.json.JSONObject;

/**
 * Created by User PC on 1/4/2016.
 */
public class ReferralFragment extends Fragment {
    TextView referralsLvlLimit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_referrals, container, false);

        referralsLvlLimit = (TextView) root.findViewById(R.id.referralsLvlLimit);

        return root;
    }

    public void checkForSettingsUpdate() {
        //request for referral_settings
        ListOfPatientsRequest.getJSONobj(getActivity(), "get_settings", "settings", new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {

//                referralsLvlLimit.setText("Referrals Level Limit: " + lvl + " level/s");
            }
        }, new ErrorListener<VolleyError>() {
            public void getError(VolleyError error) {
                Toast.makeText(getActivity(), "Couldn't update settings. Please check your Internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
