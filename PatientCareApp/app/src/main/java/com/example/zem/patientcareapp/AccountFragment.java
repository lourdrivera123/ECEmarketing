package com.example.zem.patientcareapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.zem.patientcareapp.GetterSetter.Patient;

public class AccountFragment extends Fragment {
    ImageView image_holder;
    EditText username, password, confirm_password;
    Button btn_save;
    DbHelper dbhelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.patient_account_info, container, false);

        image_holder = (ImageView) rootView.findViewById(R.id.image_holder);
        username = (EditText) rootView.findViewById(R.id.username);
        password = (EditText) rootView.findViewById(R.id.password);
        confirm_password = (EditText) rootView.findViewById(R.id.confirm_password);
        btn_save = (Button) rootView.findViewById(R.id.btn_save);

        int edit = EditTabsActivity.edit_int;
        dbhelper = new DbHelper(getActivity());

        if (edit > 0) {
            String edit_uname = HomeTileActivity.getUname();
            Patient patient = dbhelper.getloginPatient(edit_uname);

            username.setText(patient.getUsername());
            password.setText(patient.getPassword());
            confirm_password.setText(patient.getPassword());

            String imgFile = patient.getPhoto();

            if (imgFile != null) {
                Bitmap yourSelectedImage = BitmapFactory.decodeFile(imgFile);
                Drawable d = new BitmapDrawable(yourSelectedImage);
                image_holder.setImageDrawable(d);
            }
        }

        return rootView;
    }

}