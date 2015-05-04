package com.example.zem.patientcareapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
TextView signup, forgotpw;
    Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_login);

        signup = (TextView) findViewById(R.id.signup);
        forgotpw = (TextView) findViewById(R.id.forgot_password);
        login_btn = (Button) findViewById(R.id.login_btn);

        signup.setOnClickListener(this);
        forgotpw.setOnClickListener(this);
        login_btn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.login_btn:
//                Intent intent = new Intent(this, PatientHomeActivity.class);
                Intent intent = new Intent(this, HomeTileActivity.class);
                startActivity(intent);
                break;
            case R.id.signup:
                Intent intent2 = new Intent(this, SignUpActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
