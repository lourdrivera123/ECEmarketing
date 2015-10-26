package com.example.zem.patientcareapp.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.DbHelper;
import com.example.zem.patientcareapp.EditTabsActivity;
import com.example.zem.patientcareapp.GetterSetter.Patient;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.ListOfPatientsRequest;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.SidebarActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner address_region, address_province, address_city_municipality, address_barangay;
    EditText unit_no, building, lot_no, block_no, phase_no, address_house_no, address_street, email, tel_no, cell_no;
    DbHelper dbhelper;

    public static ArrayList<HashMap<String, String>> hashOfBarangays;
    public ArrayList<HashMap<String, String>> hashOfProvinces, hashOfMunicipalities, hashOfRegions;
    ArrayList<String> listOfRegions, listOfProvinces, listOfMunicipalities, listOfBarangays;
    ArrayAdapter<String> regions_adapter, provinces_adapter, municipalities_adapter, barangays_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts_fragment, container, false);

        unit_no = (EditText) rootView.findViewById(R.id.unit_no);
        building = (EditText) rootView.findViewById(R.id.building);
        lot_no = (EditText) rootView.findViewById(R.id.lot_no);
        block_no = (EditText) rootView.findViewById(R.id.block_no);
        phase_no = (EditText) rootView.findViewById(R.id.phase_no);
        address_house_no = (EditText) rootView.findViewById(R.id.address_house_no);
        address_street = (EditText) rootView.findViewById(R.id.address_street);
        email = (EditText) rootView.findViewById(R.id.email);
        tel_no = (EditText) rootView.findViewById(R.id.tel_no);
        cell_no = (EditText) rootView.findViewById(R.id.cell_no);
        address_region = (Spinner) rootView.findViewById(R.id.address_region);
        address_barangay = (Spinner) rootView.findViewById(R.id.address_barangay);
        address_city_municipality = (Spinner) rootView.findViewById(R.id.address_city_municipality);
        address_province = (Spinner) rootView.findViewById(R.id.address_province);

        hashOfRegions = new ArrayList();
        listOfRegions = new ArrayList();

        ListOfPatientsRequest.getJSONobj(getActivity(), "get_regions", new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                try {
                    JSONArray json_array_mysql = response.getJSONArray("regions");
                    for (int x = 0; x < json_array_mysql.length(); x++) {
                        JSONObject json_obj = json_array_mysql.getJSONObject(x);
                        HashMap<String, String> map = new HashMap();
                        map.put("name", json_obj.getString("name"));
                        map.put("code", json_obj.getString("code"));
                        map.put("region_server_id", json_obj.getString("id"));
                        hashOfRegions.add(map);
                    }

                    for (int y = 0; y < hashOfRegions.size(); y++) {
                        listOfRegions.add(hashOfRegions.get(y).get("name"));
                    }

                    regions_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listOfRegions);
                    address_region.setAdapter(regions_adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new ErrorListener<VolleyError>() {
            public void getError(VolleyError error) {
                System.out.print("Error in ReferralActivity" + error);
                Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

            }
        });
//        address_province.setVisibility(View.GONE);

//        address_city_municipality.setVisibility(View.GONE);
//        address_barangay.setVisibility(View.GONE);

        address_region.setOnItemSelectedListener(this);
        address_province.setOnItemSelectedListener(this);
        address_city_municipality.setOnItemSelectedListener(this);

        int edit = EditTabsActivity.edit_int;
        dbhelper = new DbHelper(getActivity());

        if (edit > 0) {
            String edit_uname = SidebarActivity.getUname();
            Patient patient = dbhelper.getloginPatient(edit_uname);

            if (patient.getUnit_floor_room_no() != 0)
                unit_no.setText("" + patient.getUnit_floor_room_no());

            if (patient.getLot_no() != 0)
                lot_no.setText("" + patient.getLot_no());

            if (patient.getBlock_no() != 0)
                block_no.setText("" + patient.getBlock_no());

            if (patient.getPhase_no() != 0)
                phase_no.setText("" + patient.getPhase_no());

            if (patient.getAddress_house_no() != 0)
                address_house_no.setText("" + patient.getAddress_house_no());

            building.setText(patient.getBuilding());
            address_street.setText(patient.getAddress_street());
            email.setText(patient.getEmail());
            tel_no.setText(patient.getTel_no());
            cell_no.setText(patient.getMobile_no());
        }

        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.address_region:
                hashOfProvinces = new ArrayList();
                listOfProvinces = new ArrayList();
                int region_server_id = Integer.parseInt(hashOfRegions.get(position).get("region_server_id"));
                ListOfPatientsRequest.getJSONobj(getActivity(), "get_provinces&region_id=" + region_server_id, new RespondListener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject response) {
                        try {
                            JSONArray json_array_mysql = response.getJSONArray("provinces");
                            for (int x = 0; x < json_array_mysql.length(); x++) {
                                JSONObject json_obj = json_array_mysql.getJSONObject(x);
                                HashMap<String, String> map = new HashMap();
                                map.put("name", json_obj.getString("name"));
                                map.put("province_server_id", json_obj.getString("id"));
                                map.put("region_server_id", json_obj.getString("region_id"));
                                hashOfProvinces.add(map);
                            }

                            for (int y = 0; y < hashOfProvinces.size(); y++) {
                                listOfProvinces.add(hashOfProvinces.get(y).get("name"));
                            }

                            provinces_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listOfProvinces);
                            address_province.setAdapter(provinces_adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new ErrorListener<VolleyError>() {
                    public void getError(VolleyError error) {
                        System.out.print("Error in ReferralActivity" + error);
                        Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                    }
                });
                break;

            case R.id.address_province:
                hashOfMunicipalities = new ArrayList();
                listOfMunicipalities = new ArrayList();
                int province_server_id = Integer.parseInt(hashOfProvinces.get(position).get("province_server_id"));
                ListOfPatientsRequest.getJSONobj(getActivity(), "get_municipalities&province_id=" + province_server_id, new RespondListener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject response) {
                        try {
                            JSONArray json_array_mysql = response.getJSONArray("municipalities");
                            for (int x = 0; x < json_array_mysql.length(); x++) {
                                JSONObject json_obj = json_array_mysql.getJSONObject(x);
                                HashMap<String, String> map = new HashMap();
                                map.put("name", json_obj.getString("name"));
                                map.put("municipality_server_id", json_obj.getString("id"));
                                map.put("province_server_id", json_obj.getString("province_id"));
                                hashOfMunicipalities.add(map);
                            }

                            for (int y = 0; y < hashOfMunicipalities.size(); y++) {
                                listOfMunicipalities.add(hashOfMunicipalities.get(y).get("name"));
                            }

                            municipalities_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listOfMunicipalities);
                            address_city_municipality.setAdapter(municipalities_adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new ErrorListener<VolleyError>() {
                    public void getError(VolleyError error) {
                        System.out.print("Error in ReferralActivity" + error);
                        Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                    }
                });
                break;

            case R.id.address_city_municipality:
                hashOfBarangays = new ArrayList();
                listOfBarangays = new ArrayList();
                int municipality_server_id = Integer.parseInt(hashOfMunicipalities.get(position).get("municipality_server_id"));
                ListOfPatientsRequest.getJSONobj(getActivity(), "get_barangays&municipality_id=" + municipality_server_id, new RespondListener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject response) {
                        try {
                            JSONArray json_array_mysql = response.getJSONArray("barangays");
                            for (int x = 0; x < json_array_mysql.length(); x++) {
                                JSONObject json_obj = json_array_mysql.getJSONObject(x);
                                HashMap<String, String> map = new HashMap();
                                map.put("name", json_obj.getString("name"));
                                map.put("barangay_server_id", json_obj.getString("id"));
                                map.put("municipality_server_id", json_obj.getString("municipality_id"));
                                hashOfBarangays.add(map);
                            }

                            for (int y = 0; y < hashOfBarangays.size(); y++) {
                                listOfBarangays.add(hashOfBarangays.get(y).get("name"));
                            }

                            barangays_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listOfBarangays);
                            address_barangay.setAdapter(barangays_adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new ErrorListener<VolleyError>() {
                    public void getError(VolleyError error) {
                        System.out.print("Error in ReferralActivity" + error);
                        Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                    }
                });
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
