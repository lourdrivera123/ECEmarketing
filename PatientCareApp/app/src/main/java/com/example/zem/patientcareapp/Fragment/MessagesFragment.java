package com.example.zem.patientcareapp.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.DbHelper;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.MessageActivity;
import com.example.zem.patientcareapp.Network.GetRequest;
import com.example.zem.patientcareapp.Network.PostRequest;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.ServerRequest;
import com.example.zem.patientcareapp.SidebarActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by User PC on 9/1/2015.
 */
public class MessagesFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.MultiChoiceModeListener, AdapterView.OnItemLongClickListener {
    ListView listOfMessages;
    TextView noMsgs;

    private MessagesAdapter adapter;
    ServerRequest serverRequest;
    DbHelper db;

    ProgressDialog dialog;

    ArrayList<HashMap<String, String>> hashOfMessages;
    ArrayList<Integer> listOfCheckedPositions;

    int nr = 0, patient_id;
    String ids = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_messages_fragment, container, false);

        listOfMessages = (ListView) v.findViewById(R.id.listOfMessages);
        noMsgs = (TextView) v.findViewById(R.id.noMsgs);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait...");
        dialog.show();

        hashOfMessages = new ArrayList();
        listOfCheckedPositions = new ArrayList();
        patient_id = SidebarActivity.getUserID();
        serverRequest = new ServerRequest();
        db = new DbHelper(getActivity());

        listOfMessages.setOnItemClickListener(this);
        listOfMessages.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listOfMessages.setMultiChoiceModeListener(this);
        listOfMessages.setOnItemLongClickListener(this);

        return v;
    }

    @Override
    public void onResume() {
        GetRequest.getJSONobj(getActivity(), "get_messages_by_user&patient_id=" + patient_id, "messages", "serverID", new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                try {
                    int success = response.getInt("success");

                    if (success == 1) {
                        hashOfMessages = new ArrayList();
                        noMsgs.setVisibility(View.GONE);
                        JSONArray json_array_mysql = response.getJSONArray("messages");

                        for (int x = 0; x < json_array_mysql.length(); x++) {
                            JSONObject obj = json_array_mysql.getJSONObject(x);

                            HashMap<String, String> map = new HashMap();
                            map.put("subject", obj.getString("subject"));
                            map.put("content", obj.getString("content"));
                            map.put("created_at", obj.getString("created_at"));
                            map.put("isRead", String.valueOf(obj.getInt("isRead")));
                            map.put("serverID", String.valueOf(obj.getInt("id")));
                            hashOfMessages.add(map);
                        }

                        adapter = new MessagesAdapter(getActivity(), R.layout.list_item_messages_fragment, hashOfMessages);
                        listOfMessages.setAdapter(adapter);
                    } else {
                        noMsgs.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Server error occurred", Toast.LENGTH_SHORT).show();
                    System.out.print("src: <MessagesFragment>: " + e);
                }
                dialog.dismiss();
            }
        }, new ErrorListener<VolleyError>() {
            public void getError(VolleyError error) {
                hashOfMessages = db.getAllMessages(patient_id);

                adapter = new MessagesAdapter(getActivity(), R.layout.list_item_messages_fragment, hashOfMessages);
                listOfMessages.setAdapter(adapter);

                dialog.dismiss();
                System.out.print("VolleyError <ReferralsFragment>: " + error);
                Toast.makeText(getActivity(), "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        super.onResume();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.delete_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int serverID = Integer.parseInt(hashOfMessages.get(position).get("serverID"));

        Intent intent = new Intent(getActivity(), MessageActivity.class);
        intent.putExtra("serverID", serverID);
        startActivity(intent);
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        if (checked) {
            nr++;
            adapter.setNewSelection(position, true);
        } else {
            nr--;
            adapter.removeSelection(position);
        }
        mode.setTitle(nr + "selected");
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        nr = 0;
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.multiple_delete_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        adapter.clearSelection();
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        listOfCheckedPositions.addAll(adapter.getCurrentCheckedPosition());

        final int no_of_records = listOfCheckedPositions.size();
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Delete?");

        if (no_of_records == 1) {
            dialog.setMessage(no_of_records + " message will be deleted");
        } else if (no_of_records > 1) {
            dialog.setMessage(no_of_records + " messages will be deleted");
        }

        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int x = (listOfCheckedPositions.size() - 1); x >= 0; x--) {
                    int pos = listOfCheckedPositions.get(x);
                    ids = ids + hashOfMessages.get(pos).get("serverID") + ",";
                }

                HashMap<String, String> hashMap = new HashMap();
                hashMap.put("table", "messages");
                hashMap.put("request", "crud");
                hashMap.put("action", "multiple_delete");
                hashMap.put("serverID", ids.substring(0, ids.length() - 1));

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
                                for (int x = (listOfCheckedPositions.size() - 1); x >= 0; x--) {
                                    int pos = listOfCheckedPositions.get(x);
                                    adapter.remove(pos);
                                }

                                listOfCheckedPositions.clear();

                                if (hashOfMessages.size() == 0) {
                                    noMsgs.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Server error occurred", Toast.LENGTH_SHORT).show();
                            System.out.print("src: MessageFragment CATCH - " + e);
                        }
                        pdialog.dismiss();
                    }
                }, new ErrorListener<VolleyError>() {
                    public void getError(VolleyError error) {
                        Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                        Log.d("error", error + "");
                        pdialog.dismiss();
                    }
                });
            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        dialog.create().show();

        nr = 0;
        adapter.clearSelection();
        mode.finish();

        return false;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        listOfMessages.setItemChecked(position, !adapter.isPositionChecked(position));
        return false;
    }

    private class MessagesAdapter extends ArrayAdapter {
        LayoutInflater inflater;
        LinearLayout msgLayout;
        TextView msgDate, msgMessage, subject;

        private HashMap<Integer, Boolean> mSelection = new HashMap();

        public MessagesAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
            super(context, resource, objects);
            inflater = LayoutInflater.from(context);
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
            int serverID = Integer.parseInt(hashOfMessages.get(position).get(db.MSGS_SERVER_ID));

            if (db.deleteSpecificMessage(serverID)) {
                hashOfMessages.remove(position);
                adapter.notifyDataSetChanged();
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
            adapter.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.list_item_messages_fragment, parent, false);

            msgLayout = (LinearLayout) view.findViewById(R.id.msgLayout);
            msgDate = (TextView) view.findViewById(R.id.msgDate);
            msgMessage = (TextView) view.findViewById(R.id.msgMessage);
            subject = (TextView) view.findViewById(R.id.subject);

            String date = hashOfMessages.get(position).get("created_at");
            String content = hashOfMessages.get(position).get("content");

            if (Integer.parseInt(hashOfMessages.get(position).get("isRead")) == 0)
                msgLayout.setBackgroundColor(Color.parseColor("#d3d3d3"));

            if (content.length() >= 30)
                msgMessage.setText(hashOfMessages.get(position).get("content").substring(0, 30) + "...");
            else
                msgMessage.setText(hashOfMessages.get(position).get("content"));


            msgDate.setText(date.substring(0, 10));
            subject.setText(hashOfMessages.get(position).get("subject"));

            if (mSelection.get(position) != null) {
                view.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
            }

            return view;
        }
    }
}
