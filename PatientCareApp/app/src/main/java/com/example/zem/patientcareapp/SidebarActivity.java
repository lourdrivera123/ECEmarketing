package com.example.zem.patientcareapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.example.zem.patientcareapp.Fragment.HomeTileFragment;
import com.example.zem.patientcareapp.Fragment.ReferralsFragment;

/**
 * Created by Zem on 7/16/2015.
 */

public class SidebarActivity extends FragmentActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    //from hometile
    public static SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    ArrayAdapter search_adapter;
    ArrayList<HashMap<String, String>> hash_allProducts;
    ArrayList<String> products;

    FragmentTransaction fragmentTransaction;

    AutoCompleteTextView search_product;
    AlarmService alarmService;

    int productID = 0;

    static com.example.zem.patientcareapp.GetterSetter.Patient patient;
    static DbHelper dbHelper;

    public static String uname;
    static String pass;
    public static int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sidebar_layout);

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList();

        // adding nav drawer items to array
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], 0));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], 0));

        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_navigator, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }

        dbHelper = new DbHelper(this);
        hash_allProducts = dbHelper.getAllProducts();
        products = new ArrayList();

        for (int x = 0; x < hash_allProducts.size(); x++) {
            products.add(hash_allProducts.get(x).get(dbHelper.PRODUCT_NAME));
        }

        ActionBar actionbar = getActionBar();
        MainActivity.setCustomActionBar(actionbar);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);

        sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        createCustomSearchBar(actionbar);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        if (dbHelper.checkUserIfRegistered(getUname()) > 0) {
            // start consultation schedules reminder
            alarmService = new AlarmService(this);
            alarmService.patientConsultationReminder();
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

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public void onBackPressed() {
        MainActivity.main.finish();
        this.finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.logout:
                editor.clear();
                editor.commit();
                startActivity(new Intent(this, MainActivity.class));
                this.finish();
                break;
        }
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
                fragment = new ReferralsFragment();
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
        } else {
            Log.e("SidebarActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
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

    public void createCustomSearchBar(ActionBar actionbar) {
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.searchview_layout, null);

        search_product = (AutoCompleteTextView) mCustomView.findViewById(R.id.search_product);
        search_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, products);
        search_product.setAdapter(search_adapter);

        search_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_product.setCursorVisible(true);
                search_product.setFocusableInTouchMode(true);
                search_product.setFocusable(true);
            }
        });

        search_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item_clicked = parent.getItemAtPosition(position).toString();
                int itemID = products.indexOf(item_clicked);
                productID = Integer.parseInt(hash_allProducts.get(itemID).get(dbHelper.SERVER_PRODUCT_ID));

                Intent intent = new Intent(getBaseContext(), SelectedProductActivity.class);
                intent.putExtra(SelectedProductActivity.PRODUCT_ID, productID);
                intent.putExtra(SelectedProductActivity.UP_ACTIVITY, "HomeTile");
                startActivity(intent);
            }
        });

        actionbar.setCustomView(mCustomView);
        actionbar.setDisplayShowCustomEnabled(true);
    }
}
