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
import android.view.WindowManager;
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
import com.example.zem.patientcareapp.Controllers.DbHelper;
import com.example.zem.patientcareapp.Controllers.PatientController;
import com.example.zem.patientcareapp.Controllers.SettingController;
import com.example.zem.patientcareapp.Model.Patient;
import com.example.zem.patientcareapp.Model.Settings;
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
    ArrayList<HashMap<String, String>> hash_myReferrals, hash_AllUsers, hash_temp, hash_hash;

    Patient patient;
    Settings settings;
    DbHelper db;
    PatientController pc;
    SettingController sc;


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
        pc = new PatientController(getActivity());
        sc = new SettingController(getActivity());
        patient = pc.getCurrentLoggedInPatient();

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

        ListOfPatientsRequest.getJSONobj(getActivity(), "get_referrals_by_user", "patients", new RespondListener<JSONObject>() {
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
                            Log.d("obj", json_obj + "");

                            String name = json_obj.get("fname") + " " + json_obj.get("lname");

                            HashMap<String, String> hash = new HashMap();
                            hash.put("serverID", String.valueOf(json_obj.getInt("id")));
                            hash.put("name", name);
                            hash.put("referral_id", json_obj.getString("referral_id"));
                            hash.put("referred_by", json_obj.getString("referred_byUser"));
                            hash_AllUsers.add(hash);

                            if (json_obj.get("referred_byUser").equals(referral_ID)) {
                                HashMap<String, String> map = new HashMap();
                                map.put("serverID", String.valueOf(json_obj.getInt("id")));
                                map.put("name", name);
                                map.put("referral_id", json_obj.getString("referral_id"));
                                map.put("referred_by", json_obj.getString("referred_byUser"));
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
                    Toast.makeText(getActivity(), e + "", Toast.LENGTH_SHORT).show();
                    System.out.print("src: <ReferralsFragment>: " + e);
                }
                dialog.dismiss();
            }
        }, new ErrorListener<VolleyError>() {
            public void getError(VolleyError error) {
                dialog.dismiss();
                System.out.print("VolleyError <ReferralsFragment>: " + error);
                Toast.makeText(getActivity(), "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_SHORT).show();
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
                settings = sc.getAllSettings();
                lvl = settings.getLvl_limit();
                referralsLvlLimit.setText("Referrals Level Limit: " + lvl + " level/s");

            }
        }, new ErrorListener<VolleyError>() {
            public void getError(VolleyError error) {
                settings = sc.getAllSettings();
                lvl = settings.getLvl_limit();
                referralsLvlLimit.setText("Referrals Level Limit: " + lvl + " level/s");

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
            String viewRef = hash_myReferrals.get(position).get("name") + " (" + hash_myReferrals.get(position).get("referral_id") + ")";
            selectedReferral.setText(viewRef);

            for (int outer = 0; outer < hash_AllUsers.size(); outer++) {
                if (hash_myReferrals.get(position).get("referral_id").equals(hash_AllUsers.get(outer).get("referred_by"))) {
                    temp_lvl = 2;

                    HashMap<String, String> map = new HashMap();
                    map.put("serverID", hash_AllUsers.get(outer).get("serverID"));
                    map.put("name", hash_AllUsers.get(outer).get("name"));
                    map.put("referral_id", hash_AllUsers.get(outer).get("referral_id"));
                    map.put("referred_by", hash_AllUsers.get(outer).get("referred_byUser"));
                    map.put("count", String.valueOf(-1));
                    hash_hash.add(map);

                    abc++;
                    listOfIndexes.add(abc);

                    hash_temp = hash_hash;

                    if (lvl > temp_lvl) {
                        for (int o = listOfIndexes.get(x); o < hash_temp.size(); o++) {
                            String referredBy = hash_temp.get(o).get("referral_id");

                            for (int inner = 0; inner < hash_AllUsers.size(); inner++) {
                                if (referredBy.equals(hash_AllUsers.get(inner).get("referred_by"))) {
                                    if (lvl != temp_lvl) {
                                        HashMap<String, String> hash = new HashMap();
                                        hash.put("serverID", hash_AllUsers.get(inner).get("serverID"));
                                        hash.put("name", hash_AllUsers.get(inner).get("name"));
                                        hash.put("referral_id", hash_AllUsers.get(inner).get("referral_id"));
                                        hash.put("referred_by", hash_AllUsers.get(inner).get("referred_byUser"));
                                        hash.put("count", String.valueOf(o));
                                        hash_hash.add(hash);

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

            if (check > 0) {
                int padding = 30;

                for (int a = 0; a < hash_hash.size(); a++) {
                    for (int b = a + 2; b < hash_hash.size(); b++) {
                        if (hash_hash.get(a).get("referral_id").equals(hash_hash.get(b).get("referred_by"))) {
                            HashMap<String, String> temp = hash_hash.get(a + 1);
                            hash_hash.set(a + 1, hash_hash.get(b));
                            hash_hash.set(b, temp);
                        }
                    }

                    TableRow row = new TableRow(getActivity());
                    TextView txt = new TextView(getActivity());
                    txt.setPadding(padding, 10, 20, 0);
                    txt.setText(hash_hash.get(a).get("name") + " (" + hash_hash.get(a).get("referral_id") + ")");
                    row.addView(txt);

                    parentLayout.addView(row);

                    if (a + 1 < hash_hash.size() && Integer.parseInt(hash_hash.get(a).get("count")) == Integer.parseInt(hash_hash.get(a + 1).get("count"))) {

                    } else {
                        padding += 30;
                    }
                }

                dialog1.show();

                //Grab the window of the dialog, and change the width
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = dialog1.getWindow();
                lp.copyFrom(window.getAttributes());

                //This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
            } else
                Toast.makeText(getActivity(), "This user has no downline", Toast.LENGTH_SHORT).show();
        }
    }
}
