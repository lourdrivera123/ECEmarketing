package com.example.zem.patientcareapp.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.Config;
import com.example.zem.patientcareapp.Constants;
import com.example.zem.patientcareapp.DbHelper;
import com.example.zem.patientcareapp.Helpers;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.PostRequest;
import com.example.zem.patientcareapp.ProductsActivity;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.SelectedProductActivity;
import com.example.zem.patientcareapp.ShowPrescriptionDialog;
import com.example.zem.patientcareapp.SidebarActivity;
import com.example.zem.patientcareapp.ViewPagerActivity;
import com.example.zem.patientcareapp.ServerRequest;
import com.nostra13.universalimageloader.core.*;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TrialPrescriptionFragment extends Fragment implements View.OnClickListener {
    GridView gridView;
    ImageButton add_pres;

    public static ArrayList<HashMap<String, String>> uploadsByUser;
    ArrayList<String> arrayOfPrescriptions;

    Helpers helper;
    ImageAdapter imgAdapter;
    DbHelper dbhelper;
    ServerRequest serverRequest;
    View rootView;

    int patientID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_trial_prescription_fragment, container, false);

        dbhelper = new DbHelper(getActivity());
        helper = new Helpers();

        gridView = (GridView) rootView.findViewById(R.id.gridView);
        add_pres = (ImageButton) rootView.findViewById(R.id.add_pres);

        patientID = SidebarActivity.getUserID();

        add_pres.setOnClickListener(this);

        arrayOfPrescriptions = refreshPrescriptionList();
        imgAdapter = new ImageAdapter(getActivity(), R.layout.item_grid_image, arrayOfPrescriptions);
        gridView.setAdapter(imgAdapter);
        gridView.setOnCreateContextMenuListener(this);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startImagePagerActivity(position);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        ProductsActivity.is_finish = 0;
        SelectedProductActivity.is_resumed = 0;
        arrayOfPrescriptions = refreshPrescriptionList();
        gridView.setAdapter(new ImageAdapter(getActivity(), 0, arrayOfPrescriptions));
        super.onResume();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.delete_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (item.getItemId() == R.id.delete_context) {
            AlertDialog.Builder delete = new AlertDialog.Builder(getActivity());
            delete.setTitle("Delete?");
            delete.setNegativeButton("No", null);
            delete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final int serverID = Integer.parseInt(uploadsByUser.get(menuInfo.position).get(DbHelper.PRESCRIPTIONS_SERVER_ID));
                    final String filename = uploadsByUser.get(menuInfo.position).get(DbHelper.PRESCRIPTIONS_FILENAME);
                    Log.d("filename log", filename);
                    serverRequest = new ServerRequest();
                    HashMap<String, String> hashMap = new HashMap();
                    hashMap.put("table", "patient_prescriptions");
                    hashMap.put("request", "crud");
                    hashMap.put("action", "delete_prescription");
                    hashMap.put("id", String.valueOf(serverID));
                    hashMap.put("url", "uploads/user_" + SidebarActivity.getUserID() + "/" + filename);

                    final ProgressDialog pdialog = new ProgressDialog(getActivity());
                    pdialog.setCancelable(false);
                    pdialog.setMessage("Deleting...");
                    pdialog.show();

                    PostRequest.send(getActivity(), hashMap, serverRequest, new RespondListener<JSONObject>() {
                        @Override
                        public void getResult(JSONObject response) {
                            try {
                                int success = response.getInt("success");

                                Log.d("success is", success + "");

                                if (success == 1) {
                                    if (dbhelper.deletePrescriptionByServerID(serverID)) {
                                        arrayOfPrescriptions = refreshPrescriptionList();
                                        gridView.setAdapter(new ImageAdapter(getActivity(), 0, arrayOfPrescriptions));
                                    } else {
                                        Toast.makeText(getActivity(), "Sorry, we can't delete your item right now. Please try again later.", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (success == 2) {
                                    pdialog.dismiss();
                                    Toast.makeText(getActivity(), "Cannot delete a prescription that is used for an order.", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                System.out.print("src: <TrialPrescriptionFragment>");
                                Toast.makeText(getActivity(), "Server error occurred", Toast.LENGTH_SHORT).show();
                            }
                            pdialog.dismiss();
                        }
                    }, new ErrorListener<VolleyError>() {
                        public void getError(VolleyError error) {
                            pdialog.dismiss();
                            Log.d("error sa pag delete", error + "");
                            Toast.makeText(getActivity(), "Couldn't delete item. Please check your Internet connection", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            delete.show();
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_pres:
                startActivity(new Intent(getActivity(), ShowPrescriptionDialog.class));
                break;
        }
    }

    protected void startImagePagerActivity(int position) {
        Intent intent = new Intent(getActivity(), ViewPagerActivity.class);
        intent.putExtra(Config.IMAGE_POSITION, position);
        startActivity(intent);
    }

    public ArrayList<String> refreshPrescriptionList() {
        uploadsByUser = dbhelper.getPrescriptionByUserID(patientID);
        ArrayList<String> prescriptionArray = new ArrayList();

        for (int x = 0; x < uploadsByUser.size(); x++) {
            prescriptionArray.add(uploadsByUser.get(x).get(DbHelper.PRESCRIPTIONS_FILENAME));
        }
        return prescriptionArray;
    }

    private class ImageAdapter extends ArrayAdapter<String> {
        String[] image_urls;
        private LayoutInflater inflater;
        private DisplayImageOptions options;

        public ImageAdapter(Context context, int resource, ArrayList<String> uploadsByUser) {
            super(context, R.layout.item_grid_image, uploadsByUser);

            inflater = LayoutInflater.from(context);
            image_urls = uploadsByUser.toArray(new String[uploadsByUser.size()]);

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.ic_stub)
                    .showImageForEmptyUri(R.mipmap.ic_empty)
                    .showImageOnFail(R.mipmap.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

        @Override
        public int getCount() {
            return image_urls.length;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.item_grid_image, parent, false);
            assert view != null;

            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            imageView.setTag(position);
            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
            progressBar.setTag(position);

            com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                    .displayImage(Constants.UPLOAD_PATH_URL + "user_" + SidebarActivity.getUserID() + "/" + image_urls[position], imageView, options, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            progressBar.setProgress(0);
                            progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }, new ImageLoadingProgressListener() {
                        @Override
                        public void onProgressUpdate(String imageUri, View view, int current, int total) {
                            progressBar.setProgress(Math.round(100.0f * current / total));
                        }
                    });

            return view;
        }
    }
}
