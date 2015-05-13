package com.example.zem.patientcareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    TextView signup, forgotpw;
    Button login_btn;

    EditText username_txtfield, password_txtfield;

    DbHelper dbhelper;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    public static final String name = "nameKey";
    public static final String pass = "passwordKey";
    String uname, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_login);

        dbhelper = new DbHelper(this);

        signup = (TextView) findViewById(R.id.signup);
        forgotpw = (TextView) findViewById(R.id.forgot_password);
        login_btn = (Button) findViewById(R.id.login_btn);
        username_txtfield = (EditText) findViewById(R.id.username_txtfield);
        password_txtfield = (EditText) findViewById(R.id.password_txtfield);

        signup.setOnClickListener(this);
        forgotpw.setOnClickListener(this);
        login_btn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(name)) {
            if (sharedpreferences.contains(pass)) {
                Intent i = new Intent(this, HomeTileActivity.class);
                startActivity(i);
            }
        }
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                uname = username_txtfield.getText().toString();
                password = password_txtfield.getText().toString();

                if (uname.equals("")) {
                    username_txtfield.setError("Field Required");
                } else if (password.equals("")) {
                    password_txtfield.setError("Field Required");
                } else {
                    if (dbhelper.LoginUser(uname, password)) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(name, uname);
                        editor.putString(pass, password);
                        editor.commit();
                        Intent i = new Intent(this, HomeTileActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(this, "Username or Password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.signup:
                startActivity(new Intent(this, EditTabsActivity.class));
                break;
        }
    }
}
