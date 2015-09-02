package com.example.zem.patientcareapp.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zem.patientcareapp.DbHelper;
import com.example.zem.patientcareapp.GetterSetter.Patient;
import com.example.zem.patientcareapp.Helpers;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.SidebarActivity;

public class PatientProfileFragment extends Fragment {
    ImageView image_holder;
    DbHelper dbhelper;
    Helpers helpers;

    TextView patient_name, username, birthdate, civil_status, height_weight, occupation, address_first_line, address_second_line, email, cp_no;
    String ptnt_name, unit_no, building, lot_no, block_no, phase_no, house_no = "";
    String patient_uname;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patient_profile_fragment, container, false);

        helpers = new Helpers();
        dbhelper = new DbHelper(getActivity());
        image_holder = (ImageView) rootView.findViewById(R.id.image_holder);
        patient_name = (TextView) rootView.findViewById(R.id.patient_name);
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
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);


        loadData();
        return rootView;
    }

    @Override
    public void onResume() {
        loadData();
        super.onResume();
    }

    private void loadData() {

        patient_uname = SidebarActivity.getUname();
        Patient loginUser = dbhelper.getloginPatient(patient_uname);

        ptnt_name = loginUser.getFname() + " " + loginUser.getLname();
        username.setText("username: " + patient_uname);

        patient_name.setText(ptnt_name);
        birthdate.setText("Birthdate: " + loginUser.getBirthdate());
        civil_status.setText("Civil Status: " + loginUser.getCivil_status());
        height_weight.setText("Height: " + loginUser.getHeight() + " ft. / Weight: " + loginUser.getWeight() + " kg.");

        if (loginUser.getOccupation() == null || loginUser.getOccupation().equals("")) {
            occupation.setVisibility(View.GONE);
        } else {
            occupation.setText("Occupation: " + loginUser.getOccupation());
        }

        if (loginUser.getUnit_floor_room_no() == 0) {
            unit_no = "";
        } else {
            unit_no = "# " + loginUser.getUnit_floor_room_no();
        }

        if (loginUser.getBuilding() == null || loginUser.getBuilding().equals("")) {
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
            cp_no.setText(loginUser.getMobile_no());
        } else {
            cp_no.setText(loginUser.getTel_no() + " / " + loginUser.getMobile_no());
        }

        String imgFile = loginUser.getPhoto();

        if (imgFile != null || !imgFile.equals("")) {
            helpers.setImage(imgFile, progressBar, image_holder);
        }
    }
}
