package com.example.zem.patientcareapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zem.patientcareapp.Fragment.HomeTileFragment;
import com.example.zem.patientcareapp.Fragment.ListOfDoctorsFragment;
import com.example.zem.patientcareapp.Fragment.MessagesFragment;
import com.example.zem.patientcareapp.Fragment.PatientHistoryFragment;
import com.example.zem.patientcareapp.Fragment.PatientProfileFragment;
import com.example.zem.patientcareapp.Fragment.PromoFragment;
import com.example.zem.patientcareapp.ImageGallery.ImageHelper;
import com.example.zem.patientcareapp.adapter.NavDrawerListAdapter;

/**
 * Created by Zem on 7/16/2015.
 */

public class SidebarActivity extends FragmentActivity {
    private String[] navMenuTitles; // slide menu items
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    ArrayList<HashMap<String, String>> hash_allProducts;
    ArrayList<String> products;

    public static String uname, pass;
    public static int userID;

    AlarmService alarmService;
    SharedPreferences.Editor editor;
    private NavDrawerListAdapter adapter;
    FragmentTransaction fragmentTransaction;
    private ActionBarDrawerToggle mDrawerToggle;
    public static SharedPreferences sharedpreferences;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ImageView img_first;
    TextView txt_uname;

    static com.example.zem.patientcareapp.GetterSetter.Patient patient;
    static DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sidebar_layout);

        sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        dbHelper = new DbHelper(this);
        hash_allProducts = dbHelper.getAllProducts();
        products = new ArrayList();

        for (int x = 0; x < hash_allProducts.size(); x++)
            products.add(hash_allProducts.get(x).get(dbHelper.PRODUCT_NAME));

        ActionBar actionbar = getActionBar();
        MainActivity.setCustomActionBar(actionbar);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);

        //////////////FOR THE SIDEBAR///////////////////////////////
        //Header of the listview, go to header.xml to customize
        View header = getLayoutInflater().inflate(R.layout.header_sidebar, null);
        img_first = (ImageView) header.findViewById(R.id.img_first);
        txt_uname = (TextView) header.findViewById(R.id.txt_uname);

        txt_uname.setText(getUname());
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.temp_user);
        img_first.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bm, 300));

        navDrawerItems = new ArrayList();

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load slide menu items
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons); // nav drawer icons from resources

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));

        navMenuIcons.recycle(); // Recycle the typed array

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.addHeaderView(header);
        mDrawerList.setAdapter(adapter);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_navigator, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            displayView(0); // on first time display view for first nav item
        }

        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        if (dbHelper.checkUserIfRegistered(getUname()) > 0) {
            // start consultation schedules reminder
//            alarmService = new AlarmService(this);
//            alarmService.patientConsultationReminder();
        } else {
            editor.clear();
            editor.commit();
            moveTaskToBack(true);
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public static String getUname() {
        uname = sharedpreferences.getString(MainActivity.name, "DEFAULT");
        return uname;
    }

    public static String getPass() {
        pass = sharedpreferences.getString(MainActivity.pass, "DEFAULT");
        return pass;
    }

    public static int getUserID() {
        patient = dbHelper.getloginPatient(getUname());
        userID = patient.getServerID();

        return userID;
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            displayView(position); // display view for selected nav drawer item
        }
    }

    @Override
    public void onBackPressed() {
        MainActivity.main.finish();
        this.finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeTileFragment();
                break;
            case 1:
                fragment = new HomeTileFragment();
                break;
            case 2:
                fragment = new PatientProfileFragment();
                break;
            case 3:
                fragment = new MessagesFragment();
                break;
            case 4:
                fragment = new PatientHistoryFragment();
                break;
            case 5:
                startActivity(new Intent(this, OrdersActivity.class));
                SidebarActivity.this.finish();
                break;
            case 6:
                fragment = new ListOfDoctorsFragment();
                break;
            case 7:
                fragment = new PromoFragment();
                break;
            case 8:
                fragment = new HomeTileFragment();
                break;
            case 9:
                editor.clear();
                editor.commit();
                startActivity(new Intent(this, MainActivity.class));
                SidebarActivity.this.finish();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else
            Log.e("SidebarAct", "Error in creating fragment");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
