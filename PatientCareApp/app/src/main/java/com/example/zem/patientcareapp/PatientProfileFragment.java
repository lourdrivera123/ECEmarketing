package com.example.zem.patientcareapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class PatientProfileFragment extends Fragment {
    ImageView image_holder;
    DbHelper dbhelper;

    TextView patient_name, username, birthdate, civil_status, height_weight, occupation, address_first_line, address_second_line, email, cp_no;
    String ptnt_name, unit_no, building, lot_no, block_no, phase_no, house_no = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.patient_profile_layout, container, false);

        dbhelper = new DbHelper(getActivity());
        image_holder = (ImageView) rootView.findViewById(R.id.image_holder);
        patient_name = (TextView) rootView.findViewById(R.id.patient_name);
        username = (TextView) rootView.findViewById(R.id.username);
        birthdate = (TextView) rootView.findViewById(R.id.birthdate);
        civil_status = (TextView) rootView.findViewById(R.id.civil_status);
        height_weight = (TextView) rootView.findViewById(R.id.height_weight);
        occupation = (TextView) rootView.findViewById(R.id.occupation);
        address_first_line = (TextView) rootView.findViewById(R.id.address_first_line);
        address_second_line = (TextView) rootView.findViewById(R.id.address_second_line);
        email = (TextView) rootView.findViewById(R.id.email);
        cp_no = (TextView) rootView.findViewById(R.id.cp_no);

        String patient_uname = HomeTileActivity.getUname();
        Patient loginUser = dbhelper.getloginPatient(patient_uname);

        ptnt_name = loginUser.getFname() + " " + loginUser.getLname();

        username.setText("username: " + patient_uname);

        patient_name.setText(ptnt_name);
        birthdate.setText("Birthdate: " + loginUser.getBirthdate());
        civil_status.setText("Civil Status: " + loginUser.getCivil_status());
        height_weight.setText("Height: " + loginUser.getHeight() + " ft. / Weight: " + loginUser.getWeight() + " kg.");

        if (loginUser.getOccupation() == null) {
            occupation.setVisibility(View.GONE);
        } else {
            occupation.setText("Occupation: " + loginUser.getOccupation());
        }

        if (loginUser.getUnit_floor_room_no() == 0) {
            unit_no = "";
        } else {
            unit_no = "# " + loginUser.getUnit_floor_room_no();
        }

        if (loginUser.getBuilding() == null) {
            building = "";
        } else {
            building = loginUser.getBuilding();
        }

        if (loginUser.getLot_no() == 0) {
            lot_no = "";
        } else {
            lot_no = "Lot " + loginUser.getLot_no();
        }

        if (loginUser.getBlock_no() == 0) {
            block_no = "";
        } else {
            block_no = "Block " + loginUser.getBlock_no();
        }

        if (loginUser.getPhase_no() == 0) {
            phase_no = "";
        } else {
            phase_no = "Phase " + loginUser.getPhase_no();
        }

        if (loginUser.getAddress_house_no() == 0) {
            house_no = "";
        } else {
            house_no = "#" + loginUser.getAddress_house_no();
        }

        address_first_line.setText(unit_no + " " + building + " " + lot_no + " " + block_no + " " + phase_no + " " + house_no + " " +
                loginUser.getAddress_street() + " " + loginUser.getAddress_barangay());
        address_second_line.setText(loginUser.getAddress_city_municipality() + ", " + loginUser.getAddress_region() + ", Philippines, " +
                loginUser.getAddress_zip());

        if (loginUser.getEmail() == null || loginUser.getEmail().equals("")) {
            email.setVisibility(View.GONE);
        } else {
            email.setText(loginUser.getEmail());
        }

        if (loginUser.getTel_no() == null || loginUser.getTel_no().equals("")) {
            cp_no.setText(loginUser.getCell_no());
        } else {
            cp_no.setText(loginUser.getTel_no() + " / " + loginUser.getCell_no());
        }

        String imgFile = loginUser.getPhoto();

        if (imgFile != null) {

            Bitmap yourSelectedImage = BitmapFactory.decodeFile(imgFile);
            Drawable d = new BitmapDrawable(yourSelectedImage);
            image_holder.setImageDrawable(d);
        }

        return rootView;
    }
}
