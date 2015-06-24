package com.example.zem.patientcareapp;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.zem.patientcareapp.GetterSetter.ImageItem;
import com.example.zem.patientcareapp.ImageGallery.AppConstant;
import com.example.zem.patientcareapp.ImageGallery.ImageUtils;
import com.example.zem.patientcareapp.adapter.GridViewAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Zemie, Esel, Dexie
 */
public class PrescriptionFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    ImageButton add_pres;
    LinearLayout pick_camera_layout, pick_gallery_layout;
    ArrayList<ImageItem> imageItems;
    public static ArrayList<String> uriItems;

    private GridView gridView;
    private ImageItem item;

    Dialog dialog1;
    private GridViewAdapter gridAdapter;
    String imageFileUri;
    Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.prescriptions_layout, container, false);

        add_pres = (ImageButton) rootView.findViewById(R.id.add_pres);
        gridView = (GridView) rootView.findViewById(R.id.gridView);

        imageItems = new ArrayList();
        uriItems = new ArrayList();

        gridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, imageItems);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(this);
        add_pres.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_pres:
                dialog1 = new Dialog(getActivity());
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.dialog_gallery_camera);
                dialog1.show();

                pick_camera_layout = (LinearLayout) dialog1.findViewById(R.id.pick_camera_layout);
                pick_gallery_layout = (LinearLayout) dialog1.findViewById(R.id.pick_gallery_layout);

                pick_camera_layout.setOnClickListener(this);
                pick_gallery_layout.setOnClickListener(this);

                break;

            case R.id.pick_camera_layout:
                Intent intent_camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                ContentValues values = new ContentValues();
//                values.put(MediaStore.Images.Media.TITLE, "New Picture");
//                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
//                imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                intent_camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(getActivity())));
//                intent_camera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent_camera, 1337);
                dialog1.dismiss();
                break;

            case R.id.pick_gallery_layout:
                Intent intent_gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent_gallery.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
                startActivityForResult(intent_gallery, 111);
                dialog1.dismiss();
                break;
        }
    }

    private File getTempFile(Context context) {
        final File path = new File(Environment.getExternalStorageDirectory(), context.getPackageName());
        if (!path.exists()) {
            path.mkdir();
        }
        return new File(path, "image.tmp");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == getActivity().RESULT_OK) {
            if (data.getData() != null && !data.getData().equals(Uri.EMPTY)) {
                Uri uri = data.getData();
                String[] projection = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(projection[0]);
                String filePath = cursor.getString(columnIndex);
                uriItems.add(filePath);
                cursor.close();

                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                item = new ImageItem(yourSelectedImage, "selected");
                item.setImage(yourSelectedImage);

                imageItems.add(new ImageItem(yourSelectedImage, "selected"));
                gridAdapter.notifyDataSetChanged();

            }
        } else if (requestCode == 1337 && resultCode == getActivity().RESULT_OK) {
            final File file = getTempFile(getActivity());
            try {
                Bitmap captureBmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.fromFile(file));
                item = new ImageItem(captureBmp, "selected");
                item.setImage(captureBmp);

                imageItems.add(new ImageItem(captureBmp, "selected"));
                gridAdapter.notifyDataSetChanged();

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getActivity(), captureBmp);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
//                File finalFile = new File(getRealPathFromURI(tempUri));
                String path = String.valueOf(file);
                uriItems.add(path);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

//    public String getRealPathFromURI(Uri uri) {
//        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//        return cursor.getString(idx);
//    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(), FullScreenViewActivity.class);
        i.putExtra("position", position);
        startActivity(i);
    }
}
