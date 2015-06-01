package com.example.zem.patientcareapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ContactsFragment extends Fragment {
    Spinner address_region;
    EditText unit_no, building, lot_no, block_no, phase_no, address_house_no, address_street, address_barangay, address_city_municipality,
            address_province, address_zip, email, tel_no, cell_no;
    DbHelper dbhelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.patient_contact_info, container, false);

        unit_no = (EditText) rootView.findViewById(R.id.unit_no);
        building = (EditText) rootView.findViewById(R.id.building);
        lot_no = (EditText) rootView.findViewById(R.id.lot_no);
        block_no = (EditText) rootView.findViewById(R.id.block_no);
        phase_no = (EditText) rootView.findViewById(R.id.phase_no);
        address_house_no = (EditText) rootView.findViewById(R.id.address_house_no);
        address_street = (EditText) rootView.findViewById(R.id.address_street);
        address_barangay = (EditText) rootView.findViewById(R.id.address_barangay);
        address_city_municipality = (EditText) rootView.findViewById(R.id.address_city_municipality);
        address_province = (EditText) rootView.findViewById(R.id.address_province);
        address_zip = (EditText) rootView.findViewById(R.id.address_zip);
        email = (EditText) rootView.findViewById(R.id.email);
        tel_no = (EditText) rootView.findViewById(R.id.tel_no);
        cell_no = (EditText) rootView.findViewById(R.id.cell_no);
        address_region = (Spinner) rootView.findViewById(R.id.address_region);

        ArrayAdapter<String> regions_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.regions));
        address_region.setAdapter(regions_adapter);

        int edit = EditTabsActivity.edit_int;
        dbhelper = new DbHelper(getActivity());

        if (edit > 0) {
            String edit_uname = HomeTileActivity.getUname();
            Patient patient = dbhelper.getloginPatient(edit_uname);

            if (patient.getUnit_floor_room_no() == 0) {

            } else {
                unit_no.setText("" + patient.getUnit_floor_room_no());
            }

            if (patient.getLot_no() == 0) {

            } else {
                lot_no.setText("" + patient.getLot_no());
            }

            if (patient.getBlock_no() == 0) {

            } else {
                block_no.setText("" + patient.getBlock_no());
            }

            if (patient.getPhase_no() == 0) {

            } else {
                phase_no.setText("" + patient.getPhase_no());
            }

            if (patient.getAddress_house_no() == 0) {

            } else {
                address_house_no.setText("" + patient.getAddress_house_no());
            }

            building.setText(patient.getBuilding());
            address_street.setText(patient.getAddress_street());
            address_barangay.setText(patient.getAddress_barangay());
            address_city_municipality.setText(patient.getAddress_city_municipality());
            address_province.setText(patient.getAddress_province());
            address_zip.setText(patient.getAddress_zip());
            email.setText(patient.getEmail());
            tel_no.setText(patient.getTel_no());
            cell_no.setText(patient.getMobile_no());
            address_region.setSelection(regions_adapter.getPosition(patient.getAddress_region()));
        }

        return rootView;
    }
}
