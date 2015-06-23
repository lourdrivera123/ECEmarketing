package com.example.zem.patientcareapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
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
import com.example.zem.patientcareapp.adapter.GridViewAdapter;

import java.util.ArrayList;

/**
 * Created by Zemie, Esel, Dexie
 */
public class PrescriptionFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    ImageButton add_pres;
    LinearLayout pick_camera_layout, pick_gallery_layout;
    ArrayList<ImageItem> imageItems;

    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private ImageItem item;

    Dialog dialog1;
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.prescriptions_layout, container, false);

        add_pres = (ImageButton) rootView.findViewById(R.id.add_pres);
        gridView = (GridView) rootView.findViewById(R.id.gridView);

        imageItems = new ArrayList();
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
                startActivityForResult(intent_camera, 1337);
                dialog1.dismiss();
                break;

            case R.id.pick_gallery_layout:
                Intent intent_gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent_gallery, 111);
                dialog1.dismiss();
                break;
        }
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
                cursor.close();

                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                item = new ImageItem(yourSelectedImage, "selected");
                item.setImage(yourSelectedImage);

                imageItems.add(new ImageItem(yourSelectedImage, "selected"));
                gridAdapter.notifyDataSetChanged();

            }
        } else if (requestCode == 1337 && resultCode == getActivity().RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            item = new ImageItem(photo, "selected");
            item.setImage(photo);

            imageItems.add(new ImageItem(photo, "selected"));
            gridAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        ImageItem item = (ImageItem) parent.getItemAtPosition(position);
//        String bitmapImage = String.valueOf(item.getImage());
//
//        Intent intent = new Intent(getActivity(), DetailsActivity.class);
//        intent.putExtra("image", bitmapImage);
//        startActivity(intent);
    }
}
