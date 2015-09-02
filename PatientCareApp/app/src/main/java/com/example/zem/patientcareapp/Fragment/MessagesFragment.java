package com.example.zem.patientcareapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zem.patientcareapp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User PC on 9/1/2015.
 */
public class MessagesFragment extends Fragment {
    ListView listOfMessages;
    MessagesAdapter adapter;

    ArrayList<HashMap<String, String>> hashOfMessages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_messages_fragment, container, false);

        listOfMessages = (ListView) v.findViewById(R.id.listOfMessages);

        hashOfMessages = new ArrayList();
        adapter = new MessagesAdapter(getActivity(), R.layout.list_item_messages_fragment, hashOfMessages);

        return v;
    }

    private class MessagesAdapter extends ArrayAdapter {
        LayoutInflater inflater;
        LinearLayout msgLayout;
        TextView msgDate, msgMessage;

        public MessagesAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
            super(context, resource, objects);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.list_item_messages_fragment, parent, false);

            msgLayout = (LinearLayout) view.findViewById(R.id.msgLayout);
            msgDate = (TextView) view.findViewById(R.id.msgDate);
            msgMessage = (TextView) view.findViewById(R.id.msgMessage);

            return view;
        }
    }
}
