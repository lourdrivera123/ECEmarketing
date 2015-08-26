package com.example.zem.patientcareapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zem.patientcareapp.R;

/**
 * Created by User PC on 8/25/2015.
 */

public class ReferralsFragment extends Fragment {
    View root;

    ListView listOfReferrals;
    EditText searchReferral;
    LinearLayout linearNumber;
    TextView textNumber, referralName, referralDate, referralTime;

    ReferralAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_referrals_fragment, container, false);

        listOfReferrals = (ListView) root.findViewById(R.id.listOfReferrals);
        searchReferral = (EditText) root.findViewById(R.id.searchReferral);

//        adapter = new ReferralAdapter(getActivity(), R.layout.list_item_referrals_fragment);
        listOfReferrals.setAdapter(adapter);

        return root;
    }

    private class ReferralAdapter extends ArrayAdapter {

        public ReferralAdapter(Context context, int resource, Object[] objects) {
            super(context, resource, objects);
        }
    }
}
