package com.example.zem.patientcareapp;

/**
 * Created by Dexter B. on 7/20/2015.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class AlarmReceiver extends BroadcastReceiver
{
    Helpers helpers;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // TODO Auto-generated method stub

        // here you can start an activity or service depending on your need
        // for ex you can start an activity to vibrate phone or to ring the phone

        helpers = new Helpers();

        /*String phoneNumberReciver="9718202185";// phone number to which SMS to be send
        String message="Hi I will be there later, See You soon";// message to send
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumberReciver, null, message, null, null);*/
        // Show the toast  like in above screen shot
//        Bundle map = intent.getExtras();

        Bundle bundle = intent.getExtras();
        System.out.println("Bundle bundle on AlarmReceiver: "+bundle.toString());



        String title = "", message = "";
        int id = 001;
        if( bundle != null ) {
            HashMap<String, String> map;
            map = (HashMap<String, String>) bundle.getSerializable("consultation");

            System.out.println("Serializable map on AlarmReceiver: "+map.toString());

            String doctor = map.get("doctor"), clinic = map.get("clinic"), am_pm = map.get("partOfDay");
            Toast.makeText(context, "Alarm Triggered and SMS Sent", Toast.LENGTH_LONG).show();
            System.out.println("ALARM!!!!!!!<source: AlarmReceiver.java>");

            title = "Consultation Reminder...";
            message = "You have a consultation today with Dr."+doctor+"\n at "+clinic+" this "+am_pm;
            id = Integer.parseInt(map.get("id"));
        }

        Intent resultIntent = new Intent(context, MainActivity.class);

        helpers.showNotification(context, resultIntent, id, title, message, false);
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);

        try {
            MediaPlayer mMediaPlayer = new MediaPlayer();
            Uri alert =  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);

                final MediaPlayer finalMMediaPlayer = mMediaPlayer;
                CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        try{
                            finalMMediaPlayer.setLooping(true);
                            finalMMediaPlayer.prepare();
                            finalMMediaPlayer.start();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                    public void onFinish() {
                        //code fire after finish
                        finalMMediaPlayer.stop();
                    }
                };
                countDownTimer.start();

            }
        } catch(Exception e) {
        }


    }

}