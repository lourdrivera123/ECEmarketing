package com.example.zem.patientcareapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AccountFragment extends Fragment implements View.OnClickListener {

    Button choose_image_btn;
    ImageView image_holder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.patient_account_info, container, false);

        choose_image_btn = (Button) rootView.findViewById(R.id.choose_image_btn);
        image_holder = (ImageView) rootView.findViewById(R.id.image_holder);

        choose_image_btn.setOnClickListener(this);

        return rootView;
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.choose_image_btn:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 111);
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            Uri uri = data.getData();
            String[] projection = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
            Drawable d = new BitmapDrawable(yourSelectedImage);
            image_holder.setImageDrawable(d);
        }
    }

}