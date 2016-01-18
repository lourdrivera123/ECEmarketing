package com.example.zem.patientcareapp.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.Controllers.BranchController;
import com.example.zem.patientcareapp.Controllers.DbHelper;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Model.OrderModel;
import com.example.zem.patientcareapp.Network.GetRequest;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.adapter.BranchesAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Zem on 11/3/2015.
 */
public class GoogleMapsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemClickListener {

    //Ravi Tamada starting
    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    //End of Ravi

    //from yancy paredes declarations
    private static final LatLng DAVAO = new LatLng(7.0722, 125.6131);
    private static LatLng MY_GEOCODE, ECE_DAVAO;
    private GoogleMap map;
    //end of yancyparedes

    //basics
    DbHelper dbHelper;
    ArrayList<HashMap<String, String>> ece_branches, ece_branches_in_the_same_region;
    ArrayList<String> listOfBranches;
    BranchesAdapter branches_adapter;

    Toolbar mytoolbar;
    ListView list_view_of_branches;
    BranchController bc;

    OrderModel order_model;
    //basics

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_map_layout);

        dbHelper = new DbHelper(getBaseContext());
        bc = new BranchController(getBaseContext());

        order_model = new OrderModel();

        list_view_of_branches = (ListView) findViewById(R.id.list_view_of_branches);
        mytoolbar = (Toolbar) findViewById(R.id.mytoolbar);

        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select Branch");
        mytoolbar.setNavigationIcon(R.drawable.ic_back);

        list_view_of_branches.setOnItemClickListener(this);

        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();

        return super.onOptionsItemSelected(item);
    }

    private void setMapMarker() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            MY_GEOCODE = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//            ECE_DAVAO = new LatLng(7.051969, 125.5947593);
            ECE_DAVAO = new LatLng(7.163199, 125.577526);
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

            // before loop:
            final List<Marker> markers = new ArrayList<Marker>();
            final List<Marker> same_region_markers = new ArrayList<Marker>();


            GetRequest.getJSONobj(getBaseContext(), "google_distance_matrix&mylocation_lat=" + mLastLocation.getLatitude() + "&mylocation_long=" + mLastLocation.getLongitude(), "branches", "branches_id", new RespondListener<JSONObject>() {
                @Override
                public void getResult(JSONObject response) {
                    Log.d("googlemapactivity", response + "");
//                    ece_branches = dbHelper.getECEBranches();
                    ece_branches = bc.getECEBranchesfromjson(response, "sorted_nearest_branches");
                    ece_branches_in_the_same_region = bc.getECEBranchesfromjson(response, "branches_in_the_same_region");

                    Bitmap marker_icon = BitmapFactory.decodeResource(getResources(), R.mipmap.map_marker_ece);
                    Bitmap my_marker_icon = BitmapFactory.decodeResource(getResources(), R.mipmap.my_map_marker);

                    Marker my_location_marker = map.addMarker(new MarkerOptions().position(MY_GEOCODE).title("You are here !").icon(BitmapDescriptorFactory.fromBitmap(my_marker_icon)));
                    same_region_markers.add(my_location_marker);

                    Log.d("srm", same_region_markers.size() + "");

                    for (int i = 0; i < ece_branches.size(); i++) {
                        double lat_from_row = Double.parseDouble(ece_branches.get(i).get("latitude"));
                        double long_from_row = Double.parseDouble(ece_branches.get(i).get("longitude"));
                        int same_region = Integer.parseInt(ece_branches.get(i).get("same_region"));
                        LatLng latlong = new LatLng(lat_from_row, long_from_row);
                        Marker marker = map.addMarker(new MarkerOptions().position(latlong).title(ece_branches.get(i).get("name")).snippet(ece_branches.get(i).get("full_address")).icon(BitmapDescriptorFactory.fromBitmap(marker_icon)));

                        if (same_region == 1)
                            same_region_markers.add(marker);
                        else
                            markers.add(marker);
                    }

                    if (same_region_markers.size() == 0) {
                        same_region_markers.add(markers.get(0));
                    }

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (Marker marker : same_region_markers) {
                        builder.include(marker.getPosition());
                    }
                    LatLngBounds bounds = builder.build();
                    int padding = 100; // offset from edges of the map in pixels

                    CameraUpdate cu1 = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    map.moveCamera(cu1);
                    map.animateCamera(cu1);

                    branches_adapter = new BranchesAdapter(getBaseContext(), ece_branches);
                    list_view_of_branches.setAdapter(branches_adapter);
                }
            }, new ErrorListener<VolleyError>() {
                public void getError(VolleyError error) {
                    Log.d("googlemapsAct0", error + "");
                    ece_branches = bc.getECEBranches();
                    branches_adapter = new BranchesAdapter(getBaseContext(), ece_branches);
                    list_view_of_branches.setAdapter(branches_adapter);
                }
            });
        } else {
            Toast.makeText(this, "Can't get ur location", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method to display the location on UI
     */
    private void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            Toast.makeText(this, "Lat = " + latitude + ", Long = " + longitude, Toast.LENGTH_LONG).show();

        } else {

            Toast.makeText(this, "Can't get your location", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Once connected with google api, get the location
        displayLocation();
        setMapMarker();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int branches_selected_id = Integer.parseInt(ece_branches.get(position).get("branches_id"));
        order_model.setBranch_id(branches_selected_id);
        Intent intent = new Intent(this, ProductsActivity.class);
        intent.putExtra("order_model", order_model);
        startActivity(new Intent(this, ProductsActivity.class));
        this.finish();
    }
}
