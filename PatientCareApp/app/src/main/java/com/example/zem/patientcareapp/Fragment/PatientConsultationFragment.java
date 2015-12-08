package com.example.zem.patientcareapp.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.Controllers.DbHelper;
import com.example.zem.patientcareapp.Model.Consultation;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.PostRequest;
import com.example.zem.patientcareapp.Activities.PatientConsultationActivity;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.Network.ServerRequest;
import com.example.zem.patientcareapp.SidebarModule.SidebarActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class PatientConsultationFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    ListView listOfConsultations;
    ImageButton add_consultation;
    Button accept_btn, reject_btn;
    LinearLayout acceptReject, rejected, ongoing_layout, setTime;
    TextView waiting, view_comment, declined_cancelled, doctor_name, clinic_address, consultation_schedule, time;

    private ConsultationAdapter consultAdapter;
    Consultation consult;
    DbHelper dbhelper;
    ServerRequest serverRequest;

    ArrayList<HashMap<String, String>> listOfAllConsultations;
    ArrayList<String> consultationDoctors;

    static String operation = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patient_consultation_fragment, container, false);

        listOfConsultations = (ListView) rootView.findViewById(R.id.consultation_schedules);
        add_consultation = (ImageButton) rootView.findViewById(R.id.add_consultation);

        serverRequest = new ServerRequest();

        add_consultation.setOnClickListener(this);
        listOfConsultations.setOnCreateContextMenuListener(this);
        listOfConsultations.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        dbhelper = new DbHelper(getActivity());
        listOfAllConsultations = dbhelper.getAllConsultationsByUserId(SidebarActivity.getUserID());
        consultationDoctors = new ArrayList();
        consult = new Consultation();

        consultAdapter = new ConsultationAdapter(getActivity(), R.layout.list_row_consultations, listOfAllConsultations);
        listOfConsultations.setAdapter(consultAdapter);

        super.onResume();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.remove_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int pos = menuInfo.position;

        if (listOfAllConsultations.get(pos).get("is_approved").equals("2") || listOfAllConsultations.get(pos).get("patient_is_approved").equals("2")) {
            HashMap<String, String> hashMap = new HashMap();
            hashMap.put("table", "consultations");
            hashMap.put("request", "crud");
            hashMap.put("action", "update");
            hashMap.put("id", listOfAllConsultations.get(pos).get("consultation_id"));
            hashMap.put("is_deleted", "1");

            final ProgressDialog pdialog = new ProgressDialog(getActivity());
            pdialog.setCancelable(false);
            pdialog.setMessage("Loading...");
            pdialog.show();

            PostRequest.send(getActivity(), hashMap, serverRequest, new RespondListener<JSONObject>() {
                @Override
                public void getResult(JSONObject response) {
                    try {
                        int success = response.getInt("success");

                        if (success == 1) {
                            if (dbhelper.removeFromSQLite(Integer.parseInt(listOfAllConsultations.get(pos).get("id")))) {
                                listOfAllConsultations.remove(pos);
                                consultAdapter.notifyDataSetChanged();
                            }
                        } else
                            Toast.makeText(getActivity(), "Server error occurred", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    pdialog.dismiss();
                }
            }, new ErrorListener<VolleyError>() {
                public void getError(VolleyError error) {
                    Log.d("p_consultFrag", error + "");
                    pdialog.dismiss();
                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "You are not allowed to remove this item.", Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), PatientConsultationActivity.class);
        intent.putExtra("request", "add");
        intent.putExtra("doctorID", 0);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listOfAllConsultations.get(position).get("is_approved").equals("2") && !listOfAllConsultations.get(position).get("comment_doctor").equals("")) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_doctor_annotation, null));
            alert.setPositiveButton("Ok", null);
            final AlertDialog dialog = alert.create();
            dialog.show();

            TextView doc_annotation = (TextView) dialog.findViewById(R.id.doc_annotation);
            doc_annotation.setText(listOfAllConsultations.get(position).get("comment_doctor"));
        } else if (listOfAllConsultations.get(position).get("is_approved").equals("2") && listOfAllConsultations.get(position).get("comment_doctor").equals(""))
            Toast.makeText(getActivity(), "No available comments", Toast.LENGTH_SHORT).show();
    }

    private class ConsultationAdapter extends ArrayAdapter {
        LayoutInflater inflater;

        public ConsultationAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
            super(context, resource, objects);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            View v = inflater.inflate(R.layout.list_row_consultations, parent, false);

            doctor_name = (TextView) v.findViewById(R.id.doctor_name);
            clinic_address = (TextView) v.findViewById(R.id.clinic_address);
            waiting = (TextView) v.findViewById(R.id.waiting);
            time = (TextView) v.findViewById(R.id.time);
            declined_cancelled = (TextView) v.findViewById(R.id.declined_cancelled);
            view_comment = (TextView) v.findViewById(R.id.view_comment);
            consultation_schedule = (TextView) v.findViewById(R.id.consultation_schedule);
            acceptReject = (LinearLayout) v.findViewById(R.id.acceptReject);
            setTime = (LinearLayout) v.findViewById(R.id.setTime);
            rejected = (LinearLayout) v.findViewById(R.id.rejected);
            ongoing_layout = (LinearLayout) v.findViewById(R.id.ongoing_layout);
            accept_btn = (Button) v.findViewById(R.id.accept_btn);
            reject_btn = (Button) v.findViewById(R.id.reject_btn);

            final HashMap<String, String> hashMap = new HashMap();
            hashMap.put("table", "consultations");
            hashMap.put("request", "crud");
            hashMap.put("action", "update");
            hashMap.put("id", String.valueOf(listOfAllConsultations.get(pos).get("consultation_id")));

            accept_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    operation = "accept";
                    hashMap.put("is_approved", "1");
                    hashMap.put("comment_patient", "");
                    hashMap.put("patient_is_approved", "1");
                    hashMap.put("isRead", "1");
                    sendRequest(hashMap);
                }
            });

            reject_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_add_annotation, null));
                    alert.setPositiveButton("Ok", null);
                    alert.setNegativeButton("Cancel", null);
                    final AlertDialog dialog = alert.create();
                    dialog.show();

                    final EditText annotation = (EditText) dialog.findViewById(R.id.annotation);
                    final CheckBox cancel_request = (CheckBox) dialog.findViewById(R.id.cancel_request);

                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            operation = "reject";
                            hashMap.put("isRead", "0");
                            hashMap.put("comment_patient", annotation.getText().toString());

                            if (cancel_request.isChecked()) {
                                hashMap.put("is_approved", "1");
                                hashMap.put("patient_is_approved", "2");
                            } else {
                                hashMap.put("is_approved", "0");
                                hashMap.put("patient_is_approved", "0");
                            }

                            sendRequest(hashMap);
                            dialog.dismiss();
                            onResume();
                        }
                    });
                }
            });

            try {
                Calendar cal = Calendar.getInstance();
                String dateNow = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
                String consultDate = listOfAllConsultations.get(pos).get("date");

                if (dateNow.compareTo(consultDate) > 0) {
                    waiting.setVisibility(View.GONE);
                    acceptReject.setVisibility(View.GONE);
                    ongoing_layout.setVisibility(View.GONE);
                } else {
                    if (listOfAllConsultations.get(pos).get("is_approved").equals("1")) {
                        setTime.setVisibility(View.VISIBLE);
                        waiting.setVisibility(View.GONE);
                        time.setText(listOfAllConsultations.get(pos).get("time"));

                        if (listOfAllConsultations.get(pos).get("patient_is_approved").equals("0"))
                            acceptReject.setVisibility(View.VISIBLE);
                        else if (listOfAllConsultations.get(pos).get("patient_is_approved").equals("1"))
                            ongoing_layout.setVisibility(View.VISIBLE);
                        else if (listOfAllConsultations.get(pos).get("patient_is_approved").equals("2")) {
                            setTime.setVisibility(View.GONE);
                            rejected.setVisibility(View.VISIBLE);
                            declined_cancelled.setText("Cancelled");
                            view_comment.setVisibility(View.GONE);
                        }
                    } else if (listOfAllConsultations.get(pos).get("is_approved").equals("2")) {
                        rejected.setVisibility(View.VISIBLE);
                        waiting.setVisibility(View.GONE);

                        if (listOfAllConsultations.get(pos).get("comment_doctor").equals(""))
                            view_comment.setVisibility(View.INVISIBLE);
                    } else if (listOfAllConsultations.get(pos).get("is_approved").equals("0"))
                        waiting.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {

            }

            doctor_name.setText("Dr. " + listOfAllConsultations.get(pos).get("doctor_name"));
            clinic_address.setText(listOfAllConsultations.get(pos).get("clinic_name"));
            consultation_schedule.setText(listOfAllConsultations.get(pos).get(DbHelper.CONSULT_DATE));

            return v;
        }

        private void sendRequest(final HashMap<String, String> map) {
            final ProgressDialog pdialog = new ProgressDialog(getActivity());
            pdialog.setCancelable(false);
            pdialog.setMessage("Please wait...");
            pdialog.show();

            PostRequest.send(getActivity(), map, serverRequest, new RespondListener<JSONObject>() {
                @Override
                public void getResult(JSONObject response) {
                    int success = 0;

                    try {
                        success = response.getInt("success");

                        if (success == 1) {
                            if (!dbhelper.AcceptRejectConsultation(map, operation))
                                Toast.makeText(getActivity(), "Error occurred", Toast.LENGTH_SHORT).show();
                            onResume();
                        } else
                            Toast.makeText(getActivity(), "Server error occurred", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    pdialog.dismiss();
                }
            }, new ErrorListener<VolleyError>() {
                public void getError(VolleyError error) {
                    pdialog.dismiss();
                    Log.d("consult_frag2", error + "");
                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void updateUI() {

        }
    }
}
