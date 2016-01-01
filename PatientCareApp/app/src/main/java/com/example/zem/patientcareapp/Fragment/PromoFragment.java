package com.example.zem.patientcareapp.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.ListOfPatientsRequest;
import com.example.zem.patientcareapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromoFragment extends Fragment implements TextWatcher, AdapterView.OnItemClickListener {
    EditText search;
    ListView listOfPromos;
    LinearLayout root;

    ArrayList<HashMap<String, String>> map_promos = new ArrayList();

    ListViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_promos, container, false);
        search = (EditText) rootView.findViewById(R.id.search);
        listOfPromos = (ListView) rootView.findViewById(R.id.listOfPromos);
        root = (LinearLayout) rootView.findViewById(R.id.root);

        listOfPromos.setOnItemClickListener(this);
        search.addTextChangedListener(this);

        final ProgressDialog pdialog = new ProgressDialog(getActivity());
        pdialog.setMessage("Please wait...");
        pdialog.show();

        ListOfPatientsRequest.getJSONobj(getActivity(), "get_promos", "promos", new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                try {
                    int success = response.getInt("success");

                    if (success == 1) {
                        JSONArray json_mysql = response.getJSONArray("promos");

                        for (int x = 0; x < json_mysql.length(); x++) {
                            JSONObject obj = json_mysql.getJSONObject(x);
                            HashMap<String, String> map = new HashMap();

                            map.put("promo_id", obj.getString("id"));
                            map.put("promo_title", obj.getString("long_title"));
                            map.put("applicability", obj.getString("product_applicability"));
                            map.put("offer_type", obj.getString("offer_type"));
                            map.put("coupon_code", obj.getString("generic_redemption_code"));
                            map.put("minimum_purchase", obj.getString("minimum_purchase_amount"));
                            map.put("start_date", obj.getString("start_date"));
                            map.put("end_date", obj.getString("end_date"));
                            map_promos.add(map);
                        }
                    } else
                        Snackbar.make(root, "There are no promos/discount ongoing", Snackbar.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Snackbar.make(root, e + "", Snackbar.LENGTH_SHORT).show();
                }
                adapter = new ListViewAdapter(getActivity(), R.layout.item_promo_products, map_promos);
                listOfPromos.setAdapter(adapter);
                pdialog.dismiss();
            }
        }, new ErrorListener<VolleyError>() {
            @Override
            public void getError(VolleyError e) {
                pdialog.dismiss();
                Snackbar.make(root, "Please check your Internet connection", Snackbar.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private class ListViewAdapter extends ArrayAdapter {
        LayoutInflater inflater;
        TextView title, applicability, duration;

        public ListViewAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
            super(context, resource, objects);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.item_promo_products, parent, false);

            title = (TextView) view.findViewById(R.id.title);
            applicability = (TextView) view.findViewById(R.id.applicability);
            duration = (TextView) view.findViewById(R.id.duration);

            return view;
        }
    }
}
