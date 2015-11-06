package com.example.zem.patientcareapp;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.GetRequest;
import com.example.zem.patientcareapp.Network.ListOfPatientsRequest;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.sql.Array;
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
    ArrayList<HashMap<String, String>> ece_branches;
    ArrayList<String> listOfBranches;
    BranchesAdapter branches_adapter;

    Toolbar mytoolbar;
    ListView list_view_of_branches;
    //basics

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_map_layout);

        dbHelper = new DbHelper(getBaseContext());
        list_view_of_branches = (ListView) findViewById(R.id.list_view_of_branches);
        mytoolbar = (Toolbar) findViewById(R.id.mytoolbar);

        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select Branch");
        mytoolbar.setNavigationIcon(R.drawable.ic_back);

        list_view_of_branches.setOnItemClickListener(this);

        //start of Ravi
        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }
        //End of ravi

        //google map from yancyparedes

//        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
//
//        Marker davao = map.addMarker(new MarkerOptions().position(DAVAO).title("Davao City").snippet("STI Davao"));
//
//        // zoom in the camera to Davao city
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(DAVAO, 15));
//
//        // animate the zoom process
//        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

        //end of yancyparedes

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
//
            // before loop:
            List<Marker> markers = new ArrayList<Marker>();


            Marker marker1 = map.addMarker(new MarkerOptions().position(MY_GEOCODE).title("You are here !").snippet("Using this app"));
            Marker marker2 = map.addMarker(new MarkerOptions().position(ECE_DAVAO).title("ECE Marketing Davao").snippet("150-5th A St., Ecoland Subdivision, Matina, Davao City, 8000").snippet("(082) 297 5606"));

            markers.add(marker1);
            markers.add(marker2);


            //request for branches request
            GetRequest.getJSONobj(getBaseContext(), "get_branches", "branches", "branches_id", new RespondListener<JSONObject>() {
                @Override
                public void getResult(JSONObject response) {
                    Log.d("response using interface <SplashActivity.java - branches request >", response + "");
                    ece_branches = dbHelper.getECEBranches();

                    branches_adapter = new BranchesAdapter(getBaseContext(), ece_branches);
                    list_view_of_branches.setAdapter(branches_adapter);

                }
            }, new ErrorListener<VolleyError>() {
                public void getError(VolleyError error) {
                    Log.d("Error", error + "");
//                    Toast.makeText(getBaseContext(), "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_SHORT).show();
                    ece_branches = dbHelper.getECEBranches();
                    branches_adapter = new BranchesAdapter(getBaseContext(), ece_branches);
                    list_view_of_branches.setAdapter(branches_adapter);
                }
            });
//
//            // zoom in the camera to Davao city
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(MY_GEOCODE, 15));
//
//            // animate the zoom process
//            map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markers) {
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();
            int padding = 100; // offset from edges of the map in pixels
//            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 5,5,5);
            CameraUpdate cu1 = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            map.moveCamera(cu1);

            map.animateCamera(cu1);


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

            Toast.makeText(this, "Can't get ur location", Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(this, ProductsActivity.class);
        startActivity(intent);
    }
}
