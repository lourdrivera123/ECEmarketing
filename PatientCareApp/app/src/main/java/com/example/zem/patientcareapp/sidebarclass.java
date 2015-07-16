//package com.example.zem.patientcareapp;
//
///**
// * Created by Zem on 7/7/2015.
// */
//public class sidebarclass {
//}
//
//
//package com.mycps;
//
//        import java.util.ArrayList;
//        import android.app.Activity;
//        import android.app.Fragment;
//        import android.content.Intent;
//        import android.content.res.Configuration;
//        import android.os.Bundle;
//        import android.support.v4.app.ActionBarDrawerToggle;
//        import android.support.v4.widget.DrawerLayout;
//        import android.view.ContextMenu;
//        import android.view.LayoutInflater;
//        import android.view.Menu;
//        import android.view.MenuItem;
//        import android.view.View;
//        import android.view.ViewGroup;
//        import android.view.ContextMenu.ContextMenuInfo;
//        import android.widget.AdapterView;
//        import android.widget.AdapterView.OnItemClickListener;
//        import android.widget.ArrayAdapter;
//        import android.widget.ListView;
//        import android.widget.AdapterView.AdapterContextMenuInfo;
//
//public class MainActivity extends Activity implements OnItemClickListener {
//
//    DrawerLayout sidebar_drawer_layout;
//    ListView sidebar_listview;
//    ActionBarDrawerToggle mDrawerToggle;
//    ArrayList<String> array_of_courses;
//
//    DbHelper dbhelper;
//    String courseperiod;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.sidebar_layout);
//        dbhelper = new DbHelper(this);
//
//        sidebar_drawer_layout = (DrawerLayout) findViewById(R.id.sidebar_drawer_layout);
//        sidebar_listview = (ListView) findViewById(R.id.sidebar_listview);
//        sidebar_listview.setOnItemClickListener(this);
//        // sidebar_list =
//        // getResources().getStringArray(R.array.sidebar_list_array);
//
//        array_of_courses = dbhelper.getAllCourses();
//        sidebar_listview.setAdapter(new ArrayAdapter<String>(this,
//                R.layout.sidebar_item, array_of_courses));
//
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);
//
//        sidebar_listview.setOnCreateContextMenuListener(this);
//
//        mDrawerToggle = new ActionBarDrawerToggle(this, sidebar_drawer_layout, R.drawable.ic_drawer, R.string.sidebar_open, R.string.sidebar_close) {
//            public void onDrawerClosed(View view) {
//                // getActionBar().setTitle("myCPS");
//                invalidateOptionsMenu();
//            }
//
//            public void onDrawerOpened(View drawerView) {
//                // getActionBar().setTitle("myCPS");
//                invalidateOptionsMenu();
//            }
//        };
//
//        sidebar_drawer_layout.setDrawerListener(mDrawerToggle);
//
//        Fragment fragment = new HomeFragment();
//        android.app.FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit();
//
//    }
//
//    @Override
//    protected void onResume() {
//        array_of_courses = dbhelper.getAllCourses();
//        sidebar_listview.setAdapter(new ArrayAdapter<String>(this, R.layout.sidebar_item, array_of_courses));
//
//        super.onResume();
//    }
//
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        getMenuInflater().inflate(R.menu.period, menu);
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterContextMenuInfo menuinfo = (AdapterContextMenuInfo) item.getMenuInfo();
//        String selected = (String) sidebar_listview.getItemAtPosition(menuinfo.position);
//
//        switch(item.getItemId()){
//            case R.id.prelim:
//                courseperiod = "PRELIM";
//                break;
//
//            case R.id.midterm:
//                courseperiod = "MIDTERM";
//                break;
//
//            case R.id.prefi:
//                courseperiod = "PREFINALS";
//                break;
//
//            case R.id.finals:
//                courseperiod = "FINALS";
//                break;
//        }
//        selectedsomethingItem(selected);
//        return super.onContextItemSelected(item);
//    }
//
//    private void selectedsomethingItem(String course){
//        Intent intent_for_select_item = new Intent(this, TableActivity.class);
//        intent_for_select_item.putExtra(TableActivity.COURSE, course);
//        intent_for_select_item.putExtra(TableActivity.PERIOD, courseperiod);
//        startActivity(intent_for_select_item);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.user, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        return super.onPrepareOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//
//        if (id == R.id.add_course) {
//            Intent intent = new Intent(this, CoursesActivity.class);
//            startActivity(intent);
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onPostCreate(savedInstanceState);
//        mDrawerToggle.syncState();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        // TODO Auto-generated method stub
//        super.onConfigurationChanged(newConfig);
//
//        mDrawerToggle.onConfigurationChanged(newConfig);
//    }
//
//    public class HomeFragment extends Fragment {
//        public static final int ARG_PLANET_NUMBER = 0;
//        public HomeFragment() {
//
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            // TODO Auto-generated method stub
//            // return super.onCreateView(inflater, container,
//            // savedInstanceState);
//            return inflater.inflate(R.layout.activity_main, container, false);
//        }
//
//        @Override
//        public void onActivityCreated(Bundle savedInstanceState) {
//            // TODO Auto-generated method stub
//            super.onActivityCreated(savedInstanceState);
//            setHasOptionsMenu(true);
//        }
//
//        public void goToCourses() {
//            Intent intent = new Intent(getActivity().getBaseContext(), CoursesActivity.class);
//            startActivity(intent);
//        }
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position,
//                            long id) {
//        Intent intent = new Intent(this, MainActivityTabs.class);
//        startActivity(intent);
//
//    }
//}
