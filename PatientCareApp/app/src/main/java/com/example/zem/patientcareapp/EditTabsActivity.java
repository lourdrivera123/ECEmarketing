package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.example.zem.patientcareapp.Fragment.AccountFragment;
import com.example.zem.patientcareapp.Fragment.ContactsFragment;
import com.example.zem.patientcareapp.Fragment.SignUpFragment;
import com.example.zem.patientcareapp.GetterSetter.Patient;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.PostRequest;
import com.example.zem.patientcareapp.Network.VolleySingleton;
import com.example.zem.patientcareapp.adapter.TabsPagerAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditTabsActivity extends FragmentActivity implements ActionBar.TabListener, View.OnClickListener, CalendarDatePickerDialogFragment.OnDateSetListener {

    public static Patient patient;
    RequestQueue queue;
    Patient editUser;
    DbHelper dbHelper;
    Helpers helpers;
    SignUpFragment fragment;
    ServerRequest serverRequest;

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private String[] tabs = {"Gen. Info", "Contact & Addr.", "Acct. Info"};
    public boolean hasError = true, hasError2 = true, hasError3 = true;

    // SIGN UP FRAGMENT
    EditText birthdate, fname, lname, mname, height, weight, occupation;
    RadioGroup sex;
    Spinner civil_status_spinner;
    String s_fname, s_lname, s_mname, s_birthdate, s_sex, s_civil_status, s_height, s_weight, s_occupation;

    // CONTACTS FRAGMENT
    EditText address_street, optional_address_line, tel_no, cell_no, email;
    Spinner address_region, address_barangay, address_city_municipality, address_province;
    String s_street, s_optional_address, s_email, s_tel_no, s_cell_no;

    // ACCOUNT INFO FRAGMENT
    String username = "", pass = "", s_filepath = "";
    ImageView image_holder;
    Drawable d;

    public static final String SIGNUP_REQUEST = "signup", EDIT_REQUEST = "edit";
    String purpose = "", image_url = "", url;
    public static int signup_int = 0, edit_int = 0;
    int check = 0, int_year, int_month, int_day, limit = 4, count = 0, unselected;
    long totalSize = 0;

    JSONObject patient_json_object_mysql = null;
    JSONArray patient_json_array_mysql = null;
    public static SharedPreferences sharedpreferences;

    private TextView txtPercentage;

    ProgressBar progressBar;
    ProgressDialog pDialog;
    Dialog upload_dialog;
    public static Intent intent;

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
        serverRequest = new ServerRequest();

        actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        MainActivity.setCustomActionBar(actionBar);
        showOverLay();

        sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);

        intent = getIntent();
        signup_int = intent.getIntExtra(SIGNUP_REQUEST, 0);
        edit_int = intent.getIntExtra(EDIT_REQUEST, 0);

        queue = VolleySingleton.getInstance().getRequestQueue();
        url = Constants.POST_URL;

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
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
                    if (unselected == 0)
                        readFromSignUp();
                    validateAtPosition2();

                } else if (position == 2) {
                    Button choose_image_btn = (Button) findViewById(R.id.choose_image_btn);
                    image_holder = (ImageView) findViewById(R.id.image_holder);
                    TableRow tbrow = (TableRow) findViewById(R.id.uploadpicturerow);

                    if (signup_int > 0) {
                        tbrow.setVisibility(View.GONE);
                    }

                    choose_image_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 111);
                        }
                    });

                    if (unselected == 1)
                        validateAtPosition2();
                    else if (unselected == 0)
                        readFromSignUp();

                    Button btn_submit = (Button) findViewById(R.id.btn_save);
                    btn_submit.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            validateUserAccountInfo();

                            if (hasError) {
                                viewPager.setCurrentItem(0);
                            } else if (hasError2) {
                                viewPager.setCurrentItem(1);
                            } else {
                                if (!hasError && !hasError2) {
                                    validateUserAccountInfo();
                                    if (!hasError3) {
                                        patient.setBarangay_id(Integer.parseInt(ContactsFragment.barangay_id));
                                        patient.setBarangay(ContactsFragment.address_barangay.getSelectedItem().toString());
                                        patient.setMunicipality(ContactsFragment.address_city_municipality.getSelectedItem().toString());
                                        patient.setProvince(ContactsFragment.address_province.getSelectedItem().toString());
                                        patient.setRegion(ContactsFragment.address_region.getSelectedItem().toString());

                                        if (edit_int > 0) {
                                            pDialog.show();
                                            editUser = dbHelper.getloginPatient(SidebarActivity.getUname());

                                            patient.setServerID(editUser.getServerID());
                                            HashMap<String, String> params = setParams("update");

                                            PostRequest.send(getBaseContext(), params, serverRequest, new RespondListener<JSONObject>() {
                                                @Override
                                                public void getResult(JSONObject response) {
                                                    int success = 0;

                                                    try {
                                                        success = response.getInt("success");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                    if (success == 1) {
                                                        if (dbHelper.savePatient(patient_json_object_mysql, patient, "update")) {
                                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                                            editor.putString(MainActivity.name, patient.getUsername());
                                                            editor.putString(MainActivity.pass, patient.getPassword());
                                                            editor.commit();

                                                            Toast.makeText(getBaseContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                                                            EditTabsActivity.this.finish();
                                                        } else
                                                            Toast.makeText(getBaseContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                                    } else
                                                        Toast.makeText(getBaseContext(), "Server Error Occurred", Toast.LENGTH_SHORT).show();
                                                    pDialog.dismiss();
                                                }
                                            }, new ErrorListener<VolleyError>() {
                                                public void getError(VolleyError error) {
                                                    pDialog.dismiss();
                                                    Toast.makeText(getBaseContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                                                    System.out.print("src <EditTabsActivity>: " + error);
                                                }
                                            });
                                        } else {
                                            pDialog.show();

                                            HashMap<String, String> params = setParams("register");

                                            PostRequest.send(getBaseContext(), params, serverRequest, new RespondListener<JSONObject>() {
                                                @Override
                                                public void getResult(JSONObject response) {
                                                    try {
                                                        int success = response.getInt("success");

                                                        if (success == 2) {
                                                            Toast.makeText(EditTabsActivity.this, "Username Already Registered", Toast.LENGTH_SHORT).show();
                                                        } else if (success == 1) {
                                                            patient_json_array_mysql = response.getJSONArray("patient");
                                                            patient_json_object_mysql = patient_json_array_mysql.getJSONObject(0);

                                                            if (dbHelper.savePatient(patient_json_object_mysql, patient, "insert")) {
                                                                SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                                                editor.putString(MainActivity.name, patient.getUsername());
                                                                editor.putString(MainActivity.pass, patient.getPassword());
                                                                editor.commit();

                                                                startActivity(new Intent(getBaseContext(), SidebarActivity.class));
                                                                EditTabsActivity.this.finish();
                                                            } else
                                                                Toast.makeText(EditTabsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (JSONException e) {
                                                        Toast.makeText(getBaseContext(), "Server error occurred", Toast.LENGTH_SHORT).show();
                                                        System.out.print("src <EditTabsActivity - catch>: " + e);
                                                    }
                                                    pDialog.dismiss();
                                                }
                                            }, new ErrorListener<VolleyError>() {
                                                public void getError(VolleyError error) {
                                                    pDialog.dismiss();
                                                    Toast.makeText(getBaseContext(), "Please check your internet connection putang ina", Toast.LENGTH_SHORT).show();
                                                    System.out.print("src <EditTabsActivity> NETWORK: " + error);
                                                }
                                            });
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

        if (signup_int > 0) {
            patient.setReferred_byUser(intent.getStringExtra("referred_by_User"));
            patient.setReferred_byDoctor(intent.getStringExtra("referred_by_Doctor"));
        }
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

    public HashMap<String, String> setParams(String request) {
        final HashMap<String, String> params = new HashMap();
        if (request.equals("update")) {
            if (AccountFragment.checkIfChangedPass > 0) {
                params.put("password", AccountFragment.NEW_PASS);
                patient.setPassword(AccountFragment.NEW_PASS);
            }
            params.put("id", String.valueOf(patient.getServerID()));
            params.put("request", "crud");
            params.put("action", "update");
            params.put("table", "patients");
        } else {
            params.put("request", "register");
            params.put("password", patient.getPassword());
            params.put("action", "insert");
            params.put("table", "patients");
            params.put("photo", patient.getPhoto());

            params.put("referred_byUser", patient.getReferred_byUser());
            params.put("referred_byDoctor", patient.getReferred_byDoctor());
        }
        params.put("fname", patient.getFname());
        params.put("lname", patient.getLname());
        params.put("mname", patient.getMname());
        params.put("username", patient.getUsername());
        params.put("occupation", patient.getOccupation());
        params.put("birthdate", patient.getBirthdate());
        params.put("sex", patient.getSex());
        params.put("civil_status", patient.getCivil_status());
        params.put("height", patient.getHeight());
        params.put("weight", patient.getWeight());
        params.put("optional_address", patient.getOptional_address());
        params.put("address_street", patient.getAddress_street());
        params.put("tel_no", patient.getTel_no());
        params.put("mobile_no", patient.getMobile_no());
        params.put("email_address", patient.getEmail());
        params.put("address_barangay_id", String.valueOf(ContactsFragment.hashOfBarangays.get(address_region.getSelectedItemPosition()).get("barangay_server_id")));

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

        Calendar calendar = Calendar.getInstance();
        int current_year = calendar.get(Calendar.YEAR);

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
        } else if ((current_year - int_year) < 18) {
            birthdate.setError("Must be 18 years old and above");
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

        if (count == limit)
            this.hasError = false;
        else
            this.hasError = true;

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
        address_street = (EditText) findViewById(R.id.address_street);
        optional_address_line = (EditText) findViewById(R.id.optional_address_line);
        email = (EditText) findViewById(R.id.email);
        tel_no = (EditText) findViewById(R.id.tel_no);
        cell_no = (EditText) findViewById(R.id.cell_no);
        address_region = (Spinner) findViewById(R.id.address_region);
        address_barangay = (Spinner) findViewById(R.id.address_barangay);
        address_city_municipality = (Spinner) findViewById(R.id.address_city_municipality);
        address_province = (Spinner) findViewById(R.id.address_province);
        s_street = address_street.getText().toString();

        s_email = email.getText().toString();
        s_tel_no = tel_no.getText().toString();
        s_cell_no = cell_no.getText().toString();
        s_optional_address = optional_address_line.getText().toString();

        count = 0;
        limit = 2;

        if (s_street.equals("")) {
            address_street.setError("Field Required");
        } else {
            patient.setAddress_street(s_street);
            count++;
        }

        if (s_cell_no.equals("")) {
            cell_no.setError("Field Required");
        } else {
            patient.setMobile_no(s_cell_no);
            count++;
        }

        if (count == limit)
            this.hasError2 = false;
        else
            this.hasError2 = true;

        patient.setTel_no(s_tel_no);
        patient.setEmail(s_email);
        patient.setOptional_address(s_optional_address);
    }

    public void validateUserAccountInfo() {
        EditText et_username = (EditText) findViewById(R.id.username);
        image_holder = (ImageView) findViewById(R.id.image_holder);

        username = et_username.getText().toString();

        count = 0;
        limit = 2;

        if (signup_int > 0) {
            EditText password = (EditText) findViewById(R.id.password);

            if (password.getText().toString().equals("")) {
                password.setError("Field is required");
            } else {
                pass = helpers.md5(password.getText().toString());
                patient.setPassword(pass);
                count++;
            }
        } else
            count++;

        if (username.equals("")) {
            et_username.setError("Field is required");
        } else {
            patient.setUsername(username);
            count++;
        }

        if (count == limit)
            this.hasError3 = false;
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
                    s_filepath = cursor.getString(columnIndex);
                    cursor.close();

                    if (edit_int > 0)
                        purpose = "profile_upload_update";
                    else
                        purpose = "profile_upload_insert";

                    showProgressbar();
                    new UploadFileToServer().execute();
                    check = 23;
                } else
                    patient.setPhoto("");
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.birthdate:
                FragmentManager fm = getSupportFragmentManager();
                Calendar now = Calendar.getInstance();
                CalendarDatePickerDialogFragment datepicker;

                if (s_birthdate.equals("")) {
                    birthdate.setError("Field Required");
                    datepicker = CalendarDatePickerDialogFragment
                            .newInstance(this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                } else {
                    int month, year, day;
                    String birth = birthdate.getText().toString();
                    int indexOfYear = birth.indexOf("-");
                    int indexOfMonthandDay = birth.lastIndexOf("-");
                    year = Integer.parseInt(birth.substring(0, indexOfYear));
                    month = Integer.parseInt(birth.substring(indexOfYear + 1, indexOfMonthandDay)) - 1;
                    day = Integer.parseInt(birth.substring(indexOfMonthandDay + 1, birth.length()));
                    count++;

                    datepicker = CalendarDatePickerDialogFragment
                            .newInstance(this, year, month, day);
                }
                datepicker.show(fm, "fragment_date_picker_name");

                break;
        }
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        String dateStr = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        birthdate.setText(dateStr);
        s_birthdate = dateStr;

        int_year = year;
        int_month = monthOfYear;
        int_day = dayOfMonth;

        birthdate.setError(null);
        patient.setBirthdate(dateStr);

        Calendar calendar = Calendar.getInstance();
        int current_year = calendar.get(Calendar.YEAR);

        if ((current_year - year) < 18)
            birthdate.setError("Must be 18 years old and above");
        else
            birthdate.setError(null);
    }

    public void showProgressbar() {
        upload_dialog = new Dialog(this);
        upload_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        upload_dialog.setContentView(R.layout.activity_upload);

        txtPercentage = (TextView) upload_dialog.findViewById(R.id.txtPercentage);
        progressBar = (ProgressBar) upload_dialog.findViewById(R.id.progressBar);
        upload_dialog.show();
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(progress[0]);
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString;

            int patientID = dbHelper.getCurrentLoggedInPatient().getServerID();

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Constants.FILE_UPLOAD_URL + "?patient_id=" + patientID);

            try {
                AndroidMultipartEntity entity = new AndroidMultipartEntity(
                        new AndroidMultipartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(s_filepath);

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));
                entity.addPart("purpose", new StringBody(purpose));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responseString = EntityUtils.toString(r_entity); // Server response
                } else {
                    responseString = "Error occurred! Http Status Code: " + statusCode;
                }
            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject jObject;

            try {
                jObject = new JSONObject(result);
                image_url = jObject.getString("file_name");
                patient.setPhoto(image_url);

                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);

                if (dbHelper.updatePatientImage(image_url, SidebarActivity.getUserID()))
                    Log.d("updated photo", "true");
                else
                    Log.d("updated photo", "false");

                helpers.setImage(image_url, progressBar, image_holder);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            upload_dialog.dismiss();
            super.onPostExecute(result);
        }
    }

    private void showOverLay() {
        if (dbHelper.checkOverlay("EditTabs", "check")) {

        } else {
            final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
            dialog.setContentView(R.layout.edittabs_overlay);

            LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.editTabsLayout);
            layout.setAlpha((float) 0.8);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (dbHelper.checkOverlay("EditTabs", "insert"))
                        dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}