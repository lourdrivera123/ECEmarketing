package com.example.zem.patientcareapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PatientHistoryFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    ListView list_of_history;
    TextView noResults;
    Button view_doctor_btn, call_doctor_btn;
    ImageButton add_record;

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

    DbHelper dbHelper;
    RoundImage roundedImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.patient_records_layout, container, false);
        dbHelper = new DbHelper(getActivity());

        hashHistory = dbHelper.getPatientRecord(HomeTileActivity.getUserID());
        medRecords = new ArrayList();
        arrayOfRecords = new ArrayList();
        selectedList = new ArrayList();
        temp_deleted = new ArrayList();

        Log.i("hash history", hashHistory + "");

        for (int x = 0; x < hashHistory.size(); x++) {
            medRecords.add(hashHistory.get(x).get("doctor_name"));
        }

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
                    mAdapter.setNewSelection(position, checked);
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
        call_doctor_btn = (Button) dialog.findViewById(R.id.call_doctor_btn);
        date = (TextView) dialog.findViewById(R.id.date);
        doctor_name = (TextView) dialog.findViewById(R.id.doctor_name);
        complaints = (EditText) dialog.findViewById(R.id.complaints);
        findings = (EditText) dialog.findViewById(R.id.findings);
        treatments = (EditText) dialog.findViewById(R.id.treatments);

        date.setText(hashHistory.get(position).get("record_date"));
        complaints.setText(hashHistory.get(position).get("complaints"));
        findings.setText(hashHistory.get(position).get("findings"));
        doctor_name.setText(hashHistory.get(position).get("doctor_name"));

        hashTreatments = dbHelper.getTreatmentRecord(Integer.parseInt(hashHistory.get(position).get("recordID")));
        if (hashTreatments.size() > 0) {
            for (int x = 0; x < hashTreatments.size(); x++) {
                arrayOfRecords.add(hashTreatments.get(x).get("medicine_name") + " - " + hashTreatments.get(x).get("prescription"));
            }
            treatments.setText("" + arrayOfRecords);
        }

        call_doctor_btn.setOnClickListener(this);
        view_doctor_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record:
                Intent intent = new Intent(getActivity(), PatientMedicalRecordActivity.class);
                startActivity(intent);

                break;
            case R.id.view_doctor_btn:
                startActivity(new Intent(getActivity(), DoctorActivity.class));
                break;

            case R.id.call_doctor_btn:
                String phoneNumber = "09095331440";
                String uriTel = "tel:" + phoneNumber;

                //Present you with the dialler
                Uri telUri = Uri.parse(uriTel);
                Intent returnIt = new Intent(Intent.ACTION_DIAL, telUri);
                startActivity(returnIt);

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
            medRecords.remove(position);
            mAdapter.notifyDataSetChanged();
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
