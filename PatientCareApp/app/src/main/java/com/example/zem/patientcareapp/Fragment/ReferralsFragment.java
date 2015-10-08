package com.example.zem.patientcareapp.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.DbHelper;
import com.example.zem.patientcareapp.GetterSetter.Patient;
import com.example.zem.patientcareapp.GetterSetter.Settings;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.GetRequest;
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

public class ReferralsFragment extends Fragment implements AdapterView.OnItemClickListener {
    ListView listOfReferrals;
    ImageView refreshList;
    TextView referralsLvlLimit;
    LinearLayout noReferralsLayout;

    ArrayAdapter adapter;
    ArrayList<String> list_myReferrals;
    ArrayList<Integer> listOfIndexes;
    ArrayList<HashMap<String, String>> hash_myReferrals;
    ArrayList<HashMap<String, String>> hash_AllUsers;
    ArrayList<HashMap<String, String>> hash_temp;
    ArrayList<HashMap<String, String>> hash_hash;

    Patient patient;
    Settings settings;
    DbHelper db;

    ProgressDialog dialog;
    View root;

    int lvl = 0, count;
    String referral_ID = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_referrals_fragment, container, false);

        listOfReferrals = (ListView) root.findViewById(R.id.listOfReferrals);
        noReferralsLayout = (LinearLayout) root.findViewById(R.id.noReferralsLayout);
        refreshList = (ImageView) root.findViewById(R.id.refreshList);
        referralsLvlLimit = (TextView) root.findViewById(R.id.referralsLvlLimit);

        db = new DbHelper(getActivity());
        patient = db.getCurrentLoggedInPatient();
        settings = db.getAllSettings();
        lvl = settings.getLvl_limit();

        list_myReferrals = new ArrayList();
        hash_myReferrals = new ArrayList();
        hash_AllUsers = new ArrayList();
        hash_temp = new ArrayList();
        hash_hash = new ArrayList();
        listOfIndexes = new ArrayList();

        referral_ID = patient.getReferral_id();

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait...");
        dialog.show();

        checkForSettingsUpdate();
        referralsLvlLimit.setText("Referrals Level Limit: " + lvl + " level/s");

        ListOfPatientsRequest.getJSONobj(getActivity(), "get_referrals_by_user", new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                try {
                    int success = response.getInt("success");

                    if (success == 1) {
                        listOfReferrals.setVisibility(View.VISIBLE);
                        noReferralsLayout.setVisibility(View.GONE);
                        count = 0;

                        JSONArray json_array_mysql = response.getJSONArray("patients");

                        for (int x = 0; x < json_array_mysql.length(); x++) {
                            JSONObject json_obj = json_array_mysql.getJSONObject(x);
                            String name = json_obj.get("fname") + " " + json_obj.get("lname");

                            HashMap<String, String> hash = new HashMap();
                            hash.put("serverID", String.valueOf(json_obj.getInt("id")));
                            hash.put("name", name);
                            hash.put("referral_id", json_obj.getString("referral_id"));
                            hash.put("referred_by", json_obj.getString("referred_by"));
                            hash_AllUsers.add(hash);

                            if (json_obj.get("referred_by").equals(referral_ID)) {
                                HashMap<String, String> map = new HashMap();
                                map.put("serverID", String.valueOf(json_obj.getInt("id")));
                                map.put("name", name);
                                map.put("referral_id", json_obj.getString("referral_id"));
                                map.put("referred_by", json_obj.getString("referred_by"));
                                hash_myReferrals.add(map);
                                list_myReferrals.add(count, hash_myReferrals.get(count).get("name"));

                                count++;
                            }
                        }
                        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list_myReferrals);
                        listOfReferrals.setAdapter(adapter);
                    } else {
                        listOfReferrals.setVisibility(View.GONE);
                        noReferralsLayout.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Server error occurred", Toast.LENGTH_SHORT).show();
                    System.out.print("src: <ReferralsFragment>: " + e);
                }
                dialog.dismiss();
            }
        }, new ErrorListener<VolleyError>() {
            public void getError(VolleyError error) {
                dialog.dismiss();
                System.out.print("VolleyError <ReferralsFragment>: " + error);
                Toast.makeText(getActivity(), "Couldn't refresh list. Plealise check your Internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        listOfReferrals.setOnItemClickListener(this);
        return root;
    }

    public void checkForSettingsUpdate() {
        //request for referral_settings
        GetRequest.getJSONobj(getActivity(), "get_settings", "settings", "serverID", new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                settings = db.getAllSettings();
                lvl = settings.getLvl_limit();
            }
        }, new ErrorListener<VolleyError>() {
            public void getError(VolleyError error) {
                Log.d("Error", error + "");
                Toast.makeText(getActivity(), "Couldn't update settings. Please check your Internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        hash_temp.clear();
        hash_hash.clear();
        int check = 0, abc = -1, x = 0, temp_lvl;

        if (lvl > 1) {
            Dialog dialog1 = new Dialog(getActivity());
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setContentView(R.layout.view_referrals_layout);

            TextView selectedReferral = (TextView) dialog1.findViewById(R.id.selectedReferral);
            TableLayout parentLayout = (TableLayout) dialog1.findViewById(R.id.parentLayout);

            selectedReferral.setText(hash_myReferrals.get(position).get("name") + " (" + hash_myReferrals.get(position).get("referral_id") + ")");

            for (int outer = 0; outer < hash_AllUsers.size(); outer++) {
                if (hash_myReferrals.get(position).get("referral_id").equals(hash_AllUsers.get(outer).get("referred_by"))) {
                    int padding = 0;
                    temp_lvl = 2;

                    dialog1.show();
                    TableRow row = new TableRow(getActivity());
                    TextView second_lvl = new TextView(getActivity());
                    second_lvl.setText(hash_AllUsers.get(outer).get("name") + " (" + hash_AllUsers.get(outer).get("referral_id") + ")");

                    HashMap<String, String> map = new HashMap();
                    map.put("serverID", hash_AllUsers.get(outer).get("serverID"));
                    map.put("name", hash_AllUsers.get(outer).get("name"));
                    map.put("referral_id", hash_AllUsers.get(outer).get("referral_id"));
                    map.put("referred_by", hash_AllUsers.get(outer).get("referred_by"));
                    hash_hash.add(map);

                    row.addView(second_lvl);
                    parentLayout.addView(row);
                    abc++;
                    listOfIndexes.add(abc);

                    hash_temp = hash_hash;

                    if (lvl > temp_lvl) {
                        for (int o = listOfIndexes.get(x); o < hash_temp.size(); o++) {
                            String referredBy = hash_temp.get(o).get("referral_id");

                            for (int inner = 0; inner < hash_AllUsers.size(); inner++) {
                                if (referredBy.equals(hash_AllUsers.get(inner).get("referred_by"))) {
                                    if (lvl != temp_lvl) {
                                        Log.d("hash outer/o", outer + " / " + o);
                                        padding += 15;
                                        TableRow child_row = new TableRow(getActivity());
                                        TextView child_view = new TextView(getActivity());
                                        child_view.setPadding(padding, 0, 0, 0);
                                        child_view.setText(hash_AllUsers.get(inner).get("name") + " (" + hash_AllUsers.get(inner).get("referral_id") + ")");

                                        child_row.addView(child_view);
                                        parentLayout.addView(child_row);

                                        HashMap<String, String> hash = new HashMap();
                                        hash.put("serverID", hash_AllUsers.get(inner).get("serverID"));
                                        hash.put("name", hash_AllUsers.get(inner).get("name"));
                                        hash.put("referral_id", hash_AllUsers.get(inner).get("referral_id"));
                                        hash.put("referred_by", hash_AllUsers.get(inner).get("referred_by"));
                                        hash_hash.add(hash);

                                        Log.d("hash hash", hash_hash + "");
                                        abc++;
                                    }
                                }
                            }
                            temp_lvl++;
                        }
                        x++;
                    }
                    check++;
                }
            }

            if (check > 0)
                dialog1.show();
            else
                Toast.makeText(getActivity(), "This user has no downline", Toast.LENGTH_SHORT).show();
        }
    }
}
