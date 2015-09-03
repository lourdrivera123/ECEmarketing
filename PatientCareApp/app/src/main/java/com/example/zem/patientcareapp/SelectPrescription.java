//package com.example.zem.patientcareapp;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.GridView;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//
//import com.example.zem.patientcareapp.Fragment.TrialPrescriptionFragment;
//import com.example.zem.patientcareapp.adapter.ImageAdapter;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.assist.FailReason;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
//import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
///**
// * Created by Dexter B. on 8/5/2015.
// */
//public class SelectPrescription extends Activity implements View.OnClickListener {
//    GridView gridView;
//    ImageButton add_pres;
//    ImageAdapter imgAdapter;
//    DbHelper dbHelper;
//    ArrayList<HashMap<String, String>> arrayOfPrescriptions;
//    ArrayList<HashMap<String, String>> uploadsByUser;
//    int patientID;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.prescriptions_layout);
//
//        dbHelper = new DbHelper(this);
//
//        gridView = (GridView) findViewById(R.id.gridView);
//        add_pres = (ImageButton) findViewById(R.id.add_pres);
//
//        patientID = dbHelper.getCurrentLoggedInPatient().getServerID();
//        arrayOfPrescriptions = refreshPrescriptionList();
//
//        System.out.println("Array of Prescriptions: "+arrayOfPrescriptions.toString());
//        add_pres.setOnClickListener(this);
//
//        imgAdapter = new ImageAdapter(this, R.layout.item_grid_image, arrayOfPrescriptions);
//        gridView.setAdapter(imgAdapter);
//        gridView.setOnCreateContextMenuListener(this);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                startImagePagerActivity(position);
//                System.out.println("SELECTED: "+position);
//            }
//        });
//
//    }
//
//
//
//    @Override
//    public void onClick(View v) {
//        switch(v.getId()){
//            case R.id.add_pres:
//                this.finish();
//                break;
//        }
//    }
//
//    public ArrayList<HashMap<String, String>> refreshPrescriptionList() {
//        uploadsByUser = dbHelper.getPrescriptionByUserID(patientID);
//        ArrayList<String> prescriptionArray = new ArrayList();
//
//        for (int x = 0; x < uploadsByUser.size(); x++) {
//            prescriptionArray.add(uploadsByUser.get(x).get(dbHelper.PRESCRIPTIONS_FILENAME));
//        }
//        return prescriptionArray;
//    }
//
//
//}
