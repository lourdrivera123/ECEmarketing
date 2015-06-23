package com.example.zem.patientcareapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PatientHistoryFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    ListView list_of_history;
    TextView noResults;
    ImageButton  update_record;
    Button view_doctor_btn;
    ImageButton add_record, medical_history_refresher;

    TextView date, doctor_name;
    EditText complaints, findings, treatments;

    ArrayList<HashMap<String, String>> hashHistory;
    ArrayList<HashMap<String, String>> hashTreatments;
    ArrayList<String> medRecords;
    ArrayList<Integer> temp_deleted;
    ArrayList<Integer> selectedList;
    ArrayList<String> arrayOfRecords;

    private int nr = 0;
    private SelectionAdapter mAdapter;

    public int DOCTOR_ID = 0;
    public int update_recordID = 0;

    DbHelper dbHelper;
    RoundImage roundedImage;
    Helpers helpers;
    JSONArray patient_record_json_array;

    Sync sync;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.patient_records_layout, container, false);
        dbHelper = new DbHelper(getActivity());

        hashHistory = dbHelper.getPatientRecord(HomeTileActivity.getUserID());
        medRecords = new ArrayList();
        arrayOfRecords = new ArrayList();
        selectedList = new ArrayList();
        temp_deleted = new ArrayList();

        for (int x = 0; x < hashHistory.size(); x++) {
            medRecords.add(hashHistory.get(x).get(DbHelper.RECORDS_DOCTOR_NAME));
        }

        add_record = (ImageButton) rootView.findViewById(R.id.add_record);
        noResults = (TextView) rootView.findViewById(R.id.noResults);
        list_of_history = (ListView) rootView.findViewById(R.id.list_of_history);
        medical_history_refresher = (ImageButton) rootView.findViewById(R.id.medical_history_refresher);

        if (hashHistory.size() == 0) {
            noResults.setVisibility(View.VISIBLE);
        }

        list_of_history.setOnItemClickListener(this);
        add_record.setOnClickListener(this);
        medical_history_refresher.setOnClickListener(this);

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
                inflater.inflate(R.menu.delete_menu, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_delete:
                        selectedList.addAll(mAdapter.getCurrentCheckedPosition());
                        temp_deleted.addAll(selectedList);
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
        Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Medical Records");
        dialog.setContentView(R.layout.patient_diagnosis_layout);
        dialog.show();

        view_doctor_btn = (Button) dialog.findViewById(R.id.view_doctor_btn);
        date = (TextView) dialog.findViewById(R.id.date);
        doctor_name = (TextView) dialog.findViewById(R.id.doctor_name);
        complaints = (EditText) dialog.findViewById(R.id.complaints);
        findings = (EditText) dialog.findViewById(R.id.findings);
        treatments = (EditText) dialog.findViewById(R.id.treatments);
        update_record = (ImageButton) dialog.findViewById(R.id.update_record);

        update_recordID = Integer.parseInt(hashHistory.get(position).get(dbHelper.RECORDS_ID));
        date.setText(hashHistory.get(position).get(dbHelper.RECORDS_DATE));
        complaints.setText(hashHistory.get(position).get(dbHelper.RECORDS_COMPLAINT));
        findings.setText(hashHistory.get(position).get(dbHelper.RECORDS_FINDINGS));
        doctor_name.setText(hashHistory.get(position).get(dbHelper.RECORDS_DOCTOR_NAME));
        DOCTOR_ID = Integer.parseInt(hashHistory.get(position).get(dbHelper.RECORDS_DOCTOR_ID));

        if (DOCTOR_ID == 0) {
            view_doctor_btn.setEnabled(false);
            view_doctor_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.disabled_button_bg));
        }

        hashTreatments = dbHelper.getTreatmentRecord(Integer.parseInt(hashHistory.get(position).get(dbHelper.RECORDS_ID)));

        if (hashTreatments.size() > 0) {
            for (int x = 0; x < hashTreatments.size(); x++) {
                arrayOfRecords.add(hashTreatments.get(x).get("medicine_name") + " - " + hashTreatments.get(x).get("prescription"));
            }
            treatments.setText("" + arrayOfRecords);
        }
        arrayOfRecords.clear();

        view_doctor_btn.setOnClickListener(this);
        update_record.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record:
                Intent intent = new Intent(getActivity(), PatientMedicalRecordActivity.class);
                startActivity(intent);
                getActivity().finish();

                break;
            case R.id.view_doctor_btn:
                Intent intent_doctoractivity = new Intent(getActivity(), DoctorActivity.class);
                intent_doctoractivity.putExtra(DbHelper.RECORDS_DOCTOR_ID, DOCTOR_ID);
                intent_doctoractivity.putExtra(DoctorActivity.PARENT_ACTIVITY, "PatientHistoryFragment");
                startActivity(intent_doctoractivity);
                break;

            case R.id.update_record:
                Intent update_record_intent = new Intent(getActivity(), PatientMedicalRecordActivity.class);
                update_record_intent.putExtra(PatientMedicalRecordActivity.UPDATE_RECORD_ID, update_recordID);
                startActivity(update_record_intent);
                getActivity().finish();
                break;
            case R.id.medical_history_refresher:
                Toast.makeText(getActivity(), "dapat mag refresh nako", Toast.LENGTH_SHORT).show();
                // Request a string response from the provided URL.

                JsonObjectRequest patient_record_request = new JsonObjectRequest(Request.Method.GET, helpers.get_url("get_patient_records"), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try{
                            patient_record_json_array = response.getJSONArray("patient_records");
                            sync = new Sync();

                            sync.checkWhatToInsertInMysql(dbHelper.getAllJSONArrayFrom("patient_records"), patient_record_json_array, "record_id");
                            sync.checkWhatToInsert(dbHelper.getAllJSONArrayFrom("patient_records"), patient_record_json_array, "record_id");
                            sync.checkWhatToUpdate(patient_record_json_array, "patient_records");
                            sync.checkWhatToUpdateInMysql(patient_record_json_array, "patient_records");

                            sync.init(getActivity(), "get_patient_records", "patient_records", "record_id", response);
                        } catch (Exception e ) {

                        }

                        try {
                            System.out.println("timestamp from server: "+response.getString("server_timestamp"));
                            dbHelper.updateLastUpdatedTable("patient_records", response.getString("server_timestamp"));
                        } catch (Exception e) {
                            System.out.println("error fetching server timestamp: "+ e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error on request", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
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
            int recordID = Integer.parseInt(hashHistory.get(position).get(dbHelper.RECORDS_ID));
            if (dbHelper.deletePatientRecord(recordID) > 0) {
                if (dbHelper.deleteTreatmentByRecordID(recordID) > 0) {
                    medRecords.remove(position);
                    hashHistory.remove(position);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Error occurred", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Error occurred", Toast.LENGTH_SHORT).show();
            }
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
            View v = super.getView(position, convertView, parent);//let the adapter handle setting up the row views
            Drawable d = getResources().getDrawable(R.drawable.list_selector);
            v.setBackgroundDrawable(d);

            ImageView img = (ImageView) v.findViewById(R.id.list_image);
            TextView record_date = (TextView) v.findViewById(R.id.record_date);
            TextView findings = (TextView) v.findViewById(R.id.findings);

            record_date.setText(hashHistory.get(position).get(DbHelper.RECORDS_DATE));
            record_date.setTag(position);
            findings.setText(hashHistory.get(position).get(DbHelper.RECORDS_FINDINGS));
            findings.setTag(position);

            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_app);
            roundedImage = new RoundImage(bm);
            img.setImageDrawable(roundedImage);

            if (mSelection.get(position) != null) {
                v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));// this is a selected position so make it red
            }
            return v;
        }
    }
}
