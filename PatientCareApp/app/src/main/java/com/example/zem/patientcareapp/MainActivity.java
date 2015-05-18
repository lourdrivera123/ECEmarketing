package com.example.zem.patientcareapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
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
    public static Activity main;

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
        main = this;

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
                showNotification();
                Intent i = new Intent(this, HomeTileActivity.class);
                startActivity(i);
            }
        }
        username_txtfield.setText("");
        password_txtfield.setText("");
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        HomeTileActivity.hometile.finish();
        super.onBackPressed();
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
                        showNotification();
                        Intent i = new Intent(this, HomeTileActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(this, "Username or Password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.signup:
                int signup = 23;

                Intent intent = new Intent(this, EditTabsActivity.class);
                intent.putExtra(EditTabsActivity.SIGNUP_REQUEST, signup);
                startActivity(intent);
                break;
        }
    }

    public void showNotification(){
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("It's your Birthday!")
                        .setSound(uri)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentText("I hope, it's your last. ;)");

        Intent resultIntent = new Intent(this, MainActivity.class);

        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
