package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.zem.patientcareapp.Fragment.SignUpFragment;
import com.example.zem.patientcareapp.GetterSetter.Patient;
import com.example.zem.patientcareapp.adapter.TabsPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditTabsActivity extends FragmentActivity implements ActionBar.TabListener, DatePickerDialog.OnDateSetListener, View.OnClickListener {

    int limit = 4, count = 0, unselected;

    public static Patient patient;
    DbHelper dbHelper;
    Helpers helpers;
    SignUpFragment fragment;

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private String[] tabs = {"Gen. Info", "Contact Info", "Acct. Info"};
    public boolean hasError = true, hasError2 = true, hasError3 = true;

    // SIGN UP FRAGMENT
    EditText birthdate, fname, lname, mname, height, weight, occupation;
    RadioGroup sex;
    Spinner civil_status_spinner;
    String s_fname, s_lname, s_mname, s_birthdate, s_sex, s_civil_status, s_height, s_weight, s_occupation;

    // CONTACTS FRAGMENT
    EditText unit_no, building, lot_no, block_no, phase_no, address_house_no, address_street, address_barangay, address_city_municipality, address_province, address_zip,
            email, tel_no, cell_no;
    Spinner address_region;
    String s_unit_no, s_building, s_lot_no, s_block_no, s_phase_no, s_house_no, s_street, s_barangay, s_city, s_province, s_zip, s_email, s_tel_no, s_cell_no, s_region;

    // ACCOUNT INFO FRAGMENT
    String username = "";
    String s_password = "";
    String s_filepath = "";
    ImageView image_holder;
    Drawable d;

    int check = 0;
    int int_year, int_month, int_day;

    public static final String SIGNUP_REQUEST = "signup";
    public static final String EDIT_REQUEST = "edit";
    public static int signup_int = 0;
    public static int edit_int = 0;

    Patient editUser;

    RequestQueue queue;
    String url;
    ProgressDialog pDialog;

    JSONObject patient_json_object_mysql = null;
    JSONArray patient_json_array_mysql = null;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.edit_profile_layout);

        dbHelper = new DbHelper(this);
        helpers = new Helpers();
        patient = new Patient();
        fragment = new SignUpFragment();

        ActionBar actionbar = getActionBar();
        MainActivity.setCustomActionBar(actionbar);

        Intent intent = getIntent();
        signup_int = intent.getIntExtra(SIGNUP_REQUEST, 0);
        edit_int = intent.getIntExtra(EDIT_REQUEST, 0);

        queue = Volley.newRequestQueue(this);
        url = "http://vinzry.0fees.us/db/post.php";

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();

        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
                if (position == 0) {
                    readFromSignUp();
                } else if (position == 1) {
                    if (unselected == 0) {
                        readFromSignUp();
                    }
                    validateAtPosition2();
                } else if (position == 2) {
                    Button choose_image_btn = (Button) findViewById(R.id.choose_image_btn);
                    image_holder = (ImageView) findViewById(R.id.image_holder);

                    choose_image_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 111);
                        }
                    });

                    if (unselected == 1) {
                        validateAtPosition2();
                        setProfilePhoto();
                    } else if (unselected == 0) {
                        readFromSignUp();
                        setProfilePhoto();
                    }

                    Button btn_submit = (Button) findViewById(R.id.btn_save);
                    btn_submit.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            validateUserAccountInfo();
                            //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
                            if (hasError) {
                                viewPager.setCurrentItem(0);
                            } else if (hasError2) {
                                viewPager.setCurrentItem(1);
                            } else {
                                if (!hasError && !hasError2) {
                                    validateUserAccountInfo();
                                    if (!hasError3) {
                                        if (edit_int > 0) {
                                            String edit_uname = SidebarActivity.getUname();
                                            editUser = dbHelper.getloginPatient(edit_uname);
                                            int userID = editUser.getId();
                                            String chosenPhoto = patient.getPhoto();
                                            String defaultPhoto = editUser.getPhoto();
                                            String photo = "";

                                            if (chosenPhoto.equals("")) {
                                                photo = editUser.getPhoto();
                                            } else {
                                                photo = patient.getPhoto();
                                            }

                                            if (defaultPhoto.equals("")) {
                                                photo = patient.getPhoto();
                                            } else {
                                                photo = editUser.getPhoto();
                                            }

                                            if (dbHelper.updatePatient(patient, userID, photo)) {
                                                Toast.makeText(getBaseContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getBaseContext(), MasterTabActivity.class);
                                                intent.putExtra("selected", 0);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(getBaseContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            pDialog.setMessage("Remember: Patience is a Virtue. So please wait while we save your information");
                                            pDialog.show();
                                            if (helpers.isNetworkAvailable(getBaseContext())) {

                                                Map<String, String> params = setParams();

                                                CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, params,
                                                        new Response.Listener<JSONObject>() {
                                                            @Override
                                                            public void onResponse(JSONObject response) {
                                                                System.out.println("response is: " + response);

                                                                try {
                                                                    int success = response.getInt("success");
                                                                    System.out.println("success is: " + success);
                                                                    if (success == 2) {
                                                                        pDialog.hide();
                                                                        Toast.makeText(EditTabsActivity.this, "Username Already Registered", Toast.LENGTH_SHORT).show();
                                                                    } else if (success == 1) {
                                                                        patient_json_array_mysql = response.getJSONArray("patient");
                                                                        patient_json_object_mysql = patient_json_array_mysql.getJSONObject(0);
                                                                        Log.d("response jsobjrequest", "" + response.toString());

                                                                        //saving to sqlite database
                                                                        if (dbHelper.insertPatient(patient_json_object_mysql, patient)) {
                                                                            SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                                                            editor.putString("nameKey", patient.getUsername());
                                                                            editor.commit();
                                                                            pDialog.hide();
                                                                            helpers.showNotification(getBaseContext());
                                                                            startActivity(new Intent(getBaseContext(), SidebarActivity.class));
                                                                        } else {
                                                                            Toast.makeText(EditTabsActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    } else {
                                                                        Toast.makeText(EditTabsActivity.this, "Error occcurred. Please try again later", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } catch (JSONException e) {
                                                                }
                                                            }
                                                        }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        pDialog.dismiss();
                                                        Log.d("volley error ", "" + error.toString());
                                                    }
                                                });
                                                queue.add(jsObjRequest);
                                            } else {
                                                Toast.makeText(EditTabsActivity.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
       public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.close_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        int pos = tab.getPosition();
        viewPager.setCurrentItem(pos);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        unselected = tab.getPosition();
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    public Map<String, String> setParams() {
        Map<String, String> params = new HashMap();

        params.put("request", "register");
        params.put("fname", patient.getFname());
        params.put("lname", patient.getLname());
        params.put("mname", patient.getMname());
        params.put("username", patient.getUsername());
        params.put("password", patient.getPassword());
        params.put("occupation", patient.getOccupation());
        params.put("birthdate", patient.getBirthdate());
        params.put("sex", patient.getSex());
        params.put("civil_status", patient.getCivil_status());
        params.put("height", patient.getHeight());
        params.put("weight", patient.getWeight());
        params.put("unit_floor_room_no", "" + patient.getUnit_floor_room_no());
        params.put("building", patient.getBuilding());
        params.put("lot_no", "" + patient.getLot_no());
        params.put("block_no", "" + patient.getBlock_no());
        params.put("phase_no", "" + patient.getPhase_no());
        params.put("address_house_no", "" + patient.getAddress_house_no());
        params.put("address_street", patient.getAddress_street());
        params.put("address_barangay", patient.getAddress_barangay());
        params.put("address_city_municipality", patient.getAddress_city_municipality());
        params.put("address_province", patient.getAddress_province());
        params.put("address_region", patient.getAddress_region());
        params.put("address_zip", patient.getAddress_zip());
        params.put("tel_no", patient.getTel_no());
        params.put("cell_no", patient.getMobile_no());
        params.put("email", patient.getEmail());
        params.put("photo", patient.getPhoto());

        return params;
    }

    public void readFromSignUp() {
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        mname = (EditText) findViewById(R.id.mname);
        height = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);
        occupation = (EditText) findViewById(R.id.occupation);
        civil_status_spinner = (Spinner) findViewById(R.id.civil_status);
        sex = (RadioGroup) findViewById(R.id.sex);
        int selectedId = sex.getCheckedRadioButtonId();

        s_fname = fname.getText().toString();
        s_lname = lname.getText().toString();
        s_mname = mname.getText().toString();
        s_height = height.getText().toString();
        s_weight = weight.getText().toString();
        s_occupation = occupation.getText().toString();
        s_civil_status = civil_status_spinner.getSelectedItem().toString();
        s_sex = selectedId == R.id.male_rb ? "Male" : "Female";

        birthdate = (EditText) findViewById(R.id.birthdate);
        s_birthdate = birthdate.getText().toString();
        birthdate.setOnClickListener(this);

        limit = 5;
        count = 0;

        if (s_fname.equals("")) {
            fname.setError("Field Required");
        } else {
            patient.setFname(s_fname);
            count++;
        }

        if (s_lname.equals("")) {
            lname.setError("Field Required");
        } else {
            patient.setLname(s_lname);
            count++;
        }

        if (s_birthdate.equals("")) {
            birthdate.setError("Field Required");
        } else {
            patient.setBirthdate(s_birthdate);
            count++;
        }

        if (s_height.equals("")) {
            height.setError("Field Required");
        } else {
            patient.setHeight(s_height);
            count++;
        }

        if (s_weight.equals("")) {
            weight.setError("Field Required");
        } else {
            patient.setWeight(s_weight);
            count++;
        }

        if (count == limit) {
            this.hasError = false;
        } else {
            this.hasError = true;
        }

        patient.setSex(s_sex);

        //NOT REQUIRED VARIABLES
        if (s_mname.equals(""))
            s_mname = "";

        if (s_occupation.equals(""))
            s_occupation = "";

        patient.setMname(s_mname);
        patient.setOccupation(s_occupation);
        patient.setCivil_status(s_civil_status);
    }

    public void validateAtPosition2() {
        unit_no = (EditText) findViewById(R.id.unit_no);
        building = (EditText) findViewById(R.id.building);
        lot_no = (EditText) findViewById(R.id.lot_no);
        block_no = (EditText) findViewById(R.id.block_no);
        phase_no = (EditText) findViewById(R.id.phase_no);
        address_house_no = (EditText) findViewById(R.id.address_house_no);
        address_street = (EditText) findViewById(R.id.address_street);
        address_barangay = (EditText) findViewById(R.id.address_barangay);
        address_city_municipality = (EditText) findViewById(R.id.address_city_municipality);
        address_province = (EditText) findViewById(R.id.address_province);
        address_zip = (EditText) findViewById(R.id.address_zip);
        email = (EditText) findViewById(R.id.email);
        tel_no = (EditText) findViewById(R.id.tel_no);
        cell_no = (EditText) findViewById(R.id.cell_no);
        address_region = (Spinner) findViewById(R.id.address_region);

        s_house_no = address_house_no.getText().toString();
        s_unit_no = unit_no.getText().toString();
        s_building = building.getText().toString();
        s_lot_no = lot_no.getText().toString();
        s_block_no = block_no.getText().toString();
        s_phase_no = phase_no.getText().toString();
        s_street = address_street.getText().toString();
        s_barangay = address_barangay.getText().toString();
        s_city = address_city_municipality.getText().toString();
        s_province = address_province.getText().toString();
        s_zip = address_zip.getText().toString();
        s_email = email.getText().toString();
        s_tel_no = tel_no.getText().toString();
        s_cell_no = cell_no.getText().toString();
        s_region = address_region.getSelectedItem().toString();

        count = 0;
        limit = 6;

        if (s_street.equals("")) {
            address_street.setError("Field Required");
        } else {
            patient.setAddress_street(s_street);
            count++;
        }

        if (s_barangay.equals("")) {
            address_barangay.setError("Field Required");
        } else {
            patient.setAddress_barangay(s_barangay);
            count++;
        }

        if (s_city.equals("")) {
            address_city_municipality.setError("Field Required");
        } else {
            patient.setAddress_city_municipality(s_city);
            count++;
        }

        if (s_province.equals("")) {
            address_province.setError("Field Required");
        } else {
            patient.setAddress_province(s_province);
            count++;
        }

        if (s_zip.equals("")) {
            address_zip.setError("Field Required");
        } else {
            patient.setAddress_zip(s_zip);
            count++;
        }


        if (s_cell_no.equals("")) {
            cell_no.setError("Field Required");
        } else {
            patient.setMobile_no(s_cell_no);
            count++;
        }

        if (count == limit) {
            this.hasError2 = false;
        } else {
            this.hasError2 = true;
        }

        //NOT REQUIRED VARIABLES
        if (s_unit_no.equals(""))
            s_unit_no = "0";

        if (s_lot_no.equals(""))
            s_lot_no = "0";

        if (s_block_no.equals(""))
            s_block_no = "0";

        if (s_phase_no.equals(""))
            s_phase_no = "0";

        if (s_house_no.equals(""))
            s_house_no = "0";

        if (s_tel_no.equals(""))
            s_tel_no = "";

        if (s_building.equals(""))
            s_building = "";

        if (s_email.equals(""))
            s_email = "";

        patient.setUnit_floor_room_no(Integer.parseInt(s_unit_no));
        patient.setLot_no(Integer.parseInt(s_lot_no));
        patient.setBlock_no(Integer.parseInt(s_block_no));
        patient.setPhase_no(Integer.parseInt(s_phase_no));
        patient.setAddress_house_no(Integer.parseInt(s_house_no));
        patient.setTel_no(s_tel_no);
        patient.setBuilding(s_building);
        patient.setEmail(s_email);
        patient.setAddress_region(s_region);
    }

    public void validateUserAccountInfo() {
        EditText et_username = (EditText) findViewById(R.id.username);
        EditText et_partial_password = (EditText) findViewById(R.id.password);
        EditText et_confirmed_password = (EditText) findViewById(R.id.confirm_password);
        image_holder = (ImageView) findViewById(R.id.image_holder);

        username = et_username.getText().toString();
        String partial_pword = et_partial_password.getText().toString(), confirmed_pword = et_confirmed_password.getText().toString();

        count = 0;
        limit = 4;

        if (username.equals("")) {
            et_username.setError("Field is required");
        } else {
            patient.setUsername(username);
            count++;
        }

        if (partial_pword.equals("")) {
            et_partial_password.setError("Field is required");
        } else {
            count++;
        }

        if (et_confirmed_password.equals("")) {
            et_confirmed_password.setError("Field is required");
        } else {
            count++;
        }


        if (partial_pword.equals(confirmed_pword)) {
            patient.setPassword(confirmed_pword);
            s_password = confirmed_pword;
            count++;
        } else {
            et_confirmed_password.setError("Passwords do not match. Please try again.");
        }

        if (count == limit) {
            this.hasError3 = false;
        }
    }

    public void setProfilePhoto() {
        String path = patient.getPhoto();

        if (check > 0) { //IF RETURNED FROM ON ACTIVITY RESULT
            Bitmap yourSelectedImage = BitmapFactory.decodeFile(path);
            d = new BitmapDrawable(yourSelectedImage);
            image_holder.setImageDrawable(d);
        } else {
            image_holder = (ImageView) findViewById(R.id.image_holder);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            try {
                if (data.getData() != null && !data.getData().equals(Uri.EMPTY)) {
                    Uri uri = data.getData();
                    String[] projection = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String filePath = cursor.getString(columnIndex);
                    s_filepath = filePath;
                    cursor.close();

                    patient.setPhoto(filePath);

                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                    d = new BitmapDrawable(yourSelectedImage);
                    image_holder.setImageDrawable(d);

                    check = 23;

                } else {
                    patient.setPhoto("");
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onClick(View v) {
        Calendar cal = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.birthdate:
                if (s_birthdate.equals("")) {
                    birthdate.setError("Field Required");
                    DatePickerDialog datePicker = new DatePickerDialog(EditTabsActivity.this, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                    datePicker.show();
                } else {
                    if (int_year > 0 && int_month > 0 && int_day > 0) {
                        updateDate(int_year, int_month, int_day);
                    } else {
                        int_year = fragment.int_year;
                        int_month = fragment.int_month;
                        int_day = fragment.int_day;
                        updateDate(int_year, int_month, int_day);
                    }
                    patient.setBirthdate(s_birthdate);
                    count++;
                }
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String dateStr = String.format("%d/%d/%d", (monthOfYear + 1), dayOfMonth, year);
        birthdate.setText(dateStr);
        birthdate.setError(null);
        patient.setBirthdate(dateStr);
        int_year = year;
        int_month = monthOfYear;
        int_day = dayOfMonth;
    }

    public void updateDate(int year, int monthOfYear, int dayOfMonth) {
        DatePickerDialog datePicker = new DatePickerDialog(EditTabsActivity.this, this, year, monthOfYear, dayOfMonth);
        datePicker.show();
    }
}