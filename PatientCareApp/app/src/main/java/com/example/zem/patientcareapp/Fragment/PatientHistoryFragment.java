package com.example.zem.patientcareapp.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.DbHelper;
import com.example.zem.patientcareapp.Helpers;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.GetRequest;
import com.example.zem.patientcareapp.Network.ListOfPatientsRequest;
import com.example.zem.patientcareapp.Network.PostRequest;
import com.example.zem.patientcareapp.PatientMedicalRecordActivity;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.ServerRequest;
import com.example.zem.patientcareapp.SidebarActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PatientHistoryFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    ListView list_of_history;
    ImageButton add_record;
    TextView noResults;

    ArrayList<HashMap<String, String>> hashHistory;
    ArrayList<String> medRecords;
    ArrayList<Integer> selectedList;
    ArrayList<String> arrayOfRecords;

    private int nr = 0;
    private SelectionAdapter mAdapter;

    public int view_record_id = 0;

    DbHelper dbHelper;
    Helpers helpers;
    Dialog dialog;
    ServerRequest serverRequest;
    ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patient_records, container, false);
        dbHelper = new DbHelper(getActivity());
        helpers = new Helpers();

        hashHistory = dbHelper.getPatientRecord(SidebarActivity.getUserID());
        medRecords = new ArrayList();
        arrayOfRecords = new ArrayList();
        selectedList = new ArrayList();
        serverRequest = new ServerRequest();

        for (int x = 0; x < hashHistory.size(); x++)
            medRecords.add(hashHistory.get(x).get(DbHelper.RECORDS_DOCTOR_NAME));

        add_record = (ImageButton) rootView.findViewById(R.id.add_record);
        noResults = (TextView) rootView.findViewById(R.id.noResults);
        list_of_history = (ListView) rootView.findViewById(R.id.list_of_history);

        if (hashHistory.size() == 0) {
            noResults.setVisibility(View.VISIBLE);
        }

        list_of_history.setOnItemClickListener(this);
        add_record.setOnClickListener(this);

        mAdapter = new SelectionAdapter(getActivity(), R.layout.listview_history_views, R.id.doctor_name, medRecords);
        list_of_history.setAdapter(mAdapter);

        list_of_history.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        list_of_history.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mAdapter.clearSelection();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                nr = 0;
                MenuInflater inflater = getActivity().getMenuInflater();
                inflater.inflate(R.menu.multiple_delete_menu, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_delete:
                        selectedList.addAll(mAdapter.getCurrentCheckedPosition());
                        final int no_of_records = selectedList.size();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setTitle("Delete?");

                        if (no_of_records == 1) {
                            dialog.setMessage(no_of_records + " record will be deleted");
                        } else if (no_of_records > 1) {
                            dialog.setMessage(no_of_records + " records will be deleted");
                        }

                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int x = (selectedList.size() - 1); x >= 0; x--) {
                                    int pos = selectedList.get(x);
                                    mAdapter.remove(pos);
                                }
                                selectedList.clear();
                                if (hashHistory.size() == 0) {
                                    noResults.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        dialog.create().show();

                        nr = 0;
                        mAdapter.clearSelection();
                        mode.finish();
                        return true;
                }
                return false;
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    nr++;
                    mAdapter.setNewSelection(position, true);
                } else {
                    nr--;
                    mAdapter.removeSelection(position);
                }
                mode.setTitle(nr + "selected");
            }
        });

        list_of_history.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                list_of_history.setItemChecked(position, !mAdapter.isPositionChecked(position));
                return false;
            }
        });

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view_record_id = Integer.parseInt(hashHistory.get(position).get(dbHelper.AI_ID));

        Intent view_record = new Intent(getActivity(), PatientMedicalRecordActivity.class);
        view_record.putExtra("viewRecord", view_record_id);
        startActivity(view_record);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record:
                dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
                dialog.setContentView(R.layout.add_new_med_records);

                ImageButton clinicRecord = (ImageButton) dialog.findViewById(R.id.clinicRecord);
                clinicRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        final Dialog dialog2 = new Dialog(getActivity());
                        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog2.setContentView(R.layout.get_clinic_record);
                        dialog2.show();

                        final EditText username = (EditText) dialog2.findViewById(R.id.username);
                        final EditText password = (EditText) dialog2.findViewById(R.id.password);
                        Button submitBtn = (Button) dialog2.findViewById(R.id.submitBtn);

                        password.setTransformationMethod(new PasswordTransformationMethod());

                        submitBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (username.getText().toString().equals(""))
                                    username.setError("Field required");
                                else if (password.getText().toString().equals(""))
                                    password.setError("Field required");
                                else {
                                    progress = new ProgressDialog(getActivity());
                                    progress.setMessage("Please wait...");
                                    progress.show();

                                    String uname = username.getText().toString();
                                    String pword = password.getText().toString();
                                    String url = "get_clinic_records&username=" + uname + "&password=" + pword + "&patient_id=" + SidebarActivity.getUserID();

                                    ListOfPatientsRequest.getJSONobj(getActivity(), url, new RespondListener<JSONObject>() {
                                        @Override
                                        public void getResult(JSONObject response) {
                                            try {
                                                int success = response.getInt("success");

                                                if (success == 1) {
                                                    JSONArray json_mysql = response.getJSONArray("records");
                                                    insertHistory(json_mysql);
                                                } else {
                                                    Toast.makeText(getActivity(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                                    progress.dismiss();
                                                }
                                            } catch (Exception e) {
                                                progress.dismiss();
                                                Toast.makeText(getActivity(), "Server error occurred", Toast.LENGTH_SHORT).show();
                                                Log.e("patientHistoryFrag0", e + "");
                                            }
                                        }
                                    }, new ErrorListener<VolleyError>() {
                                        @Override
                                        public void getError(VolleyError e) {
                                            progress.dismiss();
                                            Log.e("patientHistoryFrag1", e + "");
                                            Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                });

                final ImageButton personalRecord = (ImageButton) dialog.findViewById(R.id.personalRecord);
                personalRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PatientMedicalRecordActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                dialog.show();

                break;
        }
    }

    public void insertHistory(final JSONArray array) {
        try {
            JSONObject object = array.getJSONObject(0);

            HashMap<String, String> map = new HashMap();
            map.put("table", "patient_records");
            map.put("request", "crud");
            map.put("action", "insert");
            map.put("clinic_patient_record_id", String.valueOf(object.getInt("clinic_patients_record_id")));
            map.put("patient_id", String.valueOf(SidebarActivity.getUserID()));
            map.put("doctor_id", String.valueOf(object.getInt("doctor_id")));
            map.put("clinic_id", String.valueOf(object.getInt("clinic_id")));
            map.put("complaints", object.getString("complaints"));
            map.put("findings", object.getString("findings"));
            map.put("record_date", object.getString("record_date"));
            map.put("note", object.getString("note"));

            PostRequest.send(getActivity(), map, serverRequest, new RespondListener<JSONObject>() {
                @Override
                public void getResult(JSONObject response) {
                    try {
                        int success = response.getInt("success");

                        if (success == 1) {
                            int last_inserted_id = response.getInt("last_inserted_id");

                            ArrayList<HashMap<String, String>> arrayOfTreatments = new ArrayList();

                            JSONArray master_arr = new JSONArray();

                            for (int x = 0; x < array.length(); x++) {
                                JSONObject obj = array.getJSONObject(x);
                                HashMap<String, String> hash = new HashMap();

                                hash.put("patient_record_id", String.valueOf(last_inserted_id));
                                hash.put("medicine_id", String.valueOf(obj.getInt("medicine_id")));
                                hash.put("no_generics", String.valueOf(obj.getInt("no_generics")));
                                hash.put("quantity", String.valueOf(obj.getInt("quantity")));
                                hash.put("route", obj.getString("route"));
                                hash.put("frequency", obj.getString("frequency"));
                                hash.put("refills", String.valueOf(obj.getInt("refills")));
                                hash.put("duration", String.valueOf(obj.getInt("duration")));
                                hash.put("duration_type", String.valueOf(obj.getInt("duration_type")));

                                arrayOfTreatments.add(hash);
                                JSONObject obj_for_server = new JSONObject(hash);
                                master_arr.put(obj_for_server);
                            }

                            JSONObject json_to_be_passed = new JSONObject();
                            json_to_be_passed.put("json_treatments", master_arr);

                            HashMap<String, String> hash = new HashMap();
                            hash.put("table", "treatments");
                            hash.put("request", "crud");
                            hash.put("action", "multiple_insert");
                            hash.put("jsobj", json_to_be_passed.toString());

                            Log.d("hash", hash + "");

                            PostRequest.send(getActivity(), hash, serverRequest, new RespondListener<JSONObject>() {
                                @Override
                                public void getResult(JSONObject response) {
                                    try {
                                        int success = response.getInt("success");

                                        Log.d("response", response + "");
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), e + "", Toast.LENGTH_SHORT).show();
                                    }
                                    progress.dismiss();
                                }
                            }, new ErrorListener<VolleyError>() {
                                public void getError(VolleyError error) {
                                    progress.dismiss();
                                    Log.d("patienthistoryfrag2", error + "");
                                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else
                            Toast.makeText(getActivity(), "Server error occurred", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e + "", Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                }
            }, new ErrorListener<VolleyError>() {
                public void getError(VolleyError error) {
                    progress.dismiss();
                    Log.d("patientHistoryFrag3", error + "");
                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class SelectionAdapter extends ArrayAdapter<String> {
        private HashMap<Integer, Boolean> mSelection = new HashMap();

        public SelectionAdapter(Context context, int resource, int textViewResourceId, ArrayList<String> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        public void setNewSelection(int position, boolean value) {
            mSelection.put(position, value);
            notifyDataSetChanged();
        }

        public boolean isPositionChecked(int position) {
            Boolean result = mSelection.get(position);
            return result == null ? false : result;
        }

        public Set<Integer> getCurrentCheckedPosition() {
            return mSelection.keySet();
        }

        public void remove(int position) {
            int recordID = Integer.parseInt(hashHistory.get(position).get(dbHelper.AI_ID));
            if (dbHelper.deletePatientRecord(recordID) > 0) {
                if (dbHelper.deleteTreatmentByRecordID(recordID) > 0) {
                    medRecords.remove(position);
                    hashHistory.remove(position);
                    mAdapter.notifyDataSetChanged();
                } else
                    Toast.makeText(getActivity(), "Error occurred", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getActivity(), "Error occurred", Toast.LENGTH_SHORT).show();
        }

        public void removeSelection(int position) {
            mSelection.remove(position);
            notifyDataSetChanged();
        }


        public void clearSelection() {
            mSelection = new HashMap();
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            Drawable d = getResources().getDrawable(R.drawable.list_selector);
            v.setBackgroundDrawable(d);

            TextView record_date = (TextView) v.findViewById(R.id.record_date);
            TextView findings = (TextView) v.findViewById(R.id.findings);
            TextView doctor = (TextView) v.findViewById(R.id.doctor_name);
            ImageView recordSource = (ImageView) v.findViewById(R.id.recordSource);

            recordSource.setVisibility(View.GONE);

            doctor.setText("Dr. " + hashHistory.get(position).get(DbHelper.RECORDS_DOCTOR_NAME));
            doctor.setTag(position);
            record_date.setText(hashHistory.get(position).get(DbHelper.RECORDS_DATE));
            record_date.setTag(position);
            findings.setText(hashHistory.get(position).get(DbHelper.RECORDS_FINDINGS));
            findings.setTag(position);

            if (mSelection.get(position) != null)
                v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));

            return v;
        }
    }
}
