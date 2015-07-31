package com.example.zem.patientcareapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zem.patientcareapp.GetterSetter.Consultation;

import java.util.ArrayList;
import java.util.HashMap;

public class PatientConsultationFragment extends Fragment implements View.OnClickListener {
    ListView listOfConsultations;
    ImageButton add_consultation;
    TextView doctor_name, clinic_address, consultation_schedule;

    private ConsultationAdapter consultAdapter;
    Consultation consult;
    DbHelper dbhelper;

    ArrayList<HashMap<String, String>> listOfAllConsultations;
    ArrayList<String> consultationDoctors;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.patient_consultation_layout, container, false);

        listOfConsultations = (ListView) rootView.findViewById(R.id.consultation_schedules);
        add_consultation = (ImageButton) rootView.findViewById(R.id.add_consultation);

        dbhelper = new DbHelper(getActivity());
        listOfAllConsultations = dbhelper.getAllConsultationsByUserId(SidebarActivity.getUserID());
        consultationDoctors = new ArrayList();
        consult = new Consultation();

        for (int x = 0; x < listOfAllConsultations.size(); x++) {
            consultationDoctors.add(listOfAllConsultations.get(x).get(DbHelper.CONSULT_DOCTOR));
        }
        consultAdapter = new ConsultationAdapter(getActivity(), R.layout.list_row_consultations, R.id.doctor_name, consultationDoctors);
        listOfConsultations.setAdapter(consultAdapter);

        add_consultation.setOnClickListener(this);
        listOfConsultations.setOnCreateContextMenuListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), PatientConsultationActivity.class);
        intent.putExtra("request", "add");
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.cart_menus, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo menuinfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.update_cart:
                Intent intent = new Intent(getActivity(), PatientConsultationActivity.class);
                intent.putExtra("updateID", listOfAllConsultations.get(menuinfo.position).get(DbHelper.CONSULT_ID));
                intent.putExtra("request", "update");
                startActivity(intent);
                break;

            case R.id.delete_context:
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Delete?");
                dialog.setMessage("1 record will be deleted.");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int consultID = Integer.parseInt(listOfAllConsultations.get(menuinfo.position).get("id"));

                        if (dbhelper.deleteConsultation(consultID)) {
                            listOfAllConsultations.remove(menuinfo.position);
                            consultationDoctors.remove(menuinfo.position);
                            consultAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "Error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                dialog.create().show();

                break;
        }
        return super.onContextItemSelected(item);
    }

    private class ConsultationAdapter extends ArrayAdapter {

        public ConsultationAdapter(Context context, int resource, int textViewResourceId, ArrayList<String> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);//let the adapter handle setting up the row views
            Drawable d = getResources().getDrawable(R.drawable.list_selector);
            v.setBackgroundDrawable(d);

            doctor_name = (TextView) v.findViewById(R.id.doctor_name);
            clinic_address = (TextView) v.findViewById(R.id.clinic_address);
            consultation_schedule = (TextView) v.findViewById(R.id.consultation_schedule);

            doctor_name.setText("Dr. " + listOfAllConsultations.get(position).get(DbHelper.CONSULT_DOCTOR));
            clinic_address.setText(listOfAllConsultations.get(position).get(DbHelper.CONSULT_CLINIC));
            consultation_schedule.setText(listOfAllConsultations.get(position).get(DbHelper.CONSULT_DATE) + ", " + listOfAllConsultations.get(position).get(DbHelper.CONSULT_PART_OF_DAY));

            return v;
        }
    }
}
